package com.jeeneee.realworld.tag.domain;

import com.jeeneee.realworld.common.exception.IllegalParameterException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder
    private Tag(Long id, String name) {
        validateParams(name);
        this.id = id;
        this.name = name;
    }

    private void validateParams(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalParameterException();
        }
    }

    public static Tag create(String name) {
        return new Tag(null, name);
    }
}