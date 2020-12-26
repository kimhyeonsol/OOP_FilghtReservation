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


public class Parser {
	
	static int index=0;
	static String[][] realOutput = new String[20][2];
	 
    public static void main(String[] args) throws IOException {
    	
    	int n=5;
    	String[] airport = {"NAARKSI", "NAARKSS", "NAARKPC", "NAARKTN", "NAARKPK"};
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
           	 	urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=9XM100EpBVaMTakx00Mzeq3pGlcrD6RKcvnx9lP7%2B39TonkVG21ZgXt3Bz9DO99royEYXc%2BKVfbvNZ58FWjH1Q%3D%3D"); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
                urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
                urlBuilder.append("&" + URLEncoder.encode("depAirportId","UTF-8") + "=" + URLEncoder.encode(realOutput[airportIdx][0], "UTF-8")); /*출발공항ID*/
                urlBuilder.append("&" + URLEncoder.encode("arrAirportId","UTF-8") + "=" + URLEncoder.encode(realOutput[airportIdx][1], "UTF-8")); /*도착공항ID*/
                urlBuilder.append("&" + URLEncoder.encode("depPlandTime","UTF-8") + "=" + URLEncoder.encode(depTime[i], "UTF-8")); /*출발일*/
                urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); 
                stringList.add(urlBuilder);
        	}
        	 
        }
        
        System.out.println(stringList.size());
//        for(int i=0; i<stringList.size(); i++)
//        	System.out.println(stringList.get(i));
        
        
        //stringList.size()
        for(int i=0; i<stringList.size(); i++) {
//        	System.out.println(stringList.get(i));
        	URL url = new URL(stringList.get(i).toString());
        	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
//            System.out.println("Response code: " + conn.getResponseCode());
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
            
            try {
            	
                JSONParser jsonParser = new JSONParser();
                //System.out.println("제발   " + sb.toString());
                JSONObject jsonObj = (JSONObject) jsonParser.parse(sb.toString());
//                System.out.println(jsonObj);
                jsonObj = (JSONObject) jsonObj.get("response");
                jsonObj = (JSONObject) jsonObj.get("body");
                //System.out.println("!!!!!!!!!!!!" + jsonObj.get("items"));
                
                if(jsonObj.get("items").equals("")==true)
                	continue;
                jsonObj = (JSONObject) jsonObj.get("items");
                
                JSONArray memberArray;
                try {
                	memberArray = (JSONArray) jsonObj.get("item"); // data: [ {...}, {...} ]
                } catch(ClassCastException e) {
                	memberArray  = new JSONArray();
                	memberArray.add((JSONObject) jsonObj.get("item"));
                	
//                	JSONArray memberArray = (JSONArray) [jsonObj.get("item")]; // data: [ {...}, {...} ]
                }
                // data: {} -> data['airlineNm']
                // data: [{}]
                //System.out.println("=====Members=====");
                
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
///////////////////////////////////////////////////////////////////////////////////        
        
        
//        StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/DmstcFlightNvgInfoService/getFlightOpratInfoList"); /*URL*/
//        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=9XM100EpBVaMTakx00Mzeq3pGlcrD6RKcvnx9lP7%2B39TonkVG21ZgXt3Bz9DO99royEYXc%2BKVfbvNZ58FWjH1Q%3D%3D"); /*Service Key*/
//        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
//        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
//        urlBuilder.append("&" + URLEncoder.encode("depAirportId","UTF-8") + "=" + URLEncoder.encode("NAARKJJ", "UTF-8")); /*출발공항ID*/
//        urlBuilder.append("&" + URLEncoder.encode("arrAirportId","UTF-8") + "=" + URLEncoder.encode("NAARKPC", "UTF-8")); /*도착공항ID*/
//        urlBuilder.append("&" + URLEncoder.encode("depPlandTime","UTF-8") + "=" + URLEncoder.encode("20201225", "UTF-8")); /*출발일*/
//        //urlBuilder.append("&" + URLEncoder.encode("airlineId","UTF-8") + "=" + URLEncoder.encode("AAR", "UTF-8")); /*항공사ID*/
//        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); 
//        URL url = new URL(urlBuilder.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//        System.out.println("Response code: " + conn.getResponseCode());
//        BufferedReader rd;
//        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//        }
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            sb.append(line);
//        }
//        rd.close();
//        conn.disconnect();
        
        
//        String jsonStr = "{\"members\":["
//                + "{\"name\":\"홍길동\","
//                + "\"email\":\"gildong@hong.com\","
//                + "\"age\":\"25\""
//                + "},"
//                + "{\"name\":\"홍길서\","
//                + "\"email\":\"gilseo@hong.com\","
//                + "\"age\":\"23\""
//                + "}]}";

     	
     	
//        try {
//        	
//       
//        	
//            JSONParser jsonParser = new JSONParser();
//            JSONObject jsonObj = (JSONObject) jsonParser.parse(sb.toString());
//            System.out.println(jsonObj);
//            jsonObj = (JSONObject) jsonObj.get("response");
//            jsonObj = (JSONObject) jsonObj.get("body");
//            jsonObj = (JSONObject) jsonObj.get("items");
//            JSONArray memberArray = (JSONArray) jsonObj.get("item");
//
//            //System.out.println("=====Members=====");
//            
//            for(int i=0 ; i<memberArray.size() ; i++){
//            	Info information = new Info();
//            	
//                JSONObject tempObj = (JSONObject) memberArray.get(i);
//                Object chargePt = tempObj.get("economyCharge");
//                if(chargePt==null) {
//                	continue;
//                }
//                Object chargePt2 = tempObj.get("prestigeCharge");
//                int economyCharge = Integer.parseInt(chargePt.toString());
//                //int prestigeCharge = Integer.parseInt(chargePt2.toString());
//                
//                information.setAirlineNm((String)tempObj.get("airlineNm"));
//                information.setArrAirportNm((String)tempObj.get("arrAirportNm"));
//                information.setArrPlandTime(String.valueOf(tempObj.get("arrPlandTime")));
//                information.setDepAirportNm((String)tempObj.get("depAirportNm"));
//                information.setDepPlandTime(String.valueOf(tempObj.get("depPlandTime")));
//                information.setEconomyCharge(Integer.parseInt(chargePt.toString()));
//                information.setPrestigeCharge(Integer.parseInt(chargePt2.toString()));
//                
//                list.add(information);
//
//            }
//
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
        for(Info i:list)
        {
        	System.out.println("항공사 "+i.getAirlineNm());
          System.out.println("출발 공항 "+i.getArrAirportNm());
          System.out.println("출발 시간 "+i.getArrPlandTime());
          System.out.println("도착 공항 "+i.getDepAirportNm());
          System.out.println("도착 시간 "+i.getDepPlandTime());
          System.out.println("economyCharge "+i.getEconomyCharge());
          System.out.println("prestigeCharge "+i.getPrestigeCharge());
          
          System.out.println("----------------------------");
      }
        
        
        
       // System.out.println(sb.toString());
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
    /////////////////////////////////////////////////////////////////////
    
    
}

