package com.example.emailsendproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private EmailService emailService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(kafkaTemplate);
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("이메일 요청을 받아 Kafka 토픽으로 메시지를 전송한다")
    void sendEmail_Success() throws Exception {
        // given
        SendEmailRequestDto request = createEmailRequest();

        // when
        emailService.sendEmail(request);

        // then
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        
        verify(kafkaTemplate, times(1)).send(topicCaptor.capture(), messageCaptor.capture());
        
        assertThat(topicCaptor.getValue()).isEqualTo("email.send");
        
        String sentMessage = messageCaptor.getValue();
        EmailSendMessage emailSendMessage = objectMapper.readValue(sentMessage, EmailSendMessage.class);
        
        assertThat(emailSendMessage.getFrom()).isEqualTo("sender@example.com");
        assertThat(emailSendMessage.getTo()).isEqualTo("receiver@example.com");
        assertThat(emailSendMessage.getSubject()).isEqualTo("테스트 제목");
        assertThat(emailSendMessage.getBody()).isEqualTo("테스트 본문");
    }

    @Test
    @DisplayName("여러 이메일 요청을 순차적으로 처리한다")
    void sendEmail_Multiple() {
        // given
        SendEmailRequestDto request1 = createEmailRequest();
        SendEmailRequestDto request2 = new SendEmailRequestDto(
                "sender2@example.com",
                "receiver2@example.com",
                "두번째 제목",
                "두번째 본문"
        );

        // when
        emailService.sendEmail(request1);
        emailService.sendEmail(request2);

        // then
        verify(kafkaTemplate, times(2)).send(eq("email.send"), org.mockito.ArgumentMatchers.anyString());
    }

    private SendEmailRequestDto createEmailRequest() {
        return new SendEmailRequestDto("sender@example.com",
                "receiver@example.com",
                "테스트 제목",
                "테스트 본문");
    }
}
