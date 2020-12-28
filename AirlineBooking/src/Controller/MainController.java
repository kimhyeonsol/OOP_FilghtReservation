package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import View.LoginUIFrame;
import View.UserUIFrame;
import View.ManagerUIFrame;

public class MainController {
	UserUIController UC;
	ManagerUIController MC;
	LoginUIController LC;
	
	class UserUIController {
		private final UserUIFrame v;

		public UserUIController(UserUIFrame ui) {
			
			this.v = ui;
			
			v.addButtonActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Object obj = e.getSource();
//					if(obj == v.loginUIpanel.backButton) {
//						System.out.println("굳!");
//					}
					
					
				}
				
			});
			
		}
	}
	class ManagerUIController {
		private final ManagerUIFrame v;
		
		public ManagerUIController(ManagerUIFrame ui) {
			
			this.v = ui;
			
			v.addButtonActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Object obj = e.getSource();
//					if(obj == v.loginUIpanel.backButton) {
//						System.out.println("굳!");
//					}
					
					
				}
				
			});
			
		}
	}
	class LoginUIController {
		private final LoginUIFrame v;
		
		public LoginUIController(LoginUIFrame ui) {
			
			this.v = ui;
			
			v.addButtonActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Object obj = e.getSource();
					if(obj == v.startUIPanel.managerButton) {
						System.out.println("매니저");
			               new ManagerUIFrame();
			               v.loginUIFrameExit();
					}
					if(obj == v.startUIPanel.userButton) {
						System.out.println("사용자");
						v.card.next(v.c);
					}
					
				
				}
				
			});
			
		}
	}
	
	public void setLoginC(LoginUIFrame ui) {
		this.LC = new LoginUIController(ui);
	}
	public void setUserC(UserUIFrame ui) {
		this.UC = new UserUIController(ui);
	}
	public void setManagerC(ManagerUIFrame ui) {
		this.MC = new ManagerUIController(ui);
	}
	
	public static void main(String[] args) {
		
		MainController Controller = new MainController();
		LoginUIFrame ui = new LoginUIFrame();
		Controller.setLoginC(ui);
		
	}
	
}
