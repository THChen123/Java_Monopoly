package final_project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.*;

import java.util.Timer;
import java.util.*;
public class Windows extends JFrame implements ActionListener {

	//國家名字和機會(?)命運(!)標示
	public String country_name[] = {"Start", "KOR", " ! ", "JPN", "MEX", " ? ", "NZL", "Toilet", "USA", "RUS", "Back", "SGP", " ! ", "ESP", "THA", " ? ", "AUS", "Park", "TUR", "TWN"};
	
	//4顆棋子在角落的座標
	int start_x[] = new int[]{48,78,48,78};
	int start_y[] = new int[]{54,54,90,90};
	int prison_x[] = new int[]{503,533,503,533};
	int prison_y[] = new int[]{54,54,90,90};
	int backtostart_x[] = new int[]{503,533,503,533};
	int backtostart_y[] = new int[]{341,341,377,377};
	int park_x[] = new int[]{48,78,48,78};
	int park_y[] = new int[]{341,341,377,377};
	
	/* 玩家資訊
	 * player_information[i][j]
	 * i代表玩家
	 * [i][0] 已購買的土地
	 * [i][1] 蓋一棟房子的國家
	 * [i][2] 蓋兩棟房子的國家
	 * [i][3] 蓋三棟房子的國家
	 */
	String player_information[][] = new String[4][4];
	
	//houserank[i][j][k] i放入currentRole_ID，代表玩家 j代表房子階級 k放入chessposition[currentRole_ID]，代表國家
	String houserank[][][] = new String[4][4][20];
	int house[] = new int[20]; //house[i][j] ，代表國家的擁有權是誰
	int countryinformation[][] = new int[9][20]; //國家資訊
	int chessposition[] = new int[4]; //玩家們的所在國家
	int chessposition_x[] = new int[4]; //玩家們的所在的x軸位置
	int chessposition_y[] = new int[4]; //玩家們的所在的x軸位置
	int lap[] = new int[4];//玩家們走的圈數
	int currentRole_ID = 0; //玩家順序(0~3):0代表玩家1，1代表玩家2，2代表玩家3，3代表玩家4
	
	int money[] = new int[4]; //玩家們的金錢
	int rest[] = new int[4]; //暫停陣列
	
	boolean end = false;
	boolean isout[] = new boolean[4]; //紀錄是否淘汰
	boolean isclassic = false;
	boolean isprop = false;
	boolean isroundover = false;
	boolean iscontinue = false;
	boolean isrolldice = false;
	boolean isdicethreetimes = false;
	boolean isaccept = false;
	boolean iscancel = false;
	boolean isexchange = false;
	int dice_num = 0; //骰子點數
	int page = 1; // 切換玩家資訊頁面
	ImageIcon[] role = new ImageIcon[4];
	ImageIcon[][] houseicon = new ImageIcon[2][4]; //house[i][j] i = 0代表橫的 i = 1代表直的  j代表1-3棟
	String dice_path = String.format("images/dice_%d.png",dice_num); //骰子路徑
	ImageIcon diceImage = new ImageIcon(dice_path);//骰子Icon  
	String country_cost_path; //國家花費卡路徑
	String country_fee_path; //國家過路費卡路徑
	ImageIcon country_costImage; //國家花費卡Icon
	ImageIcon country_feeImage; //國家過路費卡Icon
	
	//層次由低到高:Default, Palette, Modal, PopUp, Drag
    JLayeredPane layeredPane; //建立一個JLayeredPane用於分層的。
    JPanel jp; 
    JLabel map_jl, dice_jl, gameinfo_jl, rule_jl, countrycost_jl, countryfee_jl,alert_jl, rank_jl, rest_jl, ownership_jl, lap_jl, chess_jl[], house_jl[][];
    JButton dice_jb, start_jb, classic_jb, prop_jb, rule_jb, accept_jb, cancel_jb, exchange_jb, continue_jb, switch_jb;
    Timer t = new Timer(); 	//計時器;
    
	public Windows() {
		initImages();
		initFrame();
		initPosition();
		init_info();
		country_information();
		setFocusable(true);
	}
	
	public void initImages(){
		role[0] = new ImageIcon("images/suitcase_0.png");
		role[1] = new ImageIcon("images/suitcase_1.png");
		role[2] = new ImageIcon("images/suitcase_2.png");
		role[3] = new ImageIcon("images/suitcase_3.png");
		houseicon[0][0] = new ImageIcon("images/flag.png");
		houseicon[0][1] = new ImageIcon("images/house_h0.png");
		houseicon[0][2] = new ImageIcon("images/house_h1.png");
		houseicon[0][3] = new ImageIcon("images/house_h2.png");
		houseicon[1][0] = new ImageIcon("images/flag.png");
		houseicon[1][1] = new ImageIcon("images/house_v0.png");
		houseicon[1][2] = new ImageIcon("images/house_v1.png");
		houseicon[1][3] = new ImageIcon("images/house_v2.png");
	}
	
	public void initFrame() {
        layeredPane = new JLayeredPane(); //new一個JLayeredPane用於分層
        String map_path = "images/backgroundmap.png"; //地圖路徑
        ImageIcon mapImage = new ImageIcon(map_path);//大富翁地圖(路徑)         
        
        //建立地圖背景的元件
        jp = new JPanel(); 
        jp.setBounds(0, 0, mapImage.getIconWidth(), mapImage.getIconHeight()); //設定jp的位置和大小(x, y, width, height)
        map_jl = new JLabel(mapImage); //new一個Label用來放大富翁地圖
        jp.add(map_jl);
        layeredPane.add(jp,JLayeredPane.DEFAULT_LAYER); //將jp(大富翁地圖)放到最底層。

		initLabel();
		initButton();
		initHouse();
        
        //JFrame基本設定
		this.setTitle("Monopoly");
		this.setLayeredPane(layeredPane);
        this.setSize(1024,768);  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(300,50);
        this.setVisible(true);
	}
	
