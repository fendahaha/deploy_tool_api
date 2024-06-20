package com.example.deploy_tool_api.command;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;

public class Program {
    String name;
    String logFile;
    String logFileSendTo;
    String commandsDir;
    HashMap<String, String> commands;
    Boolean running = false;
    CommandExec commandExec;
    Process cmdProcess;
    ExecutorService executorService;
    MessageSender messageSender;

    public synchronized CommandExecResult exec(String cmd) {
        if (!running) {
            String s = commands.get(cmd);
            if (s != null) {
                cmdProcess = commandExec.exec(s);
                if (cmdProcess != null) {
                    running = true;
                    executorService.submit(this::finishedCallback);
                    return new CommandExecResult(true, true);
                }
            }
        }
        return new CommandExecResult(false, running);
    }

    public boolean is_running() {
        if (cmdProcess != null) {
            return cmdProcess.isAlive();
        }
        return false;
    }

    public void finishedCallback() {
        try {
            cmdProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cmdProcess.destroyForcibly();
        running = false;
    }

    public void start_watch() {
        FileReadSendProcess fileReadSendProcess = new FileReadSendProcess(logFile, logFileSendTo, messageSender);
        executorService.submit(fileReadSendProcess);
    }

    public Program(String name, String logFile, String logFileSendTo, String commandsDir, HashMap<String, String> commands, ExecutorService executorService, MessageSender messageSender) {
        this.name = name;
        this.logFile = logFile;
        this.logFileSendTo = logFileSendTo;
        this.commandsDir = commandsDir;
        this.commands = commands;
        this.executorService = executorService;
        this.commandExec = new CommandExec(commandsDir);
        this.messageSender = messageSender;
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

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public Process getCmdProcess() {
        return cmdProcess;
    }

    public void setCmdProcess(Process cmdProcess) {
        this.cmdProcess = cmdProcess;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
