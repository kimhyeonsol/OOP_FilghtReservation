package Controller;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
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
		private int _selectedAirLine;
		private boolean isChangeSeat;

		public void updateSelectedSeat(int selectedAirLine) throws SQLException {
			_selectedAirLine = selectedAirLine;
			ArrayList<Reservation> resList = rDAO.getReservationListByALInfo(selectedAirLine);
			System.out.println(selectedAirLine);
			v.selectSeatPanel.seatlist.clear();

			for (Reservation res : resList) {
				v.selectSeatPanel.seatButton[res.getSeatNum()].setBackground((new Color(255, 192, 0)));
				v.selectSeatPanel.seatButton[res.getSeatNum()].setFocusPainted(true);
//				v.selectSeatPanel.seatButton[res.getSeatNum()].setEnabled(false);
//				if(res.getUser().equals(_userID)) {
//					int i = res.getSeatNum();
//					String str = "";
//					if (i < 10) {
//						str += "A";
//					} else if (i < 20) {
//						str += "B";
//					} else if (i < 30) {
//						str += "C";
//					} else if (i < 40) {
//						str += "D";
//					}
//					v.selectSeatPanel.seatlist.add(i);
//					v.selectedSeatTextarea
//					.setText(v.selectedSeatTextarea.getText() + "  " + str + (i % 10 + 1));
//				}

			}
		}

		public void updateReservationList() {
			ArrayList<Reservation> res = null;
			User currentUser = uDAO.getUser(_userID);
			try {
				res = rDAO.getReservationListByUser(currentUser.getID());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			String reservationListStr = "예약 ID\t항공사 이름\t예약자 이름\t좌석\t출발 시간\t도착 시간\t출발 공항\t도착 공항\t비용\n";
			for (Reservation r : res) {
				AirLine outAL = aDAO.getALInfo(r.getInfo());
				User outU = uDAO.getUser(r.getUser());

				int i = r.getSeatNum();
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

				str += i % 10 + 1;
				int pee = i % 10 == 0 ? outAL.getPrestigeCharge() : outAL.getEconomyCharge();
				reservationListStr += r.getID() + "\t" + outAL.getAirLineNm() + "\t" + outU.getName() + "\t" + str
						+ "\t" + outAL.getDepPlandTime() + "\t" + outAL.getArrPlandTime() + "\t"
						+ outAL.getDepAirportNm() + "\t" + outAL.getArrAirportNm() + "\t" + pee + "\n";
			}

			v.myInfoPanel.myReservationUpdatePanel.textArea.setText(reservationListStr);
		}

		public UserUIController(UserUIFrame ui) {

			this.v = ui;
			String data[] = new String[6];
			isChangeSeat = false;

			v.addButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Object obj = e.getSource();
//					FlightResPanel pnFR = v.flightResPanel;
					User currentUser = uDAO.getUser(v._userId);
					_userID = currentUser.getID();

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
						updateReservationList();

					} else if (obj == v.userMenuPanel.flightResButton) {
						// 예약버튼
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

						updateReservationList();
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
							int num = Integer.parseInt(resnum);
							int selectReser_info;
							try {

								Reservation search = rDAO.getReservation(num);
								if (!search.getUser().equals(currentUser.getID())) {
									return;
								}
								selectReser_info = search.getInfo();
								rDAO.deleteReservation(search.getID());
//								v._selectedDepAirLine = selectReser_info;
								updateSelectedSeat(selectReser_info);
//								v.selectSeatPanel.seatlist.clear();
								isChangeSeat = true;
								v.card.show(v.c, "selectSeat");
								System.out.println(num);
							} catch (SQLException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}

						}
					} else if (obj == v.myInfoPanel.myReservationUpdatePanel.cancleResBtn) {// 예약취소하기 버튼
						String resnum = JOptionPane.showInputDialog("예약취소 할 항공편의 예약번호를 입력하세요");
						if (resnum != null) {
							////////// dB에서 해당 항공기 예약정보를 삭제
							Reservation output = null;
							try {
								output = rDAO.getReservation(Integer.parseInt(resnum));
							} catch (NumberFormatException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							} catch (SQLException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
								return;
							}
							// #### DAO ####
							rDAO.deleteReservation(Integer.parseInt(resnum));
							// #### DAO ####
							// reservationList update
							updateReservationList();
						}
					}

//					public FlightResPanel flightResPanel;
					else if (obj == v.flightResPanel.backButton) {// 뒤로가기 버튼
						v.card.show(v.c, "userMenu");
					}

