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
	
	String jdbcDriver = "com.mysql.jdbc.Driver";
	//String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul&useSSL=false";

	static Gson gson = new Gson();

	java.sql.Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	java.sql.Statement st;

	int r;

	AirPortParkingLot airPortParkingLot;
	InCheonAirPortParkingLot inCheonAirPortParkingLot;

	LinkedList<AirPortParkingLot> airPortParkingLotInfoList = new LinkedList<AirPortParkingLot>();
	LinkedList<InCheonAirPortParkingLot> inCheonAirPortParkingLotList = new LinkedList<InCheonAirPortParkingLot>();

	String airPort[] = { "GMP", "PUS", "CJU", "TAE" };

	public AirPortParkingLotParser() throws SQLException {

		LinkedList<AirPortParkingLot> airPortParkingLotInfoList = null;
		try {
			airPortParkingLotInfoList = airPortParkingLotParsing();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LinkedList<InCheonAirPortParkingLot> inCheonAirPortParkingLotList = null;
		try {
			inCheonAirPortParkingLotList = incheonAirPortParkingLotParsing();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		connectDB();

		for (AirPortParkingLot li : airPortParkingLotInfoList) {
			insertAPInfo(li);
		}
		for (InCheonAirPortParkingLot li : inCheonAirPortParkingLotList) {
			insertInCheonAPInfo(li);
		}
		closeDB();
	}

	public void connectDB() throws SQLException {
		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, "madang", "madang");// check your username and pw
			st = conn.createStatement();

			String sql = "DROP TABLE if exists incheonairportlot";
			r = st.executeUpdate(sql);
			sql = "CREATE TABLE incheonairportlot(\r\n" + "floor VARCHAR(45) NOT NULL,\r\n"
					+ "parking VARCHAR(45) NOT NULL,\r\n" + "parkingarea VARCHAR(45) NOT NULL,\r\n"
					+ "datetm VARCHAR(45) NOT NULL\r\n" + ")";
			r = st.executeUpdate(sql);
			sql = "ALTER TABLE incheonairportlot \r\n" + "  ADD CONSTRAINT floor_pk PRIMARY KEY (floor)\r\n";
			r = st.executeUpdate(sql);

			////////////////////////////////////////////////////////////////////

			sql = "DROP TABLE if exists airportlot";
			r = st.executeUpdate(sql);

			sql = "CREATE TABLE airportlot(\r\n" + "airportEng VARCHAR(45) NOT NULL,\r\n"
					+ "airportKor VARCHAR(45) NOT NULL,\r\n" + "parkingAirportCodeName VARCHAR(45) NOT NULL,\r\n"
					+ "parkingCongestion VARCHAR(45) NOT NULL,\r\n"
					+ "parkingCongestionDegree VARCHAR(45) NOT NULL,\r\n"
					+ "parkingOccupiedSpace  VARCHAR(45) NOT NULL,\r\n" + "parkingTotalSpace VARCHAR(45) NOT NULL,\r\n"
					+ "sysGetdate VARCHAR(45) NOT NULL,\r\n" + "sysGettime VARCHAR(45) NOT NULL\r\n" + ")";
			r = st.executeUpdate(sql);

			sql = "ALTER TABLE airportlot \r\n"
					+ "  ADD CONSTRAINT parkingAirportCodeName_pk PRIMARY KEY (parkingAirportCodeName)\r\n";
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

	public void insertInCheonAPInfo(InCheonAirPortParkingLot inCheonAirPortParkingLot) {
		String sql = "insert into incheonairportlot( floor,parking, parkingarea, datetm)  values(?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inCheonAirPortParkingLot.getFloor());
			pstmt.setString(2, inCheonAirPortParkingLot.getParking());
			pstmt.setString(3, inCheonAirPortParkingLot.getParkingarea());
			pstmt.setString(4, inCheonAirPortParkingLot.getDatetm());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertAPInfo(AirPortParkingLot airPortParkingLot) {
		String sql = "insert into airportlot( airportEng,airportKor, parkingAirportCodeName, parkingCongestion, parkingCongestionDegree, parkingOccupiedSpace, parkingTotalSpace, sysGetdate , sysGettime)  values(?,?,?,?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, airPortParkingLot.getAirportEng());
			pstmt.setString(2, airPortParkingLot.getAirportKor());
			pstmt.setString(3, airPortParkingLot.getParkingAirportCodeName());
			pstmt.setString(4, airPortParkingLot.getParkingCongestion());
			pstmt.setString(5, airPortParkingLot.getParkingCongestionDegree());
			pstmt.setString(6, airPortParkingLot.getParkingOccupiedSpace());
			pstmt.setString(7, airPortParkingLot.getParkingTotalSpace());
			pstmt.setString(8, airPortParkingLot.getSysGetdate());
			pstmt.setString(9, airPortParkingLot.getSysGettime());
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LinkedList<InCheonAirPortParkingLot> incheonAirPortParkingLotParsing() throws IOException {

		for (int i = 0; i < 1; i++) {
			StringBuilder urlBuilder = new StringBuilder(
					"http://openapi.airport.kr/openapi/service/StatusOfParking/getTrackingParking"); /* URL */
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8")
					+ "=FpPWO7Gz4nzdNAPaCzneE6y9T5SZSVpqsbPjpWPXd8sGGLoZz5%2FrkNpLZ%2BrmwDmrDL5hc38x8xKv08sq%2BzUcDw%3D%3D"); /*
																																 * Service
																																 * Key
																																 */
			urlBuilder.append(
					"&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 페이지번호 */
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
					+ URLEncoder.encode("15", "UTF-8")); /* 한 페이지 결과 수 */
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
				InCheonAirPortParkingLot inCheonAirPortParkingLot = gson.fromJson(object,
						InCheonAirPortParkingLot.class);
				inCheonAirPortParkingLotList.add(inCheonAirPortParkingLot);
			}
		}
		return inCheonAirPortParkingLotList;
	}

	public LinkedList<AirPortParkingLot> airPortParkingLotParsing() throws IOException {

		for (int i = 0; i < 4; i++) {
			StringBuilder urlBuilder = new StringBuilder(
					"http://openapi.airport.co.kr/service/rest/AirportParkingCongestion/airportParkingCongestionRT"); /*
																														 * URL
																														 */
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8")
					+ "=FpPWO7Gz4nzdNAPaCzneE6y9T5SZSVpqsbPjpWPXd8sGGLoZz5%2FrkNpLZ%2BrmwDmrDL5hc38x8xKv08sq%2BzUcDw%3D%3D"); /*
																																 * Service
																																 * Key
																																 */
			urlBuilder.append(
					"&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 페이지번호 */
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
					+ URLEncoder.encode("10", "UTF-8")); /* 한 페이지 결과 수 */
			urlBuilder.append("&" + URLEncoder.encode("schAirportCode", "UTF-8") + "="
					+ URLEncoder.encode(airPort[i], "UTF-8")); /* 김포국제공항, 김해국제공항, 제주국제공항, 대구국제공항 */
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
				AirPortParkingLot airPortParkingLot = gson.fromJson(object, AirPortParkingLot.class);
				airPortParkingLotInfoList.add(airPortParkingLot);
			}
		}
		return airPortParkingLotInfoList;
	}

	public static void main(String[] args) throws IOException, SQLException {
		new AirPortParkingLotParser();
	}

}