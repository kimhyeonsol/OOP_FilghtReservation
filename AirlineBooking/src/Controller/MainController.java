package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.AirLineDAO;
import Model.AirLineDTO;
import Model.ReservationDAO;
import Model.User;
import Model.UserDAO;
import View.LoginUIFrame;
import View.ManagerUIFrame;
import View.UserUIFrame;

public class MainController {
	MainController MCT;
	UserUIController UC;
	ManagerUIController MC;
	LoginUIController LC;

	AirLineDAO aDAO = new AirLineDAO();
	ReservationDAO rDAO = new ReservationDAO();
	UserDAO uDAO = new UserDAO();
	public MainController() {
		MCT = this;
	}

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
					if (obj == v.managerMenuPanel.backButton) {
						System.out.println("제발!!");
						LoginUIFrame lu = new LoginUIFrame(); // 로그인 메뉴 화면 띄움
						MCT.setLoginC(lu);
						v.managerMenuExit(); // 관리자 메뉴 프레임 닫음
					}
					if (obj == v.managerMenuPanel.memButton) {
						v.card.show(v.c, "manager"); // 회원 관리 패널로 넘어감
					}
					if (obj == v.managerMenuPanel.resButton) {
						v.card.show(v.c, "reservation"); // 예약 관리 패널로 넘어감
					}
					if (obj == v.managerMenuPanel.fliButton) {
						v.card.show(v.c, "flight"); // 항공 관리 패널로 넘어감
					}
					if (obj == v.managerPanel.backButton) {
						v.card.show(v.c, "managerMenu"); // 관리자메뉴로 넘어감
					}

					if (obj == v.flightPanel.flightSearchButton_c) {
						//// textarea에 전체 출력
						ArrayList<AirLineDTO> list = new ArrayList<AirLineDTO>();
						try {
							list = aDAO.getAllALInfo();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("항공코드\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t\t일반석운임\t비즈니스석운임\n");
							for (AirLineDTO p : list) {
								sb.append(p.getID() + "\t");
								sb.append(p.getAirLineNm() + "\t");
								sb.append(p.getArrAirportNm() + "\t");
								sb.append(p.getDepAirportNm() + "\t");
								sb.append(p.getArrPlandTime() + "\t");
								sb.append(p.getDepPlandTime() + "\t\t");
								sb.append(p.getEconomyCharge() + "\t");
								sb.append(p.getPrestigeCharge() + "\t\n");
							}
						}
						v.setTextArea(v.flightPanel.textArea_c, sb);

					}

					if (obj == v.flightPanel.flightSearchButton_u) {
						//// textarea에 전체 출력
						ArrayList<AirLineDTO> list = new ArrayList<AirLineDTO>();
						try {
							list = aDAO.getAllALInfo();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("항공코드\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t\t일반석운임\t비즈니스석운임\n");
							for (AirLineDTO p : list) {
								sb.append(p.getID() + "\t");
								sb.append(p.getAirLineNm() + "\t");
								sb.append(p.getArrAirportNm() + "\t");
								sb.append(p.getDepAirportNm() + "\t");
								sb.append(p.getArrPlandTime() + "\t");
								sb.append(p.getDepPlandTime() + "\t\t");
								sb.append(p.getEconomyCharge() + "\t");
								sb.append(p.getPrestigeCharge() + "\t\n");
							}
						}
						v.setTextArea(v.flightPanel.textArea_u, sb);
					}

					if (obj == v.flightPanel.flightSearchButton_d) {
						//// textarea에 전체 출력
						ArrayList<AirLineDTO> list = new ArrayList<AirLineDTO>();
						try {
							list = aDAO.getAllALInfo();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("항공코드\t항공사\t출발공항\t도착공항\t출발시간\t도착시간\t\t일반석운임\t비즈니스석운임\n");
							for (AirLineDTO p : list) {
								sb.append(p.getID() + "\t");
								sb.append(p.getAirLineNm() + "\t");
								sb.append(p.getArrAirportNm() + "\t");
								sb.append(p.getDepAirportNm() + "\t");
								sb.append(p.getArrPlandTime() + "\t");
								sb.append(p.getDepPlandTime() + "\t\t");
								sb.append(p.getEconomyCharge() + "\t");
								sb.append(p.getPrestigeCharge() + "\t\n");
							}
						}
						v.setTextArea(v.flightPanel.textArea_d, sb);
					}

