package com.example.l1t2_term_project.Server;


import com.example.l1t2_term_project.DTO.LoginDTO;
import com.example.l1t2_term_project.DTO.NotificationDTO;
import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Model.Offer;
import com.example.l1t2_term_project.Model.Player.PlayerCollection;
import com.example.l1t2_term_project.Utils.ActivityLogger;
import com.example.l1t2_term_project.Utils.SocketWrapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {

    private HashMap<String, SocketWrapper> clientMap;
    private List<Club> clubs;
    private static final String CLUBS_PATH = "src/main/java/com/example/l1t2_term_project/Database/Clubs.csv";
    private List<LoginDTO> credentials;
    private static final String CREDENTIALS_PATH = "src/main/java/com/example/l1t2_term_project/Database/Credentials.csv";
    private List<Offer> offers;
    private static final String OFFERS_PATH = "src/main/java/com/example/l1t2_term_project/Database/Offers.csv";

    Server() {
        PlayerCollection.readFromFile();
        readClubs();
        readCredentials();
        readOffers();

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

    public void notifyAllClient()
    {
        for (SocketWrapper socket : clientMap.values()) {
            try {
                socket.write(new NotificationDTO("Refresh"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void readClubs() {
        clubs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CLUBS_PATH))) {
            reader.readLine(); // Ignoring header line
            while (true) {
                String line = reader.readLine();
                if (line == null) break;

                Club club = new Club();
                String[] tokens = line.split(",");
                if (tokens.length != 6) throw new IOException("Invalid club info length");

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
        }
    }

    public void writeClubsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CLUBS_PATH, false)))
        {
            writer.println("name,leagueName,country,budget,stadiumName,managerName");
            for (Club club : clubs) writer.println(club.toCSVLine());
        }
        catch (IOException e)
        {
            ActivityLogger.log("In writeClubToFile method - " + e);
        }
    }

    public void readCredentials() {
        credentials = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_PATH)))
        {
            reader.readLine(); // Ignoring header line
            while (true) {
                String line = reader.readLine();
                if (line == null) break;

                String[] tokens = line.split(",");
                if (tokens.length != 2) throw new IOException("Invalid info length");

                credentials.add(new LoginDTO(tokens[0].trim(), tokens[1].trim(), null));
            }
        }
        catch (IOException e)
        {
            ActivityLogger.log("In readCredentials method - " + e);
        }
    }

    public void writeCredentialsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CREDENTIALS_PATH, false)))
        {
            writer.println("username,password");
            for (LoginDTO loginDTO : credentials)
            {
                writer.println(loginDTO.getUsername() + "," + loginDTO.getPassword());
            }
        }
        catch (IOException e)
        {
            ActivityLogger.log("In writeCredentialsToFile method - " + e);
        }
    }

    public void readOffers() {
        offers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(OFFERS_PATH))) {
            reader.readLine(); // Ignoring header line
            while (true) {
                String line = reader.readLine();
                if (line == null) break;

                Offer offer = new Offer(0, Offer.Status.Make);
                String[] tokens = line.split(",");
                if (tokens.length != 6) throw new IOException("Invalid offer info length");

                offer.setId(Integer.parseInt(tokens[0].trim()));
                offer.setFromClub(tokens[1].trim());
                offer.setFromClubPlayer(tokens[2].trim());
                offer.setToClub(tokens[3].trim());
                offer.setToClubPlayer(tokens[4].trim());
                offer.setAmount(Long.parseLong(tokens[5].trim()));

                offers.add(offer);
            }
        } catch (IOException e) {
            ActivityLogger.log("In readClubs method - " + e);
        }
    }

    public void writeOffersToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(OFFERS_PATH, false)))
        {
            writer.println("id,fromClub,fromClubPlayer,toClub,toClubPlayer,amount");
            for (Offer offer : offers) writer.println(offer.toCSVLine());
        }
        catch (IOException e)
        {
            ActivityLogger.log("In writeOffersToFile method - " + e);
        }
    }

    public boolean validateCredentials(LoginDTO loginDTO)
    {
        return credentials.contains(loginDTO);
    }

    public boolean changePass(LoginDTO loginDTO) {
        LoginDTO credential = null;
        for (LoginDTO dto : credentials)
        {
            if (dto.getUsername().equalsIgnoreCase(loginDTO.getUsername()))
            {
                credential = dto;
                break;
            }
        }
        if (credential != null && !loginDTO.getPassword().isEmpty())
        {
            credential.setPassword(loginDTO.getPassword());
            ActivityLogger.log("Changed password for '" + credential.getUsername() + "'");
            writeCredentialsToFile();
            return true;
        }
        else
        {
            ActivityLogger.log("Failed attempt for password change");
            return false;
        }

    }

    public Club getClub(String name) {
        for (Club club : clubs) {
            if (club.getName().equalsIgnoreCase(name.trim())) {
                return club;
            }
        }
        ActivityLogger.log("In getClub method - Club not found");
        return null;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public Offer getOffer(int id) {
        for (Offer offer : offers)
        {
            if (offer.getId() == id) return offer;
        }
        return null;
    }

    public void addOffer(Offer offer)
    {
        if (offers.isEmpty()) offer.setId(1);
        else offer.setId(offers.get(offers.size() - 1).getId() + 1);
        offers.add(offer);
        writeOffersToFile();
    }

    public void removeOffer(int id)
    {
        offers.remove(getOffer(id));
        writeOffersToFile();
    }

    public static void main(String[] args) {
        new Server();
    }
}

