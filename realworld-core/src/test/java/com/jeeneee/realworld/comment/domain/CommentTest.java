package com.jeeneee.realworld.comment.domain;

import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_BODY;
import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_DESCRIPTION;
import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_TITLE;
import static com.jeeneee.realworld.user.domain.UserTest.EMAIL;
import static com.jeeneee.realworld.user.domain.UserTest.PASSWORD;
import static com.jeeneee.realworld.user.domain.UserTest.USERNAME;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        author = User.create(EMAIL, USERNAME, PASSWORD);
        article = Article.create(ARTICLE_TITLE, ARTICLE_DESCRIPTION, ARTICLE_BODY, author);
    }

    @DisplayName("댓글 생성(예외 발생)")
    @Test
    void createCommentWithoutInfo() {
        assertThrows(
            IllegalParameterException.class, () -> Comment.create(COMMENT_BODY, author, null));
        assertThrows(IllegalParameterException.class,
            () -> Comment.create(COMMENT_BODY, null, article));
        assertThrows(IllegalParameterException.class, () -> Comment.create(null, author, article));
    }

    @DisplayName("댓글 생성")
    @Test
    void createComment() {
        Comment comment = Comment.create(COMMENT_BODY, author, article);

        assertAll(
            () -> assertEquals(COMMENT_BODY, comment.getBody()),
            () -> assertEquals(author, comment.getAuthor()),
            () -> assertEquals(article, comment.getArticle())
        );
    }
}