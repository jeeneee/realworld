package com.jeeneee.realworld.comment.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeneee.realworld.comment.domain.Comment;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.dto.ProfileResponse.ProfileInfo;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SingleCommentResponse {

    private CommentInfo comment;

    public static SingleCommentResponse of(Comment comment, User user) {
        return new SingleCommentResponse(CommentInfo.of(comment, user));
    }

    @Getter
    @Builder
    public static class CommentInfo {

        private final Long id;
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
        private final LocalDateTime createdAt;
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
        private final LocalDateTime updatedAt;
        private final String body;
        private final ProfileInfo author;

        public static CommentInfo of(Comment comment, User user) {
            return CommentInfo.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .body(comment.getBody())
                .author(ProfileInfo.of(comment.getAuthor(), user))
                .build();
        }
    }
}
