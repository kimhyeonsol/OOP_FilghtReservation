package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Conf {

	String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	// String jdbcDriver = "com.mysql.jdbc.Driver";
	
	String jdbcUrl = "jdbc:mysql://localhost:3306/"; 
	String meta = "?&serverTimezone=Asia/Seoul&useSSL=false";
	Connection conn;

	PreparedStatement pstmt;
	ResultSet rs;

	Vector<String> items = null;
	String sql;

	String _userName, _userPw;
	String _schemaName;

	public Conf() {
		    _userName = "project"; // example
    		_userPw = "project";
	   }

	public void connectDB() throws SQLException {
		jdbcUrl += _schemaName + meta;
		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, _userName, _userPw);
			if (conn == null)
				System.out.println("conn is null");
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}

	public void closeDB() {
		try {
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}

}
