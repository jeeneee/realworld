package com.jeeneee.realworld.article.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonRootName("articles")
public class MultipleArticleResponse {

    private List<SingleArticleResponse> articles;

    private int articlesCount;

    public MultipleArticleResponse(List<SingleArticleResponse> articles) {
        this.articles = articles;
        this.articlesCount = articles.size();
    }
}
