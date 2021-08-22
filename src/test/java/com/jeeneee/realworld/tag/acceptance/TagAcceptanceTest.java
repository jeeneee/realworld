package com.jeeneee.realworld.tag.acceptance;

import static com.jeeneee.realworld.fixture.ArticleFixture.ARTICLE1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jeeneee.realworld.AcceptanceTest;
import com.jeeneee.realworld.tag.dto.MultipleTagResponse;
import java.util.stream.Stream;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class TagAcceptanceTest extends AcceptanceTest {

    @DisplayName("태그 관련 기능")
    @TestFactory
    Stream<DynamicTest> manageTag() throws JsonProcessingException {

        createArticle(ARTICLE1);

        return Stream.of(
            dynamicTest("태그 조회", () -> {
                MultipleTagResponse response = get("/api/tags", HttpStatus.SC_OK,
                    MultipleTagResponse.class);
                assertThat(response.getTags()).hasSize(ARTICLE1.getTags().size());
            })
        );
    }
}
