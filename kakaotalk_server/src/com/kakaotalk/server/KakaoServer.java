package com.kakaotalk.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class ConnectedSocket extends Thread {
	private static List<ConnectedSocket> socketList = new ArrayList<>(); 
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public ConnectedSocket(Socket socket) {
		this.socket = socket;
		socketList.add(this);
	}
	
	@Override
	public void run() {
		
	}
}

public class KakaoServer {
	
	public static void main(String[] args) {
		
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(9090);
			System.out.println("========<< 서버 실행 >>========");
			
			while(true) {
				Socket socket = serverSocket.accept();
				ConnectedSocket connectedSocket = new ConnectedSocket(socket);
				connectedSocket.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if(serverSocket != null) {
				try {
					serverSocket.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("========<< 서버 종료 >>========");
		}
	}
}
