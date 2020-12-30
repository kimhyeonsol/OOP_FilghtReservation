package Model;

public class User {
	private String ID;
	private String name;
	private String pw;
	private String email;
	private String birth;
	private String phone;

	// getter
	public String getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public String getPw() {
		return pw;
	}
	public String getEmail() {
		return email;
	}
	public String getBirth() {
		return birth;
	}
	public String getPhone() {
		return phone;
	}

	// setter
	public void setID(String ID) {
		this.ID = ID;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setUser(String[] data) {
		setName(data[0]);
		setID(data[1]);
		setPw(data[2]);
		setEmail(data[3]);
		setBirth(data[4]);
		setPhone(data[5]);
	}
	
}
