package com.jeeneee.realworld.user.service;

import static com.jeeneee.realworld.user.domain.UserTest.EMAIL;
import static com.jeeneee.realworld.user.domain.UserTest.PASSWORD;
import static com.jeeneee.realworld.user.domain.UserTest.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder, tokenProvider);

        user = User.builder()
            .email(EMAIL)
            .username(USERNAME)
            .password(PASSWORD)
            .build();
    }

    @DisplayName("회원가입 - 이미 존재하는 유저명인 경우 예외 발생")
    @Test
    void register_UsernameExist_ExceptionThrown() {
        RegisterRequest request = RegisterRequest.builder()
            .email(EMAIL)
            .username(USERNAME)
            .password(PASSWORD)
            .build();
        given(passwordEncoder.encode(any())).willReturn("password");
        given(userRepository.existsByUsername(any())).willReturn(true);

        assertThatThrownBy(() -> userService.register(request))
            .isInstanceOf(DuplicateUsernameException.class)
            .hasMessageContaining("The username is already being used.");
    }

    @DisplayName("회원가입 - 이미 존재하는 이메일인 경우 예외 발생")
    @Test
    void register_EmailExist_ExceptionThrown() {
        RegisterRequest request = RegisterRequest.builder()
            .email(EMAIL)
            .username(USERNAME)
            .password(PASSWORD)
            .build();
        given(passwordEncoder.encode(any())).willReturn("password");
        given(userRepository.existsByUsername(any())).willReturn(false);
        given(userRepository.existsByEmail(any())).willReturn(true);

        assertThatThrownBy(() -> userService.register(request))
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessageContaining("The email is already being used.");
    }

    @DisplayName("회원가입")
    @Test
    void register_Normal_Success() {
        RegisterRequest request = RegisterRequest.builder()
            .email(EMAIL)
            .username(USERNAME)
            .password(PASSWORD)
            .build();
        given(passwordEncoder.encode(any())).willReturn("password");
        given(userRepository.existsByUsername(any())).willReturn(false);
        given(userRepository.existsByEmail(any())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(user);

        UserResponse response = userService.register(request);

        assertThat(response.getEmail()).isEqualTo(EMAIL);
        assertThat(response.getUsername()).isEqualTo(USERNAME);
    }

    @DisplayName("로그인 - 해당 이메일이 존재하지 않는 경우 예외 발생")
    @Test
    void login_EmailNotExist_ExceptionThrown() {
        LoginRequest request = LoginRequest.builder()
            .email(EMAIL)
            .password(PASSWORD)
            .build();
        given(userRepository.findByEmail(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login(request))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("The user does not exist.");
    }

    @DisplayName("로그인 - 비밀번호가 틀렸을 경우 예외 발생")
    @Test
    void login_WrongPassword_ExceptionThrown() {
        LoginRequest request = LoginRequest.builder()
            .email(EMAIL)
            .password(PASSWORD)
            .build();
        given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(any(), any())).willReturn(false);

        assertThatThrownBy(() -> userService.login(request))
            .isInstanceOf(WrongPasswordException.class)
            .hasMessageContaining("wrong password");
    }

    @DisplayName("로그인")
    @Test
    void login_Normal_Success() {
        LoginRequest request = LoginRequest.builder()
            .email(EMAIL)
            .password(PASSWORD)
            .build();
        given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(any(), any())).willReturn(true);
        given(tokenProvider.generate(any())).willReturn("abc.def.ghi");

        UserResponse response = userService.login(request);

        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getUsername()).isEqualTo(user.getUsername());
        assertThat(response.getToken()).isEqualTo("abc.def.ghi");
    }

    @DisplayName("회원 정보 수정 - 이미 존재하는 유저명인 경우 예외 발생")
    @Test
    void update_UsernameExist_ExceptionThrown() {
        UpdateRequest request = UpdateRequest.builder()
            .username("tester")
            .build();
        given(userRepository.existsByUsername(any())).willReturn(true);

        assertThatThrownBy(() -> userService.update(request, user))
            .isInstanceOf(DuplicateUsernameException.class);
    }

    @DisplayName("회원 정보 수정 - 이미 존재하는 이메일인 경우 예외 발생")
    @Test
    void update_EmailExist_ExceptionThrown() {
        UpdateRequest request = UpdateRequest.builder()
            .email("test@test.com")
            .username("tester")
            .build();
        given(userRepository.existsByUsername(any())).willReturn(false);
        given(userRepository.existsByEmail(any())).willReturn(true);

        assertThatThrownBy(() -> userService.update(request, user))
            .isInstanceOf(DuplicateEmailException.class);
    }

    @DisplayName("회원 정보 수정")
    @Test
    void update_Normal_Success() {
        UpdateRequest request = UpdateRequest.builder()
            .email("test@test.com")
            .username("테스터")
            .password("password")
            .image("https://avatars.githubusercontent.com/u/23521870?v=4")
            .bio("반갑습니다.")
            .build();
        given(userRepository.existsByUsername(any())).willReturn(false);
        given(userRepository.existsByEmail(any())).willReturn(false);
        given(passwordEncoder.encode(any())).willReturn("encoded.password");
        given(tokenProvider.generate(any())).willReturn("abc.def.ghi");

        UserResponse response = userService.update(request, user);

        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getUsername()).isEqualTo(request.getUsername());
        assertThat(user.getPassword()).isEqualTo("encoded.password");
        assertThat(user.getImage()).isEqualTo(request.getImage());
        assertThat(user.getBio()).isEqualTo(request.getBio());
        assertThat(response.getToken()).isEqualTo("abc.def.ghi");
    }
}