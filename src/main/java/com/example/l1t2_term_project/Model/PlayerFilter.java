package com.example.l1t2_term_project.Model;

public class PlayerFilter
{
    private String name;
    private String position;
    private String specificPosition;
    private String nationality;
    private String team;
    private boolean forSale;
    private int startingValue;
    private int endingValue;

    public PlayerFilter()
    {
        name = null;
        position = null;
        specificPosition = null;
        nationality = null;
        team = null;
        forSale = false;
        startingValue = 0;
        endingValue = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSpecificPosition() {
        return specificPosition;
    }

    public void setSpecificPosition(String specificPosition) {
        this.specificPosition = specificPosition;
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
}
