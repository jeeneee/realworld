package com.jeeneee.realworld.user.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.jeeneee.realworld.user.domain.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonRootName("user")
public class RegisterRequest {

    @NotBlank(message = "username cannot be empty.")
    @Length(min = 2, max = 20, message = "username should be between 2-20")
    private String username;

    @NotBlank(message = "email cannot be empty.")
    @Email(message = "email address is not in a valid format.")
    private String email;

    @NotBlank(message = "password cannot be empty.")
    @Length(min = 8, max = 20, message = "password should be between 8-20")
    private String password;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
            .email(email)
            .username(username)
            .password(passwordEncoder.encode(password))
            .build();
    }
}
