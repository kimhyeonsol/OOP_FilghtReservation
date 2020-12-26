import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SelectSeatUIFrame extends JFrame implements ActionListener{
	Container c = getContentPane();
	
   JPanel p[] = new JPanel[1];
   
   JButton ASeatButton[] = new JButton[10];
   JButton BSeatButton[] = new JButton[10];
   JButton CSeatButton[] = new JButton[10];
   JButton DSeatButton[] = new JButton[10];
   JButton resetButton = new JButton("좌석 선택 초기화");
   
   
   JLabel rowLabel[] = new JLabel[4];
   JLabel columnLabel[] = new JLabel[10];
   
   String rowStr[] = {"A", "B", "C", "D"};
   String columnStr[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
   int _resNum,cnt=0;
   
   JTextArea ta=new JTextArea();
   
   SelectSeatUIFrame(int resNum){
      _resNum=resNum;
      setTitle("좌석 선택 페이지");
      setLayout(null);
      
      for(int i=0; i<rowLabel.length; i++) {
         rowLabel[i] = new JLabel(rowStr[i]);
         rowLabel[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 25));
         if(i==2 || i==3) {
            rowLabel[i].setBounds(55, 110+(i*75), 40, 40);
         }
         else {
            rowLabel[i].setBounds(55, 65+(i*75), 40, 40);
         }
         p[0].add(rowLabel[i]);
      }
      
      for(int i=0; i<columnLabel.length; i++) {
         columnLabel[i] = new JLabel(columnStr[i]);
         columnLabel[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 25));
         if(i==9) {
            columnLabel[i].setBounds(120+(i*74), 15, 40, 40);
         }
         else {
            columnLabel[i].setBounds(120+(i*75), 15, 40, 40);
         }
         p[0].add(columnLabel[i]);
      }
      resetButton.setFocusPainted(false);
      resetButton.setBackground(new Color(128, 128, 128));
      resetButton.setForeground(Color.white);
      resetButton.setFont(new Font("한컴산뜻돋움", Font.BOLD, 15));
      resetButton.setBounds(685,410,150,30);
      resetButton.addActionListener(this);
      p[0].add(resetButton);
      
      for(int i=0; i<ASeatButton.length; i++) {//좌석달기
         
         ASeatButton[i] = new JButton("A"+(i+1));
         ASeatButton[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 13));
         ASeatButton[i].setForeground(Color.white);
         ASeatButton[i].setBounds(100+(i*75), 60, 60, 60);
         ASeatButton[i].setFocusPainted(false);
         ASeatButton[i].addActionListener(this);
         p[0].add(ASeatButton[i]);
         
         BSeatButton[i] = new JButton("B"+(i+1));
         BSeatButton[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 13));
         BSeatButton[i].setForeground(Color.white);
         BSeatButton[i].setBounds(100+(i*75), 140, 60, 60);
         BSeatButton[i].setFocusPainted(false);
         BSeatButton[i].addActionListener(this);
         p[0].add(BSeatButton[i]);
         
         CSeatButton[i] = new JButton("C"+(i+1));
         CSeatButton[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 13));
         CSeatButton[i].setForeground(Color.white);
         CSeatButton[i].setBounds(100+(i*75), 250, 60, 60);
         CSeatButton[i].setFocusPainted(false);
         CSeatButton[i].addActionListener(this);
         p[0].add(CSeatButton[i]);
         
         DSeatButton[i] = new JButton("D"+(i+1));
         DSeatButton[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 13));
         DSeatButton[i].setForeground(Color.white);
         DSeatButton[i].setBounds(100+(i*75), 330, 60, 60);
         DSeatButton[i].setFocusPainted(false);
         DSeatButton[i].addActionListener(this);
         p[0].add(DSeatButton[i]);
         
         if(i == 0) {
            ASeatButton[i].setBackground(new Color(112, 48, 160));
            BSeatButton[i].setBackground(new Color(112, 48, 160));
            CSeatButton[i].setBackground(new Color(112, 48, 160));
            DSeatButton[i].setBackground(new Color(112, 48, 160));
         }
         else {
            ASeatButton[i].setBackground(new Color(46, 117, 182));
            BSeatButton[i].setBackground(new Color(46, 117, 182));
            CSeatButton[i].setBackground(new Color(46, 117, 182));
            DSeatButton[i].setBackground(new Color(46, 117, 182));
         }
      }
      
      setBounds(100, 100, 900, 600);
      setVisible(true);
   }
  
   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      for(int i=0; i<ASeatButton.length; i++) {
         if(e.getSource() == ASeatButton[i]) {
            cnt++;
            if(cnt>_resNum)
               return;
            ASeatButton[i].setBackground(new Color(255, 192, 0));
            ASeatButton[i].setFocusPainted(false);
         }
         else if(e.getSource() == BSeatButton[i]) {
            cnt++;
            if(cnt>_resNum)
               return;
            BSeatButton[i].setBackground(new Color(255, 192, 0));
            BSeatButton[i].setFocusPainted(false);
         }
         else if(e.getSource() == CSeatButton[i]) {
            cnt++;
            if(cnt>_resNum)
               return;
            CSeatButton[i].setBackground(new Color(255, 192, 0));
            CSeatButton[i].setFocusPainted(false);
         }
         else if(e.getSource() == DSeatButton[i]) {
            cnt++;
            if(cnt>_resNum)
               return;
            DSeatButton[i].setBackground(new Color(255, 192, 0));
            DSeatButton[i].setFocusPainted(false);
         }
         else if(e.getSource() == resetButton) {
             if(i == 0) {
                ASeatButton[i].setBackground(new Color(112, 48, 160));
                BSeatButton[i].setBackground(new Color(112, 48, 160));
                CSeatButton[i].setBackground(new Color(112, 48, 160));
                DSeatButton[i].setBackground(new Color(112, 48, 160));
             }
             else {
                ASeatButton[i].setBackground(new Color(46, 117, 182));
                BSeatButton[i].setBackground(new Color(46, 117, 182));
                CSeatButton[i].setBackground(new Color(46, 117, 182));
                DSeatButton[i].setBackground(new Color(46, 117, 182));
             }
          }

      }
   }
}