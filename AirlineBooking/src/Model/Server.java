package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.google.gson.Gson;

import message.Message;


public class Server {

	// 서버 소켓 및 클라이언트 연결 소켓
	private ServerSocket ss = null;
	private Socket s = null;

	// 연결된 클라이언트 스레드를 관리하는 ArrayList
	ArrayList<FlightReservationThread> reservesThreadsList = new ArrayList<FlightReservationThread>();

	// 로거 객체
	Logger logger;
	
	AirLineDAO aDAO=new AirLineDAO();
	ReservationDAO rDAO=new ReservationDAO();
	UserDAO uDAO=new UserDAO();

	String msg;
	
	// 멀티 채팅 메인 프로그램 부분
	

	synchronized boolean resultCreateReservation(Reservation r) {
		boolean result;
		result = rDAO.checkReservationByInfoWithSeat(r.getInfo(), r.getSeatNum());
		if(!result) {
			rDAO.newReservation(r);
		}
		return result;
	}
	
	
	public void start() {
		logger = Logger.getLogger(this.getClass().getName());
		try {
			// 서버 소켓 생성
			ss = new ServerSocket(9000);
			logger.info("MultiReservationServer start");

			// 무한 루프를 돌면서 클라이언트 연결을 기다린다.
			while (true) {
				s = ss.accept();
				// 연결된 클라이언트에 대해 스레드 클래스 생성
				FlightReservationThread reserve = new FlightReservationThread();
				// 클라이언트 리스트 추가
				reservesThreadsList.add(reserve);
				// 스레드 시작
				reserve.start();
			}
		} catch (Exception e) {
			logger.info("[MultiReservationServer]start() Exception 발생!!");
			e.printStackTrace();
		}
	}

	class FlightReservationThread extends Thread {
		// 수신 메시지 및 파싱 메시지 처리를 위한 변수 선언
		LinkedList<String> strArray;
		// 메시지 객체 생성
		Message m = new Message();
		// JSON 파서 초기화
		Gson gson = new Gson();

		String userid;

		boolean status = true;

