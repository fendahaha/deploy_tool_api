package com.example.deploy_tool_api.entity;

public class ChatMessage {
    public String user;
    public String text;

    public Long time;

    public ChatMessage() {
    }

    public ChatMessage(String text) {
        this.text = text;
    }

    public ChatMessage(String user, String text) {
        this.user = user;
        this.text = text;
    }

    public ChatMessage(String user, String text, Long time) {
        this.user = user;
        this.text = text;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
