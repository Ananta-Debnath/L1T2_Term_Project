package com.example.l1t2_term_project.Model.Player;

import java.util.ArrayList;
import java.util.List;

public enum Position {
    ATTACKER, MIDFIELDER, DEFENDER, GOALKEEPER;

    public List<Role> getRoles()
    {
        List<Role> roles = new ArrayList<>();
        for (Role role : Role.values())
        {
            if (role.getPosition() == this) roles.add(role);
        }
        return roles;
    }

    public static Position fromString(String str) {
        if (str == null) return null;
        try {
            return Position.valueOf(str.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        String lower = name().toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
