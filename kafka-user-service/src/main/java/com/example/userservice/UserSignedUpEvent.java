package com.example.userservice;

public class UserSignedUpEvent {
    private Long userId;
    private String email;
    private String name;

    public UserSignedUpEvent(Long userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }
}
