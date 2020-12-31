package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class AirLineDAO {

	String jdbcDriver = "com.mysql.cj.jdbc.Driver";
//	String jdbcDriver = "com.mysql.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://localhost:3306/airplanereservation?&serverTimezone=Asia/Seoul&useSSL=false";
	Connection conn;

	PreparedStatement pstmt;
	ResultSet rs;

	Vector<String> items = null;
	String sql;

	public AirLineDAO() {
		connectDB();
	}

	public void connectDB() {
		try {
			Class.forName(jdbcDriver);

			conn = DriverManager.getConnection(jdbcUrl, "root", "111111");
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

	// 전체 가져오기
	public ArrayList<AirLine> getAllALInfo() throws SQLException {
		// connectDB();
		sql = "select * from airlineinfo";

		ArrayList<AirLine> datas = new ArrayList<AirLine>();
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			AirLine p = new AirLine();
			p.setID(rs.getInt("ID"));
			p.setAirLineNm(rs.getString("airLineNm"));
			p.setArrAirportNm(rs.getString("arrAirportNm"));
			p.setArrPlandTime(rs.getString("arrPlandTime"));
			p.setDepAirportNm(rs.getString("depAirportNm"));
			p.setDepPlandTime(rs.getString("depPlandTime"));
			p.setEconomyCharge(rs.getInt("economyCharge"));
			p.setPrestigeCharge(rs.getInt("prestigeCharge"));
			datas.add(p);
			// throws SQLException
		}

		return datas;
	}

	// ID에 해당하는 정보 가져오기
	public AirLine getALInfo(int ID) {
		try {
			sql = "select * from airlineinfo where ID = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, String.valueOf(ID));
			rs = pstmt.executeQuery();
			
			AirLine p = new AirLine();
			rs.next();
			p.setID(rs.getInt("ID"));
			p.setAirLineNm(rs.getString("airLineNm"));
			p.setArrAirportNm(rs.getString("arrAirportNm"));
			p.setArrPlandTime(rs.getString("arrPlandTime"));
			p.setDepAirportNm(rs.getString("depAirportNm"));
			p.setDepPlandTime(rs.getString("depPlandTime"));
			p.setEconomyCharge(rs.getInt("economyCharge"));
			p.setPrestigeCharge(rs.getInt("prestigeCharge"));
			return p;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public ArrayList<AirLine> getALInfoByChoice(String depAirportNm, String arrAirportNm, String date) throws SQLException {
		// connectDB();
		sql = "select * from airlineinfo where arrAirportNm = ? and depAirportNm = ? and depPlandTime like ?";
		
		ArrayList<AirLine> datas = new ArrayList<AirLine>();
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, arrAirportNm);
		pstmt.setString(2, depAirportNm);
		pstmt.setString(3, date+"%");
		System.out.println(pstmt);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			AirLine p = new AirLine();
			p.setID(rs.getInt("ID"));
			p.setAirLineNm(rs.getString("airLineNm"));
			p.setArrAirportNm(rs.getString("arrAirportNm"));
			p.setArrPlandTime(rs.getString("arrPlandTime"));
			p.setDepAirportNm(rs.getString("depAirportNm"));
			p.setDepPlandTime(rs.getString("depPlandTime"));
			p.setEconomyCharge(rs.getInt("economyCharge"));
			p.setPrestigeCharge(rs.getInt("prestigeCharge"));
			datas.add(p);
			System.out.println(p.getID());
			// throws SQLException
		}

		return datas;
	}
	
	public Vector<String> getItems() {
		Vector<String> arr = new Vector<String>();
		try {
			sql = "select * from airlineinfo";
			pstmt = conn.prepareStatement(sql);

			ResultSet sucess = pstmt.executeQuery();
			while (sucess.next()) {
				arr.add(sucess.getString("ID"));
			}
			return arr;
		} catch (SQLException e) {
		}
		return arr;
	}

	public int updateALInfo(AirLine p) {
		sql = "UPDATE airlineinfo SET arrPlandTime = ?, depPlandTime = ?, economyCharge = ?, prestigeCharge = ? WHERE ID = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, p.getArrPlandTime());
			pstmt.setString(2, String.valueOf(p.getDepPlandTime()));
			pstmt.setString(3, String.valueOf(p.getEconomyCharge()));
			pstmt.setString(4, String.valueOf(p.getPrestigeCharge()));
			pstmt.setString(5, String.valueOf(p.getID()));
			System.out.println(pstmt);

			int r = pstmt.executeUpdate();
			if (r > 0)
				System.out.println(p.getID() + "update success");
			else
				System.out.println(p.getID() + " update fail");

			return r;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int addALInfo(AirLine p) {

		try {
			sql = "insert into airlineinfo values(0, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, p.getAirLineNm());
			pstmt.setString(2, p.getArrAirportNm());
			pstmt.setString(3, p.getArrPlandTime());
			pstmt.setString(4, p.getDepAirportNm());
			pstmt.setString(5, p.getDepPlandTime());
			pstmt.setInt(6, p.getEconomyCharge());
			pstmt.setInt(7, p.getEconomyCharge());

			int r = pstmt.executeUpdate();
			if (r > 0) {
				System.out.println(p.getAirLineNm() + " insert success");
			}

			else
				System.out.println(p.getAirLineNm() + " insert failure");

			return r;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int delALInfo(int ID) {

		try {
			sql = "delete from airlineinfo where ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ID);

			int r = pstmt.executeUpdate();

			if (r > 0)
				System.out.println(ID + " delete success");
			else
				System.out.println(ID + " delete failure");

			return r;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
}