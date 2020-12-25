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
   BufferedImage backImage, menuImage1, menuImage2;
   BufferedImage Image3;
   
   ManagerMenuPanel managerMenuPanel = new ManagerMenuPanel();
   MemberManagerUI managerPanel = new MemberManagerUI();
   ManagerReservationUI reservationPanel = new ManagerReservationUI();
   
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
   
   class memberManageButton extends JButton{   //회원 관리 버튼
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
   
   class ManagerMenuPanel extends JPanel implements ActionListener{
     GoBackButton backButton = new GoBackButton();
      memberManageButton memButton = new memberManageButton();
      reservationManageButton resButton = new reservationManageButton();
      
      ManagerMenuPanel(){
         try {
            backImage = ImageIO.read(new File("Menu.png"));
           } catch (IOException e) {
              e.printStackTrace();
           }
         setLayout(null);
         
         backButton.setBounds(10,10,80,80);
         backButton.addActionListener(this);
         
         memButton.setBounds(280, 300, 180, 180);
         resButton.setBounds(530, 300, 180, 180);
         
         backButton.addActionListener(this);
         memButton.addActionListener(this);
         resButton.addActionListener(this);
         
         add(backButton);
         add(memButton);
         add(resButton);
      }
      protected void paintComponent(Graphics g) {
         super.paintComponents(g);
         g.drawImage(backImage,0,0,getWidth(),getHeight(),null);
      }
      @Override
      public void actionPerformed(ActionEvent e) {
         // TODO Auto-generated method stub
         if(e.getSource() == memButton) {
            card.show(c, "manager");
         }
         else if(e.getSource() == resButton) {
            card.show(c, "reservation");
         }
         else if(e.getSource() == backButton) {
            new LoginUIFrame();
            managerMenuExit();
         }
      }
   }

   //////////////////////////////////회원관리 패널///////////////////////////////
   
   class MemberManagerUI extends JPanel implements ActionListener{
      GoBackButton backButton = new GoBackButton();
      
      MemberManagerUI(){
      
         setBackground(Color.lightGray);
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
         mainJtabUI.addTab("회원 변경", new MemberUpdate());
         mainJtabUI.addTab("회원 삭제", new MemberDelete());
         
         add(lb);
         add(backButton);
         add(mainJtabUI);
         
         setVisible(true);
      }
      
      /////////////////////////회원 변경////////////////////////////////
      class MemberUpdate extends JPanel implements ActionListener{
         JPanel mainPanel = new JPanel();
         JPanel p[] = new JPanel[2];
         JPanel searchPanel[] = new JPanel[2];
         JTextArea textArea = new JTextArea(29,43);
         JLabel infoLabel[] = new JLabel[6];
         JTextField textField[] = new JTextField[6];
         String infoStr[] = {"이름","아이디", "비밀번호", "이메일", "생년월일", "전화번호"};
         JButton updateButton = new JButton("변경하기");
         JButton searchButton = new JButton("조회하기");
         
         MemberUpdate(){
            for(int i=0; i<p.length; i++) {
               p[i] = new JPanel();
               p[i].setBackground(new Color(176, 224, 230));
               searchPanel[i] = new JPanel();
               searchPanel[i].setBackground(new Color(176, 224, 230));
            }
            
            p[0].setLayout(new BorderLayout());
            
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
            for(int i=0; i<6; i++) {
               infoLabel[i].setLocation(50, 20+(i*65));
               infoLabel[i].setSize(80,80);
               p[1].add(infoLabel[i]);
               textField[i].setLocation(160, 35+(i*65));
               textField[i].setSize(200,60);
               p[1].add(textField[i]);
            }
            
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
         if(e.getSource() == searchButton) {   //모든 회원정보 조회하기
            
         }
         else if(e.getSource() == updateButton) { //회원정보 변경하기
            
         }
      }
      }
      
      /////////////////////////회원 삭제////////////////////////////////
      class MemberDelete extends JPanel implements ActionListener{
         JPanel mainPanel = new JPanel();
         JPanel p[] = new JPanel[2];
         JPanel searchPanel[] = new JPanel[2];
         JTextArea textArea = new JTextArea(29,43);
         JLabel infoLabel[] = new JLabel[2];
         JTextField textField[] = new JTextField[2];
         String infoStr[] = {"아이디", "비밀번호"};
         JButton deleteButton = new JButton("삭제하기");
         JButton searchButton = new JButton("조회하기");
         
         MemberDelete(){
            for(int i=0; i<p.length; i++) {
               p[i] = new JPanel();
               p[i].setBackground(new Color(176, 224, 230));
               searchPanel[i] = new JPanel();
               searchPanel[i].setBackground(new Color(176, 224, 230));
            }
              
            p[0].setLayout(new BorderLayout());
            
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
         if(e.getSource() == searchButton) {   //모든 회원정보 조회하기
            
         }
         else if(e.getSource() == deleteButton) { //회원 삭제하기
            
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
    class ReservationSearch extends JPanel implements ActionListener{
       JPanel mainPanel = new JPanel();
         JPanel p[] = new JPanel[2];
         JTextArea textArea = new JTextArea(29,87);
         JButton searchButton = new JButton("조회하기");
       
       ReservationSearch(){
          for(int i=0; i<p.length; i++) {
                  p[i] = new JPanel();
                  p[i].setBackground(new Color(176, 224, 230));
               }
          
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

}