	public void initLabel() {
	    //建立遊戲說明畫面
        String gamestart_path = "images/gamestart.png"; 
        ImageIcon gamestartImage = new ImageIcon(gamestart_path);
        gameinfo_jl = new JLabel(gamestartImage);
        gameinfo_jl.setOpaque(true);
        gameinfo_jl.setBounds(0, 0, gamestartImage.getIconWidth(), gamestartImage.getIconHeight());
        layeredPane.add(gameinfo_jl,JLayeredPane.PALETTE_LAYER);
        gameinfo_jl.setVisible(true); 
        
        //建立遊戲規則畫面
        String gamerule_path = "images/rule.png"; 
        ImageIcon gameruleImage = new ImageIcon(gamerule_path);
        rule_jl = new JLabel(gameruleImage);
        rule_jl.setOpaque(true);
        rule_jl.setBounds(0, 0, gamestartImage.getIconWidth(), gamestartImage.getIconHeight());
        layeredPane.add(rule_jl,JLayeredPane.PALETTE_LAYER);
        rule_jl.setVisible(false); 
        
        //建立棋子圖片
        chess_jl = new JLabel[4];
        for(int i = 0; i < chess_jl.length ; i++) {
        	chess_jl[i]= new JLabel(role[i]);
	        chess_jl[i].setOpaque(false);
	        layeredPane.add(chess_jl[i],JLayeredPane.PALETTE_LAYER);
	        chess_jl[i].setVisible(true);
        }
        
        //建立骰骰子點數元件
        dice_jl = new JLabel(diceImage);   
        dice_jl.setBounds(254, 190, diceImage.getIconWidth(), diceImage.getIconHeight());
        dice_jl.setOpaque(false);
        layeredPane.add(dice_jl,JLayeredPane.PALETTE_LAYER);
        dice_jl.setVisible(true);
        
        //國家花費卡元件
        countrycost_jl = new JLabel(country_costImage);
        countrycost_jl.setBounds(605, 480, 200, 250);
        countrycost_jl.setOpaque(false);
        layeredPane.add(countrycost_jl,JLayeredPane.PALETTE_LAYER);
        countrycost_jl.setVisible(false);
        
        //國家過路費卡元件
        countryfee_jl = new JLabel(country_feeImage);
        countryfee_jl.setBounds(805, 480, 200, 250);
        countryfee_jl.setOpaque(false);
        layeredPane.add(countryfee_jl,JLayeredPane.PALETTE_LAYER);
        countryfee_jl.setVisible(false);
        
        //建立提示說明文字元件
        alert_jl = new JLabel("", SwingConstants.CENTER);
        alert_jl.setBounds(0, 478, 620, 290);
        alert_jl.setOpaque(false);
        alert_jl.setForeground(Color.WHITE);
        alert_jl.setFont(new Font("標楷體",Font.PLAIN,28));
        layeredPane.add(alert_jl,JLayeredPane.PALETTE_LAYER);
          
        //建立資訊欄元件
        //排名
        rank_jl = new JLabel("", SwingConstants.CENTER);
        rank_jl.setBounds(607, 0, 417, 300);
        rank_jl.setOpaque(false);
        rank_jl.setForeground(Color.BLACK);
        rank_jl.setFont(new Font("標楷體",Font.PLAIN,24));
        layeredPane.add(rank_jl,JLayeredPane.PALETTE_LAYER);
        t.schedule(new print_rank_timerTask(), 0,500);
        //休息回合
        rest_jl = new JLabel("", SwingConstants.CENTER);
        rest_jl.setBounds(607, 200, 417, 300);
        rest_jl.setOpaque(false);
        rest_jl.setForeground(Color.BLACK);
        rest_jl.setFont(new Font("標楷體",Font.PLAIN,24));
        layeredPane.add(rest_jl,JLayeredPane.PALETTE_LAYER);
        t.schedule(new print_rest_timerTask(), 0,500);
        //國家擁有權        
        ownership_jl = new JLabel("", SwingConstants.CENTER);
        ownership_jl.setBounds(607, 0, 417, 478); 
        ownership_jl.setOpaque(false);
        ownership_jl.setForeground(Color.BLACK);
        ownership_jl.setBackground(Color.white);
        ownership_jl.setFont(new Font("標楷體",Font.PLAIN,24));
        layeredPane.add(ownership_jl,JLayeredPane.PALETTE_LAYER); 
        ownership_jl.setVisible(false);
        t.schedule(new print_ownership_timerTask(), 0,500);
        //圈數
        lap_jl = new JLabel("", SwingConstants.CENTER); 
        lap_jl.setBounds(607, 0, 417, 300); 
        lap_jl.setOpaque(false);
        lap_jl.setForeground(Color.BLACK);
        lap_jl.setFont(new Font("標楷體",Font.PLAIN,24));
        layeredPane.add(lap_jl,JLayeredPane.PALETTE_LAYER);
        lap_jl.setVisible(false);
        t.schedule(new print_lap_timerTask(), 0, 2000);
	}
	
	public void initButton() {
	    //建立開始遊戲按鈕元件
        String startbt_path = "images/start_button.png"; 
    	ImageIcon startbtImage = new ImageIcon(startbt_path);
        start_jb = new JButton(startbtImage);
        start_jb.setBounds(462, 384, 100, 50);
        start_jb.addActionListener(this); 
        layeredPane.add(start_jb,JLayeredPane.MODAL_LAYER); //將start_jb放到其他元件高層的地方
        start_jb.setVisible(true);
        
        //建立開始遊戲按鈕元件
        String rulebt_path = "images/rule_button.png"; 
    	ImageIcon rulebtImage = new ImageIcon(rulebt_path);
        rule_jb = new JButton(rulebtImage);
        rule_jb.setBounds(630, 20, 100, 50);
        rule_jb.addActionListener(this);
        layeredPane.add(rule_jb,JLayeredPane.MODAL_LAYER); //將rule_jb放到其他元件高層的地方
        rule_jb.setVisible(false);
        
        //建立經典模式按鈕元件
        String classicbt_path = "images/classic_button.png"; 
    	ImageIcon classicbtImage = new ImageIcon(classicbt_path);
    	classic_jb = new JButton(classicbtImage); 
    	classic_jb.setBounds(332, 300, 100, 50); 
    	classic_jb.addActionListener(this); 
        layeredPane.add(classic_jb,JLayeredPane.MODAL_LAYER); //將classic_jb放到其他元件高層的地方
        classic_jb.setVisible(false);
        
        //建立道具模式按鈕元件
        String propbt_path = "images/prop_button.png"; 
    	ImageIcon propbtImage = new ImageIcon(propbt_path);
    	prop_jb = new JButton(propbtImage);
    	prop_jb.setBounds(592, 300, 100, 50);
    	prop_jb.addActionListener(this);
        layeredPane.add(prop_jb,JLayeredPane.MODAL_LAYER); //將prop_jb放到其他元件高層的地方
        prop_jb.setVisible(false);
        
        //建立骰骰子按鈕元件
        String dicebt_path = "images/dice_button.png"; 
    	ImageIcon dicebtImage = new ImageIcon(dicebt_path);
        dice_jb = new JButton(dicebtImage);
        dice_jb.setBounds(507, 650, 100, 50);
        dice_jb.addActionListener(this);
        layeredPane.add(dice_jb,JLayeredPane.PALETTE_LAYER);
        dice_jb.setVisible(false);
        
        //建立確認取消按鈕元件
        String acceptbt_path = "images/buy_button.png"; 
    	ImageIcon acceptbtImage = new ImageIcon(acceptbt_path);
        accept_jb = new JButton(acceptbtImage);
        accept_jb.setBounds(407, 650, 100, 50);
        accept_jb.addActionListener(this);
        layeredPane.add(accept_jb,JLayeredPane.PALETTE_LAYER);
        accept_jb.setVisible(false);
        
        String cancelbt_path = "images/cancel_button.png"; 
    	ImageIcon cancelbtImage = new ImageIcon(cancelbt_path);
        cancel_jb = new JButton(cancelbtImage);
        cancel_jb.setBounds(507, 650, 100, 50);
        cancel_jb.addActionListener(this);
        layeredPane.add(cancel_jb,JLayeredPane.PALETTE_LAYER);
        cancel_jb.setVisible(false);
        
        //建立兌換道具按鈕元件
        String exchangebt_path = "images/exchange_button.png"; 
    	ImageIcon exchangebtImage = new ImageIcon(exchangebt_path);
    	exchange_jb = new JButton(exchangebtImage);
    	exchange_jb.setBounds(407, 650, 100, 50);
    	exchange_jb.addActionListener(this);
        layeredPane.add(exchange_jb,JLayeredPane.PALETTE_LAYER);
        exchange_jb.setVisible(false);
        
        //建立繼續按鈕元件
        String continuebt_path = "images/continue_button.png"; 
    	ImageIcon continuebtImage = new ImageIcon(continuebt_path);
        continue_jb = new JButton(continuebtImage);
        continue_jb.setBounds(507, 650, 100, 50);
        continue_jb.addActionListener(this);
        layeredPane.add(continue_jb,JLayeredPane.PALETTE_LAYER);
        continue_jb.setVisible(false);
        
        //建立切換按鈕元件
        String switchbt_path = "images/switch_button.png"; 
    	ImageIcon switchbtImage = new ImageIcon(switchbt_path);
        switch_jb = new JButton(switchbtImage);
        switch_jb.setBounds(900, 20, 100, 50);
        switch_jb.addActionListener(this);
        layeredPane.add(switch_jb,JLayeredPane.PALETTE_LAYER);
        switch_jb.setVisible(true);
	}
	
