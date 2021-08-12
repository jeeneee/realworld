package com.jeeneee.realworld.fixture;

import com.jeeneee.realworld.user.domain.User;

public class UserFixture {

    public static final User USER1 = User.builder()
        .id(1L)
        .email("jeeneee@gmail.com")
        .username("jeeneee")
        .password("$2a$10$F9sYwvEPNZhCLhS2NbcoeO8sl8YRv1e5zwWQ1Gwth/b.sowQNwVe2")
        .image("https://avatars.githubusercontent.com/u/23521870?v=4")
        .bio("반갑습니다.")
        .build();

    public static final User USER2 = User.builder()
        .id(2L)
        .email("woojin7124@naver.com")
        .username("woojin")
        .password("$2a$10$YuVxABvHDPL3oIVUuFeXGuLugm.UNDT32jUS5KaHpVf1JGdrSi8fu")
        .image("https://avatars.githubusercontent.com/u/39071798?v=4")
        .bio("안녕하세요.")
        .build();
}
