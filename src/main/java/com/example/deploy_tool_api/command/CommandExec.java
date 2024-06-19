package com.example.deploy_tool_api.command;

import java.io.File;
import java.io.IOException;


public class CommandExec {
    Runtime runtime = Runtime.getRuntime();
    String workDir = null;

    public CommandExec() {
    }

    public CommandExec(String workDir) {
        this.workDir = workDir;
    }

    public Process exec(String command) {
        try {
            File dir = null;
            if (workDir != null) {
                dir = new File(workDir);
            }
            return runtime.exec(command, null, dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
