import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

public class fifteenPuzzleTest {

	
	String names[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", ""};
	
	
	// Test the checkAdjacent function
	@Test
	public void test1() {
		
		GUI game = new GUI(); 
		assert(!game.checkAdjacent(0, 0));
		assert(!game.checkAdjacent(3, 4));
		assert(!game.checkAdjacent(0, 8));
		assert(!game.checkAdjacent(0, 15));
		assert(!game.checkAdjacent(11, 12));
		assert(game.checkAdjacent(2, 6));
		assert(game.checkAdjacent(11, 15));
		assert(game.checkAdjacent(1, 2));
		assert(game.checkAdjacent(9, 8));	
	}
	
	//Test the 'checkForWin' function
	@Test
	public void test2(){
		
		GUI game = new GUI(); 
		game.startGame();
		ArrayList<Button> btns = new ArrayList<Button>();
		
		for(String s : names){
			btns.add(new Button(s));
		}
		assert(game.checkForWin(btns));
		Collections.shuffle(btns);
		assert(!game.checkForWin(btns));
		
	}
	
	// Check to make sure the 'findBlankButton' works for an ordered list
	@Test 
	public void test3(){
		
		GUI game = new GUI(); 
		game.startGame();
		ArrayList<Button> btns = new ArrayList<Button>();
		
		for(String s : names){
			btns.add(new Button(s));
		}
		
		int pos = game.findBlank(btns);
		
		assertEquals(pos, 15); 
		
	}
	
	
	// Check to make sure the 'findBlankButton' works for a shuffled list
	@Test
	public void test4(){
		
		GUI game = new GUI(); 
		ArrayList<Button> btns = new ArrayList<Button>();
		
		for(String s : names){
			btns.add(new Button(s));
		}
		
	}
	
	// Checks if 'findBlank' returns the correct position of the blank button
	@Test
	public void test5(){
		
		GUI game = new GUI(); 
		ArrayList<Button> btns = new ArrayList<Button>();
		for(String s : names){
			btns.add(new Button(s));
		}
		
		int pos = game.findBlank(btns);	
		assert(btns.get(pos).getText() == "");
		
		Collections.shuffle(btns);
		
		pos = game.findBlank(btns);		
		
		assert(btns.get(pos).getText() == "");
		
	}
	
	// Test the inversion count for a shuffled list
	@Test
	public void test6(){
		
		GUI game = new GUI(); 
		game.startGame();			
		game.shuffle();
		
		int inversion = game.inversionCount();			
		assert(inversion != 0);
		
	}
	
	// test the buttonsToString function
	@Test
	public void test7(){
		
		GUI game = new GUI(); 
		ArrayList<Button> btns = new ArrayList<Button>();
		for(String s : names){
			btns.add(new Button(s));
		}
		
		String board = game.buttonsToString(btns);	
		assertEquals(board ,"123456789abcdefs");
		
		Collections.shuffle(btns);
		board = game.buttonsToString(btns);
		
		assert(board != "123456789abcdefs");
		
	}
	
	// Checking the reset function with inversion count
	@Test
	public void test8(){
	
		GUI game = new GUI(); 
		game.resetButtons();
		int i = game.inversionCount();
		assertEquals(0, i);
		
	}
	
	// Checking make move
	@Test
	public void test9(){
		
		GUI game = new GUI(); 
		game.startGame();
		game.resetButtons();
		
		game.makeMove(11);
		game.makeMove(10);
		
		int pos = game.findBlank(game.buttons);
		
		assertEquals(pos, 10);
		
	}
	
	// test undo
	@Test
	public void test10(){
		
		GUI game = new GUI(); 
		game.startGame();
		String before = game.buttonsToString(game.buttons);
		int blank = game.findBlank(game.buttons);
		game.makeMove(11);
		game.undoMove();
		
		String after = game.buttonsToString(game.buttons);
		
		assert(before!=  after);	
		
	}
	
	//test undoAll
	@Test
	public void test11(){
		
		GUI game = new GUI(); 
		game.startGame();
		String before = game.buttonsToString(game.buttons);
		int blank = game.findBlank(game.buttons);
		game.makeMove(11);
		game.makeMove(10);
		game.makeMove(9);
		game.makeMove(8);
		game.undoAll();
		
		String after = game.buttonsToString(game.buttons);
		assert(before!=  after);
				
	}
	
	//test bfs
	@Test
	public void test12(){
		GUI game = new GUI(); 
		game.startGame();
		
		game.makeMove(11);
		game.makeMove(10);
		
		Tuple result = game.bfs();
		assert(result.getMoves().size() == 2);
		
	}
	
	//Test that a path is found correctly
	@Test
	public void test13(){
		GUI game = new GUI(); 
		game.startGame();
		
		game.makeMove(11);
		game.makeMove(10);
		game.makeMove(11);
		
		Tuple result = game.bfs();
		assert(result.getMoves().size() == 1);
	}
	
	//test reset with button placement
	@Test
	public void test14(){
		GUI game = new GUI(); 
		game.startGame();
		
		int before = game.findBlank(game.buttons);
		game.makeMove(11);
		game.makeMove(10);
		game.makeMove(9);
		
		game.resetButtons();
		int after = game.findBlank(game.buttons);
		assertEquals(before, after);
	}
	
	// Check that neighbors only returns 2 possible moves when the blank is in the corner
	@Test
	public void test15(){
		GUI game = new GUI(); 
		game.startGame();
		ArrayList<String> n = game.getNeighbors(game.buttons);
		
		assertEquals(n.size(), 2);
		
		
	}
}
