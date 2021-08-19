package com.jeeneee.realworld.comment.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.comment.domain.Comment;
import com.jeeneee.realworld.user.domain.User;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@JsonTypeName("comment")
public class CommentCreateRequest {

    @NotBlank(message = "body cannot be empty")
    private String body;

    public Comment toEntity(Article article, User author) {
        return Comment.builder()
            .body(body)
            .article(article)
            .author(author)
            .build();
    }
}

