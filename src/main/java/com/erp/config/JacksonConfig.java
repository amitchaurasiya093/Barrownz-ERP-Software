package com.erp.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @jakarta.annotation.PostConstruct
    public void setup() {
        Jackson2ObjectMapperBuilder.json()
                .modulesToInstall(JavaTimeModule.class);
    }
}
