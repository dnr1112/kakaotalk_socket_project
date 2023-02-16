package com.kakaotalk.clientDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateRespDto {
	
	
	private List<String> connectedRooms;  // 생성된 방의 이름을 List에 담는다.
}
