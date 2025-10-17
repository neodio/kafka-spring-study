package com.example.emailsendproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {"email.send"}, 
               brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
@DirtiesContext
class EmailIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("이메일 전송 API 전체 플로우 테스트")
    void sendEmail_Integration() throws Exception {
        // given
        SendEmailRequestDto request = new SendEmailRequestDto(
                "sender@example.com",
                "receiver@example.com",
                "통합 테스트 제목",
                "통합 테스트 본문"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/emails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("이메일 발송 요청 완료"));
    }

    @Test
    @DisplayName("필수 필드가 누락된 경우에도 요청은 성공한다")
    void sendEmail_WithNullFields() throws Exception {
        // given
        SendEmailRequestDto request = new SendEmailRequestDto(null, null, null, null);
        String requestBody = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/emails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }
}
