
/***
 * 
 * 
 * 	IMPORTANT:
 * 
 * 
 * 	We decided to model a "Piece" as being a Stack with a height of 1. The concept of a Piece is only defined in the rulebook.
 * 	It made it much easier to build when we decided to implement a Piece as simply a Stack with height of 1.
 * 
 * 
 *  Stack - a class which extends JButton. Represents both the Piece and Stack concept.
 * 	
 * 
 * 
 */

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JButton;

public class Stack extends JButton {
	private int[] coords;
	private Color colour;
	private Boolean stack;
	private int stackHeight;
	private ArrayList<Color> stackOrder;

	public Stack(int xCoord, int yCoord, Color color) {

		super();
		stackHeight = 1;
		stack = false;
		coords = new int[] { xCoord, yCoord };
		this.colour = color;
		String buttonLabel = String.valueOf(stackHeight);
		stackOrder = new ArrayList<Color>();
		stackOrder.add(color);

		this.setBackground(color);
		this.setPreferredSize(new Dimension(10, 10));
		this.setBorderPainted(false);
		this.setOpaque(true);
		this.setText(buttonLabel);
	}

	public Color getColour() {
		return colour;
	}

	public void setColor(Color col) {
		colour = col;
	}

	public int getStackHeight() {
		return stackHeight;
	}

	public void setStackHeight(int num) {
		stackHeight = num;
	}

	public boolean isStack() {
		return stack;
	}

	public ArrayList<Color> getStackOrder() {
		return stackOrder;
	}

	public void setStackOrder(ArrayList<Color> newOrder) {
		stackOrder = newOrder;
	}

	public int[] getCoords() {
		return coords;
	}

	public void updateHeight() {
		stackHeight = stackOrder.size();
	}

	public void clearStackOrder() {
		stackOrder.clear();
	}

}
