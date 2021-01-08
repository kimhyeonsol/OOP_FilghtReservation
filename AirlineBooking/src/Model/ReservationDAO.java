package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class ReservationDAO extends Conf{

	public ReservationDAO() {
		super();
		_schemaName = "project";
		try {
			connectDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("ReservationDAO DB 연결 실패!");
		}
	}

	public ArrayList<Reservation> getAll() throws SQLException {
		sql = "select * from reservation";

		ArrayList<Reservation> datas = new ArrayList<Reservation>();

		items = new Vector<String>();
		items.add("전체");

		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			Reservation r = new Reservation();
			r.setID(rs.getInt("ID"));

			r.setUser(rs.getString("user"));
			r.setInfo(rs.getInt("info"));
			r.setSeatNum(rs.getInt("seatNum"));
			datas.add(r);
		}

		return datas;

	}

	public Reservation getReservation(int ID) throws SQLException {
		sql = "select * from reservation where ID = ?";
		Reservation r = null;
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, ID);
		rs = pstmt.executeQuery();

		rs.next();

		r = new Reservation();
		r.setID(rs.getInt("ID"));
		r.setUser(rs.getString("user"));
		r.setInfo(rs.getInt("info"));
		r.setSeatNum(rs.getInt("seatNum"));

		return r;
	}

	public boolean checkReservationByInfoWithSeat(int info, int seatNum) {
		Reservation r = null;
		try {
			sql = "select * from reservation where info = ? and seatNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, info);
			pstmt.setInt(2, seatNum);
			rs = pstmt.executeQuery();
			rs.next();
			r = new Reservation();
			r.setID(rs.getInt("ID"));
			r.setUser(rs.getString("user"));
			r.setInfo(rs.getInt("info"));
			r.setSeatNum(rs.getInt("seatNum"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
			System.out.println("쿼리문 오류");
			return false;
		}
		return true;
	}

	public ArrayList<Reservation> getReservationListByUser(String UserID) throws SQLException {
		sql = "select * from reservation where user = ?";

		ArrayList<Reservation> datas = new ArrayList<Reservation>();

		items = new Vector<String>();
		items.add("전체");

		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, UserID);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			Reservation r = new Reservation();
			r.setID(rs.getInt("ID"));

			r.setUser(rs.getString("user"));
			r.setInfo(rs.getInt("info"));
			r.setSeatNum(rs.getInt("seatNum"));
			datas.add(r);
//         items.add(String.valueOf(rs.getInt("ID")));
		}

		return datas;

	}

	public ArrayList<Reservation> getReservationListByALInfo(int ALInfoID) throws SQLException {
		sql = "select * from reservation where info = ?";

		ArrayList<Reservation> datas = new ArrayList<Reservation>();

		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, ALInfoID);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			Reservation r = new Reservation();
			r.setID(rs.getInt("ID"));

			r.setUser(rs.getString("user"));
			r.setInfo(rs.getInt("info"));
			r.setSeatNum(rs.getInt("seatNum"));
			datas.add(r);
//         items.add(String.valueOf(rs.getInt("ID")));
		}

		return datas;
	}

	public boolean newReservation(Reservation r) {
		sql = "insert into reservation(user, info, seatNum) " + "values(?,?,?)";

		try {
			pstmt = conn.prepareStatement(sql);
			int i = 1;
			pstmt.setString(i++, r.getUser());
			pstmt.setInt(i++, r.getInfo());
			pstmt.setInt(i++, r.getSeatNum());
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
//			e.printStackTrace();
			return false;
		}
	}

	public boolean updateReservation(Reservation p) {
		sql = "update reservation set user = ?, info = ?, seatNum = ? where ID = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			int i = 1;
			pstmt.setString(i++, p.getUser());
			pstmt.setInt(i++, p.getInfo());
			pstmt.setInt(i++, p.getSeatNum());
			pstmt.setInt(i++, p.getID());
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
//			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteReservation(int ID) {
		sql = "delete from reservation where ID = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ID);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
//			e.printStackTrace();
			return false;
		}
	}

	public Vector<String> getItems() {
		return items;
	}

}