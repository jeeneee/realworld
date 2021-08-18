package com.jeeneee.realworld.descriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class CommentFieldDescriptor {

    public static final FieldDescriptor[] comment = new FieldDescriptor[] {
        fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일"),
        fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정일"),
        fieldWithPath("body").type(JsonFieldType.STRING).description("내용"),
        fieldWithPath("author").type(JsonFieldType.OBJECT).description("작성자 프로필")
    };
}