	public void initHouse() {
        //建立房子圖示
        house_jl = new JLabel[20][5]; //house_jl[i][j]  i代表國家(0-19) j代表幾棟房子(0=空 1=買土地 2=一棟房子)
        for(int i = 0; i < house_jl.length; i++) {
        	house_jl[i][0] = new JLabel();
        	layeredPane.add(house_jl[i][0],JLayeredPane.PALETTE_LAYER);
        	house_jl[i][0].setVisible(true);
        }
         
        //橫的
        for(int i = 1; i < house_jl.length ; i++) {
        	for(int j = 1; j < house_jl[0].length ; j++) {
	        	house_jl[i][j]= new JLabel(houseicon[0][j-1]);
	        	house_jl[i][j].setOpaque(false);
		        layeredPane.add(house_jl[i][j],JLayeredPane.PALETTE_LAYER);
		        house_jl[i][j].setVisible(false);
        	}
        }
        //直的
        for(int j = 1; j < house_jl[0].length ; j++) {
        	house_jl[8][j]= new JLabel(houseicon[1][j-1]);
        	house_jl[9][j]= new JLabel(houseicon[1][j-1]);
        	house_jl[18][j]= new JLabel(houseicon[1][j-1]);
        	house_jl[19][j]= new JLabel(houseicon[1][j-1]);
        	house_jl[8][j].setOpaque(false);
        	house_jl[9][j].setOpaque(false);
        	house_jl[18][j].setOpaque(false);
        	house_jl[19][j].setOpaque(false);
	        layeredPane.add(house_jl[8][j],JLayeredPane.PALETTE_LAYER);
	        layeredPane.add(house_jl[9][j],JLayeredPane.PALETTE_LAYER); 
	        layeredPane.add(house_jl[18][j],JLayeredPane.PALETTE_LAYER); 
	        layeredPane.add(house_jl[19][j],JLayeredPane.PALETTE_LAYER); 
	        house_jl[8][j].setVisible(false);
	        house_jl[9][j].setVisible(false);
	        house_jl[18][j].setVisible(false);
	        house_jl[19][j].setVisible(false);
        }
        
        //房子橫的長寬
        int housewidth_h1 = houseicon[0][1].getIconWidth();
        int housewidth_h2 = houseicon[0][2].getIconWidth();
        int housewidth_h3 = houseicon[0][3].getIconWidth();
        int househeight_h1 = houseicon[0][1].getIconHeight();
        int househeight_h2 = houseicon[0][2].getIconHeight();
        int househeight_h3 = houseicon[0][3].getIconHeight();
        //房子直的長寬
        int housewidth_v1 = houseicon[1][1].getIconWidth();
        int housewidth_v2 = houseicon[1][2].getIconWidth();
        int housewidth_v3 = houseicon[1][3].getIconWidth();
        int househeight_v1 = houseicon[1][1].getIconHeight();
        int househeight_v2 = houseicon[1][2].getIconHeight();
        int househeight_v3 = houseicon[1][3].getIconHeight();
        
        house_jl[1][1].setBounds(137, 13, housewidth_h1, househeight_h1);
        house_jl[1][2].setBounds(132, 13, housewidth_h1, househeight_h1);
        house_jl[1][3].setBounds(122, 13, housewidth_h2, househeight_h2);
        house_jl[1][4].setBounds(112, 13, housewidth_h3, househeight_h3);
        house_jl[3][1].setBounds(267, 13, housewidth_h1, househeight_h1);
        house_jl[3][2].setBounds(262, 13, housewidth_h1, househeight_h1);
        house_jl[3][3].setBounds(252, 13, housewidth_h2, househeight_h2);
        house_jl[3][4].setBounds(242, 13, housewidth_h3, househeight_h3);
        house_jl[4][1].setBounds(332, 13, housewidth_h1, househeight_h1);
        house_jl[4][2].setBounds(327, 13, housewidth_h1, househeight_h1);
        house_jl[4][3].setBounds(317, 13, housewidth_h2, househeight_h2);
        house_jl[4][4].setBounds(307, 13, housewidth_h3, househeight_h3);
        house_jl[6][1].setBounds(462, 13, housewidth_h1, househeight_h1);
        house_jl[6][2].setBounds(457, 13, housewidth_h1, househeight_h1);
        house_jl[6][3].setBounds(447, 13, housewidth_h2, househeight_h2);
        house_jl[6][4].setBounds(437, 13, housewidth_h3, househeight_h3);
       
        house_jl[8][1].setBounds(581, 180, housewidth_v1, househeight_v1);
        house_jl[8][2].setBounds(576, 180, housewidth_v1, househeight_v1);
        house_jl[8][3].setBounds(576, 170, housewidth_v2, househeight_v2);
        house_jl[8][4].setBounds(576, 160, housewidth_v3, househeight_v3);
        house_jl[9][1].setBounds(581, 276, housewidth_v1, househeight_v1);
        house_jl[9][2].setBounds(576, 276, housewidth_v1, househeight_v1);
        house_jl[9][3].setBounds(576, 266, housewidth_v2, househeight_v2);
        house_jl[9][4].setBounds(576, 256, housewidth_v3, househeight_v3);
        
        house_jl[11][1].setBounds(462, 443, housewidth_h1, househeight_h1);
        house_jl[11][2].setBounds(457, 443, housewidth_h1, househeight_h1);
        house_jl[11][3].setBounds(447, 443, housewidth_h2, househeight_h2);
        house_jl[11][4].setBounds(437, 443, housewidth_h3, househeight_h3);
        house_jl[13][1].setBounds(332, 443, housewidth_h1, househeight_h1);
        house_jl[13][2].setBounds(327, 443, housewidth_h1, househeight_h1);
        house_jl[13][3].setBounds(317, 443, housewidth_h2, househeight_h2);
        house_jl[13][4].setBounds(307, 443, housewidth_h3, househeight_h3);
        house_jl[14][1].setBounds(267, 443, housewidth_h1, househeight_h1);
        house_jl[14][2].setBounds(262, 443, housewidth_h1, househeight_h1);
        house_jl[14][3].setBounds(252, 443, housewidth_h2, househeight_h2);
        house_jl[14][4].setBounds(242, 443, housewidth_h3, househeight_h3);
        house_jl[16][1].setBounds(137, 443, housewidth_h1, househeight_h1);
        house_jl[16][2].setBounds(132, 443, housewidth_h1, househeight_h1);
        house_jl[16][3].setBounds(122, 443, housewidth_h2, househeight_h2);
        house_jl[16][4].setBounds(112, 443, housewidth_h3, househeight_h3);
        
        house_jl[18][1].setBounds(17, 276, housewidth_v1, househeight_v1);
        house_jl[18][2].setBounds(12, 276, housewidth_v1, househeight_v1);
        house_jl[18][3].setBounds(12, 266, housewidth_v2, househeight_v2);
        house_jl[18][4].setBounds(12, 256, housewidth_v3, househeight_v3);
        house_jl[19][1].setBounds(17, 180, housewidth_v1, househeight_v1);
        house_jl[19][2].setBounds(12, 180, housewidth_v1, househeight_v1);
        house_jl[19][3].setBounds(12, 170, housewidth_v2, househeight_v2);
        house_jl[19][4].setBounds(12, 160, housewidth_v3, househeight_v3);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		//點開始遊戲按鈕
		if(evt.getSource() == start_jb) {
			start_jb.setVisible(false);
			classic_jb.setVisible(true);
			prop_jb.setVisible(true);
		}
		//點經典模式按鈕
		if(evt.getSource() == classic_jb) {
			isclassic = true;
			gameinfo_jl.setVisible(false);
			classic_jb.setVisible(false);
			prop_jb.setVisible(false);
			for (int i = 0; i < 4; i++) {
				money[i] = 5000;
			}	
		}		
		//點道具模式按鈕
		if(evt.getSource() == prop_jb) {
			isprop = true;
			gameinfo_jl.setVisible(false);
			classic_jb.setVisible(false);
			prop_jb.setVisible(false);
			for (int i = 0; i < 4; i++) {
				money[i] = 15000;
			}
		}
		//點遊戲規則按鈕
		if(evt.getSource() == rule_jb) {
			if(!rule_jl.isVisible()) {
		        String rulebt_path = "images/back_button.png"; 
		        ImageIcon rulebtImage = new ImageIcon(rulebt_path);
		        rule_jb.setIcon(rulebtImage);
				rule_jb.setBounds(800,650,100,50);
				rule_jl.setVisible(true);
			}else{
				rule_jl.setVisible(false);
		        String rulebt_path = "images/rule_button.png"; 
		        ImageIcon rulebtImage = new ImageIcon(rulebt_path);
				rule_jb.setIcon(rulebtImage);
				rule_jb.setBounds(630, 20, 100, 50);
			}	
		}
		//點骰子按鈕
		if(evt.getSource() == dice_jb) {
			isrolldice = true;
			dice_jb.setVisible(false);
			dice_path = "images/dice.gif";
			diceImage = new ImageIcon(dice_path);//骰子Icon
			dice_jl.setIcon(diceImage);
			dice_num = (int)(Math.random()*6+1);
			dice_path = String.format("images/dice_%d.png",dice_num);
			diceImage = new ImageIcon(dice_path);//骰子Icon
			if(isdicethreetimes) {
				isdicethreetimes = false;
				dice_num *= 3;
			}
			t.schedule(new dice_timerTask(), 1000);
			chess_move();	
		}
		//點確認按鈕
		if(evt.getSource() == accept_jb) {
			isaccept = true;
			accept_jb.setVisible(false);
			cancel_jb.setVisible(false);
		}
		//點取消按鈕
		if(evt.getSource() == cancel_jb) {
			iscancel = true;
			cancel_jb.setVisible(false);
			accept_jb.setVisible(false);
			if(isprop) {
				exchange_jb.setVisible(false);
			}
		}
		//點交換道具按鈕
		if(evt.getSource() == exchange_jb) {
			if(money[currentRole_ID] >= 3000) {
				money[currentRole_ID] -= 3000;
				prop();
			}else {
				alert_jl.setText("你的錢不夠買呦!");
			}
			isexchange = true;
			cancel_jb.setVisible(false);
			exchange_jb.setVisible(false);	
		}
		//點繼續按鈕
		if(evt.getSource() == continue_jb) {
			iscontinue = true;
			continue_jb.setVisible(false);
		}
		//點切換按鈕
		if(evt.getSource() == switch_jb) {
			switch(page){
				case 1:
					rank_jl.setVisible(false);
					rest_jl.setVisible(false);
					ownership_jl.setVisible(true);
					lap_jl.setVisible(false);
					page = 2;
					break;
				case 2:
					rank_jl.setVisible(false);
					rest_jl.setVisible(false);
					ownership_jl.setVisible(false);
					lap_jl.setVisible(true);
					page = 3;
					break;
				case 3:
					rank_jl.setVisible(true);
					rest_jl.setVisible(true);
					ownership_jl.setVisible(false);
					lap_jl.setVisible(false);
					page = 1;
					break;
			}	
		}
	}

