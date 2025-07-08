package com.example.l1t2_term_project.Model.Player;

public class PlayerFilter {
    private String name;
    private Position position;
    private Role role;
    private String nationality;
    private String team;
    private boolean forSale;
    private int startingValue;
    private int endingValue;

    public PlayerFilter() {
        name = null;
        position = null;
        role = null;
        nationality = null;
        team = null;
        forSale = true;
        startingValue = 0;
        endingValue = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public boolean isForSale() {
        return forSale;
    }

    public void setForSale(boolean forSale) {
        this.forSale = forSale;
    }

    public int getStartingValue() {
        return startingValue;
    }

    public void setStartingValue(int startingValue) {
        this.startingValue = startingValue;
    }

    public int getEndingValue() {
        return endingValue;
    }

    public void setEndingValue(int endingValue) {
        this.endingValue = endingValue;
    }

    @Override
    public String toString() {
        return "PlayerFilter{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", role=" + role +
                ", nationality='" + nationality + '\'' +
                ", team='" + team + '\'' +
                ", forSale=" + forSale +
                ", startingValue=" + startingValue +
                ", endingValue=" + endingValue +
                '}';
    }
}
