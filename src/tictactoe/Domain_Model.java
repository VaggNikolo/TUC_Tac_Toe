package tictactoe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

class YourJLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	YourJLabel() {
		super();
		setOpaque(true); // going on memory, by default jlabels opaque is false, or transparent
	}

	protected void paintComponent(Graphics gr) {
		super.paintComponent(gr);
	}
}

public class Domain_Model {

	static int Game_Mode;
	// HUMAN -> HUMAN = 0
	// HUMAN -> AI SMART = 1
	// AI SMART -> AI SMART = 2
	// HUMAN -> AI MRBEAN = 3
	// AI MRBEAN -> AI MRBEAN = 4
	// AI SMART -> AI MRBEAN = 5
	// AI SMART -> HUMAN = 6
	// AI MRBEAN -> HUMAN = 7
	// AI MRBEAN -> AI SMART = 8

	static JFrame frame = new JFrame();
	static JPanel Player1_Panel = new JPanel();
	static JPanel Player2_Panel = new JPanel();
	static JPanel Game_Panel = new JPanel();
	static JPanel Hall_Of_Fame_Panel = new JPanel();
	static JPanel Mouse_Panel = new JPanel();

	// 3x3 Grid Buttons
	static JButton cell[] = new JButton[10];

	// initialize player structure
	Object[] Accounts = { "Smart_AI", "Mr_Bean" };
	static Player_Account Player_List[] = new Player_Account[1000];
	static int Player_Count = 0;
	static boolean Player1_Ready = false;
	static boolean Player2_Ready = false;
	static Player_Account Player1_current = new Player_Account();
	static Player_Account Player2_current = new Player_Account();;
	
	//domain junit
	//WRITE PLAYER TO FILE
	public static void Write_Player_To_File(Player_Account[] Players) throws IOException {
		PrintWriter writer = new PrintWriter("tuctactoe.ser");
		writer.print("");
		writer.close();
		FileOutputStream f = new FileOutputStream("tuctactoe.ser");
		ObjectOutputStream o = new ObjectOutputStream(f);
		o.writeObject(Players);
		o.reset();
		o.close();
		f.close();
	}
	
	//domain junit
	//READ ALL PLAYERS FROM FILE
	public static void Read_Players_From_File() throws IOException, ClassNotFoundException {
		File f = new File("tuctactoe.ser");
		if(!f.exists()){
		  f.createNewFile();
		}else{
		  System.out.println("File already exists");
		}
		FileInputStream fi = new FileInputStream("tuctactoe.ser");
		if (fi.available() != 0){
	        ObjectInputStream oi = new ObjectInputStream(fi);
	        Player_Account[] Players = (Player_Account[]) oi.readObject();
	        if (Players == null) {
	        	oi.close();
	        	fi.close();
	        	return;
	        }
	        Player_List = Players;
	        int i = 0;
	        while (Player_List[i] != null) {
	        	System.out.println("Player name "+ Player_List[i].Player_Name +" ");
	        	i++;
	        }
	        Player_Count = i;
	        System.out.println("Player read "+ Player_Count +" ");

        	oi.close();
        	fi.close();
		}
	}
	
	
	//domain junit
	//CHECK IF PLAYER EXISTS TO FILE
	public static int Check_If_Player_Exists(Player_Account Player) throws IOException, ClassNotFoundException {
	        int i = 0;
	        while(Player_List[i] != null) {
	        	 if(Player_List[i].Player_Name.equals(Player.Player_Name)) {
	        		 return 1;
	        	 }
	        	 i++;
        	}
	        return 0;
	}

	//domain junit
	//converts Player List to Player names array
	public static String[] Player_List_to_String_Array(Player_Account[] Player_List) {
		String Player_Names[] = new String[Player_Count];
		for (int i = 0; i < Player_Count; i++) {
			Player_Names[i] = Player_List[i].Player_Name;
		}
		return Player_Names;
	}

	// Enum defining current player
	enum Player {
		Player1, Player2
	}

	public static Player Player_turn = Player.Player1;
	
	//domain junit
	// Function Get_Player returns current player
	public static int Get_Player() {
		return Player_turn.ordinal();
	}
	
	//domain junit
	// Function Get_Player returns reverse player
	public static int Get_Player_Reverse() {
		if (Get_Player() == 0) {
			return 1;
		} else {
			return 0;
		}
	}
	
