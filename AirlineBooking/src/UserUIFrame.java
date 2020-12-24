import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserUIFrame extends JFrame{// user 프레임(카드레이 아웃)
	
	Container c;
	CardLayout card;
	UserMenuPanel userMenuPanel=new UserMenuPanel();
	
	BufferedImage backImage, menuImage1, menuImage2;
	
/////////////////////////////////////////////////////////////////////////////////////
	
	UserUIFrame(){//사용자에 대한 매개변수 추가해야함.(ID값)
		setTitle("UserUIFrame");
		setBounds(100, 100, 1000, 700);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setBackground(Color.black);
		
		c=this.getContentPane();
		card=new CardLayout();
		
		this.setLayout(card);
		add(userMenuPanel,"1");
		setVisible(true);
	}
	
////////////////////////////////////////사용자 메뉴 패널/////////////////////////////////////////////
	
	class MyInfoButton extends JButton{	
		MyInfoButton(){
	         try {
	        	 menuImage1 = ImageIO.read(new File("MyinfoBTN.jpg"));
	           } catch (IOException e) {
	            e.printStackTrace();
	         }
	      }
	      protected void paintComponent(Graphics g) {
	         super.paintComponent(g);
	         g.drawImage(menuImage1,0,0,getWidth(),getHeight(),null);
	       }
	}
	
	
	class FlightResButton extends JButton{
		FlightResButton(){
	         try {
	        	 menuImage2 = ImageIO.read(new File("reservateBTN.jpg"));
	           } catch (IOException e) {
	            e.printStackTrace();
	         }
	      }
	      protected void paintComponent(Graphics g) {
	         super.paintComponent(g);
	         g.drawImage(menuImage2,0,0,getWidth(),getHeight(),null);
	       }
	}
	
	class UserMenuPanel extends JPanel implements ActionListener{
		MyInfoButton myInfoButton = new MyInfoButton();
		FlightResButton flightResButton = new FlightResButton();
		UserMenuPanel(){
			try {
				backImage = ImageIO.read(new File("Menu.png"));
	        } catch (IOException e) {
	           e.printStackTrace();
	        }
			setLayout(null);
			myInfoButton.setBounds(280, 300, 180, 180);
			flightResButton.setBounds(530, 300, 180, 180);
			
			add(myInfoButton);
			add(flightResButton);
			
		}
		protected void paintComponent(Graphics g) {
		        super.paintComponents(g);
		        g.drawImage(backImage,0,0,getWidth(),getHeight(),null);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
		}
	 } 
	
///////////////////////////////////사용자 정보 수정 패널//////////////////////////////////////
		
		class MyInfoPanel extends JPanel implements ActionListener{
			BufferedImage backImage, menuImage1, menuImage2;
			MyInfoButton myInfoButton = new MyInfoButton();
			FlightResButton flightResButton = new FlightResButton();
			MyInfoPanel(){
				try {
					backImage = ImageIO.read(new File("Menu.png"));
		        } catch (IOException e) {
		           e.printStackTrace();
		        }
				setLayout(null);
				myInfoButton.setBounds(280, 300, 180, 180);
				flightResButton.setBounds(530, 300, 180, 180);
				
				add(myInfoButton);
				add(flightResButton);
				
			}
			protected void paintComponent(Graphics g) {
			        super.paintComponents(g);
			        g.drawImage(backImage,0,0,getWidth(),getHeight(),null);
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		}
////////////////////////////////////////////////항공편 예약 패널///////////////////////////////////////////////

		class FlightResPanel extends JPanel implements ActionListener{
			BufferedImage backImage, menuImage1, menuImage2;
			MyInfoButton myInfoButton = new MyInfoButton();
			FlightResButton flightResButton = new FlightResButton();
			FlightResPanel(){
				try {
					backImage = ImageIO.read(new File("Menu.png"));
		        } catch (IOException e) {
		           e.printStackTrace();
		        }
				setLayout(null);
				myInfoButton.setBounds(280, 300, 180, 180);
				flightResButton.setBounds(530, 300, 180, 180);
				
				add(myInfoButton);
				add(flightResButton);
				
			}
			protected void paintComponent(Graphics g) {
			        super.paintComponents(g);
			        g.drawImage(backImage,0,0,getWidth(),getHeight(),null);
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		}
		
}
