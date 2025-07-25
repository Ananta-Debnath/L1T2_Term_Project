package com.example.l1t2_term_project.Model.Club;

import com.example.l1t2_term_project.Client;
import com.example.l1t2_term_project.Model.Player.Player;
import com.example.l1t2_term_project.Model.Player.PlayerFilter;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Club implements Serializable{

    private String name;
    private String leagueName;
    private String country;
    private long budget;
    private String stadiumName;
    private String managerName;
    private List<Integer> playerIDs;
    private List<Player> PlayersList;

    //private String password;


    {
        playerIDs=new ArrayList<>();
        PlayersList=new ArrayList<>();
    }

    public Club(){ }

    public Club(String name, String leagueName, String country, long budget, String stadiumName, String managerName) {
        this.name = name;
        this.leagueName = leagueName;
        this.country = country;
        this.budget = budget;
        this.stadiumName = stadiumName;
        this.managerName = managerName;
    }

    public void setPlayersList(List<Player> playersList) {
        PlayersList = playersList;
    }

    public List<Player> getPlayersList() {
        return PlayersList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public List<Integer> getPlayerIDs() {
        return playerIDs;
    }

    public void setPlayerIDs(List<Integer> playerIDs) {
        this.playerIDs = playerIDs;
    }



    public void addPlayer(int playerID){

        if(!playerIDs.contains(playerID)){
            playerIDs.add(playerID);
        }
    }

    public void addPlayer(Player p){

        if(!PlayersList.contains(p)){
            PlayersList.add(p);
        }
    }

    public void removePlayer(int playerID){

            playerIDs.remove(Integer.valueOf(playerID));

    }

    public void removePlayer(Player p){

        PlayersList.remove(p);
    }

    public boolean canBuy(long value){

        return budget>=value;
    }

    public void changeBudget(long amount)
    {
        budget += amount;
    }





    public String toCSVLine(){

        StringBuilder str=new StringBuilder();

        str.append(name).append(",");
        str.append(leagueName).append(",");
        str.append(country).append(",");
        str.append(budget).append(",");
        str.append(stadiumName).append(",");
        str.append(managerName);

        return str.toString();

    }

    public static Club readFromServer(Client client)
    {
        client.write(client.getCurrentClub());
        Object obj = client.read();
        if (obj instanceof Club) return (Club) obj;
        else
        {
            System.err.println("Wrong object type - " + obj.getClass());
            return new Club();
        }
    }

    public void loadPlayers(Client client)
    {
        PlayersList.clear(); // Clear list

        PlayerFilter filter = new PlayerFilter(null);
        filter.setForSale(false);
        filter.setTeam(this.name);
        client.write(filter);

        Object obj = client.read();
        if (obj instanceof List<?>)
        {
            List<?> list = (List<?>) obj;
            if (list.isEmpty())
            {
                System.out.println("No players found");
            }
            else if (list.get(0) instanceof Player)
            {
                @SuppressWarnings("unchecked")
                List<Player> players = (List<Player>) list;
                for (Player player : players) addPlayer(player);
            }
            else
            {
                System.err.println("Wrong object");
            }
        }
    }


    /*
    public class ClubManager {
    private List<Club> clubs;

    public void transferPlayer(int playerId, Club fromClub, Club toClub, double fee) {
        if (fromClub.canBuy(fee)) {
            fromClub.removePlayer(playerId);
            toClub.addPlayer(playerId);
            fromClub.setBudget(fromClub.getBudget() - fee);
            toClub.setBudget(toClub.getBudget() + fee);
        }
    }
  }
     */
}
