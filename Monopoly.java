package final_project;

import java.util.Scanner;

public class Monopoly {

	public static void main (String[] args) {
		Windows windows = new Windows();
		windows.setVisible(true);
		gameProcedure(windows);

	}
	public static void gameProcedure(Windows windows) {
		while(windows.end == false) {
			while(true) {
				sleep(100);
				if(!windows.gameinfo_jl.isVisible()) {
					windows.rule_jb.setVisible(true);
					break;
				}
			}
			for(windows.currentRole_ID = 0; windows.currentRole_ID <= 3; windows.currentRole_ID++) {
				if(windows.isout[windows.currentRole_ID]) {
					continue;
				}
				windows.isroundover = false;
				windows.continue_jb.setVisible(true);
				while(true) {
					sleep(100);
					if(windows.iscontinue) {
						windows.iscontinue = false;
						break;
					}
				}
				windows.alert_jl.setText("���쪱�a"+ (windows.currentRole_ID+1) + "�F!");				
				if(windows.rest[windows.currentRole_ID] != 0) { //�ˬd���a�O�_�ݭn��
					windows.rest[windows.currentRole_ID] -= 1;
					windows.continue_jb.setVisible(true);
					while(true) {
						sleep(100);
						if(windows.iscontinue) {
							windows.iscontinue = false;
							break;
						}
					}
					if(windows.rest[windows.currentRole_ID] != 0) {
						windows.alert_jl.setText("�o�^�X�n�𮧮@~�٭n��"+ windows.rest[windows.currentRole_ID] + "�^�X" );
					}else {
						windows.alert_jl.setText("�o�^�X�n�𮧮@~�U�^�X�N���A��" );
					}
					windows.isroundover = true;
				}else {
					//�Y��l
					windows.continue_jb.setVisible(true);
					while(true) {
						sleep(100);
						if(windows.iscontinue) {
							windows.iscontinue = false;
							break;
						}
					}
					if(windows.isprop) {
						windows.alert_jl.setText("�п�ܬO�_�I���D��");
						windows.exchange_jb.setVisible(true);
						windows.cancel_jb.setVisible(true);
						while(true) {
							sleep(100);
							if(windows.iscancel || windows.isexchange) {
								if(windows.isexchange) {
									windows.continue_jb.setVisible(true);
									while(true) {
										sleep(100);
										if(windows.iscontinue) {
											windows.iscontinue = false;
											break;
										}
									}
								}
								windows.iscancel = false;
								windows.isexchange = false;
								break;
							}
						}
						windows.dice_jb.setVisible(true);
						windows.alert_jl.setText("���I�����l���s�i���Y��l");
						
					}
					else if(windows.isclassic)  {
						windows.dice_jb.setVisible(true);
						windows.alert_jl.setText("���I�����l���s�i���Y��l");
					}
				}
				while(true) {
					sleep(100);
					if(windows.isroundover) {
						break;
					}
				}
				//�C�������P�w
				windows.end = windows.check_end();
				if(windows.end == true) {
					break;
				}
			}				
		}
	}
	
	public static void sleep(int time) {
		try {
		    Thread.sleep(time);
		} catch(Exception e) {
		}
	}
}

