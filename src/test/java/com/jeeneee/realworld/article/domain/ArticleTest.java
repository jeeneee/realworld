package com.jeeneee.realworld.article.domain;

import static com.jeeneee.realworld.user.domain.UserTest.EMAIL;
import static com.jeeneee.realworld.user.domain.UserTest.PASSWORD;
import static com.jeeneee.realworld.user.domain.UserTest.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jeeneee.realworld.article.exception.DuplicateFavoriteException;
import com.jeeneee.realworld.common.exception.IllegalParameterException;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.exception.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ArticleTest {

    public static final String ARTICLE_TITLE = "How to train your dragon";
    public static final String ARTICLE_SLUG = "how-to-train-your-dragon";
    public static final String ARTICLE_DESCRIPTION = "Ever wonder how?";
    public static final String ARTICLE_BODY = "You have to believe";

    private Article article;
    private User author;

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

    @DisplayName("게시글 생성 - 제목이 없으면 예외 발생")
    @Test
    void createArticleWithoutTitle() {
        assertThatThrownBy(
            () -> Article.builder()
                .description(ARTICLE_DESCRIPTION)
                .body(ARTICLE_BODY)
                .author(author)
                .build())
            .isInstanceOf(IllegalParameterException.class);
    }

    @DisplayName("게시글 생성 - 요약이 없으면 예외 발생")
    @Test
    void createArticleWithoutDescription() {
        assertThatThrownBy(
            () -> Article.builder()
                .title(ARTICLE_TITLE)
                .body(ARTICLE_BODY)
                .author(author)
                .build())
            .isInstanceOf(IllegalParameterException.class);
    }

    @DisplayName("게시글 생성 - 내용이 없으면 예외 발생")
    @Test
    void createArticleWithoutBody() {
        assertThatThrownBy(
            () -> Article.builder()
                .title(ARTICLE_TITLE)
                .description(ARTICLE_DESCRIPTION)
                .author(author)
                .build())
            .isInstanceOf(IllegalParameterException.class);
    }

    @DisplayName("게시글 생성 - 작성자이 없으면 예외 발생")
    @Test
    void createArticleWithoutAuthor() {
        assertThatThrownBy(
            () -> Article.builder()
                .title(ARTICLE_TITLE)
                .description(ARTICLE_DESCRIPTION)
                .body(ARTICLE_BODY)
                .build())
            .isInstanceOf(IllegalParameterException.class);
    }

    @DisplayName("게시글 생성")
    @Test
    void createArticle() {
        Article created = Article.builder()
            .title(ARTICLE_TITLE)
            .description(ARTICLE_DESCRIPTION)
            .body(ARTICLE_BODY)
            .author(author)
            .build();

        assertThat(created.getSlugValue()).isEqualTo(ARTICLE_SLUG);
        assertThat(created.getTitle()).isEqualTo(ARTICLE_TITLE);
        assertThat(created.getDescription()).isEqualTo(ARTICLE_DESCRIPTION);
        assertThat(created.getBody()).isEqualTo(ARTICLE_BODY);
        assertThat(created.getAuthor()).isEqualTo(author);
    }

    @DisplayName("게시글 수정")
    @Test
    void updateArticle() {
        String title = "Did you train your dragon";

        article.update(title, article.getDescription(), article.getBody());

        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getSlugValue()).isEqualTo("did-you-train-your-dragon");
    }

    @DisplayName("찜한 게시글을 다시 찜하면 예외 발생")
    @Test
    void favoriteArticleTwice() {
        article.favorite(author);

        assertThatThrownBy(() -> article.favorite(author))
            .isInstanceOf(DuplicateFavoriteException.class);
    }

    @DisplayName("찜하지 않은 게시글을 찜 취소하면 예외 발생")
    @Test
    void unfavoriteArticleTwice() {
        assertThatThrownBy(() -> article.unfavorite(author))
            .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("게시글 좋아요")
    @Test
    void favoriteArticle() {
        article.favorite(author);

        Assertions.assertThat(article.getFavorites()).contains(author);
        assertThat(article.isFavoriteBy(author)).isTrue();
    }

    @DisplayName("게시글 좋아요 취소")
    @Test
    void unfavoriteArticle() {
        article.getFavorites().add(author);

        article.unfavorite(author);

        Assertions.assertThat(article.getFavorites()).isEmpty();
        assertThat(article.isFavoriteBy(author)).isFalse();
    }

    @DisplayName("게시글 좋아요 수")
    @Test
    void countFavorites() {
        article.getFavorites().add(author);

        assertThat(article.favoriteCount()).isEqualTo(1);
    }
}