package com.jeeneee.realworld.article.service;

import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_BODY;
import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_DESCRIPTION;
import static com.jeeneee.realworld.article.domain.ArticleTest.ARTICLE_TITLE;
import static com.jeeneee.realworld.user.domain.UserTest.EMAIL;
import static com.jeeneee.realworld.user.domain.UserTest.PASSWORD;
import static com.jeeneee.realworld.user.domain.UserTest.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.article.domain.ArticleRepository;
import com.jeeneee.realworld.article.domain.Slug;
import com.jeeneee.realworld.article.dto.ArticleCreateRequest;
import com.jeeneee.realworld.article.dto.ArticleUpdateRequest;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private TagService tagService;

    private ArticleService articleService;

    private List<String> tagList;
    private List<Tag> tags;
    private User author;
    private Article article;

    @BeforeEach
    void setUp() {
        articleService = new ArticleService(articleRepository, tagService);

        tagList = List.of("reactjs", "angularjs");
        tags = tagList.stream().map(Tag::create).collect(Collectors.toList());
        author = User.builder()
            .email(EMAIL)
            .username(USERNAME)
            .password(PASSWORD)
            .build();
        article = Article.builder()
            .title(ARTICLE_TITLE)
            .description(ARTICLE_DESCRIPTION)
            .body(ARTICLE_BODY)
            .author(author)
            .build();
    }

    @DisplayName("게시글 생성 - 슬러그가 이미 존재하면 예외 발생")
    @Test
    void save_SlugExist_ExceptionThrown() {
        ArticleCreateRequest request = ArticleCreateRequest.builder()
            .title(ARTICLE_TITLE)
            .description(ARTICLE_DESCRIPTION)
            .body(ARTICLE_BODY)
            .tagList(tagList)
            .build();
        given(articleRepository.existsBySlug(any())).willReturn(true);

        assertThatThrownBy(() -> articleService.save(request, author))
            .isInstanceOf(DuplicateSlugException.class)
            .hasMessageContaining("The article with the slug already exists.");
    }

    @DisplayName("게시글 생성 - 태그 포함")
    @Test
    void save_TagsIncluded_Success() {
        ArticleCreateRequest request = ArticleCreateRequest.builder()
            .title(ARTICLE_TITLE)
            .description(ARTICLE_DESCRIPTION)
            .body(ARTICLE_BODY)
            .tagList(tagList)
            .build();
        given(articleRepository.existsBySlug(any())).willReturn(false);
        given(tagService.findOrSave(anyList())).willReturn(tags);
        given(articleRepository.save(any(Article.class))).willReturn(article);

        SingleArticleResponse response = articleService.save(request, author);

        assertThat(response.getTagList()).containsAll(tagList);
    }

    @DisplayName("게시글 수정 - 존재하지 않는 게시글인 경우 예외 발생")
    @Test
    void update_ArticleNotExists_ExceptionThrown() {
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
            .title("수정된 제목")
            .build();
        given(articleRepository.findBySlug_Value(any())).willReturn(Optional.empty());

        assertThatThrownBy(
            () -> articleService.update(request, article.getSlugValue(), author))
            .isInstanceOf(ArticleNotFoundException.class);
    }

    @DisplayName("게시글 수정 - 슬러그가 이미 존재하면 예외 발생")
    @Test
    void update_SlugExists_ExceptionThrown() {
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
            .title("수정된 제목")
            .build();
        given(articleRepository.findBySlug_Value(any())).willReturn(Optional.of(article));
        given(articleRepository.existsBySlug(any(Slug.class))).willReturn(true);

        assertThatThrownBy(
            () -> articleService.update(request, article.getSlugValue(), author))
            .isInstanceOf(DuplicateSlugException.class);
    }

    @DisplayName("게시글 수정 - 작성자가 본인이 아닌 경우 예외 발생")
    @Test
    void update_IsNotAuthor_ExceptionThrown() {
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
            .title("수정된 제목")
            .build();
        User other = User.builder()
            .email("other@other.com")
            .username("other")
            .password("other_password")
            .build();
        given(articleRepository.findBySlug_Value(any())).willReturn(Optional.of(article));
        given(articleRepository.existsBySlug(any(Slug.class))).willReturn(false);

        assertThatThrownBy(
            () -> articleService.update(request, article.getSlugValue(), other))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Only the author can fulfill this request.");
    }

    @DisplayName("게시글 수정 - 제목이 변경되면 슬러그도 변경된다.")
    @Test
    void update_ChangeTitle_SlugChanged() {
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
            .title("수정된 제목")
            .build();
        given(articleRepository.findBySlug_Value(any())).willReturn(Optional.of(article));
        given(articleRepository.existsBySlug(any(Slug.class))).willReturn(false);

        SingleArticleResponse response = articleService
            .update(request, article.getSlugValue(), author);

        assertThat(response.getSlug()).isEqualTo("수정된-제목");
    }

    @DisplayName("게시글 삭제 - 존재하지 않는 게시글인 경우 예외 발생")
    @Test
    void delete_ArticleNotExists_ExceptionThrown() {
        given(articleRepository.findBySlug_Value(any())).willReturn(Optional.empty());

        assertThatThrownBy(
            () -> articleService.delete(article.getSlugValue(), author))
            .isInstanceOf(ArticleNotFoundException.class);
    }

    @DisplayName("게시글 삭제 - 작성자가 본인이 아닌 경우 예외 발생")
    @Test
    void delete_IsNotAuthor_ExceptionThrown() {
        User other = User.builder()
            .email("other@other.com")
            .username("other")
            .password("other_password")
            .build();
        given(articleRepository.findBySlug_Value(any())).willReturn(Optional.of(article));

        assertThatThrownBy(
            () -> articleService.delete(article.getSlugValue(), other))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Only the author can fulfill this request.");
    }

    @DisplayName("게시글 삭제 - 작성자가 본인이 아닌 경우 예외 발생")
    @Test
    void delete_Normal_Success() {
        given(articleRepository.findBySlug_Value(any())).willReturn(Optional.of(article));

        articleService.delete(article.getSlugValue(), author);

        verify(articleRepository, times(1)).delete(article);
    }
}