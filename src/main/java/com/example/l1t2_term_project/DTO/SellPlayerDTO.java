package com.example.l1t2_term_project.DTO;

public class SellPlayerDTO {
    private int playerId;
    private int newValue;
    private String currentClub; // For verification

    public SellPlayerDTO(int playerId, int newValue, String currentClub) {
        this.playerId = playerId;
        this.newValue = newValue;
        this.currentClub = currentClub;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getNewValue() {
        return newValue;
    }

    public void setNewValue(int newValue) {
        this.newValue = newValue;
    }

    public String getCurrentClub() {
        return currentClub;
    }

    public void setCurrentClub(String currentClub) {
        this.currentClub = currentClub;
    }
}
