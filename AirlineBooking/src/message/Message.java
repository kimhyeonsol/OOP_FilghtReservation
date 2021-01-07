package message;

import java.util.ArrayList;
import java.util.LinkedList;

import Model.AirLine;
import Model.Reservation;

public class Message {
	private String id;
	private String passwd;
	private LinkedList<String> msg=new LinkedList<String>();
	private String type;
	private ArrayList<AirLine> airlinemsg1 =new ArrayList<AirLine>();//가는 항공편 리스트
	private ArrayList<AirLine> airlinemsg2 =new ArrayList<AirLine>();//오는 항공편 리스트

	public Message(){
	}
	//일반 메시지 생성자
	public Message(String id,String passwd,LinkedList<String> msg,String type){
		this.id=id;
		this.passwd=passwd;
		this.msg=msg;
		this.type=type;
	}
	//가는 항공편, 오는 항공편 메시지 생성자
	public Message(String id,String passwd,ArrayList<AirLine> airlinemsg1,ArrayList<AirLine> airlinemsg2,String type){
		this.id=id;
		this.passwd=passwd;
		this.airlinemsg1=airlinemsg1;
		this.airlinemsg2=airlinemsg2;
		this.type=type;
	}
	
	public String getId() {
		return id;
	}
	
	public String getPasswd() {
		return passwd;
	}
	
	public LinkedList<String> getMsg() {
		return msg;
	}
	
	public ArrayList<AirLine> getAirlinemsg1() {
		return airlinemsg1;
	}
	
	//편도일 경우 2번쨰 arraylist는 null
	public ArrayList<AirLine> getAirlinemsg2() {
		return airlinemsg2;
	}
	
	public String getType() {
		return type;
	}
	
}
