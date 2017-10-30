import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class MenuBar extends JMenuBar{

	public MenuBar(){
		
		Menu fileMenu = new Menu("File");	
		fileMenu.setSize(35, 45);
		this.add(fileMenu);
				
		MenuItem about = new MenuItem("About");
		fileMenu.add(about);
		about.addActionListener(
		         new ActionListener() {  
		             public void actionPerformed( ActionEvent event )
		            {
		               JOptionPane.showMessageDialog( null ,
		                  "Author: Zaynab Almoujahed\n"
		                  + "Project 2: Fifteen Puzzle\n"
		                  + "CS 342, Fall 2017\n"
		                  + "University of IL at Chicago\n",
		                  "About", JOptionPane.PLAIN_MESSAGE );
		            }
		         }); 
		
		MenuItem exit = new MenuItem("Exit");
		fileMenu.add(exit);
		exit.addActionListener(
		         new ActionListener() {  
		        	public void actionPerformed( ActionEvent event )
		            {
		               System.exit( 0 );
		            }
		         });	
		
		MenuItem help = new MenuItem("Help");
		fileMenu.add(help);
		help.addActionListener(
		         new ActionListener() {  
		             public void actionPerformed( ActionEvent event )
		            {
		               JOptionPane.showMessageDialog( null ,
		                  "To start the game, go to the 'Actions' menu and click start.\n"
		                  + "There you will also find the buttons to undo a move, undo all moves, shuffle the board, and solve the game",
		                  "About", JOptionPane.PLAIN_MESSAGE );
		            }
		         });
	}	
}
//________________________________________//
class Menu extends JMenu{
	
	public Menu(String text){
		super(text);
				
	}	
}
//________________________________________//
class MenuItem extends JMenuItem{
	
	public MenuItem(String text){
		super(text);
	}	
}