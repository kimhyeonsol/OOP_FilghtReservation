
public class Reservation {
	private int ID;
	private User user;
	private AirLineDTO info;
	private int seatNum;

	
	// getter
	public int getID() {
		return ID;
	}
	public User getUser() {
		return user;
	}
	public AirLineDTO getInfo() {
		return info;
	}
	public int getSeatNum() {
		return seatNum;
	}
	
	public String getUserID() {
		return user.getID();
	}
	
	public int getInfoID() {
		return info.getID();
	}
	
	

	// setter
	public void setID(int ID) {
		this.ID = ID;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setInfo(AirLineDTO info) {
		this.info = info;
	}
	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}
}
