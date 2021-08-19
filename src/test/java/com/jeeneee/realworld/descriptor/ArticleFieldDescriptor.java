package com.jeeneee.realworld.descriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class ArticleFieldDescriptor {

    public static final FieldDescriptor[] article = new FieldDescriptor[] {
        fieldWithPath("slug").type(JsonFieldType.STRING).description("슬러그"),
        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
        fieldWithPath("description").type(JsonFieldType.STRING).description("요약"),
        fieldWithPath("body").type(JsonFieldType.STRING).description("내용"),
        fieldWithPath("tagList").type(JsonFieldType.ARRAY).description("태그"),
        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일"),
        fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정일"),
        fieldWithPath("favorited").type(JsonFieldType.BOOLEAN).description("찜 여부"),
        fieldWithPath("favoritesCount").type(JsonFieldType.NUMBER).description("찜 여부"),
        fieldWithPath("author").type(JsonFieldType.OBJECT).description("작성자 프로필")
    };
}
