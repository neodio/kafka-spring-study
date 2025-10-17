package com.example.userservice;

public class User {

    private String email;
    private String name;
    private String password;

    public User() {
    }

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
