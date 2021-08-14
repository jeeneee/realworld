package com.jeeneee.realworld.user.controller;

import com.jeeneee.realworld.infra.security.LoginUser;
import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.dto.LoginRequest;
import com.jeeneee.realworld.user.dto.RegisterRequest;
import com.jeeneee.realworld.user.dto.UpdateRequest;
import com.jeeneee.realworld.user.dto.UserResponse;
import com.jeeneee.realworld.user.service.UserService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.created(URI.create("/api/profiles/" + request.getUsername()))
            .body(userService.register(request));
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok().body(userService.login(request));
    }

    @GetMapping("/api/user")
    public ResponseEntity<UserResponse> find(@LoginUser User user) {
        return ResponseEntity.ok().body(UserResponse.of(user));
    }

    @PutMapping("/api/user")
    public ResponseEntity<UserResponse> update(@Valid @RequestBody UpdateRequest request,
        @LoginUser User user) {
        return ResponseEntity.ok().body(userService.update(request, user));
    }
}
