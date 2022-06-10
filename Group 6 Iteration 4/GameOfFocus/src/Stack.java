
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
 * 	It holds what defines a stack.
 * 
 * 
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class Stack extends JButton {
	private int[] coords;
	private Color colour;
	private Boolean stack;
	private boolean active;
	private boolean isHighlight;
	private int stackHeight;
	private ArrayList<Color> stackOrder;
	private Integer stackID;
	private boolean preview;

	public Stack(int xCoord, int yCoord, Color color) {

		super();
		stackHeight = 1;
		stack = false;
		active = true;
		coords = new int[] { xCoord, yCoord };
		this.colour = color;
		String buttonLabel = String.valueOf(stackHeight);
		stackOrder = new ArrayList<Color>();
		stackOrder.add(color);
		isHighlight = false;
		preview = false;

		this.setBackground(color);
		this.setPreferredSize(new Dimension(10, 10));
		// this.setBorderPainted(false);
		this.setOpaque(true);
		this.setText(buttonLabel);
		this.setBorder(new EmptyBorder(35, 10, 35, 10));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.getBackground().equals(Color.LIGHT_GRAY) || this.getBackground() == Color.LIGHT_GRAY) {
			this.setBackground(Color.LIGHT_GRAY);
			return;
		} else {
			this.setBackground(Color.WHITE);
			Dimension d = getSize();
			if (getStackHeight() == 0) {
				g.setColor(null);
			} else {
				g.setColor(getStackOrder().get(0));
			}
			g.fillOval(16, 7, 65, 65);
			g.setColor(Color.BLACK);
			g.drawString(String.valueOf(stackHeight), 46, 46);

		}
	}

	public boolean getPreview() {
		return preview;
	}

	public void setPreview(boolean bool) {
		preview = bool;
	}

	public boolean isEmpty() {
		if (stackOrder.size() == 0) {
			return true;
		}
		return false;
	}

	public Color getColour() {
		return colour;
	}

	public Color topColor() {

		if (stackOrder.size() > 0) {
			return stackOrder.get(0);
		}

		return null;
	}

	public void setColor(Color col) {
		colour = col;
	}

	public int getStackHeight() {
		return stackHeight;
	}

	public Integer getStackID() {
		return stackID;
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

	public void setStackID(Integer i) {
		stackID = i;
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

	public boolean isActive() {
		return active;
	}

	public void deactivateStack() {
		active = false;
	}

	public boolean isHighlight() {
		return isHighlight;
	}

	public void setHighlight() {
		isHighlight = true;
	}

	public void removeHighlight() {
		isHighlight = false;
	}

	@Override
	public String toString() {

		return "Stack height: " + stackHeight + "\nStack Coords: " + coords[0] + " " + coords[1] + "\n"
				+ "Stack Color: " + this.colour + "\n";
	}

}