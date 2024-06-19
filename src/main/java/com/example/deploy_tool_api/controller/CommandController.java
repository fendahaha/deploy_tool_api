package com.example.deploy_tool_api.controller;

import com.example.deploy_tool_api.command.CommandsExecResult;
import com.example.deploy_tool_api.command.CommandsExec;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("/command")
@RestController
public class CommandController {

    @Resource(name = "apiCommands")
    CommandsExec apiCommands;
    @Resource(name = "frontCommands")
    CommandsExec frontCommands;

    @GetMapping("/exec/{program}/{cmd}")
    public CommandsExecResult exec(@PathVariable String program, @PathVariable String cmd) {
        if (program.equals("api")) {
            return apiCommands.exec(cmd);
        }
        if (program.equals("front")) {
            return frontCommands.exec(cmd);
        }
        return null;
    }

    @GetMapping("/status/{program}")
    public Object status(@PathVariable String program) {
        if (program.equals("api")) {
            return status_result(apiCommands.is_running());
        }
        if (program.equals("front")) {
            return status_result(frontCommands.is_running());
        }
        return null;
    }

    public HashMap<String, Boolean> status_result(boolean state) {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("running", state);
        return map;
    }
}
