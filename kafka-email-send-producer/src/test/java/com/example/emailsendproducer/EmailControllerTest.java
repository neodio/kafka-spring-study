package com.example.emailsendproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmailService emailService;

    @Test
    @DisplayName("이메일 전송 요청이 성공하면 200 OK를 반환한다")
    void sendEmail_Success() throws Exception {
        // given
        SendEmailRequestDto request = createEmailRequest();
        String requestBody = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/emails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("이메일 발송 요청 완료"));

        verify(emailService, times(1)).sendEmail(any(SendEmailRequestDto.class));
    }

    @Test
    @DisplayName("잘못된 JSON 형식으로 요청하면 400 Bad Request를 반환한다")
    void sendEmail_InvalidJson() throws Exception {
        // given
        String invalidJson = "{invalid json}";

        // when & then
        mockMvc.perform(post("/api/emails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    private SendEmailRequestDto createEmailRequest() {
        return new SendEmailRequestDto("sender@example.com",
                "receiver@example.com",
                "테스트 제목",
                "테스트 본문");
    }
}
