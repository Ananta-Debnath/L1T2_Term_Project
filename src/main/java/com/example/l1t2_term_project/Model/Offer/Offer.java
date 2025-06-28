package com.example.l1t2_term_project.Model.Offer;

public abstract class Offer
{
    int playerId;
    int fromClubId;
    int toClubId;

    public Offer(int playerId, int fromClubId, int toClubId)
    {
        this.playerId = playerId;
        this.fromClubId = fromClubId;
        this.toClubId = toClubId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getFromClubId() {
        return fromClubId;
    }

    public void setFromClubId(int fromClubId) {
        this.fromClubId = fromClubId;
    }

    public int getToClubId() {
        return toClubId;
    }

    public void setToClubId(int toClubId) {
        this.toClubId = toClubId;
    }

    public abstract void accept();

    public abstract void reject();

    public abstract void counter();
}
