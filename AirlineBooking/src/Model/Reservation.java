package Model;

public class Reservation {
	private int ID;
	private String user;
	private int info;
	private int seatNum;

	
	// getter
	public int getID() {
		return ID;
	}
	public String getUser() {
		return user;
	}
	public int getInfo() {
		return info;
	}
	public int getSeatNum() {
		return seatNum;
	}

	// setter
	public void setID(int ID) {
		this.ID = ID;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setInfo(int info) {
		this.info = info;
	}
	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}
}
