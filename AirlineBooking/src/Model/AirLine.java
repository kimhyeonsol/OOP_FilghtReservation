package Model;

public class AirLine {

	int ID;
	String airLineNm;
	String arrAirportNm;
	String arrPlandTime;
	String depAirportNm;
	String depPlandTime;
	int economyCharge;
	int prestigeCharge;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public String getAirLineNm() {
		return airLineNm;
	}

	public void setAirLineNm(String airLineNm) {
		this.airLineNm = airLineNm;
	}

	public String getArrAirportNm() {
		return arrAirportNm;
	}

	public void setArrAirportNm(String arrAirportNm) {
		this.arrAirportNm = arrAirportNm;
	}

	public String getArrPlandTime() {
		return arrPlandTime;
	}

	public void setArrPlandTime(String arrPlandTime) {
		this.arrPlandTime = arrPlandTime;
	}

	public String getDepAirportNm() {
		return depAirportNm;
	}

	public void setDepAirportNm(String depAirportNm) {
		this.depAirportNm = depAirportNm;
	}

	public String getDepPlandTime() {
		return depPlandTime;
	}

	public void setDepPlandTime(String depPlandTime) {
		this.depPlandTime = depPlandTime;
	}

	public int getEconomyCharge() {
		return economyCharge;
	}

	public void setEconomyCharge(int economyCharge) {
		this.economyCharge = economyCharge;
	}

	public int getPrestigeCharge() {
		return prestigeCharge;
	}

	public void setPrestigeCharge(int prestigeCharge) {
		this.prestigeCharge = prestigeCharge;
	}

}
