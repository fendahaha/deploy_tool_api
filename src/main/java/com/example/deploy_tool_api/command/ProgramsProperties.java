package com.example.deploy_tool_api.command;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;

@ConfigurationProperties("programs-properties")
public class ProgramsProperties {
    ArrayList<ProgramProperties> programs;

    public ArrayList<ProgramProperties> getPrograms() {
        return programs;
    }

    public void setPrograms(ArrayList<ProgramProperties> programs) {
        this.programs = programs;
    }

    static class ProgramProperties {
        String name;
        String logFile;
        String logFileSendTo;
        String commandsDir;
        HashMap<String, String> commands;

        public ProgramProperties() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogFile() {
            return logFile;
        }

        public void setLogFile(String logFile) {
            this.logFile = logFile;
        }

        public String getLogFileSendTo() {
            return logFileSendTo;
        }

        public void setLogFileSendTo(String logFileSendTo) {
            this.logFileSendTo = logFileSendTo;
        }

        public String getCommandsDir() {
            return commandsDir;
        }

        public void setCommandsDir(String commandsDir) {
            this.commandsDir = commandsDir;
        }

        public HashMap<String, String> getCommands() {
            return commands;
        }

        public void setCommands(HashMap<String, String> commands) {
            this.commands = commands;
        }
    }
}
