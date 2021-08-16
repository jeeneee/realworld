package com.jeeneee.realworld.fixture;

import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.dto.LoginRequest;
import com.jeeneee.realworld.user.dto.RegisterRequest;
import com.jeeneee.realworld.user.dto.UserUpdateRequest;

public class UserFixture {

    public static final User REGISTER_USER = User.builder()
        .id(1L)
        .email("user@abc.com")
        .username("username")
        .password("password")
        .build();

    public static final User USER1 = User.builder()
        .id(1L)
        .email("user@abc.com")
        .username("username")
        .password("$2a$10$F9sYwvEPNZhCLhS2NbcoeO8sl8YRv1e5zwWQ1Gwth/b.sowQNwVe2")
        .image("https://avatars.githubusercontent.com/u/23521870?v=4")
        .bio("반갑습니다.")
        .token("abc.def.ghi")
        .build();

    public static final User USER2 = User.builder()
        .id(2L)
        .email("user2@abc.com")
        .username("jeeneee")
        .password("$2a$10$YuVxABvHDPL3oIVUuFeXGuLugm.UNDT32jUS5KaHpVf1JGdrSi8fu")
        .image("https://avatars.githubusercontent.com/u/39071798?v=4")
        .bio("안녕하세요.")
        .token("abc.def.ghj")
        .build();

    public static final RegisterRequest REGISTER_REQUEST = RegisterRequest.builder()
        .email(REGISTER_USER.getEmail())
        .username(REGISTER_USER.getUsername())
        .password(REGISTER_USER.getPassword())
        .build();

    public static final LoginRequest LOGIN_REQUEST = LoginRequest.builder()
        .email(USER1.getEmail())
        .password("password")
        .build();

    public static final UserUpdateRequest UPDATE_REQUEST = UserUpdateRequest.builder()
        .email(USER2.getEmail())
        .username(USER2.getUsername())
        .password("password2")
        .image(USER2.getImage())
        .bio(USER2.getBio())
        .build();
}
