package com.jeeneee.realworld.user.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@ToString
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@JsonTypeName("user")
public class UserUpdateRequest {

    @Length(min = 2, max = 20, message = "username should be between 2-20")
    private String username;

    @Email(message = "email address is not in a valid format.")
    private String email;

    @Length(min = 8, max = 20, message = "password should be between 8-20")
    private String password;

    @URL
    private String image;

    private String bio;

    public void setPassword(String password) {
        this.password = password;
    }
}
