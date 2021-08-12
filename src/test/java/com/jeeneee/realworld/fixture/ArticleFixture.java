package com.jeeneee.realworld.fixture;

import static com.jeeneee.realworld.fixture.UserFixture.USER1;
import static com.jeeneee.realworld.fixture.UserFixture.USER2;

import com.jeeneee.realworld.article.domain.Article;

public class ArticleFixture {

    public static final Article ARTICLE1 = Article.builder()
        .id(1L)
        .title("How to train your dragon")
        .description("Ever wonder how?")
        .body("You have to believe")
        .author(USER1)
        .build();

    public static final Article ARTICLE2 = Article.builder()
        .id(2L)
        .title("gradle 멀티모듈 구성")
        .description("데모 프로젝트인 realworld를 기반으로 글을 작성해보려 한다.")
        .body("비록 작은 규모의 프로젝트지만 단일 프로젝트가 아닌 멀티모듈로 구성해보자.\n"
            + "멀티모듈에 대한 자세한 설명과 장점은 멀티모듈 설계 이야기 with Spring, Gradle 블로깅을 참고하면 좋다.\n"
            + "여기선 realworld란 root 프로젝트 안에 realworld-api, realworld-common, realworld-core란 서브모듈을 둘 것이다.")
        .author(USER2)
        .build();
}
