package com.kakaotalk.serverDto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data

public class CreateReqDto {
    private String roomName;
    private String userName;
    private List<String> userList = new ArrayList<>();

    public CreateReqDto(String roomName, String userName, List<String> userList) {
        this.roomName = roomName;
        this.userName = userName;
        this.userList = userList;
        //this.userList.add(userName); // 첫 번째 유저 추가
    }
}