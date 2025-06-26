package com.example.l1t2_term_project.Model;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    // Info
    private int id;
    private String name;
    private int age;
    private float height; // meter
    private String position;
    private List<String> extraPositons;
    private String nationality;
    private String team;
    // private int teamID;
    // private int jerseyNumber;

    // Stats
    private int goals;
    private int assists;
    private int tackles; // for defenders
    private int interceptions; // for defenders
    private int saves; // for GK
    private int matchPlayed;
    // private int minutesPlayed;
    private List<String> attributes;
    private String form;

    // Other
    private double weeklySalary;
    private double value;
    private String contractEnd; // Date
    private boolean forSale;


    // Constructor


    public Player(int id, String name, int age, float height, String position, String nationality, String team, int goals, int assists, int tackles, int interceptions, int saves, int matchPlayed, String form, double weeklySalary, double value, String contractEnd, boolean forSale) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.position = position;
        this.nationality = nationality;
        this.team = team;
        this.goals = goals;
        this.assists = assists;
        this.tackles = tackles;
        this.interceptions = interceptions;
        this.saves = saves;
        this.matchPlayed = matchPlayed;
        this.form = form;
        this.weeklySalary = weeklySalary;
        this.value = value;
        this.contractEnd = contractEnd;
        this.forSale = forSale;

        this.extraPositons = new ArrayList<>();
        this.attributes = new ArrayList<>();
    }

    // Getter-Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getTackles() {
        return tackles;
    }

    public void setTackles(int tackles) {
        this.tackles = tackles;
    }

    public int getInterceptions() {
        return interceptions;
    }

    public void setInterceptions(int interceptions) {
        this.interceptions = interceptions;
    }

    public int getSaves() {
        return saves;
    }

    public void setSaves(int saves) {
        this.saves = saves;
    }

    public int getMatchPlayed() {
        return matchPlayed;
    }

    public void setMatchPlayed(int matchPlayed) {
        this.matchPlayed = matchPlayed;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public double getWeeklySalary() {
        return weeklySalary;
    }

    public void setWeeklySalary(double weeklySalary) {
        this.weeklySalary = weeklySalary;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(String contractEnd) {
        this.contractEnd = contractEnd;
    }

    public List<String> getExtraPositons() {
        return extraPositons;
    }

    public void setExtraPositons(List<String> extraPositons) {
        this.extraPositons = extraPositons;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public boolean isForSale() {
        return forSale;
    }

    public void setForSale(boolean forSale) {
        this.forSale = forSale;
    }

    // Static methods
    // public static Player readFromFile(---);

    // public static --- getPlayerImage(int playerId);
}
