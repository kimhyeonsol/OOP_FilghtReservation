package Model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.sun.jdi.connect.spi.Connection;

public class Parser extends Conf{
	
	static int count=0;
	static int index=0;
	static String[][] realOutput = new String[20][2];
	java.sql.Statement st;
	int r;
	   
	   Scanner scanner=new Scanner(System.in);
	
	public Parser() throws SQLException {
		super();
		_schemaName = "project";
		ArrayList<Info> list = null;
		try {
			list = parsing();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectDB();
		//////////////////////////////
		
		
		st = conn.createStatement();
		
		//airlineinfo 테이블 생성
		sql = "CREATE TABLE if not exists airlineinfo(\r\n" + "ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\r\n"
				+ "airLineNm VARCHAR(45) NOT NULL,\r\n" + "arrAirportNm VARCHAR(45) NOT NULL,\r\n"
				+ "arrPlandTime VARCHAR(12) NOT NULL,\r\n" + "depAirportNm VARCHAR(45) NOT NULL,\r\n"
				+ "depPlandTime VARCHAR(12) NOT NULL,\r\n" + "economyCharge INT NULL,\r\n"
				+ "prestigeCharge INT NULL\r\n" +  ")";
		r = st.executeUpdate(sql);
	
		
		//user table 생성
		sql = "CREATE TABLE if not exists user(\r\n" + "ID VARCHAR(45) PRIMARY KEY NOT NULL,\r\n"
				+ "name VARCHAR(45) NOT NULL,\r\n" + "pw VARCHAR(45) NOT NULL,\r\n"
				+ "email VARCHAR(45) NOT NULL,\r\n" + "birth VARCHAR(8) NOT NULL,\r\n"
				+ "phone VARCHAR(11) NOT NULL\r\n"  + ")";
		r = st.executeUpdate(sql);
		
		//reservation table 생성
		sql = "CREATE TABLE if not exists reservation(\r\n" + "ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\r\n"
				+ "user VARCHAR(45) NOT NULL,\r\n" + "info INT NOT NULL,\r\n"
				+ "seatNum INT NULL,\r\n"
				+"  FOREIGN KEY (user)\r\n" 
				+"  REFERENCES user (ID)\r\n"
				+"  ON DELETE CASCADE\r\n"
				+"  ON UPDATE NO ACTION, "
				+"  FOREIGN KEY (info)\r\n"
				+ "  REFERENCES airlineinfo (ID)\r\n"
				+"  ON DELETE CASCADE\r\n" 
				+"  ON UPDATE NO ACTION"
				+")";
		r = st.executeUpdate(sql);
		
		

		for(Info li:list) {
			insertAPInfo(li);
			
		}
		sql = "update airlineinfo set prestigeCharge = '125000' where prestigeCharge='0' ";
		r = st.executeUpdate(sql);
		
		System.out.println("r의 값   "+ r);
		System.out.println(count);
		closeDB();
	}
   
	public void insertAPInfo(Info in) {
		String sql = "insert into airlineinfo(airLineNm, arrAirportNm, arrPlandTime, depAirportNm, depPlandTime, economyCharge,prestigeCharge)  values(?,?,?,?,?,?,?)";
		
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			if(pstmt==null)
				System.out.println("!");
			pstmt.setString(1, in.getAirlineNm());
			pstmt.setString(2, in.getArrAirportNm());
			pstmt.setString(3, in.getArrPlandTime());
			pstmt.setString(4, in.depAirportNm);
			pstmt.setString(5, in.depPlandTime);
			pstmt.setInt(6, in.getEconomyCharge());
			pstmt.setInt(7, in.getPrestigeCharge());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		count++;
	}
	
    
    public ArrayList<Info> parsing() throws IOException{

    	int n=5;
    	String[] airport = {"NAARKSI", "NAARKSS", 	"NAARKPC", "NAARKTN", "NAARKPK"};
        String[] output = new String[n];
        boolean[] visited = new boolean[n];
        ArrayList<Info> list = new ArrayList<Info>();
       
        //출발공항-도착공항 경우의수(순열)
        perm(airport, output, visited, 0, n, 2);
        
        String[] depTime = {"20201225", "20201226" , "20201227", "20201228", "20201229", "20201230"
        					, "20201231", "20210101", "20210102", "20210103", "20210104", "20210105"
        					, "20210106", "20210107", "20210108", "20210109", "20210110", "20210111"
        					, "20210112", "20210113", "20210114", "20210115", "20210116", "20210117"
        					, "20210118", "20210119", "20210120", "20210121", "20210122", "20210123", "20210124"};
    	
        ArrayList<StringBuilder> stringList = new ArrayList<StringBuilder>();
      
        for(int i=0; i<depTime.length; i++) {
        	for(int airportIdx=0; airportIdx<20; airportIdx++) {
        		StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/DmstcFlightNvgInfoService/getFlightOpratInfoList"); /*URL*/
           	 	urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=FpPWO7Gz4nzdNAPaCzneE6y9T5SZSVpqsbPjpWPXd8sGGLoZz5%2FrkNpLZ%2BrmwDmrDL5hc38x8xKv08sq%2BzUcDw%3D%3D"); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
                urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
                urlBuilder.append("&" + URLEncoder.encode("depAirportId","UTF-8") + "=" + URLEncoder.encode(realOutput[airportIdx][0], "UTF-8")); /*출발공항ID*/
                urlBuilder.append("&" + URLEncoder.encode("arrAirportId","UTF-8") + "=" + URLEncoder.encode(realOutput[airportIdx][1], "UTF-8")); /*도착공항ID*/
                urlBuilder.append("&" + URLEncoder.encode("depPlandTime","UTF-8") + "=" + URLEncoder.encode(depTime[i], "UTF-8")); /*출발일*/
                urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); 
                stringList.add(urlBuilder);
        	}
        	 
        }
        

        for(int i=0; i<stringList.size(); i++) {
        	URL url = new URL(stringList.get(i).toString());
        	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

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
            
            try {
            	
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObj = (JSONObject) jsonParser.parse(sb.toString());
                
                jsonObj = (JSONObject) jsonObj.get("response");
                jsonObj = (JSONObject) jsonObj.get("body");
                
               
                
                if(jsonObj == null)
                {
                	continue;
                }
                	
                if(jsonObj.get("items").equals("")==true)
                	continue;
                
                jsonObj = (JSONObject) jsonObj.get("items");
                
                JSONArray memberArray;
                try {
                	memberArray = (JSONArray) jsonObj.get("item"); // data: [ {...}, {...} ]
                } catch(ClassCastException e) {
                	memberArray  = new JSONArray();
                	memberArray.add((JSONObject) jsonObj.get("item"));
                	
                }
                
                for(int ii=0 ; ii<memberArray.size() ; ii++){
                	
                	Info information = new Info();
                	
                    JSONObject tempObj = (JSONObject) memberArray.get(ii);
                    Object chargePt = tempObj.get("economyCharge");
                    if(chargePt==null) {
                    	continue;
                    }
                    Object chargePt2 = tempObj.get("prestigeCharge");
                    int economyCharge = Integer.parseInt(chargePt.toString());
                    //int prestigeCharge = Integer.parseInt(chargePt2.toString());
                    
                    information.setAirlineNm((String)tempObj.get("airlineNm"));
                    information.setArrAirportNm((String)tempObj.get("arrAirportNm"));
                    information.setArrPlandTime(String.valueOf(tempObj.get("arrPlandTime")));
                    information.setDepAirportNm((String)tempObj.get("depAirportNm"));
                    information.setDepPlandTime(String.valueOf(tempObj.get("depPlandTime")));
                    information.setEconomyCharge(Integer.parseInt(chargePt.toString()));
                    information.setPrestigeCharge(Integer.parseInt(chargePt2.toString()));
                    
                    list.add(information);

                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
        return list;
    }
    
    
    //////////////////////////////////////////////////////////////////////////////
    //permutation method
    static void perm(String[] arr, String[] output, boolean[] visited, int depth, int n, int r) {
        if (depth == r) {
            print(output, r);
            return;
        }

        for (int i = 0; i < n; i++) {
            if (visited[i] != true) {
                visited[i] = true;
                output[depth] = arr[i];
                perm(arr, output, visited, depth + 1, n, r);
                visited[i] = false;
            }
        }
    }

    
    static void print(String[] arr, int r) {
    	
        for (int i = 0; i < r; i++)
        {
        	realOutput[index][i]=arr[i];

        }
        index++;

    }
  
    
}

