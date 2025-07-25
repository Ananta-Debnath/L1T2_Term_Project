package com.example.l1t2_term_project.Model.Player;

import java.io.Serializable;

public class PlayerFilter implements Serializable {
    private String name;
    private Position position;
    private Role role;
    private String nationality;
    private String team;
    private boolean forSale;
    private long minValue;
    private long maxValue;

    private String currentClub;

    public PlayerFilter(String currentClub) {
        name = null;
        position = null;
        role = null;
        nationality = null;
        team = null;
        forSale = true;
        minValue = 0;
        maxValue = 0;

        this.currentClub = currentClub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
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

    public long getMinValue() {
        return minValue;
    }

    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    public String getCurrentClub() {
        return currentClub;
    }

    public void setCurrentClub(String currentClub) {
        this.currentClub = currentClub;
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
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                '}';
    }
}