		// 입출력 스트림
		private BufferedReader inMsg = null;
		private PrintWriter outMsg = null;

		
		FlightReservationThread() {
			try {
				inMsg = new BufferedReader(new InputStreamReader(s.getInputStream()));
				outMsg = new PrintWriter(s.getOutputStream(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// reservation 동기화 (select-좌석의 예약 여부 확인 후 좌석이 비어있는 경우 -> create-예약 생성)
		// (select-예약 확인 -> create-예약 생성) -> blocked: (select-예약 확인 -> create-예약 생성)
		
		public void run() {
			// 상태정보가 true 이면 루프 돌면서 사용자에게서 수신된 메시지 처리
			while (status) {
				// 수신된 메시지를 msg변수에 저장
				try {
					msg = inMsg.readLine();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
				// JSON 메시지를 Message 객체로 매핑
				m = gson.fromJson(msg, Message.class);

				// 로그인 메시지일 떄
				if (m.getType().equals("login")) {
					userid = m.getId();
				}
				//로그아웃 메시지일때
				else if(m.getType().equals("logout")) {
					reservesThreadsList.remove(this);
					status=false;
				}
				// 예약 취소 메시지일 떄
				else if (m.getType().equals("cancel")) {
					userid = m.getId();
					Reservation output=null;
					LinkedList<String> strArrayrecieve;
					try {
						output=rDAO.getReservation(Integer.valueOf(m.getMsg().get(0)));
					} 
					catch (NumberFormatException e) {
						e.printStackTrace();
						return;
						
					} catch (SQLException e) {
						strArrayrecieve=new LinkedList<String>();
						strArrayrecieve.add("invalid");
						Message Gsonmsg=new Message("","",strArrayrecieve,"cancel");
						reservationMsgSend(gson.toJson(Gsonmsg), m.getId());
						return;
					}
					rDAO.deleteReservation(Integer.valueOf(m.getMsg().get(0)));
					strArrayrecieve=new LinkedList<String>();
					strArrayrecieve.add("valid");
					Message Gsonmsg=new Message("","",strArrayrecieve,"cancel");
					reservationMsgSend(gson.toJson(Gsonmsg), m.getId());
				}
				//자리예약 메시지일때(자리선택하기 페이지)
				else if(m.getType().equals("reservation")) {
					strArray=m.getMsg();
					
					Reservation r = new Reservation();
					r.setInfo(Integer.valueOf(strArray.get(0)));
					r.setSeatNum(Integer.valueOf(strArray.get(1)));
					r.setUser(strArray.get(2));
					
					LinkedList<String> strArrayrecieve=new LinkedList<String>();
					
					boolean output = resultCreateReservation(r);
					
					if(output){
						logger.info("false보냄");
						strArrayrecieve.add("false");
						Message Gsonmsg=new Message("","",strArrayrecieve,"reservationMessage");
						reservationMsgSend(gson.toJson(Gsonmsg), m.getId());
					}
					else {
						logger.info("true보냄");
						strArrayrecieve.add("true");
						strArrayrecieve.add(strArray.get(1));
						Message Gsonmsg=new Message("","",strArrayrecieve,"reservationMessage");
						reservationMsgSend(gson.toJson(Gsonmsg), m.getId());
					}
				}
				//내정보 가져오기 메시지일때
				else if(m.getType().equals("MyInfo")) {
					User currentUser = uDAO.getUser(m.getMsg().get(0));
					LinkedList<String> strArrayrecieve=new LinkedList<String>();
					strArrayrecieve.add(currentUser.getName());
					strArrayrecieve.add(currentUser.getID());
					strArrayrecieve.add(currentUser.getPw());
					strArrayrecieve.add(currentUser.getEmail());
					strArrayrecieve.add(currentUser.getBirth());
					strArrayrecieve.add(currentUser.getPhone());
		
					Message Gsonmsg=new Message("","",strArrayrecieve,"MyInfo");
					reservationMsgSend(gson.toJson(Gsonmsg), m.getId());
				}
				//내정보 변경하기 메시지일때
				else if(m.getType().equals("MyInfoChange")) {
					User user = new User();
					user.setUser(m.getMsg());
					uDAO.updateUser(user);
					LinkedList<String> strArrayrecieve=new LinkedList<String>();
					Message Gsonmsg=new Message("","",strArrayrecieve,"MyInfoChange");
					reservationMsgSend(gson.toJson(Gsonmsg), m.getId());
				}
				//내정보 탈퇴하기 메시지일때
				else if(m.getType().equals("MyInfoDelete")) {
					// #### DAO #### -> 삭제 전: dialog 필요성,
					User user = new User();
					user.setUser(m.getMsg());
					uDAO.deleteUser(user.getID());
				}
				//좌석 변경 가능한 상황인지 알아보는 메시지일때
				else if(m.getType().equals("MySeatChange")) {
					int num = Integer.parseInt(m.getMsg().get(0));
					Reservation search;
					LinkedList<String> strArrayrecieve=new LinkedList<String>();
					try {
						search = rDAO.getReservation(num);
						if (!search.getUser().equals(m.getMsg().get(1))) {
							strArrayrecieve.add("invalid");
						}
						else {
							strArrayrecieve.add("valid");
							strArrayrecieve.add(Integer.toString(search.getInfo()));
							strArrayrecieve.add(Integer.toString(search.getID()));
						}
						Message Gsonmsg=new Message("","",strArrayrecieve,"MySeatChange");
						reservationMsgSend(gson.toJson(Gsonmsg), m.getId());
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				//좌석 변경 가능한 상황일떄 좌석변경하기 메시지 
				else if(m.getType().equals("MySeatChange_isvalid")) {
					rDAO.deleteReservation(Integer.valueOf(m.getMsg().get(0)));
					
					LinkedList<String> strArrayrecieve=new LinkedList<String>();
					strArrayrecieve.add("deleteComplete");
					Message Gsonmsg=new Message("","",strArrayrecieve,"MySeatChange_isvalid");
					reservationMsgSend(gson.toJson(Gsonmsg), m.getId());
				}
				//왕복 항공편 조회하는 메시지
				else if(m.getType().equals("AirLineInfo_roundtrip")) {
					ArrayList<AirLine> output = null;
					ArrayList<AirLine> output2 = null; // 돌아오는
					
					try {
						output = aDAO.getALInfoByChoice(m.getMsg().get(0), m.getMsg().get(1), m.getMsg().get(2));
						output2 = aDAO.getALInfoByChoice(m.getMsg().get(1), m.getMsg().get(0), m.getMsg().get(3));
						// results by current day/time
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						// 결과가 없음..? 비행기 안뜸.. or 너무 미래
						// 과거 시간 선택 검증
					}
					Message Gsonmsg=new Message("","",output,output2,"AirLineInfo_roundtrip");
					reservationMsgSend(gson.toJson(Gsonmsg), m.getId());
				}
				//편도 항공편 조회하는 메시지
				else if(m.getType().equals("AirLineInfo_oneway")) {
					ArrayList<AirLine> output = null;
					ArrayList<AirLine> output2 = null; // 돌아오는
					try {
						output = aDAO.getALInfoByChoice(m.getMsg().get(0), m.getMsg().get(1), m.getMsg().get(2));
					} catch (SQLException e) {
						
						e.printStackTrace();
						// 결과가 없음..? 비행기 안뜸.. or 너무 미래
						// 과거 시간 선택 검증
					}
					Message Gsonmsg=new Message("","",output,output2,"AirLineInfo_roundtrip");
					reservationMsgSend(gson.toJson(Gsonmsg), m.getId());
					
				}
				// 그 밖의 경우, 즉 일반 메시지일 때
				else {
					msgSendAll(msg);
				}
			}
			// 루프를 벗어나면 클라이언트 연결이 종료되므로 스레드 인터럽트
			this.interrupt();
			logger.info(this.getName() + "종료됨!!");
		}

		// 연결된 모든 클라이언트에 메시지 중계
		public void msgSendAll(String msg) {
			for (FlightReservationThread ct : reservesThreadsList) {
				ct.outMsg.println(msg);
			}
		}
		public void reservationMsgSend(String msg, String reciever) {
			for (FlightReservationThread ct : reservesThreadsList) {
				System.out.println(ct.userid+": "+reciever);
				if(ct.userid.equals(reciever)) {
					ct.outMsg.println(msg);
					break;
				}
			}
		}
	}

	public static void main(String[] args) throws SQLException {
		Server server = new Server();
		new Parser();//파싱
		server.start();//서버 스타트
	}
}
