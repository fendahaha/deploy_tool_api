package com.example.deploy_tool_api.command;

public class CommandsExecResult {
    Boolean success;
    Boolean running;

    public CommandsExecResult(Boolean success, Boolean running) {
        this.success = success;
        this.running = running;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }
}
