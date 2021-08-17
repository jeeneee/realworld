package com.jeeneee.realworld.article.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonRootName("articles")
public class MultipleArticleResponse {

    @JsonValue
    private List<SingleArticleResponse> articles;

    public MultipleArticleResponse(List<SingleArticleResponse> articles) {
        this.articles = articles;
    }
}
