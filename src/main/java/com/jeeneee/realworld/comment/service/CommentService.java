package com.jeeneee.realworld.comment.service;

import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.article.service.ArticleService;
import com.jeeneee.realworld.comment.domain.Comment;
import com.jeeneee.realworld.comment.domain.CommentRepository;
import com.jeeneee.realworld.comment.dto.CommentCreateRequest;
import com.jeeneee.realworld.comment.dto.MultipleCommentResponse;
import com.jeeneee.realworld.comment.dto.SingleCommentResponse;
import com.jeeneee.realworld.comment.exception.CommentNotFoundException;
import com.jeeneee.realworld.common.exception.BadRequestException;
import com.jeeneee.realworld.user.domain.User;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleService articleService;

    @Transactional
    public SingleCommentResponse create(String slug, CommentCreateRequest request, User author) {
        Article article = articleService.getArticle(slug);
        Comment comment = request.toEntity(article, author);
        return SingleCommentResponse.of(commentRepository.save(comment), author);
    }

    @Transactional
    public void delete(Long commentId, User user) {
        Comment comment = getComment(commentId);
        verifyAuthor(comment, user);
        commentRepository.delete(comment);
    }

    public MultipleCommentResponse findAll(String slug, User user) {
        return new MultipleCommentResponse(
            commentRepository.findAllByArticle_Slug_Value(slug).stream()
                .map(comment -> SingleCommentResponse.of(comment, user))
                .collect(Collectors.toList()));
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(CommentNotFoundException::new);
    }

    private void verifyAuthor(Comment comment, User user) {
        if (comment.getAuthor() != user) {
            throw new BadRequestException("Only the author can fulfill this request.");
        }
    }
}
