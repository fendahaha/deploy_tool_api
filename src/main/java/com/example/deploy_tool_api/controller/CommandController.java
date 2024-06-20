package com.example.deploy_tool_api.controller;

import com.example.deploy_tool_api.command.CommandExecResult;
import com.example.deploy_tool_api.command.ProgramsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/command")
@RestController
public class CommandController {
    @Autowired
    ProgramsManager programsManager;

    @GetMapping("/exec/{program}/{cmd}")
    public CommandExecResult exec(@PathVariable String program, @PathVariable String cmd) throws Exception {
        return programsManager.exec(program, cmd);
    }

    @GetMapping("/status/{program}")
    public Object status(@PathVariable String program) throws Exception {
        return programsManager.status(program);
    }
}
