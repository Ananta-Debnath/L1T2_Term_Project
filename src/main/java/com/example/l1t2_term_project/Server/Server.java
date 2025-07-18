package com.example.l1t2_term_project.Server;


import com.example.l1t2_term_project.DTO.LoginDTO;
import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Model.Player.Player;
import com.example.l1t2_term_project.Model.Player.PlayerCollection;
import com.example.l1t2_term_project.Model.Player.Role;
import com.example.l1t2_term_project.Utils.ActivityLogger;
import com.example.l1t2_term_project.Utils.SocketWrapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Server {

    private HashMap<String, SocketWrapper> clientMap;
    private List<Club> clubs;
    private static final String CLUBS_PATH = "src/main/java/com/example/l1t2_term_project/Database/Clubs.csv";
    private List<LoginDTO> credentials;
    private static final String CREDENTIALS_PATH = "src/main/java/com/example/l1t2_term_project/Database/Credentials.csv";

    Server() {
        PlayerCollection.readFromFile();
        readClubs();
        readCredentials();

        startServer();
    }

    private void startServer()
    {
        ActivityLogger.log("Server Started");
        clientMap = new HashMap<>();
        try (ServerSocket serverSocket = new ServerSocket(12913)) {
            while (true) { // Will be running forever
                Socket clientSocket = serverSocket.accept();
                serve(clientSocket);
            }
        } catch (Exception e) {
            ActivityLogger.log("Server starts:" + e);
            System.out.println("Server starts:" + e);
        }
    }

    public void serve(Socket clientSocket) throws IOException {
        SocketWrapper socketWrapper = new SocketWrapper(clientSocket);
        new ServerThread(socketWrapper, this).start();
    }

    public synchronized void addClient(String name, SocketWrapper socketWrapper)
    {
        clientMap.put(name, socketWrapper);
    }

    public synchronized void removeClient(String name)
    {
        clientMap.remove(name);
    }

    public void readClubs()
    {
        clubs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CLUBS_PATH))) {
            reader.readLine(); // Ignoring header line
            while (true) {
                String line = reader.readLine();
                if (line == null) break;

                Club club = new Club();
                String[] tokens = line.split(",");
                if (tokens.length != 6) throw new IOException("Invalid info length");

                club.setName(tokens[0].trim());
                club.setLeagueName(tokens[1].trim());
                club.setCountry(tokens[2].trim());
                club.setBudget(Long.parseLong(tokens[3].trim()));
                club.setStadiumName(tokens[4].trim());
                club.setManagerName(tokens[5].trim());

                clubs.add(club);
            }
        } catch (IOException e) {
            ActivityLogger.log("In readClubs method - " + e);
            e.printStackTrace();
        }
    }

    public void readCredentials()
    {
        credentials = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_PATH)))
        {
            reader.readLine(); // Ignoring header line
            while (true) {
                String line = reader.readLine();
                if (line == null) break;

                String[] tokens = line.split(",");
                if (tokens.length != 2) throw new IOException("Invalid info length");

                credentials.add(new LoginDTO(tokens[0].trim(), tokens[1].trim(), false));
            }
        }
        catch (IOException e)
        {
            ActivityLogger.log("In readCredentials method - " + e);
            e.printStackTrace();
        }
    }

    public boolean validateCredentials(LoginDTO loginDTO)
    {
        return credentials.contains(loginDTO);
    }

    public Club getClub(String name)
    {
        for (Club club : clubs) {
            if (club.getName().equalsIgnoreCase(name.trim())) {
                return club;
            }
        }
        ActivityLogger.log("In getClub method - Club not found");
        return null;
    }

    public static void main(String[] args) {
        new Server();
    }
}

