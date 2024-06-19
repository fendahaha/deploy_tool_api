package com.example.deploy_tool_api.controller;

import com.example.deploy_tool_api.command.CommandExecResult;
import com.example.deploy_tool_api.command.Program;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("/command")
@RestController
public class CommandController {

    @Resource(name = "apiProgram")
    Program apiProgram;
    @Resource(name = "frontProgram")
    Program frontProgram;

    @GetMapping("/exec/{program}/{cmd}")
    public CommandExecResult exec(@PathVariable String program, @PathVariable String cmd) {
        if (program.equals("api")) {
            return apiProgram.exec(cmd);
        }
        if (program.equals("front")) {
            return frontProgram.exec(cmd);
        }
        return null;
    }

    @GetMapping("/status/{program}")
    public Object status(@PathVariable String program) {
        if (program.equals("api")) {
            return status_result(apiProgram.is_running());
        }
        if (program.equals("front")) {
            return status_result(frontProgram.is_running());
        }
        return null;
    }

    public HashMap<String, Boolean> status_result(boolean state) {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("running", state);
        return map;
    }
}
