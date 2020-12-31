package View;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class test extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test frame = new test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public test() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(133, 86, 234, 33);
		contentPane.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(478, 86, 272, 33);
		contentPane.add(comboBox_1);
		
		JLabel label = new JLabel("공항 주변 관광 코스 날씨 정보");
		label.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(40, 34, 291, 33);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("공항 선택");
		label_1.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		label_1.setBounds(45, 89, 92, 20);
		contentPane.add(label_1);
		
		JLabel label_1_1 = new JLabel("코스선택");
		label_1_1.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		label_1_1.setBounds(398, 89, 92, 20);
		contentPane.add(label_1_1);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(60, 161, 867, 468);
		contentPane.add(textArea);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(654, 34, 50, 15);
		contentPane.add(lblNewLabel);
	}
}