	//domain junit
	// Function Rank_Players ranks the player list 
	public static Player_Account[] Rank_Players(Player_Account[] Players) {
			Player_Account[] Players_Ranked = Players;
			for (int i = 0 ; i < Players_Ranked.length-1; i++) {
				Player_Account p = Players_Ranked[i];
				Player_Account next =  Players_Ranked[i+1];
				Player_Account tmp;
				
				if(p == null || next == null) {
					continue;
				}
				
				if(p.Score_Calculate(p) < next.Score_Calculate(next)) {
					tmp = Players_Ranked[i];
					Players_Ranked[i] = Players_Ranked[i+1];
					Players_Ranked[i+1] = tmp;
			    }
			}
			return Players_Ranked;
		}
	
	
	// Get_Board returns current board state
	public static JButton[] Get_Board() {
		return cell;
	}

	// Tic_Tac_Toe Image Icons
	static ImageIcon[] Tic_Tac_Toe_Icons = new ImageIcon[] { new ImageIcon("blank_cell.png"), new ImageIcon("X.png"),
			new ImageIcon("O.png") };

	// Function grid initializer creates jbuttons within the grid with blank icons
	public static void Grid_initializer(JPanel Game_Panel) {
		for (int i = 0; i < 9; i++) {
			cell[i] = new JButton("");
			Game_Panel.add(cell[i]);
		}
	}

	// Function grid reset resets grid with blank icons
	public static void Grid_reset() {
		for (int i = 0; i < 9; i++) {
			cell[i].setIcon(Tic_Tac_Toe_Icons[0]);
		}
	}

	
	//domain junit
	// Three cell checker
	public static int Three_Cell_Checker(Icon cell1, Icon cell2, Icon cell3) {
		if (cell1 == cell2 && cell2 == cell3 && cell1 != Tic_Tac_Toe_Icons[0]) {
			// System.out.println("Player "+ Player_turn.ordinal() +" Won");
			return Player_turn.ordinal();
		}
		return -1;
	}

	//domain junit
	// Function Game State Checker returns the state of the game | return 0 if Player 1 Won |
	// return 1 if Player 2 Won | return 2 if Draw | return 3 if Continue |
	public static int Game_State_Checker() {
		int state;

		state = Three_Cell_Checker(cell[0].getIcon(), cell[1].getIcon(), cell[2].getIcon());
		if (state != -1) {
			return Get_Player_Reverse();
		}
		state = Three_Cell_Checker(cell[3].getIcon(), cell[4].getIcon(), cell[5].getIcon());
		if (state != -1) {
			return Get_Player_Reverse();
		}
		state = Three_Cell_Checker(cell[6].getIcon(), cell[7].getIcon(), cell[8].getIcon());
		if (state != -1) {
			return Get_Player_Reverse();
		}

		state = Three_Cell_Checker(cell[0].getIcon(), cell[3].getIcon(), cell[6].getIcon());
		if (state != -1) {
			return Get_Player_Reverse();
		}
		state = Three_Cell_Checker(cell[1].getIcon(), cell[4].getIcon(), cell[7].getIcon());
		if (state != -1) {
			return Get_Player_Reverse();
		}
		state = Three_Cell_Checker(cell[2].getIcon(), cell[5].getIcon(), cell[8].getIcon());
		if (state != -1) {
			return Get_Player_Reverse();
		}

		state = Three_Cell_Checker(cell[0].getIcon(), cell[4].getIcon(), cell[8].getIcon());
		if (state != -1) {
			return Get_Player_Reverse();
		}
		state = Three_Cell_Checker(cell[2].getIcon(), cell[4].getIcon(), cell[6].getIcon());
		if (state != -1) {
			return Get_Player_Reverse();
		}

		for (int i = 0; i < 9; i++) {
			if (cell[i].getIcon() == Tic_Tac_Toe_Icons[0]) {
				return 3;
			}
		}

		return 2;
	}

