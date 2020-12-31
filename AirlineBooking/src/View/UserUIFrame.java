package View;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UserUIFrame extends JFrame {// user 프레임(카드레이 아웃)

	//////////////////////////////////////////// 필드///////////////////////////////////////////////////////////////////
	public ArrayList<JButton> componentList = new ArrayList<JButton>();
	public Container c;
	public CardLayout card;

	public BufferedImage backImage, menuImage1, menuImage2;
	public BufferedImage Image1, Image2, Image3, pageImg;

	public String _userId = "";// 이 페이지의 사용자 ID
	
	public int resNum = 0;// 탑승할 인원수

	public JTextArea selectedSeatTextarea = new JTextArea();// 선택된 좌석 출력되는 textArea

	public UserMenuPanel userMenuPanel;
	public MyInfoPanel myInfoPanel;
	public FlightResPanel flightResPanel;
	public SelectSeatPanel selectSeatPanel;

	/////////////////////////////////////////// 생성자//////////////////////////////////////////////////////////////////
	public UserUIFrame(String userId) {// 사용자 ID를 매개변수로 프레임 생성

		_userId = userId;
		setTitle(userId + " 페이지");
		setBounds(250, 50, 1000, 700);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setBackground(Color.black);

		c = this.getContentPane();
		card = new CardLayout();

		this.setLayout(card);

		userMenuPanel = new UserMenuPanel();// 사용자 메뉴 카드
		myInfoPanel = new MyInfoPanel();// 내 프로필 카드
		flightResPanel = new FlightResPanel();// 비행기 조회 카드
		selectSeatPanel = new SelectSeatPanel();// 좌석 선택 카드

		add(userMenuPanel, "userMenu");
		add(myInfoPanel, "myInfo");
		add(flightResPanel, "reservation");
		add(selectSeatPanel, "selectSeat");

		setVisible(true);
	}

	public void userMenuExit() {
		this.dispose();
	}

	class GoBackButton extends JButton {// 뒤로가기 버튼
		GoBackButton() {
			try {
				Image3 = ImageIO.read(new File("뒤로가기.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(Image3, 0, 0, getWidth(), getHeight(), null);
		}
	}

	//////////////////////////////////////// 사용자 메뉴
	//////////////////////////////////////// 카드////////////////////////////////////////////////////////////
	public class UserMenuPanel extends JPanel {
		public GoBackButton backButton = new GoBackButton();
		public MyInfoButton myInfoButton = new MyInfoButton();
		public FlightResButton flightResButton = new FlightResButton();

		UserMenuPanel() {
			try {
				backImage = ImageIO.read(new File("Menu.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLayout(null);

			backButton.setBounds(10, 10, 80, 80);
//			backButton.addActionListener(this);
			componentList.add(backButton);

			myInfoButton.setBounds(280, 300, 180, 180);
			flightResButton.setBounds(530, 300, 180, 180);

//			myInfoButton.addActionListener(this);
			componentList.add(myInfoButton);

//			flightResButton.addActionListener(this);
			componentList.add(flightResButton);

			add(backButton);
			add(myInfoButton);
			add(flightResButton);

		}

		protected void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);
		}

		class MyInfoButton extends JButton {// 내 프로필 메뉴 버튼 클래스
			MyInfoButton() {
				try {
					menuImage1 = ImageIO.read(new File("MyinfoBTN.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(menuImage1, 0, 0, getWidth(), getHeight(), null);
			}
		}

		class FlightResButton extends JButton {// 항공편 예약 버튼 클래스
			FlightResButton() {
				try {
					menuImage2 = ImageIO.read(new File("reservateBTN.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(menuImage2, 0, 0, getWidth(), getHeight(), null);
			}
		}

//		@Override
//		public void actionPerformed(ActionEvent e) {
//			if (e.getSource() == myInfoButton) {
//				card.show(c, "myInfo");
//			} else if (e.getSource() == flightResButton) {
//				card.show(c, "reservation");
//			} else if (e.getSource() == backButton) {
//				new LoginUIFrame();
//				userMenuExit();
//			}
//		}
	}

	//////////////////////////////////////// 내 프로필
	//////////////////////////////////////// 카드/////////////////////////////////////////////////////////////////
	public class MyInfoPanel extends JPanel {

		public GoBackButton backButton = new GoBackButton();// 뒤로가기 버튼
		public MyInfoUpdatePanel myInfoUpdatePanel = new MyInfoUpdatePanel();
		public MyReservationUpdatePanel myReservationUpdatePanel = new MyReservationUpdatePanel();
		MyInfoPanel() {// 내 프로필 카드 생성자
			try {
				pageImg = ImageIO.read(new File("pageImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLayout(null);
			setBounds(200, 150, 1000, 700);

			JLabel lb = new JLabel("마이 페이지");
			lb.setHorizontalAlignment(JLabel.CENTER);
			lb.setFont(new Font("맑은고딕", Font.BOLD, 40));
			lb.setBounds(350, 0, 300, 100);
			add(lb);

			backButton.setBounds(10, 10, 80, 80);// 뒤로가기 버튼
//			backButton.addActionListener(this);
			componentList.add(backButton);

			add(backButton);

			JTabbedPane mainJtabUI = new JTabbedPane(JTabbedPane.TOP);// 탭팬
			mainJtabUI.setBounds(50, 100, 900, 550);
			mainJtabUI.addTab("내 정보 수정하기", myInfoUpdatePanel);
			mainJtabUI.addTab("내 항공편 예약 현황", myReservationUpdatePanel);
			add(mainJtabUI);

			setVisible(true);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(pageImg, 0, 0, getWidth(), getHeight(), null);
		}

		////////////////////////////////////////////////////////// 내 정보 수정하기
		////////////////////////////////////////////////////////// 탭팬///////////////////////////////////////////////////////////
		public class MyInfoUpdatePanel extends JPanel {

			public JPanel mainPanel = new JPanel();// 메인 패널

			public JPanel p[] = new JPanel[2];// 패널1: 내 정보 저장하기 + 패널2: 내 정보 수정/탈퇴

			// 패널 1 필드
			public JPanel savePanel[] = new JPanel[3];// 패널 1에 부착된 패널 3개(north, center, south)
			public JButton saveButton = new JButton("내 컴퓨터에 문서 저장하기");// 저장하기 버튼
			public JTextArea textArea = new JTextArea(26, 43);// 패널1 textArea(미리보기)
			public JFileChooser chooser = new JFileChooser();
			public String pathName;

			// 패널 2 필드
			public JLabel infoLabel[] = new JLabel[6];// 밑의 배열 순서로 레이블 설정되어있음
			public String infoStr[] = { "이름", "아이디", "비밀번호", "이메일", "생년월일", "전화번호" };

			public JTextField textField[] = new JTextField[6];// 레이블에 대한 정보 받는 배열

			public JButton updateButton = new JButton("변경하기");// 변경하기 버튼
			public JButton cancelButton = new JButton("탈퇴하기");// 탈퇴하기 버튼

			MyInfoUpdatePanel() {

				for (int i = 0; i < p.length; i++) {
					p[i] = new JPanel();
					p[i].setBackground(new Color(209, 233, 255));
				}

				///////////////////////////////// 내 정보 문서로 저장하는 패널 p[0]
				p[0].setLayout(new BorderLayout());
				for (int i = 0; i < savePanel.length; i++) {
					savePanel[i] = new JPanel();
					savePanel[i].setBackground(new Color(209, 233, 255));
				}

				savePanel[0].setLayout(new GridLayout(2, 1));

				JLabel lb1 = new JLabel("아이디/ 비밀번호 txt로 저장");
				lb1.setHorizontalAlignment(JLabel.CENTER);
				lb1.setFont(new Font("맑은고딕", Font.BOLD, 18));
				JLabel lb2 = new JLabel("(미리보기)");
				savePanel[0].add(lb1);
				savePanel[0].add(lb2);

				savePanel[1].add(new JScrollPane(textArea));

				savePanel[2].setLayout(new FlowLayout());
				saveButton.setFocusPainted(false);
				saveButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
				saveButton.setForeground(new Color(255, 255, 255));
				saveButton.setBackground(new Color(128, 128, 128));
//				saveButton.addActionListener(this);
				componentList.add(saveButton);
				savePanel[2].add(saveButton);

				p[0].add(savePanel[0], BorderLayout.NORTH);
				p[0].add(savePanel[1], BorderLayout.CENTER);
				p[0].add(savePanel[2], BorderLayout.SOUTH);

				///////////////////////////////////// 내 정보 수정하는 패널 p[1]
				p[1].setLayout(null);

				JLabel lb3 = new JLabel("내 정보");
				lb3.setBounds(200, 0, 100, 50);
				lb3.setHorizontalAlignment(JLabel.CENTER);
				lb3.setFont(new Font("맑은고딕", Font.BOLD, 18));
				p[1].add(lb3);

				for (int i = 0; i < infoLabel.length; i++) {
					infoLabel[i] = new JLabel(infoStr[i]);
					infoLabel[i].setHorizontalAlignment(JLabel.CENTER);
					infoLabel[i].setFont(new Font("맑은고딕", Font.BOLD, 16));
				}
				for (int i = 0; i < textField.length; i++) {
					textField[i] = new JTextField();
				}
				for (int i = 0; i < 6; i++) {
					infoLabel[i].setLocation(65, 40 + (i * 60));
					infoLabel[i].setSize(80, 70);
					p[1].add(infoLabel[i]);
					textField[i].setLocation(175, 50 + (i * 60));
					textField[i].setSize(200, 55);
					p[1].add(textField[i]);
				}

				textField[1].setText(_userId);
				textField[1].setEditable(false);
				String memo = new String("*항공예약시스템에 저장된 내 정보*\r\n\r\n");
				for (int i = 0; i < 6; i++) {
					memo += (infoStr[i] + ": " + textField[i].getText() + "\r\n");
				}
				textArea.setText(memo);

				updateButton.setFocusPainted(false);
				updateButton.setForeground(new Color(255, 255, 255));
				updateButton.setBackground(new Color(128, 128, 128));
				updateButton.setFont(new Font("맑은고딕", Font.BOLD, 17));
				updateButton.setLocation(50, 430);
				updateButton.setSize(340, 40);
//				updateButton.addActionListener(this);
				componentList.add(updateButton);
				p[1].add(updateButton);

				cancelButton.setFocusPainted(false);
				cancelButton.setForeground(Color.yellow);
				cancelButton.setBackground(new Color(128, 128, 128));
				cancelButton.setFont(new Font("맑은고딕", Font.BOLD, 12));
				cancelButton.setLocation(280, 475);
				cancelButton.setSize(110, 30);
//				cancelButton.addActionListener(this);
				componentList.add(cancelButton);
				p[1].add(cancelButton);

				mainPanel.setLayout(new GridLayout(1, 2));
				mainPanel.add(p[0]);
				mainPanel.add(p[1]);
				add(mainPanel);

			}

//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//				if (e.getSource() == saveButton) {// 내정보 문서로 저장하기 버튼
//					int ret = chooser.showSaveDialog(null);
//					if (ret != JFileChooser.APPROVE_OPTION) {
//						JOptionPane.showMessageDialog(null, "경로를 선택하지 않았습니다");
//						return;
//					}
//					pathName = chooser.getSelectedFile().getPath();
//					File wfile = new File(pathName);
//					try {
//						BufferedWriter writer = new BufferedWriter(new FileWriter(wfile));
//						String s;
//						s = textArea.getText();
//						writer.write(s + "\r\n");
//						writer.close();
//					} catch (Exception e1) {
//						e1.printStackTrace();
//					}
//					System.out.println("SAVE Done...");
//				}
//
//				else if (e.getSource() == updateButton) {// 내정보 변경하기 버튼
//					String memo = new String("*항공예약시스템에 저장된 내 정보*\r\n\r\n");
//					for (int i = 0; i < 6; i++) {
//						memo += (infoStr[i] + ": " + textField[i].getText() + "\r\n");
//					}
//					textArea.setText(memo);
//				} else if (e.getSource() == cancelButton) {// 프로그램 탈퇴하기 버튼
//					String memo = new String("*항공예약시스템에 저장된 내 정보*\r\n\r\n");
//					for (int i = 0; i < 6; i++) {
//						memo += (infoStr[i] + ": " + textField[i].getText() + "\r\n");
//					}
//					textArea.setText(memo);
//				}
//			}
		}

		////////////////////////////////////////////////////////// 내 항공편 예약 현황
		////////////////////////////////////////////////////////// 탭팬///////////////////////////////////////////////////////////
		public class MyReservationUpdatePanel extends JPanel {// 내 항공편 예약 현황

			public JLabel pageName = new JLabel("내 항공편 예약 현황");
			public JTextArea textArea = new JTextArea();// 내 항공편 예약 현황을 띄우는 textarea
            
			public ChangeSeatBtn changeSeatBtn = new ChangeSeatBtn();// 자리바꾸기 버튼
			public CancleResBtn cancleResBtn = new CancleResBtn();// 예약 취소하기 버튼

			MyReservationUpdatePanel() {// 패널 생성자
				setLayout(null);
				setBackground(new Color(209, 233, 255));

				pageName.setBounds(10, 10, 200, 30);// 페이지 이름 라벨
				pageName.setFont(new Font("맑은고딕", Font.BOLD, 16));
				add(pageName);

				textArea.setEditable(false);
				JScrollPane scroll = new JScrollPane(textArea);// 자기 항공편 예약 현황 나타남
				scroll.setBounds(30, 50, 830, 280);
				add(scroll);

				changeSeatBtn.setBounds(30, 350, 380, 150);
//				changeSeatBtn.addActionListener(this);
				componentList.add(changeSeatBtn);

				add(changeSeatBtn);

				cancleResBtn.setBounds(480, 350, 380, 150);
//				cancleResBtn.addActionListener(this);
				componentList.add(cancleResBtn);

				add(cancleResBtn);
			}

			class ChangeSeatBtn extends JButton {// 자리바꾸기 버튼 클래스
				ChangeSeatBtn() {
					try {
						Image2 = ImageIO.read(new File("좌석변경.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(Image2, 0, 0, getWidth(), getHeight(), null);
				}
			}

			class CancleResBtn extends JButton {// 예약 취소하기 버튼 클래스
				CancleResBtn() {
					try {
						Image1 = ImageIO.read(new File("예약취소.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(Image1, 0, 0, getWidth(), getHeight(), null);
				}
			}

//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (e.getSource() == changeSeatBtn) {// 자리변경하기 버튼
//					String resnum = JOptionPane.showInputDialog("자리변경 할 항공편의 예약번호를 입력하세요");
//					if (resnum != null) {
//						////////// dB에서 해당 항공기 예약정보를 삭제하고
//						////////// 항공편 자리선택 화면으로 가서 다시 예약
//						card.show(c, "selectSeat");
//					}
//				} else if (e.getSource() == cancleResBtn) {// 예약취소하기 버튼
//					String resnum = JOptionPane.showInputDialog("예약취소 할 항공편의 예약번호를 입력하세요");
//					if (resnum != null) {
//						////////// dB에서 해당 항공기 예약정보를 삭제
//						JOptionPane.showMessageDialog(null, "예약취소 되었습니다.");
//					}
//				}
//			}
		}

//		@Override
//		public void actionPerformed(ActionEvent arg0) {
//			// TODO Auto-generated method stub
//			if (arg0.getSource() == backButton) {// 뒤로가기버튼
//				card.show(c, "userMenu");
//			}
//		}
	}

	//////////////////////////////////////// 비행기 조회
	//////////////////////////////////////// 카드///////////////////////////////////////////////////////////////
	public class FlightResPanel extends JPanel {

		public GoBackButton backButton = new GoBackButton();// 뒤로가기 버튼

		public JLabel titleLb = new JLabel("항공권 검색");

		public JLabel lb[] = new JLabel[3];// 비행기 선택 조건 레이블
		public String lbStr[] = { "가는 날", "오는 날", "가는 인원" };
		public JTextField flightsearchTextField[] = new JTextField[3];// 내가 선택한 조건 받아오는 textField
		public JButton flightSearchButton = new JButton("검색하기");// 해당 조건 비행기 검색 버튼

		public JComboBox departureAirportCombo = new JComboBox();// 출발공항
		public JComboBox destAirportCombo = new JComboBox();// 도착공항
		public String comboStr[] = { "인천", "김포", "제주", "대구", "김해" };

		public JRadioButton radio[] = new JRadioButton[2];// 편도 왕복 라디오버튼
		public String radioStr[] = { "편도", "왕복" };

		public JLabel airport1Label = new JLabel("출발 공항");
		public JLabel airport2Label = new JLabel("도착 공항");

		public JLabel chooseFightLabel1 = new JLabel("가는 항공권 ID: ");
		public JTextField selectedFlightIDTextField1 = new JTextField();

		public JLabel chooseFightLabel2 = new JLabel("오는 항공권 ID: ");
		public JTextField selectedFlightIDTextField2 = new JTextField();

		public JButton searchAirPortParkingLot = new JButton ("공항 주차장 혼잡도 검색하기");
		public JButton selectSeatButton1 = new JButton("좌석선택하기");
		public JButton selectSeatButton2 = new JButton("좌석선택하기");

		public JTextArea departureAirportTextArea = new JTextArea();
		public JTextArea destAirportTextArea = new JTextArea();

		FlightResPanel() {
			try {
				pageImg = ImageIO.read(new File("pageImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLayout(null);
			setBounds(200, 150, 1000, 700);

			JLabel lb = new JLabel("항공기 예약");
			lb.setHorizontalAlignment(JLabel.CENTER);
			lb.setFont(new Font("맑은고딕", Font.BOLD, 40));
			lb.setBounds(350, 0, 300, 100);

			backButton.setBounds(10, 10, 80, 80);
//			backButton.addActionListener(this);
			componentList.add(backButton);

			JTabbedPane mainJtabUI = new JTabbedPane(JTabbedPane.TOP);
			mainJtabUI.setBounds(50, 100, 900, 550);
			mainJtabUI.addTab("항공기 예약", new RegisterFlightPanel());
			
			//주차장 혼잡도 검색하기 버튼
			searchAirPortParkingLot.setHorizontalAlignment(JLabel.CENTER);
			searchAirPortParkingLot.setFont(new Font("맑은고딕", Font.BOLD, 16));
			searchAirPortParkingLot.setForeground(Color.WHITE);
			searchAirPortParkingLot.setBackground(new Color(161, 82, 23));
			searchAirPortParkingLot.setFocusPainted(false);
			searchAirPortParkingLot.setBounds(650, 80, 250, 35);
			
			add(searchAirPortParkingLot);
			add(lb);
			add(backButton);
			add(mainJtabUI);

			setVisible(true);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(pageImg, 0, 0, getWidth(), getHeight(), null);
		}

		public class RegisterFlightPanel extends JPanel implements ItemListener {
			public ButtonGroup group = new ButtonGroup();

			RegisterFlightPanel() {
				this.setBackground(new Color(209, 233, 255));
				this.setLayout(null);
				titleLb.setBounds(14, 14, 220, 20);
				titleLb.setFont(new Font("맑은고딕", Font.BOLD, 20));
				add(titleLb);

				////////////////////////////////// 검색조건부분////////////////////////////////////////

				// 출발공항 레이블
				airport1Label.setHorizontalAlignment(JLabel.CENTER);
				airport1Label.setFont(new Font("맑은고딕", Font.BOLD, 16));
				airport1Label.setBounds(30, 50, 100, 40);
				add(airport1Label);

				// 출발공항 콤보박스
				for (int i = 0; i < comboStr.length; i++) {
					departureAirportCombo.addItem(comboStr[i]);
				}
				departureAirportCombo.setBounds(160, 50, 200, 35);
				departureAirportCombo.setFont(new Font("맑은고딕", Font.BOLD, 16));
//				departureAirportCombo.addActionListener(this);

				add(departureAirportCombo);

				// 도착공항 레이블
				airport2Label.setHorizontalAlignment(JLabel.CENTER);
				airport2Label.setFont(new Font("맑은고딕", Font.BOLD, 16));
				airport2Label.setBounds(30, 95, 100, 35);
				add(airport2Label);

				// 도착공항 콤보박스
				for (int i = 0; i < comboStr.length; i++) {
					destAirportCombo.addItem(comboStr[i]);
				}
				destAirportCombo.setBounds(160, 95, 200, 35);
				destAirportCombo.setFont(new Font("맑은고딕", Font.BOLD, 16));
//				destAirportCombo.addActionListener(this);
				add(destAirportCombo);

				// 편도,왕복 라디오버튼
				for (int i = 0; i < radio.length; i++) {
					radio[i] = new JRadioButton(radioStr[i]);
					radio[i].setFont(new Font("맑은고딕", Font.BOLD, 16));
					radio[i].setBounds(120 + (100 * i), 135, 100, 45);
					radio[i].setBackground(new Color(209, 233, 255));
					radio[i].addItemListener(this);
					group.add(radio[i]);
					add(radio[i]);
				}

				// 가는날, 오는날, 인원수 라벨
				for (int i = 0; i < lb.length; i++) {
					lb[i] = new JLabel(lbStr[i]);
					lb[i].setHorizontalAlignment(JLabel.CENTER);
					lb[i].setFont(new Font("맑은고딕", Font.BOLD, 16));
					lb[i].setLocation(450, 55 + (i * 45));
					lb[i].setSize(80, 20);
					add(lb[i]);
				}

				// 가는날, 오는날, 인원수 텍스트필드
				for (int i = 0; i < flightsearchTextField.length; i++) {
					flightsearchTextField[i] = new JTextField();
					flightsearchTextField[i].setLocation(625, 50 + (i * 45));
					flightsearchTextField[i].setSize(200, 35);
					add(flightsearchTextField[i]);
				}

				// 비행기 조회 버튼
				flightSearchButton.setHorizontalAlignment(JLabel.CENTER);
				flightSearchButton.setFont(new Font("맑은고딕", Font.BOLD, 16));
				flightSearchButton.setForeground(new Color(255, 255, 255));
				flightSearchButton.setBackground(new Color(128, 128, 128));
				flightSearchButton.setFocusPainted(false);
				flightSearchButton.setBounds(675, 187, 150, 30);
//				flightSearchButton.addActionListener(this);
				componentList.add(flightSearchButton);

				add(flightSearchButton);

				////////////////////////////////// textArea부분////////////////////////////////////////

				// 선택항 조건에 대한 항공기 목록 출력하는 textArea

				departureAirportTextArea.setEditable(false);
				JScrollPane scroll = new JScrollPane(departureAirportTextArea);
				add(scroll);
				scroll.setBounds(20, 230, 400, 200);

				destAirportTextArea.setEditable(false);
				scroll = new JScrollPane(destAirportTextArea);
				add(scroll);
				scroll.setBounds(470, 230, 400, 200);

				////////////////////////////////// 예약할 항공기 선택하는
				////////////////////////////////// 부분////////////////////////////////////////

				// 가는 비행기 라벨부착
				chooseFightLabel1.setBounds(50, 445, 200, 20);
				chooseFightLabel1.setFont(new Font("맑은고딕", Font.BOLD, 16));
				add(chooseFightLabel1);

				// 선택한 항공기 ID값 받아오는 텍스트필드
				selectedFlightIDTextField1.setBounds(200, 440, 200, 35);
				selectedFlightIDTextField1.setFont(new Font("맑은고딕", Font.BOLD, 16));
				add(selectedFlightIDTextField1);

				// 오는 비행기 라벨부착
				chooseFightLabel2.setBounds(500, 445, 200, 20);
				chooseFightLabel2.setFont(new Font("맑은고딕", Font.BOLD, 16));
				add(chooseFightLabel2);

				// 선택한 항공기 ID값 받아오는 텍스트필드
				selectedFlightIDTextField2.setBounds(650, 440, 200, 35);
				selectedFlightIDTextField2.setFont(new Font("맑은고딕", Font.BOLD, 16));
				add(selectedFlightIDTextField2);

				// 좌석선택하기 버튼
				selectSeatButton1.setHorizontalAlignment(JLabel.CENTER);
				selectSeatButton1.setFont(new Font("맑은고딕", Font.BOLD, 16));
				selectSeatButton1.setForeground(new Color(255, 255, 255));
				selectSeatButton1.setBackground(new Color(128, 128, 128));
				selectSeatButton1.setFocusPainted(false);
				selectSeatButton1.setBounds(200, 480, 200, 35);
//				selectSeatButton1.addActionListener(this);
				componentList.add(selectSeatButton1);

				add(selectSeatButton1);
				// 좌석선택하기 버튼
				selectSeatButton2.setHorizontalAlignment(JLabel.CENTER);
				selectSeatButton2.setFont(new Font("맑은고딕", Font.BOLD, 16));
				selectSeatButton2.setForeground(new Color(255, 255, 255));
				selectSeatButton2.setBackground(new Color(128, 128, 128));
				selectSeatButton2.setFocusPainted(false);
				selectSeatButton2.setBounds(650, 480, 200, 35);
//				selectSeatButton2.addActionListener(this);
				componentList.add(selectSeatButton2);

				add(selectSeatButton2);

			}

//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//
//				if (e.getSource() == flightSearchButton) {// 비행기 검색하기 버튼
//
//				}
//
//				if (e.getSource() == selectSeatButton1) {// 가는자리선택하기 버튼
//					if (flightsearchTextField[2].getText().equals("")) {// 인원수 textfield 비어있으면
//						JOptionPane.showMessageDialog(null, "탑승할 인원을 선택하세요!");
//						return;
//					}
//					resNum = Integer.valueOf(flightsearchTextField[2].getText());
//					card.show(c, "selectSeat");
//				}
//
//				if (e.getSource() == selectSeatButton2) {// 오는자리선택하기 버튼
//					if (flightsearchTextField[2].getText().equals("")) {// 인원수 textfield 비어있으면
//						JOptionPane.showMessageDialog(null, "탑승할 인원을 선택하세요!");
//						return;
//					}
//					resNum = Integer.valueOf(flightsearchTextField[2].getText());
//					card.show(c, "selectSeat");
//				}
//
//				if (e.getSource() == departureAirportCombo) {// 출발 공항 콤보박스
//					int index = departureAirportCombo.getSelectedIndex();
//				}
//				if (e.getSource() == destAirportCombo) {// 도착 공항 콤보박스
//					int index = destAirportCombo.getSelectedIndex();
//				}
//			}

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub

				if (radio[0].isSelected()) {// 편도가 선택된 경우
					flightsearchTextField[1].setText("");// 오는 날 textfield 비우기
					flightsearchTextField[1].setEditable(false);
					selectedFlightIDTextField2.setEditable(false);
				}

				if (radio[1].isSelected()) {// 왕복이 선택된 경우
					flightsearchTextField[1].setEditable(true);
					selectedFlightIDTextField2.setEditable(true);
				}
			}
		}

//		@Override
//		public void actionPerformed(ActionEvent arg0) {
//			// TODO Auto-generated method stub
//			if (arg0.getSource() == backButton) {// 뒤로가기 버튼
//				card.show(c, "userMenu");
//			}
//		}
	}

	//////////////////////////////////////// 좌석 선택
	//////////////////////////////////////// 카드//////////////////////////////////////////////////////////////////
	public class SelectSeatPanel extends JPanel {

		public GoBackButton backButton = new GoBackButton();// 뒤로 가기버튼
		public JButton reserveButton = new JButton("예약하기");

		public int cnt = 0;// 선택된 좌석 수 변수
		public LinkedList<Integer> seatlist = new LinkedList<Integer>();
		public JButton[] seatButton = new JButton[40];

		public SelectSeatTab selectSeatTab = new SelectSeatTab();

		SelectSeatPanel() {
			try {
				pageImg = ImageIO.read(new File("pageImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLayout(null);
			setBounds(200, 150, 1000, 700);

			JLabel lb = new JLabel("항공기 예약");
			lb.setHorizontalAlignment(JLabel.CENTER);
			lb.setFont(new Font("맑은고딕", Font.BOLD, 40));
			lb.setBounds(350, 0, 300, 100);

			backButton.setBounds(10, 10, 80, 80);
//			backButton.addActionListener(this);
			componentList.add(backButton);

			JTabbedPane mainJtabUI = new JTabbedPane(JTabbedPane.TOP);
			mainJtabUI.setBounds(50, 100, 900, 550);
			mainJtabUI.addTab("좌석 선택", selectSeatTab);

			add(lb);
			add(backButton);
			add(mainJtabUI);

			setVisible(true);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(pageImg, 0, 0, getWidth(), getHeight(), null);
		}

		public class SelectSeatTab extends JPanel {
			public Container c = getContentPane();

			public JButton resetButton = new JButton("좌석 선택 초기화");// 좌석 선택 초기화 버튼

			public JLabel rowLabel[] = new JLabel[4];
			public JLabel columnLabel[] = new JLabel[10];

			public String rowStr[] = { "A", "B", "C", "D" };
			public String columnStr[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

			SelectSeatTab() {
				this.setBackground(new Color(209, 233, 255));
				setBounds(100, 100, 900, 600);
				setLayout(null);

				// 좌석라벨 부착
				for (int i = 0; i < rowLabel.length; i++) {
					rowLabel[i] = new JLabel(rowStr[i]);
					rowLabel[i].setFont(new Font("맑은고딕", Font.BOLD, 25));
					if (i == 2 || i == 3) {
						rowLabel[i].setBounds(55, 110 + (i * 75), 40, 40);
					} else {
						rowLabel[i].setBounds(55, 65 + (i * 75), 40, 40);
					}
					add(rowLabel[i]);
				}
				for (int i = 0; i < columnLabel.length; i++) {
					columnLabel[i] = new JLabel(columnStr[i]);
					columnLabel[i].setFont(new Font("맑은고딕", Font.BOLD, 25));
					if (i == 9) {
						columnLabel[i].setBounds(120 + (i * 74), 15, 40, 40);
					} else {
						columnLabel[i].setBounds(120 + (i * 75), 15, 40, 40);
					}
					add(columnLabel[i]);
				}

				// 좌석 선택 초기화 버튼
				resetButton.setFocusPainted(false);
				resetButton.setBackground(new Color(128, 128, 128));
				resetButton.setForeground(Color.white);
				resetButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
				resetButton.setBounds(685, 410, 150, 30);
//				resetButton.addActionListener(this);
				componentList.add(resetButton);

				add(resetButton);

				int seatcnt = 0;
				// 좌석부착
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 10; j++) {
						String str = "";
						if (i == 0) {
							str += "A";
						} else if (i == 1) {
							str += "B";
						} else if (i == 2) {
							str += "C";
						} else if (i == 3) {
							str += "D";
						}
						seatButton[seatcnt] = new JButton(str + (j + 1));
						seatButton[seatcnt].setFont(new Font("맑은고딕", Font.BOLD, 13));
						seatButton[seatcnt].setForeground(Color.white);
						seatButton[seatcnt].setBounds(100 + (j * 75), 60 + (i * 75), 60, 60);
						if (i > 1) {
							seatButton[seatcnt].setBounds(100 + (j * 75), 60 + (i * 75) + 35, 60, 60);
						}
						seatButton[seatcnt].setFocusPainted(false);
//						seatButton[seatcnt].addActionListener(this);
						componentList.add(seatButton[seatcnt]);

						add(seatButton[seatcnt]);
						if (j == 0) {// 비즈니스석 색깔
							seatButton[seatcnt].setBackground(new Color(112, 48, 160));
						} else {// 이코노미석 색깔
							seatButton[seatcnt].setBackground(new Color(46, 117, 182));
						}
						seatcnt++;
					}
				}
				// 라벨 부착
				JLabel selecLb = new JLabel("▼ 선택한 좌석  ▼");
				selecLb.setBounds(90, 420, 200, 40);
				selecLb.setFont(new Font("맑은고딕", Font.BOLD, 15));
				add(selecLb);

				// 자기 항공편 예약 현황 나타나는 textArea부착
				selectedSeatTextarea.setEditable(false);
				JScrollPane scroll = new JScrollPane(selectedSeatTextarea);
				selectedSeatTextarea.setFont(new Font("맑은고딕", Font.BOLD, 15));
				scroll.setBounds(80, 460, 500, 40);
				add(scroll);

				// 예약하기 버튼
				reserveButton.setHorizontalAlignment(JLabel.CENTER);
				reserveButton.setFont(new Font("맑은고딕", Font.BOLD, 20));
				reserveButton.setForeground(new Color(255, 255, 255));
				reserveButton.setBackground(new Color(128, 128, 128));
				reserveButton.setFocusPainted(false);
				reserveButton.setBounds(650, 460, 150, 40);
//				reserveButton.addActionListener(this);
				componentList.add(reserveButton);

				add(reserveButton);

				setVisible(true);

			}

//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//				if (e.getSource() == reserveButton) {// 예약하기버튼
//					JOptionPane.showMessageDialog(null, "예약되셨습니다!");
//					card.show(c, "reservation");
//					cnt = 0;
//					seatlist.clear();
//					selectedSeatTextarea.setText("");
//					for (int i = 0; i < 40; i++) {// 일단 좌석 색깔 다 초기화 시킴, 예약된 좌석은 라벨 x로 바꾸고, 선택 안되게 설정해주어야함.
//						if (i % 10 == 0) {
//							seatButton[i].setBackground(new Color(112, 48, 160));
//						} else {
//							seatButton[i].setBackground(new Color(46, 117, 182));
//						}
//					}
//					return;// 밑에 for문이니깐 예약하기 버튼이면 걍 return 시켜주기
//				}
//
//				for (int i = 0; i < seatButton.length; i++) {
//
//					if (e.getSource() == seatButton[i]) {
//						for (int j = 0; j < seatlist.size(); j++) {
//							if (i == seatlist.get(j)) {
//								return;
//							}
//						}
//						seatlist.add(i);
//						cnt++;
//						if (cnt > resNum) {
//							return;
//						}
//						seatButton[i].setBackground(new Color(255, 192, 0));
//						seatButton[i].setFocusPainted(false);
//						String str = "";
//						if (i < 10) {
//							str += "A";
//						} else if (i < 20) {
//							str += "B";
//						} else if (i < 30) {
//							str += "C";
//						} else if (i < 40) {
//							str += "D";
//						}
//						selectedSeatTextarea.setText(selectedSeatTextarea.getText() + "  " + str + (i % 10 + 1));
//					}
//
//					else if (e.getSource() == resetButton) {// 좌석초기화 버튼
//						if (i % 10 == 0) {
//							seatButton[i].setBackground(new Color(112, 48, 160));
//						} else {
//							seatButton[i].setBackground(new Color(46, 117, 182));
//						}
//						cnt = 0;
//						seatlist.clear();
//						selectedSeatTextarea.setText("");
//					}
//				}
//			}
		}

//		@Override
//		public void actionPerformed(ActionEvent e) {
//			// TODO Auto-generated method stub
//			if (e.getSource() == backButton) {// 뒤로가기 버튼
//				card.show(c, "reservation");
//
//				cnt = 0;
//				seatlist.clear();
//				selectedSeatTextarea.setText("");
//
//				for (int i = 0; i < 40; i++) {
//					if (i % 10 == 0) {
//						seatButton[i].setBackground(new Color(112, 48, 160));
//					} else {
//						seatButton[i].setBackground(new Color(46, 117, 182));
//					}
//				}
//			}
//		}
	}

	public void addButtonActionListener(ActionListener listener) {
		for (JButton comp : componentList) {
			comp.addActionListener(listener);
		}
		// RegisterFlightPanel
		flightResPanel.departureAirportCombo.addActionListener(listener);
		flightResPanel.destAirportCombo.addActionListener(listener);

	}

}