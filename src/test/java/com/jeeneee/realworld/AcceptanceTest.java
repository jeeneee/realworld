package com.jeeneee.realworld;

import static com.jeeneee.realworld.fixture.UserFixture.USER1;
import static com.jeeneee.realworld.infra.security.JwtAuthenticationFilter.AUTHORIZATION_HEADER;
import static com.jeeneee.realworld.infra.security.TokenProvider.HEADER_PREFIX;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeneee.realworld.article.domain.Article;
import com.jeeneee.realworld.article.dto.ArticleCreateRequest;
import com.jeeneee.realworld.article.dto.SingleArticleResponse;
import com.jeeneee.realworld.tag.domain.Tag;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.dto.LoginRequest;
import com.jeeneee.realworld.user.dto.RegisterRequest;
import com.jeeneee.realworld.user.dto.UserResponse;
import io.restassured.RestAssured;
import java.util.stream.Collectors;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    protected static final String AUTHORIZATION_HEADER_NAME = AUTHORIZATION_HEADER;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String token;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.port = port;
        token = registerAndLogin(USER1);
    }

    public void register(User user) throws JsonProcessingException {
        RegisterRequest request = RegisterRequest.builder()
            .email(user.getEmail())
            .username(user.getUsername())
            .password(user.getPassword())
            .build();
        String body = objectMapper.writeValueAsString(request);
        post("/api/users", body, HttpStatus.SC_CREATED, UserResponse.class);
    }

    private String login(User user) throws JsonProcessingException {
        LoginRequest request = LoginRequest.builder()
            .email(user.getEmail())
            .password(user.getPassword())
            .build();
        String body = objectMapper.writeValueAsString(request);
        UserResponse response = post("/api/users/login", body, HttpStatus.SC_OK,
            UserResponse.class);
        return response.getToken();
    }

    public String registerAndLogin(User user) throws JsonProcessingException {
        register(user);
        return login(user);
    }

    public void createArticle(Article article) throws JsonProcessingException {
        ArticleCreateRequest request = ArticleCreateRequest.builder()
            .title(article.getTitle())
            .description(article.getDescription())
            .body(article.getBody())
            .tagList(article.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
            .build();
        String body = objectMapper.writeValueAsString(request);
        post("/api/articles", body, token, HttpStatus.SC_CREATED, SingleArticleResponse.class);
    }

    protected <T> T post(String uri, String body, int status, Class<T> cls) {
        return
            given()
                .log().all()
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(body)
            .when()
                .post(uri)
            .then()
                .log().all()
                .statusCode(status)
                .contentType(APPLICATION_JSON_VALUE)
                .extract().as(cls);
    }

    protected <T> T post(String uri, String body, String token, int status, Class<T> cls) {
        return
            given()
                .log().all()
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION_HEADER_NAME, HEADER_PREFIX + token)
                .body(body)
            .when()
                .post(uri)
            .then()
                .log().all()
                .statusCode(status)
                .contentType(APPLICATION_JSON_VALUE)
                .extract().as(cls);
    }

    protected <T> T get(String uri, int status, Class<T> cls) {
        return
            given()
                .log().all()
            .when()
                .get(uri)
            .then()
                .log().all()
                .statusCode(status)
                .contentType(APPLICATION_JSON_VALUE)
                .extract().as(cls);
    }

    protected <T> T get(String uri, String token, int status, Class<T> cls) {
        return
            given()
                .log().all()
                .header(AUTHORIZATION_HEADER_NAME, HEADER_PREFIX + token)
            .when()
                .get(uri)
            .then()
                .log().all()
                .statusCode(status)
                .contentType(APPLICATION_JSON_VALUE)
                .extract().as(cls);
    }

    protected <T> T put(String uri, String body, String token, int status, Class<T> cls) {
        return
            given()
                .log().all()
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION_HEADER_NAME, HEADER_PREFIX + token)
                .body(body)
            .when()
                .put(uri)
            .then()
                .log().all()
                .statusCode(status)
                .contentType(APPLICATION_JSON_VALUE)
                .extract().as(cls);
    }

    protected void delete(String uri, String token) {
        given()
            .log().all()
            .header(AUTHORIZATION_HEADER_NAME, HEADER_PREFIX + token)
        .when()
            .delete(uri)
        .then()
            .log().all()
            .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    protected <T> T delete(String uri, String token, int status, Class<T> cls) {
        return
            given()
                .log().all()
                .header(AUTHORIZATION_HEADER_NAME, HEADER_PREFIX + token)
            .when()
                .delete(uri)
            .then()
                .log().all()
                .statusCode(status)
                .contentType(APPLICATION_JSON_VALUE)
                .extract().as(cls);
    }
}
