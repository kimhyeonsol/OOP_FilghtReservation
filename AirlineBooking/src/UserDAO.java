import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class UserDAO {

	String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://localhost/airplanereservation?&serverTimezone=Asia/Seoul&useSSL=false";
	Connection conn;

	PreparedStatement pstmt;
	ResultSet rs;

	Vector<String> items = null;
	String sql;

	public void connectDB() throws SQLException {
		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, "root", "111111");
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
		connectDB();
		sql = "select * from product";

		ArrayList<User> datas = new ArrayList<User>();

		items = new Vector<String>();
		items.add("전체");

		while (rs.next()) {
			User p = new User();
			p.setPrcode(rs.getInt("prcode"));
			p.setPrname(rs.getString("prname"));
			p.setPrice(rs.getInt("price"));
			p.setManufacture(rs.getString("manufacture"));
			datas.add(p);
			items.add(String.valueOf(rs.getInt("prcode")));
		}

		return datas;

	}

	public User getUser(int prcode) {
		sql = "select * from product where prcde = ?";
		User p = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prcode);
			rs = pstmt.executeQuery();

			rs.next();
			p = new User();
			p.setPrcode(rs.getInt("prcode"));
			p.setPrname(rs.getString("prname"));
			p.setPrice(rs.getInt("price"));
			p.setManufacture(rs.getString("manufacture"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}

	public boolean newUser(User p) {
		sql = "insert into product(prcode, prname, price, manufacture) " + "values(?, ?, ?, ?)";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, p.getPrcode());
			pstmt.setString(2, p.getPrname());
			pstmt.setInt(3, p.getPrice());
			pstmt.setString(4, p.getManufacture());
			pstmt.executeQuery();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateUser(User p) {
		sql = "update product set prname = ?, price = ?, manufacture = ? where prcode = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, p.getPrname());
			pstmt.setInt(2, p.getPrice());
			pstmt.setString(3, p.getManufacture());
			pstmt.setInt(4, p.getPrcode());
			pstmt.executeQuery();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteUser(int prcode) {
		sql = "delete from product where prcode = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prcode);
			pstmt.executeQuery();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Vector<String> getItems() {
		return items;
	}

}
