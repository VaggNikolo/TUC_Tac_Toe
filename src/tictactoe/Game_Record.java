package tictactoe;

import java.io.Serializable;

public class Game_Record implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String Player1_Name;
	String Player2_Name;
	int winner;
	int Player1_Score=0;
	int Player2_Score=0;
	int Time_Stamp = 0;
	
}
