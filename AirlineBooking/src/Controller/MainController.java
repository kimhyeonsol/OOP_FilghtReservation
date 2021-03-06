package Controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.gson.Gson;
//import com.mysql.cj.x.protobuf.MysqlxNotice.Warning.Level;

import Model.AirLine;
import Model.AirLineDAO;
import Model.AirPortParkingLot;
import Model.AirPortParkingLotDAO;
import Model.AirPortParkingLotParser;
import Model.InCheonAirPortParkingLot;
import Model.Reservation;
import Model.ReservationDAO;
import Model.User;
import Model.UserDAO;
import View.AirPortParkingLotUIFrame;
import View.LoginUIFrame;
import View.ManagerUIFrame;
import View.UserUIFrame;
import message.Message;

public class MainController {
	MainController MCT;
	UserUIController UC;
	ManagerUIController MC;
	LoginUIController LC;
	AirPortParkingLotUIController PC;

	AirLineDAO aDAO = new AirLineDAO();
	ReservationDAO rDAO = new ReservationDAO();
	UserDAO uDAO;
	AirPortParkingLotDAO pDAO = new AirPortParkingLotDAO();

	int seatNum;
	
	public MainController() {
		MCT = this;
		uDAO = new UserDAO();
		// 로거 객체 초기화
	}

	UserUIFrame UF;
	ManagerUIFrame MF;
	LoginUIFrame LF;
	AirPortParkingLotUIFrame PF;

	class UserUIController extends Thread {
		private final UserUIFrame v;
		private String _userID;
		private int _selectedAirLine;
		private boolean isChangeSeat;
		String data[] = new String[6];
		int selectReser_info;
		
		Thread thread;
		Gson gson = new Gson();
		Socket socket;
		String ip = "127.0.0.1";
		boolean status;
		Message m;
		LinkedList<String> array = new LinkedList<String>();

		// 입출력 스트림
		private BufferedReader inMsg = null;
		private PrintWriter outMsg = null;

		private ArrayList<PrintWriter> outMsgs = new ArrayList<PrintWriter>();

		public void updateSelectedSeat(int selectedAirLine) { // 좌석 선택 패널의 예약된 좌석 갱신 함수
			_selectedAirLine = selectedAirLine;
			ArrayList<Reservation> resList = null;
			try {
				resList = rDAO.getReservationListByALInfo(selectedAirLine);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("DB 오류!");
			}
			System.out.println(selectedAirLine); // 현재 선택된 항공편 ID 출력
			v.selectSeatPanel.seatlist.clear(); // 이전 선택한 좌석 리스트 초기화

			for (int i = 0; i < 40; i++) {
				if (i % 10 == 0) {
					v.selectSeatPanel.seatButton[i].setBackground(new Color(112, 48, 160)); // 비즈니스 좌석 0, 10, 20, 30 
				} else {
					v.selectSeatPanel.seatButton[i].setBackground(new Color(46, 117, 182)); // 그 외 일반 좌석
				}
			}
			for (Reservation res : resList) { // 해당 항공편의 예약을 모두 표기 -> 색 변환
				v.selectSeatPanel.seatButton[res.getSeatNum()].setBackground((new Color(255, 192, 0)));
				v.selectSeatPanel.seatButton[res.getSeatNum()].setFocusPainted(true);
			}
		}

		public void updateReservationList() { // 예약 변경(추가, 좌석 변경)에 따른 예약 리스트 갱신 함수
			ArrayList<Reservation> res = null;
			User currentUser = uDAO.getUser(_userID); // 현재 로그인 유저의
			try { 
				res = rDAO.getReservationListByUser(currentUser.getID()); // 모든 예약 리스트를 받고
			} catch (SQLException e1) {
//				e1.printStackTrace();
				System.out.println("DB 오류!");
			}

			String reservationListStr = "예약 ID\t항공사 이름\t예약자 이름\t좌석\t출발 시간\t도착 시간\t출발 공항\t도착 공항\t비용\n";
			for (Reservation r : res) { // List에 추가한다.
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

			v.myInfoPanel.myReservationUpdatePanel.textArea.setText(reservationListStr); // reservationList에 세팅
			v.myInfoPanel.myReservationUpdatePanel.repaint();
		}

		public UserUIController(UserUIFrame ui) {
			this.v = ui;
			
			isChangeSeat = false; // ChangeSeat가 true인 경우, 좌석 선택 패널에서 현재 예약 정보 패널 카드로 돌아간다.

			connectServer();
//         connectServer(); Thread 동기화 테스트
//         connectServer();

			v.addExitWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) { // 창을 닫을 시 서버에 로그아웃 메세지 전달
					int confirm = JOptionPane.showOptionDialog(null, "Are You Sure to Close Application?",
							"Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
							null);
					LinkedList<String> strArray = new LinkedList<String>();
					outMsg.println(gson.toJson(new Message(v._userId, "", strArray, "logout")));
					if (confirm == 0) {
						System.exit(0);
					}
				}
			});
			v.addButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Object obj = e.getSource();
					User currentUser = uDAO.getUser(v._userId);
					_userID = currentUser.getID();

					if (obj == v.userMenuPanel.myInfoButton) {//내정보 검색하기 버튼
						v.card.show(v.c,"myInfo");
						LinkedList<String> strArray = new LinkedList<String>();
						strArray.add(v._userId);
						outMsg.println(gson.toJson(new Message(v._userId, "", strArray, "MyInfo")));
					} 
					
					else if (obj == v.userMenuPanel.flightResButton) {// 예약버튼
						v.card.show(v.c, "reservation");
					} 
					
					else if (obj == v.userMenuPanel.backButton) {//사용자 메뉴페이지 뒤로가기 버튼
						LF = new LoginUIFrame();
						MCT.setLoginC(LF);
						LinkedList<String> strArray = new LinkedList<String>();
						outMsg.println(gson.toJson(new Message(v._userId, "", strArray, "logout")));
						v.userMenuExit();  // view에서 dispose 호출
					}

					else if (obj == v.myInfoPanel.backButton) {// 뒤로가기버튼
						v.card.show(v.c, "userMenu"); // 내 정보 -> 유저 메뉴
					}

