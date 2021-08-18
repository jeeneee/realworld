package com.jeeneee.realworld.comment.domain;

import static javax.persistence.FetchType.LAZY;

import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.common.domain.BaseTimeEntity;
import com.jeeneee.realworld.common.exception.IllegalParameterException;
import com.jeeneee.realworld.user.domain.User;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String body;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder
    private Comment(Long id, String body, User author, Article article, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
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
}
