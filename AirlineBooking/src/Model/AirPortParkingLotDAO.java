package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class AirPortParkingLotDAO {
	String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://localhost:3306/airplanereservation?&serverTimezone=Asia/Seoul&useSSL=false";
	Connection conn;

	PreparedStatement pstmt;
	ResultSet rs;

	Vector<String> items = null;
	String sql;

	public AirPortParkingLotDAO() {
		connectDB();
	}

	public void connectDB() {
		try {
			Class.forName(jdbcDriver);

			conn = DriverManager.getConnection(jdbcUrl, "root", "root");
			if (conn == null)
				System.out.println("conn is null");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeDB() {
		try {
			pstmt.close();
			rs.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<AirPortParkingLot> getAPInfo(String airPort) {
		AirPortParkingLot p = new AirPortParkingLot();
		try {
			sql = "select * from airlineinfo where airportKor = ? ";
			
			ArrayList<AirPortParkingLot> datas = new ArrayList<AirPortParkingLot>();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, airPort);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				p.setId(rs.getInt("id"));
				p.setAirportEng(rs.getString("airportEng"));
				p.setAirportKor(rs.getString("airportKor"));
				p.setParkingAirportCodeName(rs.getString("parkingAirportCodeName"));
				p.setParkingCongestion(rs.getString("parkingCongestion"));
				p.setParkingCongestionDegree(rs.getString("parkingCongestionDegree"));
				p.setParkingOccupiedSpace(rs.getString("parkingOccupiedSpace"));
				p.setParkingTotalSpace(rs.getString("parkingTotalSpace"));
				p.setSysGetdate(rs.getString("sysGetdate"));
				p.setSysGettime(rs.getString("sysGettime"));
				datas.add(p);
			}
			return datas;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
