package Controller;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import View.LoginUIFrame;
import View.UserUIFrame;
import View.LoginUIFrame.LoginUIPanel;
import View.UserUIFrame.UserMenuPanel;
import View.UserUIFrame.MyInfoPanel.MyInfoUpdatePanel;
import View.UserUIFrame.MyInfoPanel.MyReservationUpdatePanel;
import View.ManagerUIFrame;

public class MainController {
	UserUIController UC;
	ManagerUIController MC;
	LoginUIController LC;
	MainController MCT;

	View.UserUIFrame UF;
	View.ManagerUIFrame MF;
	View.LoginUIFrame LF;

	public MainController() {
		MCT = this;
	}

	class UserUIController {
		private final UserUIFrame v;

		public UserUIController(UserUIFrame ui) {

			this.v = ui;

			v.addButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Object obj = e.getSource();

//					public UserMenuPanel userMenuPanel;
					if (obj == v.userMenuPanel.myInfoButton) {
						v.card.show(v.c, "myInfo");
					} else if (obj == v.userMenuPanel.flightResButton) {
						v.card.show(v.c, "reservation");
					} else if (obj == v.userMenuPanel.backButton) {
						LF = new LoginUIFrame();
						MCT.setLoginC(LF);
						v.userMenuExit();
					}

//					public MyInfoPanel myInfoPanel;
					if (obj == v.myInfoPanel.backButton) {// 뒤로가기버튼
						v.card.show(v.c, "userMenu");
					}

//					public class MyInfoUpdatePanel extends JPanel {
					if (e.getSource() == v.myInfoPanel.myInfoUpdatePanel.saveButton) {// 내정보 문서로 저장하기 버튼
						int ret = v.myInfoPanel.myInfoUpdatePanel.chooser.showSaveDialog(null);
						if (ret != JFileChooser.APPROVE_OPTION) {
							JOptionPane.showMessageDialog(null, "경로를 선택하지 않았습니다");
							return;
						}
						v.myInfoPanel.myInfoUpdatePanel.pathName = v.myInfoPanel.myInfoUpdatePanel.chooser
								.getSelectedFile().getPath();
						File wfile = new File(v.myInfoPanel.myInfoUpdatePanel.pathName);
						try {
							BufferedWriter writer = new BufferedWriter(new FileWriter(wfile));
							String s;
							s = v.myInfoPanel.myInfoUpdatePanel.textArea.getText();
							writer.write(s + "\r\n");
							writer.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						System.out.println("SAVE Done...");
					}

					else if (e.getSource() == v.myInfoPanel.myInfoUpdatePanel.updateButton) {// 내정보 변경하기 버튼
						String memo = new String("*항공예약시스템에 저장된 내 정보*\r\n\r\n");
						for (int i = 0; i < 6; i++) {
							memo += (v.myInfoPanel.myInfoUpdatePanel.infoStr[i] + ": "
									+ v.myInfoPanel.myInfoUpdatePanel.textField[i].getText() + "\r\n");
						}
						v.myInfoPanel.myInfoUpdatePanel.textArea.setText(memo);
					} else if (e.getSource() == v.myInfoPanel.myInfoUpdatePanel.cancelButton) {// 프로그램 탈퇴하기 버튼
						String memo = new String("*항공예약시스템에 저장된 내 정보*\r\n\r\n");
						for (int i = 0; i < 6; i++) {
							memo += (v.myInfoPanel.myInfoUpdatePanel.infoStr[i] + ": "
									+ v.myInfoPanel.myInfoUpdatePanel.textField[i].getText() + "\r\n");
						}
						v.myInfoPanel.myInfoUpdatePanel.textArea.setText(memo);
					}

//					public class MyReservationUpdatePanel extends JPanel {// 내 항공편 예약 현황
					if (e.getSource() == v.myInfoPanel.myReservationUpdatePanel.changeSeatBtn) {// 자리변경하기 버튼
						String resnum = JOptionPane.showInputDialog("자리변경 할 항공편의 예약번호를 입력하세요");
						if (resnum != null) {
							////////// dB에서 해당 항공기 예약정보를 삭제하고
							////////// 항공편 자리선택 화면으로 가서 다시 예약
							v.card.show(v.c, "selectSeat");
						}
					} else if (e.getSource() == v.myInfoPanel.myReservationUpdatePanel.cancleResBtn) {// 예약취소하기 버튼
						String resnum = JOptionPane.showInputDialog("예약취소 할 항공편의 예약번호를 입력하세요");
						if (resnum != null) {
							////////// dB에서 해당 항공기 예약정보를 삭제
							JOptionPane.showMessageDialog(null, "예약취소 되었습니다.");
						}
					}

//					public FlightResPanel flightResPanel;
					if (obj == v.flightResPanel.flightSearchButton) {// 비행기 검색하기 버튼

					}

//					public class RegisterFlightPanel extends JPanel implements ItemListener {
					if (e.getSource() == v.flightResPanel.flightSearchButton) {// 비행기 검색하기 버튼

					}

					if (e.getSource() == v.flightResPanel.selectSeatButton1) {// 가는자리선택하기 버튼
						if (v.flightResPanel.flightsearchTextField[2].getText().equals("")) {// 인원수 textfield 비어있으면
							JOptionPane.showMessageDialog(null, "탑승할 인원을 선택하세요!");
							return;
						}
						v.resNum = Integer.valueOf(v.flightResPanel.flightsearchTextField[2].getText());
						v.card.show(v.c, "selectSeat");
					}

					if (e.getSource() == v.flightResPanel.selectSeatButton2) {// 오는자리선택하기 버튼
						if (v.flightResPanel.flightsearchTextField[2].getText().equals("")) {// 인원수 textfield 비어있으면
							JOptionPane.showMessageDialog(null, "탑승할 인원을 선택하세요!");
							return;
						}
						v.resNum = Integer.valueOf(v.flightResPanel.flightsearchTextField[2].getText());
						v.card.show(v.c, "selectSeat");
					}

					if (e.getSource() == v.flightResPanel.departureAirportCombo) {// 출발 공항 콤보박스
						int index = v.flightResPanel.departureAirportCombo.getSelectedIndex();
					}
					if (e.getSource() == v.flightResPanel.destAirportCombo) {// 도착 공항 콤보박스
						int index = v.flightResPanel.destAirportCombo.getSelectedIndex();
					}

					if (obj == v.flightResPanel.selectSeatButton1) {// 가는자리선택하기 버튼
						if (v.flightResPanel.flightsearchTextField[2].getText().equals("")) {// 인원수 textfield 비어있으면
							JOptionPane.showMessageDialog(null, "탑승할 인원을 선택하세요!");
							return;
						}
						v.resNum = Integer.valueOf(v.flightResPanel.flightsearchTextField[2].getText());
						v.card.show(v.c, "selectSeat");
					}

					if (obj == v.flightResPanel.selectSeatButton2) {// 오는자리선택하기 버튼
						if (v.flightResPanel.flightsearchTextField[2].getText().equals("")) {// 인원수 textfield 비어있으면
							JOptionPane.showMessageDialog(null, "탑승할 인원을 선택하세요!");
							return;
						}
						v.resNum = Integer.valueOf(v.flightResPanel.flightsearchTextField[2].getText());
						v.card.show(v.c, "selectSeat");
					}

					if (obj == v.flightResPanel.departureAirportCombo) {// 출발 공항 콤보박스
						int index = v.flightResPanel.departureAirportCombo.getSelectedIndex();
					}
					if (obj == v.flightResPanel.destAirportCombo) {// 도착 공항 콤보박스
						int index = v.flightResPanel.destAirportCombo.getSelectedIndex();
					}

//					public SelectSeatPanel selectSeatPanel;
					if (e.getSource() == v.selectSeatPanel.backButton) {// 뒤로가기 버튼
						v.card.show(v.c, "reservation");

						v.selectSeatPanel.cnt = 0;
						v.selectSeatPanel.seatlist.clear();
						v.selectedSeatTextarea.setText("");

						for (int i = 0; i < 40; i++) {
							if (i % 10 == 0) {
								v.selectSeatPanel.seatButton[i].setBackground(new Color(112, 48, 160));
							} else {
								v.selectSeatPanel.seatButton[i].setBackground(new Color(46, 117, 182));
							}
						}
					}
//					public class SelectSeatTab extends JPanel {
					if (e.getSource() == v.selectSeatPanel.reserveButton) {// 예약하기버튼
						JOptionPane.showMessageDialog(null, "예약되셨습니다!");
						v.card.show(v.c, "reservation");
						v.selectSeatPanel.cnt = 0;
						v.selectSeatPanel.seatlist.clear();
						v.selectedSeatTextarea.setText("");
						for (int i = 0; i < 40; i++) {// 일단 좌석 색깔 다 초기화 시킴, 예약된 좌석은 라벨 x로 바꾸고, 선택 안되게 설정해주어야함.
							if (i % 10 == 0) {
								v.selectSeatPanel.seatButton[i].setBackground(new Color(112, 48, 160));
							} else {
								v.selectSeatPanel.seatButton[i].setBackground(new Color(46, 117, 182));
							}
						}
						return;// 밑에 for문이니깐 예약하기 버튼이면 걍 return 시켜주기
					}

					for (int i = 0; i < v.selectSeatPanel.seatButton.length; i++) {

						if (e.getSource() == v.selectSeatPanel.seatButton[i]) {
							for (int j = 0; j < v.selectSeatPanel.seatlist.size(); j++) {
								if (i == v.selectSeatPanel.seatlist.get(j)) {
									return;
								}
							}
							v.selectSeatPanel.seatlist.add(i);
							v.selectSeatPanel.cnt++;
							if (v.selectSeatPanel.cnt > v.resNum) {
								return;
							}
							v.selectSeatPanel.seatButton[i].setBackground(new Color(255, 192, 0));
							v.selectSeatPanel.seatButton[i].setFocusPainted(false);
							String str = "";
							if (i < 10) {
								str += "A";
							} else if (i < 20) {
								str += "B";
							} else if (i < 30) {
								str += "C";
							} else if (i < 40) {
								str += "D";
							}
							v.selectedSeatTextarea
									.setText(v.selectedSeatTextarea.getText() + "  " + str + (i % 10 + 1));
						}

						else if (e.getSource() == v.selectSeatPanel.selectSeatTab.resetButton) {// 좌석초기화 버튼
							if (i % 10 == 0) {
								v.selectSeatPanel.seatButton[i].setBackground(new Color(112, 48, 160));
							} else {
								v.selectSeatPanel.seatButton[i].setBackground(new Color(46, 117, 182));
							}
							v.selectSeatPanel.cnt = 0;
							v.selectSeatPanel.seatlist.clear();
							v.selectedSeatTextarea.setText("");
						}
					}

				}

			});

		}
	}

	class ManagerUIController {
		private final ManagerUIFrame v;

		public ManagerUIController(ManagerUIFrame ui) {

			this.v = ui;

			v.addButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Object obj = e.getSource();
//					if(obj == v.loginUIpanel.backButton) {
//						System.out.println("굳!");
//					}

				}

			});

		}
	}

	class LoginUIController {
		private final LoginUIFrame v;

		public LoginUIController(LoginUIFrame ui) {

			this.v = ui;

			v.addButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Object obj = e.getSource();
					if (obj == v.startUIPanel.managerButton) {
						System.out.println("매니저");
						new ManagerUIFrame();
						v.loginUIFrameExit();
					}
					if (obj == v.startUIPanel.userButton) {
						System.out.println("사용자");
//						LF = new View.LoginUIFrame();
//						MCT.setLoginC(LF);
//						v.
						v.card.next(v.c);
					}

					if (obj == v.loginUIpanel.loginButton[0]) {
						System.out.println("로그인");
					}

//					public LoginUIPanel loginUIpanel=new LoginUIPanel();
//				      public JButton backButton = new JButton("돌아가기");//돌아가기 버튼
//				      
//				      public JLabel loginLabel[] = new JLabel[2];//로그인 레이블
//				      public String loginLabelStr[] = {"아이디", "비밀번호"};
//				      public JTextField loginTextField[] = new JTextField[2];//로그인 정보 입력받는 textField
//				      public JButton loginButton[] = new JButton[2];//로그인 버튼
//				      public String loginButtonStr[] = {"로그인", "회원가입"};

					if (obj == v.loginUIpanel.loginButton[0]) {// 로그인 버튼
						v.userId = v.loginUIpanel.loginTextField[0].getText();
						if (v.dao.getUser(v.userId) == null) {
							JOptionPane.showMessageDialog(null, "존재하지 않는 회원정보입니다.");
							return;
						}
						UF = new UserUIFrame(v.userId);
						MCT.setUserC(UF);
						v.loginUIFrameExit();

					} else if (obj == v.loginUIpanel.loginButton[1]) {// 회원가입 버튼
						v.card.next(v.c);
					}

					else if (obj == v.loginUIpanel.backButton) {// 돌아가기 버튼
						v.card.show(v.c, "1");
					}

				}

			});

		}
	}

	public void setLoginC(LoginUIFrame ui) {
		this.LC = new LoginUIController(ui);
	}

	public void setUserC(UserUIFrame ui) {
		this.UC = new UserUIController(ui);
	}

	public void setManagerC(ManagerUIFrame ui) {
		this.MC = new ManagerUIController(ui);
	}

	public static void main(String[] args) {

		LoginUIFrame ui = new LoginUIFrame();
		MainController Controller = new MainController();
		Controller.setLoginC(ui);

	}

}
