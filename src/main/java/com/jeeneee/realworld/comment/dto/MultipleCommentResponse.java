package com.jeeneee.realworld.comment.dto;

import com.jeeneee.realworld.comment.domain.Comment;
import com.jeeneee.realworld.comment.dto.SingleCommentResponse.CommentInfo;
import com.jeeneee.realworld.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultipleCommentResponse {

    private List<CommentInfo> comments;

    public MultipleCommentResponse(List<CommentInfo> comments) {
        this.comments = comments;
    }

    public static MultipleCommentResponse of(List<Comment> comments, User user) {
        return new MultipleCommentResponse(comments.stream()
            .map(comment -> CommentInfo.of(comment, user))
            .collect(Collectors.toList()));
    }
}
