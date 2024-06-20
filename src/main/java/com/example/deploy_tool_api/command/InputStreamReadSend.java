package com.example.deploy_tool_api.command;

import com.example.deploy_tool_api.entity.ChatMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class InputStreamReadSend implements Runnable {
    private InputStream inputStream;
    private SimpMessagingTemplate template;

    String groupId = "file_watch";
    String roomId;
    String user = "system";

    public InputStreamReadSend(InputStream inputStream, SimpMessagingTemplate template, String roomId) {
        this.inputStream = inputStream;
        this.template = template;
        this.roomId = roomId;
    }

    public void send(String text) {
        ChatMessage chatMessage = new ChatMessage(user, text);
        this.template.convertAndSend("/topic/chats/" + groupId + "/" + roomId, chatMessage);
    }

    public void read1() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                send(line);
            }
        } catch (IOException e) {
            System.out.println("read error");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void read2() {
        BufferedInputStream stream = null;
        try {
            stream = new BufferedInputStream(inputStream);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b, 0, b.length)) > 0) {
                String s = new String(b, 0, n, StandardCharsets.UTF_8);
                send(s);
            }
        } catch (IOException e) {
            System.out.println("read error");
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        read2();
    }
}
