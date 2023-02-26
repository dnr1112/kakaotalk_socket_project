package com.kakaotalk.server;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Room {
    private String roomName;
    private String roomOwner;
    private List<ConnectedSocket> selectedRoomList = new ArrayList<>();

    public Room(String roomName, String roomOwner, ConnectedSocket selectedRoomList) {
        this.roomName = roomName;
        this.roomOwner = roomOwner;
        
    }
    
    public void addUser(ConnectedSocket joinSocket) {
    	selectedRoomList.add(joinSocket);
    }

}   