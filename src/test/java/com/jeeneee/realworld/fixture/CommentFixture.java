package com.jeeneee.realworld.fixture;

import static com.jeeneee.realworld.fixture.ArticleFixture.ARTICLE1;
import static com.jeeneee.realworld.fixture.UserFixture.USER1;
import static com.jeeneee.realworld.fixture.UserFixture.USER2;

import com.jeeneee.realworld.comment.domain.Comment;
import com.jeeneee.realworld.comment.dto.CommentCreateRequest;
import java.time.LocalDateTime;

public class CommentFixture {

    public static final Comment COMMENT1 = Comment.builder()
        .id(1L)
        .body("His name was my name too.")
        .article(ARTICLE1)
        .author(USER1)
        .createdAt(LocalDateTime.of(2021, 8, 16, 15, 10))
        .updatedAt(LocalDateTime.of(2021, 8, 16, 15, 10))
        .build();

    public static final Comment COMMENT2 = Comment.builder()
        .id(2L)
        .body("Her name was my name too.")
        .article(ARTICLE1)
        .author(USER2)
        .createdAt(LocalDateTime.of(2021, 8, 16, 15, 10))
        .updatedAt(LocalDateTime.of(2021, 8, 16, 15, 10))
        .build();

    public static final CommentCreateRequest CREATE_REQUEST = new CommentCreateRequest(
        COMMENT1.getBody());
}
