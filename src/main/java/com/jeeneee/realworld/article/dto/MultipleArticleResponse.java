package com.jeeneee.realworld.article.dto;

import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.article.dto.SingleArticleResponse.ArticleInfo;
import com.jeeneee.realworld.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultipleArticleResponse {

    private List<ArticleInfo> articles;
    private int articlesCount;

    public MultipleArticleResponse(List<ArticleInfo> articles) {
        this.articles = articles;
        this.articlesCount = articles.size();
    }

    public static MultipleArticleResponse of(List<Article> articles, User user) {
        return new MultipleArticleResponse(articles.stream()
            .map(article -> ArticleInfo.of(article, user))
            .collect(Collectors.toList()));
    }
}
