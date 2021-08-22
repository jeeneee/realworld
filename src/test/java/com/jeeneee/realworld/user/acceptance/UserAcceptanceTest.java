package com.jeeneee.realworld.user.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.jeeneee.realworld.AcceptanceTest;
import com.jeeneee.realworld.user.dto.LoginRequest;
import com.jeeneee.realworld.user.dto.RegisterRequest;
import com.jeeneee.realworld.user.dto.UserResponse;
import com.jeeneee.realworld.user.dto.UserUpdateRequest;
import java.util.stream.Stream;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class UserAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 관련 기능")
    @TestFactory
    Stream<DynamicTest> manageUser() {
        RegisterRequest registerRequest = RegisterRequest.builder()
            .email("register@register.com")
            .username("register")
            .password("password")
            .build();
        LoginRequest loginRequest = LoginRequest.builder()
            .email("register@register.com")
            .password("password")
            .build();
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
            .email("update@update.com")
            .build();

        return Stream.of(
            dynamicTest("회원가입", () -> {
                String body = objectMapper.writeValueAsString(registerRequest);
                var response = post("/api/users", body, HttpStatus.SC_CREATED, UserResponse.class);
                assertThat(response.getEmail()).isEqualTo(registerRequest.getEmail());
                assertThat(response.getUsername()).isEqualTo(registerRequest.getUsername());
            }),
            dynamicTest("로그인", () -> {
                String body = objectMapper.writeValueAsString(loginRequest);
                var response = post("/api/users/login", body, HttpStatus.SC_OK, UserResponse.class);
                token = response.getToken();
                assertThat(token).isNotNull();
            }),
            dynamicTest("개인 정보 조회", () -> {
                var response = get("/api/user", token, HttpStatus.SC_OK, UserResponse.class);
                assertThat(response.getEmail()).isEqualTo(loginRequest.getEmail());
            }),
            dynamicTest("개인 정보 수정", () -> {
                String body = objectMapper.writeValueAsString(updateRequest);
                var response = put("/api/user", body, token, HttpStatus.SC_OK, UserResponse.class);
                assertThat(response.getEmail()).isEqualTo(updateRequest.getEmail());
                assertThat(response.getToken()).isNotEqualTo(token);
            })
        );
    }
}
