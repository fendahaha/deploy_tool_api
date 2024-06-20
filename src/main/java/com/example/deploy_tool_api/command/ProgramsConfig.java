package com.example.deploy_tool_api.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ProgramsConfig {
    @Autowired
    ProgramsProperties programsProperties;

    @Autowired
    MessageSender messageSender;

    @Bean("programsManager")
    public ProgramsManager programsManager() {
        ObjectMapper objectMapper = new ObjectMapper();
        String s = null;
        try {
            s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(programsProperties);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(s);
        return new ProgramsManager(programsProperties, messageSender);
    }
}
