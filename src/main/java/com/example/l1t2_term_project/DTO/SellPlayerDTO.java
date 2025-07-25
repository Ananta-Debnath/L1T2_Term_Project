package com.example.l1t2_term_project.DTO;

import java.io.Serializable;

public class SellPlayerDTO implements Serializable {
    private int playerId;
    private long newValue;
    private String currentClub; // For verification

    public SellPlayerDTO(int playerId, long newValue, String currentClub) {
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

    public long getNewValue() {
        return newValue;
    }

    public void setNewValue(long newValue) {
        this.newValue = newValue;
    }

    public String getCurrentClub() {
        return currentClub;
    }

    public void setCurrentClub(String currentClub) {
        this.currentClub = currentClub;
    }
}
