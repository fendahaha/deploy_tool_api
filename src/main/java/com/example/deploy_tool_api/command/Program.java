package com.example.deploy_tool_api.command;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Program {
    Boolean running = false;
    CommandExec commandExec;
    HashMap<String, String> map;

    Process currProcess;
    ExecutorService executorService;

    public Program(CommandExec commandExec, HashMap<String, String> map) {
        this.commandExec = commandExec;
        this.map = map;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public synchronized CommandExecResult exec(String cmd) {
        if (!running) {
            String s = map.get(cmd);
            if (s != null) {
                currProcess = commandExec.exec(s);
                if (currProcess != null) {
                    running = true;
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            finishedCallback();
                        }
                    });
                    return new CommandExecResult(true, true);
                }
            }
        }
        return new CommandExecResult(false, running);
    }

    public boolean is_running() {
        if (currProcess != null) {
            return currProcess.isAlive();
        }
        return false;
    }

    public void finishedCallback() {
        try {
            currProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currProcess.destroyForcibly();
        running = false;
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }
}