package com.jeeneee.realworld.tag.service;

import com.jeeneee.realworld.tag.domain.Tag;
import com.jeeneee.realworld.tag.domain.TagRepository;
import com.jeeneee.realworld.tag.dto.MultipleTagResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public List<Tag> findOrSave(List<String> tagList) {
        return tagList.stream()
            .map(name -> tagRepository.findByName(name)
                .orElseGet(() -> tagRepository.save(Tag.create(name))))
            .collect(Collectors.toList());
    }

    public MultipleTagResponse findAll() {
        return new MultipleTagResponse(tagRepository.findAll().stream()
            .map(Tag::getName).collect(Collectors.toList()));
    }
}
