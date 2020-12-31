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
	
	ReservationDAO rDAO=new ReservationDAO();

	String msg;
	
	// 멀티 채팅 메인 프로그램 부분
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
		
		public void run() {
			// 상태정보가 true 이면 루프 돌면서 사용자에게서 수신된 메시지 처리
			while (status) {
				// 수신된 메시지를 msg변수에 저장
				try {
					msg = inMsg.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// JSON 메시지를 Message 객체로 매핑
				m = gson.fromJson(msg, Message.class);

				
				// 로그인 메시지일 떄
				if (m.getType().equals("login")) {
					userid = m.getId();
				}
				else if(m.getType().equals("reservation")) {
					strArray=m.getMsg();
					
					Reservation r = new Reservation();
					r.setInfo(Integer.valueOf(strArray.get(0)));
					r.setSeatNum(Integer.valueOf(strArray.get(1)));
					r.setUser(strArray.get(2));
					
					LinkedList<String> strArrayrecieve=new LinkedList<String>();
					
					if(rDAO.checkReservationByInfoWithSeat(r.getInfo(), r.getSeatNum())){
						strArrayrecieve.add("false");
						Message Gsonmsg=new Message("","",strArrayrecieve,"reservationMessage");
						reservationMsgSend(gson.toJson(Gsonmsg), m.getId());
					}
					else {
						strArrayrecieve.add("true");
						Message Gsonmsg=new Message("","",strArrayrecieve,"reservationMessage");
						reservationMsgSend(gson.toJson(Gsonmsg), m.getId());
					}
					
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
				if(ct.userid==reciever) {
				ct.outMsg.println(msg);
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
