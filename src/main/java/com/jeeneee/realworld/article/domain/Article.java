package com.jeeneee.realworld.article.domain;

import static javax.persistence.FetchType.LAZY;

import com.jeeneee.realworld.article.exception.DuplicateFavoriteException;
import com.jeeneee.realworld.common.domain.BaseTimeEntity;
import com.jeeneee.realworld.common.exception.IllegalParameterException;
import com.jeeneee.realworld.tag.domain.Tag;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "articles")
@Entity
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Embedded
    private Slug slug;

    @Column(nullable = false)
    private String description;

    @Lob
    @Column(nullable = false)
    private String body;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToMany
    @JoinTable(name = "article_tags",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private final List<Tag> tags = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "article_favorites",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private final List<User> favorites = new ArrayList<>();

    @Builder
    private Article(Long id, String title, String description, String body, User author) {
        validateParams(title, description, body, author);
        this.id = id;
        this.title = title;
        this.slug = Slug.from(title);
        this.description = description;
        this.body = body;
        this.author = author;
    }

    private void validateParams(String title, String description, String body, User author) {
        if (!StringUtils.hasText(title) || !StringUtils.hasText(description) ||
            !StringUtils.hasText(body) || Objects.isNull(author)) {
            throw new IllegalParameterException();
        }
    }

    public void update(String title, String description, String body) {
        validateParams(title, description, body, this.author);
        this.title = title;
        this.slug = Slug.from(title);
        this.description = description;
        this.body = body;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public void favorite(User user) {
        if (this.isFavoriteBy(user)) {
            throw new DuplicateFavoriteException("The article has already been liked.");
        }
        favorites.add(user);
    }

    public void unfavorite(User user) {
        if (!this.isFavoriteBy(user)) {
            throw new UserNotFoundException();
        }
        favorites.remove(user);
    }

    public boolean isFavoriteBy(User user) {
        return favorites.contains(user);
    }

    public int favoriteCount() {
        return favorites.size();
    }
}
