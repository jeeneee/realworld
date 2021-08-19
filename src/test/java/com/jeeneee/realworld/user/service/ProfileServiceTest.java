package com.jeeneee.realworld.user.service;

import static com.jeeneee.realworld.user.domain.UserTest.EMAIL;
import static com.jeeneee.realworld.user.domain.UserTest.PASSWORD;
import static com.jeeneee.realworld.user.domain.UserTest.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.domain.UserRepository;
import com.jeeneee.realworld.user.dto.ProfileResponse.ProfileInfo;
import com.jeeneee.realworld.user.exception.UserNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    private ProfileService profileService;

    private User user;
    private User target;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService(userRepository);
        user = User.builder()
            .email(EMAIL)
            .username(USERNAME)
            .password(PASSWORD)
            .build();
        target = User.builder()
            .email("target@target.com")
            .username("target")
            .password("password")
            .build();
    }

    @DisplayName("회원 조회 - 해당 유저명을 갖는 회원이 존재하지 않는 경우 예외 발생")
    @Test
    void find_UsernameNotExist_ExceptionThrown() {
        given(userRepository.findByUsername(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.find(target.getUsername(), user))
            .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("회원 조회 - 로그인하지 않은 경우 팔로잉은 반드시 false이다.")
    @Test
    void find_Logout_FollowingFalse() {
        given(userRepository.findByUsername(any())).willReturn(Optional.of(target));

        ProfileInfo profile = profileService.find(target.getUsername(), null).getProfile();

        assertThat(profile.isFollowing()).isFalse();
    }

    @DisplayName("팔로잉한 회원 조회")
    @Test
    void find_FollowingUser_FollowingTrue() {
        user.follow(target);
        given(userRepository.findByUsername(any())).willReturn(Optional.of(target));

        ProfileInfo profile = profileService.find(target.getUsername(), user).getProfile();

        assertThat(profile.isFollowing()).isTrue();
        assertThat(profile.getUsername()).isEqualTo(target.getUsername());
        assertThat(profile.getBio()).isEqualTo(target.getBio());
        assertThat(profile.getImage()).isEqualTo(target.getImage());
    }

    @DisplayName("팔로잉하지 않은 회원 조회")
    @Test
    void find_NotFollowingUser_FollowingFalse() {
        given(userRepository.findByUsername(any())).willReturn(Optional.of(target));

        ProfileInfo profile = profileService.find(target.getUsername(), user).getProfile();

        assertThat(profile.isFollowing()).isFalse();
        assertThat(profile.getUsername()).isEqualTo(target.getUsername());
        assertThat(profile.getBio()).isEqualTo(target.getBio());
        assertThat(profile.getImage()).isEqualTo(target.getImage());
    }

    @DisplayName("팔로우")
    @Test
    void follow_Normal_Success() {
        given(userRepository.findByUsername(any())).willReturn(Optional.of(target));

        ProfileInfo profile = profileService.follow(target.getUsername(), user).getProfile();

        assertThat(profile.isFollowing()).isTrue();
        assertThat(profile.getUsername()).isEqualTo(target.getUsername());
        assertThat(profile.getBio()).isEqualTo(target.getBio());
        assertThat(profile.getImage()).isEqualTo(target.getImage());
    }

    @DisplayName("언팔로우")
    @Test
    void unfollow_Normal_Success() {
        user.follow(target);
        given(userRepository.findByUsername(any())).willReturn(Optional.of(target));

        ProfileInfo profile = profileService.unfollow(target.getUsername(), user).getProfile();

        assertThat(profile.isFollowing()).isFalse();
        assertThat(profile.getUsername()).isEqualTo(target.getUsername());
        assertThat(profile.getBio()).isEqualTo(target.getBio());
        assertThat(profile.getImage()).isEqualTo(target.getImage());
    }
}