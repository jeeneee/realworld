package com.jeeneee.realworld.comment.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonRootName("comments")
public class MultipleCommentResponse {

    @JsonValue
    private List<SingleCommentResponse> comments;

    public MultipleCommentResponse(List<SingleCommentResponse> comments) {
        this.comments = comments;
    }
}
