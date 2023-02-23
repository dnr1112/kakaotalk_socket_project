package com.kakaotalk.server;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Room {
    private String roomName;
    private String roomOwner;
    private List<String> userList;

    public Room(String roomName, String roomOwner) {
        this(roomName, roomOwner, new ArrayList<>());
        this.userList.add(roomOwner);
    }
}