import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UserUIFrame extends JFrame{// user 프레임(카드레이 아웃)
   
   Container c;
   CardLayout card;
   
   UserMenuPanel userMenuPanel=new UserMenuPanel();
   MyInfoPanel myInfoPanel=new MyInfoPanel();
   FlightResPanel flightResPanel=new FlightResPanel();
   
   BufferedImage backImage, menuImage1, menuImage2;
   BufferedImage Image1, Image2, Image3;
   
   
/////////////////////////////////////////////////////////////////////////////////////
   
   UserUIFrame(){//사용자에 대한 매개변수 추가해야함.(ID값)
      setTitle("UserUIFrame");
      setBounds(100, 100, 1000, 700);
      this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
      setBackground(Color.black);
      
      c=this.getContentPane();
      card=new CardLayout();
      
      this.setLayout(card);
      
      add(userMenuPanel,"userMenu");
      add(myInfoPanel,"myInfo");
      add(flightResPanel,"reservation");
      
      setVisible(true);
   }
   public void userMenuExit() {
         this.dispose();
   }
    class GoBackButton extends JButton{
       GoBackButton(){
            try {
               Image3 = ImageIO.read(new File("뒤로가기.png"));
              } catch (IOException e) {
               e.printStackTrace();
            }
          }
         protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(Image3,0,0,getWidth(),getHeight(),null);
          }
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
	  GoBackButton backButton = new GoBackButton();
      MyInfoButton myInfoButton = new MyInfoButton();
      FlightResButton flightResButton = new FlightResButton();
      
      UserMenuPanel(){
         try {
            backImage = ImageIO.read(new File("Menu.png"));
           } catch (IOException e) {
              e.printStackTrace();
           }
         setLayout(null);
         
         backButton.setBounds(10,10,80,80);
         backButton.addActionListener(this);
         
         myInfoButton.setBounds(280, 300, 180, 180);
         flightResButton.setBounds(530, 300, 180, 180);
         
         myInfoButton.addActionListener(this);
         flightResButton.addActionListener(this);
         
         add(backButton);
         add(myInfoButton);
         add(flightResButton);
         
      }
      protected void paintComponent(Graphics g) {
              super.paintComponents(g);
              g.drawImage(backImage,0,0,getWidth(),getHeight(),null);
      }
      @Override
      public void actionPerformed(ActionEvent e) {
         if(e.getSource()==myInfoButton) {
            card.show(c, "myInfo");
         }
         else if(e.getSource()==flightResButton) {
            card.show(c, "reservation");
         }
         else if(e.getSource() == backButton) {
        	 new LoginUIFrame();
        	 userMenuExit();
         }
      }
    } 
   
