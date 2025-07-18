package com.example.l1t2_term_project.Server;

import com.example.l1t2_term_project.DTO.LoginDTO;
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
                else if (obj instanceof Player) {
                    handlePlayerTransfer((Player) obj);
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
            ActivityLogger.log("Server - Client connection disrupted");
            return null;
        }
    }

    public void write(Object obj)
    {
        try{
            socketWrapper.write(obj);
        } catch (IOException e) {
            ActivityLogger.log("Server - Client connection disrupted");
        }
    }

    public void validateLogin(LoginDTO loginDTO)
    {
        if (server.validateCredentials(loginDTO))
        {
            synchronized (server) {
                clubName = loginDTO.getUsername();
                if (clubName.equals("a")) clubName = "Liverpool"; // TODO: REMOVE THIS LATER
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
    public void handlePlayerTransfer(Player player)
    {
        synchronized (server) {
            Player original = PlayerCollection.getPlayer(player.getId());
            boolean updated = false;
            if (original == null) // New player
            {
                PlayerCollection.addPlayer(player);
                updated = true;
                ActivityLogger.log("New player added with ID - " + player.getId());
            }
            else if (original.getJerseyNumber() != player.getJerseyNumber()) // Change jersey number
            {
                original.setJerseyNumber(player.getJerseyNumber());
                updated = true;
                ActivityLogger.log("Player (ID: " + original.getId() + ") jersey number changed");
            }
            else if (player.isForSale()) // Listing for sale
            {
                original.setForSale(true);
                original.setValue(player.getValue());
                updated = true;
                ActivityLogger.log("Player (ID: " + original.getId() + ") listed for sale");
            }
            else if (original.isForSale() && !player.isForSale()) // Buy
            {
                Club buyer = server.getClub(player.getTeam());
                if (buyer.canBuy(original.getValue())) {
                    buyer.changeBudget(-original.getValue());
                    server.getClub(original.getTeam()).changeBudget(original.getValue());
                    original.setForSale(false);
                    original.setTeam(buyer.getName());
                    server.writeClubsToFile();
                    updated = true;
                    ActivityLogger.log("Player (ID: " + original.getId() + ") transferd to '" + buyer.getName() + "'");
                }
                else {
                    ActivityLogger.log("Transfer failed: '" + buyer.getName() + "' cannot afford Player (ID: " + original.getId() + ")");
                }
            }

            if (updated) PlayerCollection.writeToFile();
            write(updated);
        }
    }
}
