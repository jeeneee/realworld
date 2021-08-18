package com.jeeneee.realworld.comment.controller;

import com.jeeneee.realworld.comment.dto.CommentCreateRequest;
import com.jeeneee.realworld.comment.dto.MultipleCommentResponse;
import com.jeeneee.realworld.comment.dto.SingleCommentResponse;
import com.jeeneee.realworld.comment.service.CommentService;
import com.jeeneee.realworld.infra.security.LoginUser;
import com.jeeneee.realworld.infra.security.OptionalUser;
import com.jeeneee.realworld.user.domain.User;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/articles/{slug}/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<SingleCommentResponse> create(@PathVariable String slug,
        @Valid @RequestBody CommentCreateRequest request, @LoginUser User author) {
        SingleCommentResponse response = commentService.create(slug, request, author);
        URI uri = URI.create("/api/articles/" + slug + "/comments");
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SingleCommentResponse> delete(@PathVariable String slug,
        @PathVariable Long id, @LoginUser User user) {
        commentService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<MultipleCommentResponse> findAll(@PathVariable String slug,
        @OptionalUser User user) {
        return ResponseEntity.ok().body(commentService.findAll(slug, user));
    }
}
