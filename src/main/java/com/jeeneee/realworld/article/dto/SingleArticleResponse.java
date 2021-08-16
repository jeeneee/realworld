package com.jeeneee.realworld.article.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.tag.domain.Tag;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.dto.ProfileResponse;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonRootName("article")
public class SingleArticleResponse {

    private String slug;

    private String title;

    private String description;

    private String body;

    private String[] tagList;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean favorited;

    private ProfileResponse author;

    public static SingleArticleResponse of(Article article, User user) {
        return SingleArticleResponse.builder()
            .slug(article.getSlugValue())
            .title(article.getTitle())
            .description(article.getDescription())
            .body(article.getBody())
            .tagList(article.getTags().stream().map(Tag::getName).toArray(String[]::new))
            .createdAt(article.getCreatedAt())
            .updatedAt(article.getUpdatedAt())
            .favorited(article.isFavoriteBy(user))
            .author(ProfileResponse.of(article.getAuthor(), user))
            .build();
    }
}
