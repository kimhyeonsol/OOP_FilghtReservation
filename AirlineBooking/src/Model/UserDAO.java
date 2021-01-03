package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;

public class UserDAO {

	String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	//String jdbcDriver = "com.mysql.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://localhost:3306/airplanereservation?&serverTimezone=Asia/Seoul&useSSL=false";
	Connection conn;

	PreparedStatement pstmt;
	ResultSet rs;

	Vector<String> items = null;
	String sql;

	public UserDAO() {
		try {
			connectDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void connectDB() throws SQLException {
		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, "root", "111111");
			System.out.println("DB 연결");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void closeDB() {
		try {
			pstmt.close();
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<User> getAll() throws SQLException {
		sql = "select * from user";

		ArrayList<User> datas = new ArrayList<User>();

		items = new Vector<String>();
		items.add("전체");

		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			User p = new User();
			p.setID(rs.getString("ID"));
			p.setName(rs.getString("name"));
			p.setPw(rs.getString("pw"));
			p.setEmail(rs.getString("email"));
			p.setBirth(rs.getString("birth"));
			p.setPhone(rs.getString("phone"));
			datas.add(p);
			items.add(String.valueOf(rs.getString("ID")));
		}

		return datas;

	}

	public User getUser(String ID) {
		sql = "select * from user where ID = ?";
		User u = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			rs = pstmt.executeQuery();

			rs.next();
			u = new User();

			u.setID(rs.getString("ID"));
			u.setName(rs.getString("name"));
			u.setPw(rs.getString("pw"));
			u.setEmail(rs.getString("email"));
			u.setBirth(rs.getString("birth"));
			u.setPhone(rs.getString("phone"));
			
		} catch (SQLException e) {
			//e.printStackTrace();
			u=null;
		}
		return u;
	}

	public boolean newUser(User u) {
		sql = "insert into user(ID, name, pw, email, birth, phone) values(?,?,?,?,?,?)";

		try {
			pstmt = conn.prepareStatement(sql);

			int i = 1;
			pstmt.setString(i++, u.getID());
			pstmt.setString(i++, u.getName());
			pstmt.setString(i++, u.getPw());
			pstmt.setString(i++, u.getEmail());
			pstmt.setString(i++, u.getBirth());
			pstmt.setString(i++, u.getPhone());
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateUser(User u) {
		sql = "update user set name = ?, pw = ?, email = ?, birth = ?, phone = ? where ID = ?";

		try {
			pstmt = conn.prepareStatement(sql);

			int i = 1;
			pstmt.setString(i++, u.getName());
			pstmt.setString(i++, u.getPw());
			pstmt.setString(i++, u.getEmail());
			pstmt.setString(i++, u.getBirth());
			pstmt.setString(i++, u.getPhone());
			pstmt.setString(i++, u.getID());
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int deleteUser(String ID) {
		sql = "delete from user where ID = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			int r = pstmt.executeUpdate();

			if (r > 0)
				System.out.println(ID + " delete success");
			else
				System.out.println(ID + " delete failure");
			
			return r;
			
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return 0;
	}

	public Vector<String> getItems() {
		return items;
	}

}
