package ru.itmo.is.lab1.security;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@SessionScoped
public class UserSession implements Serializable {

    private String username;

    public boolean isLoggedIn() {
        return username != null;
    }

    public String getUsername() {
        return username;
    }

    public void login(String username) {
        this.username = username;
    }

    public void logout() {
        this.username = null;
    }
}
