package com.jeeneee.realworld.descriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class ProfileFieldDescriptor {

    public static final FieldDescriptor[] profile = new FieldDescriptor[] {
        fieldWithPath("username").type(JsonFieldType.STRING).description("유저명"),
        fieldWithPath("bio").type(JsonFieldType.STRING).description("자기소개"),
        fieldWithPath("image").type(JsonFieldType.STRING).description("이미지"),
        fieldWithPath("following").type(JsonFieldType.BOOLEAN).description("팔로잉 여부")
    };
}