///////////////////////////////////사용자 정보 패널//////////////////////////////////////
      
   class MyInfoPanel extends JPanel implements ActionListener{
      
         GoBackButton backButton = new GoBackButton();
         
         MyInfoPanel(){
              setBackground(Color.lightGray);
              setLayout(null);
              setBounds(200, 150, 1000, 700);
              
              JLabel lb=new JLabel("마이 페이지");
              lb.setHorizontalAlignment(JLabel.CENTER);
              lb.setFont(new Font("한컴산뜻돋움", Font.BOLD, 40));
              lb.setBounds(350,0,300,100);
              
              backButton.setHorizontalAlignment(JLabel.CENTER);
              backButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 40));
              backButton.setBounds(10,10,80,80);
              backButton.addActionListener(this);
              
              JTabbedPane mainJtabUI = new JTabbedPane(JTabbedPane.TOP);
              mainJtabUI.setBounds(50,100,900,550);
              mainJtabUI.addTab("내 정보 수정하기", new MyInfoUpdatePanel());
              mainJtabUI.addTab("내 항공편 예약 현황", new MyReservationUpdatePanel());
              
              add(lb);
              add(backButton);
              add(mainJtabUI);
               
              setVisible(true);
         }
         ////////////////////////////////////////////////////////
         class MyInfoUpdatePanel extends JPanel{//내정보 수정
               JPanel mainPanel = new JPanel();
               JPanel p[] = new JPanel[2];
               JTextArea textArea = new JTextArea(31,43);
               JLabel infoLabel[] = new JLabel[6];
               JTextField textField[] = new JTextField[6];
               String infoStr[] = {"이름","아이디", "비밀번호", "이메일", "생년월일", "전화번호"};
               JButton button = new JButton("변경하기");
               
               MyInfoUpdatePanel(){
                  for(int i=0; i<p.length; i++) {
                     p[i] = new JPanel();
                     p[i].setBackground(new Color(176, 224, 230));
                  }
                  p[0].add(new JScrollPane(textArea));
                  
                  p[1].setLayout(null);
                  for(int i=0; i<infoLabel.length; i++) {
                     infoLabel[i] = new JLabel(infoStr[i]);
                     infoLabel[i].setHorizontalAlignment(JLabel.CENTER);
                     infoLabel[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 18));
                  }
                  for(int i=0; i<textField.length; i++) {
                     textField[i] = new JTextField();
                  }
                  for(int i=0; i<6; i++) {
                     infoLabel[i].setLocation(50, 20+(i*65));
                     infoLabel[i].setSize(80,80);
                     p[1].add(infoLabel[i]);
                     textField[i].setLocation(160, 35+(i*65));
                     textField[i].setSize(200,60);
                     p[1].add(textField[i]);
                  }
                  
                  button.setForeground(new Color(255, 255, 255));
                  button.setBackground(new Color(128, 128, 128));
                  button.setFont(new Font("한컴산뜻돋움", Font.BOLD, 17));
                  button.setLocation(50, 450);
                  button.setSize(340,50);
                  
                  p[1].add(button);
                  
                  mainPanel.setLayout(new GridLayout(1,2));
                  mainPanel.add(p[0]);
                  mainPanel.add(p[1]);
                  add(mainPanel);
               }
            }
         /////////////////////////////////////////////////////////
           class ChangeSeatBtn extends JButton{
              ChangeSeatBtn(){
                  try {
                     Image2 = ImageIO.read(new File("좌석변경.png"));
                    } catch (IOException e) {
                     e.printStackTrace();
                  }
                }
               protected void paintComponent(Graphics g) {
                  super.paintComponent(g);
                  g.drawImage(Image2,0,0,getWidth(),getHeight(),null);
                }
           }
              class CancleResBtn extends JButton{   
                      CancleResBtn(){
                     try {
                        Image1 = ImageIO.read(new File("예약취소.png"));
                       } catch (IOException e) {
                        e.printStackTrace();
                     }
                  }
                  protected void paintComponent(Graphics g) {
                     super.paintComponent(g);
                     g.drawImage(Image1,0,0,getWidth(),getHeight(),null);
                   }
            }
              
             class MyReservationUpdatePanel extends JPanel implements ActionListener{//내 항공편 예약 현황
            
               JTextArea textArea = new JTextArea();
               JLabel pageName = new JLabel("내 항공편 예약 현황");
               ChangeSeatBtn changeSeatBtn = new ChangeSeatBtn();
               CancleResBtn cancleResBtn = new CancleResBtn();
               
               MyReservationUpdatePanel(){
                 setLayout(null);
                 setBackground(new Color(176, 224, 230));
                 
                 pageName.setBounds(10,10,200,30);//페이지 이름 라벨
                 pageName.setFont(new Font("한컴산뜻돋움", Font.BOLD, 16));
                 add(pageName);
                 
                 JScrollPane scroll=new JScrollPane(textArea);//자기 항공편 예약 현황 나타남
                 scroll.setBounds(30, 50, 830, 280);
                  add(scroll);
                  
                  changeSeatBtn.setBounds(30,350,380,150);
                  changeSeatBtn.addActionListener(this);
                  add(changeSeatBtn);
                  
                  cancleResBtn.addActionListener(this);
                  cancleResBtn.setBounds(480,350,380,150);
                  add(cancleResBtn);
               }

            @Override
            public void actionPerformed(ActionEvent e) {
               if(e.getSource()==changeSeatBtn) {
                  String resnum=JOptionPane.showInputDialog("자리변경 할 항공편의 예약번호를 입력하세요");
                  if(resnum!=null) {
                     //////////dB에서 해당 항공기 예약정보를 삭제하고
                     //////////항공편 자리선택 화면으로 가서 다시 예약
                     JOptionPane.showMessageDialog(null, "자리변경되었습니다.");
                  }
               }
               else if(e.getSource()==cancleResBtn) {
                  String resnum=JOptionPane.showInputDialog("예약취소 할 항공편의 예약번호를 입력하세요");
                  if(resnum!=null) {
                     //////////dB에서 해당 항공기 예약정보를 삭제
                     JOptionPane.showMessageDialog(null, "예약취소 되었습니다.");
                  }
               }   
            }
            }
            @Override
            public void actionPerformed(ActionEvent arg0) {
               // TODO Auto-generated method stub
               if(arg0.getSource() == backButton) {
                  card.show(c, "userMenu");
               }
            }
      }
////////////////////////////////////////////////항공편 예약 패널///////////////////////////////////////////////

      class FlightResPanel extends JPanel implements ActionListener{
         GoBackButton backButton = new GoBackButton();
            
          FlightResPanel(){
             setBackground(Color.lightGray);
              setLayout(null);
              setBounds(200, 150, 1000, 700);
              
              JLabel lb=new JLabel("항공기 예약");
              lb.setHorizontalAlignment(JLabel.CENTER);
              lb.setFont(new Font("한컴산뜻돋움", Font.BOLD, 40));
              lb.setBounds(350,0,300,100);
              
              backButton.setHorizontalAlignment(JLabel.CENTER);
              backButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 40));
              backButton.setBounds(10,10,80,80);
              backButton.addActionListener(this);
              
              JTabbedPane mainJtabUI = new JTabbedPane(JTabbedPane.TOP);
              mainJtabUI.setBounds(50,100,900,550);
              mainJtabUI.addTab("항공기 예약", new RegisterFlightPanel());
              
              
              add(lb);
              add(backButton);
              add(mainJtabUI);
             
              setVisible(true);
            }
            
            class RegisterFlightPanel extends JPanel{
                  RegisterFlightPanel(){
                   
                  }
            }
               @Override
           public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
               if(arg0.getSource() == backButton) {
                  card.show(c, "userMenu");
               }
           } 
      }
      
}