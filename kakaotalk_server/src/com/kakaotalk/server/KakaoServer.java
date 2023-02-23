package com.kakaotalk.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.kakaotalk.serverDto.CreateReqDto;
import com.kakaotalk.serverDto.CreateRespDto;
import com.kakaotalk.serverDto.JoinReqDto;
import com.kakaotalk.serverDto.JoinRespDto;
import com.kakaotalk.serverDto.MessageReqDto;
import com.kakaotalk.serverDto.MessageRespDto;
import com.kakaotalk.serverDto.RequestDto;
import com.kakaotalk.serverDto.ResponseDto;
import com.kakaotalk.serverDto.SelectReqDto;
import com.kakaotalk.serverDto.SelectRespDto;

import lombok.Data;

@Data
class ConnectedSocket extends Thread{
	private static List<ConnectedSocket> socketList = new ArrayList<>();
	private static List<String> selectedChatting = new ArrayList<>();
	private static List<Room> roomList = new ArrayList<>();
	private static List<String> roomNames = new ArrayList<>();

	private Socket socket;
	
	
	private InputStream inputStream;
	private OutputStream outputStream;

	private Gson gson;
	
	private String username;
	private String roomName;
	private String chattingRoomName;
	private String roomOwner;
	private List<String> userList ;
	

	
	public ConnectedSocket(Socket socket) {
		this.socket = socket;
		gson = new Gson(); // gson 생성
		socketList.add(this);
	}
	
	
	
	
	@Override
	public void run() {
		try {
			inputStream = socket.getInputStream();  // 들어올 문을 열어줌
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			
			
			while(true) {
				String request = in.readLine();  // requestDto(JSON) 
				RequestDto requestDto = gson.fromJson(request, RequestDto.class);
				
				
				switch(requestDto.getResource()) {
					case "join" : 
						JoinReqDto joinReqDto = gson.fromJson(requestDto.getBody(), JoinReqDto.class);
						username = joinReqDto.getUsername();
						List<String> connectedUsers = new ArrayList<>();
						for(ConnectedSocket connectedSocket : socketList) {
						connectedUsers.add(connectedSocket.getUsername());
						}
						JoinRespDto joinRespDto = new JoinRespDto(connectedUsers);
						sendToAll(requestDto.getResource(), "ok",gson.toJson(joinRespDto));
						CreateRespDto createRespDto1 = new CreateRespDto(selectedChatting);
		                sendToAll("create", "ok",gson.toJson(createRespDto1));
						break;
						
					case "create":
						CreateReqDto createReqDto = gson.fromJson(requestDto.getBody(), CreateReqDto.class);
						roomName = createReqDto.getRoomName();
						roomOwner = createReqDto.getUserName();
						Room room = new Room(roomName, roomOwner, this);
						roomList = new ArrayList<>();
						roomList.add(room);
						System.out.println(roomName);
						System.out.println(roomOwner);
					    System.out.println(roomList);
					    roomNames.add(roomName);
					    CreateRespDto createRespDto = new CreateRespDto(roomNames);
					    sendToAll("create", "ok", gson.toJson(createRespDto));
					    break;	
					    
					    
					case "selectChatRoom":
					    SelectReqDto selectReqDto = gson.fromJson(requestDto.getBody(), SelectReqDto.class);
					    chattingRoomName = selectReqDto.getSelectUser();
					    selectedChatting.add(chattingRoomName);
					    Room selectedRoom = Room.findRoomByName(roomList, chattingRoomName);
					    if (selectedRoom != null) {
					    	System.out.println(this);
					        selectedRoom.addUser(this);
					        userList = new ArrayList<>();
					        userList.add(username);
					        SelectRespDto selectRespDto = new SelectRespDto();
					        selectRespDto.setSelectedUserList(selectedChatting);
					        sendToAll("selectChatRoom", "ok", gson.toJson(selectRespDto));
					    } else {
					        sendToAll("selectChatRoom", "error", "Failed to find the room.");
					    }
					    break;
						
						
								
					case "sendMessage":
						
						MessageReqDto messageReqDto = gson.fromJson(requestDto.getBody(), MessageReqDto.class);
						
						if(messageReqDto.getToUser().equalsIgnoreCase("welcome")) {
							//System.out.println(messageReqDto);
							String message = "[Server]: " + messageReqDto.getMessageValue();
							MessageRespDto messageRespDto2 = new MessageRespDto(message);
							sendToAll(requestDto.getResource(), "ok", gson.toJson(messageRespDto2));
						}else if(messageReqDto.getToUser().equalsIgnoreCase("exit")) {
							String message = "[Server]: " + messageReqDto.getMessageValue();
							MessageRespDto messageRespDto2 = new MessageRespDto(message);
							sendToAll(requestDto.getResource(), "ok", gson.toJson(messageRespDto2));
						}else {
							String message = messageReqDto.getFromUser() + "> " + messageReqDto.getMessageValue();
							MessageRespDto messageRespDto2 = new MessageRespDto(message);
							sendToAll(requestDto.getResource(), "ok", gson.toJson(messageRespDto2));
						}
						break;
				}
			}
		
			
		}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		//System.out.println(room == null);
	}
	
	
	private void sendToAll(String resource,String status,String body) throws IOException {
		ResponseDto responseDto = new ResponseDto(resource, status, body);
		for(ConnectedSocket connectedSocket : socketList) {
			OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
			PrintWriter out = new PrintWriter(outputStream,true);
			
			out.println(gson.toJson(responseDto));
				
		}
	}
	
	private synchronized void createRoom(Room room) {
        roomList.add(room);   // 방 목록에 추가
        System.out.println(room.getRoomName() + "방 생성");
    }
    
	private synchronized List<String> getRoomList() {
       
        for (Room room : roomList) {
        	roomNames.add(room.getRoomName());
        }
        return roomNames;
    }
    
	private synchronized Room getRoomByName(String roomName) {
        for (Room room : roomList) {
            if (room.getRoomName().equals(roomName)) {
                return room;
            }
        }
        return null;
    }
	
//	private void sendToUser(String resouce, String status, String body, String toUser) throws IOException {
//		
//		ResponseDto responseDto = new ResponseDto(resouce, status, body);
//		for(ConnectedSocket connectedSocket : socketListUsers) {
//			if(connectedSocket.getUsername().equals(toUser)||connectedSocket.getUsername().equals(username)) {
//				OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
//				PrintWriter out = new PrintWriter(outputStream,true);
//				
//				out.println(gson.toJson(responseDto));
//				
//			}
//		}
//	}
	
}


public class KakaoServer {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			 serverSocket = new ServerSocket(9090);
			 System.out.println("====<<< 서버 실행 >>>====");
				while(true) {
					Socket socket = serverSocket.accept(); // 클라이언트의 접속을 기다리는 녀석 // 연결버튼 누를때 까지
					
					ConnectedSocket connectedSocket = new ConnectedSocket(socket); //쓰레드
					connectedSocket.start();
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("====<<< 서버 종료 >>>====");
		}
	}
}
