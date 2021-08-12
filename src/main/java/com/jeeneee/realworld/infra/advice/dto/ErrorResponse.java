package com.jeeneee.realworld.infra.advice.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonRootName("errors")
public class ErrorResponse {

    private final List<String> body;

    public static ErrorResponse from(BindingResult bindingResult) {
        List<String> collect = bindingResult.getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
        return new ErrorResponse(collect);
    }

    public static ErrorResponse from(String message) {
        return new ErrorResponse(List.of(message));
    }
}
