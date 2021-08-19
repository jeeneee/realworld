package com.jeeneee.realworld.article.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.tag.domain.Tag;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.dto.ProfileResponse.ProfileInfo;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SingleArticleResponse {

    private ArticleInfo article;

    public static SingleArticleResponse of(Article article, User user) {
        return new SingleArticleResponse(ArticleInfo.of(article, user));
    }

    @Getter
    @Builder
    public static class ArticleInfo {

        private final String slug;
        private final String title;
        private final String description;
        private final String body;
        private final String[] tagList;
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
        private final LocalDateTime createdAt;
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
        private final LocalDateTime updatedAt;
        private final boolean favorited;
        private final int favoritesCount;
        private final ProfileInfo author;

        public static ArticleInfo of(Article article, User user) {
            return ArticleInfo.builder()
                .slug(article.getSlugValue())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .tagList(article.getTags().stream().map(Tag::getName).toArray(String[]::new))
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .favorited(article.isFavoriteBy(user))
                .favoritesCount(article.favoriteCount())
                .author(ProfileInfo.of(article.getAuthor(), user))
                .build();
        }
    }
}
