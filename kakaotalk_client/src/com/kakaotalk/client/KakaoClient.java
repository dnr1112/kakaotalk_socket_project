package com.kakaotalk.client;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.google.gson.Gson;
import com.kakaotalk.clientDto.CreateReqDto;
import com.kakaotalk.clientDto.JoinReqDto;
import com.kakaotalk.clientDto.MessageReqDto;
import com.kakaotalk.clientDto.RequestDto;

import lombok.Getter;



@Getter
public class KakaoClient extends JFrame {
	
	
	//kakaoClient singleton 생성
	
	private static KakaoClient instance;
	
	public static KakaoClient getInstance() {
		
		if(instance == null) {
				
			
				try {
					instance = new KakaoClient();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return instance;
	}
	
	
	
	private Socket socket;
	private Gson gson;
	private String username;
	private String createroom;
	
    
    private static final long serialVersionUID = 1L;
    private JPanel loginPanel;
    private JTextField userNameField;
    private JPanel chattingPanel;
    private CardLayout cardLayout;
    private JTextField messageInput;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private JList<String> chattingList;
	private DefaultListModel<String> chattingListModel;
    
    
    
    public static void main(String[] args) {
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KakaoClient frame = KakaoClient.getInstance();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
    
    
    private KakaoClient() throws IOException {
    	
    	gson = new Gson();
    	
        setResizable(false);
        setTitle("KakaoTalk");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 480, 800);
        
        // 배경화면 이미지를 불러옵니다.
        final BufferedImage loginImg = ImageIO.read(KakaoClient.class.getResource("/com/kakaotalk/client/images/kakao.png"));
        final ImageIcon loginButtonImg = new ImageIcon(KakaoClient.class.getResource("/com/kakaotalk/client/images/kakao_login_medium_narrow.png"));
        final ImageIcon chattingPlusButtonImg = new ImageIcon(KakaoClient.class.getResource("/com/kakaotalk/client/images/plus2.png"));
        final BufferedImage chattingImg = ImageIO.read(KakaoClient.class.getResource("/com/kakaotalk/client/images/kakaoChatting.png"));
        final ImageIcon enterButtonImg = new ImageIcon(KakaoClient.class.getResource("/com/kakaotalk/client/images/Enter.png"));
        final ImageIcon exitButtonImg = new ImageIcon(KakaoClient.class.getResource("/com/kakaotalk/client/images/Exit.png"));
        
        
        
   
        
        // 커스텀 JPanel 클래스를 생성합니다.
        JPanel mainPanel = new JPanel() {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(loginImg, 0, 0, getWidth(), getHeight(), null);
            }
        };
        
        JPanel createPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(chattingImg, 0, 0, getWidth(), getHeight(), null);
            }
        };
        
     
        
        
        setContentPane(mainPanel);
        mainPanel.setLayout(new CardLayout(0, 0));
        cardLayout = (CardLayout) mainPanel.getLayout();
        
        loginPanel = new JPanel();
        loginPanel.setOpaque(false);
        mainPanel.add(loginPanel, "loginPanel");
        loginPanel.setLayout(null);
        
        userNameField = new JTextField();
        userNameField.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        userNameField.setHorizontalAlignment(SwingConstants.CENTER);
        userNameField.setBounds(101, 429, 256, 47);
        loginPanel.add(userNameField);
        userNameField.setColumns(10);
        
        JButton loginButton = new JButton(loginButtonImg);
        
        loginButton.setBounds(101, 486, 256, 47);
        loginButton.setBorderPainted(true);
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        loginButton.setRolloverIcon(loginButtonImg);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	String ip = "127.0.0.1";
            	int port = 9090;
            	
