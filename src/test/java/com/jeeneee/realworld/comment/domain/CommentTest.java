package com.jeeneee.realworld.comment.domain;

import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_BODY;
import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_DESCRIPTION;
import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_TITLE;
import static com.jeeneee.realworld.user.domain.UserTest.EMAIL;
import static com.jeeneee.realworld.user.domain.UserTest.PASSWORD;
import static com.jeeneee.realworld.user.domain.UserTest.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.common.exception.IllegalParameterException;
import com.jeeneee.realworld.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommentTest {

    public static final String COMMENT_BODY = "Privilege roster tariff okay.";

    private User author;
    private Article article;

    @BeforeEach
    void setUp() {
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
            .build();
    }

    @DisplayName("댓글 생성 - 내용이 없으면 예외 발생")
    @Test
    void createCommentWithoutBody() {
        assertThatThrownBy(() -> Comment.builder()
            .article(article)
            .author(author)
            .build())
            .isInstanceOf(IllegalParameterException.class);
    }

    @DisplayName("댓글 생성 - 해당 게시글이 없으면 예외 발생")
    @Test
    void createCommentWithoutArticle() {
        assertThatThrownBy(() -> Comment.builder()
            .body(COMMENT_BODY)
            .author(author)
            .build())
            .isInstanceOf(IllegalParameterException.class);
    }

    @DisplayName("댓글 생성 - 작성자가 없으면 예외 발생")
    @Test
    void createCommentWithoutAuthor() {
        assertThatThrownBy(() -> Comment.builder()
            .body(COMMENT_BODY)
            .article(article)
            .build())
            .isInstanceOf(IllegalParameterException.class);
    }

    @DisplayName("댓글 생성")
    @Test
    void createComment() {
        Comment comment = Comment.builder()
            .body(COMMENT_BODY)
            .article(article)
            .author(author)
            .build();

        assertThat(comment.getBody()).isEqualTo(COMMENT_BODY);
        assertThat(comment.getAuthor()).isEqualTo(author);
        assertThat(comment.getArticle()).isEqualTo(article);
    }
}