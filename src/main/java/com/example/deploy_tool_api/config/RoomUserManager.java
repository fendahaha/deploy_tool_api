package com.example.deploy_tool_api.config;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class RoomUserManager {
    private static final ConcurrentHashMap<String, HashMap<String, HashMap<String, Boolean>>> map;

    static {
        map = new ConcurrentHashMap<String, HashMap<String, HashMap<String, Boolean>>>();
    }

    public static void addUser(String groupId, String roomId, String sessionId) {
        HashMap<String, HashMap<String, Boolean>> group = map.get(groupId);
        HashMap<String, Boolean> room;
        if (group == null) {
            room = createRoom(groupId, roomId);
        } else {
            room = group.get(roomId);
            if (room == null) {
                room = createRoom(groupId, roomId);
            }
        }
        room.put(sessionId, true);
    }

    public static void removeUser(String groupId, String roomId, String sessionId) {
        HashMap<String, HashMap<String, Boolean>> group = map.get(groupId);
        if (group != null) {
            HashMap<String, Boolean> room = group.get(roomId);
            if (room != null) {
                room.remove(sessionId);
            }
        }
    }

    public static synchronized HashMap<String, Boolean> createRoom(String groupId, String roomId) {
        HashMap<String, HashMap<String, Boolean>> group = map.computeIfAbsent(groupId, k -> new HashMap<String, HashMap<String, Boolean>>());
        HashMap<String, Boolean> room = group.computeIfAbsent(roomId, k -> new HashMap<String, Boolean>());
        return room;
    }

    public static Integer getRoomUsers(String groupId, String roomId) {
        HashMap<String, HashMap<String, Boolean>> group = map.get(groupId);
        if (group != null) {
            HashMap<String, Boolean> room = group.get(roomId);
            if (room != null) {
                return room.values().stream().filter(e -> e).toList().size();
            }
        }
        return 0;
    }

    public static Object groups() {
        return map;
    }

    public static Object rooms(String groupId) {
        return map.get(groupId);
    }
}
