package com.example.l1t2_term_project.DTO;

import java.io.Serializable;

public class LoginDTO implements Serializable {
    private String username;
    private String password;

    public LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof LoginDTO)) return false;

        LoginDTO loginDTO = (LoginDTO) obj;
        return username.equalsIgnoreCase(loginDTO.getUsername()) && password.equals(loginDTO.getPassword());
    }

    @Override
    public String toString() {
        return "[username='" + username + "', password='" + password + "']";
    }
}
