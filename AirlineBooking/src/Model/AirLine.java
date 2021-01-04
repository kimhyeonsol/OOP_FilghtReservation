package Model;

public class AirLine {

   int ID;// 항공ID
   String airLineNm;// 항공사 이름
   String arrAirportNm;// 도착공항
   String arrPlandTime;// 도착시간
   String depAirportNm;// 출발공항
   String depPlandTime;// 출발시간
   int economyCharge;// 이코노미 운임
   int prestigeCharge;// 비즈니스 운임

   public int getID() {
      return ID;
   }

   public void setID(int iD) {
      ID = iD;
   }
   
   public String getAirLineNm() {
      return airLineNm;
   }

   public void setAirLineNm(String airLineNm) {
      this.airLineNm = airLineNm;
   }

   public String getArrAirportNm() {
      return arrAirportNm;
   }

   public void setArrAirportNm(String arrAirportNm) {
      this.arrAirportNm = arrAirportNm;
   }

   public String getArrPlandTime() {
      return arrPlandTime;
   }

   public void setArrPlandTime(String arrPlandTime) {
      this.arrPlandTime = arrPlandTime;
   }

   public String getDepAirportNm() {
      return depAirportNm;
   }

   public void setDepAirportNm(String depAirportNm) {
      this.depAirportNm = depAirportNm;
   }

   public String getDepPlandTime() {
      return depPlandTime;
   }

   public void setDepPlandTime(String depPlandTime) {
      this.depPlandTime = depPlandTime;
   }

   public int getEconomyCharge() {
      return economyCharge;
   }

   public void setEconomyCharge(int economyCharge) {
      this.economyCharge = economyCharge;
   }

   public int getPrestigeCharge() {
      return prestigeCharge;
   }

   public void setPrestigeCharge(int prestigeCharge) {
      this.prestigeCharge = prestigeCharge;
   }

}