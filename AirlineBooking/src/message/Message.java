package message;

import java.util.LinkedList;

public class Message {
	private String id;
	private String passwd;
	private LinkedList<String> msg=new LinkedList<String>();
	private String type;

	
	public Message(){
		
	}
	
	public Message(String id,String passwd,LinkedList<String> msg,String type){
		this.id=id;
		this.passwd=passwd;
		this.msg=msg;
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
	public String getType() {
		return type;
	}
//	public String getRecieverId() {
//		return recieverId;
//	}
	
}
