package com.example.l1t2_term_project.Server;

import com.example.l1t2_term_project.DTO.BuyPlayerDTO;
import com.example.l1t2_term_project.DTO.LoginDTO;
import com.example.l1t2_term_project.DTO.SellPlayerDTO;
import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Model.Player.Player;
import com.example.l1t2_term_project.Model.Player.PlayerCollection;
import com.example.l1t2_term_project.Model.Player.PlayerFilter;
import com.example.l1t2_term_project.Utils.ActivityLogger;
import com.example.l1t2_term_project.Utils.SocketWrapper;

import java.io.IOException;
import java.util.List;

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
        try {
            while (true) {
                Object obj = socketWrapper.read();
                if (obj instanceof LoginDTO)
                {
                    LoginDTO loginDTO = (LoginDTO) obj;
                    if (loginDTO.isLogInReq()) validateLogin(loginDTO);

                    else handleSignOut(loginDTO);
                }
                else if (obj instanceof PlayerFilter)
                {
                    List<Player> players = PlayerCollection.getFilteredPlayers((PlayerFilter) obj, clubName);
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
                else if (obj instanceof Player) {
                    addNewPlayer((Player) obj);
                }
                else {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            ActivityLogger.log("Server - Client connection disrupted");
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
                ActivityLogger.log("\"" + clubName + "\" logged in");
                server.addClient(clubName, socketWrapper);
                write(true); // Confirm

                write(PlayerCollection.getAllNationalities()); // Export nations
                write(PlayerCollection.getAllTeams()); // Export clubs

                write(server.getClub(clubName)); // Export logged in clubName
                PlayerFilter filter = new PlayerFilter();
                filter.setTeam(clubName);
                filter.setForSale(false);
                List<Player> players = PlayerCollection.getFilteredPlayers(filter, null);
                write(players); // Export clubName players
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
        ActivityLogger.log("\"" + username + "\" logged out");
    }

    // TODO: Method not tested yet
    public void handleTransfer(BuyPlayerDTO buyPlayerDTO)
    {
        Player player = PlayerCollection.getPlayer(buyPlayerDTO.getPlayerId());
        assert player != null;
        if (!player.getTeam().equalsIgnoreCase(buyPlayerDTO.getCurrentClub()) || !player.isForSale())
        {
            ActivityLogger.log("Player (ID: " + player.getId() + ") information mismatch");
            write(false);
            return;
        }

        Club buyer = server.getClub(buyPlayerDTO.getNewClub());
        if (buyer.canBuy(player.getValue())) {
            buyer.changeBudget(-player.getValue());
            server.getClub(player.getTeam()).changeBudget(player.getValue());

            player.setForSale(false);
            player.setWeeklySalary(buyPlayerDTO.getNewSalary());
            player.setTeam(buyer.getName());

            PlayerCollection.writeToFile();
            server.writeClubsToFile();
            ActivityLogger.log("Player (ID: " + player.getId() + ") transferd to '" + buyer.getName() + "'");
            write(true);
        }
        else {
            ActivityLogger.log("Transfer failed: '" + buyer.getName() + "' cannot afford Player (ID: " + player.getId() + ")");
            write(false);
        }
    }

    // TODO: Method not tested yet
    public void listForSale(SellPlayerDTO sellPlayerDTO)
    {
        Player player = PlayerCollection.getPlayer(sellPlayerDTO.getPlayerId());
        assert player != null;
        if (player.getTeam().equalsIgnoreCase(sellPlayerDTO.getCurrentClub()))
        {
            ActivityLogger.log("Player (ID: " + player.getId() + ") information mismatch");
            write(false);
        }
        else
        {
            player.setForSale(true);
            player.setValue(player.getValue());

            PlayerCollection.writeToFile();
            ActivityLogger.log("Player (ID: " + player.getId() + ") listed for sale");
            write(true);
        }
    }

    // TODO: Method not tested yet
    public void addNewPlayer(Player player)
    {
        Player original = PlayerCollection.getPlayer(player.getId());
        if (original == null)
        {
            PlayerCollection.addPlayer(player);
            ActivityLogger.log("New player added with ID - " + player.getId());
            write(true);
        }
        else if (original.getJerseyNumber() != player.getJerseyNumber())
        {
            original.setJerseyNumber(player.getJerseyNumber());
            PlayerCollection.writeToFile();
            ActivityLogger.log("Player (ID: " + original.getId() + ") jersey number changed");
            write(true);
        }
        else
        {
            ActivityLogger.log("Player (ID: " + player.getId() + ") information mismatch");
            write(false);
        }
    }

}
