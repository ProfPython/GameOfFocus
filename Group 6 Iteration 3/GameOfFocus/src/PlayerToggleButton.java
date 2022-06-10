import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

/***
 * PlayerToggleButton is a custom JButton class which adds some functionality which helps with certain logic
 * handling once a button has been pressed.
 * 
 */

public class PlayerToggleButton extends JButton {

	boolean isComputer;
	int playerNumber;
	
	public PlayerToggleButton(int playerNum, Color col) {
		
		setText("Human Player");
		setPreferredSize(new Dimension(150, 50));
		setFont(new Font("Lucida Console", Font.BOLD, 15 ));
		
		playerNumber = playerNum;
	}
	
	public int getPlayerNum() {
		return playerNumber;
	}
	
	public int getisComputer() {
		return playerNumber;
	}
	
	public void setPlayer(boolean opt) {
		isComputer = opt;
	}
		
}
