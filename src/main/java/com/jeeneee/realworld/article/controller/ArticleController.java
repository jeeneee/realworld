package com.jeeneee.realworld.article.controller;

import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.article.dto.ArticleCreateRequest;
import com.jeeneee.realworld.article.dto.ArticleSearchCondition;
import com.jeeneee.realworld.article.dto.ArticleUpdateRequest;
import com.jeeneee.realworld.article.dto.MultipleArticleResponse;
import com.jeeneee.realworld.article.dto.SingleArticleResponse;
import com.jeeneee.realworld.article.service.ArticleService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/articles")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<SingleArticleResponse> create(@LoginUser User author,
        @Valid @RequestBody ArticleCreateRequest request) {
        SingleArticleResponse response = articleService.save(request, author);
        URI uri = URI.create("/api/articles/" + response.getSlug());
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<SingleArticleResponse> update(@RequestBody ArticleUpdateRequest request,
        @PathVariable String slug, @LoginUser User user) {
        return ResponseEntity.ok().body(articleService.update(request, slug, user));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> delete(@PathVariable String slug, @LoginUser User user) {
        articleService.delete(slug, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{slug}")
    public ResponseEntity<SingleArticleResponse> findOne(@PathVariable String slug,
        @OptionalUser User user) {
        Article article = articleService.getArticle(slug);
        return ResponseEntity.ok().body(SingleArticleResponse.of(article, user));
    }

    @GetMapping
    public ResponseEntity<MultipleArticleResponse> findAll(
        @RequestParam(defaultValue = "") String tag,
        @RequestParam(defaultValue = "") String favorited,
        @RequestParam(defaultValue = "") String author,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "20") int limit,
        @OptionalUser User user) {
        var condition = new ArticleSearchCondition(tag, author, favorited, limit, offset);
        return ResponseEntity.ok().body(articleService.findAll(condition, user));
    }

    @GetMapping("/feed")
    public ResponseEntity<MultipleArticleResponse> feed(
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "20") int limit,
        @LoginUser User user) {
        var condition = new ArticleSearchCondition(limit, offset);
        return ResponseEntity.ok().body(articleService.findFeedArticles(condition, user));
    }
}
