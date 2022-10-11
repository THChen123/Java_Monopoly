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
				windows.alert_jl.setText("輪到玩家"+ (windows.currentRole_ID+1) + "了!");				
				if(windows.rest[windows.currentRole_ID] != 0) { //檢查玩家是否需要休息
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
						windows.alert_jl.setText("這回合要休息哦~還要休息"+ windows.rest[windows.currentRole_ID] + "回合" );
					}else {
						windows.alert_jl.setText("這回合要休息哦~下回合就換你嘍" );
					}
					windows.isroundover = true;
				}else {
					//擲骰子
					windows.continue_jb.setVisible(true);
					while(true) {
						sleep(100);
						if(windows.iscontinue) {
							windows.iscontinue = false;
							break;
						}
					}
					if(windows.isprop) {
						windows.alert_jl.setText("請選擇是否兌換道具");
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
						windows.alert_jl.setText("請點擊骰骰子按鈕進行擲骰子");
						
					}
					else if(windows.isclassic)  {
						windows.dice_jb.setVisible(true);
						windows.alert_jl.setText("請點擊骰骰子按鈕進行擲骰子");
					}
				}
				while(true) {
					sleep(100);
					if(windows.isroundover) {
						break;
					}
				}
				//遊戲結束判定
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

