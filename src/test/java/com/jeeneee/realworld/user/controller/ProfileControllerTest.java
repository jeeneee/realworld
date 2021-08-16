package com.jeeneee.realworld.user.controller;

import static com.jeeneee.realworld.fixture.UserFixture.USER2;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jeeneee.realworld.ControllerTest;
import com.jeeneee.realworld.descriptor.ProfileFieldDescriptor;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.dto.ProfileResponse;
import com.jeeneee.realworld.user.service.ProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest extends ControllerTest {

    @MockBean
    private ProfileService profileService;

    @DisplayName("회원 프로필 조회")
    @Test
    void getProfile() throws Exception {
        ProfileResponse response = ProfileResponse.of(USER2, false);
        given(profileService.find(any(), any(User.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            get("/api/profiles/{username}", USER2.getUsername())
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("profile/find-user",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("토큰").optional()
                    ),
                    responseFields(
                        fieldWithPath("profile").type(JsonFieldType.OBJECT).description("프로필")
                    ).andWithPrefix("profile.", ProfileFieldDescriptor.profile)
                )
            );
    }

    @DisplayName("팔로우")
    @Test
    void follow() throws Exception {
        ProfileResponse response = ProfileResponse.of(USER2, true);
        given(profileService.follow(any(), any(User.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            post("/api/profiles/{username}/follow", USER2.getUsername())
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("profile/follow",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("토큰")
                    ),
                    responseFields(
                        fieldWithPath("profile").type(JsonFieldType.OBJECT).description("프로필")
                    ).andWithPrefix("profile.", ProfileFieldDescriptor.profile)
                )
            );
    }

    @DisplayName("언팔로우")
    @Test
    void unfollow() throws Exception {
        ProfileResponse response = ProfileResponse.of(USER2, false);
        given(profileService.unfollow(any(), any(User.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            delete("/api/profiles/{username}/follow", USER2.getUsername())
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("profile/unfollow",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("토큰")
                    ),
                    responseFields(
                        fieldWithPath("profile").type(JsonFieldType.OBJECT).description("프로필")
                    ).andWithPrefix("profile.", ProfileFieldDescriptor.profile)
                )
            );
    }
}