package com.jeeneee.realworld.article.domain;

import static com.jeeneee.realworld.user.domain.UserTest.EMAIL;
import static com.jeeneee.realworld.user.domain.UserTest.PASSWORD;
import static com.jeeneee.realworld.user.domain.UserTest.USERNAME;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jeeneee.realworld.article.exception.DuplicatedFavoriteException;
import com.jeeneee.realworld.common.exception.IllegalParameterException;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ArticleTest {

    public static final String ARTICLE_TITLE = "How to train your dragon";
    public static final String ARTICLE_SLUG = "how-to-train-your-dragon";
    public static final String ARTICLE_DESCRIPTION = "Ever wonder how?";
    public static final String ARTICLE_BODY = "You have to believe";

    private User author;
    private User user;
    private Article article;

    @BeforeEach
    void setUp() {
        author = User.create(EMAIL, USERNAME, PASSWORD);
        user = User.create("woojin7124@naver.com", "jerry", "12341234");
        article = Article.create(ARTICLE_TITLE, ARTICLE_DESCRIPTION, ARTICLE_BODY, author);
    }

    @DisplayName("게시글 생성(예외 발생)")
    @Test
    void createArticleWithoutInfo() {
        assertThrows(IllegalParameterException.class,
            () -> Article.create(ARTICLE_TITLE, ARTICLE_DESCRIPTION, ARTICLE_BODY, null));
        assertThrows(IllegalParameterException.class,
            () -> Article.create(ARTICLE_TITLE, ARTICLE_DESCRIPTION, null, author));
        assertThrows(IllegalParameterException.class,
            () -> Article.create(ARTICLE_TITLE, null, ARTICLE_BODY, author));
        assertThrows(IllegalParameterException.class,
            () -> Article.create(null, ARTICLE_DESCRIPTION, ARTICLE_BODY, author));
    }

    @DisplayName("게시글 생성")
    @Test
    void createArticle() {
        Article created = Article.create(ARTICLE_TITLE, ARTICLE_DESCRIPTION, ARTICLE_BODY, author);

        assertAll(
            () -> assertEquals(ARTICLE_SLUG, created.getSlug().getValue()),
            () -> assertEquals(ARTICLE_TITLE, created.getTitle()),
            () -> assertEquals(ARTICLE_DESCRIPTION, created.getDescription()),
            () -> assertEquals(ARTICLE_BODY, created.getBody()),
            () -> assertEquals(author, created.getAuthor())
        );
    }

    @DisplayName("게시글 수정")
    @Test
    void updateArticle() {
        // given
        String title = "Did you train your dragon";

        // when
        article.update(title, article.getDescription(), article.getBody());

        // then
        assertAll(
            () -> assertEquals(title, article.getTitle()),
            () -> assertEquals("did-you-train-your-dragon", article.getSlug().getValue())
        );
    }

    @DisplayName("찜한 게시글을 다시 찜하면 예외 발생")
    @Test
    void favoriteArticleTwice() {
        // given
        article.favorite(user);
        assertTrue(article.isFavoriteBy(user));

        // when & then
        assertThrows(DuplicatedFavoriteException.class, () -> article.favorite(user));
    }

    @DisplayName("찜하지 않은 게시글을 찜 취소하면 예외 발생")
    @Test
    void unfavoriteArticleTwice() {
        // given
        assertFalse(article.isFavoriteBy(user));

        // when & then
        assertThrows(UserNotFoundException.class, () -> article.unfavorite(user));
    }

    @DisplayName("게시글 좋아요")
    @Test
    void favoriteArticle() {
        // given
        assertTrue(article.getFavorites().isEmpty());
        assertFalse(article.isFavoriteBy(user));

        // when
        article.favorite(user);

        // then
        assertTrue(article.getFavorites().contains(user));
        assertTrue(article.isFavoriteBy(user));
    }

    @DisplayName("게시글 좋아요 취소")
    @Test
    void unfavoriteArticle() {
        // given
        article.getFavorites().add(user);
        assertTrue(article.isFavoriteBy(user));

        // when
        article.unfavorite(user);

        // then
        assertTrue(article.getFavorites().isEmpty());
        assertFalse(article.isFavoriteBy(user));
    }

    @DisplayName("게시글 좋아요 수")
    @Test
    void countFavorites() {
        // given
        assertTrue(article.getFavorites().isEmpty());
        assertEquals(0, article.favoriteCount());

        // when
        article.getFavorites().add(user);

        // then
        assertEquals(1, article.favoriteCount());
    }
}