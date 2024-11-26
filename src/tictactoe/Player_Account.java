package tictactoe;

import java.io.Serializable;
import java.sql.Timestamp;

public class Player_Account implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String Player_Name;
	int Number_Of_Games = 0;
	int Number_Of_Losses = 0;
	int Number_Of_Wins = 0;
	Game_Record All_Games[] = new Game_Record[10000];
	
	public int Score_Calculate(Player_Account Player) {
		int Number_Of_Draws =  Player.Number_Of_Games - Player.Number_Of_Wins - Player.Number_Of_Losses;
		if (Player.Number_Of_Games == 0) {
			return 0;
		}
		return 50 * (2 * Player.Number_Of_Wins + Number_Of_Draws) / Player.Number_Of_Games;
	}
	
	public Game_Record[] Get_5_Best_Games(Player_Account Player) {
		return Player.All_Games;
	}
	
	public static int get_time() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return (int) timestamp.getTime();
	}
	
	public void Add_Game_Record(Game_Record Record) {
		Record.Time_Stamp = get_time();
		All_Games[Number_Of_Games] = Record;
		Number_Of_Games++;
		if (Player_Name == Record.Player1_Name && Record.winner == 0) {
			Number_Of_Wins++;
		} else if (Player_Name == Record.Player2_Name && Record.winner == 1) {
			Number_Of_Wins++;
		} else if (Record.winner != 2){
			Number_Of_Losses++;
		}
	}
	
}
