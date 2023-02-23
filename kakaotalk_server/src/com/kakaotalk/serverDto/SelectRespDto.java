package com.kakaotalk.serverDto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SelectRespDto {
    private List<String> selectedUserList;
    
    public SelectRespDto() {
        selectedUserList = new ArrayList<>();
    }
}
