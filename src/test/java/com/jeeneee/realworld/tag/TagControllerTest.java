package com.jeeneee.realworld.tag;

import static com.jeeneee.realworld.fixture.TagFixture.TAG1;
import static com.jeeneee.realworld.fixture.TagFixture.TAG2;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jeeneee.realworld.ControllerTest;
import com.jeeneee.realworld.tag.controller.TagController;
import com.jeeneee.realworld.tag.dto.MultipleTagResponse;
import com.jeeneee.realworld.tag.service.TagService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TagController.class)
class TagControllerTest extends ControllerTest {

    @MockBean
    private TagService tagService;

    @DisplayName("태그 전체 조회")
    @Test
    void findAll() throws Exception {
        List<String> list = List.of(TAG1.getName(), TAG2.getName());
        MultipleTagResponse response = new MultipleTagResponse(list);
        given(tagService.findAll()).willReturn(response);

        ResultActions result = mockMvc.perform(
            get("/api/tags")
                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
        );

        result.andExpect(status().isOk())
            .andDo(
                document("tag/find-all",
                    requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER_NAME).description("토큰").optional()
                    ),
                    responseFields(
                        fieldWithPath("tags").type(JsonFieldType.ARRAY).description("태그 목록")
                    )
                )
            );
    }
}