package com.example.deploy_tool_api.command;

import com.example.deploy_tool_api.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    String groupId = "file_watch";
    String user = "system";
    private final SimpMessagingTemplate template;

    @Autowired
    public MessageSender(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void send(String roomId, String text) {
        ChatMessage chatMessage = new ChatMessage(user, text);
        this.template.convertAndSend("/topic/chats/" + groupId + "/" + roomId, chatMessage);
    }
}
