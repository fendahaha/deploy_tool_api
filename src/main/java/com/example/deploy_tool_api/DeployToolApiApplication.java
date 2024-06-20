package com.example.deploy_tool_api;

import com.example.deploy_tool_api.command.ProgramsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({ProgramsProperties.class})
@SpringBootApplication
public class DeployToolApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeployToolApiApplication.class, args);
    }

}
