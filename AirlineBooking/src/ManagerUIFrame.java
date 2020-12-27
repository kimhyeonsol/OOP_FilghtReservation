import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ManagerUIFrame extends JFrame{
   BufferedImage backImage, menuImage1, menuImage2, menuImage3;
   BufferedImage Image3, pageImg;
   
   ManagerMenuPanel managerMenuPanel = new ManagerMenuPanel();
   MemberManagerUI managerPanel = new MemberManagerUI();
   ManagerReservationUI reservationPanel = new ManagerReservationUI();
   ManagerFlightUI flightPanel = new ManagerFlightUI();
   
   Container c;
   CardLayout card;
         
   ManagerUIFrame(){
      setTitle("관리자 페이지");
      setBounds(100, 100, 1000, 700);
      this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
      setBackground(Color.black);
      
      c=this.getContentPane();
      card=new CardLayout();
      
      this.setLayout(card);
      
      add(managerMenuPanel,"managerMenu");
      add(managerPanel,"manager");
      add(reservationPanel, "reservation");
      add(flightPanel, "flight");
      
      setVisible(true);
   }
   
   public void managerMenuExit() {
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
   
   ////////////////////////////관리자 메뉴 패널////////////////////////////
   
    class memberManageButton extends JButton{//회원 관리 버튼
         memberManageButton(){
               try {
                  menuImage1 = ImageIO.read(new File("memberButton.jpg"));
                 } catch (IOException e) {
                  e.printStackTrace();
               }
         }
         protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               g.drawImage(menuImage1,0,0,getWidth(),getHeight(),null);
         }
    }
   
    class reservationManageButton extends JButton{   //예약 관리 버튼
         reservationManageButton(){
               try {
                  menuImage2 = ImageIO.read(new File("reservationButton.jpg"));
                 } catch (IOException e) {
                  e.printStackTrace();
               }
         }
         protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               g.drawImage(menuImage2,0,0,getWidth(),getHeight(),null);
         }
    }
    
    class flightManageButton extends JButton{   //항공 관리 버튼
         flightManageButton(){
               try {
                  menuImage3 = ImageIO.read(new File("flightButton.png"));
                 } catch (IOException e) {
                  e.printStackTrace();
               }
         }
         protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               g.drawImage(menuImage3,0,0,getWidth(),getHeight(),null);
         }
    }
   
    class ManagerMenuPanel extends JPanel implements ActionListener{ //관리자 메뉴 패널
         GoBackButton backButton = new GoBackButton();
         memberManageButton memButton = new memberManageButton();
         reservationManageButton resButton = new reservationManageButton();
         flightManageButton fliButton = new flightManageButton();
         
         ManagerMenuPanel(){
             try {
                backImage = ImageIO.read(new File("Menu.png"));
               } catch (IOException e) {
                  e.printStackTrace();
             }
             
             setLayout(null);
             
             backButton.setBounds(10,10,80,80);
             backButton.addActionListener(this);
             
             memButton.setBounds(250, 300, 150, 150);
             resButton.setBounds(418, 300, 150, 150);
             fliButton.setBounds(585, 300, 150, 150);
             
             memButton.addActionListener(this);
             resButton.addActionListener(this);
             fliButton.addActionListener(this);
             
             add(backButton);
             add(memButton);
             add(resButton);
             add(fliButton);
      }
      protected void paintComponent(Graphics g) {
         super.paintComponents(g);
         g.drawImage(backImage,0,0,getWidth(),getHeight(),null);
      }
      
      @Override
      public void actionPerformed(ActionEvent e) { //관리자 메뉴 패널 액션 리스너
         // TODO Auto-generated method stub
         if(e.getSource() == memButton) { //회원 관리 버튼 누를 경우
            card.show(c, "manager"); //회원 관리 패널로 넘어감
         }
         else if(e.getSource() == resButton) { //예약 관리 버튼 누를 경우
            card.show(c, "reservation"); //예약 관리 패널로 넘어감
         }
         else if(e.getSource() == fliButton) { //항공 관리 버튼 누를 경우
            card.show(c, "flight"); //항공 관리 패널로 넘어감
         }
         else if(e.getSource() == backButton) { //뒤로가기 버튼 누를 경우
            new LoginUIFrame(); //로그인 메뉴 화면 띄움
            managerMenuExit(); //관리자 메뉴 프레임 닫음
         }
      }
   }

   //////////////////////////////////회원관리 패널///////////////////////////////
   
   class MemberManagerUI extends JPanel implements ActionListener{
        GoBackButton backButton = new GoBackButton();
        
        MemberManagerUI(){
      	  try {
      		  pageImg = ImageIO.read(new File("pageImg.jpg"));
               } catch (IOException e) {
                e.printStackTrace();
        }
      	
      	setLayout(null);
        setBounds(200, 150, 1000, 700);
        
        JLabel lb=new JLabel("회원정보 관리");
        lb.setHorizontalAlignment(JLabel.CENTER);
        lb.setFont(new Font("한컴산뜻돋움", Font.BOLD, 40));
        lb.setBounds(350,0,300,100);
        
        backButton.setBounds(10,10,80,80);
        backButton.addActionListener(this);
        
        JTabbedPane mainJtabUI = new JTabbedPane(JTabbedPane.TOP);
        mainJtabUI.setBounds(50,100,900,550);
        mainJtabUI.addTab("회원 삭제", new MemberDelete());
        
        add(lb);
        add(backButton);
        add(mainJtabUI);
        
        setVisible(true);
      }
      protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          g.drawImage(pageImg,0,0,getWidth(),getHeight(),null);
      }
      
      ////////////////////////////////회원 삭제////////////////////////////////
      class MemberDelete extends JPanel implements ActionListener{
    	  JPanel mainPanel = new JPanel();
          JPanel p[] = new JPanel[2];
          JPanel searchPanel[] = new JPanel[2];
          JTextArea textArea = new JTextArea(29,43); //회원 조회 textArea
          JLabel infoLabel[] = new JLabel[2]; //회원 삭제 라벨
          String infoStr[] = {"아이디", "비밀번호"};
          JTextField textField[] = new JTextField[2]; //회원 삭제 텍스트필드
          JButton deleteButton = new JButton("삭제하기"); //삭제하기 버튼
          JButton searchButton = new JButton("조회하기"); //조회하기 버튼
          
          MemberDelete(){
              for(int i=0; i<p.length; i++) {
                 p[i] = new JPanel();
                 p[i].setBackground(new Color(209, 233, 255));
                 searchPanel[i] = new JPanel();
                 searchPanel[i].setBackground(new Color(209, 233, 255));
              }
                
              p[0].setLayout(new BorderLayout());
              
              searchButton.setFocusPainted(false);
              searchButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 15));
              searchButton.setForeground(new Color(255, 255, 255));
              searchButton.setBackground(new Color(128, 128, 128));
                
              searchPanel[0].setLayout(new FlowLayout());
              searchButton.addActionListener(this);
              searchPanel[0].add(searchButton);
              searchPanel[1].add(new JScrollPane(textArea));
                
              p[0].add(searchPanel[0], BorderLayout.NORTH);
              p[0].add(searchPanel[1], BorderLayout.CENTER);
              
              p[1].setLayout(null);
              for(int i=0; i<infoLabel.length; i++) {
                 infoLabel[i] = new JLabel(infoStr[i]);
                 infoLabel[i].setHorizontalAlignment(JLabel.CENTER);
                 infoLabel[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 18));
              }
              for(int i=0; i<textField.length; i++) {
                 textField[i] = new JTextField();
              }
              for(int i=0; i<2; i++) {
                 infoLabel[i].setLocation(50, 160+(i*80));
                 infoLabel[i].setSize(80,80);
                 p[1].add(infoLabel[i]);
                 textField[i].setLocation(190, 175+(i*80));
                 textField[i].setSize(200,60);
                 p[1].add(textField[i]);
              }
              
              deleteButton.setForeground(new Color(255, 255, 255));
              deleteButton.setBackground(new Color(128, 128, 128));
              deleteButton.setFocusPainted(false);
              deleteButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 17));
              deleteButton.setLocation(50, 400);
              deleteButton.setSize(340,50);
              deleteButton.addActionListener(this);
              p[1].add(deleteButton);
              
              mainPanel.setLayout(new GridLayout(1,2));
              mainPanel.add(p[0]);
              mainPanel.add(p[1]);
              add(mainPanel);
           }
           
           @Override
           public void actionPerformed(ActionEvent e) {
              // TODO Auto-generated method stub
              if(e.getSource() == searchButton) {   //조회하기 버튼 누를 경우 모든 회원정보 조회
                
              }
              else if(e.getSource() == deleteButton) { //삭제하기 버튼 누를 경우 해당 회원 삭제
                
              }
           }
      }
      
      @Override
      public void actionPerformed(ActionEvent arg0) { //회원 관리 패널 액션리스너
         // TODO Auto-generated method stub
         if(arg0.getSource() == backButton) { //뒤로 가기 버튼 누를 경우
            card.show(c, "managerMenu"); //관리자 메뉴로 돌아감
         }
      }
   }
   
   /////////////////////////////예약 관리 패널/////////////////////////////////
   
   class ManagerReservationUI extends JPanel implements ActionListener{
      GoBackButton backButton = new GoBackButton();
      
      ManagerReservationUI(){
         setBackground(Color.lightGray);
          setLayout(null);
          setBounds(200, 150, 1000, 700);
          
          JLabel lb=new JLabel("예약정보 관리");
          lb.setHorizontalAlignment(JLabel.CENTER);
          lb.setFont(new Font("한컴산뜻돋움", Font.BOLD, 40));
          lb.setBounds(350,0,300,100);
          
          backButton.setBounds(10,10,80,80);
          backButton.addActionListener(this);
          
          JTabbedPane mainJtabUI = new JTabbedPane(JTabbedPane.TOP);
          mainJtabUI.setBounds(50,100,900,550);
          mainJtabUI.addTab("예약 조회", new ReservationSearch());
          
          add(lb);
          add(backButton);
          add(mainJtabUI);
          
          setVisible(true);
    }
    class ReservationSearch extends JPanel implements ActionListener{ //예약 조회
       JPanel mainPanel = new JPanel();
         JPanel p[] = new JPanel[2];
         JTextArea textArea = new JTextArea(29,87); //예약 조회 textArea
         JButton searchButton = new JButton("조회하기"); //조회 버튼
       
       ReservationSearch(){
          for(int i=0; i<p.length; i++) {
                  p[i] = new JPanel();
                  p[i].setBackground(new Color(176, 224, 230));
               }
          
          searchButton.setFocusPainted(false);
          searchButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 15));
          searchButton.setForeground(new Color(255, 255, 255));
          searchButton.setBackground(new Color(128, 128, 128));
          searchButton.addActionListener(this);
          
          p[0].add(searchButton);
          p[1].add(new JScrollPane(textArea));
               
          mainPanel.setLayout(new BorderLayout());
          mainPanel.add(p[0], BorderLayout.NORTH);
          mainPanel.add(p[1], BorderLayout.CENTER);
          add(mainPanel);
            
       }

       @Override
       public void actionPerformed(ActionEvent e) {
          // TODO Auto-generated method stub
          if(e.getSource() == searchButton) { //조회하기 버튼 누를 경우 현재 모든 예약 조회
        	  
          }
       }
    }
    
    @Override
     public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        if(arg0.getSource() == backButton) {
           card.show(c, "managerMenu");
        }
     }
      
   }
   
   ////////////////////////////항공 관리 패널////////////////////////////////
   
   class ManagerFlightUI extends JPanel implements ActionListener{
      GoBackButton backButton = new GoBackButton();
      
      ManagerFlightUI(){
            setBackground(Color.lightGray);
            setLayout(null);
            setBounds(200, 150, 1000, 700);
            
            JLabel lb=new JLabel("항공정보 관리");
            lb.setHorizontalAlignment(JLabel.CENTER);
            lb.setFont(new Font("한컴산뜻돋움", Font.BOLD, 40));
            lb.setBounds(350,0,300,100);
            
            backButton.setBounds(10,10,80,80);
            backButton.addActionListener(this);
            
            JTabbedPane mainJtabUI = new JTabbedPane(JTabbedPane.TOP);
            mainJtabUI.setBounds(50,100,900,550);
            mainJtabUI.addTab("항공 등록", new FlightCreate());
            mainJtabUI.addTab("항공 변경", new FlightUpdate());
            mainJtabUI.addTab("항공 삭제", new FlightDelete());
            
            add(lb);
            add(backButton);
            add(mainJtabUI);
            
            setVisible(true);
      }
      
      class FlightCreate extends JPanel implements ActionListener{ //항공 등록 패널
            JPanel mainPanel = new JPanel();
            JPanel p[] = new JPanel[2];
            JPanel searchPanel[] = new JPanel[2];
            JTextArea textArea = new JTextArea(29,43); //모든 항공 조회 textArea
            JLabel infoLabel[] = new JLabel[8]; //항공 등록 라벨
            String infoStr[] = {"항공편명","항공사명", "출발시간", "도착시간", "일반운임", "비즈니스운임", "출발공항", "도착공항"};
            JTextField textField[] = new JTextField[8]; //항공 등록 텍스트필드
            JButton createButton = new JButton("등록하기"); //등록하기 버튼
            JButton searchButton = new JButton("조회하기"); //조회하기 버튼
            
            FlightCreate(){
               for(int i=0; i<p.length; i++) {
                  p[i] = new JPanel();
                  p[i].setBackground(new Color(176, 224, 230));
                  searchPanel[i] = new JPanel();
                  searchPanel[i].setBackground(new Color(176, 224, 230));
               }
               
               p[0].setLayout(new BorderLayout());
               
               searchButton.setFocusPainted(false);
               searchButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 15));
               searchButton.setForeground(new Color(255, 255, 255));
               searchButton.setBackground(new Color(128, 128, 128));
               
               searchPanel[0].setLayout(new FlowLayout());
               searchButton.addActionListener(this);
               searchPanel[0].add(searchButton);
               searchPanel[1].add(new JScrollPane(textArea));
               
               p[0].add(searchPanel[0], BorderLayout.NORTH);
               p[0].add(searchPanel[1], BorderLayout.CENTER);
               
               
               p[1].setLayout(null);
               for(int i=0; i<infoLabel.length; i++) {
                  infoLabel[i] = new JLabel(infoStr[i]);
                  infoLabel[i].setHorizontalAlignment(JLabel.CENTER);
                  infoLabel[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 18));
               }
               for(int i=0; i<textField.length; i++) {
                  textField[i] = new JTextField();
               }
               for(int i=0; i<8; i++) {
                  infoLabel[i].setLocation(45, 23+(i*50));
                  infoLabel[i].setSize(120,60);
                  p[1].add(infoLabel[i]);
                  textField[i].setLocation(165, 35+(i*50));
                  textField[i].setSize(200,40);
                  p[1].add(textField[i]);
               }
               
               createButton.setFocusPainted(false);
               createButton.setForeground(new Color(255, 255, 255));
               createButton.setBackground(new Color(128, 128, 128));
               createButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 17));
               createButton.setLocation(50, 450);
               createButton.setSize(340,50);
               createButton.addActionListener(this);
               
               p[1].add(createButton);
               
               mainPanel.setLayout(new GridLayout(1,2));
               mainPanel.add(p[0]);
               mainPanel.add(p[1]);
               add(mainPanel);
            }
            @Override
            public void actionPerformed(ActionEvent e) {
               // TODO Auto-generated method stub
               if(e.getSource() == createButton) { //등록하기 버튼 누를 경우 해당 항공 등록
            	   
               }
               else if(e.getSource() == searchButton) { //조회하기 버튼 누를 경우 모든 항공 조회
            	   
               }
            }
      }
      
      class FlightUpdate extends JPanel implements ActionListener{ //항공 변경 패널
            JPanel mainPanel = new JPanel();
            JPanel p[] = new JPanel[2];
            JPanel searchPanel[] = new JPanel[2];
            JTextArea textArea = new JTextArea(29,43); //모든 항공 조회 textArea
            JLabel infoLabel[] = new JLabel[5]; //항공 변경 라벨
            String infoStr[] = {"항공편명","출발시간", "도착시간", "일반운임", "비즈니스운임"};
            JTextField textField[] = new JTextField[5]; //항공 변경 텍스트필드
            JButton updateButton = new JButton("변경하기"); //변경하기 버튼
            JButton searchButton = new JButton("조회하기"); //조회하기 버튼
            
            FlightUpdate(){
               for(int i=0; i<p.length; i++) {
                  p[i] = new JPanel();
                  p[i].setBackground(new Color(176, 224, 230));
                  searchPanel[i] = new JPanel();
                  searchPanel[i].setBackground(new Color(176, 224, 230));
               }
               
               p[0].setLayout(new BorderLayout());
               
               searchButton.setFocusPainted(false);
               searchButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 15));
               searchButton.setForeground(new Color(255, 255, 255));
               searchButton.setBackground(new Color(128, 128, 128));
               
               searchPanel[0].setLayout(new FlowLayout());
               searchButton.addActionListener(this);
               searchPanel[0].add(searchButton);
               searchPanel[1].add(new JScrollPane(textArea));
               
               p[0].add(searchPanel[0], BorderLayout.NORTH);
               p[0].add(searchPanel[1], BorderLayout.CENTER);
               
               
               p[1].setLayout(null);
               for(int i=0; i<infoLabel.length; i++) {
                  infoLabel[i] = new JLabel(infoStr[i]);
                  infoLabel[i].setHorizontalAlignment(JLabel.CENTER);
                  infoLabel[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 18));
               }
               for(int i=0; i<textField.length; i++) {
                  textField[i] = new JTextField();
               }
               for(int i=0; i<5; i++) {
                  infoLabel[i].setLocation(45, 35+(i*75));
                  infoLabel[i].setSize(120,80);
                  p[1].add(infoLabel[i]);
                  textField[i].setLocation(165, 50+(i*75));
                  textField[i].setSize(200,60);
                  p[1].add(textField[i]);
               }
               
               updateButton.setFocusPainted(false);
               updateButton.setForeground(new Color(255, 255, 255));
               updateButton.setBackground(new Color(128, 128, 128));
               updateButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 17));
               updateButton.setLocation(50, 450);
               updateButton.setSize(340,50);
               updateButton.addActionListener(this);
               
               p[1].add(updateButton);
               
               mainPanel.setLayout(new GridLayout(1,2));
               mainPanel.add(p[0]);
               mainPanel.add(p[1]);
               add(mainPanel);
            }
            @Override
            public void actionPerformed(ActionEvent e) {
               // TODO Auto-generated method stub
               if(e.getSource() == updateButton) { //변경하기 버튼 누를 경우 해당 항공 변경
            	   
               }
               else if(e.getSource() == searchButton) { //조회하기 버튼 누를 경우 모든 항공 조회
            	   
               }
            }
      }
      
      class FlightDelete extends JPanel implements ActionListener{ //항공 삭제 패널
            JPanel mainPanel = new JPanel();
            JPanel p[] = new JPanel[2];
            JPanel searchPanel[] = new JPanel[2];
            JTextArea textArea = new JTextArea(29,43); //모든 항공 조회 textArea
            JLabel infoLabel = new JLabel("항공편코드"); //항공 삭제 라벨
            JTextField textField = new JTextField(); //항공 삭제 텍스트필드
            JButton deleteButton = new JButton("삭제하기"); //삭제하기 버튼
            JButton searchButton = new JButton("조회하기"); //조회하기 버튼
            
            FlightDelete(){
               for(int i=0; i<p.length; i++) {
                  p[i] = new JPanel();
                  p[i].setBackground(new Color(176, 224, 230));
                  searchPanel[i] = new JPanel();
                  searchPanel[i].setBackground(new Color(176, 224, 230));
               }
                 
               p[0].setLayout(new BorderLayout());
               
               searchButton.setFocusPainted(false);
               searchButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 15));
               searchButton.setForeground(new Color(255, 255, 255));
               searchButton.setBackground(new Color(128, 128, 128));
                 
               searchPanel[0].setLayout(new FlowLayout());
               searchButton.addActionListener(this);
               searchPanel[0].add(searchButton);
               searchPanel[1].add(new JScrollPane(textArea));
                 
               p[0].add(searchPanel[0], BorderLayout.NORTH);
               p[0].add(searchPanel[1], BorderLayout.CENTER);
               
               p[1].setLayout(null);
               
               infoLabel.setHorizontalAlignment(JLabel.CENTER);
               infoLabel.setFont(new Font("한컴산뜻돋움", Font.BOLD, 18));
     
               infoLabel.setLocation(45, 160);
               infoLabel.setSize(120,90);
               p[1].add(infoLabel);
               textField.setLocation(185, 175);
               textField.setSize(200,60);
               p[1].add(textField);
               
               deleteButton.setFocusPainted(false);
               deleteButton.setForeground(new Color(255, 255, 255));
               deleteButton.setBackground(new Color(128, 128, 128));
               deleteButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 17));
               deleteButton.setLocation(50, 390);
               deleteButton.setSize(340,50);
               deleteButton.addActionListener(this);
               p[1].add(deleteButton);
               
               mainPanel.setLayout(new GridLayout(1,2));
               mainPanel.add(p[0]);
               mainPanel.add(p[1]);
               add(mainPanel);
            }
            @Override
            public void actionPerformed(ActionEvent e) {
               // TODO Auto-generated method stub
               if(e.getSource() == deleteButton) { //삭제하기 버튼 누를 경우 해당 항공 삭제
            	   
               }
               else if(e.getSource() == searchButton) { //조회하기 버튼 누를 경우 모든 항공 조회
            	   
               }
            }
      }
      
      @Override
      public void actionPerformed(ActionEvent e) {
         // TODO Auto-generated method stub
         if(e.getSource() == backButton) {
              card.show(c, "managerMenu");
           }
      }
   }
}