package com.example.l1t2_term_project.Model;

import java.io.Serializable;

public class Offer implements Serializable {
    public enum Status {
        Accept, Reject, Counter, Make
    }
    private int id;
    private Status status;

    private String fromClub;
    private int fromClubPlayerID;
    private long fromClubAmount;

    private String toClub;
    private int toClubPlayerID;
    private long toClubAmount;

    public Offer(int id, Status status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFromClub() {
        return fromClub;
    }

    public void setFromClub(String fromClub) {
        this.fromClub = fromClub;
    }

    public int getFromClubPlayerID() {
        return fromClubPlayerID;
    }

    public void setFromClubPlayerID(int fromClubPlayerID) {
        this.fromClubPlayerID = fromClubPlayerID;
    }

    public long getFromClubAmount() {
        return fromClubAmount;
    }

    public void setFromClubAmount(long fromClubAmount) {
        this.fromClubAmount = fromClubAmount;
    }

    public String getToClub() {
        return toClub;
    }

    public void setToClub(String toClub) {
        this.toClub = toClub;
    }

    public int getToClubPlayerID() {
        return toClubPlayerID;
    }

    public void setToClubPlayerID(int toClubPlayerID) {
        this.toClubPlayerID = toClubPlayerID;
    }

    public long getToClubAmount() {
        return toClubAmount;
    }

    public void setToClubAmount(long toClubAmount) {
        this.toClubAmount = toClubAmount;
    }
}
