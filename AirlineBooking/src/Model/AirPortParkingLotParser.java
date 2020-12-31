package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AirPortParkingLotParser {

	String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul&useSSL=false";

	static Gson gson = new Gson();

	java.sql.Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	java.sql.Statement st;

	int r;

	AirPortParkingLotInfo airPortParkingLotInfo;
	
	LinkedList<AirPortParkingLotInfo> airPortParkingLotInfoList = new LinkedList<AirPortParkingLotInfo>();
	
	int count = 1;
	int countSQL = 0;
	String airPort[] = {"GMP", "PUS", "CJU", "TAE"}; 
	
	public AirPortParkingLotParser() throws SQLException {

		LinkedList<AirPortParkingLotInfo> airPortParkingLotInfoList = null;
		try {
			airPortParkingLotInfoList = airPortParkingLotParsing();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectDB();

		for (AirPortParkingLotInfo li : airPortParkingLotInfoList) {
			insertAPInfo(li);
		}

		System.out.println(count);
		System.out.println(countSQL);
		closeDB();
	}

	public void connectDB() throws SQLException {
		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, "madang", "madang");// check your username and pw
			st = conn.createStatement();

			String sql = "DROP TABLE if exists airportlot";
			r = st.executeUpdate(sql);

			sql = "CREATE TABLE airportlot(\r\n" + "id INT NOT NULL,\r\n"
					+ "airportEng VARCHAR(45) NOT NULL,\r\n" + "airportKor VARCHAR(45) NOT NULL,\r\n"
					+ "parkingAirportCodeName VARCHAR(45) NOT NULL,\r\n" + "parkingCongestion VARCHAR(45) NOT NULL,\r\n"
					+ "parkingCongestionDegree VARCHAR(45) NOT NULL,\r\n" + "parkingOccupiedSpace  VARCHAR(45) NOT NULL,\r\n"
					+ "parkingTotalSpace VARCHAR(45) NOT NULL,\r\n" + "sysGetdate VARCHAR(45) NOT NULL,\r\n"
					+ "sysGettime VARCHAR(45) NOT NULL\r\n" + ")";
			r = st.executeUpdate(sql);

			sql = "ALTER TABLE airportlot \r\n" + "  ADD CONSTRAINT id_pk PRIMARY KEY (id)\r\n";
			r = st.executeUpdate(sql);

			System.out.println("연결완료");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void closeDB() {
		try {
			pstmt.close();
			// rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertAPInfo(AirPortParkingLotInfo airPortParkingLotInfo) {
		String sql = "insert into airportlot(id, airportEng,airportKor, parkingAirportCodeName, parkingCongestion, parkingCongestionDegree, parkingOccupiedSpace, parkingTotalSpace, sysGetdate , sysGettime)  values(?,?,?,?,?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			if (pstmt == null)
				System.out.println("!");

			pstmt.setInt(1, airPortParkingLotInfo.getId());
			pstmt.setString(2, airPortParkingLotInfo.getAirportEng());
			pstmt.setString(3, airPortParkingLotInfo.getAirportKor());
			pstmt.setString(4, airPortParkingLotInfo.getParkingAirportCodeName());
			pstmt.setString(5, airPortParkingLotInfo.getParkingCongestion());
			pstmt.setString(6, airPortParkingLotInfo.getParkingCongestionDegree());
			pstmt.setString(7, airPortParkingLotInfo.getParkingOccupiedSpace());
			pstmt.setString(8, airPortParkingLotInfo.getParkingTotalSpace());
			pstmt.setString(9, airPortParkingLotInfo.getSysGetdate());
			pstmt.setString(10, airPortParkingLotInfo.getSysGettime());

			pstmt.executeUpdate();
			countSQL++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LinkedList<AirPortParkingLotInfo> airPortParkingLotParsing() throws IOException {

		for (int i = 0; i < 4; i++) {
			StringBuilder urlBuilder = new StringBuilder("http://openapi.airport.co.kr/service/rest/AirportParkingCongestion/airportParkingCongestionRT"); /*URL*/
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8")+ "=FpPWO7Gz4nzdNAPaCzneE6y9T5SZSVpqsbPjpWPXd8sGGLoZz5%2FrkNpLZ%2BrmwDmrDL5hc38x8xKv08sq%2BzUcDw%3D%3D"); /*Service Key*/
			urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 페이지번호 */
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="+ URLEncoder.encode("10", "UTF-8")); /* 한 페이지 결과 수 */
			urlBuilder.append("&" + URLEncoder.encode("schAirportCode", "UTF-8") + "=" + URLEncoder.encode(airPort[i],"UTF-8")); /* 김포국제공항, 김해국제공항, 제주국제공항, 대구국제공항 */
			urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");
			System.out.println("Response code: " + conn.getResponseCode());
			BufferedReader rd;
			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
			conn.disconnect();
			System.out.println(sb.toString());

			
			Gson gson = new Gson();

			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(sb.toString());
			JsonObject object = element.getAsJsonObject();
			object = (JsonObject) object.get("response");
			if (object == null) {
				continue;
			}
			object = (JsonObject) object.get("body");
			if (object == null) {
				continue;
			}
			object = (JsonObject) object.get("items");
			JsonArray array = object.getAsJsonArray("item");
			for (int j = 0; j < array.size(); j++) {
				object = (JsonObject) array.get(j);
				if (object == null) {
					continue;
				}
				AirPortParkingLotInfo airPortParkingLotInfo = gson.fromJson(object, AirPortParkingLotInfo.class);
				airPortParkingLotInfo.setId(count);
				airPortParkingLotInfoList.add(airPortParkingLotInfo);
				count++;
			}
		}
		return airPortParkingLotInfoList;
	}

	public static void main(String[] args) throws IOException, SQLException {
		new AirPortParkingLot();
	}

}