					else if (obj == v.myInfoPanel.myInfoUpdatePanel.saveButton) {// 내정보 문서로 저장하기 버튼
						int ret = v.myInfoPanel.myInfoUpdatePanel.chooser.showSaveDialog(null);
						if (ret != JFileChooser.APPROVE_OPTION) {
							v.myInfoSaveDialog();
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
					}

					else if (obj == v.myInfoPanel.myInfoUpdatePanel.updateButton) {// 내정보 변경하기 버튼
						LinkedList<String> strArray = new LinkedList<String>();
						for(int i=0;i<data.length;i++) {
							strArray.add(data[i]);
						}
						outMsg.println(gson.toJson(new Message(v._userId, "", strArray, "MyInfoChange")));
					} 
					
					else if (obj == v.myInfoPanel.myInfoUpdatePanel.cancelButton) {// 프로그램 탈퇴하기 버튼
						LinkedList<String> strArray = new LinkedList<String>();//내정보 탈퇴하기 메시지 송신
						for(int i=0;i<data.length;i++) {
							strArray.add(data[i]);
						}
						outMsg.println(gson.toJson(new Message(v._userId, "", strArray, "MyInfoDelete")));
						
						MCT.LF = new LoginUIFrame();
						MCT.setLoginC(MCT.LF);
						
						strArray = new LinkedList<String>();//로그아웃 메시지 송신
						outMsg.println(gson.toJson(new Message(v._userId, "", strArray, "logout")));
						v.userMenuExit();
					}
					
					else if (obj == v.myInfoPanel.myReservationUpdatePanel.changeSeatBtn) {// 자리변경하기 버튼
						String resnum = v.myReservationChangeSeatDialog();
						v.resNum = 1;
						
						// 텍스트 필드에 아무것도 입력되지 않았을 때
						if(resnum == null)
						{
							v.myReservationTextFieldDialog();
							return;
						}
						
						int flag=0;
						
						// textArea에 출력된 예약id들을 arr에 저장하고, 사용자가 입력한 값이 포함되어 있는지 확인
						try {
							StringReader result = new StringReader(v.myInfoPanel.myReservationUpdatePanel.textArea.getText());
							BufferedReader br = new BufferedReader(result);
							List<String> data = new ArrayList<String>();
							while (true) {
								String line = br.readLine();
								if (line == null)
									break;
								data.add(line);
							}
							
							ListIterator<String> iterator = data.listIterator();
							while (iterator.hasNext()) {
								String line = iterator.next();
								String[] arr = line.split("\t", 2);
								if (arr[0].equals(resnum)) {
									flag = 1;
									break;
								}
							}
							if (flag == 0) { // 검색한 항목에 없는 예약 id 입력한 경우
								v.myReservationIDDialog();
								return;
							}
						}catch (IOException e1) {
							e1.printStackTrace();
						}
						
						if (resnum != null) {
							int num = Integer.parseInt(resnum);
							LinkedList<String> strArray = new LinkedList<String>();//로그아웃 메시지 송신
								strArray.add(resnum);
								strArray.add(currentUser.getID());
								outMsg.println(gson.toJson(new Message(v._userId, "", strArray, "MySeatChange")));
						}
					} 
					
					else if (obj == v.myInfoPanel.myReservationUpdatePanel.cancleResBtn) {// 예약취소하기 버튼
						String resnum = v.myReservationCancleResDialog();
						// 텍스트 필드에 아무것도 입력되지 않았을 때
						if(resnum.equals(""))
						{
							v.myReservationTextFieldDialog();
							return;
						}
						
						
						int flag=0;
						// textArea에 출력된 예약id들을 arr에 저장하고, 사용자가 입력한 값이 포함되어 있는지 확인
						try {
							StringReader result = new StringReader(v.myInfoPanel.myReservationUpdatePanel.textArea.getText());
							BufferedReader br = new BufferedReader(result);
							List<String> data = new ArrayList<String>();
							while (true) {
								String line = br.readLine();
								if (line == null)
									break;
								data.add(line);
							}
							
							ListIterator<String> iterator = data.listIterator();
							while (iterator.hasNext()) {
								String line = iterator.next();
								String[] arr = line.split("\t", 2);
								if (arr[0].equals(resnum)) {
									flag = 1;
									break;
								}
							}
							if (flag == 0) { // 검색한 항목에 없는 예약 id 입력한 경우
								v.myReservationIDDialog();
								return;
							}
						}catch (IOException e1) {
							e1.printStackTrace();
						}
						
						if (resnum != null) {
							////////// dB에서 해당 항공기 예약정보를 삭제
							Reservation output = null;
							LinkedList<String> strArray = new LinkedList<String>();
							strArray.add(resnum);
							outMsg.println(gson.toJson(new Message(v._userId, "", strArray, "cancel")));
						}
					}

					else if (obj == v.flightResPanel.backButton) {// 뒤로가기 버튼
						v.card.show(v.c, "userMenu");
					}

					if (obj == v.flightResPanel.flightSearchButton) {// 비행기 검색하기 버튼
						// #### DAO ####
						String departAirport = v.flightResPanel.departureAirportCombo.getSelectedItem().toString();// 출발
						// 공항
						String destAirport = v.flightResPanel.destAirportCombo.getSelectedItem().toString();// 도착 공항
						String departDate = v.flightResPanel.flightsearchTextField[0].getText();// 가는 날
						String destDate = v.flightResPanel.flightsearchTextField[1].getText();// 오는 날

						if (!v.flightResPanel.radio[1].isSelected() && !v.flightResPanel.radio[0].isSelected()) {
							v.flightResRadioButtonDialog();
							return;
						}
						int pNum; // 왕복 편도 if 문에서 값 저장함

						// 왕복
						if (v.flightResPanel.radio[1].isSelected()) {

							// 입력 칸 모두 채웠는지 검사
							if (departDate.equals("") || destDate.equals("")
									|| v.flightResPanel.flightsearchTextField[2].getText().equals("")) {
								v.flightResTextFieldDialog();
								return;
							}
							// 인원은 숫자로만 입력받아야 하기 때문에 검사
							if (!isStringDouble(v.flightResPanel.flightsearchTextField[2].getText())) {
								v.flightResPersonnelDialog();
								return;
							}

							pNum = Integer.parseInt(v.flightResPanel.flightsearchTextField[2].getText());// 가는 인원

								LinkedList<String> strArray = new LinkedList<String>();
								strArray.add(departAirport);
								strArray.add(destAirport);
								strArray.add(departDate);
								strArray.add(destDate);
								outMsg.println(gson.toJson(new Message(v._userId, "", strArray, "AirLineInfo_roundtrip")));
						
							// output -> 항공기 textArea(scroll)1 반영 및 refresh
						} 
						else if (v.flightResPanel.radio[0].isSelected()) {
							// 편도
							// 입력칸 검사
							if (departDate.equals("")
									|| v.flightResPanel.flightsearchTextField[2].getText().equals("")) {
								v.flightResTextFieldDialog();
								return;
							}
							// 인원은 숫자로만 입력받아야 하기 때문에 검사
							if (!isStringDouble(v.flightResPanel.flightsearchTextField[2].getText())) {

								v.flightResPersonnelDialog();
								return;
							}

							pNum = Integer.parseInt(v.flightResPanel.flightsearchTextField[2].getText());// 가는 인원

							
								LinkedList<String> strArray = new LinkedList<String>();
								strArray.add(departAirport);
								strArray.add(destAirport);
								strArray.add(departDate);
								
								outMsg.println(gson.toJson(new Message(v._userId, "", strArray, "AirLineInfo_oneway")));
						}
					}
					if (obj == v.flightResPanel.selectSeatButton1) {// 가는자리선택하기 버튼
						int flag = 0;
						if (v.flightResPanel.flightsearchTextField[2].getText().equals("")) {// 인원수 textfield 비어있으면
							v.flightResTextFieldDialog();
							return;
						}
						if (!isStringDouble(v.flightResPanel.selectedFlightIDTextField1.getText())) {
							// 운임은 숫자로만 입력해주세요! dialog 띄우기
							v.flightResIDNumDialog();
							return;
						}
						try {
							AirLine selectedAirLine = new AirLine();
							StringReader result = new StringReader(v.flightResPanel.departureAirportTextArea.getText());
							BufferedReader br = new BufferedReader(result);
							List<String> data = new ArrayList<String>();
							while (true) {
								String line = br.readLine();
								if (line == null)
									break;
								data.add(line);
							}
							ListIterator<String> iterator = data.listIterator();
							while (iterator.hasNext()) {
								String line = iterator.next();
								String[] arr = line.split("\t", 2);
								if (arr[0].equals(v.flightResPanel.selectedFlightIDTextField1.getText())) {
									selectedAirLine = aDAO.getALInfo(
											Integer.parseInt(v.flightResPanel.selectedFlightIDTextField1.getText()));
									flag = 1;
									break;
								}
							}
							if (flag == 0) { // 검색한 항목에 없는 항공id 입력한 경우
								v.flightResIDDialog();
								return;
							}
							if (selectedAirLine == null) {
								return;
							}
							updateSelectedSeat(selectedAirLine.getID());

							v.resNum = Integer.valueOf(v.flightResPanel.flightsearchTextField[2].getText());
							isChangeSeat = false;
							v.card.show(v.c, "selectSeat");

						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

					if (obj == v.flightResPanel.selectSeatButton2) {// 오는자리선택하기 버튼
						int flag = 0;
						if (v.flightResPanel.flightsearchTextField[2].getText().equals("")) {// 인원수 textfield 비어있으면
							v.flightResTextFieldDialog();
							return;
						}
						if (!isStringDouble(v.flightResPanel.selectedFlightIDTextField2.getText())) {
							// 운임은 숫자로만 입력해주세요! dialog 띄우기
							v.flightResIDNumDialog();
							return;
						}

						try {
							AirLine selectedAirLine = new AirLine();
							StringReader result = new StringReader(v.flightResPanel.destAirportTextArea.getText());
							BufferedReader br = new BufferedReader(result);
							List<String> data = new ArrayList<String>();
							while (true) {
								String line = br.readLine();
								if (line == null)
									break;

								data.add(line);
							}

							ListIterator<String> iterator = data.listIterator();
							while (iterator.hasNext()) {
								String line = iterator.next();
								String[] arr = line.split("\t", 2);
								if (arr[0].equals(v.flightResPanel.selectedFlightIDTextField2.getText())) {
									selectedAirLine = aDAO.getALInfo(
											Integer.parseInt(v.flightResPanel.selectedFlightIDTextField2.getText()));
									flag = 1;
									break;
								}
							}

							if (flag == 0) { // 검색한 항목에 없는 항공id 입력한 경우
								v.flightResIDDialog();
								return;
							}
							if (selectedAirLine == null) {
								return;
							}
							updateSelectedSeat(selectedAirLine.getID());
							v.resNum = Integer.valueOf(v.flightResPanel.flightsearchTextField[2].getText());
							isChangeSeat = false;
							v.card.show(v.c, "selectSeat");

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


					else if (obj == v.selectSeatPanel.backButton) {// 뒤로가기 버튼
						updateReservationList();
						if (v.resNum == v.selectSeatPanel.seatlist.size()) {
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
						} else {
							v.selectSeatDialog();
						}
					}

					else if (obj == v.selectSeatPanel.reserveButton) {// 예약하기버튼
						if (v.selectSeatPanel.seatlist.size() != v.resNum) {
							v.selectSeatDialog();
							return;
						}
						for (int i = 0; i < v.resNum; i++) {
							seatNum = v.selectSeatPanel.seatlist.get(i);
//							System.out.println("**" + v.resNum);
							LinkedList<String> strArray = new LinkedList<String>();
							strArray.add(Integer.toString(_selectedAirLine));
							strArray.add(Integer.toString(seatNum));
							strArray.add(currentUser.getID());
							strArray.add(Boolean.toString(isChangeSeat));
							outMsg.println(gson.toJson(new Message(v._userId, "", strArray, "reservation")));
						}	
						v.selectSeatPanel.cnt = 0;
						v.selectSeatPanel.seatlist.clear();
						v.selectedSeatTextarea.setText("");

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
						v.selectSeatPanel.cnt = 0; // 초기화 
						v.selectSeatPanel.seatlist.clear();
						v.selectedSeatTextarea.setText("");

						updateSelectedSeat(_selectedAirLine);
					} else if (obj == v.myInfoPanel.searchAirPortParkingLot) {// 주차장 조회 버튼
						PF = new AirPortParkingLotUIFrame();
						setParkingC(PF);

					}

					for (int i = 0; i < v.selectSeatPanel.seatButton.length; i++) {

						if (obj == v.selectSeatPanel.seatButton[i]) {
							if (v.selectSeatPanel.seatButton[i].getBackground().equals(new Color(255, 192, 0))) {
								return;
							}

							v.selectSeatPanel.seatlist.add(i);
							System.out.println(i); // 선택한 좌석의 index 0~39
//							System.out.println(v.selectSeatPanel.cnt);
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

		public void connectServer() {
			try {
				// 소켓 생성
				socket = new Socket(ip, 9000);

				// logger.log(Level.INFO, "[Client]Server 연결 성공!!");

				// 입출력 스트림 생성
				outMsg = new PrintWriter(socket.getOutputStream(), true);
				inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            outMsgs.add(outMsg);
				// 서버에 로그인 메시지 전달
				m = new Message(v._userId, "", array, "login");
				outMsg.println(gson.toJson(m));

				// 메시지 수신을 위한 스레드 생성
				thread = new Thread(this);
				thread.start();

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			// 수신 메시지를 처리하는 데 필요한 변수 선언
			String msg = null;
			status = true;

			while (status) {
				try {
					// 메시지 수신 및 파싱
					msg = inMsg.readLine();

				} catch (IOException e) {
					// logger.log(Level.WARNING, "[MultiChatUI]메시지 스트림 종료!!");
				}

				// JSON 메시지를 Message 객체로 매핑
				m = gson.fromJson(msg, Message.class);
//            System.out.println("데이터 수신");
				if (m.getType().equals("reservationMessage")) {
					System.out.println("데이터:" + m.getMsg().get(0));
					if (m.getMsg().get(0).equals("false")) {
						v.reservedSeatDialog(seatNum);
					} else {

						int i = Integer.parseInt(m.getMsg().get(1));
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
						v.selectSeatResDialog(str, i);

						updateSelectedSeat(_selectedAirLine);
						updateReservationList();
						if (isChangeSeat) {
							v.card.show(v.c, "myInfo");
						} else {
							v.card.show(v.c, "reservation");
						}
					}
				} 
				// 예약 취소 메시지일 떄
				else if (m.getType().equals("cancel")) {
					if(m.getMsg().get(0).equals("valid"))
							updateReservationList();
					else if(m.getMsg().get(0).equals("invalid"))
						return;
				}
				//내정보 가져오기 메시지일때
				else if(m.getType().equals("MyInfo")) {
					for(int i=0;i<m.getMsg().size();i++)
						v.myInfoPanel.myInfoUpdatePanel.textField[i].setText(m.getMsg().get(i));
					String memo = new String("*항공예약시스템에 저장된 내 정보*\r\n\r\n");
					for (int i = 0; i < 6; i++) {
						memo += (v.myInfoPanel.myInfoUpdatePanel.infoStr[i] + ": "
								+ v.myInfoPanel.myInfoUpdatePanel.textField[i].getText() + "\r\n");
						data[i] = v.myInfoPanel.myInfoUpdatePanel.textField[i].getText();
					}
					v.myInfoPanel.myInfoUpdatePanel.textArea.setText(memo);
					updateReservationList();
				}
				//내정보 변경하기 메시지일때
				else if(m.getType().equals("MyInfoChange")) {
					String memo = new String("*항공예약시스템에 저장된 내 정보*\r\n\r\n");
					for (int i = 0; i < 6; i++) {
						memo += (v.myInfoPanel.myInfoUpdatePanel.infoStr[i] + ": "
								+ v.myInfoPanel.myInfoUpdatePanel.textField[i].getText() + "\r\n");
						data[i] = v.myInfoPanel.myInfoUpdatePanel.textField[i].getText();
					}
					updateReservationList();
					v.myInfoPanel.myInfoUpdatePanel.textArea.setText(memo);
				}
				//내좌석 변경하기 메시지일때
				else if(m.getType().equals("MySeatChange")) {
					if(m.getMsg().get(0).equals("invalid")) {
						return;
					}
					else if(m.getMsg().get(0).equals("valid")) {
						selectReser_info = Integer.valueOf(m.getMsg().get(1));
						LinkedList<String> strArray = new LinkedList<String>();//로그아웃 메시지 송신
						strArray.add(m.getMsg().get(2));
						outMsg.println(gson.toJson(new Message(v._userId, "", strArray, "MySeatChange_isvalid")));
					}
				}
				//좌석 변경 가능한 상황일떄 좌석변경하기 메시지 
				else if(m.getType().equals("MySeatChange_isvalid")) {
					if(m.getMsg().get(0).equals("deleteComplete")){
						updateSelectedSeat(selectReser_info);
						updateReservationList();
						isChangeSeat = true;
						v.card.show(v.c, "selectSeat");
					}
				}
				
				//왕복 항공편 조회하는 메시지
				else if(m.getType().equals("AirLineInfo_roundtrip")) {
					String depAirLineStr = "항공ID\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t일반석운임\t비즈니스석운임\n";
					for (AirLine r : m.getAirlinemsg1()) {
						depAirLineStr += r.getID() + "\t" + r.getAirLineNm() + "\t" + r.getDepAirportNm() + "\t"
								+ r.getArrAirportNm() + "\t" + r.getDepPlandTime().substring(8, 10) + ":"
								+ r.getDepPlandTime().substring(10, 12) + "\t"
								+ r.getArrPlandTime().substring(8, 10) + ":"
								+ r.getArrPlandTime().substring(10, 12) + "\t" + r.getEconomyCharge() + "\t"
								+ r.getPrestigeCharge() + "\n";
					}
					String desAirLineStr = "항공ID\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t일반석운임\t비즈니스석운임\n";
					for (AirLine r : m.getAirlinemsg2()) {
						desAirLineStr += r.getID() + "\t" + r.getAirLineNm() + "\t" + r.getDepAirportNm() + "\t"
								+ r.getArrAirportNm() + "\t" + r.getDepPlandTime().substring(8, 10) + ":"
								+ r.getDepPlandTime().substring(10, 12) + "\t"
								+ r.getArrPlandTime().substring(8, 10) + ":"
								+ r.getArrPlandTime().substring(10, 12) + "\t" + r.getEconomyCharge() + "\t"
								+ r.getPrestigeCharge() + "\n";
					}
					if (m.getAirlinemsg1().size() == 0)
						v.flightResPanel.departureAirportTextArea.setText("검색 결과가 없습니다.");
					else
						v.flightResPanel.departureAirportTextArea.setText(depAirLineStr);

					if (m.getAirlinemsg2().size() == 0)
						v.flightResPanel.destAirportTextArea.setText("검색 결과가 없습니다.");
					else
						v.flightResPanel.destAirportTextArea.setText(desAirLineStr);
					
					
				}
				
				//편도 항공편 조회하는 메시지
				else if(m.getType().equals("AirLineInfo_oneway")) {
					String depAirLineStr = "항공ID\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t일반석운임\t비즈니스석운임\n";
					for (AirLine r :m.getAirlinemsg1()) {
						depAirLineStr += r.getID() + "\t" + r.getAirLineNm() + "\t" + r.getDepAirportNm() + "\t"
								+ r.getArrAirportNm() + "\t" + r.getDepPlandTime().substring(8, 10) + ":"
								+ r.getDepPlandTime().substring(10, 12) + "\t"
								+ r.getArrPlandTime().substring(8, 10) + ":"
								+ r.getArrPlandTime().substring(10, 12) + "\t" + r.getEconomyCharge() + "\t"
								+ r.getPrestigeCharge() + "\n";
					}

					if (m.getAirlinemsg1().size() == 0)
						v.flightResPanel.departureAirportTextArea.setText("검색 결과가 없습니다.");
					else
						v.flightResPanel.departureAirportTextArea.setText(depAirLineStr);

					v.flightResPanel.destAirportTextArea.setText("");

					// output -> 항공기 textArea(scroll)1 반영 및 refresh
				}
			}
		}

	}

	// 관리자 화면
	class ManagerUIController{
		private final ManagerUIFrame v;
		public ManagerUIController(ManagerUIFrame ui) {

			this.v = ui;

			// 항공 관리 탭팬 액션리스너
			v.addChangeListener((ChangeListener) new ChangeListener() {
				public void stateChanged(ChangeEvent e) { // 컴포넌트들 기본값으로 초기화
					if (v.flightPanel.mainJtabUI.getSelectedIndex() == 0) { //'항공 등록' 탭팬 선택
						v.flightPanel.createTextArea.setText("");
						v.flightPanel.updateTextArea.setText("");
						v.flightPanel.deleteTextArea.setText(""); //textArea 초기화
						
						for (int i = 0; i < v.flightPanel.fliCreatetextField.length; i++) { //항공 등록 textField 초기화
							v.flightPanel.fliCreatetextField[i].setText("");
						}
						for (int i = 0; i < v.flightPanel.fliUpdatetextField.length; i++) { //항공 변경 textField 초기화
							v.flightPanel.fliUpdatetextField[i].setText("");
						}
						v.flightPanel.fliDeletetextField.setText(""); //항공 삭제 textField 초기화
						
						v.flightPanel.departureAirportCreateCombo.setSelectedIndex(0);
						v.flightPanel.destAirportCreateCombo.setSelectedIndex(0); //공항 선택 comboBox 초기화
					} 
					else if (v.flightPanel.mainJtabUI.getSelectedIndex() == 1) { //'항공 변경' 탭팬 선택
						v.flightPanel.createTextArea.setText("");
						v.flightPanel.updateTextArea.setText("");
						v.flightPanel.deleteTextArea.setText(""); //textArea 초기화
						
						for (int i = 0; i < v.flightPanel.fliCreatetextField.length; i++) { //항공 등록 textField 초기화
							v.flightPanel.fliCreatetextField[i].setText("");
						}
						for (int i = 0; i < v.flightPanel.fliUpdatetextField.length; i++) { //항공 변경 textField 초기화
							v.flightPanel.fliUpdatetextField[i].setText("");
						}
						v.flightPanel.fliDeletetextField.setText(""); //항공 삭제 textField 초기화
						
						v.flightPanel.departureAirportCreateCombo.setSelectedIndex(0);
						v.flightPanel.destAirportCreateCombo.setSelectedIndex(0); //공항 선택 comboBox 초기화
					} 
					else if (v.flightPanel.mainJtabUI.getSelectedIndex() == 2) { //'항공 삭제' 탭팬 선택
						v.flightPanel.createTextArea.setText("");
						v.flightPanel.updateTextArea.setText("");
						v.flightPanel.deleteTextArea.setText(""); //textArea 초기화
						
						for (int i = 0; i < v.flightPanel.fliCreatetextField.length; i++) { //항공 등록 textField 초기화
							v.flightPanel.fliCreatetextField[i].setText("");
						}
						for (int i = 0; i < v.flightPanel.fliUpdatetextField.length; i++) { //항공 변경 textField 초기화
							v.flightPanel.fliUpdatetextField[i].setText("");
						}
						v.flightPanel.fliDeletetextField.setText(""); //항공 삭제 textField 초기화
						
						v.flightPanel.departureAirportCreateCombo.setSelectedIndex(0);
						v.flightPanel.destAirportCreateCombo.setSelectedIndex(0); //공항 선택 comboBox 초기화
					}
				}
			});

			v.addButtonActionListener(new ActionListener() {
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
						v.managerPanel.memTextArea.setText(""); // 텍스트아리아 초기화
						v.managerPanel.memDeletetextField.setText(""); // 텍스트필드 초기화
						v.card.show(v.c, "manager"); // 회원 관리 패널로 넘어감
					}
					if (obj == v.managerMenuPanel.resButton) {
						v.reservationPanel.resTextArea.setText(""); // 텍스트아리아 초기화
						v.card.show(v.c, "reservation"); // 예약 관리 패널로 넘어감
					}
					if (obj == v.managerMenuPanel.fliButton) {
						v.flightPanel.createTextArea.setText("");
						v.flightPanel.updateTextArea.setText("");
						v.flightPanel.deleteTextArea.setText(""); // 텍스트아리아 초기화

						for (int i = 0; i < 5; i++) // 텍스트필드 초기화
							v.flightPanel.fliCreatetextField[i].setText("");
						for (int i = 0; i < 5; i++)
							v.flightPanel.fliUpdatetextField[i].setText("");
						v.flightPanel.fliDeletetextField.setText("");

						// 콤보박스 기본값으로 돌아가기
						v.flightPanel.departureAirportCreateCombo.setSelectedIndex(0);
						v.flightPanel.destAirportCreateCombo.setSelectedIndex(0);

						v.flightPanel.mainJtabUI.setSelectedIndex(0); // 항공 관리 탭팬 기본값이 '항공 등록' 탭
						v.card.show(v.c, "flight"); // 항공 관리 패널로 넘어감
					}

					if (obj == v.managerPanel.backButton) {
						v.card.show(v.c, "managerMenu"); // 관리자 메뉴로 넘어감
					}
					
					// 항공 등록 창 - 조회하기 버튼
					if (obj == v.flightPanel.flightCreateSearchButton) {
						ArrayList<AirLine> list = new ArrayList<AirLine>();
						try {
							list = aDAO.getAllALInfo();
						} catch (SQLException e1) {
//							e1.printStackTrace();
							System.out.println("DB 오류!");
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("항공ID\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t\t일반석운임\t비즈니스석운임\n");
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
						v.flightPanel.createTextArea.setText(sb.toString());
						v.flightPanel.createTextArea.setCaretPosition(0); //텍스트아리아 맨 위로
					}
					
					// 항공 변경 창 - 조회하기 버튼
					if (obj == v.flightPanel.flightUpdateSearchButton) {
						ArrayList<AirLine> list = new ArrayList<AirLine>();
						try {
							list = aDAO.getAllALInfo();
						} catch (SQLException e1) {
//							e1.printStackTrace();
							System.out.println("DB 오류!");
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("항공ID\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t\t일반석운임\t비즈니스석운임\n");
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
						v.flightPanel.updateTextArea.setText(sb.toString());
						v.flightPanel.updateTextArea.setCaretPosition(0);
					}

					// 항공 삭제 창 - 조회하기 버튼
					if (obj == v.flightPanel.flightDeleteSearchButton) {
						ArrayList<AirLine> list = new ArrayList<AirLine>();
						try {
							list = aDAO.getAllALInfo();
						} catch (SQLException e1) {
//							e1.printStackTrace();
							System.out.println("DB 오류!");
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("항공ID\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t\t일반석운임\t비즈니스석운임\n");
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
						v.flightPanel.deleteTextArea.setText(sb.toString());
						v.flightPanel.deleteTextArea.setCaretPosition(0);
					}

					//항공 등록하기 버튼
					if (obj == v.flightPanel.flightCreateButton) {
						AirLine info = new AirLine();
						// 텍스트필드 다 입력되었는지 검사
						if (v.flightPanel.fliCreatetextField[0].getText().equals("")
								|| v.flightPanel.fliCreatetextField[1].getText().equals("")
								|| v.flightPanel.fliCreatetextField[2].getText().equals("")
								|| v.flightPanel.fliCreatetextField[3].getText().equals("")
								|| v.flightPanel.fliCreatetextField[4].getText().equals("")) {
							v.flightTextFieldDialog();
							return;
						}
						// 운임은 숫자로만 입력해야 되기 때문에 검사
						if (!isStringDouble(v.flightPanel.fliCreatetextField[3].getText())
								|| !isStringDouble(v.flightPanel.fliCreatetextField[4].getText())) {
							v.flightFreightDialog();
							return;
						}
						
						//출발시간, 도착시간 입력형태 검사
						if(v.flightPanel.fliCreatetextField[1].getText().length()!=12 ||
								v.flightPanel.fliCreatetextField[2].getText().length()!=12) {
							v.flightTimeDialog();
							return;
						}
						
						info.setAirLineNm(v.flightPanel.fliCreatetextField[0].getText());
						info.setDepPlandTime(v.flightPanel.fliCreatetextField[1].getText());
						info.setArrPlandTime(v.flightPanel.fliCreatetextField[2].getText());
						info.setEconomyCharge(Integer.parseInt(v.flightPanel.fliCreatetextField[3].getText()));
						info.setPrestigeCharge(Integer.parseInt(v.flightPanel.fliCreatetextField[4].getText()));
						info.setDepAirportNm(v.flightPanel.departureAirportCreateCombo.getSelectedItem().toString());
						info.setArrAirportNm(v.flightPanel.destAirportCreateCombo.getSelectedItem().toString());
						int result = aDAO.addALInfo(info);

						// result > 0 이면 항공기 추가 완료 다이얼로그 띄우기
						v.flightCreateDialog(result);

						for (int i = 0; i < 5; i++)
							v.flightPanel.fliCreatetextField[i].setText("");

						// 콤보박스 기본값으로 돌아가기
						v.flightPanel.departureAirportCreateCombo.setSelectedIndex(0);
						v.flightPanel.destAirportCreateCombo.setSelectedIndex(0);

						// textArea update
						ArrayList<AirLine> list = new ArrayList<AirLine>();
						try {
							list = aDAO.getAllALInfo();
						} catch (SQLException e1) {
//							e1.printStackTrace();
							System.out.println("DB 오류!");
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("항공ID\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t\t일반석운임\t비즈니스석운임\n");
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
						v.flightPanel.createTextArea.setText(sb.toString());
						v.flightPanel.createTextArea.setCaretPosition(0);
					}
					
					// 항공 변경하기 버튼
					if (obj == v.flightPanel.flightUpdateButton) {

						AirLine info = new AirLine();
						// 텍스트필드 다 입력되었는지 검사
						if (v.flightPanel.fliUpdatetextField[0].getText().equals("")
								|| v.flightPanel.fliUpdatetextField[1].getText().equals("")
								|| v.flightPanel.fliUpdatetextField[2].getText().equals("")
								|| v.flightPanel.fliUpdatetextField[3].getText().equals("")
								|| v.flightPanel.fliUpdatetextField[4].getText().equals("")) {
							v.flightTextFieldDialog();
							return;
						}
						
						//출발시간, 도착시간 입력형태 검사
						if(v.flightPanel.fliUpdatetextField[1].getText().length()!=12 ||
								v.flightPanel.fliUpdatetextField[2].getText().length()!=12) {
							v.flightTimeDialog();
							return;
						}
						
						// 항공권 id와 운임은 int형이기 때문에 숫자로만 입력되어야 함
						if (!isStringDouble(v.flightPanel.fliUpdatetextField[0].getText())) {
							v.flightIDDialog();
							return;
						}

						if (!isStringDouble(v.flightPanel.fliUpdatetextField[3].getText())
								|| !isStringDouble(v.flightPanel.fliUpdatetextField[4].getText())) {
							v.flightFreightDialog();
							return;
						}
						info.setID(Integer.parseInt(v.flightPanel.fliUpdatetextField[0].getText()));
						info.setDepPlandTime(v.flightPanel.fliUpdatetextField[1].getText());
						info.setArrPlandTime(v.flightPanel.fliUpdatetextField[2].getText());
						info.setEconomyCharge(Integer.parseInt(v.flightPanel.fliUpdatetextField[3].getText()));
						info.setPrestigeCharge(Integer.parseInt(v.flightPanel.fliUpdatetextField[4].getText()));

						int result = aDAO.updateALInfo(info);
						// result > 0 이면 항공기 변경 완료 다이얼로그 띄우기
						v.flightUpdateDialog(result);

						for (int i = 0; i < 5; i++)
							v.flightPanel.fliUpdatetextField[i].setText("");
						// textArea update
						ArrayList<AirLine> list = new ArrayList<AirLine>();
						try {
							list = aDAO.getAllALInfo();
						} catch (SQLException e1) {
//							e1.printStackTrace();
							System.out.println("DB 오류!");
						}
						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("항공ID\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t\t일반석운임\t비즈니스석운임\n");
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
						v.flightPanel.updateTextArea.setText(sb.toString());
						v.flightPanel.updateTextArea.setCaretPosition(0);
					}
					
					// 항공 삭제하기 버튼
					if (obj == v.flightPanel.flightDeleteButton) {
						// 텍스트필드 입력 되었는지 검사
						if (v.flightPanel.fliDeletetextField.getText().equals("")) {
							v.flightTextFieldDialog();
							return;
						}
						
						// 항공권 id와 운임은 int형이기 때문에 숫자로만 입력되어야 함
						if (!isStringDouble(v.flightPanel.fliDeletetextField.getText())) {
							v.flightIDDialog();
							return;
						}

						int result = aDAO.delALInfo(Integer.parseInt(v.flightPanel.fliDeletetextField.getText()));
						// result > 0 이면 항공기 삭제 완료 다이얼로그 띄우기
						v.flightDeleteDialog(result);

						v.flightPanel.fliDeletetextField.setText("");

						// textArea update
						ArrayList<AirLine> list = new ArrayList<AirLine>();
						try {
							list = aDAO.getAllALInfo();
						} catch (SQLException e1) {
//							e1.printStackTrace();
							System.out.println("DB 오류!");
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("항공ID\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t\t일반석운임\t비즈니스석운임\n");
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
						v.flightPanel.deleteTextArea.setText(sb.toString());
						v.flightPanel.deleteTextArea.setCaretPosition(0);

					}
					
					// 항공 관리 창의 뒤로가기 버튼
					if (obj == v.flightPanel.backButton) {
						v.card.show(v.c, "managerMenu");
					}

					// 회원 관리 창의 조회하기 버튼
					if (obj == v.managerPanel.memSearchButton) {
						// 모든 회원정보 조회
						ArrayList<User> list = new ArrayList<User>();
						try {
							list = uDAO.getAll();
						} catch (SQLException e1) {
//							e1.printStackTrace();
							System.out.println("DB 오류!");
						}
						StringBuffer sb = new StringBuffer("");
						if (list != null) {
							sb.append("아이디\t이름\t비밀번호\t이메일\t생일\t전화번호\n");
							for (User p : list) {
								sb.append(p.getID() + "\t");
								sb.append(p.getName() + "\t");
								sb.append(p.getPw() + "\t");
								sb.append(p.getEmail() + "\t");
								sb.append(p.getBirth() + "\t");
								sb.append(p.getPhone() + "\t\n");

							}
						}
						v.managerPanel.memTextArea.setText(sb.toString());
						v.managerPanel.memTextArea.setCaretPosition(0);

					}

					// 회원 관리 창의 삭제하기 버튼
					if (obj == v.managerPanel.memDeleteButton) {
						// 멤버 삭제
						int result = uDAO.deleteUser(v.managerPanel.memDeletetextField.getText());
						v.memberDeleteDialog(result);
						v.managerPanel.memDeletetextField.setText("");

						// 바뀐정보 업데이트하기
						ArrayList<User> list = new ArrayList<User>();
						StringBuffer sb = new StringBuffer("");
						try {
							list = uDAO.getAll();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
//							e1.printStackTrace();
							System.out.println("DB 오류!");
						}

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
						v.managerPanel.memTextArea.setText(sb.toString());
						v.managerPanel.memTextArea.setCaretPosition(0);

					}
					
					// 예약 관리 창의 뒤로가기 버튼
					if (obj == v.reservationPanel.backButton) {
						v.card.show(v.c, "managerMenu");
					}

					// 예약 관리 창의 조회하기 버튼
					if (obj == v.reservationPanel.reservationSearchButton) {
						ArrayList<Reservation> list = new ArrayList<Reservation>();
						try {
							list = rDAO.getAll();
						} catch (SQLException e1) {
//							e1.printStackTrace();
							System.out.println("DB 오류!");
						}
						String reservationListStr = "예약 ID\t항공사 이름\t예약자 이름\t좌석\t출발 시간\t도착 시간\t출발 공항\t도착 공항\t비용\n";

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

						v.reservationPanel.resTextArea.setText(reservationListStr);
						v.reservationPanel.resTextArea.setCaretPosition(0);
					}
				}

				public boolean isStringDouble(String s) {
					try {
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
					Object obj = e.getSource();
					if (obj == v.startUIPanel.managerButton) {
						System.out.println("매니저");
						ManagerUIFrame mu = new ManagerUIFrame();
						MCT.setManagerC(mu);
						v.loginUIFrameExit();
					}
					if (obj == v.startUIPanel.userButton) {
						System.out.println("사용자");
						v.card.next(v.c);
					}

					if (obj == v.loginUIpanel.loginButton[0]) {
						System.out.println("로그인");
					}

					if (obj == v.loginUIpanel.loginButton[0]) {// 로그인 버튼
						v.userId = v.loginUIpanel.loginTextField[0].getText();
						String pw = v.loginUIpanel.loginTextField[1].getText();
						uDAO = new UserDAO();
						if (uDAO.getUser(v.userId) == null) {
							v.loginMemberInfoDialog();
							return;
						} else {
							if (uDAO.getUser(v.userId).getPw().equals(pw)) {
								UF = new UserUIFrame(v.userId);
								MCT.setUserC(UF);
								v.loginUIFrameExit();
							} else {
								v.loginPasswordDialog();
								return;
							}

						}

					} else if (obj == v.loginUIpanel.loginButton[1]) {// 회원가입 버튼
						v.card.next(v.c);
					}

					else if (obj == v.loginUIpanel.backButton) {// 돌아가기 버튼
						v.card.show(v.c, "1");
					}


					if (obj == v.signUpPanel.signUpButton) {// 회원가입 버튼

						if (v.signUpPanel.textField[0].getText().equals("")
								|| v.signUpPanel.textField[1].getText().equals("")
								|| v.signUpPanel.textField[2].getText().equals("")
								|| v.signUpPanel.textField[3].getText().equals("")
								|| v.signUpPanel.textField[4].getText().equals("")
								|| v.signUpPanel.textField[5].getText().equals("")) {
							v.signUpTextFieldDialog();
							return;
						}

						User user = new User();
						String userData[] = new String[6];

						for (int i = 0; i < 6; i++) {
							userData[i] = v.signUpPanel.textField[i].getText();
						}
						user.setUser(userData);

						if (!uDAO.newUser(user)) {
							v.signUpIDDialog();
							for (int i = 0; i < 6; i++) {
								v.signUpPanel.textField[i].setText("");
							}
							return;
						}
						v.signUpDialog();

						for (int i = 0; i < 6; i++) {
							v.signUpPanel.textField[i].setText("");
						}
						v.card.previous(v.c);
					} else if (obj == v.signUpPanel.backButton) {// 돌아가기 버튼
						v.card.show(v.c, "2");
					}

				}

			});

		}

	}

	class AirPortParkingLotUIController {
		private final AirPortParkingLotUIFrame v;

		AirPortParkingLotUIController(AirPortParkingLotUIFrame ui) {
			this.v = ui;
			v.addButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String airport = "";
					
					//공항 주차장 창의 검색하기 버튼 
					if (e.getSource() == v.airPortButton) {
						airport = v.airPortComboBox.getSelectedItem().toString();
						StringBuffer sb = new StringBuffer();
						try {
							new AirPortParkingLotParser();
						} catch (SQLException e2) {
//							e2.printStackTrace();
							System.out.println("DB 오류!");
						}

						//인천공항과 나머지 4가지 공항의 데이터가 다르기 때문에 if문으로 구분하여 사용
						if (airport.equals("인천국제공항")) {
							ArrayList<InCheonAirPortParkingLot> list = new ArrayList<InCheonAirPortParkingLot>();
							list = pDAO.getICAPInfo();
							if (list != null) {
								sb.append("주차구역명\t\t 입고된 차량 수\t\t 전체 주차면 수\t 업데이트 날짜\t 업데이트 시간\n");
								for (InCheonAirPortParkingLot a : list) {
									if (a.getFloor().length() > 10)
										sb.append(a.getFloor() + "\t");
									else
										sb.append(a.getFloor() + "\t\t");
									sb.append(a.getParking() + "\t\t ");
									sb.append(a.getParkingarea() + "\t ");
									String timeString = a.getDatetm();

									// 사용자가 한 눈에 알아보기 쉽도록 하기 위해서 문자열을 쪼갬
									String year = timeString.substring(0, 4);
									String month = timeString.substring(4, 6);
									String day = timeString.substring(6, 8);
									String updateDate = year + "-" + month + "-" + day;

									String h = timeString.substring(8, 10);
									String m = timeString.substring(10, 12);
									String s = timeString.substring(12, 14);
									String updateTime = h + ":" + m + ":" + s;
									sb.append(updateDate + "\t");
									sb.append(updateTime + "\n");

								}
							}
							v.airPortTextArea.setText(sb.toString());

						} else {
							ArrayList<AirPortParkingLot> list = new ArrayList<AirPortParkingLot>();
							list = pDAO.getAPInfo(airport);
							if (list != null) {
								sb.append("주차구역명\t\t 주차장 혼잡도\t 주차장 혼잡률\t 입고된 차량 수\t 전체 주차면 수\t 업데이트 날짜\t 업데이트 시간\n");
								for (AirPortParkingLot a : list) {
									if (a.getParkingAirportCodeName().length() > 8)
										sb.append(a.getParkingAirportCodeName() + "\t");
									else
										sb.append(a.getParkingAirportCodeName() + "\t\t");
									sb.append(a.getParkingCongestion() + "\t ");
									sb.append(a.getParkingCongestionDegree() + "\t ");
									sb.append(a.getParkingOccupiedSpace() + "\t ");
									sb.append(a.getParkingTotalSpace() + "\t ");
									sb.append(a.getSysGetdate() + "\t ");
									sb.append(a.getSysGettime() + "\t\n");
								}
							}
							v.airPortTextArea.setText(sb.toString());
						}

					}

				}

			});
		}
	}

	public boolean isStringDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
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

	public void setParkingC(AirPortParkingLotUIFrame ui) {
		PC = new AirPortParkingLotUIController(ui);
	}

	public static void main(String[] args) {

		MainController Controller = new MainController();
		Controller.LF = new LoginUIFrame();
		Controller.setLoginC(Controller.LF);

	}

}