	class dice_timerTask extends TimerTask{
		@Override
		public void run() {
			dice_jl.setIcon(diceImage);		
		}
	}
	
	class print_rank_timerTask extends TimerTask{
		@Override
		public void run() {
			rank();	
		}
	}
	class print_rest_timerTask extends TimerTask{
		@Override
		public void run() {
			rest();	
		}
	}
	class print_ownership_timerTask extends TimerTask{
		@Override
		public void run() {
			ownership();	
		}
	}
	class print_lap_timerTask extends TimerTask{
		@Override
		public void run() {
			lap();	
		}
	}
	
	public void initPosition() {
		for (int i = 0; i < 4; i++) {
			chessposition[i] = 0;
		}
		chessposition_x[0] = 48;
		chessposition_y[0] = 54;
		chessposition_x[1] = 78;
		chessposition_y[1] = 54;
		chessposition_x[2] = 48;
		chessposition_y[2] = 90;
		chessposition_x[3] = 78;
		chessposition_y[3] = 90;

		for(int i = 0; i < chess_jl.length; i++) {
			chess_jl[i].setBounds(chessposition_x[i], chessposition_y[i], role[i].getIconWidth(), role[i].getIconHeight());
		}	
	}
	
	public void init_info() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (j == 0) {
					player_information[i][j] = "無";
				}
				if (j == 1) {
					player_information[i][j] = "無";
				}
				if (j == 2) {
					player_information[i][j] = "無";
				}
				if (j == 3) {
					player_information[i][j] = "無";
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			chessposition[i] = 0;
			rest[i] = 0;
			isout[i] = false;
		}
		for (int i = 0; i < 20; i++) {
			house[i] = -1;
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 20; k++) {
				houserank[i][j][k] = "";
				}
			}
		}
	}
	
	public void chess_move() {
		//currentRole_ID:玩家順序 0是玩家1，1是玩家2，2是玩家3，3是玩家4
		chessposition[currentRole_ID] += dice_num;
		//chessposition(棋子位置):0~19，如果超過19就要-20
		if (chessposition[currentRole_ID] > 19) {
			chessposition[currentRole_ID] -=20 ;
			lap[currentRole_ID]++; 
			money[currentRole_ID] += 3000;
		}		
		t.schedule(new move_timerTask(), 1700);
	}

	class move_timerTask extends TimerTask{
		@Override
		public void run() {
			//4個角落的棋子座標
			if(chessposition[currentRole_ID] >= 1 && chessposition[currentRole_ID] <= 7){
				int outbound = chessposition[currentRole_ID] - 0;	//超過Start幾格
				chessposition_x[currentRole_ID] = start_x[currentRole_ID] + outbound*65;
				chessposition_y[currentRole_ID] = start_y[currentRole_ID];
				chess_jl[currentRole_ID].setBounds(chessposition_x[currentRole_ID], chessposition_y[currentRole_ID], role[currentRole_ID].getIconWidth(), role[currentRole_ID].getIconHeight());
			}
			else if(chessposition[currentRole_ID] >= 8 && chessposition[currentRole_ID] <= 10) {
				int outbound = chessposition[currentRole_ID] - 7;	//超過Prison幾格		
				chessposition_x[currentRole_ID] = prison_x[currentRole_ID];
				chessposition_y[currentRole_ID] = prison_y[currentRole_ID] + outbound*96;
				chess_jl[currentRole_ID].setBounds(chessposition_x[currentRole_ID], chessposition_y[currentRole_ID], role[currentRole_ID].getIconWidth(), role[currentRole_ID].getIconHeight());
			}
			else if(chessposition[currentRole_ID] >= 11 && chessposition[currentRole_ID] <= 17) {
				int outbound = chessposition[currentRole_ID] - 10; //超過BackToStart幾格		
				chessposition_x[currentRole_ID] = backtostart_x[currentRole_ID] - outbound*65;
				chessposition_y[currentRole_ID] = backtostart_y[currentRole_ID];
				chess_jl[currentRole_ID].setBounds(chessposition_x[currentRole_ID], chessposition_y[currentRole_ID], role[currentRole_ID].getIconWidth(), role[currentRole_ID].getIconHeight());
			}
			else if(chessposition[currentRole_ID] == 18 || chessposition[currentRole_ID] == 19 || chessposition[currentRole_ID] == 0) {
				if(chessposition[currentRole_ID] == 0) {
					chessposition_x[currentRole_ID] = start_x[currentRole_ID];
					chessposition_y[currentRole_ID] = start_y[currentRole_ID]; 
				}else {
					int outbound = chessposition[currentRole_ID] - 17; //超過Park幾格		
					chessposition_x[currentRole_ID] = park_x[currentRole_ID];
					chessposition_y[currentRole_ID] = park_y[currentRole_ID] - outbound*96;
				}
				chess_jl[currentRole_ID].setBounds(chessposition_x[currentRole_ID], chessposition_y[currentRole_ID], role[currentRole_ID].getIconWidth(), role[currentRole_ID].getIconHeight());
			}
			chess_action();		
		}
	}
	
	public void chess_action() {
		if (chessposition[currentRole_ID] == 0 || chessposition[currentRole_ID] == 7 || chessposition[currentRole_ID] == 10 || chessposition[currentRole_ID] == 17) {
			//4個角落的
			if (chessposition[currentRole_ID] == 0 ) {
				alert_jl.setText("走回起點了!獲得3000元");
			}
			if (chessposition[currentRole_ID] == 7 ) {
				alert_jl.setText("走到監獄，休息1回合");
				rest[currentRole_ID] = 1;
			}
			if (chessposition[currentRole_ID] == 10 ) {
				alert_jl.setText("走到返回起點");
				chessposition[currentRole_ID] = 0;
				chessposition_x[currentRole_ID] = start_x[currentRole_ID];
				chessposition_y[currentRole_ID] = start_y[currentRole_ID];
				chess_jl[currentRole_ID].setBounds(chessposition_x[currentRole_ID], chessposition_y[currentRole_ID], role[currentRole_ID].getIconWidth(), role[currentRole_ID].getIconHeight());
			}
			if (chessposition[currentRole_ID] == 17 ) {
				alert_jl.setText("走到公園，休息2回合");
				rest[currentRole_ID] = 2;
			}
			continue_jb.setVisible(true);
			while(true) {
				sleep(100);
				if(iscontinue) {
					iscontinue = false;
					break;
				}
			}
			print_player_informaiton();
		}
		else if (chessposition[currentRole_ID] == 5 || chessposition[currentRole_ID] == 15) {
			//機會
			alert_jl.setText("走到機會格了!點擊按鈕翻開機會卡!");
	        String continuebt_path = "images/open_button.png"; 
	        ImageIcon continuebtImage = new ImageIcon(continuebt_path);
	        continue_jb.setIcon(continuebtImage);
			continue_jb.setVisible(true);
			while(true) {
				sleep(100);
				if(iscontinue) {
					iscontinue = false;
					break;
				}
			}
			chance_cards(); //機會函式
	        continuebt_path = "images/continue_button.png"; 
	        continuebtImage = new ImageIcon(continuebt_path);
	        continue_jb.setIcon(continuebtImage);
			continue_jb.setVisible(true);
			while(true) {
				sleep(100);
				if(iscontinue) {
					iscontinue = false;
					break;
				}
			}
			print_player_informaiton(); //print玩家資訊
		}
		else if (chessposition[currentRole_ID] == 2 || chessposition[currentRole_ID] == 12) {
			//命運			
			alert_jl.setText("走到命運格了!點擊按鈕翻開命運卡!");
	        String continuebt_path = "images/open_button.png"; 
	        ImageIcon continuebtImage = new ImageIcon(continuebt_path);
	        continue_jb.setIcon(continuebtImage);
			continue_jb.setVisible(true);
			while(true) {
				sleep(100);
				if(iscontinue) {
					iscontinue = false;
					break;
				}
			}			
			community_chest_cards(); //命運函式
	        continuebt_path = "images/continue_button.png"; 
	        continuebtImage = new ImageIcon(continuebt_path);
	        continue_jb.setIcon(continuebtImage);
			continue_jb.setVisible(true);
			while(true) {
				sleep(100);
				if(iscontinue) {
					iscontinue = false;
					break;
				}
			}
			print_player_informaiton(); //print玩家資訊
		}
		else {
			//檢查能不能買土地或蓋房子
			boolean has_house_been_bought = check_house(); //檢查土地擁有權
			if (has_house_been_bought == true && house_jl[chessposition[currentRole_ID]][4].isVisible() == false) {
				country_cost_path = String.format("images/cost_%d.png",chessposition[currentRole_ID]); //國家花費卡路徑
				country_fee_path = String.format("images/fee_%d.png",chessposition[currentRole_ID]); //國家過路費卡路徑
				country_costImage = new ImageIcon(country_cost_path);
				country_feeImage = new ImageIcon(country_fee_path);
				countrycost_jl.setIcon(country_costImage);
				countryfee_jl.setIcon(country_feeImage);
				countrycost_jl.setVisible(true);
				countryfee_jl.setVisible(true);
				alert_jl.setText("請問是否要購買土地or蓋房子?");
				accept_jb.setVisible(true);
				cancel_jb.setVisible(true);
				isaccept = false;
				iscancel = false;
				while(true) {
					sleep(100);
					if (isaccept || iscancel) {
						iscancel = false;
						countrycost_jl.setVisible(false);
						countryfee_jl.setVisible(false);
						break;
					}
				}
				if(isaccept) {
					buy_house();
				}
			}
			else {
				//付過路費
				alert_jl.setText("哎呀!走到別人的土地了!");
				sleep(1500);
				if(rest[house[chessposition[currentRole_ID]]] == 0) {
					country_fee_path = String.format("images/fee_%d.png",chessposition[currentRole_ID]); //國家過路費卡路徑
					country_feeImage = new ImageIcon(country_fee_path);
					countryfee_jl.setIcon(country_feeImage);
					countryfee_jl.setVisible(true);
					for (int i = 1; i <= 4; i++) {
						if(house_jl[chessposition[currentRole_ID]][i].isVisible() == true) {
							alert_jl.setText("你要付" + countryinformation[i+3][chessposition[currentRole_ID]] + "元給玩家" + ((house[chessposition[currentRole_ID]])+1)); //如果house[chessposition[currentRole_ID]] = 0代表是玩家1的，所以要+1(0+1=玩家1)
							money[currentRole_ID] -= countryinformation[i+3][chessposition[currentRole_ID]];
							money[(house[chessposition[currentRole_ID]])] += countryinformation[i+3][chessposition[currentRole_ID]];
						}
					}
				}else {
					alert_jl.setText("玩家" + ((house[chessposition[currentRole_ID]])+1) + "休息中，無須付過路費");
				}
				continue_jb.setVisible(true);
				while(true) {
					sleep(100);
					if(iscontinue) {
						countryfee_jl.setVisible(false);
						iscontinue = false;
						break;
					}
				}				
			}	
		print_player_informaiton(); //print玩家資訊	
		}
		isroundover = true;
	}
	
	public void print_player_informaiton() {
		alert_jl.setText("<html>"
				+ "<body><span>你現在所持有的金額:"+ money[currentRole_ID] +"</span><br/>"
				+ "<span>你現在所持有的土地:"+ player_information[currentRole_ID][0] +"</span><br/>"
				+ "<span>你現在蓋一棟房子的國家:"+ player_information[currentRole_ID][1] +"</span><br/>"
				+ "<span>你現在蓋二棟房子的國家:"+ player_information[currentRole_ID][2] +"</span><br/>"
				+ "<span>你現在蓋三棟房子的國家:"+ player_information[currentRole_ID][3] +"</span><br/>"
				+ "</body>"
				+ "</html>");
	}

	//國家土地和房子費用及過路費
	public void country_information() {
		
		/*[0][i]買土地的錢		[4][i]買土地的過路費
		 *[1][i]蓋一棟房子的錢	[5][i]蓋一棟房子的過路費
		 *[2][i]蓋兩棟房子的錢	[6][i]蓋兩棟房子的過路費
		 *[3][i]蓋三棟房子的錢	[7][i]蓋三棟房子的過路費
		 */
		
		//start
		countryinformation[0][0] = 1500;
		
		//KOR
		//cost
		countryinformation[0][1] = 2200;
		countryinformation[1][1] = 800;
		countryinformation[2][1] = 1500;
		countryinformation[3][1] = 3000;
		//fee
		countryinformation[4][1] = 880;
		countryinformation[5][1] = 1280;
		countryinformation[6][1] = 1630;
		countryinformation[7][1] = 2380;
		
		//JPN
		//cost
		countryinformation[0][3] = 2400;
		countryinformation[1][3] = 1000;
		countryinformation[2][3] = 1800;
		countryinformation[3][3] = 3500;
		//fee
		countryinformation[4][3] = 960;
		countryinformation[5][3] = 1460;
		countryinformation[6][3] = 1860;
		countryinformation[7][3] = 2710;
		
		//MEX
		//cost
		countryinformation[0][4] = 3000;
		countryinformation[1][4] = 1300;
		countryinformation[2][4] = 2400;
		countryinformation[3][4] = 4200;
		//fee
		countryinformation[4][4] = 1200;
		countryinformation[5][4] = 1850;
		countryinformation[6][4] = 2400;
		countryinformation[7][4] = 3300;
		
		//NZL
		//cost
		countryinformation[0][6] = 3600;
		countryinformation[1][6] = 1500;
		countryinformation[2][6] = 2800;
		countryinformation[3][6] = 5500;
		//fee
		countryinformation[4][6] = 1440;
		countryinformation[5][6] = 2190;
		countryinformation[6][6] = 2840;
		countryinformation[7][6] = 4190;
		
		//USA
		//cost
		countryinformation[0][8] = 2200;
		countryinformation[1][8] = 800;
		countryinformation[2][8] = 1500;
		countryinformation[3][8] = 3000;
		//fee
		countryinformation[4][8] = 880;
		countryinformation[5][8] = 1280;
		countryinformation[6][8] = 1630;
		countryinformation[7][8] = 2380;
		
		//RUS
		//cost
		countryinformation[0][9] = 1000;
		countryinformation[1][9] = 500;
		countryinformation[2][9] = 800;
		countryinformation[3][9] = 1500;
		//fee
		countryinformation[4][9] = 400;
		countryinformation[5][9] = 650;
		countryinformation[6][9] = 800;
		countryinformation[7][9] = 1150;
		
		//SGP
		//cost
		countryinformation[0][11] = 3000;
		countryinformation[1][11] = 1300;
		countryinformation[2][11] = 2400;
		countryinformation[3][11] = 4200;
		//fee
		countryinformation[4][11] = 1200;
		countryinformation[5][11] = 1900;
		countryinformation[6][11] = 2400;
		countryinformation[7][11] = 3300;
		
		//ESP
		//cost
		countryinformation[0][13] = 1600;
		countryinformation[1][13] = 900;
		countryinformation[2][13] = 1900;
		countryinformation[3][13] = 3700;
		//fee
		countryinformation[4][13] = 640;
		countryinformation[5][13] = 1090;
		countryinformation[6][13] = 1590;
		countryinformation[7][13] = 2490;
		
		//THA
		//cost
		countryinformation[0][14] = 2000;
		countryinformation[1][14] = 700;
		countryinformation[2][14] = 1400;
		countryinformation[3][14] = 2900;
		//fee
		countryinformation[4][14] = 800;
		countryinformation[5][14] = 1150;
		countryinformation[6][14] = 1500;
		countryinformation[7][14] = 2250;
		
		//AUS
		//cost
		countryinformation[0][16] = 2400;
		countryinformation[1][16] = 1000;
		countryinformation[2][16] = 1800;
		countryinformation[3][16] = 3500;
		//fee
		countryinformation[4][16] = 960;
		countryinformation[5][16] = 1460;
		countryinformation[6][16] = 1860;
		countryinformation[7][16] = 2710;
		
		//TUR
		//cost
		countryinformation[0][18] = 2200;
		countryinformation[1][18] = 800;
		countryinformation[2][18] = 1500;
		countryinformation[3][18] = 3000;
		//fee
		countryinformation[4][18] = 880;
		countryinformation[5][18] = 1280;
		countryinformation[6][18] = 1630;
		countryinformation[7][18] = 2300;
		
		//TWN
		//cost
		countryinformation[0][19] = 4000;
		countryinformation[1][19] = 1800;
		countryinformation[2][19] = 3200;
		countryinformation[3][19] = 6000;
		//fee
		countryinformation[4][19] = 1600;
		countryinformation[5][19] = 2500;
		countryinformation[6][19] = 3200;
		countryinformation[7][19] = 4600;
	}
	
	public boolean check_house() {
		//如果土地是自己的，boolean值為true
		boolean has_house_been_bought = true; 
		boolean house_notvisible = false; //判斷房子圖示是否為隱藏
		for(int i = 1; i < 4; i++) {
			if(house_jl[chessposition[currentRole_ID]][i].isVisible() == false) {
				house_notvisible = true;					
			}else {
				house_notvisible = false;
				break;
			}
		}
		
		if ((house_notvisible == true) || (house[chessposition[currentRole_ID]]) == currentRole_ID) { 
			has_house_been_bought = true;
		}else {
			has_house_been_bought = false;
		}
			
		return(has_house_been_bought);
	}
	
	//購買土地或蓋房子
	public void buy_house() {
		
		for (int i = 0; i <= 3; i++) {
			if (money[currentRole_ID] >= countryinformation[i][chessposition[currentRole_ID]]) { //如果錢夠多
				if (house_jl[chessposition[currentRole_ID]][i].isVisible() == true) {
					house[chessposition[currentRole_ID]] = currentRole_ID;
					house_jl[chessposition[currentRole_ID]][i].setVisible(false);
					house_jl[chessposition[currentRole_ID]][i+1].setVisible(true);
					money[currentRole_ID] -= countryinformation[i][chessposition[currentRole_ID]];
					for (int j = 1; j <= 4; j++) {
						if (player_information[currentRole_ID][i] == "無") {
							player_information[currentRole_ID][i] = "";
						}
						if (house_jl[chessposition[currentRole_ID]][1].isVisible() == true) { //symbol[5]:" ■ "(土地)
							//houserank[i][j][k] i放入currentRole_ID，代表玩家 j代表房子階級 k放入chessposition[currentRole_ID]，代表國家
							houserank[currentRole_ID][0][chessposition[currentRole_ID]] = country_name[chessposition[currentRole_ID]] + " "; //在houserank放入國家名
						}
						else if (house_jl[chessposition[currentRole_ID]][j].isVisible() == true) { //symbol[j]:有蓋房子
							//如果玩家蓋新房子，ex:蓋一棟房子
							houserank[currentRole_ID][j-2][chessposition[currentRole_ID]] = ""; //將國家名從舊陣列(土地)拿出
							houserank[currentRole_ID][j-1][chessposition[currentRole_ID]] = country_name[chessposition[currentRole_ID]] + " "; //將國家名放入新陣列(一棟房子)
						}
					}
					for (int k = 0; k <= 3; k++) {
						if (player_information[currentRole_ID][k] != "無") {
							player_information[currentRole_ID][k] = "";
							for (int m = 0; m <= 19; m++) {
								//將houserank陣列裡的國家名照順序放入player_information陣列，之後要print出來
								player_information[currentRole_ID][k] = player_information[currentRole_ID][k] + houserank[currentRole_ID][k][m];
							}
							//如果player_information陣列裡沒有國家名，則放"無"
							if (player_information[currentRole_ID][k] == "") {
								player_information[currentRole_ID][k] = "無";
							}
						}
					}
				}
				else {
					continue;
				}
				alert_jl.setText("恭喜你成功購買土地");	
				sleep(1500);
				break;
			}
			else {
				alert_jl.setText("你的錢不夠買呦!");
				sleep(1500);
				break;
			}
		}
	}
	
	//機會
	public void chance_cards() {
		
		int chancecard = (int)(Math.random()*9+1);
		switch(chancecard) {
			case 1:
				alert_jl.setText("去新加坡吃口香糖，被政府抓罰200元");
				money[currentRole_ID] -= 200;
				break;
			case 2:
				alert_jl.setText("在日本欣賞藝妓表演花費500元");
				money[currentRole_ID] -= 500;
				break;
			case 3:
				alert_jl.setText("去泰國抓飯吃沒洗手，感染痢疾休息1回合");
				rest[currentRole_ID] = 1;
				break;
			case 4:
				alert_jl.setText("在台北101看煙火，撿到800元");
				money[currentRole_ID] += 800;
				break;
			case 5:
				alert_jl.setText("到北海道擠牛奶，精神百倍，前進1格");					
				chessposition[currentRole_ID] += 1; //棋子位置+1
				if(chessposition[currentRole_ID] == 6) {
					chessposition_x[currentRole_ID] += 65;
				}else if(chessposition[currentRole_ID] == 16) {
					chessposition_x[currentRole_ID] -= 65;
				}
				chess_jl[currentRole_ID].setBounds(chessposition_x[currentRole_ID], chessposition_y[currentRole_ID], role[currentRole_ID].getIconWidth(), role[currentRole_ID].getIconHeight()); //將棋子移到新位置
				break;
			case 6:
				alert_jl.setText("在韓國路上搶劫，到監獄看守2回合");					
				chessposition[currentRole_ID] = 7; //棋子位置改到監獄位置
				chessposition_x[currentRole_ID] = prison_x[currentRole_ID];
				chessposition_y[currentRole_ID] = prison_y[currentRole_ID];
				chess_jl[currentRole_ID].setBounds(chessposition_x[currentRole_ID], chessposition_y[currentRole_ID], role[currentRole_ID].getIconWidth(), role[currentRole_ID].getIconHeight()); //將棋子移到新位置
				rest[currentRole_ID] = 2; //休息2回合，暫停陣列放2
				
				break;	
			case 7:
				alert_jl.setText("到泰國被誤認為是人妖被抓去表演人妖秀，賺了500元");
				money[currentRole_ID] += 500;
				break;	
			case 8:
				alert_jl.setText("到墨西哥學習做捲餅，自己開店賺了1000元");
				money[currentRole_ID] += 1000;
				break;
			case 9:
				alert_jl.setText("<html><body>在台灣學會當魔術師，跟最多錢的玩家交換金錢，<br>若自己最多可額外獲得1000元</body></html>");
				int max = 0;
				int player = -1;
				for(int i = 0; i < 4; i++) {
					if(money[i] > max) {
						max = money[i];
						player = i;
					}	
				}
				if(player == currentRole_ID) {
					money[currentRole_ID] += 1000;
				}else {
					money[player] = money[currentRole_ID];
					money[currentRole_ID] = max;
				}	
		}
	}
	
	//命運
	public void community_chest_cards() {
		int communitychestcard = (int)(Math.random()*9+1);
		switch(communitychestcard) {
			case 1:
				alert_jl.setText("在紐西蘭泡溫泉泡到昏倒送醫急救，花費200元");
				money[currentRole_ID] -= 200;
				break;
			case 2:
				alert_jl.setText("看到自由女神被嚇到，收驚花500元");
				money[currentRole_ID] -= 500;
				break;
			case 3:
				alert_jl.setText("到俄羅斯遇到大風雪，到公園休息2回合");					
				chessposition[currentRole_ID] = 17; //棋子位置改到公園位置
				chessposition_x[currentRole_ID] = park_x[currentRole_ID];
				chessposition_y[currentRole_ID] = park_y[currentRole_ID];
				chess_jl[currentRole_ID].setBounds(chessposition_x[currentRole_ID], chessposition_y[currentRole_ID], role[currentRole_ID].getIconWidth(), role[currentRole_ID].getIconHeight()); //將棋子移到新位置
				rest[currentRole_ID] = 2; //休息2回合，暫停陣列放2
				
				break;
			case 4:
				alert_jl.setText("在日本收集到7顆龍珠，跟所有玩家各收取200元");
				//跟所有玩家各收取200元:自己+600，其他人-200
				//自己先+800，再全部人-200 = 自己+600，其他人-200
				money[currentRole_ID] += 800;
				for(int i = 0; i < 4; i++) {
					money[i] -= 200;
				}
				break;
			case 5:
				alert_jl.setText("到美國看王建民，要到簽名，轉手獲利500元");
				money[currentRole_ID] += 500;
				break;
			case 6:
				alert_jl.setText("<html><body>到西班牙參加鬥牛節，不慎發身意外，<br>所有玩家捐出200元</body></html>");
				for(int i = 0; i < 4; i++) {
					money[i] -= 200;
				}
				break;
			case 7:
				alert_jl.setText("到澳洲打工度假，賺了1000元");
				money[currentRole_ID] += 1000;
				break;
			case 8:
				alert_jl.setText("到土耳其跳旋轉舞，頭暈跌到，倒退1格");
				chessposition[currentRole_ID] -= 1; //棋子位置-1
				if(chessposition[currentRole_ID] == 1) {
					chessposition_x[currentRole_ID] -= 65;
				}else if(chessposition[currentRole_ID] == 11) {
					chessposition_x[currentRole_ID] += 65;
				}
				chess_jl[currentRole_ID].setBounds(chessposition_x[currentRole_ID], chessposition_y[currentRole_ID], role[currentRole_ID].getIconWidth(), role[currentRole_ID].getIconHeight());//將棋子放入新位置
				break;
			case 9:
				alert_jl.setText("搭郵輪遇到風暴，跟隨機一位玩家交換位置");
				continue_jb.setVisible(true);
				while(true) {
					sleep(100);
					if(iscontinue) {
						countryfee_jl.setVisible(false);
						iscontinue = false;
						break;
					}
				}		
				int random;
				int temp;
				do {
					random = (int)(Math.random()*4);
				}while(random == currentRole_ID);
				alert_jl.setText("和玩家"+ (random+1) + "交換位置!");
				temp = chessposition[currentRole_ID];
				chessposition[currentRole_ID] = chessposition[random];
				chessposition[random] = temp;
				temp = chessposition_x[currentRole_ID];
				chessposition_x[currentRole_ID] = chessposition_x[random];
				chessposition_x[random] = temp;
				temp = chessposition_y[currentRole_ID];
				chessposition_y[currentRole_ID] = chessposition_y[random];
				chessposition_y[random] = temp;
				chess_jl[currentRole_ID].setBounds(chessposition_x[currentRole_ID], chessposition_y[currentRole_ID], role[currentRole_ID].getIconWidth(), role[currentRole_ID].getIconHeight());
				chess_jl[random].setBounds(chessposition_x[random], chessposition_y[random], role[random].getIconWidth(), role[random].getIconHeight());
		}
	}
	
	public void prop(){
		int prop = (int)(Math.random()*5+1);
		switch(prop) {
			case 1:
				alert_jl.setText("獲得炸彈，將第一名的玩家炸回起點");
				int max = 0;
				int player = currentRole_ID;
				for(int i = 0; i < 4; i++) {
					if((lap[i] + chessposition[i]) > max) {
						max = lap[i] + chessposition[i];
						player = i;
					}	
				}
				if(currentRole_ID != player) {
					chessposition[player] = 0;
					chessposition_x[player] = start_x[player];
					chessposition_y[player] = start_y[player];
					chess_jl[player].setBounds(chessposition_x[player], chessposition_y[player], role[player].getIconWidth(), role[player].getIconHeight());
				}
				
				break;
			case 2:
				alert_jl.setText("獲得飛機，走完該圈回到起點");
				chessposition_x[currentRole_ID] = start_x[currentRole_ID];
				chessposition_y[currentRole_ID] = start_y[currentRole_ID];
				chess_jl[currentRole_ID].setBounds(chessposition_x[currentRole_ID], chessposition_y[currentRole_ID], role[currentRole_ID].getIconWidth(), role[currentRole_ID].getIconHeight());
				lap[currentRole_ID]++;
				money[currentRole_ID] += 3000;
				break;
			case 3:
				alert_jl.setText("獲得複製能力，骰子點數變成3倍");
				isdicethreetimes = true;
				break;
			case 4:
				alert_jl.setText("獲得印鈔機，現有金錢變成2倍");
				money[currentRole_ID] *= 2;
				break;
			case 5:
				alert_jl.setText("變身為時間能力者，其他玩家暫停1回合");
				for(int i = 0; i < 4; i++) {
					if(currentRole_ID != i) {
						rest[i] += 1; 
					}
				}
				break;
		}
	}
	
	//結束遊戲判定
	public boolean check_end() {
		boolean end = false;
		//如果有人破產，代表遊戲結束，boolean值end = true
		if(isclassic) {
			if(money[currentRole_ID] < 0) {
				end = true;
				rank();	
				alert_jl.setFont(new Font("標楷體",Font.PLAIN,48));
				alert_jl.setText("Game Over");
				
			}
		}else if(isprop) {
			for(int i = 0; i < 4; i++) {
				if(money[i] < 0) {
					isout[i] = true;
				}
			}
			if(lap[currentRole_ID] >= 5) {
				end = true;
				alert_jl.setFont(new Font("標楷體",Font.PLAIN,48));
				alert_jl.setText("Game Over");
			}
		}
		//回傳end = true 或 false
		return(end);
	}
	
	public void rank() {
		int moneychange;
		int classicchange;
		
		int lapchange;
		int richchange;
		
		int playermoneyorder[] = new int[4]; //金錢排名順序
		int playerclassicorder[] = new int[4]; //經典模式玩家排名順序
		
		int playerlaporder[] = new int[4]; //圈數排名順序
		int playerproporder[] = new int[4]; //富豪模式玩家排名順序
		//將玩家和對應持有的金錢放入陣列
		for (int i = 0; i < 4; i++) {
			playermoneyorder[i] = money[i];
			playerclassicorder[i] = i;
		}
		
		//將玩家和對應走過的格數放入陣列
		for (int i = 0; i < 4; i++) {
			playerlaporder[i] = lap[i]*20+chessposition[i];
			playerproporder[i] = i;
		}
	
			//排名順序
		for(int i=3 ; i>0 ; i--) {
			for(int j=0 ; j<i ; j++) {
				if(playermoneyorder[j] < playermoneyorder[j+1]) { //經典模式
					moneychange = playermoneyorder[j];
					classicchange = playerclassicorder[j];
					playermoneyorder[j] = playermoneyorder[j+1];
					playerclassicorder[j] = playerclassicorder[j+1];
					playermoneyorder[j+1] = moneychange;
					playerclassicorder[j+1] = classicchange;
				}
				
				if(playerlaporder[j] < playerlaporder[j+1]) { //道具模式
					lapchange = playerlaporder[j];
					richchange = playerproporder[j];
					playerlaporder[j] = playerlaporder[j+1];
					playerproporder[j] = playerproporder[j+1];
					playerlaporder[j+1] = lapchange;
					playerproporder[j+1] = richchange;
				}
			}
		}
		if(isclassic) {
			rank_jl.setText("<html>"
				+ "<body><span><font color = red size = 36><b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp玩家排名</b></span><br/>"
				+ "<span>第一名: 玩家"+ (playerclassicorder[0]+1) + "<img src = file:images/suitcase_"+playerclassicorder[0]+".png>" 
				+ " 擁有金額:" + playermoneyorder[0] +"</span><br/>"
				+ "<span>第二名: 玩家"+ (playerclassicorder[1]+1) + "<img src = file:images/suitcase_"+playerclassicorder[1]+".png>" 
				+ " 擁有金額:" + playermoneyorder[1] +"</span><br/>"
				+ "<span>第三名: 玩家"+ (playerclassicorder[2]+1) + "<img src = file:images/suitcase_"+playerclassicorder[2]+".png>" 
				+ " 擁有金額:" + playermoneyorder[2] +"</span><br/>"
				+ "<span>第四名: 玩家"+ (playerclassicorder[3]+1) + "<img src = file:images/suitcase_"+playerclassicorder[3]+".png>" 
				+ " 擁有金額:" + playermoneyorder[3] +"</span><br/>"
				+ "</body>"
				+ "</html>");
		}else if(isprop) {
			rank_jl.setText("<html>"
					+ "<body><span><font color = red size = 36><b>&nbsp&nbsp&nbsp&nbsp玩家排名</b></span><br/>"
					+ "<span>第一名: 玩家"+ (playerproporder[0]+1) + "<img src = file:images/suitcase_"+playerproporder[0]+".png>"
					+ " " + lap[playerproporder[0]] + " 圈 " + chessposition[playerproporder[0]] + " 格" +"</span><br/>"
					+ "<span>第二名: 玩家"+ (playerproporder[1]+1) + "<img src = file:images/suitcase_"+playerproporder[1]+".png>"
					+ " " + lap[playerproporder[1]] + " 圈 " + chessposition[playerproporder[1]] + " 格" +"</span><br/>"
					+ "<span>第三名: 玩家"+ (playerproporder[2]+1) + "<img src = file:images/suitcase_"+playerproporder[2]+".png>"
					+ " " + lap[playerproporder[2]] + " 圈 " + chessposition[playerproporder[2]] + " 格" +"</span><br/>"
					+ "<span>第四名: 玩家"+ (playerproporder[3]+1) + "<img src = file:images/suitcase_"+playerproporder[3]+".png>"
					+ " " + lap[playerproporder[3]] + " 圈 " + chessposition[playerproporder[3]] + " 格" +"</span><br/>"
					+ "</body>"
					+ "</html>");
		}		
	}
	
	public void rest() {
		rest_jl.setText("<html>"
				+ "<body><span><font color = red size = 36><b>&nbsp&nbsp&nbsp休息回合</b></span><br/>"
				+ "<img src = file:images/suitcase_0.png></img>"
				+ "<span>玩家1: 休息 " + rest[0] +" 回合</span><br/>"
				+ "<img src = file:images/suitcase_1.png></img>"
				+ "<span>玩家2: 休息 " + rest[1] +" 回合</span><br/>"
				+ "<img src = file:images/suitcase_2.png></img>"
				+ "<span>玩家3: 休息 " + rest[2] +" 回合</span><br/>"
				+ "<img src = file:images/suitcase_3.png></img>"
				+ "<span>玩家4: 休息 " + rest[3] +" 回合</span><br/>"
				+ "</body>"
				+ "</html>");
	}
	
	public void ownership() {
		ownership_jl.setText("<html>"
				+ "<body><span><font color = red size = 36><b>&nbsp&nbsp&nbsp&nbsp國家擁有權</b></span><br/>"
				+ "<span>KOR: 玩家"+ (house[1]+1) + "<img src = file:images/suitcase_"+house[1]+".png><span>&nbsp</span>"
				+ "JPN: 玩家"+ (house[3]+1) + "<img src = file:images/suitcase_"+house[3]+".png>" + "</span><br/>"
				+ "<span>MEX: 玩家"+ (house[4]+1) + "<img src = file:images/suitcase_"+house[4]+".png><span>&nbsp</span>"
				+ "NZL: 玩家"+ (house[6]+1) + "<img src = file:images/suitcase_"+house[6]+".png>" + "</span><br/>"
				+ "<span>USA: 玩家"+ (house[8]+1) + "<img src = file:images/suitcase_"+house[8]+".png><span>&nbsp</span>"
				+ "RUS: 玩家"+ (house[9]+1) + "<img src = file:images/suitcase_"+house[9]+".png>" + "</span><br/>"
				+ "<span>SGP: 玩家"+ (house[11]+1) + "<img src = file:images/suitcase_"+house[11]+".png><span>&nbsp</span>"
				+ "ESP: 玩家"+ (house[13]+1) + "<img src = file:images/suitcase_"+house[13]+".png>" + "</span><br/>"
				+ "<span>THA: 玩家"+ (house[14]+1) + "<img src = file:images/suitcase_"+house[14]+".png><span>&nbsp</span>"
				+ "AUS: 玩家"+ (house[16]+1) + "<img src = file:images/suitcase_"+house[16]+".png>" + "</span><br/>"
				+ "<span>TUR: 玩家"+ (house[18]+1) + "<img src = file:images/suitcase_"+house[18]+".png><span>&nbsp</span>"
				+ "TWN: 玩家"+ (house[19]+1) + "<img src = file:images/suitcase_"+house[19]+".png>" + "</span><br/>"
				+ "</body>"
				+ "</html>");
	}
	
	public void lap() {
		if(isclassic) {
			lap_jl.setText("<html>"
				+ "<body><span><font color = red size = 36><b>&nbsp&nbsp&nbsp&nbsp玩家圈數</b></span><br/>"
				+ "<img src = file:images/suitcase_0.png></img>"
				+ "<span>玩家1: 圈數 " + lap[0] +" 圈 " + chessposition[0] + " 格</span><br/>"
				+ "<img src = file:images/suitcase_1.png></img>"
				+ "<span>玩家2: 圈數 " + lap[1] +" 圈 " + chessposition[1] + " 格</span><br/>"
				+ "<img src = file:images/suitcase_2.png></img>"
				+ "<span>玩家3: 圈數 " + lap[2] +" 圈 " + chessposition[2] + " 格</span><br/>"
				+ "<img src = file:images/suitcase_3.png></img>"
				+ "<span>玩家4: 圈數 " + lap[3] +" 圈 " + chessposition[3] + " 格</span><br/>"
				+ "</body>"
				+ "</html>");
		}else if(isprop) {
			lap_jl.setText("<html>"
					+ "<body><span><font color = red size = 36><b>&nbsp&nbsp&nbsp&nbsp玩家金額</b></span><br/>"
					+ "<img src = file:images/suitcase_0.png></img>"
					+ "<span>玩家1: 擁有金額: " + money[0] + " </span><br/>"
					+ "<img src = file:images/suitcase_1.png></img>"
					+ "<span>玩家2: 擁有金額: " + money[1] + " </span><br/>"
					+ "<img src = file:images/suitcase_2.png></img>"
					+ "<span>玩家3: 擁有金額: " + money[2] + " </span><br/>"
					+ "<img src = file:images/suitcase_3.png></img>"
					+ "<span>玩家4: 擁有金額: " + money[3] + " </span><br/>"
					+ "</body>"
					+ "</html>");
		}
	}
	
	public void sleep(int time) {
		try {
		    Thread.sleep(time);
		} catch(Exception e) {
		}
	}	
}
