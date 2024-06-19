package com.example.deploy_tool_api.command;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class Configs {

//    CommandExec commandExec = new CommandExec("D:\\develop\\project\\company-project\\siamsport.io\\wf-score-x-front");
    CommandExec commandExec = new CommandExec("/opt/project/app");

    @Bean(name = "apiProgram")
    public Program apiProgram() {
        HashMap<String, String> map = new HashMap<>();
        map.put("start", "/bin/sh service.sh api start");
        map.put("stop", "/bin/sh service.sh api stop");
        map.put("pull_restart", "/bin/sh service.sh api stop pull start");
        return new Program(commandExec, map);
    }


    @Bean(name = "frontProgram")
    public Program frontProgram() {
        HashMap<String, String> map = new HashMap<>();
//        map.put("install", "D:\\develop\\language\\nodejs\\npm.cmd install > D:\\develop\\project\\company-project\\deploy_tool_api\\1.txt 2>&1");
//        map.put("build", "D:\\develop\\language\\nodejs\\npm.cmd run build > D:\\develop\\project\\company-project\\deploy_tool_api\\1.txt 2>&1");
        map.put("install", "/bin/sh service.sh front install");
        map.put("build", "/bin/sh service.sh front build");
        map.put("start", "/bin/sh service.sh front start");
        map.put("stop", "/bin/sh service.sh front stop");
        map.put("pull_restart", "/bin/sh service.sh front pull install stop build start");
        return new Program(commandExec, map);
    }

}
