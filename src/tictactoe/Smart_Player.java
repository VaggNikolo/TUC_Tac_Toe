package tictactoe;

import javax.swing.Icon;
import javax.swing.JButton;

class Smart_Player {

	static int turn = 0;

	// if game array full return 0
	static int Check_If_full(JButton[] game_array) {
		for (int i = 0; i < 9; i++) {
			if (game_array[i].getIcon() == Domain_Model.Tic_Tac_Toe_Icons[0]) {
				return 1;
			}
		}
		return 0;
	}

	// Three cell checker
	public static int Three_Cell_Checker(Icon cell1, Icon cell2, Icon cell3) {
		if (cell1 == cell2 && cell2 == cell3 && cell1 != Domain_Model.Tic_Tac_Toe_Icons[0]) {
			if (cell1 == Domain_Model.Tic_Tac_Toe_Icons[1]) {
				return 0;
			} else if (cell1 == Domain_Model.Tic_Tac_Toe_Icons[2]) {
				return 1;
			}
		}
		return -1;
	}

	// Function Game State Checker returns the state of the game | return 0 if Player 1 Won |
	// return 1 if Player 2 Won | return 2 if Draw | return 3 if Continue |
	public static int Game_State_Checker(JButton[] cell) {
		int state;

		state = Three_Cell_Checker(cell[0].getIcon(), cell[1].getIcon(), cell[2].getIcon());
		if (state != -1) {
			return state;
		}
		state = Three_Cell_Checker(cell[3].getIcon(), cell[4].getIcon(), cell[5].getIcon());
		if (state != -1) {
			return state;
		}
		state = Three_Cell_Checker(cell[6].getIcon(), cell[7].getIcon(), cell[8].getIcon());
		if (state != -1) {
			return state;
		}

		state = Three_Cell_Checker(cell[0].getIcon(), cell[3].getIcon(), cell[6].getIcon());
		if (state != -1) {
			return state;
		}
		state = Three_Cell_Checker(cell[1].getIcon(), cell[4].getIcon(), cell[7].getIcon());
		if (state != -1) {
			return state;
		}
		state = Three_Cell_Checker(cell[2].getIcon(), cell[5].getIcon(), cell[8].getIcon());
		if (state != -1) {
			return state;
		}

		state = Three_Cell_Checker(cell[0].getIcon(), cell[4].getIcon(), cell[8].getIcon());
		if (state != -1) {
			return state;
		}
		state = Three_Cell_Checker(cell[2].getIcon(), cell[4].getIcon(), cell[6].getIcon());
		if (state != -1) {
			return state;
		}

		for (int i = 0; i < 9; i++) {
			if (cell[i].getIcon() == Domain_Model.Tic_Tac_Toe_Icons[0]) {
				return 3;
			}
		}

		return 2;
	}

	// check state of the game return 0 if x won or 1 if o won
	static int Check_Game_State(JButton[] game_array, int player) {
		int state = Game_State_Checker(game_array);
		if (player == 0) {
			if (state == 0) {
				return +1;
			} else if (state == 1) {
				return -1;
			}
		} else {
			if (state == 0) {
				return -1;
			} else if (state == 1) {
				return 1;
			}
		}

		return 0;
	}

	static int minmax(JButton[] game_array, int mode, int player) {
		int winner = Check_Game_State(game_array, player);
		int best_total;

		if (mode == 1) {
			best_total = -10;
		} else {
			best_total = 10;
		}

		if (winner == 1 || winner == -1) {
			return winner;
		}

		if (Check_If_full(game_array) == 0) {
			return 0;
		}

		if (mode == 1) {
			mode = 0;
			for (int i = 0; i < 9; i++) {
				if (game_array[i].getIcon() == Domain_Model.Tic_Tac_Toe_Icons[0]) {
					if (player == 0) {
						game_array[i].setIcon(Domain_Model.Tic_Tac_Toe_Icons[1]);
					} else {
						game_array[i].setIcon(Domain_Model.Tic_Tac_Toe_Icons[2]);
					}

					best_total = Math.max(best_total, minmax(game_array, 0, player));
					game_array[i].setIcon(Domain_Model.Tic_Tac_Toe_Icons[0]);
				}
			}
			return best_total;
		} else {
			mode = 0;
			for (int i = 0; i < 9; i++) {
				if (game_array[i].getIcon() == Domain_Model.Tic_Tac_Toe_Icons[0]) {
					if (player == 0) {
						game_array[i].setIcon(Domain_Model.Tic_Tac_Toe_Icons[2]);
					} else {
						game_array[i].setIcon(Domain_Model.Tic_Tac_Toe_Icons[1]);
					}

					best_total = Math.min(best_total, minmax(game_array, 1, player));
					game_array[i].setIcon(Domain_Model.Tic_Tac_Toe_Icons[0]);
				}
			}
			return best_total;
		}
	}

	public static class Move {
		int row, col;
	};

	static int findBestMove(JButton[] game_array, int player) {
		int bestVal = -10;
		int best_move = -1;

		for (int i = 0; i < 9; i++) {
			if (game_array[i].getIcon() == Domain_Model.Tic_Tac_Toe_Icons[0]) {

				if (player == 0) {
					game_array[i].setIcon(Domain_Model.Tic_Tac_Toe_Icons[1]);
				} else {
					game_array[i].setIcon(Domain_Model.Tic_Tac_Toe_Icons[2]);
				}

				int current_total = minmax(game_array, 0, player);

				game_array[i].setIcon(Domain_Model.Tic_Tac_Toe_Icons[0]);
				if (current_total > bestVal) {
					best_move = i;
					bestVal = current_total;
				}
			}

		}

		return best_move;
	}

}
