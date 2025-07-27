package com.example.l1t2_term_project.DTO;

import java.io.Serializable;

public class LoginDTO implements Serializable {
    public enum Type {
        SignIn, SignOut, ChangePass;
    }
    private String username;
    private String password;
    private Type type;

    public LoginDTO(String username, String password, Type type) {
        if (username.equalsIgnoreCase("a")) username = "Liverpool"; // TODO: remove later
        if (password != null && password.equalsIgnoreCase("a")) password = "liver"; // TODO: remove later
        this.username = username;
        this.password = password;
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