					if (obj == v.flightPanel.flightCreateButton) {

						AirLineDTO info = new AirLineDTO();
						info.setAirLineNm(v.flightPanel.fliCreatetextField[0].getText());
						info.setArrPlandTime(v.flightPanel.fliCreatetextField[1].getText());
						info.setDepPlandTime(v.flightPanel.fliCreatetextField[2].getText());
						info.setEconomyCharge(Integer.parseInt(v.flightPanel.fliCreatetextField[3].getText()));
						info.setPrestigeCharge(Integer.parseInt(v.flightPanel.fliCreatetextField[4].getText()));
						info.setArrAirportNm(v.flightPanel.fliCreatetextField[5].getText());
						info.setDepAirportNm(v.flightPanel.fliCreatetextField[6].getText());
						aDAO.addALInfo(info);
						for (int i = 0; i < 7; i++)
							v.flightPanel.fliCreatetextField[i].setText("");
					}

					if (obj == v.flightPanel.flightUpdateButton) {

						AirLineDTO info = new AirLineDTO();
						info.setID(Integer.parseInt(v.flightPanel.fliUpdatetextField[0].getText()));
						info.setArrPlandTime(v.flightPanel.fliUpdatetextField[1].getText());
						info.setDepPlandTime(v.flightPanel.fliUpdatetextField[2].getText());
						info.setEconomyCharge(Integer.parseInt(v.flightPanel.fliUpdatetextField[3].getText()));
						info.setPrestigeCharge(Integer.parseInt(v.flightPanel.fliUpdatetextField[4].getText()));

						aDAO.updateALInfo(info);
						for (int i = 0; i < 5; i++)
							v.flightPanel.fliUpdatetextField[i].setText("");
					}

					if (obj == v.flightPanel.flightDeleteButton) {
						aDAO.delALInfo(Integer.parseInt(v.flightPanel.fliDeletetextField.getText()));
						v.flightPanel.fliDeletetextField.setText("");
					}
					
					if(obj == v.managerPanel.memSearchButton) {
						//모든 회원정보 조회
						ArrayList<User> list = new ArrayList<User>();
						try {
							list = uDAO.getAll();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						StringBuffer sb = new StringBuffer();
						if (list != null) {
							sb.append("아이디\t이름\t비밀번호\t이메일\t\t생일\t전화번호\n");
							for (User p : list) {
								sb.append(p.getID() + "\t");
								sb.append(p.getName()+ "\t");
								sb.append(p.getPw() + "\t");
								sb.append(p.getEmail() + "\t");
								sb.append(p.getBirth() + "\t");
								sb.append(p.getPhone() + "\t\n");

							}
						}
						v.setTextArea(v.managerPanel.textArea_m, sb);
						
					}
					
					if(obj == v.managerPanel.memDeleteButton) {
						//멤버 삭제
						//uDAO.deleteUser(Integer.parseInt(v.managerPanel.fliDeletetextField.getText()));
						//v.flightPanel.fliDeletetextField.setText("");
						//프론트 수정 후 다시 하기!
					}
					
					if(obj == v.reservationPanel.backButton) {
						v.card.show(v.c, "managerMenu");
					}
					
					
					//수정 필요.
//					if(obj == v.reservationPanel.reservationSearchButton) {
//						ArrayList<Reservation> list = new ArrayList<Reservation>();
//						try {
//							list = rDAO.getAll();
//						} catch (SQLException e1) {
//							e1.printStackTrace();
//						}
//
//						StringBuffer sb = new StringBuffer();
//						if (list != null) {
//							sb.append("예약코드\t회원ID\t항공ID\t좌석번호\n");
//							for (Reservation p : list) {
//								sb.append(p.getID() + "\t");
//								sb.append(p.getUser().getID() + "\t");
//								sb.append(p.getInfo().getID() + "\t");
//								sb.append(p.getSeatNum() + "\t\n");
//							}
//						}
//						v.setTextArea(v.reservationPanel.textArea_r, sb);
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
					if (obj == v.startUIPanel.managerButton) {
						System.out.println("매니저");
						ManagerUIFrame mu = new ManagerUIFrame();
						MCT.setManagerC(mu);
						v.loginUIFrameExit();
					}
					if (obj == v.startUIPanel.userButton) {
						System.out.println("사용자");
						v.card.next(v.c);
					}

				}

			});

		}
	}

	public void setLoginC(LoginUIFrame ui) {
		LC = new LoginUIController(ui);
	}

	public void setUserC(UserUIFrame ui) {
		UC = new UserUIController(ui);
	}

	public void setManagerC(ManagerUIFrame ui) {
		MC = new ManagerUIController(ui);
	}

	public static void main(String[] args) {

		MainController Controller = new MainController();
		LoginUIFrame ui = new LoginUIFrame();
		Controller.setLoginC(ui);

	}

}
