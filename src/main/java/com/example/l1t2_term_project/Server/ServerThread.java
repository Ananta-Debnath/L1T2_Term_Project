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
                    validateLogin((LoginDTO) obj);
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
            ActivityLogger.log("\"" + loginDTO.getUsername() + "\" logged in");
            server.addClient(loginDTO.getUsername(), socketWrapper);
            write(true);

            write(server.getClub(loginDTO.getUsername()));
            PlayerFilter filter = new PlayerFilter();
            filter.setTeam(loginDTO.getUsername());
            filter.setForSale(false);
            System.out.println(filter);
            List<Player> players = PlayerCollection.getFilteredPlayers(filter);
            for (Player player : players) System.out.println(player);
            write(players);
        }
        else
        {
            ActivityLogger.log("Failed login attempted - " + loginDTO);
            write(false);
        }
    }
}
