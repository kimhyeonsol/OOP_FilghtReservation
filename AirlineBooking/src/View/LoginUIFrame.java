package View;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Model.User;
import Model.UserDAO;

public class LoginUIFrame extends JFrame {
	public ArrayList<JButton> componentList = new ArrayList<JButton>();

	public Container c;
	public CardLayout card;
	public BufferedImage backImage, btn1Image, btn2Image, Image;
	public StartUIPanel startUIPanel = new StartUIPanel();
	public LoginUIPanel loginUIpanel = new LoginUIPanel();
	public SignUpPanel signUpPanel = new SignUpPanel();
	public String userId = "", font = "함초롬돋움";// 사용자 아이디 문자열

	public UserDAO dao;


	public LoginUIFrame() {
		setTitle("LoginUIFrame");
		setBounds(450, 200, 500, 380);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		c = this.getContentPane();
		card = new CardLayout();
		c.setLayout(card);
		c.add(startUIPanel, "1");
		c.add(loginUIpanel, "2");
		c.add(signUpPanel, "3");

		setVisible(true);
	}

	public void loginUIFrameExit() {
		this.dispose();
	}

	/////////////////////////////////////////////// 관리자, 사용자 선택
	/////////////////////////////////////////////// 카드/////////////////////////////////
	public class StartUIPanel extends JPanel {
		public ManagerButton managerButton = new ManagerButton();// 관리자 버튼
		public UserButton userButton = new UserButton();// 사용자 버튼

