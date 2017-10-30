import javax.swing.JButton;


public class Button extends JButton
{
	
  private int num;
  
//________________________________________//
  public Button ( String text ){
    super (text);
    if(text != "")
    	num = Integer.parseInt(text);
    else
    	num = -1;
  }
//________________________________________//
  public int getNum(){
	  return num;
  }
}