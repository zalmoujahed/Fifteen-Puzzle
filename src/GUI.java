
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

import javax.swing.*;

public class GUI extends JFrame implements ActionListener{
	
	public ArrayList<Button> buttons;
	private Button blankButton;
	private String names[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
	private Container container;
	private GridLayout grid;	
	private JLabel stats;
	private JPanel panel;
	private MenuBar bar;
	private Stack<String> moveStack = new Stack<String>();
	private boolean gameHasBegun = false;
	private int moveCount = 0;
	private int complexity = 0;
	
	// Constructor
	public GUI(){
		
		super("Fifteen Puzzle");
		this.setLocation(500, 200);
		
		setMenu();
		setGridLayout();
		
		printGrid();
		
	}
	//_________________________________________________________________________________//
	//Sets up the grid and buttons 
	public void setGridLayout(){
		
		// Set up the layout
		stats = new JLabel("Moves: " + moveCount + "          Complexity: " + complexity);
		
		grid = new GridLayout(4,4,5,5);
				
		container = getContentPane();
	    container.setLayout( new BorderLayout() );
	    
	    panel = new JPanel(grid, false);
	    
	    // Create all the buttons and add them to the 'buttons' array list
	    buttons = new ArrayList<Button>();
	    
		for ( int i = 0; i < names.length; i++ ) {
	         Button b = new Button( names[ i ] );
	         buttons.add(b);
	         buttons.get(i).addActionListener( this );
	         panel.add( buttons.get(i) );
	      }
		
		// Create a blank button
		blankButton = new Button("");
		buttons.add(blankButton);
		panel.add(buttons.get(15));
		blankButton.setEnabled(false);
		
		container.add(stats, BorderLayout.SOUTH);
		container.add(panel, BorderLayout.CENTER);
		
		setSize( 800, 800 );
	    setVisible( true );

	}
	//_________________________________________________________________________________//
	// Sets up the menu bar and its actions
	public void setMenu(){
		
		// Set up the menu bar
		bar = new MenuBar();		// Calling this makes a 'file' menu that has 'about' and 'exit'
		setJMenuBar(bar);
		
		
		JButton start = new JButton("Start");
		bar.add(start);
		start.addActionListener(
				new ActionListener() {  
		             public void actionPerformed( ActionEvent event ){
		            	 startGame();
		            	 }
		             }
				); 
		JButton undo = new JButton("Undo");
		bar.add(undo);
		undo.addActionListener(
				new ActionListener() {  
		             public void actionPerformed( ActionEvent event ){
		            	 undoMove();
		            }
		         }  
		      ); 
		JButton undoA = new JButton("Undo All");
		bar.add(undoA);
		undoA.addActionListener(
				new ActionListener() {  
		             public void actionPerformed( ActionEvent event ){
		            	 undoAll();
		            }
		         }  
		      );
		JButton shuffle = new JButton("Shuffle");
		bar.add(shuffle);
		shuffle.addActionListener(
				new ActionListener() {  
		             public void actionPerformed( ActionEvent event ){
		            	 shuffle();
		             }
		         }  
		      );
		JButton solve = new JButton("Solve");
		bar.add(solve);
		solve.addActionListener(
				new ActionListener() {  
		             public void actionPerformed( ActionEvent event ){
		            	 solve();
		            }
		         }  
		      );	
	}	
	//_________________________________________________________________________________//
	// Event handler to make the moves
	public void actionPerformed(ActionEvent event) {
		
		JButton button = (JButton)event.getSource();
		int buttonPos = -1;
		// find which button the command came from
		for(Button b : buttons) {
	         if ( button.getActionCommand() == b.getText() ){
	        	  buttonPos = buttons.indexOf(b);
	         } 	    
		}
		// Make a move if the game has started
		if (gameHasBegun)
	      makeMove(buttonPos);
	      

	}
	//_________________________________________________________________________________//
		// If the game has not begun yet, set it up to begin
		public void startGame(){
			if( !gameHasBegun){
	     	    shuffle();
	     	    printGrid();
	     	    moveStack.push(buttonsToString(buttons));
	     	   gameHasBegun = true;
	    	}
		}
	//_________________________________________________________________________________//
	// Performs a swap if the button clicked is adjacent to the blank button
	public void makeMove(int curPos ){
		if(!gameHasBegun)
			return;
		int blankPos = findBlank(buttons);
		if(checkAdjacent(blankPos, curPos)){
			Collections.swap(buttons, curPos, findBlank(buttons));
			moveStack.push(buttonsToString(buttons));
			moveCount++;
			printGrid();
		}
	}
	//_________________________________________________________________________________//
	// Undo a move by popping the move stack
	public void undoMove(){
		// if the move we are trying to do is a duplicate, remove it from the stack
		if(!moveStack.empty() && buttonsToString(buttons).equals(moveStack.peek()) )
			moveStack.pop();
		
		// Pop the prev move off of the stack and set the buttons to reflect that
		if(gameHasBegun && !moveStack.empty()){
   		 	String moves = moveStack.pop();
   		 	stringToButtons(moves);
   		 	moveCount--;
   		 	printGrid();
   	 	}
		
	}	
	//_________________________________________________________________________________//
	// Undo all moves
	public void undoAll() {
		
		Timer time = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
				undoMove();
				printGrid();
				
				// If we are done undoing moves, stop the timer
				if (moveStack.empty()){
					time.cancel();
				}
				// If we have just emptied the entire stack, push the current board as the initional pos
				if(gameHasBegun && moveStack.empty())
					moveStack.push(buttonsToString(buttons));
			}
		};
		
