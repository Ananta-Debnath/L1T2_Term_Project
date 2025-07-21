package com.example.l1t2_term_project.DTO;

import java.io.Serializable;

public class BuyPlayerDTO implements Serializable {
    private int playerId;
    private String newClub;
    private long newSalary;
    private String currentClub; // For verification

    public BuyPlayerDTO(int playerId, String newClub, long newSalary, String currentClub) {
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

    public long getNewSalary() {
        return newSalary;
    }

    public void setNewSalary(long newSalary) {
        this.newSalary = newSalary;
    }

    public String getCurrentClub() {
        return currentClub;
    }

    public void setCurrentClub(String currentClub) {
        this.currentClub = currentClub;
    }
}
