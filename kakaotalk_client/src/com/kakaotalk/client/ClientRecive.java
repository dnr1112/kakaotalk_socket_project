package com.kakaotalk.client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.google.gson.Gson;
import com.kakaotalk.clientDto.CreateRespDto;
import com.kakaotalk.clientDto.JoinRespDto;
import com.kakaotalk.clientDto.MessageRespDto;
import com.kakaotalk.clientDto.ResponseDto;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor

public class ClientRecive extends Thread {
	private final Socket socket;
	private InputStream inputStream;
	private Gson gson;
	private static CreateRespDto createRespDto;

		 @Override
		public void run() {
			
			 try {
				inputStream = socket.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
				gson = new Gson();
				
				while(true) {
					
					String request = in.readLine();
					ResponseDto responseDto = gson.fromJson(request, ResponseDto.class);
					switch (responseDto.getResource()) {
						
						case "join" :
								JoinRespDto joinRespDto = gson.fromJson(responseDto.getBody(), JoinRespDto.class);
								KakaoClient.getInstance().getUserListModel().clear();
								//KakaoClient.getInstance().getUserListModel().size();
								KakaoClient.getInstance().getUserListModel().addElement("접속 유저 현황"+"("+joinRespDto.getConnectedUsers().size()+"명)");
								KakaoClient.getInstance().getUserListModel().addAll(joinRespDto.getConnectedUsers());
								System.out.println(joinRespDto);
								break;
					
						case "create" :
								createRespDto = gson.fromJson(responseDto.getBody(), CreateRespDto.class);
								KakaoClient.getInstance().getChattingListModel().clear();
								KakaoClient.getInstance().getChattingListModel().addElement("--- 채팅방 목록 ---");
								KakaoClient.getInstance().getChattingListModel().addAll(createRespDto.getConnectedRooms());
								System.out.println(createRespDto);
								//KakaoClient.getInstance().getChattingList().setSelectedIndex(0);
								break;
						case "sendMessage" :
								MessageRespDto messageRespDto = gson.fromJson(responseDto.getBody(),MessageRespDto.class);
								KakaoClient.getInstance().getContentView().append(messageRespDto.getMessageValue()+ "\n");
								System.out.println(messageRespDto);
					}
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
}
