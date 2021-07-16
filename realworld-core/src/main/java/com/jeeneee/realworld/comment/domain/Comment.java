package com.jeeneee.realworld.comment.domain;

import static javax.persistence.FetchType.LAZY;

import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.common.domain.BaseTimeEntity;
import com.jeeneee.realworld.common.exception.IllegalParameterException;
import com.jeeneee.realworld.user.domain.User;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "comments")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String body;

    @ManyToOne(fetch = LAZY)
    private User author;

    @ManyToOne(fetch = LAZY)
    private Article article;

    @Builder
    private Comment(Long id, String body, User author, Article article) {
        validateParams(body, author, article);
        this.id = id;
        this.body = body;
        this.author = author;
        this.article = article;
    }

    private void validateParams(String body, User author, Article article) {
        if (!StringUtils.hasText(body) || Objects.isNull(author) || Objects.isNull(article)) {
            throw new IllegalParameterException();
        }
    }

    public static Comment create(String body, User author, Article article) {
        return new Comment(null, body, author, article);
    }
}
