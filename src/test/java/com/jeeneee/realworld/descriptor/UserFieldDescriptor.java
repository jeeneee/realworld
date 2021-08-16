package com.jeeneee.realworld.descriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class UserFieldDescriptor {

    public static final FieldDescriptor[] user = new FieldDescriptor[] {
        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
        fieldWithPath("token").type(JsonFieldType.STRING).description("토큰"),
        fieldWithPath("username").type(JsonFieldType.STRING).description("유저명"),
        fieldWithPath("bio").type(JsonFieldType.STRING).description("자기소개"),
        fieldWithPath("image").type(JsonFieldType.STRING).description("이미지")
    };

    public static final FieldDescriptor[] registered_user = new FieldDescriptor[] {
        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
        fieldWithPath("token").type(JsonFieldType.NULL).description("토큰"),
        fieldWithPath("username").type(JsonFieldType.STRING).description("유저명"),
        fieldWithPath("bio").type(JsonFieldType.NULL).description("자기소개"),
        fieldWithPath("image").type(JsonFieldType.NULL).description("이미지")
    };
}
