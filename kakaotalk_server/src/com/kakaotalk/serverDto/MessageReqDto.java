package com.kakaotalk.serverDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageReqDto {
	private String toUser;
	private String fromUser;
	private String messageValue;
}
