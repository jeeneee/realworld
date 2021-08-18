package com.jeeneee.realworld.tag;

import com.jeeneee.realworld.tag.dto.MultipleTagResponse;
import com.jeeneee.realworld.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TagController {

    private final TagService tagService;

    @GetMapping("/api/tags")
    public ResponseEntity<MultipleTagResponse> findAll() {
        return ResponseEntity.ok().body(tagService.findAll());
    }
}
