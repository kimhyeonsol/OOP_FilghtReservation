package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class AirPortParkingLotDAO extends Conf{

   public AirPortParkingLotDAO() {
	   super();
	   _schemaName = "project";
      try {
		connectDB();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
//		e.printStackTrace();
		System.out.println("AirPortParkingLotDAO DB 연결 실패!");
	}
   }

   public ArrayList<AirPortParkingLot> getAPInfo(String airPort) { //airPort값과 일치하는 공항의 주차장 정보를 가져옴
      
      try {
         sql = "select * from airportlot where airportKor = ? ";
         
         ArrayList<AirPortParkingLot> datas = new ArrayList<AirPortParkingLot>();
         
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, airPort);
         rs = pstmt.executeQuery();

         while (rs.next()) {
        	AirPortParkingLot p = new AirPortParkingLot();
            p.setAirportKor(rs.getString("airportKor"));
            p.setParkingAirportCodeName(rs.getString("parkingAirportCodeName"));
            p.setParkingCongestion(rs.getString("parkingCongestion"));
            p.setParkingCongestionDegree(rs.getString("parkingCongestionDegree"));
            p.setParkingOccupiedSpace(rs.getString("parkingOccupiedSpace"));
            p.setParkingTotalSpace(rs.getString("parkingTotalSpace"));
            p.setSysGetdate(rs.getString("sysGetdate"));
            p.setSysGettime(rs.getString("sysGettime"));
            datas.add(p);
         }
         return datas;

      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return null;
   }

   public ArrayList<InCheonAirPortParkingLot> getICAPInfo() { //인천 공항 주차장 정보를 가져옴
      
      try {
         sql = "select * from incheonairportlot";

         ArrayList<InCheonAirPortParkingLot> datas = new ArrayList<InCheonAirPortParkingLot>();

         pstmt = conn.prepareStatement(sql);
         rs = pstmt.executeQuery();

         while (rs.next()) {
        	InCheonAirPortParkingLot p = new InCheonAirPortParkingLot();
            p.setFloor(rs.getString("floor"));
            p.setParking(rs.getString("parking"));
            p.setParkingarea(rs.getString("parkingarea"));
            p.setDatetm(rs.getString("datetm"));
            datas.add(p);
         }
         return datas;

      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return null;
   }
}