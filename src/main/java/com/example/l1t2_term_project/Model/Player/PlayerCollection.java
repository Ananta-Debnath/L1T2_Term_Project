package com.example.l1t2_term_project.Model.Player;

import com.example.l1t2_term_project.Utils.ActivityLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerCollection
{
    private static final String DATABASE_FILE_PATH = "src/main/java/com/example/l1t2_term_project/Database/Players.csv";
    private static List<Player> players = new ArrayList<>();

    public static List<Player> getPlayers() {
        return players;
    }

    public static void readFromFile() {
        // try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(PlayerCollection.class.getResourceAsStream(INPUT_FILE_PATH)))))
        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE_PATH)))
        {
            reader.readLine(); // Ignoring header line
            while (true) {
                String line = reader.readLine();
                if (line == null) break;

                Player player = new Player();
                String[] tokens = line.split(",");
                if (tokens.length != 21) throw new IOException("Invalid info length");

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

                player.setWeeklySalary(Double.parseDouble(tokens[17]));
                player.setValue(Double.parseDouble(tokens[18]));
                player.setContractEnd(tokens[19]);
                player.setForSale(Boolean.parseBoolean(tokens[20]));

                players.add(player);
            }
        }
        catch (IOException e)
        {
            ActivityLogger.log("In PlayerCollection readFromFile method - " + e);
            e.printStackTrace();
        }
    }

    public static void writeToFile()
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATABASE_FILE_PATH, false)))
        {
            writer.println("id,name,age,height,role,extraPositions,nationality,team,jerseyNumber,goals,assists,tackles,interceptions,saves,matchPlayed,attributes,form,salary,value,contractEnd,forSale");
            for (Player player : players) writer.println(player.toCSVLine());
        }
        catch (IOException e)
        {
            ActivityLogger.log("In PlayerCollection writeToFile method - " + e);
            e.printStackTrace();
        }
    }

    public static List<Player> getFilteredPlayers(PlayerFilter filter)
    {
        return new ArrayList<>();
    }


}
