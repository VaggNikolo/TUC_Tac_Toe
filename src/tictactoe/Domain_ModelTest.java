package tictactoe;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tictactoe.Domain_Model.Player;

class Domain_ModelTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testCheck_If_Player_Exists() {
		Player_Account Vagg = new Player_Account();
		Vagg.Player_Name = "Vagg";
		
		try {
			assertEquals(0,Domain_Model.Check_If_Player_Exists(Vagg));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	void testGet_Player() {
		if(Domain_Model.Player_turn==Player.Player1) {
			assertEquals(0,Domain_Model.Get_Player());
		}
		else assertEquals(1,Domain_Model.Get_Player());
	}

	@Test
	void testCell_Finder() {
		//cell 0 test (0,0)
		int horizontal0 = 0;
		int vertical0 = 0;
		assertEquals(0,Domain_Model.Cell_Finder(horizontal0, vertical0));
		//cell 1 test (1,0)
		int horizontal1 = 200;
		int vertical1 = 0;
		assertEquals(1,Domain_Model.Cell_Finder(horizontal1, vertical1));
		//cell 2 test (2,0)
		int horizontal2 = 400;
		int vertical2 = 0;
		assertEquals(2,Domain_Model.Cell_Finder(horizontal2, vertical2));
		//cell 3 test (0,1)
		int horizontal3 = 0;
		int vertical3 = 220;
		assertEquals(3,Domain_Model.Cell_Finder(horizontal3, vertical3));
		//cell 4 test (1,1)
		int horizontal4 = 200;
		int vertical4 = 220;
		assertEquals(4,Domain_Model.Cell_Finder(horizontal4, vertical4));
		//cell 5 test (2,1)
		int horizontal5 = 400;
		int vertical5 = 220;
		assertEquals(5,Domain_Model.Cell_Finder(horizontal5, vertical5));
		//cell 6 test (0,2)
		int horizontal6 = 0;
		int vertical6 = 440;
		assertEquals(6,Domain_Model.Cell_Finder(horizontal6, vertical6));
		//cell 7 test (1,2)
		int horizontal7 = 200;
		int vertical7 = 440;
		assertEquals(7,Domain_Model.Cell_Finder(horizontal7, vertical7));
		//cell 8 test (2,2)
		int horizontal8 = 400;
		int vertical8 = 440;
		assertEquals(8,Domain_Model.Cell_Finder(horizontal8, vertical8));

	}

	@Test
	void testFind_Player() {
		Player_Account Vagg = new Player_Account();
		Vagg.Player_Name = "Vagg";
		assertEquals(null,Domain_Model.Find_Player(Vagg.Player_Name));
	}
}
