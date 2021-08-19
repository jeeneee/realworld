package com.jeeneee.realworld.user.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@JsonTypeName("user")
public class UserResponse {

    private String email;
    private String token;
    private String username;
    private String bio;
    private String image;

    public static UserResponse of(User user) {
        return UserResponse.builder()
            .email(user.getEmail())
            .token(user.getToken())
            .username(user.getUsername())
            .bio(user.getBio())
            .image(user.getImage())
            .build();
    }
}