		public StartUIPanel() {
			try {
				backImage = ImageIO.read(new File("loginImg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLayout(null);

			managerButton.setBounds(50, 150, 150, 150);
			userButton.setBounds(280, 150, 150, 150);
			add(managerButton);
			add(userButton);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);
		}

		class ManagerButton extends JButton {// 관리자 버튼 클래스
			ManagerButton() {
				try {
					btn1Image = ImageIO.read(new File("managerImg.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(btn1Image, 0, 0, getWidth(), getHeight(), null);
			}
		}

		class UserButton extends JButton {// 사용자 버튼 클래스

			UserButton() {
				try {
					btn2Image = ImageIO.read(new File("UserImg.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(btn2Image, 0, 0, getWidth(), getHeight(), null);
			}
		}

	}

	////////////////////////////////////////////// 로그인
	////////////////////////////////////////////// 카드////////////////////////////////////////////
	public class LoginUIPanel extends JPanel {

		public JButton backButton = new JButton("돌아가기");// 돌아가기 버튼

		public JLabel loginLabel[] = new JLabel[2];// 로그인 레이블
		public String loginLabelStr[] = { "아이디", "비밀번호" };
		public JTextField loginTextField[] = new JTextField[2];// 로그인 정보 입력받는 textField
		public JButton loginButton[] = new JButton[2];// 로그인 버튼
		public String loginButtonStr[] = { "로그인", "회원가입" };

		public LoginUIPanel() {
			setLayout(null);

			// 로그인 레이블 부착
			for (int i = 0; i < loginLabel.length; i++) {
				loginLabel[i] = new JLabel(loginLabelStr[i]);
				loginLabel[i].setFont(new Font("맑은고딕", Font.BOLD, 20));
				add(loginLabel[i]);
			}
			loginLabel[0].setBounds(66, 135, 82, 40);
			loginLabel[1].setBounds(66, 177, 105, 40);

			// 로그인 텍스트필드 부착
			for (int i = 0; i < loginTextField.length; i++) {
				loginTextField[i] = new JTextField();
				loginTextField[i].setColumns(10);
				add(loginTextField[i]);
			}
			loginTextField[0].setBounds(169, 135, 241, 40);
			loginTextField[1].setBounds(169, 177, 241, 40);

			// 로그인 버튼 부착
			for (int i = 0; i < loginButton.length; i++) {
				loginButton[i] = new JButton(loginButtonStr[i]);
				loginButton[i].setForeground(new Color(255, 255, 255));
				loginButton[i].setBackground(new Color(37, 51, 42));
				loginButton[i].setFocusPainted(false);

				componentList.add(loginButton[i]);
				add(loginButton[i]);
			}
			loginButton[0].setBounds(65, 240, 345, 30);
			loginButton[1].setBounds(280, 284, 130, 27);
			loginButton[0].setFont(new Font("맑은고딕", Font.BOLD, 17));
			loginButton[1].setFont(new Font("맑은고딕", Font.BOLD, 15));

			// 뒤로가기 버튼 부착
			backButton.setForeground(new Color(255, 255, 255));
			backButton.setBackground(new Color(37, 51, 42));
			backButton.setFocusPainted(false);

			componentList.add(backButton);
			backButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
			backButton.setBounds(65, 284, 130, 27);
			add(backButton);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);
		}

	}

	////////////////////////////////////////////// 회원가입
	////////////////////////////////////////////// 카드////////////////////////////////////////////
	public class SignUpPanel extends JPanel {

		public JLabel titleLabel = new JLabel("회원가입");// 회원가입 라벨
		public JLabel infoLabel[] = new JLabel[6];// 회원 정보 라벨 배열
		public String infoStr[] = { "이름", "아이디", "비밀번호", "이메일", "생년월일(ex.980814)", "전화번호" };
		public JTextField textField[] = new JTextField[6];// 회원 정보 라벨 텍스트필드
		public JButton signUpButton = new JButton("회원가입");// 회원가입 버튼
		public JButton backButton = new JButton("돌아가기");// 뒤로가기 버튼

		public SignUpPanel() {

			setLayout(null);
			this.setBackground(new Color(113, 151, 126));

			// 타이틀 라벨 부착
			titleLabel.setForeground(Color.white);
			titleLabel.setFont(new Font(font, Font.BOLD, 25));
			titleLabel.setBounds(26, 10, 200, 40);
			add(titleLabel);

			// 회원정보 라벨 부착
			for (int i = 0; i < infoLabel.length; i++) {
				infoLabel[i] = new JLabel(infoStr[i]);
				infoLabel[i].setFont(new Font("맑은고딕", Font.BOLD, 16));
				infoLabel[i].setBounds(80, 64 + (40 * i), 150, 23);
				add(infoLabel[i]);
			}

			// 회원정보 텍스트필드 부착
			for (int i = 0; i < textField.length; i++) {
				textField[i] = new JTextField();
				textField[i].setBounds(278, 64 + (40 * i), 130, 25);
				add(textField[i]);
			}

			// 돌아가기 버튼 부착
			backButton.setBackground(new Color(37, 51, 42));
			backButton.setFocusPainted(false);
			backButton.setForeground(Color.white);
			backButton.setFont(new Font("맑은고딕", Font.BOLD, 17));
			backButton.setBounds(80, 308, 120, 23);
			componentList.add(backButton);
			add(backButton);

			// 회원가입 버튼 부착
			signUpButton.setBackground(new Color(37, 51, 42));
			signUpButton.setFocusPainted(false);
			signUpButton.setForeground(Color.white);
			signUpButton.setFont(new Font("맑은고딕", Font.BOLD, 17));
			signUpButton.setBounds(290, 308, 120, 23);
			componentList.add(signUpButton);
			add(signUpButton);
			dao = new UserDAO();
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
		}

	}

	public void addButtonActionListener(ActionListener listener) {
		startUIPanel.userButton.addActionListener(listener);
		startUIPanel.managerButton.addActionListener(listener);

		for (JButton comp : componentList) { // 배열 내 버튼 처리
			comp.addActionListener(listener);
		}
	}
	
	public void signUpTextFieldDialog() { //회원가입 텍스트필드 다이얼로그
		JOptionPane.showMessageDialog(null, "입력칸을 모두 채워주세요!");
	}
	
	public void signUpIDDialog() { //회원가입 ID 다이얼로그
		JOptionPane.showMessageDialog(null, "사용 불가능한 ID입니다!");
	}
	
	public void signUpDialog() { //회원가입 다이얼로그
		JOptionPane.showMessageDialog(null, "회원가입되었습니다!");
	}
	
	public void loginMemberInfoDialog() { //로그인 회원정보 다이얼로그
		JOptionPane.showMessageDialog(null, "존재하지 않는 회원정보입니다!");
	}
	
	public void loginPasswordDialog() { //로그인 비밀번호 다이얼로그
		JOptionPane.showMessageDialog(null, "비밀번호를 틀렸습니다!");
	}
	
	
	

}