package com.example.l1t2_term_project.Model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player
{
    // Info
    private int id;
    private String name;
    private int age;
    private float height; // meter
    private Role role;
    private List<Role> extraRoles;
    private String nationality;
    private String team;
    // private int teamID;
    private int jerseyNumber;

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


    // Instance Block
    {
        this.extraRoles = new ArrayList<>();
        this.attributes = new ArrayList<>();
    }
    // Constructor
    public Player(){};

    public Player(int id, String name, int age, float height, Role role, String nationality, String team, int goals, int assists, int tackles, int interceptions, int saves, int matchPlayed, String form, double weeklySalary, double value, String contractEnd, boolean forSale) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Position getPosition() {
        return role.getPosition();
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

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
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

    public List<Role> getExtraRoles() {
        return extraRoles;
    }

    public void setExtraRoles(List<Role> extraRoles) {
        this.extraRoles = extraRoles;
    }

    public void setExtraRoles(String roles) throws IOException {
        roles = roles.trim();
        if (roles.isEmpty()) return;
        String[] tokens = roles.split("-");

        for (String role : tokens)
        {
            try
            {
                extraRoles.add(Role.valueOf(role));
            } catch (IllegalArgumentException e) {
                throw new IOException(e);
            }
        }
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public void setAttributes(String str) {
        str = str.trim();
        if (str.isEmpty()) return;

        String[] tokens = str.split("-");
        attributes.addAll(List.of(tokens));
    }

    public boolean isForSale() {
        return forSale;
    }

    public void setForSale(boolean forSale) {
        this.forSale = forSale;
    }

    public String toCSVLine() {
        StringBuilder str = new StringBuilder();

        str.append(id).append(",");
        str.append(name).append(",");
        str.append(age).append(",");
        str.append(height).append(",");
        str.append(role.name()).append(",");

        // Join extra positions with "-"
        for (int i = 0; i < extraRoles.size(); i++) {
            str.append(extraRoles.get(i).name());
            if (i < extraRoles.size() - 1) str.append("-");
        }
        str.append(",");

        str.append(nationality).append(",");
        str.append(team).append(",");
        str.append(jerseyNumber).append(",");
        str.append(goals).append(",");
        str.append(assists).append(",");
        str.append(tackles).append(",");
        str.append(interceptions).append(",");
        str.append(saves).append(",");
        str.append(matchPlayed).append(",");

        // Join attributes with "-"
        for (int i = 0; i < attributes.size(); i++) {
            str.append(attributes.get(i).trim());
            if (i < attributes.size() - 1) str.append("-");
        }
        str.append(",");

        str.append(form).append(",");
        str.append(weeklySalary).append(",");
        str.append(value).append(",");
        str.append(contractEnd).append(",");
        str.append(forSale);

        return str.toString();
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", role=" + role +
                ", extraRoles=" + extraRoles +
                ", nationality='" + nationality + '\'' +
                ", team='" + team + '\'' +
                ", jerseyNumber=" + jerseyNumber +
                ", goals=" + goals +
                ", assists=" + assists +
                ", tackles=" + tackles +
                ", interceptions=" + interceptions +
                ", saves=" + saves +
                ", matchPlayed=" + matchPlayed +
                ", attributes=" + attributes +
                ", form='" + form + '\'' +
                ", weeklySalary=" + weeklySalary +
                ", value=" + value +
                ", contractEnd='" + contractEnd + '\'' +
                ", forSale=" + forSale +
                '}';
    }

    // Static methods
    // public static Player readFromFile(---);

    // public static --- getPlayerImage(int playerId);
}
