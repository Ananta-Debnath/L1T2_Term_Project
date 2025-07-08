package com.example.l1t2_term_project.Model.Player;

public enum Role {
    ST, LW, RW, CF,
    CAM, CM, CDM, LM, RM,
    CB, LB, RB,
    GK;

    public Position getPosition() {
        switch (this) {
            case ST: case LW: case RW: case CF: case CAM:
                return Position.ATTACKER;
            case CM: case CDM: case LM: case RM:
                return Position.MIDFIELDER;
            case CB: case LB: case RB:
                return Position.DEFENDER;
            case GK:
                return Position.GOALKEEPER;
            default:
                throw new IllegalArgumentException("Unknown role: " + this);
        }
    }
}
