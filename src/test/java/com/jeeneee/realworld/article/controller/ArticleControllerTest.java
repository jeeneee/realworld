package com.jeeneee.realworld.article.controller;

import static com.jeeneee.realworld.fixture.ArticleFixture.ARTICLE1;
import static com.jeeneee.realworld.fixture.ArticleFixture.ARTICLE2;
import static com.jeeneee.realworld.fixture.ArticleFixture.CREATE_REQUEST;
import static com.jeeneee.realworld.fixture.ArticleFixture.UPDATE_REQUEST;
import static com.jeeneee.realworld.fixture.TagFixture.TAG2;
import static com.jeeneee.realworld.fixture.UserFixture.USER1;
import static com.jeeneee.realworld.fixture.UserFixture.USER2;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jeeneee.realworld.ControllerTest;
import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.article.dto.ArticleSearchCondition;
import com.jeeneee.realworld.article.dto.MultipleArticleResponse;
import com.jeeneee.realworld.article.dto.SingleArticleResponse;
import com.jeeneee.realworld.article.service.ArticleService;
import com.jeeneee.realworld.descriptor.ArticleFieldDescriptor;
import com.jeeneee.realworld.descriptor.ProfileFieldDescriptor;
import com.jeeneee.realworld.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest extends ControllerTest {

    @MockBean
    private ArticleService articleService;

    @DisplayName("????????? ??????")
    @Test
    void create() throws Exception {
        String request = objectMapper.writeValueAsString(CREATE_REQUEST);
        SingleArticleResponse response = SingleArticleResponse.of(ARTICLE1, USER1);
        given(articleService.save(any(), any(User.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            post("/api/articles")
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                .content(request)
        );

        result.andExpect(status().isCreated())
            .andDo(
                document("article/create",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("??????")
                    ),
                    requestFields(
                        fieldWithPath("article.title").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("article.description").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("article.body").type(JsonFieldType.STRING).description("??????"),
                        fieldWithPath("article.tagList").type(JsonFieldType.ARRAY).description("??????").optional()
                    ),
                    responseFields(
                        fieldWithPath("article").type(JsonFieldType.OBJECT).description("?????????")
                    ).andWithPrefix("article.", ArticleFieldDescriptor.article)
                        .andWithPrefix("article.author.", ProfileFieldDescriptor.profile)
                )
            );
    }

    @DisplayName("????????? ??????")
    @Test
    void update() throws Exception {
        String request = objectMapper.writeValueAsString(UPDATE_REQUEST);
        SingleArticleResponse response = SingleArticleResponse.of(ARTICLE2, USER1);
        given(articleService.update(any(), any(), any(User.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            put("/api/articles/{slug}", ARTICLE1.getSlugValue())
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                .content(request)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("article/update",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("??????")
                    ),
                    requestFields(
                        fieldWithPath("article.title").type(JsonFieldType.STRING).description("??????").optional(),
                        fieldWithPath("article.description").type(JsonFieldType.STRING).description("??????").optional(),
                        fieldWithPath("article.body").type(JsonFieldType.STRING).description("??????").optional()
                    ),
                    responseFields(
                        fieldWithPath("article").type(JsonFieldType.OBJECT).description("?????????")
                    ).andWithPrefix("article.", ArticleFieldDescriptor.article)
                        .andWithPrefix("article.author.", ProfileFieldDescriptor.profile)
                )
            );
    }

    @DisplayName("????????? ??????")
    @Test
    void deleteArticle() throws Exception {
        doNothing().when(articleService).delete(any(), any(User.class));

        ResultActions result = mockMvc.perform(
            delete("/api/articles/{slug}", ARTICLE1.getSlugValue())
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
        );

        result.andExpect(status().isNoContent())
            .andDo(
                document("article/delete",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("??????")
                    )
                )
            );
    }

    @DisplayName("????????? ?????? ??????")
    @Test
    void findOne() throws Exception {
        given(articleService.getArticle(any())).willReturn(ARTICLE1);

        ResultActions result = mockMvc.perform(
            get("/api/articles/{slug}", ARTICLE1.getSlugValue())
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("article/find-one-by-slug",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("??????").optional()
                    ),
                    responseFields(
                        fieldWithPath("article").type(JsonFieldType.OBJECT).description("?????????")
                    ).andWithPrefix("article.", ArticleFieldDescriptor.article)
                        .andWithPrefix("article.author.", ProfileFieldDescriptor.profile)
                )
            );
    }

    @DisplayName("????????? ?????? ??????")
    @Test
    void findAll() throws Exception {
        List<Article> list = List.of(ARTICLE1, ARTICLE2);
        MultipleArticleResponse response = MultipleArticleResponse.of(list, USER1);
        given(articleService.findAll(any(ArticleSearchCondition.class), any(User.class)))
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            get("/api/articles")
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                .queryParam("tag", TAG2.getName())
                .queryParam("author", USER1.getUsername())
        );

        result.andExpect(status().isOk())
            .andDo(
                document("article/find-all",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("??????").optional()
                    ),
                    requestParameters(
                        parameterWithName("tag").description("??????").optional(),
                        parameterWithName("author").description("?????????").optional(),
                        parameterWithName("favorited").description("?????? ?????????").optional(),
                        parameterWithName("limit").description("limit(20)").optional(),
                        parameterWithName("offset").description("offset(0)").optional()
                    ),
                    responseFields(
                        fieldWithPath("articles").type(JsonFieldType.ARRAY).description("????????? ??????"),
                        fieldWithPath("articlesCount").type(JsonFieldType.NUMBER).description("????????? ???")
                    ).andWithPrefix("articles[].", ArticleFieldDescriptor.article)
                        .andWithPrefix("articles[].author.", ProfileFieldDescriptor.profile)
                )
            );
    }

    @DisplayName("????????? ?????? ??????")
    @Test
    void feed() throws Exception {
        USER2.follow(USER1);
        List<Article> list = List.of(ARTICLE1, ARTICLE2);
        MultipleArticleResponse response = MultipleArticleResponse.of(list, USER2);
        given(articleService.findFeedArticles(any(ArticleSearchCondition.class), any(User.class)))
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            get("/api/articles/feed")
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                .queryParam("limit", "20")
                .queryParam("offset", "0")
        );

        result.andExpect(status().isOk())
            .andDo(
                document("article/feed",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("??????")
                    ),
                    requestParameters(
                        parameterWithName("limit").description("limit(20)").optional(),
                        parameterWithName("offset").description("offset(0)").optional()
                    ),
                    responseFields(
                        fieldWithPath("articles").type(JsonFieldType.ARRAY).description("????????? ??????"),
                        fieldWithPath("articlesCount").type(JsonFieldType.NUMBER).description("????????? ???")
                    ).andWithPrefix("articles[].", ArticleFieldDescriptor.article)
                        .andWithPrefix("articles[].author.", ProfileFieldDescriptor.profile)
                )
            );
        USER2.unfollow(USER1);
    }

    @DisplayName("????????? ?????????")
    @Test
    void favorite() throws Exception {
        ARTICLE1.favorite(USER1);
        SingleArticleResponse response = SingleArticleResponse.of(ARTICLE1, USER1);
        given(articleService.favorite(any(), any(User.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            post("/api/articles/{slug}/favorite", ARTICLE1.getSlugValue())
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("article/favorite",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("??????")
                    ),
                    responseFields(
                        fieldWithPath("article").type(JsonFieldType.OBJECT).description("?????????")
                    ).andWithPrefix("article.", ArticleFieldDescriptor.article)
                        .andWithPrefix("article.author.", ProfileFieldDescriptor.profile)
                )
            );
        ARTICLE1.unfavorite(USER1);
    }

    @DisplayName("????????? ????????? ??????")
    @Test
    void unfavorite() throws Exception {
        SingleArticleResponse response = SingleArticleResponse.of(ARTICLE1, USER1);
        given(articleService.unfavorite(any(), any(User.class))).willReturn(response);

        ResultActions result = mockMvc.perform(
            delete("/api/articles/{slug}/favorite", ARTICLE1.getSlugValue())
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("article/unfavorite",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("??????")
                    ),
                    responseFields(
                        fieldWithPath("article").type(JsonFieldType.OBJECT).description("?????????")
                    ).andWithPrefix("article.", ArticleFieldDescriptor.article)
                        .andWithPrefix("article.author.", ProfileFieldDescriptor.profile)
                )
            );
    }
}