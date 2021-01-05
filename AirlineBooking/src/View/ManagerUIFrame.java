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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;

public class ManagerUIFrame extends JFrame {
	public BufferedImage backImage, menuImage1, menuImage2, menuImage3;
	public BufferedImage backButtonImg, pageImg; // 이미지

	// controller에서 필요한 것들
	public ManagerMenuPanel managerMenuPanel = new ManagerMenuPanel(); // 관리자 메뉴 패널
	public MemberManagerPanel managerPanel = new MemberManagerPanel(); // 회원 관리 패널
	public ManagerReservationPanel reservationPanel = new ManagerReservationPanel(); // 예약 관리 패널
	public ManagerFlightPanel flightPanel = new ManagerFlightPanel(); // 항공 관리 패널

	public Container c;
	public CardLayout card;

	public ManagerUIFrame() {
		setTitle("관리자 페이지");
		setBounds(250, 50, 1000, 700);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setBackground(Color.black);

		c = this.getContentPane();
		card = new CardLayout();

		this.setLayout(card);

		add(managerMenuPanel, "managerMenu");
		add(managerPanel, "manager");
		add(reservationPanel, "reservation");
		add(flightPanel, "flight");

		setVisible(true);
	}

	public void managerMenuExit() { //관리자 페이지 프레임 닫음
		this.dispose();
	}

