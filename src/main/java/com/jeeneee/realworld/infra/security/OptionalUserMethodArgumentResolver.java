package com.jeeneee.realworld.infra.security;

import com.jeeneee.realworld.user.domain.User;
import com.jeeneee.realworld.user.domain.UserRepository;
import com.jeeneee.realworld.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class OptionalUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OptionalUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String token = (String) authentication.getCredentials();
        String email = (String) authentication.getPrincipal();

        if (StringUtils.isEmpty(token)) {
            return null;
        }

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        user.setToken(token);
        return user;
    }
}
