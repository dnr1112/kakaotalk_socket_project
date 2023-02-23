package com.kakaotalk.serverDto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateReqDto {
    private String roomName;
    private String userName;
    private List<String> userList;

    public CreateReqDto(String roomName, String userName) {
        this.roomName = roomName;
        this.userName = userName;
        this.userList = new ArrayList<>();
        this.userList.add(userName); // 첫 번째 유저 추가
    }
}