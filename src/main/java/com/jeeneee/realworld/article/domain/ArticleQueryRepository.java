package com.jeeneee.realworld.article.domain;

import static com.jeeneee.realworld.article.domain.QArticle.article;
import static com.jeeneee.realworld.tag.domain.QTag.tag;
import static com.jeeneee.realworld.user.domain.QUser.user;

import com.jeeneee.realworld.article.dto.ArticleSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ArticleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Article> findAll(ArticleSearchCondition condition) {
        return queryFactory.selectFrom(article)
            .leftJoin(article.author, user)
            .leftJoin(article.favorites, user)
            .leftJoin(article.tags, tag)
            .where(
                eqAuthor(condition.getAuthor()),
                eqFavorite(condition.getFavorited()),
                eqTag(condition.getTag())
            )
            .limit(condition.getLimit())
            .offset(condition.getOffset())
            .orderBy(article.createdAt.desc())
            .fetch();
    }

    private BooleanExpression eqAuthor(String author) {
        return StringUtils.isBlank(author) ? null : article.author.username.eq(author);
    }

    private BooleanExpression eqFavorite(String favorited) {
        return StringUtils.isBlank(favorited) ? null : user.username.eq(favorited);
    }

    private BooleanExpression eqTag(String tagName) {
        return StringUtils.isBlank(tagName) ? null : tag.name.eq(tagName);
    }
}
