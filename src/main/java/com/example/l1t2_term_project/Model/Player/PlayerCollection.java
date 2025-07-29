package com.example.l1t2_term_project.Model.Player;

import com.example.l1t2_term_project.Utils.ActivityLogger;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerCollection
{
    private static final String DATABASE_FILE_PATH = "src/main/java/com/example/l1t2_term_project/Database/Players.csv";
    private static List<Player> players = new ArrayList<>();

    public static List<Player> getPlayers() {
        return players;
    }

    public static Player getPlayer(int id)
    {
        for (Player p : players)
        {
            if (p.getId() == id) return p;

            else if (p.getId() > id) break;
        }
        return null;
    }

    public static Player getPlayerByName(String name)
    {
        for (Player p : players)
        {
            if (p.getName().equalsIgnoreCase(name.trim())) return p;
        }
        return null;
    }

    public static void addPlayer(Player player)
    {
        player.setId(players.get(players.size() - 1).getId() + 1);
        players.add(player);
        writeToFile();
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

                player.setWeeklySalary(Long.parseLong(tokens[17]));
                player.setValue(Long.parseLong(tokens[18]));
                player.setContractEnd(tokens[19]);
                player.setForSale(Boolean.parseBoolean(tokens[20]));

                players.add(player);
            }

            players.sort(Comparator.comparingInt(Player::getId));
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
        Stream<Player> filteredPlayers = players.stream();
        if (filter.getCurrentClub() != null) filteredPlayers = filteredPlayers.filter(p -> !p.getTeam().equalsIgnoreCase(filter.getCurrentClub())); // Ignore players of current club

        if (filter.getName() != null) filteredPlayers = filteredPlayers.filter(p -> {
                    String[] nameParts = p.getName().toLowerCase().split(" ");
                    for (String part : nameParts) {
                        if (part.startsWith(filter.getName())) return true;
                    }
                    return false;
                });

        if (filter.getPosition() != null) filteredPlayers = filteredPlayers.filter(p -> p.getPosition() == filter.getPosition());
        if (filter.getRole() != null) filteredPlayers = filteredPlayers.filter(p -> p.getRole() == filter.getRole() || p.getExtraRoles().contains(filter.getRole()));
        if (filter.getNationality() != null) filteredPlayers = filteredPlayers.filter(p -> p.getNationality().equalsIgnoreCase(filter.getNationality()));
        if (filter.getTeam() != null) filteredPlayers = filteredPlayers.filter(p -> p.getTeam().equalsIgnoreCase(filter.getTeam()));
        if (filter.isForSale()) filteredPlayers = filteredPlayers.filter(p -> p.isForSale() == filter.isForSale());
        if (filter.getMinValue() != 0) filteredPlayers = filteredPlayers.filter(p -> p.getValue() >= filter.getMinValue());
        if (filter.getMaxValue() != 0) filteredPlayers = filteredPlayers.filter(p -> p.getValue() <= filter.getMaxValue());

        return filteredPlayers.collect(Collectors.toList());
    }

    public static List<String> getAllNationalities()
    {
        Set<String> nations = new TreeSet<>();
        for (Player player : players) nations.add(player.getNationality());
        return new ArrayList<>(nations);
    }

    public static List<String> getAllTeams()
    {
        Set<String> teams = new TreeSet<>();
        for (Player player : players) teams.add(player.getTeam());
        return new ArrayList<>(teams);
    }


}
