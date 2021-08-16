package com.jeeneee.realworld.user.service;

import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.domain.UserRepository;
import com.jeeneee.realworld.user.dto.ProfileResponse;
import com.jeeneee.realworld.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileResponse find(String username, User user) {
        User target = getUserByUsername(username);
        return ProfileResponse.of(target, user);
    }

    @Transactional
    public ProfileResponse follow(String username, User user) {
        User target = getUserByUsername(username);
        user.follow(target);
        return ProfileResponse.of(target, user);
    }

    @Transactional
    public ProfileResponse unfollow(String username, User user) {
        User target = getUserByUsername(username);
        user.unfollow(target);
        return ProfileResponse.of(target, user);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
    }
}