		time.scheduleAtFixedRate(task, 500, 500);
	}
	//_________________________________________________________________________________//
	// Returns true if cur button pos is adjacent to blank pos
	public boolean checkAdjacent(int blankPos,int curPos){
		
		
		if(((curPos+1 == blankPos || curPos-1 == blankPos)&& sameRow(curPos, blankPos) )||
		   ((curPos+4 == blankPos || curPos-4 == blankPos)&& sameColumn(curPos, blankPos))){
			return true;
		}		
		return false;
	}
	
	//_________________________________________________________________________________//
	// Shuffles the buttons and checks if its solvable. If not, re-shuffle
	public void shuffle(){
		if(!gameHasBegun)
			return;
		
		boolean solvable = false;
		
		while(!solvable ){
			Collections.shuffle(buttons);
			int inversionCnt = inversionCount();
			int blankRow = findBlank(buttons)/4;
			
		    // Solvable if inversion count is even and blank row is odd 
			// 		or inversion count is odd and blank row is even
			
			if(inversionCnt % 2 == 0 && blankRow % 2 == 1 ||
			   inversionCnt % 2 == 1 && blankRow % 2 == 0	){ 			// Solvable
				solvable = true;
			}
		}
		// Push the new grid onto the stack of moves and reset moveCount
		moveStack.removeAllElements();
		moveStack.push(buttonsToString(buttons));
		moveCount = 0;
		
		printGrid();
		
		
	}
	//_________________________________________________________________________________//
	// Calculate inversion count of current board
	public int inversionCount(){
		complexity = 0;
		ArrayList<Button> temp = new ArrayList<Button>();
		//remove blank space
		int pos = findBlank(buttons);
		Button blank = buttons.remove(pos);
		
		temp = (ArrayList<Button>) buttons.clone();
		
		for(Button b : buttons){
			temp.remove(0);
			for(Button t : temp){
				if(b.getNum() > t.getNum()){
					complexity++;
				}
			}	
		}
		// Put blank space back
		 buttons.add(pos, blank);
		
		return complexity;
	}
	//_________________________________________________________________________________//
	// Solve the puzzle by calling the bfs function
	public void solve(){
		if(!gameHasBegun)
			return;
		
		Tuple result = bfs();
		if(result != null){			
			Collections.reverse(result.getMoves());
			makeSolvedMoves(result.getMoves());
		}
	}
	//_________________________________________________________________________________//
	// prints the solution with animation
	public void makeSolvedMoves(Stack<String> solution){
		
		Timer time = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
				
				// if the move we are trying to do is a duplicate, remove it from the stack
				if(!solution.empty() && buttonsToString(buttons).equals(solution.peek()) )
					solution.pop();
				
				// Pop the  move off of the stack and set the buttons to reflect that
				if(gameHasBegun && !solution.empty()){
		   		 	String moves = solution.pop();
		   		    stringToButtons(moves);
		   		 	//ArrayList<Button> btns = makeButtons(moves);
		   		 	moveCount++;
		   		 	printSolvedGrid(buttons);
		   	 	}
				
				// If we are done making moves, stop the timer
				if (solution.empty()){
					time.cancel();
				}
				// If we have just emptied the entire stack, push the current board as the initional pos
				if(gameHasBegun && solution.empty()){
					moveStack.removeAllElements();
					moveStack.push(buttonsToString(buttons));
				}
			}
		};
		
		time.scheduleAtFixedRate(task, 500, 500);
		
	}
	//_________________________________________________________________________________//
	public void printSolvedGrid(ArrayList<Button> btns){
		panel.removeAll();
		
		for(Button b: btns){
			if(b.getText() == ""){
				b.setEnabled(false);
			}
			panel.add(b);
		}
		panel.revalidate();
		panel.repaint();
		stats.setText("Moves: " + moveCount + "          Complexity: " + complexity);
		if (checkForWin(btns) && gameHasBegun){
			JOptionPane.showMessageDialog( this ,
	                   "Congrats!! You pressed the solve the button and solved the game! It took " + moveCount +" moves.",
	                  "Game Solved!", JOptionPane.PLAIN_MESSAGE );
			resetButtons();
			gameHasBegun = false;
		}
	}
	//_________________________________________________________________________________//
	public void resetButtons(){
		
		buttons.removeAll(buttons);
		
		for ( int i = 0; i < names.length; i++ ) {
	         Button b = new Button( names[ i ] );
	         buttons.add(b);
	         buttons.get(i).addActionListener( this );
	         panel.add( buttons.get(i) );
	      }
		
		// Create a blank button
		blankButton = new Button("");
		buttons.add(blankButton);
		panel.add(buttons.get(15));
		blankButton.setEnabled(false);
	}
	//_________________________________________________________________________________//
	// Use breadth first search to find a path to the solution
	public Tuple bfs(){
		Set<String> set = new HashSet<String>();
		Queue<Tuple> queue = new LinkedList<Tuple>();
		
		String p = buttonsToString(buttons);		
		queue.add(new Tuple(p, null));
		set.add(p);
		
		while(!queue.isEmpty()){
			
			Tuple cur = queue.remove();
			ArrayList<Button> tempButtons = makeButtons(cur.getPieces());
			if(checkForWin(tempButtons)){
				return cur;
			}
			ArrayList<String> neighbors = getNeighbors(tempButtons);
			for(String n : neighbors){
				Stack<String> MovementList;
				
				if(!set.contains(n)){
					
					if(cur.emptyMoves()){
						MovementList = new Stack<String>();
					}
					else{
						MovementList = (Stack<String>)(cur.getMoves()).clone();
					}					
					
					MovementList.add(n);
					queue.add(new Tuple(n, MovementList));
					set.add(n);
				}
			}
		}
		return null;
	}
	//_________________________________________________________________________________//
	// finds the possible places to move to
	public ArrayList<String> getNeighbors(ArrayList<Button> btns){
		
		ArrayList<String> neighbors = new ArrayList<String>();
		int blankPos = findBlank(btns);
		
		if (blankPos+4 < 16 && checkAdjacent(blankPos, blankPos+4)){
			ArrayList<Button> clone = (ArrayList<Button>) btns.clone();
			Collections.swap(clone, blankPos, blankPos+4);
			neighbors.add(buttonsToString(clone));
		}
		if (blankPos+1 < 16 && checkAdjacent(blankPos, blankPos+1)){
			ArrayList<Button> clone = (ArrayList<Button>) btns.clone();
			Collections.swap(clone, blankPos, blankPos+1);
			neighbors.add(buttonsToString(clone));
		}
		if (blankPos-4 > -1 && checkAdjacent(blankPos, blankPos-4)){
			ArrayList<Button> clone = (ArrayList<Button>) btns.clone();
			Collections.swap(clone, blankPos, blankPos-4);
			neighbors.add(buttonsToString(clone));
		}
		if (blankPos-1 > -1 && checkAdjacent(blankPos, blankPos-1)){
			ArrayList<Button> clone = (ArrayList<Button>) btns.clone();
			Collections.swap(clone, blankPos, blankPos-1);
			neighbors.add(buttonsToString(clone));
		}
		
		return neighbors;
	}
	//_________________________________________________________________________________//
	public boolean checkForWin(ArrayList<Button> btns){
		int num = 1;
		for(Button b : btns){
			if(b.getNum() == -1 && num != 16)
				return false;
		
			else if (b.getNum() == -1 && num == 16)
				return true;
			
			else if(b.getNum() != num)
				return false;
			num++;
			
		}
		
		return true;
	}
	//_________________________________________________________________________________//
	// Finds the blank button in the grid and returns its position
	public int findBlank(ArrayList<Button> btns){
		for(Button btn : btns) {
	         if ( btn.getText() == "" ){
	        	  return btns.indexOf(btn); 
	         } 
		}
		return -1;
	}
	//_________________________________________________________________________________//
	public boolean sameRow(int pos1, int pos2){
		if(pos1/4 == pos2/4)
			return true;
		else 
			return false;
	}
	//_________________________________________________________________________________//
	public boolean sameColumn(int pos1, int pos2){
		if(pos1%4 == pos2%4)
			return true;
		else 
			return false;
	}
	//_________________________________________________________________________________//
	// Converts the current button positions into a string to easily store the moves to undo later
	public String buttonsToString(ArrayList<Button> btns){
		String result = "";
		for(Button btn : btns) {			
			if (btn.getNum() == -1)
				result += "s";
			else
				result += Integer.toHexString(btn.getNum());
		}
		
		return result;
	}
	//_________________________________________________________________________________//
	// Converts a string back to buttons to be displayed as the new board
	public void stringToButtons(String s){
		
		Button b;
		buttons.removeAll(buttons);
		
		char moves[] = s.toCharArray();
		for (char c : moves)
		switch (c){
			case 'a':
				 b = new Button("10");
				 b.addActionListener( this );
		        buttons.add(b);
				break;
			case 'b':
				b = new Button("11");
				b.addActionListener( this );
		        buttons.add(b);
				break;
			case 'c':
				b = new Button("12");
				b.addActionListener( this );
		        buttons.add(b);
				break;
			case 'd':
				b = new Button("13");
				b.addActionListener( this );
		        buttons.add(b);
				break;
			case 'e':
				b = new Button("14");
				b.addActionListener( this );
		        buttons.add(b);
				break;
			case 'f':
				b = new Button("15");
				b.addActionListener( this );
		        buttons.add(b);
				break;
			case 's':
				b = new Button("");
				b.setEnabled(false);
		        buttons.add(b);
				break;
			default:
				b = new Button("" + c);
				b.addActionListener( this );
		        buttons.add(b);
		}
		
	}
	//_________________________________________________________________________________//
		// Makes and returns an array list of buttons from a string
		public ArrayList<Button> makeButtons (String s){
			
			ArrayList<Button> btns = new ArrayList<Button>();
			Button b;
			
			char moves[] = s.toCharArray();
			for (char c : moves)
			switch (c){
				case 'a':
					b = new Button("10");
					btns.add(b);
					break;
				case 'b':
					b = new Button("11");
					btns.add(b);
					break;
				case 'c':
					b = new Button("12");
					btns.add(b);
					break;
				case 'd':
					b = new Button("13");
					btns.add(b);
					break;
				case 'e':
					b = new Button("14");
					btns.add(b);
					break;
				case 'f':
					b = new Button("15");
					btns.add(b);
					break;
				case 's':
					b = new Button("");
					btns.add(b);
					break;
				default:
					b = new Button("" + c);
					btns.add(b);
			}
			
			return btns;
		}	
	//_________________________________________________________________________________//
	// Re-paints the grid 
	public void printGrid(){
		panel.removeAll();
		for(Button b: buttons){
			panel.add(b);
		}
		panel.revalidate();
		panel.repaint();
		stats.setText("Moves: " + moveCount + "          Complexity: " + complexity);
		if (checkForWin(buttons) && gameHasBegun){
			JOptionPane.showMessageDialog( this ,
	                   "Congrats!! You have succeeded in putting all the pieces back in their place in "+ moveCount + " moves!\n"
	                   		+ "The complexity level of this puzzle was " + complexity + ".",
	                  "You Won!", JOptionPane.PLAIN_MESSAGE );
			gameHasBegun = false;
		}

	}
	//_________________________________________________________________________________//
}
