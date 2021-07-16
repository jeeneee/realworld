package com.jeeneee.realworld.user.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jeeneee.realworld.common.exception.IllegalParameterException;
import com.jeeneee.realworld.user.exception.DuplicatedFollowException;
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
        user = User.create(EMAIL, USERNAME, PASSWORD);
        target = User.create("woojin7124@naver.com", "jerry", "12341234");
    }

    @DisplayName("유저 생성(예외 발생)")
    @Test
    void createUserWithoutInfo() {
        assertThrows(IllegalParameterException.class, () -> User.create(EMAIL, PASSWORD, null));
        assertThrows(IllegalParameterException.class, () -> User.create(EMAIL, null, PASSWORD));
        assertThrows(IllegalParameterException.class, () -> User.create(null, USERNAME, PASSWORD));
    }

    @DisplayName("유저 생성")
    @Test
    void createUser() {
        User created = User.create(EMAIL, USERNAME, PASSWORD);

        assertAll(
            () -> assertEquals(EMAIL, created.getEmail()),
            () -> assertEquals(PASSWORD, created.getPassword()),
            () -> assertEquals(USERNAME, created.getUsername())
        );
    }

    @DisplayName("정보 수정")
    @Test
    void updateUser() {
        // given
        String email = "jake@jake.jake";
        String image = "https://i.stack.imgur.com/xHWG8.jpg";
        String bio = "I like to skateboard";

        // when
        user.update(email, user.getUsername(), user.getPassword(), image, bio);

        // then
        assertAll(
            () -> assertEquals(email, user.getEmail()),
            () -> assertEquals(image, user.getImage()),
            () -> assertEquals(bio, user.getBio())
        );
    }

    @DisplayName("팔로우한 유저를 다시 팔로우하면 예외 발생")
    @Test
    void followUserTwice() {
        // given
        user.getFollows().add(target);
        assertTrue(user.followed(target));

        // when & then
        assertThrows(DuplicatedFollowException.class, () -> user.follow(target));
    }

    @DisplayName("팔로우하지 않은 유저를 팔로우 취소하면 예외 발생")
    @Test
    void unfollowUserTwice() {
        // given
        assertFalse(user.followed(target));

        // when & then
        assertThrows(UserNotFoundException.class, () -> user.unfollow(target));
    }

    @DisplayName("팔로우")
    @Test
    void followUser() {
        // given
        assertTrue(user.getFollows().isEmpty());
        assertFalse(user.followed(target));

        // when
        user.follow(target);

        // then
        assertTrue(user.getFollows().contains(target));
        assertTrue(user.followed(target));
    }

    @DisplayName("팔로우 취소")
    @Test
    void unfollowUser() {
        // given
        user.getFollows().add(target);
        assertTrue(user.followed(target));

        // when
        user.unfollow(target);

        // then
        assertTrue(user.getFollows().isEmpty());
        assertFalse(user.followed(target));
    }
}