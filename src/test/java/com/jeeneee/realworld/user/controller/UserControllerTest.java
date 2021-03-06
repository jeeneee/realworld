package com.jeeneee.realworld.user.controller;

import static com.jeeneee.realworld.fixture.UserFixture.LOGIN_REQUEST;
import static com.jeeneee.realworld.fixture.UserFixture.REGISTER_REQUEST;
import static com.jeeneee.realworld.fixture.UserFixture.REGISTER_USER;
import static com.jeeneee.realworld.fixture.UserFixture.UPDATE_REQUEST;
import static com.jeeneee.realworld.fixture.UserFixture.USER1;
import static com.jeeneee.realworld.fixture.UserFixture.USER2;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jeeneee.realworld.ControllerTest;
import com.jeeneee.realworld.descriptor.UserFieldDescriptor;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.dto.LoginRequest;
import com.jeeneee.realworld.user.dto.RegisterRequest;
import com.jeeneee.realworld.user.dto.UserResponse;
import com.jeeneee.realworld.user.dto.UserUpdateRequest;
import com.jeeneee.realworld.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(UserController.class)
class UserControllerTest extends ControllerTest {

    @MockBean
    private UserService userService;

    @DisplayName("????????????")
    @Test
    void register() throws Exception {
        String request = objectMapper.writeValueAsString(REGISTER_REQUEST);
        UserResponse response = UserResponse.of(REGISTER_USER);
        given(userService.register(any(RegisterRequest.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            post("/api/users")
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(request)
        );

        result.andExpect(status().isCreated())
            .andDo(
                document("user/register",
                    requestFields(
                        fieldWithPath("user.username").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("user.email").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("user.password").type(JsonFieldType.STRING).description("????????????")
                    ),
                    responseFields(
                        fieldWithPath("user").type(JsonFieldType.OBJECT).description("?????? ??????")
                    ).andWithPrefix("user.", UserFieldDescriptor.registered_user)
                )
            );
    }

    @DisplayName("?????????")
    @Test
    void login() throws Exception {
        String request = objectMapper.writeValueAsString(LOGIN_REQUEST);
        UserResponse response = UserResponse.of(USER1);
        given(userService.login(any(LoginRequest.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            post("/api/users/login")
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(request)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("user/login",
                    requestFields(
                        fieldWithPath("user.email").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("user.password").type(JsonFieldType.STRING).description("????????????")
                    ),
                    responseFields(
                        fieldWithPath("user").type(JsonFieldType.OBJECT).description("?????? ??????")
                    ).andWithPrefix("user.", UserFieldDescriptor.user)
                )
            );
    }

    @DisplayName("???????????? ??????")
    @Test
    void find() throws Exception {
        ResultActions result = mockMvc.perform(
            get("/api/user")
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("user/find-my-info",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("??????")
                    ),
                    responseFields(
                        fieldWithPath("user").type(JsonFieldType.OBJECT).description("?????? ??????")
                    ).andWithPrefix("user.", UserFieldDescriptor.user)
                )
            );
    }

    @DisplayName("???????????? ??????")
    @Test
    void update() throws Exception {
        String request = objectMapper.writeValueAsString(UPDATE_REQUEST);
        UserResponse response = UserResponse.of(USER2);
        given(userService.update(any(UserUpdateRequest.class), any(User.class)))
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            put("/api/user")
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                .content(request)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("user/update",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("??????")
                    ),
                    requestFields(
                        fieldWithPath("user.username").type(JsonFieldType.STRING).description("?????????").optional(),
                        fieldWithPath("user.email").type(JsonFieldType.STRING).description("?????????").optional(),
                        fieldWithPath("user.password").type(JsonFieldType.STRING).description("????????????").optional(),
                        fieldWithPath("user.bio").type(JsonFieldType.STRING).description("????????????").optional(),
                        fieldWithPath("user.image").type(JsonFieldType.STRING).description("?????????").optional()
                    ),
                    responseFields(
                        fieldWithPath("user").type(JsonFieldType.OBJECT).description("?????? ??????")
                    ).andWithPrefix("user.", UserFieldDescriptor.user)
                )
            );
    }
}