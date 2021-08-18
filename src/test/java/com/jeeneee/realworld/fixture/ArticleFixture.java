package com.jeeneee.realworld.fixture;

import static com.jeeneee.realworld.fixture.TagFixture.TAG1;
import static com.jeeneee.realworld.fixture.TagFixture.TAG2;
import static com.jeeneee.realworld.fixture.TagFixture.TAG3;
import static com.jeeneee.realworld.fixture.UserFixture.USER1;

import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.article.dto.ArticleCreateRequest;
import com.jeeneee.realworld.article.dto.ArticleUpdateRequest;
import java.time.LocalDateTime;
import java.util.List;

public class ArticleFixture {

    public static Article ARTICLE1 = Article.builder()
        .title("How to train your dragon")
        .description("Ever wonder how?")
        .body("You have to believe")
        .author(USER1)
        .createdAt(LocalDateTime.of(2021, 8, 16, 15, 10))
        .updatedAt(LocalDateTime.of(2021, 8, 16, 15, 10))
        .build();

    public static Article ARTICLE2 = Article.builder()
        .title("Did you train your dragon?")
        .description("Ever wonder why?")
        .body("you must believe")
        .author(USER1)
        .createdAt(LocalDateTime.of(2021, 8, 16, 15, 10))
        .updatedAt(LocalDateTime.of(2021, 8, 16, 15, 10))
        .build();

    static {
        ARTICLE1.addTag(TAG1);
        ARTICLE1.addTag(TAG2);
        ARTICLE2.addTag(TAG2);
        ARTICLE2.addTag(TAG3);
    }

    public static final ArticleCreateRequest CREATE_REQUEST = ArticleCreateRequest.builder()
        .title(ARTICLE1.getTitle())
        .description(ARTICLE1.getDescription())
        .body(ARTICLE1.getBody())
        .tagList(List.of(TAG1.getName(), TAG2.getName()))
        .build();

    public static final ArticleUpdateRequest UPDATE_REQUEST = ArticleUpdateRequest.builder()
        .title(ARTICLE2.getTitle())
        .description(ARTICLE2.getDescription())
        .body(ARTICLE2.getBody())
        .build();
}