//					public class RegisterFlightPanel extends JPanel implements ItemListener {
					if (obj == v.flightResPanel.flightSearchButton) {// 비행기 검색하기 버튼
						// #### DAO ####
						String departAirport = v.flightResPanel.departureAirportCombo.getSelectedItem().toString();// 출발
																													// 공항
						String destAirport = v.flightResPanel.destAirportCombo.getSelectedItem().toString();// 도착 공항
						String departDate = v.flightResPanel.flightsearchTextField[0].getText();// 가는 날
						String destDate = v.flightResPanel.flightsearchTextField[1].getText();// 오는 날
						
						if(departDate.equals("")||destDate.equals("")||v.flightResPanel.flightsearchTextField[2].getText().equals(""))
						{
							JOptionPane.showMessageDialog(null, "입력칸을 모두 채워주세요!");
							return;
						}
					
						int pNum = Integer.parseInt(v.flightResPanel.flightsearchTextField[2].getText());// 가는 인원
						// 왕복
						if (v.flightResPanel.radio[1].isSelected()) {

							ArrayList<AirLine> output = null;
							ArrayList<AirLine> output2 = null; // 돌아오는
							try {
								output = aDAO.getALInfoByChoice(departAirport, destAirport, departDate);
								output2 = aDAO.getALInfoByChoice(destAirport, departAirport, destDate);
								// results by current day/time
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								// 결과가 없음..? 비행기 안뜸.. or 너무 미래
								// 과거 시간 선택 검증
							}
							String depAirLineStr = "항공ID\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t이코니미운임\t비즈니스운임\n";
							for (AirLine r : output) {
								depAirLineStr += r.getID() + "\t" + r.getAirLineNm() + "\t" + r.getDepAirportNm() + "\t"
										+ r.getArrAirportNm() + "\t" + r.getDepPlandTime().substring(8, 10) + ":"
										+ r.getDepPlandTime().substring(10, 12) + "\t"
										+ r.getArrPlandTime().substring(8, 10) + ":"
										+ r.getArrPlandTime().substring(10, 12) + "\t" + r.getEconomyCharge() + "\t"
										+ r.getPrestigeCharge() + "\n";
							}
							String desAirLineStr = "항공ID\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t이코니미운임\t비즈니스운임\n";
							for (AirLine r : output2) {
								desAirLineStr += r.getID() + "\t" + r.getAirLineNm() + "\t" + r.getDepAirportNm() + "\t"
										+ r.getArrAirportNm() + "\t" + r.getDepPlandTime().substring(8, 10) + ":"
										+ r.getDepPlandTime().substring(10, 12) + "\t"
										+ r.getArrPlandTime().substring(8, 10) + ":"
										+ r.getArrPlandTime().substring(10, 12) + "\t" + r.getEconomyCharge() + "\t"
										+ r.getPrestigeCharge() + "\n";
							}
							if (output.size() == 0)
								v.flightResPanel.departureAirportTextArea.setText("검색 결과가 없습니다.");
							else
								v.flightResPanel.departureAirportTextArea.setText(depAirLineStr);

							if (output2.size() == 0)
								v.flightResPanel.destAirportTextArea.setText("검색 결과가 없습니다.");
							else
								v.flightResPanel.destAirportTextArea.setText(desAirLineStr);

							// output -> 항공기 textArea(scroll)1 반영 및 refresh
						} else if (v.flightResPanel.radio[0].isSelected()) {
							// 편도
							// String departDate = v.flightResPanel.flightsearchTextField[0].getText();
							ArrayList<AirLine> output = null;
							try {
								output = aDAO.getALInfoByChoice(departAirport, destAirport, departDate);
								// results by current day/time
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								// 결과가 없음..? 비행기 안뜸.. or 너무 미래
								// 과거 시간 선택 검증
							}

							String depAirLineStr = "항공ID\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t이코니미운임\t비즈니스운임\n";
							for (AirLine r : output) {
								depAirLineStr += r.getID() + "\t" + r.getAirLineNm() + "\t" + r.getDepAirportNm() + "\t"
										+ r.getArrAirportNm() + "\t" + r.getDepPlandTime().substring(8, 10) + ":"
										+ r.getDepPlandTime().substring(10, 12) + "\t"
										+ r.getArrPlandTime().substring(8, 10) + ":"
										+ r.getArrPlandTime().substring(10, 12) + "\t" + r.getEconomyCharge() + "\t"
										+ r.getPrestigeCharge() + "\n";
							}

							if (output.size() == 0)
								v.flightResPanel.departureAirportTextArea.setText("검색 결과가 없습니다.");
							else
								v.flightResPanel.departureAirportTextArea.setText(depAirLineStr);

//							v.flightResPanel.departureAirportTextArea.setText(depAirLineStr);
							v.flightResPanel.destAirportTextArea.setText("");

							// output -> 항공기 textArea(scroll)1 반영 및 refresh
						}

						// #### DAO ####
					}

					if (obj == v.flightResPanel.selectSeatButton1) {// 가는자리선택하기 버튼
						String[] arr= {};
						int flag=0;
						if (v.flightResPanel.flightsearchTextField[2].getText().equals("")) {// 인원수 textfield 비어있으면
							JOptionPane.showMessageDialog(null, "탑승할 인원을 선택하세요!");
							return;
						}
						try {
							AirLine selectedAirLine = new AirLine();
							StringReader result=new StringReader(v.flightResPanel.departureAirportTextArea.getText());
							BufferedReader br = new BufferedReader(result);
							String line="";
							while ( ( line = br.readLine() ) != null ){
								arr = line.split("\t");
							}
							for(int i=0; i<arr.length; i++) {
								if(arr[i].equals(Integer.parseInt(v.flightResPanel.selectedFlightIDTextField1.getText()))) {
									selectedAirLine = aDAO
											.getALInfo(Integer.parseInt(v.flightResPanel.selectedFlightIDTextField1.getText()));
									flag=1;
									break;
								}

							}
							if(flag==0) // 검색한 항목에 없는 항공id 입력한 경우
							{
								JOptionPane.showMessageDialog(null, "위 결과에 있는 항공권 ID를 입력하세요!");
								return;
							}

							if (selectedAirLine == null) {
								return;
							}

							// updateSeat

							updateSelectedSeat(selectedAirLine.getID());

							v.resNum = Integer.valueOf(v.flightResPanel.flightsearchTextField[2].getText());
							isChangeSeat = false;
							v.card.show(v.c, "selectSeat");

						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}

					if (obj == v.flightResPanel.selectSeatButton2) {// 오는자리선택하기 버튼
						String[] arr= {};
						int flag=0;
						if (v.flightResPanel.flightsearchTextField[2].getText().equals("")) {// 인원수 textfield 비어있으면
							JOptionPane.showMessageDialog(null, "탑승할 인원을 선택하세요!");
							return;
						}

						try {
							AirLine selectedAirLine = new AirLine();
							StringReader result=new StringReader(v.flightResPanel.destAirportTextArea.getText());
							BufferedReader br = new BufferedReader(result);
							String line="";
							while ( ( line = br.readLine() ) != null ){
								arr = line.split("\t");
							}
							for(int i=0; i<arr.length; i++) {
								if(arr[i].equals(Integer.parseInt(v.flightResPanel.selectedFlightIDTextField2.getText()))) {
									selectedAirLine = aDAO
											.getALInfo(Integer.parseInt(v.flightResPanel.selectedFlightIDTextField2.getText()));
									flag=1;
									break;
								}

							}
							if(flag==0) // 검색한 항목에 없는 항공id 입력한 경우
							{
								JOptionPane.showMessageDialog(null, "위 결과에 있는 항공권 ID를 입력하세요!");
								return;
							}
//							selectedAirLine = aDAO
//									.getALInfo(Integer.parseInt(v.flightResPanel.selectedFlightIDTextField2.getText()));
							if (selectedAirLine == null) {
								return;
							}

							updateSelectedSeat(selectedAirLine.getID());
							v.resNum = Integer.valueOf(v.flightResPanel.flightsearchTextField[2].getText());
							isChangeSeat = false;
							v.card.show(v.c, "selectSeat");

						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}

					if (obj == v.flightResPanel.departureAirportCombo) {// 출발 공항 콤보박스
						int index = v.flightResPanel.departureAirportCombo.getSelectedIndex();
					} else if (obj == v.flightResPanel.destAirportCombo) {// 도착 공항 콤보박스
						int index = v.flightResPanel.destAirportCombo.getSelectedIndex();
					}

//					public SelectSeatPanel selectSeatPanel;
					else if (obj == v.selectSeatPanel.backButton) {// 뒤로가기 버튼
						updateReservationList();
						if (isChangeSeat) {
							v.card.show(v.c, "myInfo");
						} else {
							v.card.show(v.c, "reservation");

						}
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
						if (v.selectSeatPanel.seatlist.size() != v.resNum) {
							JOptionPane.showMessageDialog(null, v.resNum + "명을 선택해주세요!");
							return;
						}

						for (int i = 0; i < v.selectSeatPanel.seatlist.size(); i++) {
							int seatNum = v.selectSeatPanel.seatlist.get(i);

							Reservation r = new Reservation();
							r.setInfo(_selectedAirLine);
							r.setSeatNum(seatNum);
							r.setUser(currentUser.getID());
							System.out.println(r.getInfo());
							System.out.println(r.getSeatNum());
							System.out.println(r.getUser());

							try {
								rDAO.getReservationByInfoWithSeat(_selectedAirLine, seatNum);
								JOptionPane.showMessageDialog(null, "이미 예약된 좌석: " + seatNum);
							} catch (HeadlessException | SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								rDAO.newReservation(r);
								JOptionPane.showMessageDialog(null, "예약되셨습니다!");

								if (isChangeSeat) {
									v.card.show(v.c, "myInfo");
								} else {
									v.card.show(v.c, "reservation");

								}

							}

						}

						// 항공기 예약 입력 초기화??
						v.selectSeatPanel.cnt = 0;
						v.selectSeatPanel.seatlist.clear();
						v.selectedSeatTextarea.setText("");
//						for (int i = 0; i < 40; i++) {// 일단 좌석 색깔 다 초기화 시킴, 예약된 좌석은 라벨 x로 바꾸고, 선택 안되게 설정해주어야함.
//							if (i % 10 == 0) {
//								v.selectSeatPanel.seatButton[i].setBackground(new Color(112, 48, 160));
//							} else {
//								v.selectSeatPanel.seatButton[i].setBackground(new Color(46, 117, 182));
//							}
//						}
						updateReservationList();
						return;// 밑에 for문이니깐 예약하기 버튼이면 걍 return 시켜주기
					} else if (obj == v.selectSeatPanel.selectSeatTab.resetButton) {// 좌석초기화 버튼
						for (int i = 0; i < v.selectSeatPanel.seatButton.length; i++) {

							if (i % 10 == 0) {
								v.selectSeatPanel.seatButton[i].setBackground(new Color(112, 48, 160));
							} else {
								v.selectSeatPanel.seatButton[i].setBackground(new Color(46, 117, 182));
							}

						}
						v.selectSeatPanel.cnt = 0;
						v.selectSeatPanel.seatlist.clear();
						v.selectedSeatTextarea.setText("");

						try {
							updateSelectedSeat(_selectedAirLine);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					for (int i = 0; i < v.selectSeatPanel.seatButton.length; i++) {

						if (obj == v.selectSeatPanel.seatButton[i]) {
							if (v.selectSeatPanel.seatButton[i].getBackground().equals(new Color(255, 192, 0))) {
								return;
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
							break;
						}

					}

				}

			});

		}
	}

	// 관리자 화면
	class ManagerUIController {
		private final ManagerUIFrame v;

		public ManagerUIController(ManagerUIFrame ui) {

			this.v = ui;

			v.addButtonActionListener(new ActionListener() {
				public void print() {
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
							sb.append(p.getDepAirportNm() + "\t");
							sb.append(p.getArrAirportNm() + "\t");
							sb.append(p.getDepPlandTime() + "\t");
							sb.append(p.getArrPlandTime() + "\t\t");
							sb.append(p.getEconomyCharge() + "\t");
							sb.append(p.getPrestigeCharge() + "\t\n");
						}
					}
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Object obj = e.getSource();
					if (obj == v.managerMenuPanel.backButton) {
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
								sb.append(p.getDepAirportNm() + "\t");
								sb.append(p.getArrAirportNm() + "\t");
								sb.append(p.getDepPlandTime() + "\t");
								sb.append(p.getArrPlandTime() + "\t\t");
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
								sb.append(p.getDepAirportNm() + "\t");
								sb.append(p.getArrAirportNm() + "\t");
								sb.append(p.getDepPlandTime() + "\t");
								sb.append(p.getArrPlandTime() + "\t\t");
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
								sb.append(p.getDepAirportNm() + "\t");
								sb.append(p.getArrAirportNm() + "\t");
								sb.append(p.getDepPlandTime() + "\t");
								sb.append(p.getArrPlandTime() + "\t\t");
								sb.append(p.getEconomyCharge() + "\t");
								sb.append(p.getPrestigeCharge() + "\t\n");
							}
						}
						v.setTextArea(v.flightPanel.textArea_d, sb);
					}

					if (obj == v.flightPanel.flightCreateButton) {
						AirLine info = new AirLine();
						if (!isStringDouble(v.flightPanel.fliCreatetextField[3].getText())
								|| !isStringDouble(v.flightPanel.fliCreatetextField[4].getText())) {
							//운임은 숫자로만 입력해주세요! dialog 띄우기
							JOptionPane.showMessageDialog(null, "운임은 숫자로만 입력해주세요.");
							return;
						}
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
						
						//textArea update
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
								sb.append(p.getDepAirportNm() + "\t");
								sb.append(p.getArrAirportNm() + "\t");
								sb.append(p.getDepPlandTime() + "\t");
								sb.append(p.getArrPlandTime() + "\t\t");
								sb.append(p.getEconomyCharge() + "\t");
								sb.append(p.getPrestigeCharge() + "\t\n");
							}
						}
						v.setTextArea(v.flightPanel.textArea_c, sb);
						
					}

					if (obj == v.flightPanel.flightUpdateButton) {

						AirLine info = new AirLine();
						if (!isStringDouble(v.flightPanel.fliUpdatetextField[3].getText())
								|| !isStringDouble(v.flightPanel.fliUpdatetextField[4].getText())) {
							//운임은 숫자로만 입력해주세요! dialog 띄우기
							JOptionPane.showMessageDialog(null, "운임은 숫자로만 입력해주세요.");
							return;
						}
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
						
						//textArea update
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
								sb.append(p.getDepAirportNm() + "\t");
								sb.append(p.getArrAirportNm() + "\t");
								sb.append(p.getDepPlandTime() + "\t");
								sb.append(p.getArrPlandTime() + "\t\t");
								sb.append(p.getEconomyCharge() + "\t");
								sb.append(p.getPrestigeCharge() + "\t\n");
							}
						}
						v.setTextArea(v.flightPanel.textArea_u, sb);
						
						
					}

					if (obj == v.flightPanel.flightDeleteButton) {
						int result = aDAO.delALInfo(Integer.parseInt(v.flightPanel.fliDeletetextField.getText()));
						// result > 0 이면 항공기 삭제 완료 다이얼로그 띄우기
						v.flightDeleteDialog(result);

						v.flightPanel.fliDeletetextField.setText("");
						
						//textArea update
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
								sb.append(p.getDepAirportNm() + "\t");
								sb.append(p.getArrAirportNm() + "\t");
								sb.append(p.getDepPlandTime() + "\t");
								sb.append(p.getArrPlandTime() + "\t\t");
								sb.append(p.getEconomyCharge() + "\t");
								sb.append(p.getPrestigeCharge() + "\t\n");
							}
						}
						v.setTextArea(v.flightPanel.textArea_d, sb);
						
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
						
						//바뀐정보 업데이트하기
						ArrayList<User> list = new ArrayList<User>();
						StringBuffer sb = new StringBuffer("");
						try {
							list = uDAO.getAll();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
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
						v.managerPanel.textArea_m.setText(sb.toString());
						
						
					}

					if (obj == v.reservationPanel.backButton) {
						v.card.show(v.c, "managerMenu");
					}

					if (obj == v.reservationPanel.reservationSearchButton) {
						ArrayList<Reservation> list = new ArrayList<Reservation>();
						try {
							list = rDAO.getAll();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

//						StringBuffer sb = new StringBuffer();
						String reservationListStr = "예약 ID\t항공사 이름\t예약자 이름\t좌석\t출발 시간\t도착 시간\t출발 공항\t도착 공항\t비용\n";
//						if (list != null) {
//							sb.append("예약코드\t회원ID\t항공ID\t좌석번호\n");
//							for (Reservation p : list) {
//								sb.append(p.getID() + "\t");
//								sb.append(p.getUser() + "\t");
//								sb.append(p.getInfo() + "\t");
//								sb.append(p.getSeatNum() + "\t\n");
//							}
//						}
//						v.setTextArea(v.reservationPanel.textArea_r, sb);

						for (Reservation r : list) {
							AirLine outAL = aDAO.getALInfo(r.getInfo());
							User outU = uDAO.getUser(r.getUser());

							int i = r.getSeatNum();
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

							str += i % 10 + 1;
							int pee = i % 10 == 0 ? outAL.getPrestigeCharge() : outAL.getEconomyCharge();
							reservationListStr += r.getID() + "\t" + outAL.getAirLineNm() + "\t" + outU.getName() + "\t"
									+ str + "\t" + outAL.getDepPlandTime() + "\t" + outAL.getArrPlandTime() + "\t"
									+ outAL.getDepAirportNm() + "\t" + outAL.getArrAirportNm() + "\t" + pee + "\n";

						}

						v.reservationPanel.textArea_r.setText(reservationListStr);

						//
					}
				}

				public boolean isStringDouble(String s) {
					try {
						// System.out.print(s+"는 : ");
						Double.parseDouble(s);
						return true;
					} catch (NumberFormatException e) {
						return false;
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