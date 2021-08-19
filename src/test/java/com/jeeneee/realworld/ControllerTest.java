package com.jeeneee.realworld;

import static com.jeeneee.realworld.fixture.UserFixture.USER1;
import static com.jeeneee.realworld.infra.security.JwtAuthenticationFilter.AUTHORIZATION_HEADER;
import static com.jeeneee.realworld.infra.security.TokenProvider.HEADER_PREFIX;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeneee.realworld.infra.security.JwtAuthenticationFilter;
import com.jeeneee.realworld.infra.security.LoginUserMethodArgumentResolver;
import com.jeeneee.realworld.infra.security.OptionalUserMethodArgumentResolver;
import com.jeeneee.realworld.infra.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(RestDocumentationExtension.class)
public class ControllerTest {

    protected static final String TOKEN = "abc.def.ghi";
    protected static final String AUTHORIZATION_HEADER_NAME = AUTHORIZATION_HEADER;
    protected static final String AUTHORIZATION_HEADER_VALUE = HEADER_PREFIX + TOKEN;

    @MockBean
    protected TokenProvider tokenProvider;

    @MockBean
    protected LoginUserMethodArgumentResolver loginUserResolver;

    @MockBean
    protected OptionalUserMethodArgumentResolver optionalUserResolver;

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @BeforeEach
    protected void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        JwtAuthenticationFilter jwtAuthenticationFilter = webApplicationContext
            .getBean("jwtAuthenticationFilter", JwtAuthenticationFilter.class);
        objectMapper = webApplicationContext.getBean("mappingJackson2HttpMessageConverter",
            MappingJackson2HttpMessageConverter.class).getObjectMapper();

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .addFilters(jwtAuthenticationFilter)
            .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                .withRequestDefaults(prettyPrint(), removeHeaders("Content-Length", "Host"))
                .withResponseDefaults(prettyPrint(), removeHeaders("Content-Length", "Vary"))
            )
            .alwaysDo(print())
            .build();

        given(loginUserResolver.supportsParameter(any())).willReturn(true);
        given(loginUserResolver.resolveArgument(any(), any(), any(), any())).willReturn(USER1);
        given(optionalUserResolver.supportsParameter(any())).willReturn(true);
        given(optionalUserResolver.resolveArgument(any(), any(), any(), any())).willReturn(USER1);
    }
}