            	try {
					socket = new Socket(ip,port);
					ClientRecive clientRecive = new ClientRecive(socket);
					clientRecive.start();
					username = userNameField.getText();
	          
					JOptionPane.showMessageDialog(null, 
							username + "님 접속을 환영합니다." , 
							"접속성공",
							JOptionPane.INFORMATION_MESSAGE);  
	                cardLayout.show(mainPanel, "createPanel");
	                JoinReqDto joinReqDto = new JoinReqDto(username);
					String joinReqDtoJson = gson.toJson(joinReqDto);
					RequestDto requestDto = new RequestDto("join", joinReqDtoJson);
					String requestDtoJson = gson.toJson(requestDto);
					OutputStream outputStream = socket.getOutputStream();
					PrintWriter out = new PrintWriter(outputStream,true);
					out.println(requestDtoJson);
				}catch (ConnectException e1) {
					
					JOptionPane.showMessageDialog(null, 
							"서버 접속 실패" , 
							"접속실패",
							JOptionPane.ERROR_MESSAGE);
            	}catch (UnknownHostException e1) {
					
					e1.printStackTrace();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
            	
            	
          
            }
        });
        loginPanel.add(loginButton);
        
        chattingPanel = new JPanel();
        mainPanel.add(createPanel, "createPanel");
        createPanel.setLayout(null);
        
        JScrollPane chattingListScroll = new JScrollPane();
        chattingListScroll.setBounds(101, 112, 308, 602);
        createPanel.add(chattingListScroll);
        
        
        chattingListModel = new DefaultListModel<>();
        chattingList = new JList<String>(chattingListModel);
		chattingListScroll.setViewportView(chattingList); 

        
        JButton chPlusButton = new JButton();
        chPlusButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
       
	try {
		   createroom =JOptionPane.showInputDialog(null,
				   "방의 제목을 입력하시오.","방 생성",
				   JOptionPane.INFORMATION_MESSAGE);
	       CreateReqDto createReqDto = new CreateReqDto(createroom);
		   String createReqDtoJson = gson.toJson(createReqDto);
		   RequestDto requestDto = new RequestDto("create", createReqDtoJson);
		   String requestDtoJson = gson.toJson(requestDto);	
		   OutputStream outputStream = socket.getOutputStream();
		   PrintWriter out = new PrintWriter(outputStream,true);
		   out.println(requestDtoJson);
	} catch (ConnectException e1) {
		
		JOptionPane.showMessageDialog(null, 
				"방 생성 실패" , 
				"생성 실패",
				JOptionPane.ERROR_MESSAGE);
	}catch (UnknownHostException e1) {
		e1.printStackTrace();
	}catch (IOException e1) {
		e1.printStackTrace();
	}
  }
});
        
        
        chPlusButton.setBounds(26, 92, 50, 50);
        chPlusButton.setIcon(chattingPlusButtonImg);
        loginButton.setBorderPainted(true);
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        createPanel.add(chPlusButton);
        
        JScrollPane userListScroll = new JScrollPane();
        userListScroll.setBounds(101, 42, 308, 60);
        createPanel.add(userListScroll);
        
        
        userListModel = new DefaultListModel<>();
		userList = new JList<String>(userListModel);
		userListScroll.setViewportView(userList);
        
        JPanel chatPanel = new JPanel();
        mainPanel.add(chatPanel, "name_2765926546827100");
        chatPanel.setLayout(null);
        
        JScrollPane contentScroll = new JScrollPane();
        contentScroll.setBounds(0, 76, 464, 577);
        chatPanel.add(contentScroll);
        
        JTextArea contentView = new JTextArea();
        contentScroll.setViewportView(contentView);
        
        JScrollPane messageScroll = new JScrollPane();
        messageScroll.setBounds(10, 675, 375, 76);
        chatPanel.add(messageScroll);
        
        messageInput = new JTextField();
        messageScroll.setViewportView(messageInput);
        messageInput.setColumns(10);
        
        JButton sendButton = new JButton();
        sendButton.setIcon(enterButtonImg);
        sendButton.setBounds(397, 689, 50, 50);
        sendButton.setBorderPainted(true);
        sendButton.setContentAreaFilled(false);
        sendButton.setFocusPainted(false);
        chatPanel.add(sendButton);
        
        JButton exitButton = new JButton();
        exitButton.setIcon(exitButtonImg);
        exitButton.setBounds(386, 10, 50, 50);
        exitButton.setBorderPainted(true);
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        chatPanel.add(exitButton);
        chattingPanel.setLayout(null);
        
        
    }
}