	//domain junit
	// Function Cell Finder selects the wanted cell based on mouse coordinates
	public static int Cell_Finder(int x, int y) {
		int chosen_cell_X = x / 200;
		int chosen_cell_Y = y / 220;
		int chosen_cell = 1;

		if (chosen_cell_X < 1 && chosen_cell_Y < 1) {
			chosen_cell = 0;
		} else if (chosen_cell_X < 2 && chosen_cell_Y < 1) {
			chosen_cell = 1;
		} else if (chosen_cell_X < 3 && chosen_cell_Y < 1) {
			chosen_cell = 2;
		} else if (chosen_cell_X < 1 && chosen_cell_Y < 2) {
			chosen_cell = 3;
		} else if (chosen_cell_X < 2 && chosen_cell_Y < 2) {
			chosen_cell = 4;
		} else if (chosen_cell_X < 3 && chosen_cell_Y < 2) {
			chosen_cell = 5;
		} else if (chosen_cell_X < 1 && chosen_cell_Y < 3) {
			chosen_cell = 6;
		} else if (chosen_cell_X < 2 && chosen_cell_Y < 3) {
			chosen_cell = 7;
		} else if (chosen_cell_X < 3 && chosen_cell_Y < 3) {
			chosen_cell = 8;
		}

		return chosen_cell;
	}

	// Function Move Checker checks if the selected cell is suitable for the next seed
	public static int Move_Checker_If_Valid(int position_X_Y) {
		if (cell[position_X_Y].getIcon() == Tic_Tac_Toe_Icons[0]) {
			return 1;
		}
		return 0;
	}

	// Player Turn Function
	public static Player Player_Turn(Player Player_turn) {

		if (Player_turn == Player.Player1) {
			Player_turn = Player.Player2;
		} else {
			Player_turn = Player.Player1;
		}

		return Player_turn;
	}

	// Set Cell Value function
	public static void Set_Cell_Value(int position_X_Y) {
		int is_Valid = 0;
		is_Valid = Move_Checker_If_Valid(position_X_Y);
		if (is_Valid == 1) {
			cell[position_X_Y].setIcon(Tic_Tac_Toe_Icons[Player_turn.ordinal() + 1]);
			Player_turn = Player_Turn(Player_turn);
		}
	}
	
	//domain junit
	// Player controller function
	public static int Player_Controller(int mode, int mouseX, int mouseY) {
		
		
		
		int position_X_Y = 0;
		if (mode == 0) {
			position_X_Y = Cell_Finder(mouseX, mouseY);
		} else if (mode == 1 && Player_turn == Player.Player2) {
			position_X_Y = Smart_Player.findBestMove(cell, 1);
		} else if (mode == 1 && Player_turn == Player.Player1) {
			position_X_Y = Cell_Finder(mouseX, mouseY);
		} else if (mode == 2) {
			if (Player_turn == Player.Player1) {
				position_X_Y = Smart_Player.findBestMove(cell, 0);
			} else {
				position_X_Y = Smart_Player.findBestMove(cell, 1);
			}
		} else if (mode == 3 && Player_turn == Player.Player2) {
			position_X_Y = Mr_Bean.Mr_Bean_Move();
		} else if (mode == 3 && Player_turn == Player.Player1) {
			position_X_Y = Cell_Finder(mouseX, mouseY);
		} else if (mode == 4) {
			if (Player_turn == Player.Player1) {
				position_X_Y = Mr_Bean.Mr_Bean_Move();
			} else {
				position_X_Y = Mr_Bean.Mr_Bean_Move();
			}
		} else if (mode == 5) {
			if (Player_turn == Player.Player1) {
				position_X_Y = Smart_Player.findBestMove(cell, 0);
			} else {
				position_X_Y = Mr_Bean.Mr_Bean_Move();
			}
		} else if (mode == 6 && Player_turn == Player.Player1) {
			position_X_Y = Smart_Player.findBestMove(cell, 0);
		} else if (mode == 6 && Player_turn == Player.Player2) {
			position_X_Y = Cell_Finder(mouseX, mouseY);
		} else if (mode == 7 && Player_turn == Player.Player1) {
			position_X_Y = Mr_Bean.Mr_Bean_Move();
		} else if (mode == 7 && Player_turn == Player.Player2) {
			position_X_Y = Cell_Finder(mouseX, mouseY);
		} else if (mode == 8) {
			if (Player_turn == Player.Player2) {
				position_X_Y = Smart_Player.findBestMove(cell, 1);
			} else {
				position_X_Y = Mr_Bean.Mr_Bean_Move();
			}
		}
		return position_X_Y;
	}

