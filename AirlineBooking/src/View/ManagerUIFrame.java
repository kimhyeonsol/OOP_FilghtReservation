package View;
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
   public BufferedImage backImage, menuImage1, menuImage2, menuImage3;
   public BufferedImage backButtonImg, pageImg;
   
   public ManagerMenuPanel managerMenuPanel = new ManagerMenuPanel();
   public MemberManagerUI managerPanel = new MemberManagerUI();
   public ManagerReservationUI reservationPanel = new ManagerReservationUI();
   public ManagerFlightUI flightPanel = new ManagerFlightUI();
   
   String font = "함초롬돋움";
   
   public Container c;
   public CardLayout card;
         
   public ManagerUIFrame(){
      setTitle("관리자 페이지");
      setBounds(250, 50, 1000, 700);
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
            	backButtonImg = ImageIO.read(new File("뒤로가기.png"));
              } catch (IOException e) {
               e.printStackTrace();
            }
       }
       protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backButtonImg,0,0,getWidth(),getHeight(),null);
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
         public GoBackButton backButton = new GoBackButton();
         public memberManageButton memButton = new memberManageButton();
         public reservationManageButton resButton = new reservationManageButton();
         public flightManageButton fliButton = new flightManageButton();
         
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
        public GoBackButton backButton = new GoBackButton();
        
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
          lb.setFont(new Font("맑은고딕", Font.BOLD, 40));
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
    	  public JPanel mainPanel = new JPanel();
          public JPanel p[] = new JPanel[2];
          public JPanel searchPanel[] = new JPanel[2];
          public JTextArea textArea = new JTextArea(29,43); //회원 조회 textArea
          public JLabel infoLabel[] = new JLabel[2]; //회원 삭제 라벨
          public String infoStr[] = {"아이디", "비밀번호"};
          public JTextField memDeletetextField[] = new JTextField[2]; //회원 삭제 텍스트필드
          public JButton memDeleteButton = new JButton("삭제하기"); //삭제하기 버튼
          public JButton memSearchButton = new JButton("조회하기"); //조회하기 버튼
          
          MemberDelete(){
        	  
              for(int i=0; i<p.length; i++) {
                 p[i] = new JPanel();
                 p[i].setBackground(new Color(209, 233, 255));
                 searchPanel[i] = new JPanel();
                 searchPanel[i].setBackground(new Color(209, 233, 255));
              }
                
              p[0].setLayout(new BorderLayout());
              
              memSearchButton.setFocusPainted(false);
              memSearchButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
              memSearchButton.setForeground(new Color(255, 255, 255));
              memSearchButton.setBackground(new Color(128, 128, 128));
                
              searchPanel[0].setLayout(new FlowLayout());
              memSearchButton.addActionListener(this);
              searchPanel[0].add(memSearchButton);
              
              textArea.setEditable(false);
              searchPanel[1].add(new JScrollPane(textArea));
                
              p[0].add(searchPanel[0], BorderLayout.NORTH);
              p[0].add(searchPanel[1], BorderLayout.CENTER);
              
              p[1].setLayout(null);
              for(int i=0; i<infoLabel.length; i++) {
                 infoLabel[i] = new JLabel(infoStr[i]);
                 infoLabel[i].setHorizontalAlignment(JLabel.CENTER);
                 infoLabel[i].setFont(new Font("맑은고딕", Font.BOLD, 18));
              }
              for(int i=0; i<memDeletetextField.length; i++) {
            	  memDeletetextField[i] = new JTextField();
              }
              for(int i=0; i<2; i++) {
                 infoLabel[i].setLocation(50, 160+(i*80));
                 infoLabel[i].setSize(80,80);
                 p[1].add(infoLabel[i]);
                 memDeletetextField[i].setLocation(190, 175+(i*80));
                 memDeletetextField[i].setSize(200,60);
                 p[1].add(memDeletetextField[i]);
              }
              
              memDeleteButton.setForeground(new Color(255, 255, 255));
              memDeleteButton.setBackground(new Color(128, 128, 128));
              memDeleteButton.setFocusPainted(false);
              memDeleteButton.setFont(new Font("맑은고딕", Font.BOLD, 17));
              memDeleteButton.setLocation(50, 400);
              memDeleteButton.setSize(340,50);
              memDeleteButton.addActionListener(this);
              p[1].add(memDeleteButton);
              
              mainPanel.setLayout(new GridLayout(1,2));
              mainPanel.add(p[0]);
              mainPanel.add(p[1]);
              add(mainPanel);
           }
           
           @Override
           public void actionPerformed(ActionEvent e) {
              // TODO Auto-generated method stub
              if(e.getSource() == memSearchButton) {   //조회하기 버튼 누를 경우 모든 회원정보 조회
                
              }
              else if(e.getSource() == memDeleteButton) { //삭제하기 버튼 누를 경우 해당 회원 삭제
                
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
      public GoBackButton backButton = new GoBackButton();
      
      ManagerReservationUI(){
    	  try {
      		  pageImg = ImageIO.read(new File("pageImg.jpg"));
          } catch (IOException e) {
                e.printStackTrace();
          }
          setLayout(null);
          setBounds(200, 150, 1000, 700);
          
          JLabel lb=new JLabel("예약정보 관리");
          lb.setHorizontalAlignment(JLabel.CENTER);
          lb.setFont(new Font("맑은고딕", Font.BOLD, 40));
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
    protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          g.drawImage(pageImg,0,0,getWidth(),getHeight(),null);
    }
    
    class ReservationSearch extends JPanel implements ActionListener{ //예약 조회
    	public JPanel mainPanel = new JPanel();
		public JPanel p[] = new JPanel[2];
		public JTextArea textArea = new JTextArea(29,87); //예약 조회 textArea
		public JButton reservationSearchButton = new JButton("조회하기"); //조회 버튼
       
       ReservationSearch(){
          for(int i=0; i<p.length; i++) {
                  p[i] = new JPanel();
                  p[i].setBackground(new Color(209, 233, 255));
               }
          
          reservationSearchButton.setFocusPainted(false);
          reservationSearchButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
          reservationSearchButton.setForeground(new Color(255, 255, 255));
          reservationSearchButton.setBackground(new Color(128, 128, 128));
          reservationSearchButton.addActionListener(this);
          
          p[0].add(reservationSearchButton);
          textArea.setEditable(false);
          p[1].add(new JScrollPane(textArea));
               
          mainPanel.setLayout(new BorderLayout());
          mainPanel.add(p[0], BorderLayout.NORTH);
          mainPanel.add(p[1], BorderLayout.CENTER);
          add(mainPanel);
            
       }

       @Override
       public void actionPerformed(ActionEvent e) {
          // TODO Auto-generated method stub
          if(e.getSource() == reservationSearchButton) { //조회하기 버튼 누를 경우 현재 모든 예약 조회
        	  
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
      public GoBackButton backButton = new GoBackButton();
      
      ManagerFlightUI(){
    	    try {
      		    pageImg = ImageIO.read(new File("pageImg.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setBackground(Color.lightGray);
            setLayout(null);
            setBounds(200, 150, 1000, 700);
            
            JLabel lb=new JLabel("항공정보 관리");
            lb.setHorizontalAlignment(JLabel.CENTER);
            lb.setFont(new Font("맑은고딕", Font.BOLD, 40));
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
      protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          g.drawImage(pageImg,0,0,getWidth(),getHeight(),null);
      }
      
      class FlightCreate extends JPanel implements ActionListener{ //항공 등록 패널
            public JPanel mainPanel = new JPanel();
            public JPanel p[] = new JPanel[2];
            public JPanel searchPanel[] = new JPanel[2];
            public JTextArea textArea = new JTextArea(29,43); //모든 항공 조회 textArea
            public JLabel infoLabel[] = new JLabel[7]; //항공 등록 라벨
            public String infoStr[] = {"항공사명", "출발시간", "도착시간", "일반운임", "비즈니스운임", "출발공항", "도착공항"};
            public JTextField fliCreatetextField[] = new JTextField[7]; //항공 등록 텍스트필드
            public JButton flightCreateButton = new JButton("등록하기"); //등록하기 버튼
            public JButton flightSearchButton = new JButton("조회하기"); //조회하기 버튼
            
            FlightCreate(){
               for(int i=0; i<p.length; i++) {
                  p[i] = new JPanel();
                  p[i].setBackground(new Color(209, 233, 255));
                  searchPanel[i] = new JPanel();
                  searchPanel[i].setBackground(new Color(209, 233, 255));
               }
               
               p[0].setLayout(new BorderLayout());
               
               flightSearchButton.setFocusPainted(false);
               flightSearchButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
               flightSearchButton.setForeground(new Color(255, 255, 255));
               flightSearchButton.setBackground(new Color(128, 128, 128));
               
               searchPanel[0].setLayout(new FlowLayout());
               flightSearchButton.addActionListener(this);
               searchPanel[0].add(flightSearchButton);
               textArea.setEditable(false);
               searchPanel[1].add(new JScrollPane(textArea));
               
               p[0].add(searchPanel[0], BorderLayout.NORTH);
               p[0].add(searchPanel[1], BorderLayout.CENTER);
               
               
               p[1].setLayout(null);
               for(int i=0; i<infoLabel.length; i++) {
                  infoLabel[i] = new JLabel(infoStr[i]);
                  infoLabel[i].setHorizontalAlignment(JLabel.CENTER);
                  infoLabel[i].setFont(new Font("맑은고딕", Font.BOLD, 18));
               }
               for(int i=0; i<fliCreatetextField.length; i++) {
            	   fliCreatetextField[i] = new JTextField();
               }
               for(int i=0; i<7; i++) {
                  infoLabel[i].setLocation(45, 45+(i*55));
                  infoLabel[i].setSize(120,60);
                  p[1].add(infoLabel[i]);
                  fliCreatetextField[i].setLocation(165, 57+(i*55));
                  fliCreatetextField[i].setSize(200,40);
                  p[1].add(fliCreatetextField[i]);
               }
               
               flightCreateButton.setFocusPainted(false);
               flightCreateButton.setForeground(new Color(255, 255, 255));
               flightCreateButton.setBackground(new Color(128, 128, 128));
               flightCreateButton.setFont(new Font("맑은고딕", Font.BOLD, 17));
               flightCreateButton.setLocation(50, 450);
               flightCreateButton.setSize(340,50);
               flightCreateButton.addActionListener(this);
               
               p[1].add(flightCreateButton);
               
               mainPanel.setLayout(new GridLayout(1,2));
               mainPanel.add(p[0]);
               mainPanel.add(p[1]);
               add(mainPanel);
            }
            @Override
            public void actionPerformed(ActionEvent e) {
               // TODO Auto-generated method stub
               if(e.getSource() == flightCreateButton) { //등록하기 버튼 누를 경우 해당 항공 등록
            	   
               }
               else if(e.getSource() == flightSearchButton) { //조회하기 버튼 누를 경우 모든 항공 조회
            	   
               }
            }
      }
      
      class FlightUpdate extends JPanel implements ActionListener{ //항공 변경 패널
            public JPanel mainPanel = new JPanel();
            public JPanel p[] = new JPanel[2];
            public JPanel searchPanel[] = new JPanel[2];
            public JTextArea textArea = new JTextArea(29,43); //모든 항공 조회 textArea
            public JLabel infoLabel[] = new JLabel[5]; //항공 변경 라벨
            public String infoStr[] = {"항공편코드","출발시간", "도착시간", "일반운임", "비즈니스운임"};
            public JTextField fliUpdatetextField[] = new JTextField[5]; //항공 변경 텍스트필드
            public JButton flightUpdateButton = new JButton("변경하기"); //변경하기 버튼
            public JButton flightSearchButton = new JButton("조회하기"); //조회하기 버튼
            
            FlightUpdate(){
               for(int i=0; i<p.length; i++) {
                  p[i] = new JPanel();
                  p[i].setBackground(new Color(209, 233, 255));
                  searchPanel[i] = new JPanel();
                  searchPanel[i].setBackground(new Color(209, 233, 255));
               }
               
               p[0].setLayout(new BorderLayout());
               
               flightSearchButton.setFocusPainted(false);
               flightSearchButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
               flightSearchButton.setForeground(new Color(255, 255, 255));
               flightSearchButton.setBackground(new Color(128, 128, 128));
               
               searchPanel[0].setLayout(new FlowLayout());
               flightSearchButton.addActionListener(this);
               searchPanel[0].add(flightSearchButton);
               textArea.setEditable(false);
               searchPanel[1].add(new JScrollPane(textArea));
               
               p[0].add(searchPanel[0], BorderLayout.NORTH);
               p[0].add(searchPanel[1], BorderLayout.CENTER);
               
               
               p[1].setLayout(null);
               for(int i=0; i<infoLabel.length; i++) {
                  infoLabel[i] = new JLabel(infoStr[i]);
                  infoLabel[i].setHorizontalAlignment(JLabel.CENTER);
                  infoLabel[i].setFont(new Font("맑은고딕", Font.BOLD, 18));
               }
               for(int i=0; i<fliUpdatetextField.length; i++) {
            	   fliUpdatetextField[i] = new JTextField();
               }
               for(int i=0; i<5; i++) {
                  infoLabel[i].setLocation(45, 35+(i*75));
                  infoLabel[i].setSize(120,80);
                  p[1].add(infoLabel[i]);
                  fliUpdatetextField[i].setLocation(165, 50+(i*75));
                  fliUpdatetextField[i].setSize(200,60);
                  p[1].add(fliUpdatetextField[i]);
               }
               
               flightUpdateButton.setFocusPainted(false);
               flightUpdateButton.setForeground(new Color(255, 255, 255));
               flightUpdateButton.setBackground(new Color(128, 128, 128));
               flightUpdateButton.setFont(new Font("맑은고딕", Font.BOLD, 17));
               flightUpdateButton.setLocation(50, 450);
               flightUpdateButton.setSize(340,50);
               flightUpdateButton.addActionListener(this);
               
               p[1].add(flightUpdateButton);
               
               mainPanel.setLayout(new GridLayout(1,2));
               mainPanel.add(p[0]);
               mainPanel.add(p[1]);
               add(mainPanel);
            }
            @Override
            public void actionPerformed(ActionEvent e) {
               // TODO Auto-generated method stub
               if(e.getSource() == flightUpdateButton) { //변경하기 버튼 누를 경우 해당 항공 변경
            	   
               }
               else if(e.getSource() == flightSearchButton) { //조회하기 버튼 누를 경우 모든 항공 조회
            	   
               }
            }
      }
      
      class FlightDelete extends JPanel implements ActionListener{ //항공 삭제 패널
            public JPanel mainPanel = new JPanel();
            public JPanel p[] = new JPanel[2];
            public JPanel searchPanel[] = new JPanel[2];
            public JTextArea textArea = new JTextArea(29,43); //모든 항공 조회 textArea
            public JLabel infoLabel = new JLabel("항공편코드"); //항공 삭제 라벨
            public JTextField fliDeletetextField = new JTextField(); //항공 삭제 텍스트필드
            public JButton flightDeleteButton = new JButton("삭제하기"); //삭제하기 버튼
            public JButton flightSearchButton = new JButton("조회하기"); //조회하기 버튼
            
            FlightDelete(){
               for(int i=0; i<p.length; i++) {
                  p[i] = new JPanel();
                  p[i].setBackground(new Color(209, 233, 255));
                  searchPanel[i] = new JPanel();
                  searchPanel[i].setBackground(new Color(209, 233, 255));
               }
                 
               p[0].setLayout(new BorderLayout());
               
               flightSearchButton.setFocusPainted(false);
               flightSearchButton.setFont(new Font("맑은고딕", Font.BOLD, 15));
               flightSearchButton.setForeground(new Color(255, 255, 255));
               flightSearchButton.setBackground(new Color(128, 128, 128));
                 
               searchPanel[0].setLayout(new FlowLayout());
               flightSearchButton.addActionListener(this);
               searchPanel[0].add(flightSearchButton);
               textArea.setEditable(false);
               searchPanel[1].add(new JScrollPane(textArea));
                 
               p[0].add(searchPanel[0], BorderLayout.NORTH);
               p[0].add(searchPanel[1], BorderLayout.CENTER);
               
               p[1].setLayout(null);
               
               infoLabel.setHorizontalAlignment(JLabel.CENTER);
               infoLabel.setFont(new Font("맑은고딕", Font.BOLD, 18));
     
               infoLabel.setLocation(45, 160);
               infoLabel.setSize(120,90);
               p[1].add(infoLabel);
               fliDeletetextField.setLocation(185, 175);
               fliDeletetextField.setSize(200,60);
               p[1].add(fliDeletetextField);
               
               flightDeleteButton.setFocusPainted(false);
               flightDeleteButton.setForeground(new Color(255, 255, 255));
               flightDeleteButton.setBackground(new Color(128, 128, 128));
               flightDeleteButton.setFont(new Font("맑은고딕", Font.BOLD, 17));
               flightDeleteButton.setLocation(50, 390);
               flightDeleteButton.setSize(340,50);
               flightDeleteButton.addActionListener(this);
               p[1].add(flightDeleteButton);
               
               mainPanel.setLayout(new GridLayout(1,2));
               mainPanel.add(p[0]);
               mainPanel.add(p[1]);
               add(mainPanel);
            }
            @Override
            public void actionPerformed(ActionEvent e) {
               // TODO Auto-generated method stub
               if(e.getSource() == flightDeleteButton) { //삭제하기 버튼 누를 경우 해당 항공 삭제
            	   
               }
               else if(e.getSource() == flightSearchButton) { //조회하기 버튼 누를 경우 모든 항공 조회
            	   
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
   public void addButtonActionListener(ActionListener listener) {
//	   userMenuPanel.backButton.addActionListener(listener);
   }
   
}