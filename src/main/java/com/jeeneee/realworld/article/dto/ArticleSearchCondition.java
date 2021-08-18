package com.jeeneee.realworld.article.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleSearchCondition {

    private String tag;

    private String author;

    private String favorited;

    private int limit;

    private int offset;

    public ArticleSearchCondition(String tag, String author, String favorited, int limit,
        int offset) {
        this.tag = tag;
        this.author = author;
        this.favorited = favorited;
        this.limit = limit;
        this.offset = offset;
    }

    public ArticleSearchCondition(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }
}
