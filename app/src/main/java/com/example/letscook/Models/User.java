package com.example.letscook.Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> recipes;



    private boolean isPremium;

    public User( String username,String firstName, String lastName, String email, String password){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isPremium = false;
        this.recipes = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
    public List<String> getRecipes() { return recipes; }
    public void setRecipes(List<String> recipes) { this.recipes = recipes; }
}

