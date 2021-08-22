package com.jeeneee.realworld.user.acceptance;

import static com.jeeneee.realworld.fixture.UserFixture.USER1;
import static com.jeeneee.realworld.fixture.UserFixture.USER2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.jeeneee.realworld.AcceptanceTest;
import com.jeeneee.realworld.user.dto.ProfileResponse;
import com.jeeneee.realworld.user.dto.ProfileResponse.ProfileInfo;
import java.util.stream.Stream;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class ProfileAcceptanceTest extends AcceptanceTest {

    @DisplayName("프로필 관련 기능")
    @TestFactory
    Stream<DynamicTest> manageProfile() {
        return Stream.of(
            dynamicTest("회원 프로필 조회", () -> {
                ProfileResponse response = get("/api/profiles/" + USER1.getUsername(), token,
                    HttpStatus.SC_OK, ProfileResponse.class);
                ProfileInfo profile = response.getProfile();
                assertThat(profile.getUsername()).isEqualTo(USER1.getUsername());
            }),
            dynamicTest("팔로우", () -> {
                register(USER2);
                ProfileResponse response = post("/api/profiles/" + USER2.getUsername() + "/follow",
                    "", token, HttpStatus.SC_OK, ProfileResponse.class);
                ProfileInfo profile = response.getProfile();
                assertThat(profile.getUsername()).isEqualTo(USER2.getUsername());
                assertThat(profile.isFollowing()).isTrue();
            }),
            dynamicTest("언팔로우", () -> {
                ProfileResponse response = delete(
                    "/api/profiles/" + USER2.getUsername() + "/follow",
                    token, HttpStatus.SC_OK, ProfileResponse.class);
                ProfileInfo profile = response.getProfile();
                assertThat(profile.getUsername()).isEqualTo(USER2.getUsername());
                assertThat(profile.isFollowing()).isFalse();
            })
        );
    }
}
