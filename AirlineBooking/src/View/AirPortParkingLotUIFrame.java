package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class AirPortParkingLotUIFrame extends JFrame { //실시간 공항 주차장 정보 조회 프레임
	public BufferedImage backImage; //배경 이미지
	public JComboBox airPortComboBox = new JComboBox(); //공항 선택 콤보박스
	String airPortStr[] = { "인천국제공항","김포국제공항", "김해국제공항", "제주국제공항", "대구국제공항" };
	JLabel label = new JLabel("공항 주차장 혼잡도");
	JLabel airPortLabel = new JLabel("공항 선택");
	public JTextArea airPortTextArea = new JTextArea(); //주차장 정보 조회 textArea
	public JButton airPortButton = new JButton("검색하기"); //주차장 정보 '검색하기' 버튼
	

	public AirPortParkingLotUIFrame() {
		
		AirPortPanel airPortPanel = new AirPortPanel();
		add(airPortPanel);

		setBounds(250, 50, 1000, 700);
		setVisible(true);
	}
	
	public class AirPortPanel extends JPanel {
		AirPortPanel() {
			try {
				backImage = ImageIO.read(new File("주차장.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			setTitle("공항 주차장 혼잡도");

			setLayout(null);
			label.setFont(new Font("맑은고딕", Font.BOLD, 40));
			label.setBounds(50, 50, 800, 40);
			add(label);

			for (int i = 0; i < airPortStr.length; i++) { //콤보박스 아이템 추가
				airPortComboBox.addItem(airPortStr[i]);
			}

			airPortLabel.setFont(new Font("맑은고딕", Font.BOLD, 20));
			airPortLabel.setBounds(200, 130, 200, 35);
			airPortComboBox.setBounds(320, 130, 200, 35);
			airPortComboBox.setFont(new Font("맑은고딕", Font.BOLD, 16));

			airPortButton.setFocusPainted(false);
			airPortButton.setFont(new Font("맑은고딕", Font.BOLD, 16));
			airPortButton.setForeground(new Color(255, 255, 255));
			airPortButton.setBackground(new Color(128, 128, 128));
			airPortButton.setBounds(600, 130, 150, 35);

			airPortTextArea.setEditable(false);
			JScrollPane scroll = new JScrollPane(airPortTextArea);
			add(scroll);
			scroll.setBounds(50, 190, 890, 450);

			add(airPortLabel);
			add(airPortComboBox);
			add(airPortButton);

			////////////////오늘 날짜////////////////
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);

			String today = new String("");
			today += year + "년 " + month + "월 " + day + "일 ";

			JLabel todayLb = new JLabel(today);
			todayLb.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			todayLb.setHorizontalAlignment(SwingConstants.CENTER);
			todayLb.setBounds(700, 34, 291, 33);
			add(todayLb);
			setBounds(250, 50, 1000, 700);
			setVisible(true);
		}

		protected void paintComponent(Graphics g) { //배경 이미지 넣기
			super.paintComponents(g);
			g.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);
		}
	}

	public void addButtonActionListener(ActionListener listener) {	//액션 리스너 부착
		airPortComboBox.addActionListener(listener);
		airPortButton.addActionListener(listener);

	}

}