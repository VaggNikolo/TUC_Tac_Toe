package tictactoe;

import java.util.Random;

import javax.swing.JButton;

class Mr_Bean {

	public static class Move {
		JButton selected;
		int index;
	};

	// getRandom function chooses a random cell from the board
	static Move getRandom(JButton[] array) {
		Move move = new Move();
		move.index = new Random().nextInt(array.length - 1);
		move.selected = array[move.index];
		return move;
	}

	// Mr_Bean_Move function uses get random in order to choose a random cell
	static int Mr_Bean_Move() {
		JButton cell[] = new JButton[10];
		Move current_move = new Move();

		cell = Domain_Model.Get_Board();
		current_move = getRandom(cell);
		
		while (current_move.selected.getIcon() != Domain_Model.Tic_Tac_Toe_Icons[0]) {
			current_move = getRandom(cell);
		}

		return current_move.index;
	}
}
