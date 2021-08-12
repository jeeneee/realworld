package com.jeeneee.realworld.tag.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jeeneee.realworld.common.exception.IllegalParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TagTest {

    @DisplayName("태그 생성(예외 발생)")
    @Test
    void createTagWithoutInfo() {
        assertThatThrownBy(() -> Tag.create(null)).isInstanceOf(IllegalParameterException.class);
    }

    @DisplayName("태그 생성")
    @Test
    void createTag() {
        String tagName = "reactjs";

        Tag tag = Tag.create(tagName);

        assertThat(tag.getName()).isEqualTo(tagName);
    }
}