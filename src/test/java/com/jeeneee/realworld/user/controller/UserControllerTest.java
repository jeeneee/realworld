package com.jeeneee.realworld.user.controller;

import static com.jeeneee.realworld.fixture.UserFixture.LOGIN_REQUEST;
import static com.jeeneee.realworld.fixture.UserFixture.REGISTER_REQUEST;
import static com.jeeneee.realworld.fixture.UserFixture.REGISTER_USER;
import static com.jeeneee.realworld.fixture.UserFixture.UPDATE_REQUEST;
import static com.jeeneee.realworld.fixture.UserFixture.USER1;
import static com.jeeneee.realworld.fixture.UserFixture.USER2;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jeeneee.realworld.ControllerTest;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.dto.LoginRequest;
import com.jeeneee.realworld.user.dto.RegisterRequest;
import com.jeeneee.realworld.user.dto.UpdateRequest;
import com.jeeneee.realworld.user.dto.UserResponse;
import com.jeeneee.realworld.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(UserController.class)
class UserControllerTest extends ControllerTest {

    @MockBean
    private UserService userService;

    @DisplayName("회원가입")
    @Test
    void register() throws Exception {
        String request = objectMapper.writeValueAsString(REGISTER_REQUEST);
        UserResponse response = UserResponse.of(REGISTER_USER);
        given(userService.register(any(RegisterRequest.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            post("/api/users")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
        );

        result.andExpect(status().isCreated())
            .andDo(
                document("user/register",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("user.username").type(JsonFieldType.STRING)
                            .description("유저명"),
                        fieldWithPath("user.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("user.password").type(JsonFieldType.STRING)
                            .description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("user.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("user.token").type(JsonFieldType.NULL).description("토큰"),
                        fieldWithPath("user.username").type(JsonFieldType.STRING)
                            .description("유저명"),
                        fieldWithPath("user.bio").type(JsonFieldType.NULL).description("자기소개"),
                        fieldWithPath("user.image").type(JsonFieldType.NULL).description("이미지")
                    )
                )
            );
    }

    @DisplayName("로그인")
    @Test
    void login() throws Exception {
        String request = objectMapper.writeValueAsString(LOGIN_REQUEST);
        UserResponse response = UserResponse.of(USER1);
        given(userService.login(any(LoginRequest.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            post("/api/users/login")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("user/login",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("user.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("user.password").type(JsonFieldType.STRING)
                            .description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("user.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("user.token").type(JsonFieldType.STRING).description("토큰"),
                        fieldWithPath("user.username").type(JsonFieldType.STRING)
                            .description("유저명"),
                        fieldWithPath("user.bio").type(JsonFieldType.STRING).description("자기소개"),
                        fieldWithPath("user.image").type(JsonFieldType.STRING).description("이미지")
                    )
                )
            );
    }

    @DisplayName("개인정보 조회")
    @Test
    void find() throws Exception {
        ResultActions result = mockMvc.perform(
            get("/api/user")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("user/find-my-info",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("토큰")
                    ),
                    responseFields(
                        fieldWithPath("user.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("user.token").type(JsonFieldType.STRING).description("토큰"),
                        fieldWithPath("user.username").type(JsonFieldType.STRING)
                            .description("유저명"),
                        fieldWithPath("user.bio").type(JsonFieldType.STRING).description("자기소개"),
                        fieldWithPath("user.image").type(JsonFieldType.STRING).description("이미지")
                    )
                )
            );
    }

    @DisplayName("개인정보 수정")
    @Test
    void update() throws Exception {
        String request = objectMapper.writeValueAsString(UPDATE_REQUEST);
        UserResponse response = UserResponse.of(USER2);
        given(userService.update(any(UpdateRequest.class), any(User.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            put("/api/user")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                .content(request)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("user/update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("토큰")
                    ),
                    requestFields(
                        fieldWithPath("user.username").type(JsonFieldType.STRING).description("유저명").optional(),
                        fieldWithPath("user.email").type(JsonFieldType.STRING).description("이메일").optional(),
                        fieldWithPath("user.password").type(JsonFieldType.STRING).description("비밀번호").optional(),
                        fieldWithPath("user.bio").type(JsonFieldType.STRING).description("자기소개").optional(),
                        fieldWithPath("user.image").type(JsonFieldType.STRING).description("이미지").optional()
                    ),
                    responseFields(
                        fieldWithPath("user.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("user.token").type(JsonFieldType.STRING).description("토큰"),
                        fieldWithPath("user.username").type(JsonFieldType.STRING).description("유저명"),
                        fieldWithPath("user.bio").type(JsonFieldType.STRING).description("자기소개"),
                        fieldWithPath("user.image").type(JsonFieldType.STRING).description("이미지")
                    )
                )
            );
    }
}