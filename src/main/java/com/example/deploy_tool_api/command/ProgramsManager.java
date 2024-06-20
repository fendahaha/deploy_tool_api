package com.example.deploy_tool_api.command;

import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class ProgramsManager {

    HashMap<String, Program> programs = new HashMap<>();

    public ProgramsManager(ProgramsProperties programsProperties, MessageSender messageSender) {
        Map<String, Program> collects = programsProperties.getPrograms().stream().map(p -> {
            return new Program(p.getName(), p.getLogFile(), p.getLogFileSendTo(), p.getCommandsDir(), p.getCommands(),
                    Executors.newFixedThreadPool(2), messageSender);
        }).collect(Collectors.toMap(Program::getName, p -> p));
        programs.putAll(collects);
    }

    public CommandExecResult exec(String programName, String cmd) throws Exception {
        Program program = programs.get(programName);
        if (program != null) {
            return program.exec(cmd);
        }
        throw new Exception(programName + " program not exist");
    }

    public HashMap<String, Boolean> status(String programName) throws Exception {
        Program program = programs.get(programName);
        if (program != null) {
            return status_result(program.is_running());
        }
        throw new Exception(programName + " program not exist");
    }

    public HashMap<String, Boolean> status_result(boolean state) {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("running", state);
        return map;
    }

    @PostConstruct
    public void watch() {
        programs.forEach((k, p) -> {
            p.start_watch();
        });
    }
}
