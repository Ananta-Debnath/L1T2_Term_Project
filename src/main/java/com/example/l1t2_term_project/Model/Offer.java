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
    private String fromClubPlayer;


    private String toClub;
    private String toClubPlayer;

    public Offer(int id, Status status) {
        this.id = id;
        this.status = status;

        amount = 0;
        fromClubPlayer = null;
        toClubPlayer = null;
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

    public String getFromClubPlayer() {
        return fromClubPlayer != null ? fromClubPlayer : "N/A";
    }

    public void setFromClubPlayer(String fromClubPlayer) {
        this.fromClubPlayer = fromClubPlayer;
    }

    public String getToClub() {
        return toClub;
    }

    public void setToClub(String toClub) {
        this.toClub = toClub;
    }

    public String getToClubPlayer() {
        return toClubPlayer != null ? toClubPlayer : "N/A";
    }

    public void setToClubPlayer(String toClubPlayer) {
        this.toClubPlayer = toClubPlayer;
    }

    public String toCSVLine()
    {
        StringBuilder str = new StringBuilder();

        str.append(id).append(",");
        str.append(fromClub).append(",");
        str.append(fromClubPlayer).append(",");
        str.append(toClub).append(",");
        str.append(toClubPlayer).append(",");
        str.append(amount);

        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return id == offer.id && amount == offer.amount && Objects.equals(fromClub, offer.fromClub) && Objects.equals(fromClubPlayer, offer.fromClubPlayer) && Objects.equals(toClub, offer.toClub) && Objects.equals(toClubPlayer, offer.toClubPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, fromClub, fromClubPlayer, toClub, toClubPlayer);
    }
}
