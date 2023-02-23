package com.kakaotalk.clientDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data

public class CreateReqDto {
    private String roomName;
    private String userName;
    
    public String getRoomName() {
        return roomName;
    }
    
    public String getUserName() {
        return userName;
    }
}
