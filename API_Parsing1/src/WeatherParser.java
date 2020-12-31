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

public class WeatherParser {

	String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul&useSSL=false";

	static Gson gson = new Gson();

	java.sql.Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	java.sql.Statement st;

	int r;

	WeatherInfo weatherInfo;

	LinkedList<WeatherInfo> weatherInfoList = new LinkedList<WeatherInfo>();
	int count = 1;
	int countSQL = 0;
	
	String courseid[]= {"26","339","340","427","386","387","388","389","390","391","392","393","394","313","314","316","317","318","319","167","168","192","204","227","236"};
	

	public WeatherParser() throws SQLException {

		LinkedList<WeatherInfo> weatherInfoList = null;
		try {
			weatherInfoList = weatherParsing();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectDB();

		for (WeatherInfo li : weatherInfoList) {
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

			String sql = "DROP TABLE if exists tourlocationweather";
			r = st.executeUpdate(sql);

			sql = "CREATE TABLE tourlocationweather(\r\n" + "id INT NOT NULL,\r\n"
					+ "courseAreaId VARCHAR(45) NOT NULL,\r\n" + "courseId VARCHAR(45) NOT NULL,\r\n"+ "courseAreaName VARCHAR(45) NOT NULL,\r\n"
					+ "courseName VARCHAR(45) NOT NULL,\r\n" + "pop VARCHAR(45) NOT NULL,\r\n"
					+ "rhm  VARCHAR(45) NOT NULL,\r\n" + "sky VARCHAR(45) NOT NULL,\r\n"
					+ "spotAreaName VARCHAR(45) NOT NULL,\r\n" + "spotName VARCHAR(45) NOT NULL,\r\n"
					+ "thema  VARCHAR(45) NOT NULL,\r\n" + "tm VARCHAR(45) NOT NULL\r\n" + ")";
			r = st.executeUpdate(sql);

			sql = "ALTER TABLE TourLocationWeather \r\n" + "  ADD CONSTRAINT id_pk PRIMARY KEY (id)\r\n";
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

	public void insertAPInfo(WeatherInfo weatherInfo) {
		String sql = "insert into tourlocationweather(id, courseAreaId,courseAreaName, courseId, courseName, pop, rhm, sky, spotAreaName , spotName, thema, tm)  values(?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			if (pstmt == null)
				System.out.println("!");

			pstmt.setInt(1, weatherInfo.getId());
			pstmt.setString(2, weatherInfo.getCourseAreaId());
			pstmt.setString(3, weatherInfo.getCourseAreaName());
			pstmt.setString(4, weatherInfo.getCourseId());
			pstmt.setString(5, weatherInfo.getCourseName());
			pstmt.setString(6, weatherInfo.getPop());
			pstmt.setString(7, weatherInfo.getRhm());
			pstmt.setString(8, weatherInfo.getSky());
			pstmt.setString(9, weatherInfo.getSpotAreaName());
			pstmt.setString(10, weatherInfo.getSpotName());
			pstmt.setString(11, weatherInfo.getThema());
			pstmt.setString(12, weatherInfo.getTm());

			pstmt.executeUpdate();
			countSQL++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LinkedList<WeatherInfo> weatherParsing() throws IOException {

		for (int i = 0; i < 1; i++) {
			
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/AsosDalyInfoService/getWthrDataList"); /*URL*/
	        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=서비스키"); /*Service Key*/
	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
	        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)*/
	        urlBuilder.append("&" + URLEncoder.encode("dataCd","UTF-8") + "=" + URLEncoder.encode("ASOS", "UTF-8")); /*자료 분류 코드*/
	        urlBuilder.append("&" + URLEncoder.encode("dateCd","UTF-8") + "=" + URLEncoder.encode("DAY", "UTF-8")); /*날짜 분류 코드*/
	        urlBuilder.append("&" + URLEncoder.encode("startDt","UTF-8") + "=" + URLEncoder.encode("20100101", "UTF-8")); /*조회 기간 시작일*/
	        urlBuilder.append("&" + URLEncoder.encode("endDt","UTF-8") + "=" + URLEncoder.encode("20100601", "UTF-8")); /*조회 기간 종료일*/
	        urlBuilder.append("&" + URLEncoder.encode("stnIds","UTF-8") + "=" + URLEncoder.encode("108", "UTF-8")); /*종관기상관측 지점 번호*/
	        URL url = new URL(urlBuilder.toString());
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-type", "application/json");
	        System.out.println("Response code: " + conn.getResponseCode());
	        BufferedReader rd;
	        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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

			/*Gson gson = new Gson();
			
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(sb.toString());
			JsonObject object = element.getAsJsonObject();
			object = (JsonObject) object.get("response");
			if(object == null)
            {
            	continue;
            }
			object = (JsonObject) object.get("body");
			if(object == null)
            {
            	continue;
            }
			object = (JsonObject) object.get("items");
			JsonArray array = object.getAsJsonArray("item");
			for (int j = 0; j < array.size(); j++) {
				object=(JsonObject) array.get(j);
				if(object == null)
	            {
	            	continue;
	            }
				WeatherInfo weatherInfo = gson.fromJson(object, WeatherInfo.class);
				weatherInfo.setId(count);
				weatherInfo.setSky(weatherInfo.getSky());
				weatherInfoList.add(weatherInfo);
				count++;
			}*/
		}
		return weatherInfoList;
	}

	public static void main(String[] args) throws IOException, SQLException {
		new WeatherParser();
	}

}