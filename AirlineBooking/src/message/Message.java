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
	private ArrayList<AirLine> airlinemsg1 =new ArrayList<AirLine>();
	private ArrayList<AirLine> airlinemsg2 =new ArrayList<AirLine>();

	public Message(){
	}
	
	public Message(String id,String passwd,LinkedList<String> msg,String type){
		this.id=id;
		this.passwd=passwd;
		this.msg=msg;
		this.type=type;
	}
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
	
	public ArrayList<AirLine> getAirlinemsg2() {
		return airlinemsg2;
	}
	public String getType() {
		return type;
	}
	
//	public String getRecieverId() {
//		return recieverId;
//	}
	
}
