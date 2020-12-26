import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;


public class Parser {
    public static void main(String[] args) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/DmstcFlightNvgInfoService/getFlightOpratInfoList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=9XM100EpBVaMTakx00Mzeq3pGlcrD6RKcvnx9lP7%2B39TonkVG21ZgXt3Bz9DO99royEYXc%2BKVfbvNZ58FWjH1Q%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("depAirportId","UTF-8") + "=" + URLEncoder.encode("NAARKJJ", "UTF-8")); /*출발공항ID*/
        urlBuilder.append("&" + URLEncoder.encode("arrAirportId","UTF-8") + "=" + URLEncoder.encode("NAARKPC", "UTF-8")); /*도착공항ID*/
        urlBuilder.append("&" + URLEncoder.encode("depPlandTime","UTF-8") + "=" + URLEncoder.encode("20201225", "UTF-8")); /*출발일*/
        //urlBuilder.append("&" + URLEncoder.encode("airlineId","UTF-8") + "=" + URLEncoder.encode("AAR", "UTF-8")); /*항공사ID*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); 
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
        
        
//        String jsonStr = "{\"members\":["
//                + "{\"name\":\"홍길동\","
//                + "\"email\":\"gildong@hong.com\","
//                + "\"age\":\"25\""
//                + "},"
//                + "{\"name\":\"홍길서\","
//                + "\"email\":\"gilseo@hong.com\","
//                + "\"age\":\"23\""
//                + "}]}";

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(sb.toString());
            System.out.println(jsonObj);
            jsonObj = (JSONObject) jsonObj.get("response");
            jsonObj = (JSONObject) jsonObj.get("body");
            jsonObj = (JSONObject) jsonObj.get("items");
            JSONArray memberArray = (JSONArray) jsonObj.get("item");

            System.out.println("=====Members=====");
            for(int i=0 ; i<memberArray.size() ; i++){
                JSONObject tempObj = (JSONObject) memberArray.get(i);
                Object chargePt = tempObj.get("economyCharge");
                if(chargePt==null) {
                	continue;
                }
                int charge = Integer.parseInt(chargePt.toString());
                
                
                
                System.out.println("항공사 "+tempObj.get("airlineNm"));
                System.out.println("출발 공항"+tempObj.get("arrAirportNm"));
                System.out.println("출발 시간"+tempObj.get("arrAirportNm"));
                System.out.println("도착 공항"+tempObj.get("depAirportNm"));
                System.out.println("도착 시간"+tempObj.get("arrAirportNm"));
                System.out.println("economyCharge "+charge);
                System.out.println("economyCharge "+tempObj.get("prestigeCharge"));
                System.out.println("항공사"+tempObj.get("airlineNm"));
                System.out.println("----------------------------");
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        System.out.println(sb.toString());
    }
}