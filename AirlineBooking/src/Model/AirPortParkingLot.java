package Model;

public class AirPortParkingLot {
	int id;
	String airportEng; //공항 영어로
	String airportKor; //공항 한국어로
	String parkingAirportCodeName; //주차장 구역
	String parkingCongestion; //혼잡도
	String parkingCongestionDegree; //혼잡률
	String parkingOccupiedSpace; //입고된 차량 수
	String parkingTotalSpace; // 전체 주차면 수
	String sysGetdate; //업데이트 날짜
	String sysGettime; //업데이트 시각
	
	public int getId() {
	      return id;
	}

	public void setId(int id) {
	      this.id = id;
	}
	
	////////////////////////////////////////////////
	
	public String getAirportEng() {
	      return airportEng;
	}

	public void setAirportEng(String airportEng) {
	      this.airportEng = airportEng;
	}
	
	////////////////////////////////////////////////
	
	public String getAirportKor() {
	      return airportKor;
	}

	public void setAirportKor(String airportKor) {
	      this.airportKor = airportKor;
	}
	
	/////////////////////////////////////////////////
	
	public String getParkingAirportCodeName() {
	      return parkingAirportCodeName;
	}

	public void setParkingAirportCodeName(String parkingAirportCodeName) {
	      this.parkingAirportCodeName = parkingAirportCodeName;
	}
	
	//////////////////////////////////////////////////
	
	public String getParkingCongestion() {
	      return parkingCongestion;
	}

	public void setParkingCongestion(String parkingCongestion) {
	      this.parkingCongestion = parkingCongestion;
	}
	
	////////////////////////////////////////////////////
	
	public String getParkingCongestionDegree() {
	      return parkingCongestionDegree;
	}

	public void setParkingCongestionDegree(String parkingCongestionDegree) {
	      this.parkingCongestionDegree = parkingCongestionDegree;
	}
	
	////////////////////////////////////////////////////
	
	public String getParkingOccupiedSpace() {
	      return parkingOccupiedSpace;
	}

	public void setParkingOccupiedSpace(String parkingOccupiedSpace) {
	      this.parkingOccupiedSpace = parkingOccupiedSpace;
	}
	
	/////////////////////////////////////////////////////
	
	public String getParkingTotalSpace() {
	      return parkingTotalSpace;
	}

	public void setParkingTotalSpace(String parkingTotalSpace) {
	      this.parkingTotalSpace = parkingTotalSpace;
	}
	
	///////////////////////////////////////////////////////
	
	public String getSysGetdate() {
	      return sysGetdate;
	}

	public void setSysGetdate(String sysGetdate) {
	      this.sysGetdate = sysGetdate;
	}
	
	////////////////////////////////////////////////////////
	
	public String getSysGettime() {
	      return sysGettime;
	}

	public void setSysGettime(String sysGettime) {
	      this.sysGettime = sysGettime;
	}
}
