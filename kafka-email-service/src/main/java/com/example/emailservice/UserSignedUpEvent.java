package com.example.emailservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserSignedUpEvent {
    private Long userId;
    private String email;
    private String name;

    public UserSignedUpEvent() {
    }

    public UserSignedUpEvent(Long userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    // Json 값을 EmailSendMessage로 역직렬화하는 메서드
    public static UserSignedUpEvent fromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, UserSignedUpEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(" JSON 파싱 실패");
        }
    }
}
