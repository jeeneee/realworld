package com.jeeneee.realworld.infra.config;

import com.jeeneee.realworld.infra.log.MessageFormattingStrategyImpl;
import com.p6spy.engine.spy.P6SpyOptions;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class P6spyLogMessageFormatConfig {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance()
            .setLogMessageFormat(MessageFormattingStrategyImpl.class.getName());
    }
}
