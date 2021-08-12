package com.jeeneee.realworld.article.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Slug {

    @Column(name = "slug", nullable = false, unique = true)
    private String value;

    public Slug(String value) {
        this.value = slugify(value);
    }

    public static Slug from(String title) {
        return new Slug(title);
    }

    private String slugify(String title) {
        String slug = title.trim().toLowerCase()
            .replaceAll("[\\uFE30-\\uFFA0\\s&’”?,.-]+", "-");
        if (slug.endsWith("-")) {
            slug = slug.substring(0, slug.length() - 1);
        }
        return slug;
    }
}
