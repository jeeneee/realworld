package com.jeeneee.realworld.comment.service;

import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_BODY;
import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_DESCRIPTION;
import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_SLUG;
import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_TITLE;
import static com.jeeneee.realworld.comment.domain.CommentTest.COMMENT_BODY;
import static com.jeeneee.realworld.user.domain.UserTest.EMAIL;
import static com.jeeneee.realworld.user.domain.UserTest.PASSWORD;
import static com.jeeneee.realworld.user.domain.UserTest.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.article.service.ArticleService;
import com.jeeneee.realworld.comment.domain.Comment;
import com.jeeneee.realworld.comment.domain.CommentRepository;
import com.jeeneee.realworld.comment.dto.CommentCreateRequest;
import com.jeeneee.realworld.comment.dto.MultipleCommentResponse;
import com.jeeneee.realworld.comment.dto.SingleCommentResponse.CommentInfo;
import com.jeeneee.realworld.comment.exception.CommentNotFoundException;
import com.jeeneee.realworld.common.exception.BadRequestException;
import com.jeeneee.realworld.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleService articleService;

    private CommentService commentService;

    private Comment comment;
    private User author;
    private Article article;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository, articleService);

        author = User.builder()
            .email(EMAIL)
            .username(USERNAME)
            .password(PASSWORD)
            .build();
        article = Article.builder()
            .title(ARTICLE_TITLE)
            .description(ARTICLE_DESCRIPTION)
            .body(ARTICLE_BODY)
            .author(author)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        comment = Comment.builder()
            .id(1L)
            .body(COMMENT_BODY)
            .author(author)
            .article(article)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @DisplayName("댓글 생성")
    @Test
    void create() {
        CommentCreateRequest request = new CommentCreateRequest(COMMENT_BODY);
        given(articleService.getArticle(any())).willReturn(article);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        CommentInfo commentInfo = commentService.create(ARTICLE_SLUG, request, author).getComment();

        assertThat(commentInfo.getBody()).isEqualTo(request.getBody());
    }

    @DisplayName("댓글 삭제 - 해당 댓글이 존재하지 않는 경우 예외 발생")
    @Test
    void delete_CommentNotExists_ExceptionThrown() {
        given(commentRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.delete(comment.getId(), author))
            .isInstanceOf(CommentNotFoundException.class);
    }

    @DisplayName("댓글 삭제 - 해당 댓글 작성자가 아닌 경우 예외 발생")
    @Test
    void delete_IsNotAuthor_ExceptionThrown() {
        User other = User.builder()
            .email("other@other.com")
            .username("other")
            .password("other_password")
            .build();
        given(commentRepository.findById(any())).willReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.delete(comment.getId(), other))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Only the author can fulfill this request.");
    }

    @DisplayName("댓글 전체 조회")
    @Test
    void findAll_Normal_Success() {
        given(commentRepository.findAllByArticle_Slug_Value(any())).willReturn(List.of(comment));

        MultipleCommentResponse response = commentService.findAll(article.getSlugValue(), author);

        assertThat(response.getComments()).hasSize(1);
    }
}