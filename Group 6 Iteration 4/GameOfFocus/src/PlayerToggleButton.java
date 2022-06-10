/***
 * PlayerToggleButton is a custom JButton class which adds some functionality which helps with certain logic
 * handling like determining a computer or human player.
 * 
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class PlayerToggleButton extends JButton {

	boolean isComputer;
	int playerNumber;
	
	public PlayerToggleButton(int playerNum) {
		
		setText("Human Player");
		setPreferredSize(new Dimension(150, 50));
		setFont(new Font("Lucida Console", Font.BOLD, 15 ));
		setForeground(Color.white);
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
