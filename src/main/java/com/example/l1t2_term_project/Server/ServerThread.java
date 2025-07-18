package com.example.l1t2_term_project.Server;

import com.example.l1t2_term_project.DTO.LoginDTO;
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

                }
                else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                socketWrapper.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void read()
    {

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
}
