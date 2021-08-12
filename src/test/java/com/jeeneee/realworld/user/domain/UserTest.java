package com.jeeneee.realworld.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jeeneee.realworld.common.exception.IllegalParameterException;
import com.jeeneee.realworld.user.exception.DuplicateFollowException;
import com.jeeneee.realworld.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

    public static final String EMAIL = "woojin7124@gmail.com";
    public static final String USERNAME = "jeeneee";
    public static final String PASSWORD = "12341234";

    private User user;
    private User target;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .email(EMAIL)
            .username(USERNAME)
            .password(PASSWORD)
            .build();
        target = User.builder()
            .email("woojin7124@naver.com")
            .username("jerry")
            .password("12341234")
            .build();
    }

    @DisplayName("유저 생성 - 이메일이 없으면 예외 발생")
    @Test
    void createUserWithoutEmail() {
        assertThatThrownBy(() -> User.builder().username(USERNAME).password(PASSWORD).build())
            .isInstanceOf(IllegalParameterException.class);
    }

    @DisplayName("유저 생성 - 유저명이 없으면 예외 발생")
    @Test
    void createUserWithoutUsername() {
        assertThatThrownBy(() -> User.builder().email(EMAIL).password(PASSWORD).build())
            .isInstanceOf(IllegalParameterException.class);
    }

    @DisplayName("유저 생성 - 비밀번호가 없으면 예외 발생")
    @Test
    void createUserWithoutPassword() {
        assertThatThrownBy(() -> User.builder().email(EMAIL).username(USERNAME).build())
            .isInstanceOf(IllegalParameterException.class);
    }

    @DisplayName("유저 생성")
    @Test
    void createUser() {
        User created = User.builder().email(EMAIL).username(USERNAME).password(PASSWORD).build();

        assertThat(created.getEmail()).isEqualTo(EMAIL);
        assertThat(created.getPassword()).isEqualTo(PASSWORD);
        assertThat(created.getUsername()).isEqualTo(USERNAME);
    }

    @DisplayName("정보 수정")
    @Test
    void updateUser() {
        String email = "jake@jake.jake";
        String image = "https://i.stack.imgur.com/xHWG8.jpg";
        String bio = "I like to skateboard";

        user.update(email, null, null, image, bio);

        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getUsername()).isEqualTo(user.getUsername());
        assertThat(user.getPassword()).isEqualTo(user.getPassword());
        assertThat(user.getImage()).isEqualTo(image);
        assertThat(user.getBio()).isEqualTo(bio);
    }

    @DisplayName("팔로우한 유저를 다시 팔로우하면 예외 발생")
    @Test
    void followUserTwice() {
        user.getFollows().add(target);

        assertThatThrownBy(() -> user.follow(target))
            .isInstanceOf(DuplicateFollowException.class)
            .hasMessageContaining("The user you have already followed.");
    }

    @DisplayName("팔로우하지 않은 유저를 팔로우 취소하면 예외 발생")
    @Test
    void unfollowUserTwice() {
        assertThatThrownBy(() -> user.unfollow(target))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("The user does not exist.");
    }

    @DisplayName("팔로우")
    @Test
    void followUser() {
        user.follow(target);

        assertThat(user.getFollows()).contains(target);
        assertThat(user.followed(target)).isTrue();
    }

    @DisplayName("팔로우 취소")
    @Test
    void unfollowUser() {
        user.getFollows().add(target);

        user.unfollow(target);

        assertThat(user.getFollows()).isEmpty();
        assertThat(user.followed(target)).isFalse();
    }
}