package com.jeeneee.realworld.tag.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultipleTagResponse {

    private List<String> tags;

    public MultipleTagResponse(List<String> tags) {
        this.tags = tags;
    }
}
