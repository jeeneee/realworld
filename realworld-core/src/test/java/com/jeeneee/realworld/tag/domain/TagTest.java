package com.jeeneee.realworld.tag.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jeeneee.realworld.common.exception.IllegalParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TagTest {

    @DisplayName("태그 생성(예외 발생)")
    @Test
    void createTagWithoutInfo() {
        assertThrows(IllegalParameterException.class, () -> Tag.create(null));
    }

    @DisplayName("태그 생성")
    @Test
    void createTag() {
        // given
        String tagName = "reactjs";

        // when
        Tag tag = Tag.create(tagName);

        // then
        assertEquals(tagName, tag.getName());
    }
}