	class GoBackButton extends JButton { //뒤로가기 버튼
		GoBackButton() {
			try {
				backButtonImg = ImageIO.read(new File("뒤로가기.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backButtonImg, 0, 0, getWidth(), getHeight(), null);
		}
	}

	//////////////////////////// 관리자 메뉴 패널////////////////////////////

	class memberManageButton extends JButton {// 회원 관리 버튼
		memberManageButton() {
			try {
				menuImage1 = ImageIO.read(new File("memberButton.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(menuImage1, 0, 0, getWidth(), getHeight(), null);
		}
	}

	class reservationManageButton extends JButton { // 예약 관리 버튼
		reservationManageButton() {
			try {
				menuImage2 = ImageIO.read(new File("reservationButton.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(menuImage2, 0, 0, getWidth(), getHeight(), null);
		}
	}

	class flightManageButton extends JButton { // 항공 관리 버튼
		flightManageButton() {
			try {
				menuImage3 = ImageIO.read(new File("flightButton.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(menuImage3, 0, 0, getWidth(), getHeight(), null);
		}
	}

	public class ManagerMenuPanel extends JPanel { // 관리자 메뉴 패널
		// controller에서 필요한 것들
		public GoBackButton backButton = new GoBackButton(); // 뒤로가기 버튼
		public memberManageButton memButton = new memberManageButton(); // 회원 관리 버튼
		public reservationManageButton resButton = new reservationManageButton(); // 예약 관리 버튼
		public flightManageButton fliButton = new flightManageButton(); // 항공 관리 버튼

		ManagerMenuPanel() {
			try {
				backImage = ImageIO.read(new File("Menu.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			setLayout(null);

			backButton.setBounds(10, 10, 80, 80);

			memButton.setBounds(250, 300, 150, 150);
			resButton.setBounds(418, 300, 150, 150);
			fliButton.setBounds(585, 300, 150, 150);

			add(backButton);
			add(memButton);
			add(resButton);
			add(fliButton);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);
		}
	}

	////////////////////////////////// 회원관리 패널///////////////////////////////

	public class MemberManagerPanel extends JPanel {
		// controller에서 필요한 것들
		public GoBackButton backButton = new GoBackButton(); // 뒤로가기 버튼
		public JTextField memDeletetextField = new JTextField(); // 회원 삭제 텍스트필드
		public JButton memDeleteButton = new JButton("삭제하기"); // 회원 '삭제하기' 버튼
		public JButton memSearchButton = new JButton("조회하기"); // 회원 '조회하기' 버튼
		public JTextArea memTextArea = new JTextArea(29, 43); // 회원 조회 textArea

		MemberManagerPanel() {
			try {
				pageImg = ImageIO.read(new File("pageImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLayout(null);
			setBounds(200, 150, 1000, 700);

			JLabel lb = new JLabel("회원정보 관리");
			lb.setHorizontalAlignment(JLabel.CENTER);
			lb.setFont(new Font("맑은고딕", Font.BOLD, 40));
			lb.setBounds(350, 0, 300, 100);

			backButton.setBounds(10, 10, 80, 80);

			JTabbedPane mainJtabUI = new JTabbedPane(JTabbedPane.TOP);
			mainJtabUI.setBounds(50, 100, 900, 550);
			mainJtabUI.addTab("회원 삭제", new MemberDelete());

			add(lb);
			add(backButton);
			add(mainJtabUI);

			setVisible(true);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(pageImg, 0, 0, getWidth(), getHeight(), null);
		}

		//////////////////////////////// 회원 삭제////////////////////////////////
		class MemberDelete extends JPanel {
			JPanel mainPanel = new JPanel();
			JPanel p[] = new JPanel[2];
			JPanel searchPanel[] = new JPanel[2];
			JLabel infoLabel = new JLabel("아이디"); // 회원 삭제 라벨

			MemberDelete() {

				for (int i = 0; i < p.length; i++) { // 패널 생성
					p[i] = new JPanel();
					p[i].setBackground(new Color(209, 233, 255));
					searchPanel[i] = new JPanel();
					searchPanel[i].setBackground(new Color(209, 233, 255));
				}

				p[0].setLayout(new BorderLayout());

				memSearchButton.setFocusPainted(false);
				memSearchButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
				memSearchButton.setForeground(new Color(255, 255, 255));
				memSearchButton.setBackground(new Color(128, 128, 128));

				searchPanel[0].setLayout(new FlowLayout());
				searchPanel[0].add(memSearchButton);

				memTextArea.setEditable(false);
				searchPanel[1].add(new JScrollPane(memTextArea));

				p[0].add(searchPanel[0], BorderLayout.NORTH);
				p[0].add(searchPanel[1], BorderLayout.CENTER);

				p[1].setLayout(null);
				infoLabel.setHorizontalAlignment(JLabel.CENTER);
				infoLabel.setFont(new Font("맑은고딕", Font.BOLD, 18));

				infoLabel.setLocation(50, 170);
				infoLabel.setSize(80, 80);
				p[1].add(infoLabel);
				memDeletetextField.setLocation(190, 185);
				memDeletetextField.setSize(200, 60);
				p[1].add(memDeletetextField); // 라벨 텍스트필드 부착

				memDeleteButton.setForeground(new Color(255, 255, 255));
				memDeleteButton.setBackground(new Color(128, 128, 128));
				memDeleteButton.setFocusPainted(false);
				memDeleteButton.setFont(new Font("맑은고딕", Font.BOLD, 17));
				memDeleteButton.setLocation(50, 380);
				memDeleteButton.setSize(340, 50);
				p[1].add(memDeleteButton);

				mainPanel.setLayout(new GridLayout(1, 2));
				mainPanel.add(p[0]);
				mainPanel.add(p[1]);
				add(mainPanel);
			}
		}
	}

	///////////////////////////// 예약 관리 패널/////////////////////////////////

	public class ManagerReservationPanel extends JPanel {
		// controller에서 필요한 것들
		public GoBackButton backButton = new GoBackButton(); // 뒤로가기 버튼
		public JTextArea resTextArea = new JTextArea(29, 87); // 예약 조회 textArea
		public JButton reservationSearchButton = new JButton("조회하기"); // 예약 '조회하기' 버튼

		ManagerReservationPanel() {
			try {
				pageImg = ImageIO.read(new File("pageImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLayout(null);
			setBounds(200, 150, 1000, 700);

			JLabel lb = new JLabel("예약정보 관리");
			lb.setHorizontalAlignment(JLabel.CENTER);
			lb.setFont(new Font("맑은고딕", Font.BOLD, 40));
			lb.setBounds(350, 0, 300, 100);

			backButton.setBounds(10, 10, 80, 80);

			JTabbedPane mainJtabUI = new JTabbedPane(JTabbedPane.TOP); // 탭팬 생성
			mainJtabUI.setBounds(50, 100, 900, 550);
			mainJtabUI.addTab("예약 조회", new ReservationSearch());

			add(lb);
			add(backButton);
			add(mainJtabUI);

			setVisible(true);
		}

		protected void paintComponent(Graphics g) { //배경 이미지 삽입
			super.paintComponent(g);
			g.drawImage(pageImg, 0, 0, getWidth(), getHeight(), null);
		}

		class ReservationSearch extends JPanel { // 예약 조회 패널
			JPanel mainPanel = new JPanel();
			JPanel p[] = new JPanel[2];

			ReservationSearch() {
				for (int i = 0; i < p.length; i++) { // 패널 생성
					p[i] = new JPanel();
					p[i].setBackground(new Color(209, 233, 255));
				}

				reservationSearchButton.setFocusPainted(false);
				reservationSearchButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
				reservationSearchButton.setForeground(new Color(255, 255, 255));
				reservationSearchButton.setBackground(new Color(128, 128, 128));

				p[0].add(reservationSearchButton);
				resTextArea.setEditable(false);
				p[1].add(new JScrollPane(resTextArea));

				mainPanel.setLayout(new BorderLayout());
				mainPanel.add(p[0], BorderLayout.NORTH);
				mainPanel.add(p[1], BorderLayout.CENTER);
				add(mainPanel);

			}
		}
	}

	//////////////////////////// 항공 관리 패널////////////////////////////////

	public class ManagerFlightPanel extends JPanel {

		// controller에서 필요한 것들
		public GoBackButton backButton = new GoBackButton(); // 뒤로가기 버튼
		public JTabbedPane mainJtabUI; // 항공 관리 패널 탭팬
		public JButton flightCreateSearchButton = new JButton("조회하기"); // 항공 등록 탭팬의 조회하기 버튼
		public JTextArea createTextArea = new JTextArea(29, 43); // 항공 등록 탭팬의 모든 항공 조회 textArea
		public JButton flightUpdateSearchButton = new JButton("조회하기"); // 항공 변경 탭팬의 조회하기 버튼
		public JTextArea updateTextArea = new JTextArea(29, 43); // 항공 변경 탭팬의 모든 항공 조회 textArea
		public JButton flightDeleteSearchButton = new JButton("조회하기"); // 항공 삭제 탭팬의 조회하기 버튼
		public JTextArea deleteTextArea = new JTextArea(29, 43); // 항공 삭제 탭팬의 모든 항공 조회 textArea

		public JButton flightCreateButton = new JButton("등록하기"); // 항공 '등록하기' 버튼
		public JTextField fliCreatetextField[] = new JTextField[5]; // 항공 등록 텍스트필드
		public JComboBox departureAirportCreateCombo = new JComboBox(); // 항공 등록에서 출발 공항 콤보박스
		public JComboBox destAirportCreateCombo = new JComboBox(); // 항공 등록에서 도착 공항 콤보박스
		public String comboStr[] = { "인천", "김포", "제주", "대구", "김해" };

		public JButton flightUpdateButton = new JButton("변경하기"); // 항공 '변경하기' 버튼
		public JTextField fliUpdatetextField[] = new JTextField[5]; // 항공 변경 텍스트필드
		public JButton flightDeleteButton = new JButton("삭제하기"); // 항공 '삭제하기' 버튼
		public JTextField fliDeletetextField = new JTextField(); // 항공 삭제 텍스트필드

		ManagerFlightPanel() {
			try {
				pageImg = ImageIO.read(new File("pageImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setBackground(Color.lightGray);
			setLayout(null);
			setBounds(200, 150, 1000, 700);

			JLabel lb = new JLabel("항공정보 관리");
			lb.setHorizontalAlignment(JLabel.CENTER);
			lb.setFont(new Font("맑은고딕", Font.BOLD, 40));
			lb.setBounds(350, 0, 300, 100);

			backButton.setBounds(10, 10, 80, 80);

			mainJtabUI = new JTabbedPane(JTabbedPane.TOP);
			mainJtabUI.setBounds(50, 100, 900, 550);
			mainJtabUI.addTab("항공 등록", new FlightCreate());
			mainJtabUI.addTab("항공 변경", new FlightUpdate());
			mainJtabUI.addTab("항공 삭제", new FlightDelete());

			add(lb);
			add(backButton);
			add(mainJtabUI);

			setVisible(true);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(pageImg, 0, 0, getWidth(), getHeight(), null);
		}

		class FlightCreate extends JPanel { // 항공 등록 패널
			JPanel mainPanel = new JPanel();
			JPanel p[] = new JPanel[2];
			JPanel searchPanel[] = new JPanel[2];
			JLabel infoLabel[] = new JLabel[7]; // 항공 등록 라벨
			String infoStr[] = { "항공사명", "출발공항", "도착공항", "출발시간", "도착시간", "일반석운임", "비즈니스석운임" }; // 라벨 내용

			FlightCreate() {

				for (int i = 0; i < p.length; i++) { // 왼쪽, 오른쪽 패널 생성
					p[i] = new JPanel();
					p[i].setBackground(new Color(209, 233, 255));
					searchPanel[i] = new JPanel();
					searchPanel[i].setBackground(new Color(209, 233, 255));
				}

				p[0].setLayout(new BorderLayout());

				flightCreateSearchButton.setFocusPainted(false);
				flightCreateSearchButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
				flightCreateSearchButton.setForeground(new Color(255, 255, 255));
				flightCreateSearchButton.setBackground(new Color(128, 128, 128));

				searchPanel[0].setLayout(new FlowLayout());
				searchPanel[0].add(flightCreateSearchButton);
				createTextArea.setEditable(false);
				searchPanel[1].add(new JScrollPane(createTextArea));

				p[0].add(searchPanel[0], BorderLayout.NORTH);
				p[0].add(searchPanel[1], BorderLayout.CENTER);

				p[1].setLayout(null);
				for (int i = 0; i < infoLabel.length; i++) { // 라벨 생성
					infoLabel[i] = new JLabel(infoStr[i]);
					infoLabel[i].setHorizontalAlignment(JLabel.CENTER);
					infoLabel[i].setFont(new Font("맑은고딕", Font.BOLD, 18));
				}

				for (int i = 0; i < fliCreatetextField.length; i++) { // 텍스트필드 생성
					fliCreatetextField[i] = new JTextField();
				}

				for (int i = 0; i < infoLabel.length; i++) { // 항공 등록 라벨 부착
					infoLabel[i].setLocation(45, 45 + (i * 55));
					infoLabel[i].setSize(130, 60);
					p[1].add(infoLabel[i]);
				}

				for (int i = 0; i < fliCreatetextField.length; i++) { // 항공 등록 텍스트필드 부착
					if (i == 0) {
						fliCreatetextField[i].setLocation(185, 57 + (i * 55));
						fliCreatetextField[i].setSize(200, 40);
						p[1].add(fliCreatetextField[i]);
					} else {
						fliCreatetextField[i].setLocation(185, 57 + ((i + 2) * 55));
						fliCreatetextField[i].setSize(200, 40);
						p[1].add(fliCreatetextField[i]);
					}
				}

				for (int i = 0; i < comboStr.length; i++) { // 콤보박스 아이템 생성
					departureAirportCreateCombo.addItem(comboStr[i]);
					destAirportCreateCombo.addItem(comboStr[i]);
				}
				departureAirportCreateCombo.setBounds(185, 112, 200, 40);
				departureAirportCreateCombo.setFont(new Font("맑은고딕", Font.BOLD, 16));
				p[1].add(departureAirportCreateCombo); // 출발공항 콤보박스 부착

				destAirportCreateCombo.setBounds(185, 167, 200, 40);
				destAirportCreateCombo.setFont(new Font("맑은고딕", Font.BOLD, 16));
				p[1].add(destAirportCreateCombo); // 도착공항 콤보박스 부착

				flightCreateButton.setFocusPainted(false);
				flightCreateButton.setForeground(new Color(255, 255, 255));
				flightCreateButton.setBackground(new Color(128, 128, 128));
				flightCreateButton.setFont(new Font("맑은고딕", Font.BOLD, 17));
				flightCreateButton.setLocation(50, 450);
				flightCreateButton.setSize(340, 50);

				p[1].add(flightCreateButton);

				mainPanel.setLayout(new GridLayout(1, 2));
				mainPanel.add(p[0]);
				mainPanel.add(p[1]);
				add(mainPanel);
			}
		}

		class FlightUpdate extends JPanel { // 항공 변경 패널
			JPanel mainPanel = new JPanel();
			JPanel p[] = new JPanel[2];
			JPanel searchPanel[] = new JPanel[2];
			JLabel infoLabel[] = new JLabel[5]; // 항공 변경 라벨
			String infoStr[] = { "항공ID", "출발시간", "도착시간", "일반석운임", "비즈니스석운임" };

			FlightUpdate() {
				for (int i = 0; i < p.length; i++) {
					p[i] = new JPanel();
					p[i].setBackground(new Color(209, 233, 255));
					searchPanel[i] = new JPanel();
					searchPanel[i].setBackground(new Color(209, 233, 255));
				}

				p[0].setLayout(new BorderLayout());

				flightUpdateSearchButton.setFocusPainted(false);
				flightUpdateSearchButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
				flightUpdateSearchButton.setForeground(new Color(255, 255, 255));
				flightUpdateSearchButton.setBackground(new Color(128, 128, 128));

				searchPanel[0].setLayout(new FlowLayout());
				searchPanel[0].add(flightUpdateSearchButton);
				updateTextArea.setEditable(false);
				searchPanel[1].add(new JScrollPane(updateTextArea));

				p[0].add(searchPanel[0], BorderLayout.NORTH);
				p[0].add(searchPanel[1], BorderLayout.CENTER);

				p[1].setLayout(null);
				for (int i = 0; i < infoLabel.length; i++) {
					infoLabel[i] = new JLabel(infoStr[i]);
					infoLabel[i].setHorizontalAlignment(JLabel.CENTER);
					infoLabel[i].setFont(new Font("맑은고딕", Font.BOLD, 18));
				}
				for (int i = 0; i < fliUpdatetextField.length; i++) {
					fliUpdatetextField[i] = new JTextField();
				}
				for (int i = 0; i < 5; i++) {
					infoLabel[i].setLocation(45, 35 + (i * 75));
					infoLabel[i].setSize(130, 80);
					p[1].add(infoLabel[i]);
					fliUpdatetextField[i].setLocation(185, 50 + (i * 75));
					fliUpdatetextField[i].setSize(200, 60);
					p[1].add(fliUpdatetextField[i]);
				}

				flightUpdateButton.setFocusPainted(false);
				flightUpdateButton.setForeground(new Color(255, 255, 255));
				flightUpdateButton.setBackground(new Color(128, 128, 128));
				flightUpdateButton.setFont(new Font("맑은고딕", Font.BOLD, 17));
				flightUpdateButton.setLocation(50, 450);
				flightUpdateButton.setSize(340, 50);

				p[1].add(flightUpdateButton);

				mainPanel.setLayout(new GridLayout(1, 2));
				mainPanel.add(p[0]);
				mainPanel.add(p[1]);
				add(mainPanel);
			}
		}

		class FlightDelete extends JPanel { // 항공 삭제 패널
			JPanel mainPanel = new JPanel();
			JPanel p[] = new JPanel[2];
			JPanel searchPanel[] = new JPanel[2];
			JLabel infoLabel = new JLabel("항공ID"); // 항공 삭제 라벨

			FlightDelete() {
				for (int i = 0; i < p.length; i++) {
					p[i] = new JPanel();
					p[i].setBackground(new Color(209, 233, 255));
					searchPanel[i] = new JPanel();
					searchPanel[i].setBackground(new Color(209, 233, 255));
				}

				p[0].setLayout(new BorderLayout());

				flightDeleteSearchButton.setFocusPainted(false);
				flightDeleteSearchButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
				flightDeleteSearchButton.setForeground(new Color(255, 255, 255));
				flightDeleteSearchButton.setBackground(new Color(128, 128, 128));

				searchPanel[0].setLayout(new FlowLayout());
				searchPanel[0].add(flightDeleteSearchButton);
				deleteTextArea.setEditable(false);
				searchPanel[1].add(new JScrollPane(deleteTextArea));

				p[0].add(searchPanel[0], BorderLayout.NORTH);
				p[0].add(searchPanel[1], BorderLayout.CENTER);

				p[1].setLayout(null);

				infoLabel.setHorizontalAlignment(JLabel.CENTER);
				infoLabel.setFont(new Font("맑은고딕", Font.BOLD, 18));

				infoLabel.setLocation(45, 160);
				infoLabel.setSize(120, 90);
				p[1].add(infoLabel);
				fliDeletetextField.setLocation(185, 175);
				fliDeletetextField.setSize(200, 60);
				p[1].add(fliDeletetextField);

				flightDeleteButton.setFocusPainted(false);
				flightDeleteButton.setForeground(new Color(255, 255, 255));
				flightDeleteButton.setBackground(new Color(128, 128, 128));
				flightDeleteButton.setFont(new Font("맑은고딕", Font.BOLD, 17));
				flightDeleteButton.setLocation(50, 390);
				flightDeleteButton.setSize(340, 50);
				p[1].add(flightDeleteButton);

				mainPanel.setLayout(new GridLayout(1, 2));
				mainPanel.add(p[0]);
				mainPanel.add(p[1]);
				add(mainPanel);
			}
		}
	}

	public void addButtonActionListener(ActionListener listener) { //버튼에 ActionListener 부착
		managerMenuPanel.backButton.addActionListener(listener);
		managerMenuPanel.memButton.addActionListener(listener);
		managerMenuPanel.resButton.addActionListener(listener);
		managerMenuPanel.fliButton.addActionListener(listener);

		managerPanel.backButton.addActionListener(listener);
		managerPanel.memDeleteButton.addActionListener(listener);
		managerPanel.memSearchButton.addActionListener(listener);

		flightPanel.flightCreateSearchButton.addActionListener(listener);
		flightPanel.flightUpdateSearchButton.addActionListener(listener);
		flightPanel.flightDeleteSearchButton.addActionListener(listener);

		flightPanel.flightCreateButton.addActionListener(listener);
		flightPanel.flightUpdateButton.addActionListener(listener);
		flightPanel.flightDeleteButton.addActionListener(listener);
		flightPanel.backButton.addActionListener(listener);
		reservationPanel.reservationSearchButton.addActionListener(listener);
		reservationPanel.backButton.addActionListener(listener);
	}

	public void addChangeListener(ChangeListener changeListener) { //항공 관리 탭팬에 ChangeListener 부착
		flightPanel.mainJtabUI.addChangeListener((ChangeListener) changeListener);
	}
	
	public void memberDeleteDialog(int result) { //회원 삭제 다이얼로그
		if (result > 0)
			JOptionPane.showMessageDialog(null, "회원 삭제 완료!");
		else
			JOptionPane.showMessageDialog(null, "회원 삭제 실패!");
	}

	public void flightCreateDialog(int result) { //항공 등록 다이얼로그
		if (result > 0)
			JOptionPane.showMessageDialog(null, "항공 등록 완료!");
		else
			JOptionPane.showMessageDialog(null, "항공 등록 실패!");
	}

	public void flightUpdateDialog(int result) { //항공 변경 다이얼로그
		if (result > 0)
			JOptionPane.showMessageDialog(null, "항공 변경 완료!");
		else
			JOptionPane.showMessageDialog(null, "항공 변경 실패!");
	}

	public void flightDeleteDialog(int result) { //항공 삭제 다이얼로그
		if (result > 0)
			JOptionPane.showMessageDialog(null, "항공 삭제 완료!");
		else
			JOptionPane.showMessageDialog(null, "항공 삭제 실패!");
	}
	
	public void flightTextFieldDialog() { //항공 텍스트필드 다이얼로그
		JOptionPane.showMessageDialog(null, "입력칸을 모두 채워주세요!");
	}
	
	public void flightFreightDialog() { //항공 운임 다이얼로그
		JOptionPane.showMessageDialog(null, "운임은 숫자로만 입력해주세요!");
	}
	
	public void flightIDDialog() { //항공권 ID 다이얼로그
		JOptionPane.showMessageDialog(null, "항공권 ID는 숫자로만 입력해주세요!");
	}
	
	

}