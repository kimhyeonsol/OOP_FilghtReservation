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
   
   String courseid[]= {"26","339","340","427","386","387","389","390","313","314","316","319","167","168","192","204"};
   

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

      for (int i = 0; i < courseid.length; i++) {
         StringBuilder urlBuilder = new StringBuilder(
               "http://apis.data.go.kr/1360000/TourStnInfoService/getTourStnVilageFcst"); /* URL */
         urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8")
               + "=37UrltMraQgPIfceu%2B9Pc4oGX4ZmSwuWOPVVYxWgN%2FuU8QJCzUOhCPEafXtilZ2dm42cQuwlUUZCaarom%2Fy0iA%3D%3D"); /*                                                                                           */
         urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "="
               + URLEncoder.encode(Integer.toString(1), "UTF-8")); /* 페이지번호 */
         urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
               + URLEncoder.encode("300", "UTF-8")); /* 한 페이지 결과 수 */
         urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "="
               + URLEncoder.encode("JSON", "UTF-8")); /* 요청자료형식(XML/JSON) */
         urlBuilder.append("&" + URLEncoder.encode("CURRENT_DATE", "UTF-8") + "="
               + URLEncoder.encode("2020122901", "UTF-8")); /* 2016-12-01 01시부터 조회 */
         urlBuilder.append("&" + URLEncoder.encode("HOUR", "UTF-8") + "="
               + URLEncoder.encode("24", "UTF-8")); /* CURRENT_DATE부터 24시간 후까지의 자료 호출 */
         urlBuilder.append("&" + URLEncoder.encode("COURSE_ID", "UTF-8") + "="
               + URLEncoder.encode(courseid[i], "UTF-8")); /* 관광 코스ID */

         URL url = new URL(urlBuilder.toString());
         HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();

         httpconn.setRequestMethod("GET");
         httpconn.setRequestProperty("Content-type", "application/json");

         

         BufferedReader rd;
         if (httpconn.getResponseCode() >= 200 && httpconn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(httpconn.getInputStream(), "UTF-8"));
         } else {
            rd = new BufferedReader(new InputStreamReader(httpconn.getErrorStream(), "UTF-8"));
         }

         StringBuilder sb = new StringBuilder();
         String line;
         while ((line = rd.readLine()) != null) {
            sb.append(line);
         }
         rd.close();
         httpconn.disconnect();
         
         System.out.println(sb.toString());

         Gson gson = new Gson();
         
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
         }
      }
      return weatherInfoList;
   }

   public static void main(String[] args) throws IOException, SQLException {
      new WeatherParser();
   }

}