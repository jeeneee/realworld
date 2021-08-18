package com.jeeneee.realworld.article.service;

import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.article.domain.ArticleQueryRepository;
import com.jeeneee.realworld.article.domain.ArticleRepository;
import com.jeeneee.realworld.article.domain.Slug;
import com.jeeneee.realworld.article.dto.ArticleCreateRequest;
import com.jeeneee.realworld.article.dto.ArticleSearchCondition;
import com.jeeneee.realworld.article.dto.ArticleUpdateRequest;
import com.jeeneee.realworld.article.dto.MultipleArticleResponse;
import com.jeeneee.realworld.article.dto.SingleArticleResponse;
import com.jeeneee.realworld.article.exception.ArticleNotFoundException;
import com.jeeneee.realworld.article.exception.DuplicateSlugException;
import com.jeeneee.realworld.common.exception.BadRequestException;
import com.jeeneee.realworld.tag.domain.Tag;
import com.jeeneee.realworld.tag.service.TagService;
import com.jeeneee.realworld.user.domain.User;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleQueryRepository articleQueryRepository;
    private final TagService tagService;

    @Transactional
    public SingleArticleResponse save(ArticleCreateRequest request, User author) {
        Article article = request.toEntity(author);
        validateSlug(article.getSlug());
        if (Optional.ofNullable(request.getTagList()).isPresent()) {
            List<Tag> tags = tagService.findOrSave(request.getTagList());
            tags.forEach(article::addTag);
        }
        articleRepository.save(article);
        return SingleArticleResponse.of(article, author);
    }

    @Transactional
    public SingleArticleResponse update(ArticleUpdateRequest request, String slug, User user) {
        Article article = getArticle(slug);
        Optional.ofNullable(request.getTitle()).ifPresent(title -> validateSlug(Slug.from(title)));
        verifyAuthor(user, article);
        article.update(request.getTitle(), request.getDescription(), request.getBody());
        return SingleArticleResponse.of(article, user);
    }

    @Transactional
    public void delete(String slug, User user) {
        Article article = getArticle(slug);
        verifyAuthor(user, article);
        articleRepository.delete(article);
    }

    public Article getArticle(String slug) {
        return articleRepository.findBySlug_Value(slug)
            .orElseThrow(ArticleNotFoundException::new);
    }

    public MultipleArticleResponse findAll(ArticleSearchCondition condition, User user) {
        return new MultipleArticleResponse(articleQueryRepository.findAll(condition).stream()
            .map(article -> SingleArticleResponse.of(article, user))
            .collect(Collectors.toList()));
    }

    public MultipleArticleResponse findFeedArticles(ArticleSearchCondition condition, User user) {
        List<Long> followIds = user.getFollows().stream()
            .map(User::getId).collect(Collectors.toList());
        return new MultipleArticleResponse(articleQueryRepository.findFeedArticles(condition, followIds).stream()
            .map(article -> SingleArticleResponse.of(article, user))
            .collect(Collectors.toList()));
    }

    @Transactional
    public SingleArticleResponse favorite(String slug, User user) {
        Article article = getArticle(slug);
        article.favorite(user);
        return SingleArticleResponse.of(article, user);
    }

    @Transactional
    public SingleArticleResponse unfavorite(String slug, User user) {
        Article article = getArticle(slug);
        article.unfavorite(user);
        return SingleArticleResponse.of(article, user);
    }

    private void verifyAuthor(User author, Article article) {
        if (article.getAuthor() != author) {
            throw new BadRequestException("Only the author can fulfill this request.");
        }
    }

    private void validateSlug(Slug slug) {
        if (articleRepository.existsBySlug(slug)) {
            throw new DuplicateSlugException();
        }
    }
}
