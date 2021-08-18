package com.jeeneee.realworld.tag.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonRootName("tags")
public class MultipleTagResponse {

    @JsonValue
    private List<String> tags;

    public MultipleTagResponse(List<String> tags) {
        this.tags = tags;
    }
}
