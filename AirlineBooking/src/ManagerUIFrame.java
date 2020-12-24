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
   MenuPanel menuPanel = new MenuPanel();
   ManagerMemberUI managerPanel = new ManagerMemberUI();
   
   Container c;
   CardLayout card;
         
   ManagerUIFrame(){
      setTitle("관리자 페이지");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      c = this.getContentPane();
      card = new CardLayout();
      c.setLayout(card);
      c.add(menuPanel, "menu");
      c.add(managerPanel, "manager");
      
      setBounds(100, 100, 1000, 700);
      setVisible(true);
   }
   public void managerMunuExit() {
      this.dispose();
   }
   class MenuPanel extends JPanel implements ActionListener{
      memberManageButton memButton = new memberManageButton();
      reservationManageButton resButton = new reservationManageButton();
      
      MenuPanel(){
         try {
            backImage = ImageIO.read(new File("Menu.png"));
           } catch (IOException e) {
              e.printStackTrace();
           }
         setLayout(null);
         
         memButton.setBounds(280, 300, 180, 180);
         resButton.setBounds(530, 300, 180, 180);
			
         memButton.addActionListener(this);
         resButton.addActionListener(this);
         
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
      }
   }
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
}
class ManagerMemberUI extends JPanel implements ActionListener{
   JButton button = new JButton();
   
   ManagerMemberUI(){
      setBackground(Color.lightGray);
      setLayout(new FlowLayout());
      
       JTabbedPane mainJtabUI = new JTabbedPane(JTabbedPane.TOP);
         
       mainJtabUI.addTab("회원 변경", new MemberUpdate());
       mainJtabUI.addTab("회원 삭제", new MemberDelete());
       
       add(mainJtabUI);
       
       setBounds(200, 150, 1000, 700);
       setVisible(true);
   }
   class MemberUpdate extends JPanel{
         JPanel mainPanel = new JPanel();
         JPanel p[] = new JPanel[2];
         JTextArea textArea = new JTextArea(37,38);
         JLabel infoLabel[] = new JLabel[5];
         JTextField textField[] = new JTextField[5];
         String infoStr[] = {"아이디", "비밀번호", "이메일", "생년월일", "전화번호"};
         JButton button = new JButton("변경하기");
         
         MemberUpdate(){
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
            for(int i=0; i<5; i++) {
               infoLabel[i].setLocation(50, 60+(i*80));
               infoLabel[i].setSize(80,80);
               p[1].add(infoLabel[i]);
               textField[i].setLocation(190, 75+(i*80));
               textField[i].setSize(200,60);
               p[1].add(textField[i]);
            }
            button.setForeground(new Color(255, 255, 255));
            button.setBackground(new Color(128, 128, 128));
            button.setFont(new Font("한컴산뜻돋움", Font.BOLD, 17));
            button.setLocation(50, 500);
            button.setSize(340,50);
            p[1].add(button);
            
            mainPanel.setLayout(new GridLayout(1,2));
            mainPanel.add(p[0]);
            mainPanel.add(p[1]);
            add(mainPanel);
         }
      }
      class MemberDelete extends JPanel{
         JPanel mainPanel = new JPanel();
         JPanel p[] = new JPanel[2];
         JTextArea textArea = new JTextArea(37,38);
         JLabel infoLabel[] = new JLabel[2];
         JTextField textField[] = new JTextField[2];
         String infoStr[] = {"아이디", "비밀번호"};
         JButton button = new JButton("삭제하기");
         
         MemberDelete(){
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
            for(int i=0; i<2; i++) {
               infoLabel[i].setLocation(50, 190+(i*80));
               infoLabel[i].setSize(80,80);
               p[1].add(infoLabel[i]);
               textField[i].setLocation(190, 205+(i*80));
               textField[i].setSize(200,60);
               p[1].add(textField[i]);
            }
            button.setForeground(new Color(255, 255, 255));
            button.setBackground(new Color(128, 128, 128));
            button.setFont(new Font("한컴산뜻돋움", Font.BOLD, 17));
            button.setLocation(50, 400);
            button.setSize(340,50);
            p[1].add(button);
            
            mainPanel.setLayout(new GridLayout(1,2));
            mainPanel.add(p[0]);
            mainPanel.add(p[1]);
            add(mainPanel);
         }
      }
      @Override
      public void actionPerformed(ActionEvent arg0) {
         // TODO Auto-generated method stub
         if(arg0.getSource() == button) {
            
         }
      }
}


