package com.kakaotalk.server;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Room {
    private String roomName;
    private String roomOwner;
    private List<ConnectedSocket> userList ;
    
    

    public Room(String roomName, String roomOwner, List<ConnectedSocket> userList) {
		this.roomName = roomName;
		this.roomOwner = roomOwner;
		this.userList = new ArrayList<>();
	}

    public void addUser(ConnectedSocket connectedSocket) {
        userList.add(connectedSocket);
    }
    
    public void removeUser(ConnectedSocket connectedSocket) {
        userList.remove(connectedSocket);
    }

	
}
