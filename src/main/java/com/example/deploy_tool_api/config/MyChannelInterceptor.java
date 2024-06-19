package com.example.deploy_tool_api.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyChannelInterceptor implements ChannelInterceptor {
    public String getRoomId(String destination) {
        String pattern = "(/\\S+)*/room/(\\S+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(destination);
        if (m.find()) {
            return m.group(2);
        }
        return null;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();
        String sessionId = accessor.getSessionId();
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (StompCommand.CONNECT.equals(command)) {
            List<String> groupIds = accessor.getNativeHeader("groupId");
            List<String> roomIds = accessor.getNativeHeader("roomId");
            if (groupIds != null && roomIds != null) {
                String groupId = groupIds.get(0);
                String roomId = roomIds.get(0);
                sessionAttributes.put("groupId", groupId);
                sessionAttributes.put("roomId", roomId);
                RoomUserManager.addUser(groupId, roomId, sessionId);
            }
        }
        if (StompCommand.DISCONNECT.equals(command)) {
            String groupId = (String) sessionAttributes.get("groupId");
            String roomId = (String) sessionAttributes.get("roomId");
            if (groupId != null && roomId != null) {
                RoomUserManager.removeUser(groupId, roomId, sessionId);
            }
        }
        return message;
    }
}
