package com.example.l1t2_term_project.Model.Club;

import com.example.l1t2_term_project.Model.Player.Player;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Club {

    private String name;
    private String leagueName;
    private String country;
    private double budget;
    private String stadiumName;
    private String managerName;
    private List<Integer> playerIDs;
    private List<Player> PlayersList;

    //private String password;


    public Club(String name, String leagueName, String country, double budget, String stadiumName, String managerName) {
        this.name = name;
        this.leagueName = leagueName;
        this.country = country;
        this.budget = budget;
        this.stadiumName = stadiumName;
        this.managerName = managerName;
        playerIDs=new ArrayList<>();
        PlayersList=new ArrayList<>();
    }

    public Club(){
       PlayersList =new ArrayList<>();
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

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
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

    public boolean canBuy(double value){

        return budget>=value;
    }


    /*
    public class ClubManager {
    private List<Club> clubs;

    public void transferPlayer(int playerId, Club fromClub, Club toClub, double fee) {
        if (fromClub.canAffordTransfer(fee)) {
            fromClub.removePlayer(playerId);
            toClub.addPlayer(playerId);
            fromClub.setBudget(fromClub.getBudget() - fee);
            toClub.setBudget(toClub.getBudget() + fee);
        }
    }
  }
     */
}
