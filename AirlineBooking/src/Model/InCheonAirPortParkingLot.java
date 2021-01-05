package Model;

public class InCheonAirPortParkingLot {
   String floor; //주차구역명
   String parking; //입고된 차량 수
   String parkingarea; //전체 주차면 수
   String datetm; //업데이트 시각
   
   
   public String getFloor() {
         return floor;
   }

   public void setFloor(String floor) {
         this.floor = floor;
   }
   ///////////////////////////////////////////////////////////
   public String getParking() {
         return parking;
   }

   public void setParking(String parking) {
         this.parking = parking;
   }
   /////////////////////////////////////////////////////////////
   public String getParkingarea() {
         return parkingarea;
   }

   public void setParkingarea(String parkingarea) {
         this.parkingarea = parkingarea;
   }
   ////////////////////////////////////////////////////////////////
   public String getDatetm() {
         return datetm;
   }

   public void setDatetm(String datetm) {
         this.datetm = datetm;
   }
   
   
}