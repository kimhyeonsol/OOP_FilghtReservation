public class WeatherInfo {
	int id;
	String courseAreaId;
	String courseAreaName;
	String courseId;
	String courseName;
	String pop;
	String rhm;
	String sky;
	String spotAreaId;
	String spotAreaName;
	String spotName;
	String th3;
	String thema;
	String tm;
	String wd;
	String ws;

	String weatherSky[] = { "맑음", "구름 조금", "구름 많음", "흐림", "비", "비눈", "눈비", "눈" };

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

//////////////////////////////////////////////////////////////
	public String getCourseAreaId() {
		return courseAreaId;
	}

	public void setCourseAreaId(String courseAreaId) {
		this.courseAreaId = courseAreaId;
	}

///////////////////////////////////////////////////////////////
	public String getCourseAreaName() {
		return courseAreaName;
	}

	public void setCourseAreaName(String courseAreaName) {
		this.courseAreaName = courseAreaName;
	}

	//////////////////////////////////////////////
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

///////////////////////////////////////////////////////////////
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

///////////////////////////////////////////////////////////////
	public String getPop() {
		return pop;
	}

	public void setPop(String pop) {
		this.pop = pop;
	}

///////////////////////////////////////////////////////////////
	public String getRhm() {
		return rhm;
	}

	public void setRhm(String rhm) {
		this.rhm = rhm;
	}

//////////////////////////////////////////////////////////////   
	public String getSky() {
		return sky;
	}

	public void setSky(String sky) {
		String str = setSkyStr(sky);
		this.sky = str;

	}

//////////////////////////////////////////////////////////////
	public String getSpotAreaId() {
		return spotAreaId;
	}

	public void setSpotAreaId(String spotAreaId) {
		this.spotAreaId = spotAreaId;
	}

//////////////////////////////////////////////////////////////
	public String getSpotAreaName() {
		return spotAreaName;
	}

	public void setSpotAreaName(String spotAreaName) {
		this.spotAreaName = spotAreaName;
	}

/////////////////////////////////////////////////////////////
	public String getSpotName() {
		return spotName;
	}

	public void setSpotName(String spotName) {
		this.spotName = spotName;
	}

/////////////////////////////////////////////////////////////
	public String getThema() {
		return thema;
	}

	public void setThema(String thema) {
		this.thema = thema;
	}

/////////////////////////////////////////////////////////////
	public String getTm() {
		return tm;
	}

	public void setTm(String tm) {
		this.tm = tm;
	}

	public String setSkyStr(String sky) {
		int temp = Integer.valueOf(sky) - 1;
		return weatherSky[temp];
	}
}