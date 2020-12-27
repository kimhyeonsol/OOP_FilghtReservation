import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginUIFrame extends JFrame{//로그인 프레임(카드레이 아웃)
	
	Container c;
	BufferedImage backImage;
	BufferedImage btn1Image;
	BufferedImage btn2Image;
	BufferedImage Image;
	CardLayout card;
	StartUIPanel startUIPanel=new StartUIPanel();
	LoginUIPanel loginUIpanel=new LoginUIPanel();
	SignUpPanel signUpPanel=new SignUpPanel();
	
	String userId="";
	
	LoginUIFrame(){
		setTitle("LoginUIFrame");
		setBounds(400, 300, 500, 380);
		setVisible(true);
		c=this.getContentPane();
		card=new CardLayout();
		c.setLayout(card);
		c.add(startUIPanel,"1");
		c.add(loginUIpanel,"2");
		c.add(signUpPanel,"3");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void loginUIFrameExit() {
	     this.dispose();
    }
	
	class ManagerButton extends JButton{//관리자 버튼
		ManagerButton(){
			try {
				  btn1Image = ImageIO.read(new File("managerImg.jpg"));
			  } catch (IOException e) {
				e.printStackTrace();
			}
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(btn1Image,0,0,getWidth(),getHeight(),null);
	    }
	}
	class UserButton extends JButton{//사용자 버튼
		UserButton(){
			try {
				  btn2Image = ImageIO.read(new File("UserImg.jpg"));
			  } catch (IOException e) {
				e.printStackTrace();
			}
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(btn2Image,0,0,getWidth(),getHeight(),null);
	    }
	}
	class StartUIPanel extends JPanel implements ActionListener{
		   ManagerButton loginButton1 = new ManagerButton();
		   UserButton loginButton2 = new UserButton();
		   public StartUIPanel() {
			  try {
				  backImage = ImageIO.read(new File("loginImg.jpg"));
			  } catch (IOException e) {	
				e.printStackTrace();
			  }
		      setLayout(null);
		     
		      loginButton1.addActionListener(this);
		      loginButton2.addActionListener(this);
		     
		      loginButton1.setBounds(50, 150, 150, 150);
		      loginButton2.setBounds(280, 150, 150, 150);
		      add(loginButton1);
		      add(loginButton2);
		   }
		   protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backImage,0,0,getWidth(),getHeight(),null);
			}
	   
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource() == loginButton1) {
					new ManagerUIFrame();
					loginUIFrameExit();
		         }
				if(e.getSource() == loginButton2) {
			       card.next(c);
			    }
			}
	}
	
	class LoginUIPanel extends JPanel implements ActionListener{
	   JLabel loginLabel[] = new JLabel[2];
	   JTextField loginTextField[] = new JTextField[2];
	   JButton loginButton[] = new JButton[2];
	   AirlineBookingUI ui;
	   String loginLabelStr[] = {"아이디", "비밀번호"};
	   String loginButtonStr[] = {"로그인", "회원가입"};
	   JButton backButton = new JButton("돌아가기");
	   
	   public LoginUIPanel() {
	      setLayout(null);
	      
	      for(int i=0; i<loginLabel.length; i++) {
	         loginLabel[i] = new JLabel(loginLabelStr[i]);
	         loginLabel[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 20));
	      }
	      loginLabel[0].setBounds(66, 135, 82, 40);
	      loginLabel[1].setBounds(66, 177, 105, 40);
	      for(int i=0; i<loginLabel.length; i++) {
	         add(loginLabel[i]);
	      }
	      
	      for(int i=0; i<loginTextField.length; i++) {
	         loginTextField[i] = new JTextField();
	         loginTextField[i].setColumns(10);
	      }
	      loginTextField[0].setBounds(169, 135, 241, 40);
	      loginTextField[1].setBounds(169, 177, 241, 40);
	      for(int i=0; i<loginTextField.length; i++) {
	         add(loginTextField[i]);
	      }
	      
	      for(int i=0; i<loginButton.length; i++) {
	         loginButton[i] = new JButton(loginButtonStr[i]);
	         loginButton[i].setForeground(new Color(255, 255, 255));
	         loginButton[i].setBackground(new Color(128, 128, 128));
	         loginButton[i].setFocusPainted(false);
	         loginButton[i].addActionListener(this);
	      }
	      loginButton[0].setBounds(83, 240, 327, 30);
	      loginButton[1].setBounds(280, 284, 130, 27);
	      loginButton[0].setFont(new Font("한컴산뜻돋움", Font.BOLD, 17));
	      loginButton[1].setFont(new Font("한컴산뜻돋움", Font.BOLD, 15));
	      for(int i=0; i<loginButton.length; i++) {
	         add(loginButton[i]);
	      }
	      
	      backButton.setForeground(new Color(255, 255, 255));
	      backButton.setBackground(new Color(128, 128, 128));
	      backButton.setFocusPainted(false);
	      backButton.addActionListener(this);
	      backButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 15));
	      backButton.setBounds(83, 284, 130, 27);
	      add(backButton);
	   }
	   
	   protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backImage,0,0,getWidth(),getHeight(),null);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == loginButton[0]) {
				userId=loginTextField[0].getText();
				new UserUIFrame(userId);
				loginUIFrameExit();
	        }
			else if(e.getSource() == loginButton[1]) {
				card.next(c);
	        }
			else if(e.getSource() == backButton) {
				card.show(c, "1");
			}
		}
	}
	class SignUpPanel extends JPanel implements ActionListener{
		
		   JLabel infoLabel[] = new JLabel[6];
		   JTextField textField[] = new JTextField[6];
		   String infoStr[] = {"이름","아이디", "비밀번호", "이메일", "생년월일(ex.980814)", "전화번호"};
		   JButton button = new JButton("회원가입");
		   JButton backButton = new JButton("돌아가기");
		   JLabel label = new JLabel("회원가입");
		   
		   public SignUpPanel() {
			   
		      setLayout(null);
		      this.setBackground(new Color(124,143,157));
		      label.setForeground(Color.white);
			  label.setFont(new Font("맑은 고딕", Font.BOLD, 25));
			  label.setBounds(26, 10, 100, 40);
				
		      for(int i=0; i<infoLabel.length; i++) {
		          infoLabel[i] = new JLabel(infoStr[i]);
		          infoLabel[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 16));
		       }
		       for(int i=0; i<textField.length; i++) {
		          textField[i] = new JTextField();
		       }
		       for(int i=0; i<6; i++) {
		    	   infoLabel[i].setBounds(80, 64+(40*i), 150, 23);
		           add(infoLabel[i]);
		           textField[i].setBounds(278, 64+(40*i), 130, 25);
		           add(textField[i]);
		       }
		       backButton.setBackground(new Color(128, 128, 128));
		       backButton.setFocusPainted(false);
		       backButton.setForeground(Color.white);
		       backButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 17));
		       backButton.setBounds(80, 308, 120, 23);
		       backButton.addActionListener(this);
		       
		       button.setBackground(new Color(128, 128, 128));
		       button.setFocusPainted(false);
		       button.setForeground(Color.white);
		       button.setFont(new Font("한컴산뜻돋움", Font.BOLD, 17));
		       button.setBounds(290, 308, 120, 23);
		       button.addActionListener(this);
		       
		       add(label);
		       add(backButton);
		       add(button);
		   }
		   protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				//g.drawImage(backImage,0,0,getWidth(),getHeight(),null);
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == button) {
					//card.show(c,"1");
					card.previous(c);
		         }
				else if(e.getSource() == backButton) {
					card.show(c, "2");
				}
			}
	}
}