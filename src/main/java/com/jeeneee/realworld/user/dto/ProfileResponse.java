package com.jeeneee.realworld.user.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.jeeneee.realworld.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonRootName("profile")
public class ProfileResponse {

    private String username;
    private String bio;
    private String image;
    private boolean following;

    public static ProfileResponse of(User target, User user) {
        boolean following = user != null && user.followed(target);
        return ProfileResponse.builder()
            .username(target.getUsername())
            .bio(target.getBio())
            .image(target.getImage())
            .following(following)
            .build();
    }

    public static ProfileResponse of(User target, boolean following) {
        return ProfileResponse.builder()
            .username(target.getUsername())
            .bio(target.getBio())
            .image(target.getImage())
            .following(following)
            .build();
    }
}
