package com.kakaotalk.clientDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateReqDto {
    private String roomName;
    private String userName;
    private List<String> userList;
    
    public String getRoomName() {
        return roomName;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public List<String> getUserList() {
        return userList;
    }
}