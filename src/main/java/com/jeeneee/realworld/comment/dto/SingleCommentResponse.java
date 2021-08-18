package com.jeeneee.realworld.comment.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.jeeneee.realworld.comment.domain.Comment;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.dto.ProfileResponse;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonRootName("comment")
public class SingleCommentResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String body;
    private ProfileResponse author;

    public static SingleCommentResponse of(Comment comment, User user) {
        return SingleCommentResponse.builder()
            .id(comment.getId())
            .createdAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
            .body(comment.getBody())
            .author(ProfileResponse.of(comment.getAuthor(), user))
            .build();
    }
}
