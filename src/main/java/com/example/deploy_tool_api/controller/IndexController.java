package com.example.deploy_tool_api.controller;


import com.example.deploy_tool_api.config.RoomUserManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @GetMapping("")
    public String index() {
        return "index2";
    }

    @ResponseBody
    @GetMapping("/chats")
    public Object groups() {
        return RoomUserManager.groups();
    }

    @ResponseBody
    @GetMapping("/chats/{groupId}")
    public Object rooms(@PathVariable String groupId) {
        return RoomUserManager.rooms(groupId);
    }

    @ResponseBody
    @GetMapping("/chats/{groupId}/{roomId}")
    public Integer roomUsers(@PathVariable String groupId, @PathVariable String roomId) {
        Integer roomUsers = RoomUserManager.getRoomUsers(groupId, roomId);
        return roomUsers;
    }
}
