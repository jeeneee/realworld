package com.jeeneee.realworld.article.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.user.domain.User;
import java.util.List;
import javax.validation.constraints.NotBlank;
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
public class ArticleCreateRequest {

    @NotBlank(message = "title cannot be empty")
    private String title;

    @NotBlank(message = "description cannot be empty")
    private String description;

    @NotBlank(message = "body cannot be empty")
    private String body;

    private List<String> tagList;

    public Article toEntity(User author) {
        return Article.builder()
            .title(title)
            .description(description)
            .body(body)
            .author(author)
            .build();
    }
}
