import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SelectSeatUIFrame extends JFrame implements ActionListener{
   JPanel p[] = new JPanel[2];
   
   JButton ASeatButton[] = new JButton[10];
   JButton BSeatButton[] = new JButton[10];
   JButton CSeatButton[] = new JButton[10];
   JButton DSeatButton[] = new JButton[10];
   
   JLabel rowLabel[] = new JLabel[4];
   JLabel columnLabel[] = new JLabel[10];
   
   String rowStr[] = {"A", "B", "C", "D"};
   String columnStr[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
   
   
   SelectSeatUIFrame(){
      setTitle("좌석 선택 페이지");
      setLayout(new BorderLayout());
      
      for(int i=0; i<p.length; i++) {
         p[i] = new JPanel();
         
      }
      p[0].setBackground(Color.WHITE);
      for(int i=0; i<rowLabel.length; i++) {
         rowLabel[i] = new JLabel(rowStr[i]);
         rowLabel[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 25));
         if(i==2 || i==3) {
            rowLabel[i].setBounds(55, 110+(i*75), 40, 40);
         }
         else {
            rowLabel[i].setBounds(55, 65+(i*75), 40, 40);
         }
         add(rowLabel[i]);
      }
      
      for(int i=0; i<columnLabel.length; i++) {
         columnLabel[i] = new JLabel(columnStr[i]);
         columnLabel[i].setFont(new Font("한컴산뜻돋움", Font.BOLD, 25));
         columnLabel[i].setBounds(110+(i*75), 15, 40, 40);
         add(columnLabel[i]);
      }
      
      for(int i=0; i<ASeatButton.length; i++) {//
    	 
      }
      
      add(p[0], BorderLayout.CENTER);
      add(p[1], BorderLayout.SOUTH);
      
      setBounds(100, 100, 900, 600);
      setVisible(true);
   }
   
   public static void main(String[] args) {
         new SelectSeatUIFrame();
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
   }
}