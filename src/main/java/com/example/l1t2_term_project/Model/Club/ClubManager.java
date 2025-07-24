package com.example.l1t2_term_project.Model.Club;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.l1t2_term_project.Model.Player.Player;
import com.example.l1t2_term_project.Model.Player.Role;
import com.example.l1t2_term_project.Utils.ActivityLogger;

public class ClubManager {

    private static final String PLAYERS_FILE_PATH = Objects.requireNonNull(
            ClubManager.class.getResource("/com/example/l1t2_term_project/Database/Players.csv")
    ).getPath();
    private static final String CLUB_FILE_PATH= Objects.requireNonNull(
            ClubManager.class.getResource("/com/example/l1t2_term_project/Database/Clubs.csv")
    ).getPath();

    private List<Club> clubs;
   // private static List<Player> players = new ArrayList<>();

   public static Club ClubWithPlayers(String clubName)throws IOException{

    Club club=clubInfo(clubName);
    List<Player>players=loadPlayersOfClub(clubName);

    for(Player p:players){
        club.addPlayer(p);
    }

    return club;
   }



private static Club clubInfo(String clubName)throws IOException{

    try(BufferedReader br=new BufferedReader(new FileReader(CLUB_FILE_PATH))){

        br.readLine();

        String line;

        while(true){
            line=br.readLine();
            if(line==null) break;

            String[] tokens=line.split(",");

            if(tokens[0].equalsIgnoreCase(clubName)){

                return new Club(tokens[0],tokens[1],tokens[2],Long.parseLong(tokens[3]),tokens[4],tokens[5]);
            }

        }

    }
    throw new IOException("Club not found: "+clubName);
}
    private static List<Player> loadPlayersOfClub(String clubName) throws IOException{

        List<Player> players=new ArrayList<>();
        System.out.println("Searching for players of club: " + clubName);

        File file=new File(PLAYERS_FILE_PATH);
        if(!file.exists()){
            throw new FileNotFoundException("Players CSV not found");
        }

        try(BufferedReader br=new BufferedReader(new FileReader( PLAYERS_FILE_PATH))){

            br.readLine();

            String line;
            

            while(true){
                line=br.readLine();
                if(line==null) break;
                String[] tokens=line.split(",");

                if(tokens[7].equalsIgnoreCase(clubName)){
                    System.out.println("MATCHED PLAYER: " + line);
                    players.add(createPlayersFromFile(tokens));
                }
            }
        }catch (Exception e){
            System.err.println("Error reading players: ");
            e.printStackTrace();
            throw e;
        }
        System.out.println("Total players found: " + players.size());
        return players;
    }


    private static Player createPlayersFromFile(String[] tokens) throws IOException{

        Player player=new Player();

        player.setId(Integer.parseInt(tokens[0]));
        player.setName(tokens[1]);
        player.setAge(Integer.parseInt(tokens[2]));
        player.setHeight(Float.parseFloat(tokens[3]));
        player.setRole(Role.valueOf(tokens[4]));
        player.setExtraRoles(tokens[5]);
        player.setNationality(tokens[6]);
        player.setTeam(tokens[7]);
        player.setJerseyNumber(Integer.parseInt(tokens[8]));

        player.setGoals(Integer.parseInt(tokens[9]));
        player.setAssists(Integer.parseInt(tokens[10]));
        player.setTackles(Integer.parseInt(tokens[11]));
        player.setInterceptions(Integer.parseInt(tokens[12]));
        player.setSaves(Integer.parseInt(tokens[13]));
        player.setMatchPlayed(Integer.parseInt(tokens[14]));
        player.setAttributes(tokens[15]);
        player.setForm(tokens[16]);

        player.setWeeklySalary(Long.parseLong(tokens[17]));
        player.setValue(Long.parseLong(tokens[18]));
        player.setContractEnd(tokens[19]);
        player.setForSale(Boolean.parseBoolean(tokens[20]));

        return player;
    }
}
