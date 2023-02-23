package com.kakaotalk.server;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Room {
    private String roomName; // 방이름
    private String roomOwner; // 방장 이름
    private List<String> userList; // 참가자 목록
    
    

    public Room(String roomName, String roomOwner) {
        this.roomName = roomName;
        this.roomOwner = roomOwner;
        this.userList = new ArrayList<>();
    }

	
}
