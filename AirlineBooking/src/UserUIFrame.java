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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UserUIFrame extends JFrame{// user 프레임(카드레이 아웃)
   
   Container c;
   CardLayout card;
   
   BufferedImage backImage, menuImage1, menuImage2;
   BufferedImage Image1, Image2, Image3;
   
   String _userId="";
   int resNum=0;
   
/////////////////////////////////////////////////////////////////////////////////////
   
   UserUIFrame(String userId){//사용자에 대한 매개변수 추가해야함.(ID값)
	  _userId=userId;
	  System.out.println(_userId);
      setTitle(userId+" 페이지");
      setBounds(100, 100, 1000, 700);
      this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
      setBackground(Color.black);
      
      c=this.getContentPane();
      card=new CardLayout();
      
      this.setLayout(card);
      
      UserMenuPanel userMenuPanel=new UserMenuPanel();
      MyInfoPanel myInfoPanel=new MyInfoPanel();
      FlightResPanel flightResPanel=new FlightResPanel();
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
              add(lb);
              
              backButton.setHorizontalAlignment(JLabel.CENTER);
              backButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 40));
              backButton.setBounds(10,10,80,80);
              backButton.addActionListener(this);
              add(backButton);
              
              JTabbedPane mainJtabUI = new JTabbedPane(JTabbedPane.TOP);
              mainJtabUI.setBounds(50,100,900,550);
              mainJtabUI.addTab("내 정보 수정하기", new MyInfoUpdatePanel());
              mainJtabUI.addTab("내 항공편 예약 현황", new MyReservationUpdatePanel());
              add(mainJtabUI);
               
              setVisible(true);
         }
         ////////////////////////////////////////////////////////
         class MyInfoUpdatePanel extends JPanel implements ActionListener{//내정보 수정
        	 JPanel mainPanel = new JPanel();
        	 JPanel p[] = new JPanel[2];
        	 
        	 JPanel savePanel[] = new JPanel[3];
             JButton saveButton = new JButton("내 컴퓨터에 문서 저장하기");
             
             JTextArea textArea = new JTextArea(26,43);
             JLabel infoLabel[] = new JLabel[6];
             JTextField textField[] = new JTextField[6];
             String infoStr[] = {"이름","아이디", "비밀번호", "이메일", "생년월일", "전화번호"};
             JButton updateButton = new JButton("변경하기");
             JButton cancelButton = new JButton("탈퇴하기");
             
             JFileChooser chooser=new JFileChooser();
             String pathName;
             
             MyInfoUpdatePanel(){
                for(int i=0; i<p.length; i++) {
                   p[i] = new JPanel();
                   p[i].setBackground(new Color(176, 224, 230));
                }
                
                //왼쪽 창
                p[0].setLayout(new BorderLayout());
                for(int i=0; i<savePanel.length; i++) {
                	savePanel[i] = new JPanel();
                	savePanel[i].setBackground(new Color(176, 224, 230));
                }
                
                savePanel[0].setLayout(new GridLayout(2,1));
                
                JLabel lb1=new JLabel("아이디/ 비밀번호 txt로 저장");
                lb1.setHorizontalAlignment(JLabel.CENTER);
                lb1.setFont(new Font("한컴산뜻돋움", Font.BOLD, 18));
                JLabel lb2=new JLabel("(미리보기)");
                savePanel[0].add(lb1);
                savePanel[0].add(lb2);
                
                savePanel[1].add(new JScrollPane(textArea));
                
                savePanel[2].setLayout(new FlowLayout());
                saveButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 15));
                saveButton.setForeground(new Color(255, 255, 255));
                saveButton.setBackground(new Color(128, 128, 128));
                saveButton.addActionListener(this);
                savePanel[2].add(saveButton);
                
                p[0].add(savePanel[0], BorderLayout.NORTH);
                p[0].add(savePanel[1], BorderLayout.CENTER);
                p[0].add(savePanel[2], BorderLayout.SOUTH);
                
                
                
                //오른쪽 창
                p[1].setLayout(null);
                
                JLabel lb3=new JLabel("내 정보");
                lb3.setBounds(200,0,100,50);
                lb3.setHorizontalAlignment(JLabel.CENTER);
                lb3.setFont(new Font("한컴산뜻돋움", Font.BOLD, 18));
                p[1].add(lb3);
                
                for(int i=0; i<infoLabel.length; i++) {
                   infoLabel[i] = new JLabel(infoStr[i]);
                   infoLabel[i].setHorizontalAlignment(JLabel.CENTER);
                   infoLabel[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 16));
                }
                for(int i=0; i<textField.length; i++) {
                   textField[i] = new JTextField();
                }
                for(int i=0; i<6; i++) {
                   infoLabel[i].setLocation(65, 40+(i*60));
                   infoLabel[i].setSize(80,70);
                   p[1].add(infoLabel[i]);
                   textField[i].setLocation(175, 50+(i*60));
                   textField[i].setSize(200,55);
                   p[1].add(textField[i]);
                }
                
                textField[1].setText(_userId);
                textField[1].setEditable(false);
                String memo= new String("*항공예약시스템에 저장된 내 정보*\r\n\r\n");
                for(int i=0; i<6; i++) {
                	memo+=(infoStr[i]+": "+textField[i].getText()+"\r\n");
                }
                textArea.setText(memo);
                
                updateButton.setForeground(new Color(255, 255, 255));
                updateButton.setBackground(new Color(128, 128, 128));
                updateButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 17));
                updateButton.setLocation(50, 430);
                updateButton.setSize(340,40);
                updateButton.addActionListener(this);
                p[1].add(updateButton);
                
                cancelButton.setForeground(Color.yellow);
                cancelButton.setBackground(new Color(128, 128, 128));
                cancelButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 12));
                cancelButton.setLocation(280, 475);
                cancelButton.setSize(110,30);
                cancelButton.addActionListener(this);
                p[1].add(cancelButton);
                
                mainPanel.setLayout(new GridLayout(1,2));
                mainPanel.add(p[0]);
                mainPanel.add(p[1]);
                add(mainPanel);
               }

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==saveButton) {
					 int ret=chooser.showSaveDialog(null);
		                if(ret!=JFileChooser.APPROVE_OPTION) {
		                      JOptionPane.showMessageDialog(null, "경로를 선택하지 않았습니다");
		                    return;
		                }
		                 pathName=chooser.getSelectedFile().getPath();
		                 File wfile=new File(pathName);
		                 try {
		                  BufferedWriter writer=new BufferedWriter(new FileWriter(wfile));
		                  String s;
		                  s=textArea.getText();
		                  writer.write(s+"\r\n");
		                  writer.close();
		               }catch(Exception e1) {
		                  e1.printStackTrace();
		               }
		               System.out.println("SAVE Done...");
				}
				if(e.getSource()==updateButton) {
					String memo= new String("*항공예약시스템에 저장된 내 정보*\r\n\r\n");
	                for(int i=0; i<6; i++) {
	                	memo+=(infoStr[i]+": "+textField[i].getText()+"\r\n");
	                }
	                textArea.setText(memo);
				}
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
         JTextArea textArea = new JTextArea();
         JTextField tf2[]=new JTextField[3];
         
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
                	  this.setLayout(null);
                	  add(new FlightSearchPanel());
                	  JScrollPane scroll=new JScrollPane(textArea);
                	  add(scroll);
                	  scroll.setBounds(50,200,800,250);
                	  add(new FlightSelectPanel());
                  }
            }
            class FlightSearchPanel extends JPanel implements ActionListener{
            	
            	JLabel titleLb=new JLabel("항공권 검색");
            	
            	JComboBox combo=new JComboBox();
            	String comboStr[]= {"인천","김포","제주","대구","김해"};
            	JRadioButton radio[]=new JRadioButton[2];
            	String radioStr[]= {"편도","왕복"};
            	
            	
            	JLabel lb[]=new JLabel[3];
            	String lbStr[]= {"가는 날","오는 날","가는 인원"};
            	
            	JLabel lb2=new JLabel("출발 공항");
            	
            	
           
            	FlightSearchPanel(){
              	  this.setLayout(null);
              	  //this.setBackground();
              	  this.setBounds(0,0,900,200);
              	  titleLb.setBounds(14,14,200,20);
              	  titleLb.setFont(new Font("한컴산뜻돋움", Font.BOLD, 20));
              	  add(titleLb);
              	  
	              lb2.setHorizontalAlignment(JLabel.CENTER);
	              lb2.setFont(new Font("한컴산뜻돋움", Font.BOLD, 16));
	              lb2.setBounds(30,60,100,40);
	              add(lb2);
              	  for(int i=0;i<comboStr.length;i++) {
              		  combo.addItem(comboStr[i]);
              	  }
              	  combo.setBounds(160,60,200,50);
              	combo.setFont(new Font("한컴산뜻돋움", Font.BOLD, 16));
              	  add(combo);
              	  
              	for(int i=0; i<radio.length; i++) {
	              	radio[i] = new JRadioButton(radioStr[i]);
	              	radio[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 16));
	              	radio[i].setBounds(120+(100*i),125,100,50);
	              	add(radio[i]);
	             }
	            for(int i=0; i<lb.length; i++) {
	              lb[i] = new JLabel(lbStr[i]);
	              lb[i].setHorizontalAlignment(JLabel.CENTER);
	              lb[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 16));
	            }
	               for(int i=0; i<tf2.length; i++) {
	            	   tf2[i] = new JTextField();
	               }
	               for(int i=0; i<lb.length; i++) {
	                lb[i].setLocation(450, 50+(i*45));
	                lb[i].setSize(80,40);
	                add(lb[i]);
	                tf2[i].setLocation(625, 50+(i*50));
	                tf2[i].setSize(200,40);
	                add(tf2[i]);
	              }
	               
              }

				@Override
		     public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					
				}
            	
            }
            class FlightSelectPanel extends JPanel implements ActionListener{
            	JLabel lb=new JLabel("선택할 항공권 ID: ");
            	JTextField tf=new JTextField();
            	JButton btn=new JButton("좌석선택하기");
            	JButton btn2=new JButton("예약하기");
            	
            	
            	FlightSelectPanel(){
              	  this.setLayout(null);
              	  this.setBounds(0,450,900,100);
              	  lb.setBounds(100,15,200,20);
              	  lb.setFont(new Font("한컴산뜻돋움", Font.BOLD, 20));
            	  add(lb);
            	  
            	  tf.setBounds(280,10,200,40);
            	  tf.setFont(new Font("한컴산뜻돋움", Font.BOLD, 20));
            	  add(tf);
            	  
            	  btn.setHorizontalAlignment(JLabel.CENTER);
            	  btn.setFont(new Font("한컴산뜻돋움", Font.BOLD, 20));
            	  btn.setBounds(510,10,180,40);
            	  btn.addActionListener(this);
            	  add(btn);
            	  
            	  btn2.setHorizontalAlignment(JLabel.CENTER);
            	  btn2.setFont(new Font("한컴산뜻돋움", Font.BOLD, 20));
            	  btn2.setBounds(700,10,150,40);
            	  btn2.addActionListener(this);
            	  add(btn2);
            	  
                }

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(e.getSource()==btn) {
						resNum=Integer.valueOf(tf2[2].getText());
						new SelectSeatUIFrame(resNum);
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
      
}