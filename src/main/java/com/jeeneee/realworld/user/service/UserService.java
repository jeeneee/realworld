package com.jeeneee.realworld.user.service;

import com.jeeneee.realworld.infra.security.TokenProvider;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.domain.UserRepository;
import com.jeeneee.realworld.user.dto.LoginRequest;
import com.jeeneee.realworld.user.dto.RegisterRequest;
import com.jeeneee.realworld.user.dto.UpdateRequest;
import com.jeeneee.realworld.user.dto.UserResponse;
import com.jeeneee.realworld.user.exception.DuplicateEmailException;
import com.jeeneee.realworld.user.exception.DuplicateUsernameException;
import com.jeeneee.realworld.user.exception.UserNotFoundException;
import com.jeeneee.realworld.user.exception.WrongPasswordException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        User user = request.toEntity(passwordEncoder);
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        return UserResponse.of(userRepository.save(user));
    }

    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }
        user.setToken(tokenProvider.generate(request.getEmail()));
        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse update(UpdateRequest request, User user) {
        Optional.ofNullable(request.getUsername()).ifPresent(this::validateUsername);
        Optional.ofNullable(request.getEmail()).ifPresent(this::validateEmail);
        Optional.ofNullable(request.getPassword()).ifPresent(
            password -> request.setPassword(passwordEncoder.encode(request.getPassword())));

        user.update(request.getEmail(), request.getUsername(), request.getPassword(),
            request.getImage(), request.getBio());

        Optional.ofNullable(request.getEmail())
            .ifPresent(email -> user.setToken(tokenProvider.generate(email)));
        return UserResponse.of(user);
    }

    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException();
        }
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
    }
}
