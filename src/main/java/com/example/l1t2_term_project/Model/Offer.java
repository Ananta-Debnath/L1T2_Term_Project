package com.example.l1t2_term_project.Model;

import java.io.Serializable;
import java.util.Objects;

public class Offer implements Serializable {
    public enum Status {
        Accept, Reject, Make, GetList
    }
    private int id;
    private Status status;
    private long amount; // fromClub to toClub - can be negative

    private String fromClub;
    private Integer fromClubPlayerID;

    private String toClub;
    private Integer toClubPlayerID;

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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
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

    public Integer getFromClubPlayerID() {
        return fromClubPlayerID;
    }

    public void setFromClubPlayerID(Integer fromClubPlayerID) {
        this.fromClubPlayerID = fromClubPlayerID;
    }

    public String getToClub() {
        return toClub;
    }

    public void setToClub(String toClub) {
        this.toClub = toClub;
    }

    public Integer getToClubPlayerID() {
        return toClubPlayerID;
    }

    public void setToClubPlayerID(Integer toClubPlayerID) {
        this.toClubPlayerID = toClubPlayerID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return id == offer.id && amount == offer.amount && Objects.equals(fromClub, offer.fromClub) && Objects.equals(fromClubPlayerID, offer.fromClubPlayerID) && Objects.equals(toClub, offer.toClub) && Objects.equals(toClubPlayerID, offer.toClubPlayerID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, fromClub, fromClubPlayerID, toClub, toClubPlayerID);
    }
}
