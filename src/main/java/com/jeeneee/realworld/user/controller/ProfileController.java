package com.jeeneee.realworld.user.controller;

import com.jeeneee.realworld.infra.security.LoginUser;
import com.jeeneee.realworld.infra.security.OptionalUser;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.dto.ProfileResponse;
import com.jeeneee.realworld.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/profiles")
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{username}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable String username,
        @OptionalUser User user) {
        return ResponseEntity.ok().body(profileService.find(username, user));
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<ProfileResponse> follow(@PathVariable String username,
        @LoginUser User user) {
        return ResponseEntity.ok().body(profileService.follow(username, user));
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<ProfileResponse> unfollow(@PathVariable String username,
        @LoginUser User user) {
        return ResponseEntity.ok().body(profileService.unfollow(username, user));
    }
}
