package com.example.trialproject.Models;

public class ProfileModel {
    private String name;
    private String email;

    private String roll;

    public ProfileModel(String name, String email,String roll) {
        this.name = name;
        this.email = email;
        this.roll=roll;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
