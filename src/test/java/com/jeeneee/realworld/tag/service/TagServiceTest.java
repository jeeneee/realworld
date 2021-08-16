package com.jeeneee.realworld.tag.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.jeeneee.realworld.tag.domain.Tag;
import com.jeeneee.realworld.tag.domain.TagRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    private TagService tagService;

    private List<String> tagList;
    private List<Tag> tags;

    @BeforeEach
    void setUp() {
        tagService = new TagService(tagRepository);

        tagList = List.of("reactjs", "angularjs");
        tags = tagList.stream().map(Tag::create).collect(Collectors.toList());
    }

    @DisplayName("태그 조회/생성 - 태그가 있으면 조회한다.")
    @Test
    void findOrSave_TagsExist_Find() {
        when(tagRepository.findByName(any()))
            .thenReturn(Optional.of(tags.get(0)))
            .thenReturn(Optional.of(tags.get(1)));

        List<Tag> result = tagService.findOrSave(tagList);

        assertThat(result).containsAll(tags);
    }

    @DisplayName("태그 조회/생성 - 태그가 없으면 생성한다.")
    @Test
    void findOrSave_TagsNotExist_Save() {
        when(tagRepository.findByName(any()))
            .thenReturn(Optional.empty())
            .thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class)))
            .thenReturn(tags.get(0))
            .thenReturn(tags.get(1));

        List<Tag> result = tagService.findOrSave(tagList);

        assertThat(result).containsAll(tags);
    }
}