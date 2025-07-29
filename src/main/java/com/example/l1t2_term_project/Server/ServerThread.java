package com.example.l1t2_term_project.Server;

import com.example.l1t2_term_project.DTO.BuyPlayerDTO;
import com.example.l1t2_term_project.DTO.LoginDTO;
import com.example.l1t2_term_project.DTO.SellPlayerDTO;
import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Model.Offer;
import com.example.l1t2_term_project.Model.Player.Player;
import com.example.l1t2_term_project.Model.Player.PlayerCollection;
import com.example.l1t2_term_project.Model.Player.PlayerFilter;
import com.example.l1t2_term_project.Utils.ActivityLogger;
import com.example.l1t2_term_project.Utils.SocketWrapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ServerThread extends Thread {
    private final SocketWrapper socketWrapper;
    private final Server server;
    private String clubName;

    public ServerThread(SocketWrapper socketWrapper, Server server) {
        ActivityLogger.log("Client connected: " + socketWrapper.getSocket());
        this.socketWrapper = socketWrapper;
        this.server = server;
    }

    public void run() {
        write(PlayerCollection.getAllNationalities()); // Export nations
        write(PlayerCollection.getAllTeams()); // Export clubs

        try {
            while (true) {
                Object obj = socketWrapper.read();
                if (obj instanceof LoginDTO)
                {
                    LoginDTO loginDTO = (LoginDTO) obj;
                    if (loginDTO.getType() == LoginDTO.Type.SignIn) validateLogin(loginDTO);

                    else if (loginDTO.getType() == LoginDTO.Type.SignOut) handleSignOut(loginDTO);

                    else if (loginDTO.getType() == LoginDTO.Type.ChangePass) write(server.changePass(loginDTO));
                }
                else if (obj instanceof String)
                {
                    write(server.getClub(clubName)); // Export logged in club
                }
                else if (obj instanceof PlayerFilter)
                {
                    List<Player> players = PlayerCollection.getFilteredPlayers((PlayerFilter) obj);
                    write(players); // Export filtered players
                }
                else if (obj instanceof BuyPlayerDTO)
                {
                    handleTransfer((BuyPlayerDTO) obj);
                }
                else if (obj instanceof SellPlayerDTO)
                {
                    listForSale((SellPlayerDTO) obj);
                }
                else if (obj instanceof Offer)
                {
                    handleOffer((Offer) obj);
                }
                else if (obj instanceof Player) {
                    addNewPlayer((Player) obj);
                }
                else {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            ActivityLogger.log("Client disconnected: " + socketWrapper.getSocket());
        } finally {
            try {
                socketWrapper.closeConnection();
            } catch (IOException e) {
                ActivityLogger.log("Cannot close connection with " + socketWrapper.getSocket());
            }
        }
    }

    public Object read()
    {
        try{
            return socketWrapper.read();
        } catch (IOException | ClassNotFoundException e) {
            ActivityLogger.log("Server unable to read");
            return null;
        }
    }

    public void write(Object obj)
    {
        try{
            socketWrapper.write(obj);
        } catch (IOException e) {
            ActivityLogger.log("Server unable to write");
        }
    }

    public void validateLogin(LoginDTO loginDTO)
    {
        if (server.validateCredentials(loginDTO))
        {
            synchronized (server) {
                clubName = loginDTO.getUsername();
                ActivityLogger.log("'" + clubName + "' logged in");
                server.addClient(clubName, socketWrapper);
                write(true); // Confirm
            }
        }
        else
        {
            ActivityLogger.log("Failed login attempted - " + loginDTO);
            write(false);
        }
    }

    public void handleSignOut(LoginDTO loginDTO)
    {
        String username = loginDTO.getUsername();
        server.removeClient(username);
        ActivityLogger.log("'" + username + "' logged out");
    }

    public void handleTransfer(BuyPlayerDTO buyPlayerDTO)
    {
        synchronized (server) {
            Player player = PlayerCollection.getPlayer(buyPlayerDTO.getPlayerId());
            assert player != null;
            if (!player.getTeam().equalsIgnoreCase(buyPlayerDTO.getCurrentClub()) || !player.isForSale()) {
                ActivityLogger.log("Player (ID: " + player.getId() + ") information mismatch");
                write(false);
                return;
            }

            Club buyer = server.getClub(buyPlayerDTO.getNewClub());
            Club seller = server.getClub(player.getTeam());
            if (buyer.canBuy(player.getValue())) {
                buyer.changeBudget(-player.getValue());
                if (seller != null) seller.changeBudget(player.getValue());

                player.setForSale(false);
                player.setWeeklySalary(buyPlayerDTO.getNewSalary());
                player.setTeam(buyer.getName());

                PlayerCollection.writeToFile();
                server.writeClubsToFile();
                ActivityLogger.log("Player (ID: " + player.getId() + ") transferred to '" + buyer.getName() + "'");
                write(true);
            } else {
                ActivityLogger.log("Transfer failed: '" + buyer.getName() + "' cannot afford Player (ID: " + player.getId() + ")");
                write(false);
            }
        }
    }

    public void listForSale(SellPlayerDTO sellPlayerDTO)
    {
        synchronized (server) {
            Player player = PlayerCollection.getPlayer(sellPlayerDTO.getPlayerId());
            assert player != null;
            if (player.getTeam().equalsIgnoreCase(sellPlayerDTO.getCurrentClub())) {
                player.setForSale(true);
                player.setValue(sellPlayerDTO.getNewValue());

                PlayerCollection.writeToFile();
                ActivityLogger.log("Player (ID: " + player.getId() + ") listed for sale");
                write(true);
            } else {
                ActivityLogger.log("Player (ID: " + player.getId() + ") information mismatch");
                write(false);
            }
        }
    }

    public void handleOffer(Offer offer)
    {
        switch (offer.getStatus())
        {
            case Make:
                makeOffer(offer);
                break;
            case Accept:
                acceptOffer(offer);
                break;
            case Reject:
                rejectOffer(offer);
                break;
            case GetList:
                exportOffers();
                break;
        }
    }

    public void exportOffers()
    {
        List<Offer> offers = server.getOffers().stream().filter(o -> o.getToClub().equalsIgnoreCase(clubName.trim())).collect(Collectors.toList());
        write(offers); // Incoming offers
        offers = server.getOffers().stream().filter(o -> o.getFromClub().equalsIgnoreCase(clubName.trim())).collect(Collectors.toList());
        write(offers); // Outgoing offers
    }

    // TODO: Method not tested yet
    public boolean validateOffer(Offer offer)
    {
        boolean valid;
        valid = (offer.getFromClubPlayer() != null) || (offer.getToClubPlayer() != null);
        valid = valid && (offer.getFromClubPlayer() != null || offer.getAmount() > 0);
        valid = valid && (offer.getToClubPlayer() != null || offer.getAmount() < 0);
        valid = valid && server.getClub(offer.getFromClub()).getBudget() >= offer.getAmount();

        if (offer.getFromClubPlayer() != null)
        {
            Player player = PlayerCollection.getPlayerByName(offer.getFromClubPlayer());
            assert player != null;
            valid = valid && player.getTeam().equalsIgnoreCase(offer.getFromClub());
        }
        if (offer.getToClubPlayer() != null)
        {
            Player player = PlayerCollection.getPlayerByName(offer.getToClubPlayer());
            assert player != null;
            valid = valid && (player.getTeam().equalsIgnoreCase(offer.getToClub()));
        }

        return valid;
    }

    public void makeOffer(Offer offer)
    {
        synchronized (server)
        {
            boolean valid = validateOffer(offer);
            if (valid)
            {
                server.removeOffer(offer.getId());
                server.addOffer(offer);
                ActivityLogger.log("'" + offer.getFromClub() + "' made offer to '" + offer.getToClub() + "'");
            }
            write(valid);
        }
    }

    // TODO: Method not tested yet
    public void acceptOffer(Offer offer)
    {
        synchronized (server)
        {
            boolean valid = server.getOffers().contains(offer) && validateOffer(offer);
            valid = valid && server.getClub(offer.getToClub()).getBudget() >= (-offer.getAmount());
            if (valid)
            {
                server.getClub(offer.getFromClub()).changeBudget(-offer.getAmount());
                server.getClub(offer.getToClub()).changeBudget(offer.getAmount());

                if (offer.getFromClubPlayer() != null)
                {
                    Player player = PlayerCollection.getPlayerByName(offer.getFromClubPlayer());
                    assert player != null;
                    player.setTeam(offer.getToClub());
                    player.setForSale(false);
                }
                if (offer.getToClubPlayer() != null)
                {
                    Player player = PlayerCollection.getPlayerByName(offer.getToClubPlayer());
                    assert player != null;
                    player.setTeam(offer.getFromClub());
                    player.setForSale(false);
                }
                server.removeOffer(offer.getId());
                server.writeClubsToFile();
                PlayerCollection.writeToFile();
                ActivityLogger.log("'" + offer.getToClub() + "' accepted offer from '" + offer.getFromClub() + "'");
            }
            write(valid);
        }
    }

    // TODO: Method not tested yet
    public void rejectOffer(Offer offer)
    {
        synchronized (server)
        {
            if (server.getOffers().contains(offer))
            {
                server.removeOffer(offer.getId());
                ActivityLogger.log("'" + offer.getToClub() + "' rejected offer from '" + offer.getFromClub() + "'");
                write(true);
            }
            else write(false);
        }
    }

    // TODO: Method not tested yet
    public void addNewPlayer(Player player)
    {
        synchronized (server) {
            Player original = PlayerCollection.getPlayer(player.getId());
            if (original == null) {
                PlayerCollection.addPlayer(player);
                ActivityLogger.log("New player added with ID - " + player.getId());
                write(true);
            } else if (original.getJerseyNumber() != player.getJerseyNumber()) {
                original.setJerseyNumber(player.getJerseyNumber());
                PlayerCollection.writeToFile();
                ActivityLogger.log("Player (ID: " + original.getId() + ") jersey number changed");
                write(true);
            } else {
                ActivityLogger.log("Player (ID: " + player.getId() + ") information mismatch");
                write(false);
            }
        }
    }

}
