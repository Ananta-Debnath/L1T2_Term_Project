package com.example.l1t2_term_project.DTO;

public class BuyPlayerDTO {
    private int playerId;
    private String newClub;
    private int newSalary;
    private String currentClub; // For verification

    public BuyPlayerDTO(int playerId, String newClub, int newSalary, String currentClub) {
        this.playerId = playerId;
        this.newClub = newClub;
        this.newSalary = newSalary;
        this.currentClub = currentClub;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getNewClub() {
        return newClub;
    }

    public void setNewClub(String newClub) {
        this.newClub = newClub;
    }

    public int getNewSalary() {
        return newSalary;
    }

    public void setNewSalary(int newSalary) {
        this.newSalary = newSalary;
    }

    public String getCurrentClub() {
        return currentClub;
    }

    public void setCurrentClub(String currentClub) {
        this.currentClub = currentClub;
    }
}
