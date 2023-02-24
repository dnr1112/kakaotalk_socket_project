package com.kakaotalk.serverDto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CreateReqDto {
    private String roomName;
    private String userName;
    private List<String> userList;

    public CreateReqDto(String roomName, String userName) {
        this.roomName = roomName;
        this.userName = userName;
        this.userList = new ArrayList<>();
    }

    public void addUser(String user) {
        this.userList.add(user);
    }
}