package Controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Model.AirLine;
import Model.AirLineDAO;
import Model.Reservation;
import Model.ReservationDAO;
import Model.User;
import Model.UserDAO;
import View.LoginUIFrame;
import View.ManagerUIFrame;
import View.UserUIFrame;

public class MainController {
	MainController MCT;
	UserUIController UC;
	ManagerUIController MC;
	LoginUIController LC;

	AirLineDAO aDAO = new AirLineDAO();
	ReservationDAO rDAO = new ReservationDAO();
	UserDAO uDAO;

	public MainController() {
		MCT = this;
		uDAO = new UserDAO();
	}

	View.UserUIFrame UF;
	View.ManagerUIFrame MF;
	View.LoginUIFrame LF;
//
//	AirLineDAO aDAO;
//	UserDAO uDAO;
//	ReservationDAO rDAO;

	class UserUIController {
		private final UserUIFrame v;
		private String _userID;

		public UserUIController(UserUIFrame ui) {

			this.v = ui;
			String data[] = new String[6];

			v.addButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Object obj = e.getSource();

//					FlightResPanel pnFR = v.flightResPanel;
					User currentUser = uDAO.getUser(v._userId);

//					public UserMenuPanel userMenuPanel;
					if (obj == v.userMenuPanel.myInfoButton) {
						v.card.show(v.c, "myInfo");
						currentUser = uDAO.getUser(v._userId);
						v.myInfoPanel.myInfoUpdatePanel.textField[0].setText(currentUser.getName());
						v.myInfoPanel.myInfoUpdatePanel.textField[1].setText(currentUser.getID());
						v.myInfoPanel.myInfoUpdatePanel.textField[2].setText(currentUser.getPw());
						v.myInfoPanel.myInfoUpdatePanel.textField[3].setText(currentUser.getEmail());
						v.myInfoPanel.myInfoUpdatePanel.textField[4].setText(currentUser.getBirth());
						v.myInfoPanel.myInfoUpdatePanel.textField[5].setText(currentUser.getPhone());

						String memo = new String("*항공예약시스템에 저장된 내 정보*\r\n\r\n");
						for (int i = 0; i < 6; i++) {
							memo += (v.myInfoPanel.myInfoUpdatePanel.infoStr[i] + ": "
									+ v.myInfoPanel.myInfoUpdatePanel.textField[i].getText() + "\r\n");
							data[i] = v.myInfoPanel.myInfoUpdatePanel.textField[i].getText();
						}
						v.myInfoPanel.myInfoUpdatePanel.textArea.setText(memo);

						// reservationList update
						ArrayList<Reservation> res = null;
						try {
							res = rDAO.getReservationListByUser(currentUser.getID());
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						String reservationListStr = "";
						for (Reservation r : res) {
							AirLine outAL = aDAO.getALInfo(r.getInfo());
							User outU = uDAO.getUser(r.getUser());

							reservationListStr += r.getID() + " " + outAL.getAirLineNm() + " " + outU.getName() + " "
									+ r.getSeatNum() + "\n";
						}

						v.myInfoPanel.myReservationUpdatePanel.textArea.setText(reservationListStr);

					} else if (obj == v.userMenuPanel.flightResButton) {

						v.card.show(v.c, "reservation");
					} else if (obj == v.userMenuPanel.backButton) {
						LF = new LoginUIFrame();
						MCT.setLoginC(LF);
						v.userMenuExit();
					}

//					public MyInfoPanel myInfoPanel;
					else if (obj == v.myInfoPanel.backButton) {// 뒤로가기버튼
						v.card.show(v.c, "userMenu");
					}

//					public class MyInfoUpdatePanel extends JPanel {
					else if (obj == v.myInfoPanel.myInfoUpdatePanel.saveButton) {// 내정보 문서로 저장하기 버튼
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

					else if (obj == v.myInfoPanel.myInfoUpdatePanel.updateButton) {// 내정보 변경하기 버튼
						String memo = new String("*항공예약시스템에 저장된 내 정보*\r\n\r\n");
						for (int i = 0; i < 6; i++) {
							memo += (v.myInfoPanel.myInfoUpdatePanel.infoStr[i] + ": "
									+ v.myInfoPanel.myInfoUpdatePanel.textField[i].getText() + "\r\n");
							data[i] = v.myInfoPanel.myInfoUpdatePanel.textField[i].getText();
						}
						// #### DAO ####
						User user = new User();
						user.setUser(data);
						uDAO.updateUser(user);
						// #### DAO ####

						System.out.println(memo);
						v.myInfoPanel.myInfoUpdatePanel.textArea.setText(memo);
					} else if (obj == v.myInfoPanel.myInfoUpdatePanel.cancelButton) {// 프로그램 탈퇴하기 버튼
						String memo = new String("*항공예약시스템에 저장된 내 정보*\r\n\r\n");
						for (int i = 0; i < 6; i++) {
							memo += (v.myInfoPanel.myInfoUpdatePanel.infoStr[i] + ": "
									+ v.myInfoPanel.myInfoUpdatePanel.textField[i].getText() + "\r\n");
							data[i] = v.myInfoPanel.myInfoUpdatePanel.textField[i].getText();
						}
						v.myInfoPanel.myInfoUpdatePanel.textArea.setText(memo);
						// #### DAO #### -> 삭제 전: dialog 필요성,
						User user = new User();
						user.setUser(data);
						uDAO.deleteUser(user.getID());
						// #### DAO ####
						MCT.LF = new LoginUIFrame();
						MCT.setLoginC(MCT.LF);
						v.userMenuExit();
					}

//					public class MyReservationUpdatePanel extends JPanel {// 내 항공편 예약 현황
					else if (obj == v.myInfoPanel.myReservationUpdatePanel.changeSeatBtn) {// 자리변경하기 버튼
						String resnum = JOptionPane.showInputDialog("자리변경 할 항공편의 예약번호를 입력하세요");
						v.resNum = 1;
						if (resnum != null) {
//							rDAO.getReservation(Integer.parseInt(resnum));
//							if() ??????
							////////// dB에서 해당 항공기 예약정보를 삭제하고
							// #### DAO ####
//							rDAO.deleteReservation(Integer.parseInt(resnum));
							// #### DAO ####
							////////// 항공편 자리선택 화면으로 가서 다시 예약
							v.card.show(v.c, "selectSeat");
						}
					} else if (obj == v.myInfoPanel.myReservationUpdatePanel.cancleResBtn) {// 예약취소하기 버튼
						String resnum = JOptionPane.showInputDialog("예약취소 할 항공편의 예약번호를 입력하세요");
						if (resnum != null) {
							////////// dB에서 해당 항공기 예약정보를 삭제
							Reservation output = rDAO.getReservation(Integer.parseInt(resnum));
							if (output == null) {
								return;
							}
							// #### DAO ####
							rDAO.deleteReservation(Integer.parseInt(resnum));
							// #### DAO ####
							// reservationList update
							ArrayList<Reservation> res = null;
							try {
								res = rDAO.getReservationListByUser(currentUser.getID());
							} catch (SQLException e1) {
								e1.printStackTrace();
							}

							String reservationListStr = "";
							for (Reservation r : res) {
								AirLine outAL = aDAO.getALInfo(r.getInfo());
								User outU = uDAO.getUser(r.getUser());

								reservationListStr += r.getID() + " " + outAL.getAirLineNm() + " " + outU.getName()
										+ " " + r.getSeatNum() + "\n";
							}

							v.myInfoPanel.myReservationUpdatePanel.textArea.setText(reservationListStr);

							JOptionPane.showMessageDialog(null, "예약취소 되었습니다.");
						}
					}

//					public FlightResPanel flightResPanel;
					else if (obj == v.flightResPanel.backButton) {// 뒤로가기 버튼
						v.card.show(v.c, "userMenu");
					}

//					public class RegisterFlightPanel extends JPanel implements ItemListener {
					if (obj == v.flightResPanel.flightSearchButton) {// 비행기 검색하기 버튼
						// #### DAO ####

						if (v.flightResPanel.radio[1].isSelected()) {
							String destDate = v.flightResPanel.flightsearchTextField[1].getText();
							ArrayList<AirLine> output = null;
							ArrayList<AirLine> output2 = null; // 돌아오는
							try {
								output = aDAO.getAllALInfo();
								output2 = aDAO.getAllALInfo();
								// results by current day/time
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								// 결과가 없음..? 비행기 안뜸.. or 너무 미래
								// 과거 시간 선택 검증
							}
							String depAirLineStr = "";
							for (AirLine r : output) {
								depAirLineStr += r.getID() + " " + r.getAirLineNm() + " " + r.getArrAirportNm() + " "
										+ r.getDepAirportNm() + " " + r.getArrPlandTime() + " " + r.getDepPlandTime()
										+ " " + r.getEconomyCharge() + " " + r.getPrestigeCharge() + "\n";
							}
							String desAirLineStr = "";
							for (AirLine r : output2) {
								desAirLineStr += r.getID() + " " + r.getAirLineNm() + " " + r.getArrAirportNm() + " "
										+ r.getDepAirportNm() + " " + r.getArrPlandTime() + " " + r.getDepPlandTime()
										+ " " + r.getEconomyCharge() + " " + r.getPrestigeCharge() + "\n";
							}
							v.flightResPanel.departureAirportTextArea.setText(depAirLineStr);
							v.flightResPanel.destAirportTextArea.setText(desAirLineStr);
							// output -> 항공기 textArea(scroll)1 반영 및 refresh
						} else if (v.flightResPanel.radio[0].isSelected()) {
							String departDate = v.flightResPanel.flightsearchTextField[0].getText();
							ArrayList<AirLine> output = null;
							try {
								output = aDAO.getAllALInfo();
								// results by current day/time
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								// 결과가 없음..? 비행기 안뜸.. or 너무 미래
								// 과거 시간 선택 검증
							}

							String depAirLineStr = "";
							for (AirLine r : output) {
								depAirLineStr += r.getID() + " " + r.getAirLineNm() + " " + r.getArrAirportNm() + " "
										+ r.getDepAirportNm() + " " + r.getArrPlandTime() + " " + r.getDepPlandTime()
										+ " " + r.getEconomyCharge() + " " + r.getPrestigeCharge() + "\n";
							}
							v.flightResPanel.departureAirportTextArea.setText(depAirLineStr);
							v.flightResPanel.destAirportTextArea.setText("");
							// output -> 항공기 textArea(scroll)1 반영 및 refresh
						}

						// #### DAO ####
					}

					if (obj == v.flightResPanel.selectSeatButton1) {// 가는자리선택하기 버튼
						if (v.flightResPanel.flightsearchTextField[2].getText().equals("")) {// 인원수 textfield 비어있으면
							JOptionPane.showMessageDialog(null, "탑승할 인원을 선택하세요!");
							return;
						}
						try {
							AirLine selectedAirLine = new AirLine();
							selectedAirLine = aDAO
									.getALInfo(Integer.parseInt(v.flightResPanel.selectedFlightIDTextField1.getText()));
							if (selectedAirLine == null) {
								return;
							}
							v._selectedDepAirLine = selectedAirLine.getID();

							ArrayList<Reservation> resList = rDAO.getReservationListByALInfo(selectedAirLine.getID());
							System.out.println(selectedAirLine.getID());
							for (Reservation res : resList) {
								v.selectSeatPanel.seatButton[res.getSeatNum()].setBackground((new Color(255, 192, 0)));
								v.selectSeatPanel.seatButton[res.getSeatNum()].setFocusPainted(true);

							}

							v.resNum = Integer.valueOf(v.flightResPanel.flightsearchTextField[2].getText());
							v.card.show(v.c, "selectSeat");

						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}

					if (obj == v.flightResPanel.selectSeatButton2) {// 오는자리선택하기 버튼
						if (v.flightResPanel.flightsearchTextField[2].getText().equals("")) {// 인원수 textfield 비어있으면
							JOptionPane.showMessageDialog(null, "탑승할 인원을 선택하세요!");
							return;
						}

						try {
							AirLine selectedAirLine = new AirLine();
							selectedAirLine = aDAO
									.getALInfo(Integer.parseInt(v.flightResPanel.selectedFlightIDTextField2.getText()));
							if (selectedAirLine == null) {
								return;
							}
							v._selectedDepAirLine = selectedAirLine.getID();

							ArrayList<Reservation> resList = rDAO.getReservationListByALInfo(selectedAirLine.getID());
							System.out.println(selectedAirLine.getID());
							for (Reservation res : resList) {
								v.selectSeatPanel.seatButton[res.getSeatNum()].setBackground((new Color(255, 192, 0)));
								v.selectSeatPanel.seatButton[res.getSeatNum()].setFocusPainted(true);

							}

							v.resNum = Integer.valueOf(v.flightResPanel.flightsearchTextField[2].getText());
							v.card.show(v.c, "selectSeat");

						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						v.resNum = Integer.valueOf(v.flightResPanel.flightsearchTextField[2].getText());
						v.card.show(v.c, "selectSeat");
					}

					if (obj == v.flightResPanel.departureAirportCombo) {// 출발 공항 콤보박스
						int index = v.flightResPanel.departureAirportCombo.getSelectedIndex();
					} else if (obj == v.flightResPanel.destAirportCombo) {// 도착 공항 콤보박스
						int index = v.flightResPanel.destAirportCombo.getSelectedIndex();
					}

//					public SelectSeatPanel selectSeatPanel;
					else if (obj == v.selectSeatPanel.backButton) {// 뒤로가기 버튼
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
					else if (obj == v.selectSeatPanel.reserveButton) {// 예약하기버튼

						JOptionPane.showMessageDialog(null, "예약되셨습니다!");
						for (int i = 0; i < v.selectSeatPanel.seatlist.size(); i++) {
							int seatNum = v.selectSeatPanel.seatlist.get(i);

							Reservation r = new Reservation();
							r.setInfo(v._selectedDepAirLine);
							r.setSeatNum(seatNum);
							r.setUser(currentUser.getID());
							System.out.println(r.getInfo());
							System.out.println(r.getSeatNum());
							System.out.println(r.getUser());
							rDAO.newReservation(r);

						}

						v.card.show(v.c, "reservation");
						// 항공기 예약 입력 초기화??
						v.selectSeatPanel.cnt = 1;
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

						if (obj == v.selectSeatPanel.seatButton[i]) {
							for (int j = 0; j < v.selectSeatPanel.seatlist.size(); j++) {
								if (i == v.selectSeatPanel.seatlist.get(j)) {
									return;
								}
							}
							v.selectSeatPanel.seatlist.add(i);
							System.out.println(i);
							System.out.println(v.selectSeatPanel.cnt);
							v.selectSeatPanel.cnt++;
							if (v.selectSeatPanel.cnt > v.resNum) {
								v.selectSeatPanel.cnt--;
								v.selectSeatPanel.seatlist.pop();// 초과 시 pop seatlist로 예약 진행
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

						else if (obj == v.selectSeatPanel.selectSeatTab.resetButton) {// 좌석초기화 버튼
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
					if (obj == v.managerMenuPanel.backButton) {
						System.out.println("제발!!");
						LoginUIFrame lu = new LoginUIFrame(); // 로그인 메뉴 화면 띄움
						MCT.setLoginC(lu);
						v.managerMenuExit(); // 관리자 메뉴 프레임 닫음
					}
					if (obj == v.managerMenuPanel.memButton) {
						v.card.show(v.c, "manager"); // 회원 관리 패널로 넘어감
					}
					if (obj == v.managerMenuPanel.resButton) {
						v.card.show(v.c, "reservation"); // 예약 관리 패널로 넘어감
					}
					if (obj == v.managerMenuPanel.fliButton) {
						v.card.show(v.c, "flight"); // 항공 관리 패널로 넘어감
					}
					if (obj == v.managerPanel.backButton) {
						v.card.show(v.c, "managerMenu"); // 관리자메뉴로 넘어감
					}

					if (obj == v.flightPanel.flightSearchButton_c) {
						//// textarea에 전체 출력
						ArrayList<AirLine> list = new ArrayList<AirLine>();
						try {
							list = aDAO.getAllALInfo();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("항공코드\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t\t일반석운임\t비즈니스석운임\n");
							for (AirLine p : list) {
								sb.append(p.getID() + "\t");
								sb.append(p.getAirLineNm() + "\t");
								sb.append(p.getArrAirportNm() + "\t");
								sb.append(p.getDepAirportNm() + "\t");
								sb.append(p.getArrPlandTime() + "\t");
								sb.append(p.getDepPlandTime() + "\t\t");
								sb.append(p.getEconomyCharge() + "\t");
								sb.append(p.getPrestigeCharge() + "\t\n");
							}
						}
						v.setTextArea(v.flightPanel.textArea_c, sb);

					}

					if (obj == v.flightPanel.flightSearchButton_u) {
						//// textarea에 전체 출력
						ArrayList<AirLine> list = new ArrayList<AirLine>();
						try {
							list = aDAO.getAllALInfo();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("항공코드\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t\t일반석운임\t비즈니스석운임\n");
							for (AirLine p : list) {
								sb.append(p.getID() + "\t");
								sb.append(p.getAirLineNm() + "\t");
								sb.append(p.getArrAirportNm() + "\t");
								sb.append(p.getDepAirportNm() + "\t");
								sb.append(p.getArrPlandTime() + "\t");
								sb.append(p.getDepPlandTime() + "\t\t");
								sb.append(p.getEconomyCharge() + "\t");
								sb.append(p.getPrestigeCharge() + "\t\n");
							}
						}
						v.setTextArea(v.flightPanel.textArea_u, sb);
					}

					if (obj == v.flightPanel.flightSearchButton_d) {
						//// textarea에 전체 출력
						ArrayList<AirLine> list = new ArrayList<AirLine>();
						try {
							list = aDAO.getAllALInfo();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("항공코드\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t\t일반석운임\t비즈니스석운임\n");
							for (AirLine p : list) {
								sb.append(p.getID() + "\t");
								sb.append(p.getAirLineNm() + "\t");
								sb.append(p.getArrAirportNm() + "\t");
								sb.append(p.getDepAirportNm() + "\t");
								sb.append(p.getArrPlandTime() + "\t");
								sb.append(p.getDepPlandTime() + "\t\t");
								sb.append(p.getEconomyCharge() + "\t");
								sb.append(p.getPrestigeCharge() + "\t\n");
							}
						}
						v.setTextArea(v.flightPanel.textArea_d, sb);
					}

					if (obj == v.flightPanel.flightCreateButton) {

						AirLine info = new AirLine();
						info.setAirLineNm(v.flightPanel.fliCreatetextField[0].getText());
						info.setArrPlandTime(v.flightPanel.fliCreatetextField[1].getText());
						info.setDepPlandTime(v.flightPanel.fliCreatetextField[2].getText());
						info.setEconomyCharge(Integer.parseInt(v.flightPanel.fliCreatetextField[3].getText()));
						info.setPrestigeCharge(Integer.parseInt(v.flightPanel.fliCreatetextField[4].getText()));
						info.setArrAirportNm(v.flightPanel.fliCreatetextField[5].getText());
						info.setDepAirportNm(v.flightPanel.fliCreatetextField[6].getText());
						int result = aDAO.addALInfo(info);

						// result > 0 이면 항공기 추가 완료 다이얼로그 띄우기
						v.flightCreateDialog(result);

						for (int i = 0; i < 7; i++)
							v.flightPanel.fliCreatetextField[i].setText("");
					}

					if (obj == v.flightPanel.flightUpdateButton) {

						AirLine info = new AirLine();
						info.setID(Integer.parseInt(v.flightPanel.fliUpdatetextField[0].getText()));
						info.setArrPlandTime(v.flightPanel.fliUpdatetextField[1].getText());
						info.setDepPlandTime(v.flightPanel.fliUpdatetextField[2].getText());
						info.setEconomyCharge(Integer.parseInt(v.flightPanel.fliUpdatetextField[3].getText()));
						info.setPrestigeCharge(Integer.parseInt(v.flightPanel.fliUpdatetextField[4].getText()));

						int result = aDAO.updateALInfo(info);
						// result > 0 이면 항공기 변경 완료 다이얼로그 띄우기
						v.flightUpdateDialog(result);

						for (int i = 0; i < 5; i++)
							v.flightPanel.fliUpdatetextField[i].setText("");
					}

					if (obj == v.flightPanel.flightDeleteButton) {
						int result = aDAO.delALInfo(Integer.parseInt(v.flightPanel.fliDeletetextField.getText()));
						// result > 0 이면 항공기 삭제 완료 다이얼로그 띄우기
						v.flightDeleteDialog(result);

						v.flightPanel.fliDeletetextField.setText("");
					}

					if (obj == v.managerPanel.memSearchButton) {
						// 모든 회원정보 조회
						ArrayList<User> list = new ArrayList<User>();
						try {
							list = uDAO.getAll();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						StringBuffer sb = new StringBuffer("");
						// v.setTextArea(v.managerPanel.textArea_m, sb);

						if (list != null) {
							sb.append("아이디\t이름\t비밀번호\t이메일\t\t생일\t전화번호\n");
							for (User p : list) {
								sb.append(p.getID() + "\t");
								sb.append(p.getName() + "\t");
								sb.append(p.getPw() + "\t");
								sb.append(p.getEmail() + "\t");
								sb.append(p.getBirth() + "\t");
								sb.append(p.getPhone() + "\t\n");

							}
						}
						v.setTextArea(v.managerPanel.textArea_m, sb);

					}

					if (obj == v.managerPanel.memDeleteButton) {
						// 멤버 삭제
						int result = uDAO.deleteUser(v.managerPanel.memDeletetextField.getText());
						v.memberDeleteDialog(result);
					}

					if (obj == v.reservationPanel.backButton) {
						v.card.show(v.c, "managerMenu");
					}

					// 수정 필요.
					if (obj == v.reservationPanel.reservationSearchButton) {
						ArrayList<Reservation> list = new ArrayList<Reservation>();
						try {
							list = rDAO.getAll();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("예약코드\t회원ID\t항공ID\t좌석번호\n");
							for (Reservation p : list) {
								sb.append(p.getID() + "\t");
								sb.append(p.getUser() + "\t");
								sb.append(p.getInfo() + "\t");
								sb.append(p.getSeatNum() + "\t\n");
							}
						}
						v.setTextArea(v.reservationPanel.textArea_r, sb);
					}
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
//					public class StartUIPanel extends JPanel {
					Object obj = e.getSource();
					if (obj == v.startUIPanel.managerButton) {
						System.out.println("매니저");
						ManagerUIFrame mu = new ManagerUIFrame();
						MCT.setManagerC(mu);
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

					if (obj == v.loginUIpanel.loginButton[0]) {// 로그인 버튼
						v.userId = v.loginUIpanel.loginTextField[0].getText();
						String pw = v.loginUIpanel.loginTextField[1].getText();
						if (v.dao.getUser(v.userId) == null) {
							JOptionPane.showMessageDialog(null, "존재하지 않는 회원정보입니다.");
							return;
						} else {
							if (v.dao.getUser(v.userId).getPw().equals(pw)) {
								UF = new UserUIFrame(v.userId);
								MCT.setUserC(UF);
								v.loginUIFrameExit();
							} else {
								JOptionPane.showMessageDialog(null, "비밀번호를 틀렸습니다.");
								return;
							}

						}

					} else if (obj == v.loginUIpanel.loginButton[1]) {// 회원가입 버튼
						v.card.next(v.c);
					}

					else if (obj == v.loginUIpanel.backButton) {// 돌아가기 버튼
						v.card.show(v.c, "1");
					}

//					public void actionPerformed(ActionEvent e) {
//					class SignUpPanel extends JPanel implements ActionListener {

					if (obj == v.signUpPanel.signUpButton) {// 회원가입 버튼
						User user = new User();
						String userData[] = new String[6];

						for (int i = 0; i < 6; i++) {
							userData[i] = v.signUpPanel.textField[i].getText();
						}
						user.setUser(userData);

						if (!uDAO.newUser(user)) {
							JOptionPane.showMessageDialog(null, "사용불가능한 ID입니다.");
							return;
						}
						JOptionPane.showMessageDialog(null, "회원가입되었습니다!!");
						v.card.previous(v.c);
					} else if (obj == v.signUpPanel.backButton) {// 돌아가기 버튼
						v.card.show(v.c, "2");
					}

				}

			});

		}
	}

	public void setLoginC(LoginUIFrame ui) {
		LC = new LoginUIController(ui);
	}

	public void setUserC(UserUIFrame ui) {
		UC = new UserUIController(ui);
	}

	public void setManagerC(ManagerUIFrame ui) {
		MC = new ManagerUIController(ui);
	}

	public static void main(String[] args) {

		MainController Controller = new MainController();
		Controller.LF = new LoginUIFrame();
		Controller.setLoginC(Controller.LF);

	}

}
