package com.jeeneee.realworld.article.acceptance;

import static com.jeeneee.realworld.fixture.ArticleFixture.ARTICLE1;
import static com.jeeneee.realworld.fixture.ArticleFixture.ARTICLE2;
import static com.jeeneee.realworld.fixture.ArticleFixture.ARTICLE3;
import static com.jeeneee.realworld.fixture.TagFixture.TAG2;
import static com.jeeneee.realworld.fixture.UserFixture.USER1;
import static com.jeeneee.realworld.fixture.UserFixture.USER2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.jeeneee.realworld.AcceptanceTest;
import com.jeeneee.realworld.article.dto.ArticleUpdateRequest;
import com.jeeneee.realworld.article.dto.MultipleArticleResponse;
import com.jeeneee.realworld.article.dto.SingleArticleResponse;
import com.jeeneee.realworld.article.dto.SingleArticleResponse.ArticleInfo;
import com.jeeneee.realworld.user.dto.ProfileResponse;
import java.util.List;
import java.util.stream.Stream;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class ArticleAcceptanceTest extends AcceptanceTest {

    @DisplayName("게시글 관련 기능")
    @TestFactory
    Stream<DynamicTest> manageArticle() {

        return Stream.of(
            dynamicTest("게시글 세 개 생성", () -> {
                createArticle(ARTICLE1);
                createArticle(ARTICLE2);
                createArticle(ARTICLE3);
            }),
            dynamicTest("게시글 단일 조회", () -> {
                String uri = "/api/articles/" + ARTICLE1.getSlugValue();
                SingleArticleResponse response = get(uri, HttpStatus.SC_OK,
                    SingleArticleResponse.class);
                ArticleInfo article = response.getArticle();
                assertThat(article.getSlug()).isEqualTo(ARTICLE1.getSlugValue());
            }),
            dynamicTest("게시글 전체 조회", () -> {
                MultipleArticleResponse response = get("/api/articles", HttpStatus.SC_OK,
                    MultipleArticleResponse.class);
                List<ArticleInfo> articles = response.getArticles();
                assertThat(response.getArticlesCount()).isEqualTo(3);
                assertCreatedAtDesc(articles);
            }),
            dynamicTest("게시글 작성자 전체 조회", () -> {
                String uri = "/api/articles?author=" + USER1.getUsername();
                MultipleArticleResponse response = get(uri, HttpStatus.SC_OK,
                    MultipleArticleResponse.class);
                List<ArticleInfo> articles = response.getArticles();
                assertAuthor(articles, USER1.getUsername());
                assertThat(response.getArticlesCount()).isEqualTo(3);
                assertCreatedAtDesc(articles);
            }),
            dynamicTest("게시글 태그 전체 조회", () -> {
                String uri = "/api/articles?tag=" + TAG2.getName();
                MultipleArticleResponse response = get(uri, HttpStatus.SC_OK,
                    MultipleArticleResponse.class);
                List<ArticleInfo> articles = response.getArticles();
                assertTag(articles, TAG2.getName());
                assertThat(response.getArticlesCount()).isEqualTo(2);
                assertCreatedAtDesc(articles);
            }),
            dynamicTest("게시글 찜하기", () -> {
                String uri = "/api/articles/" + ARTICLE1.getSlugValue() + "/favorite";
                SingleArticleResponse response = post(uri, "", token, HttpStatus.SC_OK,
                    SingleArticleResponse.class);
                ArticleInfo article = response.getArticle();
                assertThat(article.getFavoritesCount()).isOne();
                assertThat(article.isFavorited()).isTrue();
            }),
            dynamicTest("찜한 게시글 전체 조회", () -> {
                String uri = "/api/articles?favorited=" + USER1.getUsername();
                MultipleArticleResponse response = get(uri, HttpStatus.SC_OK,
                    MultipleArticleResponse.class);
                assertThat(response.getArticlesCount()).isOne();
                assertCreatedAtDesc(response.getArticles());
            }),
            dynamicTest("게시글 찜하기 취소", () -> {
                String uri = "/api/articles/" + ARTICLE1.getSlugValue() + "/favorite";
                SingleArticleResponse response = delete(uri, token, HttpStatus.SC_OK,
                    SingleArticleResponse.class);
                ArticleInfo article = response.getArticle();
                assertThat(article.getFavoritesCount()).isZero();
                assertThat(article.isFavorited()).isFalse();
            }),
            dynamicTest("게시글 수정", () -> {
                String uri = "/api/articles/" + ARTICLE1.getSlugValue();
                ArticleUpdateRequest request = ArticleUpdateRequest.builder()
                    .title("How to train your dragoon")
                    .build();
                String body = objectMapper.writeValueAsString(request);
                SingleArticleResponse response = put(uri, body, token, HttpStatus.SC_OK,
                    SingleArticleResponse.class);
                assertThat(response.getArticle().getTitle()).isEqualTo(request.getTitle());
            }),
            dynamicTest("게시글 삭제", () -> {
                delete("/api/articles/" + ARTICLE3.getSlugValue(), token);
            }),
            dynamicTest("피드 조회", () -> {
                String user2Token = registerAndLogin(USER2);
                post("/api/profiles/" + USER1.getUsername() + "/follow", "", user2Token,
                    HttpStatus.SC_OK, ProfileResponse.class);
                MultipleArticleResponse response = get("/api/articles/feed", user2Token,
                    HttpStatus.SC_OK, MultipleArticleResponse.class);
                List<ArticleInfo> articles = response.getArticles();
                assertThat(response.getArticlesCount()).isEqualTo(2);
                assertFollowed(articles);
                assertCreatedAtDesc(articles);
            })
        );
    }

    private void assertCreatedAtDesc(List<ArticleInfo> articles) {
        for (int i = 0; i < articles.size() - 1; i++) {
            assertThat(articles.get(i).getCreatedAt()).isAfter(articles.get(i + 1).getCreatedAt());
        }
    }

    private void assertAuthor(List<ArticleInfo> articles, String author) {
        for (ArticleInfo article : articles) {
            assertThat(article.getAuthor().getUsername()).isEqualTo(author);
        }
    }

    private void assertTag(List<ArticleInfo> articles, String tag) {
        for (ArticleInfo article : articles) {
            assertThat(article.getTagList()).contains(tag);
        }
    }

    private void assertFollowed(List<ArticleInfo> articles) {
        for (ArticleInfo article : articles) {
            assertThat(article.getAuthor().isFollowing()).isTrue();
        }
    }
}
