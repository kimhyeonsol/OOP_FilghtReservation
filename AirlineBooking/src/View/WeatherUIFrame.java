package View;

import java.awt.Font;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class WeatherUIFrame extends JFrame{
	JLabel label = new JLabel("공항 주변 관광 코스 날씨 정보");
	JLabel label2 = new JLabel("공항 선택");
	JLabel label3 = new JLabel("코스선택");
	
	JComboBox localComboBox = new JComboBox();
	JComboBox courseComboBox = new JComboBox();
	public String comboStr[] = {"인천", "김포", "제주", "대구", "김해" };
	
	Calendar cal=Calendar.getInstance();
	
	
	WeatherUIFrame(){
		this.setTitle("공항 주변 관광지 날씨 정보");
		this.setBounds(100,100,1000,700);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		
		setLayout(null);
		localComboBox.setBounds(133, 96, 234, 33);
		localComboBox.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		add(localComboBox);
		for (int i = 0; i < comboStr.length; i++) {
			localComboBox.addItem(comboStr[i]);
		}
		
		courseComboBox.setBounds(478, 96, 272, 33);
		courseComboBox.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		add(courseComboBox);
		
		label.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(40, 30, 350, 33);
		add(label);
		
		label2.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		label2.setBounds(50, 103, 92, 20);
		add(label2);
		
		label3.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		label3.setBounds(403, 103, 92, 20);
		add(label3);
		
		JTextArea textArea = new JTextArea();
		JScrollPane scroll=new JScrollPane(textArea);
		scroll.setBounds(60, 161, 867, 468);
		add(scroll);
		
		///////////////////오늘 날짜////////////
		int year= cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH)+1;
		int day=cal.get(Calendar.DAY_OF_MONTH);
			
		String today=new String("");
		today+=year+"년 "+month+"월 "+day+"일 ";
		
		JLabel todayLb=new JLabel(today);
		todayLb.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		todayLb.setHorizontalAlignment(SwingConstants.CENTER);
		todayLb.setBounds(700, 34, 291, 33);
		add(todayLb);
		
		setVisible(true);
	}
	static public void main(String args[]) {
		new WeatherUIFrame();
	}
}
