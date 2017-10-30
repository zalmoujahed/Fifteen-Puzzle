import java.util.*;

public class Tuple {

	private String pieces;
	private Stack<String> moves = new Stack<String>();
	
	public Tuple(String p ,Stack<String> m){
		
		pieces = p;
		moves = m;
		
	}
	public String getPieces(){
		return pieces;
	}
	public Stack<String> getMoves(){
		return moves;
	}
	public boolean emptyMoves(){
		if(moves == null ){
			return true;
		}
		else 
			return false;
	}
}
