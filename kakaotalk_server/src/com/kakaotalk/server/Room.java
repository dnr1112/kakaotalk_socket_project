package com.kakaotalk.server;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Room {
    private String roomName;
    private String roomOwner;
    private List<ConnectedSocket> userList = new ArrayList<>();

    public Room(String roomName, String roomOwner, ConnectedSocket selectUser) {
    	this.roomName = roomName;
    	this.roomOwner = roomOwner;
    	userList.add(selectUser);
    }
    
    public static Room findRoomByName(List<Room> rooms, String roomName) {
        for (Room room : rooms) {
            if (room.getRoomName().equals(roomName)) {
                return room;
            }
        }
        return null;
    }
    
    public void addUser(ConnectedSocket socket) {
        userList.add(socket);
    }
    
    
}