	// Get mouse X Y coordinates
	public static int[] Get_Mouse_X_Y() {
		int position[] = new int[2];
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		position[0] = (int) b.getX();
		position[1] = (int) b.getY();
		return position;
	}
	
	
	// Move mouse to X Y coordinates
	public static void Move_Mouse_X_Y(int X, int Y) throws AWTException {
		 Robot robot = new Robot();
		 robot.mouseMove(X, Y);
	}

	// Game controller function
	public static void Game_Controller(int mode, int mouseX, int mouseY) {
		
		if (mode == 0) {
			Set_Cell_Value(Player_Controller(mode, mouseX, mouseY));
		} else if (mode == 1 || mode == 3) {
			Set_Cell_Value(Player_Controller(mode, mouseX, mouseY));
			try {
				if (Player_turn == Player.Player2) {
					int current_mouse_position[] = Get_Mouse_X_Y();
					click(frame.getX() + 400, frame.getY() + 200);
					Move_Mouse_X_Y(current_mouse_position[0],current_mouse_position[1]);
				}
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (mode == 6 || mode == 7) {
			Set_Cell_Value(Player_Controller(mode, mouseX, mouseY));
			try {
				if (Player_turn == Player.Player1) {
					int current_mouse_position[] = Get_Mouse_X_Y();
					click(frame.getX() + 400, frame.getY() + 200);
					Move_Mouse_X_Y(current_mouse_position[0],current_mouse_position[1]);
				}
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (mode == 2 || mode == 4 || mode == 5 || mode == 8) {
			Set_Cell_Value(Player_Controller(mode, mouseX, mouseY));
			try {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {		
					e.printStackTrace();
				}
				int current_mouse_position[] = Get_Mouse_X_Y();
				click(frame.getX() + 400, frame.getY() + 200);
				Move_Mouse_X_Y(current_mouse_position[0],current_mouse_position[1]);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//domain junit
	// Count Blank Cells function
	public static int Count_Blank_Cells() {
		int num_of_blank = 0;
		for (int i = 0; i < 9; i++) {
			if (cell[i].getIcon() == Tic_Tac_Toe_Icons[0]) {
				num_of_blank++;
			}
		}
		return num_of_blank;
	}

	// fake mouse click function
	public static void click(int x, int y) throws AWTException {
		Robot bot = new Robot();
		bot.mouseMove(x, y);
		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	// Board_Init function initializes the Game mouse listener and creates the appropriate
	// panels
	public static void Board_Init(JButton start, JButton add, JButton select1, JButton select2, JButton done) {

		Mouse_Panel.setBounds(300, 100, 600, 700);
		Mouse_Panel.setBackground(new Color(0, 0, 0, 0));
		
		Hall_Of_Fame_Panel.setBackground(Color.cyan);
		Hall_Of_Fame_Panel.setBounds(300, 100, 600, 700);
		
		Game_Panel.setBackground(Color.orange);
		Game_Panel.setBounds(300, 100, 600, 700);
		
		// ADD GRID 3X3 LAYOUT TO GAME PANEL
		Game_Panel.setLayout(new GridLayout(3, 3));
		// initialize the grid
		Grid_initializer(Game_Panel);
		Grid_reset();

		Mouse_Panel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int mouseX = e.getX();
				int mouseY = e.getY();
				int Game_State;

				start.setEnabled(false);
				add.setEnabled(false);
				select1.setEnabled(false);
				select2.setEnabled(false);
				done.setEnabled(false);
				
				
				Game_Controller(Game_Mode, mouseX, mouseY);
				Game_State = Game_State_Checker();

				int Player1_index = Find_Player_Index(Player1_current.Player_Name);
				int Player2_index = Find_Player_Index(Player2_current.Player_Name);
				
				if (Game_State == 0 || Game_State == 1 || Game_State == 2) {						
					
					Game_Record Record = new Game_Record();
					Record.Player1_Name = Player1_current.Player_Name;
					Record.Player2_Name = Player2_current.Player_Name;
					if(Game_State == 0) {
						Record.winner=0;
					} else if (Game_State == 1){
						Record.winner=1;
					} else {
						Record.winner=2;
					}
					
					Record.Player1_Score = Player_List[Player1_index].Score_Calculate(Player_List[Player1_index]);
					Record.Player2_Score = Player_List[Player2_index].Score_Calculate(Player_List[Player2_index]);
					
					Player_List[Player1_index].Add_Game_Record(Record);
					Player_List[Player2_index].Add_Game_Record(Record);
					
					Print_Player_Stats(Player1_Panel, frame, Find_Player(Player1_current.Player_Name));
					Print_Player_Stats(Player2_Panel, frame, Find_Player(Player2_current.Player_Name));
					
					Mouse_Panel.setVisible(false);
					
					done.setEnabled(true);
					start.setEnabled(true);
					add.setEnabled(true);
					select1.setEnabled(true);
					select2.setEnabled(true);
						
				}
			}
		});

		frame.getContentPane().add(Mouse_Panel);
		frame.getContentPane().add(Game_Panel);
		frame.getContentPane().add(Hall_Of_Fame_Panel);
	}

	// hides the main game panel
	public static void Board_Hide() {
		Game_Panel.setVisible(false);
		Mouse_Panel.setVisible(false);
	}
	
	// resets the game state
	public static void Board_Reset() {
		Grid_reset();
		Game_Panel.setVisible(true);
		Mouse_Panel.setVisible(true);
	}

	public static void Hall_Of_Fame_Hide() {
		Hall_Of_Fame_Panel.setVisible(false);
	}
	
	public static void Hall_Of_Fame_Reset() {
		Hall_Of_Fame_Panel.setVisible(true);
	}
	
	public static void Players_Panel_Hide() {
		Player1_Panel.setVisible(false);
		Player2_Panel.setVisible(false);
	}
	
	public static void Players_Panel_Reset() {
		Player1_Panel.setVisible(true);
		Player2_Panel.setVisible(true);
	}
	
	//domain junit
	// find player by name
	static Player_Account Find_Player(String Player_Name) {
		for (int i = 0; i < Player_Count; i++) {
			if (Player_List[i].Player_Name == Player_Name) {
				return Player_List[i];
			}
		}
		return null;
	}
		
		//domain junit
	    // find player index by name 
		static int Find_Player_Index(String Player_Name) {
			for (int i = 0; i < Player_Count; i++) {
				if (Player_List[i].Player_Name == Player_Name) {
					return i;
				}
			}
			return -1;
		}

	// Print Player Stats
	static void Print_Player_Stats(JPanel Player_Panel, JFrame frame, Player_Account Player) {

		Font font_style = new Font("Courier New", Font.BOLD, 20);

		Player_Panel.removeAll();
		Player_Panel.setLayout(new GridLayout(14, 1));
		
			YourJLabel clsLabel = new YourJLabel();
			clsLabel.setBackground(Color.orange);
			clsLabel.setText("   Player's Name: " + Player.Player_Name);
			clsLabel.setFont(font_style);

			Player_Panel.add(clsLabel);
			clsLabel = new YourJLabel();
			clsLabel.setBackground(Color.orange);
			clsLabel.setText("   Total: " + Player.Number_Of_Games);
			clsLabel.setFont(font_style);

			Player_Panel.add(clsLabel);
			clsLabel = new YourJLabel();
			clsLabel.setBackground(Color.orange);

			if (Player.Number_Of_Games != 0) {
				clsLabel.setText("   Won: " + (Player.Number_Of_Wins* 100)/Player.Number_Of_Games  +"%" );
			} else {
				clsLabel.setText("   Won: " + 0  +"%" );				
			}
			clsLabel.setFont(font_style);
			Player_Panel.add(clsLabel);

			Player_Panel.add(clsLabel);
			clsLabel = new YourJLabel();
			clsLabel.setBackground(Color.orange);
			if (Player.Number_Of_Games != 0) {
				clsLabel.setText("   Lost: " + (Player.Number_Of_Losses * 100)/Player.Number_Of_Games +"%");
			} else {
				clsLabel.setText("   Lost: " + 0 +"%");		
			}
			clsLabel.setFont(font_style);
			Player_Panel.add(clsLabel);
		
			Player_Panel.add(clsLabel);
			clsLabel = new YourJLabel();
			clsLabel.setBackground(Color.orange);
			if (Player.Number_Of_Games != 0) {
				clsLabel.setText("   Draw: " + ((Player.Number_Of_Games - Player.Number_Of_Wins - Player.Number_Of_Losses) * 100) / Player.Number_Of_Games +"%");
			} else {
				clsLabel.setText("   Draw: " + 0 +"%");
			}
			clsLabel.setFont(font_style);
			Player_Panel.add(clsLabel);

			Player_Panel.add(clsLabel);
			clsLabel = new YourJLabel();
			clsLabel.setBackground(Color.orange);
			clsLabel.setText("   Total Score: " + Player.Score_Calculate(Player));
			clsLabel.setFont(font_style);
			Player_Panel.add(clsLabel);


			Player_Panel.add(clsLabel);
			clsLabel = new YourJLabel();
			clsLabel.setBackground(Color.orange);
			clsLabel.setText("   Best Games");
			clsLabel.setFont(font_style);
			Player_Panel.add(clsLabel);
			
			Player_Panel.add(clsLabel);
			clsLabel = new YourJLabel();
			clsLabel.setBackground(Color.orange);
			if(Player.All_Games[0] != null) {
				clsLabel.setText("   " + Player.All_Games[0].Player1_Name + " vs. " + Player.All_Games[0].Player2_Name);
			}
			clsLabel.setFont(font_style);
			Player_Panel.add(clsLabel);
			
			Player_Panel.add(clsLabel);
			clsLabel = new YourJLabel();
			clsLabel.setBackground(Color.orange);
			if(Player.All_Games[1] != null) {
				clsLabel.setText("   " + Player.All_Games[1].Player1_Name + " vs. " + Player.All_Games[1].Player2_Name);
			}
			clsLabel.setFont(font_style);
			Player_Panel.add(clsLabel);
			
			Player_Panel.add(clsLabel);
			clsLabel = new YourJLabel();
			clsLabel.setBackground(Color.orange);
			if(Player.All_Games[2] != null) {
				clsLabel.setText("   " + Player.All_Games[2].Player1_Name + " vs. " + Player.All_Games[2].Player2_Name);
			}
			clsLabel.setFont(font_style);
			Player_Panel.add(clsLabel);
			
			Player_Panel.add(clsLabel);
			clsLabel = new YourJLabel();
			clsLabel.setBackground(Color.orange);
			if(Player.All_Games[3] != null) {
				clsLabel.setText("   " + Player.All_Games[3].Player1_Name + " vs. " + Player.All_Games[3].Player2_Name);
			}
			clsLabel.setFont(font_style);
			Player_Panel.add(clsLabel);
			
			Player_Panel.add(clsLabel);
			clsLabel = new YourJLabel();
			clsLabel.setBackground(Color.orange);
			if(Player.All_Games[4] != null) {
				clsLabel.setText("   " + Player.All_Games[4].Player1_Name + " vs. " + Player.All_Games[4].Player2_Name);
			}
			clsLabel.setFont(font_style);
			Player_Panel.add(clsLabel);

	}

	// Print Hall Of Fame Stats
	static void Print_Hall_Of_Fame_Stats(JPanel Hall_Of_Fame_Panel, JFrame frame) {
		
		Player_Account[] Ranked_Players = Rank_Players(Player_List);
		if (Ranked_Players == null) {
			return;
		}
		
		Font font_style = new Font("Courier New", Font.BOLD, 20);
		Font title_style = new Font("Courier New", Font.BOLD, 30);
		
		
		Hall_Of_Fame_Panel.removeAll();
		Hall_Of_Fame_Panel.setLayout(new GridLayout(13, 1));
		
		YourJLabel titleLabel = new YourJLabel();
		titleLabel.setBackground(Color.cyan);
		titleLabel.setText("    #########################");
		titleLabel.setFont(title_style);
		Hall_Of_Fame_Panel.add(titleLabel);
		
		YourJLabel titleLabel1 = new YourJLabel();
		titleLabel1.setBackground(Color.cyan);
		titleLabel1.setText("    Hall Of Fame - Top Players  ");
		titleLabel1.setFont(title_style);
		Hall_Of_Fame_Panel.add(titleLabel1);
		
		YourJLabel titleLabel2 = new YourJLabel();
		titleLabel2.setBackground(Color.cyan);
		titleLabel2.setText("    #########################");
		titleLabel2.setFont(title_style);
		Hall_Of_Fame_Panel.add(titleLabel2);
		
		
			for (int i = 0; i < 10; i ++) {
				if (Ranked_Players[i] != null) {
					YourJLabel clsLabel = new YourJLabel();
					clsLabel.setBackground(Color.cyan);
					clsLabel.setText("    Player's Name: " +  Ranked_Players[i].Player_Name + "    Score: " + Ranked_Players[i].Score_Calculate(Ranked_Players[i]));
					clsLabel.setFont(font_style);
					Hall_Of_Fame_Panel.add(clsLabel);
				}	
			}
			

	}
	
	// Choose a Player
	static void Player_Choose_Left(JButton Player1, JFrame frame) {
		Player1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				String Players[] = Player_List_to_String_Array(Player_List);
				String Player_Chosen = (String) JOptionPane.showInputDialog(frame, "Choose Player", "Player 1 Select",
						JOptionPane.PLAIN_MESSAGE, null, Players, "ham");

				// If a string was returned, say so.
				if ((Player_Chosen != null) && (Player_Chosen.length() > 0)) {
					Player1_Ready = true;
					Player1_current = Find_Player(Player_Chosen);
					Print_Player_Stats(Player1_Panel, frame, Find_Player(Player_Chosen));
					frame.setVisible(true);
					return;
				}
			}
		});
	}

	// Choose a Player
	static void Player_Choose_Right(JButton Player2, JFrame frame) {
		Player2.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				String Players[] = Player_List_to_String_Array(Player_List);
				String Player_Chosen = (String) JOptionPane.showInputDialog(frame, "Choose Player", "Player 2 Select",
						JOptionPane.PLAIN_MESSAGE, null, Players, "ham");

				// If a string was returned, say so.
				if ((Player_Chosen != null) && (Player_Chosen.length() > 0)) {
					Player2_Ready = true;
					Player2_current = Find_Player(Player_Chosen);
					Print_Player_Stats(Player2_Panel, frame, Find_Player(Player_Chosen));
					frame.setVisible(true); 
					return;
				}
			}
		});
	}
	
	//domain junit
	// Create Bot Players
	static void Bots_Create() throws IOException, ClassNotFoundException {
		
		// create smart AI
		Player_Account bot1 = new Player_Account();
		bot1.Player_Name = "Hal";
		if (Check_If_Player_Exists(bot1) == 0) {
			Player_List[Player_Count] = new Player_Account();
			Player_List[Player_Count] = bot1;
			Write_Player_To_File(Player_List);
			Player_Count++;
		}

		
		// create Mr_Bean
		Player_Account bot2 = new Player_Account();
		bot2.Player_Name = "Mr Bean Bot";
		if (Check_If_Player_Exists(bot2) == 0) {
			Player_List[Player_Count] = bot2;
			Write_Player_To_File(Player_List);
			Player_Count++;
		}
	}

	// Create a Player
	static void Player_Create(JButton add_player, JFrame frame) {
		add_player.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				String Player_Name = (String) JOptionPane.showInputDialog(frame, "Create Player \nType the Name below");

				// If a string was returned, say so.
				if ((Player_Name != null) && (Player_Name.length() > 0) && (Player_Name.length() <= 20)) {
					System.out.println("Player " + Player_Name + " Added");
					Player_List[Player_Count] = new Player_Account();
					Player_List[Player_Count].Player_Name = Player_Name;				
					Player_Count++;
					return;
				}
			}
		});
	}

	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	// Is_Bot function checks if a player is a bot return 0 if AI, 1 if Mr Bean, 2 if Normal Player
		static int Is_Bot (Player_Account Player) {
			if(Player == null) {
				return 4;
			}
			if (Player.Player_Name.equals("Hal")) {
				return 0;
			} else if (Player.Player_Name.equals("Mr Bean Bot")) {
				return 1;
			} else {
				return 2;
			}
		}
	
	//click to initiate
		static void click_to_initiate() {
			int current_mouse_position[] = Get_Mouse_X_Y();
			try {
				click(frame.getX() + 400, frame.getY() + 200);
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Move_Mouse_X_Y(current_mouse_position[0],current_mouse_position[1]);
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
		
	// Start Game
	static void Start_Game(JButton start_game, JFrame frame) {
		start_game.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Player_turn = Player.Player1;
				if(Player1_current.Player_Name == null || Player2_current.Player_Name == null) {
					infoBox("Please Choose Both Players first", "Choose Players");
				}
				else if(Player1_current.Player_Name == Player2_current.Player_Name && Is_Bot(Player1_current) == 2 ) {
					infoBox("You chose the same Player", "Choose Players");
				}else if (Player1_Ready && Player2_Ready) {
					//Players_Panel_Reset();
					Hall_Of_Fame_Hide();
					Board_Reset();
				} else {
					infoBox("Please Choose Both Players first", "Choose Players");
				} 
				
				//Change Game Mode if AI chosen
				if (Is_Bot(Player1_current) == 2 && Is_Bot(Player2_current) == 2) {
					Game_Mode = 0;
				} else if(Is_Bot(Player1_current) == 2 && Is_Bot(Player2_current) == 0) {
					Game_Mode = 1;
				} else if(Is_Bot(Player1_current) == 0 && Is_Bot(Player2_current) == 0) {
					Game_Mode = 2;
					click_to_initiate();
				} else if(Is_Bot(Player1_current) == 2 && Is_Bot(Player2_current) == 1) {
					Game_Mode = 3;
				} else if(Is_Bot(Player1_current) == 1 && Is_Bot(Player2_current) == 1) {
					Game_Mode = 4;
					click_to_initiate();
				} else if(Is_Bot(Player1_current) == 0 && Is_Bot(Player2_current) == 1) {
					Game_Mode = 5;
					click_to_initiate();
				} else if(Is_Bot(Player1_current) == 0 && Is_Bot(Player2_current) == 2) {
					Game_Mode = 6;
					click_to_initiate();
				} else if(Is_Bot(Player1_current) == 1 && Is_Bot(Player2_current) == 2) {
					Game_Mode = 7;
					click_to_initiate();
				} else if(Is_Bot(Player1_current) == 1 && Is_Bot(Player2_current) == 0) {
					Game_Mode = 8;
					click_to_initiate();
				}
				System.out.println("Mode "+ Game_Mode );
				
			}
		});
	}

	// End Game
	static void Quit_Game(JButton quit, JFrame frame) {
		quit.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				try {
					Write_Player_To_File(Player_List);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
	}
	
	// Disable buttons when not needed
	
	static void Button_Disable(JButton start, JButton add, JButton select1, JButton select2, JButton done) {
		
		int game_state = Game_State_Checker();
		
		if (game_state == 0 || game_state == 1 || game_state == 2) {
			
			start.setEnabled(false);
			add.setEnabled(false);
			select1.setEnabled(false);
			select2.setEnabled(false);
			done.setEnabled(false);
		
		}
	};
	
	// End Current Game
	static void End_Game(JButton quit, JFrame frame) {
		quit.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//Players_Panel_Hide();
				Hall_Of_Fame_Reset();
				Board_Hide();
				Print_Hall_Of_Fame_Stats(Hall_Of_Fame_Panel, frame);
			}
		});
	}
	
	Domain_Model() throws IOException, ClassNotFoundException {
		
		try {
			Read_Players_From_File();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Board_Hide();
		Bots_Create();
		
		Player1_Panel.setBackground(Color.orange);
		Player1_Panel.setBounds(0, 100, 300, 700);

		Player2_Panel.setBackground(Color.orange);
		Player2_Panel.setBounds(900, 100, 300, 700);

		JButton quit = new JButton("Quit");
		quit.setBounds(105, 30, 80, 20);

		JButton add_player = new JButton("Add Player");
		add_player.setBounds(500, 50, 100, 20);

		JButton start_game = new JButton("Start Game");
		start_game.setBounds(600, 50, 100, 20);

		JButton select_player_left = new JButton("Select Player");
		select_player_left.setBounds(70, 70, 150, 20);

		JButton select_player_right = new JButton("Select Player");
		select_player_right.setBounds(970, 70, 150, 20);

		JButton done = new JButton("Done");
		done.setBounds(1005, 30, 80, 20);
		
		Print_Hall_Of_Fame_Stats(Hall_Of_Fame_Panel, frame);
		
		Board_Init(start_game, add_player, select_player_left, select_player_right, done);
		
		Player_Create(add_player, frame);
		Player_Choose_Left(select_player_left, frame);
		Player_Choose_Right(select_player_right, frame);
		Quit_Game(quit, frame);
		Start_Game(start_game, frame);
		End_Game(done, frame);
		
		
		
		// add buttons
		frame.add(quit);
		frame.add(add_player);
		frame.add(start_game);
		frame.add(select_player_left);
		frame.add(select_player_right);
		frame.add(done);

		// add panels
		frame.getContentPane().add(Player1_Panel);
		frame.getContentPane().add(Player2_Panel);

		//frame.setSize(1200, 800);
		frame.setSize(1200, 800);
		frame.setLayout(null);		
		frame.setVisible(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		new Domain_Model();
	}

}
