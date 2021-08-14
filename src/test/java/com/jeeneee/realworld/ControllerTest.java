package com.jeeneee.realworld;

import static com.jeeneee.realworld.fixture.UserFixture.USER1;
import static com.jeeneee.realworld.infra.security.JwtAuthenticationFilter.AUTHORIZATION_HEADER;
import static com.jeeneee.realworld.infra.security.TokenProvider.HEADER_PREFIX;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeneee.realworld.infra.config.JacksonConfig;
import com.jeeneee.realworld.infra.security.JwtAuthenticationFilter;
import com.jeeneee.realworld.infra.security.LoginUserMethodArgumentResolver;
import com.jeeneee.realworld.infra.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@Import(JacksonConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public class ControllerTest {

    protected static final String TOKEN = "abc.def.ghi";
    protected static final String AUTHORIZATION_HEADER_NAME = AUTHORIZATION_HEADER;
    protected static final String AUTHORIZATION_HEADER_VALUE = HEADER_PREFIX + TOKEN;

    @MockBean
    protected TokenProvider tokenProvider;

    @MockBean
    protected LoginUserMethodArgumentResolver resolver;

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @BeforeEach
    protected void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentationContextProvider) {
        JwtAuthenticationFilter jwtAuthenticationFilter = webApplicationContext
            .getBean("jwtAuthenticationFilter", JwtAuthenticationFilter.class);
        objectMapper = webApplicationContext.getBean("mappingJackson2HttpMessageConverter",
            MappingJackson2HttpMessageConverter.class).getObjectMapper();

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .addFilters(jwtAuthenticationFilter)
            .apply(documentationConfiguration(restDocumentationContextProvider))
            .alwaysDo(print())
            .build();

        given(resolver.supportsParameter(any())).willReturn(true);
        given(resolver.resolveArgument(any(), any(), any(), any())).willReturn(USER1);
    }
}