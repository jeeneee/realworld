package com.jeeneee.realworld.comment.acceptance;

import static com.jeeneee.realworld.fixture.ArticleFixture.ARTICLE1;
import static com.jeeneee.realworld.fixture.UserFixture.USER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jeeneee.realworld.AcceptanceTest;
import com.jeeneee.realworld.comment.dto.CommentCreateRequest;
import com.jeeneee.realworld.comment.dto.MultipleCommentResponse;
import com.jeeneee.realworld.comment.dto.SingleCommentResponse;
import com.jeeneee.realworld.comment.dto.SingleCommentResponse.CommentInfo;
import java.util.List;
import java.util.stream.Stream;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class CommentAcceptanceTest extends AcceptanceTest {

    private final String uri = "/api/articles/" + ARTICLE1.getSlugValue() + "/comments";

    @DisplayName("댓글 관련 기능")
    @TestFactory
    Stream<DynamicTest> manageComment() throws JsonProcessingException {

        createArticle(ARTICLE1);

        return Stream.of(
            dynamicTest("댓글 생성", () -> {
                SingleCommentResponse response = createComment("comment1");
                CommentInfo comment = response.getComment();
                assertThat(comment.getAuthor().getUsername()).isEqualTo(USER1.getUsername());
                assertThat(comment.getBody()).isEqualTo("comment1");
            }),
            dynamicTest("게시글에 달린 모든 댓글 조회", () -> {
                createComment("comment2");
                createComment("comment3");
                MultipleCommentResponse response = get(uri, HttpStatus.SC_OK,
                    MultipleCommentResponse.class);
                List<CommentInfo> comments = response.getComments();
                assertThat(comments).hasSize(3);
            }),
            dynamicTest("댓글 삭제", () -> {
                delete(uri + "/3", token);
            })
        );
    }

    private SingleCommentResponse createComment(String commentBody) throws JsonProcessingException {
        CommentCreateRequest request = new CommentCreateRequest(commentBody);
        String body = objectMapper.writeValueAsString(request);
        return post(uri, body, token, HttpStatus.SC_CREATED, SingleCommentResponse.class);
    }
}
