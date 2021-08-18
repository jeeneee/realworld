package com.jeeneee.realworld.comment.controller;

import static com.jeeneee.realworld.fixture.ArticleFixture.ARTICLE1;
import static com.jeeneee.realworld.fixture.CommentFixture.COMMENT1;
import static com.jeeneee.realworld.fixture.CommentFixture.COMMENT2;
import static com.jeeneee.realworld.fixture.CommentFixture.CREATE_REQUEST;
import static com.jeeneee.realworld.fixture.UserFixture.USER1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jeeneee.realworld.ControllerTest;
import com.jeeneee.realworld.comment.dto.CommentCreateRequest;
import com.jeeneee.realworld.comment.dto.MultipleCommentResponse;
import com.jeeneee.realworld.comment.dto.SingleCommentResponse;
import com.jeeneee.realworld.comment.service.CommentService;
import com.jeeneee.realworld.descriptor.CommentFieldDescriptor;
import com.jeeneee.realworld.descriptor.ProfileFieldDescriptor;
import com.jeeneee.realworld.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends ControllerTest {

    @MockBean
    private CommentService commentService;

    @DisplayName("댓글 생성")
    @Test
    void create() throws Exception {
        String request = objectMapper.writeValueAsString(CREATE_REQUEST);
        SingleCommentResponse response = SingleCommentResponse.of(COMMENT1, USER1);
        given(commentService.create(any(), any(CommentCreateRequest.class), any(User.class)))
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            post("/api/articles/{slug}/comments", ARTICLE1.getSlugValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                .content(request)
        );

        result.andExpect(status().isCreated())
            .andDo(
                document("comment/create",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("토큰")
                    ),
                    requestFields(
                        fieldWithPath("comment.body").type(JsonFieldType.STRING).description("내용")
                    ),
                    responseFields(
                        fieldWithPath("comment").type(JsonFieldType.OBJECT).description("댓글")
                    ).andWithPrefix("comment.", CommentFieldDescriptor.comment)
                        .andWithPrefix("comment.author.", ProfileFieldDescriptor.profile)
                )
            );
    }

    @DisplayName("댓글 삭제")
    @Test
    void deleteComment() throws Exception {
        doNothing().when(commentService).delete(anyLong(), any(User.class));

        ResultActions result = mockMvc.perform(
            delete("/api/articles/{slug}/comments/{id}", ARTICLE1.getSlugValue(), COMMENT1.getId())
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
        );

        result.andExpect(status().isNoContent())
            .andDo(
                document("comment/delete",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("토큰")
                    )
                )
            );
    }

    @DisplayName("댓글 전체 조회")
    @Test
    void findAll() throws Exception {
        List<SingleCommentResponse> list = List.of(SingleCommentResponse.of(COMMENT1, USER1),
            SingleCommentResponse.of(COMMENT2, USER1));
        MultipleCommentResponse response = new MultipleCommentResponse(list);
        given(commentService.findAll(any(), any(User.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            get("/api/articles/{slug}/comments", ARTICLE1.getSlugValue())
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("comment/find-all",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("토큰").optional()
                    ),
                    responseFields(
                        fieldWithPath("comments").type(JsonFieldType.ARRAY).description("댓글 목록")
                    ).andWithPrefix("comments[].", CommentFieldDescriptor.comment)
                        .andWithPrefix("comments[].author.", ProfileFieldDescriptor.profile)
                )
            );
    }
}