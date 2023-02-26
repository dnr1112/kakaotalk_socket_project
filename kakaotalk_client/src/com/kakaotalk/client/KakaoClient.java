package com.kakaotalk.client;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import com.kakaotalk.clientDto.SelectReqDto;

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
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel createPanel;
    private JPanel chatPanel;
    private CardLayout cardLayout;
    private JLabel  titleLabel;
    
    private JTextField userNameField;
    private JTextField messageInput;
    
    private JTextArea contentView;
    
    private JList<String> jUserList ;
    private DefaultListModel<String> userListModel;
    private JList<String> jChattingList;
	private DefaultListModel<String> chattingListModel;
	private List<String> userList = new ArrayList<>();
    
    
    
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
    	
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 480, 800);
        setResizable(false);
        setTitle("KakaoTalk");
        
        
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
        
        setContentPane(mainPanel);
        mainPanel.setLayout(new CardLayout(0, 0));
        cardLayout = (CardLayout) mainPanel.getLayout();
        
        JPanel loginPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(chattingImg, 0, 0, getWidth(), getHeight(), null);
            }
        };
        
        // 로그인 화면
    	loginPanel = new JPanel();
        loginPanel.setOpaque(false);
        mainPanel.add(loginPanel, "loginPanel");
        loginPanel.setLayout(null);
        
        
        // 방만들기 화면
        JPanel createPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(chattingImg, 0, 0, getWidth(), getHeight(), null);
            }
        };
        
        //createPanel = new JPanel();
        createPanel.setOpaque(false);
        mainPanel.add(createPanel, "createPanel");
        createPanel.setLayout(null);
          
        // 채팅 화면
        JPanel chatPanel = new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
               // g.drawImage(chattingImg, 0, 0, getWidth(), getHeight(), null);
            }
        };
        
        //chatPanel = new JPanel();
        chatPanel.setOpaque(false);
        mainPanel.add(createPanel, "createPanel");
        chatPanel.setLayout(null);
     
   
        
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
        loginPanel.add(loginButton);
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	
            	String ip = "127.0.0.1";
            	int port = 9090;
            	
            	 username = userNameField.getText().trim(); // trim()을 사용하여 입력값 앞뒤 공백 제거
            	    
            	 if(username == null || username.equals("")) {
            		    JOptionPane.showMessageDialog(null, "사용자 이름을 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            		    return;
            		}

            		// 입력값이 공백으로만 이루어진 경우 에러메시지 출력 후 리턴
            		if(username.matches("\\s+")) {
            		    JOptionPane.showMessageDialog(null, "사용자 이름은 공백으로만 입력할 수 없습니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            		    return;
            		}
            	
            	try {
					socket = new Socket(ip,port);
					JOptionPane.showMessageDialog(null, 
							username + "님 접속을 환영합니다." , 
							"접속성공",
							JOptionPane.INFORMATION_MESSAGE); 
					
					ClientRecive clientRecive = new ClientRecive(socket);
					clientRecive.start();
	           
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
        
        JScrollPane chattingListScroll = new JScrollPane();
        chattingListScroll.setBounds(101, 112, 308, 602);
        createPanel.add(chattingListScroll);
        
        JButton chPlusButton = new JButton();
        chPlusButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    createroom = JOptionPane.showInputDialog(null,
                            "방의 제목을 입력하시오.", "방 생성",
                            JOptionPane.INFORMATION_MESSAGE);
                    if (createroom == null || createroom.trim().isEmpty()) {
                        throw new IllegalArgumentException("방 제목을 입력하세요.");
                    } else if (createroom.matches("^\\s+$")) {
                        throw new IllegalArgumentException("방 제목은 공백으로만 이루어질 수 없습니다.");
                    }

                    titleLabel.setText("방 제목: " + createroom);
                    CreateReqDto createReqDto = new CreateReqDto(createroom, username);
                   // System.out.println(createReqDto);
                    String createReqDtoJson = gson.toJson(createReqDto);
                    RequestDto requestDto = new RequestDto("create", createReqDtoJson);
                    String requestDtoJson = gson.toJson(requestDto);
                    OutputStream outputStream = socket.getOutputStream();
                    PrintWriter out = new PrintWriter(outputStream, true);
                    out.println(requestDtoJson);
                    //System.out.println(requestDtoJson);
                } catch (IllegalArgumentException e1) {
                    JOptionPane.showMessageDialog(null,
                            e1.getMessage(), "입력 오류",
                            JOptionPane.ERROR_MESSAGE);
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        
        
        chattingListModel = new DefaultListModel<>();
        jChattingList = new JList<String>(chattingListModel);
		chattingListScroll.setViewportView(jChattingList); 
		
		
		
		jChattingList.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        String selectedRoom = jChattingList.getSelectedValue();
		        if (selectedRoom != null && !selectedRoom.equals("--- 채팅방 목록 ---")) {
		            int choice = JOptionPane.showConfirmDialog(null, "채팅방에 입장하시겠습니까?", "입장 알림", JOptionPane.YES_NO_OPTION);
		            if (choice == JOptionPane.YES_OPTION) {
		                    SelectReqDto selectReqDto = new SelectReqDto(selectedRoom);
		                    String selectReqDtoJson = gson.toJson(selectReqDto);
		                    RequestDto requestDto = new RequestDto("selectChatRoom", selectReqDtoJson);
		                    String requestDtoJson = gson.toJson(requestDto);
		                    OutputStream outputStream;
		                    try {
		                        outputStream = socket.getOutputStream();
		                        PrintWriter out = new PrintWriter(outputStream, true);
		                        out.println(requestDtoJson);
		                    } catch (IOException e1) {
		                        e1.printStackTrace();
		                    }
		                    cardLayout.show(mainPanel, "chatPanel");
		                    welcomeSendMessage();
		                } else {
		                    // No를 선택한 경우 아무런 작업도 수행하지 않습니다. 
		            }
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
        jUserList = new JList<String>(userListModel);
		userListScroll.setViewportView(jUserList);
        
     
        mainPanel.add(chatPanel, "chatPanel");
        chatPanel.setLayout(null);
        
        JScrollPane textScroll = new JScrollPane();
        textScroll.setPreferredSize(new Dimension(292, 45));
        textScroll.setBounds(69, 56, 292, 45);

        
        titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 18));
        titleLabel.setBounds(93, 10, 249, 50);
        chatPanel.add(titleLabel);
        

        
        JScrollPane contentScroll = new JScrollPane();
        contentScroll.setBounds(0, 70, 464, 621);
        chatPanel.add(contentScroll);
        
        contentView = new JTextArea();
        contentView.setEditable(false);
        contentScroll.setViewportView(contentView);
        
        JScrollPane messageScroll = new JScrollPane();
        messageScroll.setBounds(10, 701, 375, 50);
        chatPanel.add(messageScroll);
        
        messageInput = new JTextField();
        messageInput.setHorizontalAlignment(SwingConstants.LEFT);
        messageInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			
					sendMessage();
				}
				
			}
		});
        messageScroll.setViewportView(messageInput);
        messageInput.setColumns(10);
        
        JButton sendButton = new JButton();
        sendButton.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		sendMessage();
        	}
        });
        sendButton.setIcon(enterButtonImg);
        sendButton.setBounds(397, 701, 50, 50);
        sendButton.setBorderPainted(true);
        sendButton.setContentAreaFilled(false);
        sendButton.setFocusPainted(false);
        chatPanel.add(sendButton);
        
        JButton exitButton = new JButton();
        exitButton.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		exitSendMessage();
        		//clearChat();
        		cardLayout.show(mainPanel, "createPanel");
        	}
        });
        exitButton.setIcon(exitButtonImg);
        exitButton.setBounds(397, 10, 50, 50);
        exitButton.setBorderPainted(true);
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        chatPanel.add(exitButton);
        chatPanel.setLayout(null);
        
        
    }
    
    
    private void sendRequest(String resouce, String body) {
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outputStream,true);
			RequestDto requestDto = new RequestDto(resouce, body);
		
			out.println(gson.toJson(requestDto));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void sendMessage() {
	
		if(!messageInput.getText().isBlank()) { 
			
			String toUser = KakaoClient.getInstance().getUsername();
			MessageReqDto messageReqDto = new MessageReqDto(toUser, username, messageInput.getText());
			sendRequest("sendMessage", gson.toJson(messageReqDto));
			System.out.println(messageReqDto);
			messageInput.setText("");
				
		  }
	}
	

	private void welcomeSendMessage() {
	        //String toUser = userList.getSelectedIndex() == 0 ? "all" : userList.getSelectedValue();
	        String username = KakaoClient.getInstance().getUsername(); // Assuming that the username is stored in the KakaoClient class
	        String welcomeMessage = username + "님 입장을 환영합니다.";
	        MessageReqDto messageReqDto = new MessageReqDto("welcome", username, welcomeMessage);
	        sendRequest("sendMessage", gson.toJson(messageReqDto));
	        //contentView.append(welcomeMessage + "\n");
	}
	
	
	
	
	private void exitSendMessage() {
        String username = KakaoClient.getInstance().getUsername(); // Assuming that the username is stored in the KakaoClient class
        String exitMessage = username + "님이 퇴장하셨습니다.";
        MessageReqDto messageReqDto = new MessageReqDto("exit", username, exitMessage);
        sendRequest("sendMessage", gson.toJson(messageReqDto));
        contentView.setText("");
	}
}