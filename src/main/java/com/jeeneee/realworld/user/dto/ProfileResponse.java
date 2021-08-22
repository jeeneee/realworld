package com.jeeneee.realworld.user.dto;

import com.jeeneee.realworld.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileResponse {

    private ProfileInfo profile;

    public static ProfileResponse of(User target, User user) {
        return new ProfileResponse(ProfileInfo.of(target, user));
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ProfileInfo {

        private String username;
        private String bio;
        private String image;
        private boolean following;

        public static ProfileInfo of(User target, User user) {
            boolean following = user != null && user.followed(target);
            return ProfileInfo.builder()
                .username(target.getUsername())
                .bio(target.getBio())
                .image(target.getImage())
                .following(following)
                .build();
        }
    }
}
