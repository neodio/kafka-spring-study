package com.example.emailservice;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_log")
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long receiverUserId; // 이메일을 받은 사용자의 id
    private String receiverEmail; // 수신자 이메일
    private String subject; // 이메일 제목

    public EmailLog() {
    }

    public EmailLog(Long receiverUserId, String receiverEmail, String subject) {
        this.receiverUserId = receiverUserId;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
    }

    public Long getId() {
        return id;
    }

    public Long getReceiverUserId() {
        return receiverUserId;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getSubject() {
        return subject;
    }
}
