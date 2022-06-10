
/***
 * 
 * 	Game Controller - the brain of the game. 
 * 
 * 	- Contains all of the move functionality
 * 	- Creates Player objects
 * 	- Handles legal actions
 * 	- Handles capturing opponents pieces and add to reserve
 * 
 */

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameController {

	private String difficulty;
	private boolean[] isComputer;
	Board board;
	Stack[][] boardGrid;
	Stack[] currMove;
	Player[] player;
	Player currPlayer;
	boolean reserveAction;
	boolean completeMove;

	public GameController(boolean[] cpuArr, String diff) {
		difficulty = diff; // to be used for AI logic.
		isComputer = cpuArr;
		currMove = new Stack[2];
		reserveAction = false;
		player = new Player[4];
		initPlayers();
		currPlayer = player[0];
		completeMove = false;
	}

	// Set Up

	public void setBoardClass(Board game) {
		board = game;
	}

	public void setBoard(Stack[][] game) {
		boardGrid = game;
	}

	private void initPlayers() {

		for (int i = 0; i < isComputer.length; i++) {
			player[i] = new Player(i, isComputer[i]);
		}
	}

	public Player getCurrPlayer() {
		return currPlayer;
	}

	// MOVE METHODS

	// Will add the first clicked Stack button to the move array.

	public void toMove(Stack Stack) {
		currMove[0] = Stack;
	}

	// Will add the first clicked Stack button to the move array.
	public void moveDest(Stack Stack) {
		currMove[1] = Stack;
		completeMove = true;
	}

	// When a user confirms the move, we will update the two pieces to reflect the
	// move.
	public void confirmMove(Stack ogStack, Stack newStack, JLabel activePlayer, JLabel[][] labels) {

		// if the move is a reserve move
		if (reserveAction) {
			// deal with the color arrays of reserve stack and destination stack
			ArrayList<Color> reserveArray = new ArrayList<Color>();
			reserveArray.add(currMove[0].getStackOrder().get(0));
			newStack.getStackOrder().stream().forEach(n -> reserveArray.add(n));
			newStack.setStackOrder(reserveArray);
			newStack.setBackground(newStack.getStackOrder().get(0));

			// update height
			newStack.updateHeight();
			String newStackHeight = String.valueOf(newStack.getStackHeight());
			newStack.setText(newStackHeight);
			currPlayer.decrementReserve();
			reserveAction = false;
		}

		// if not a reserve move
		else {

			ArrayList<Color> origStack = ogStack.getStackOrder();
			int ogHeight = ogStack.getStackHeight();
			int distance = getDistance(ogStack.getCoords(), newStack.getCoords());

			if (ogHeight == distance) {
				fullMove(ogStack, newStack, origStack);
			}

			else if (distance < ogHeight) {
				cutMove(ogStack, newStack, origStack, distance);
			}

			if (!reserveAction) {
				// Update the heights
				newStack.updateHeight();
				ogStack.updateHeight();

				// UPDATE THE LABELS OF THE BUTTONS
				String newOGHeight = String.valueOf(ogStack.getStackHeight());
				String newStackHeight = String.valueOf(newStack.getStackHeight());
				ogStack.setText(newOGHeight);
				newStack.setText(newStackHeight);

			}

		}

		currMove[0] = null;
		currMove[1] = null;
		completeMove = false;
		nextTurn(activePlayer, labels);

	}

	public Stack[] getCurrMove() {
		return currMove;
	}

	// Gets the move distance
	public int getDistance(int[] ogCoords, int[] newCoords) {
		int distance = Math.abs((ogCoords[0] - newCoords[0])) + Math.abs((ogCoords[1] - newCoords[1]));
		return distance;
	}

	// Handles the sequences of clicks to complete a move action.
	public void moveOrder(Stack stack) {

		// handles the canceling of a move if the initial Stack was clicked again
		if ((currMove[0] == stack)) {
			currMove[0] = null;
			currMove[1] = null;
			board.getTopInfoPanel().removeAll();
			board.getBottomInfoPanel().removeAll();
			board.getTopInfoPanel().repaint();
			board.getBottomInfoPanel().repaint();
		}

		// handles the canceling of only the second half of the move.
		else if (currMove[1] == stack) {
			currMove[1] = null;
			completeMove = false;
			board.getBottomInfoPanel().removeAll();
			board.getBottomInfoPanel().repaint();
		}

		// if no button has been clicked yet
		else if ((currMove[0] == null) && (currMove[1] == null)) {

			// illegal selection of empty stack
			if (stack.getStackOrder().size() == 0) {
				currMove[0] = null;
			}

			// illegal player color
			else if (!(stack.getStackOrder().get(0) == (currPlayer.getColor()))) {
				currMove[0] = null;
			}
			// put in first index of toMove array.
			else {
				toMove(stack);
			}
		}

		// deals with the second click
		else if ((currMove[0] != null) && (currMove[1] == null)) {

			if (reserveAction) {
				moveDest(stack);
			}

			// checks if legal move
			else if (isLegal(currMove[0], stack)) {
				moveDest(stack);
			}

			// not legal therefore null
			else {
				currMove[1] = null;
				board.getBottomInfoPanel().removeAll();
				board.getBottomInfoPanel().repaint();
			}
		}
	}

	// a move where distance equals stack height.
	private void fullMove(Stack ogStack, Stack newStack, ArrayList<Color> origStack) {
		newStack.getStackOrder().stream().forEach(n -> origStack.add(n));

		ArrayList<Color> updateStack = (ArrayList<Color>) origStack.clone();
		if (updateStack.size() > 5) {
			ArrayList<Color> maxStack = capAndReserve(updateStack);
			newStack.setStackOrder(maxStack);
		}

		else {
			newStack.setStackOrder(updateStack);
		}

		// Set the button background
		newStack.setBackground(newStack.getStackOrder().get(0));
		ogStack.getStackOrder().clear(); // clear the moved stack to empty
		ogStack.setBackground(null); // color it gray
	}

	// Handles the move where the distance moved is less than stack height
	private void cutMove(Stack ogStack, Stack newStack, ArrayList<Color> origStack, int distance) {

		// check to see if empty piece
		if (origStack.size() > 0) {

			ArrayList<Color> cutTop = new ArrayList<>();
			ArrayList<Color> cutBottom = new ArrayList<>();
			for (int i = 0; i < distance; i++) {
				cutTop.add(origStack.get(i));
			}

			if (newStack.getStackHeight() > 0) {
				newStack.getStackOrder().stream().forEach(n -> cutTop.add(n));
			}

			if (cutTop.size() > 5) {
				ArrayList<Color> maxStack = capAndReserve(cutTop);
				newStack.setStackOrder(maxStack);
			}

			else {
				newStack.setStackOrder(cutTop);
			}

			newStack.setBackground(ogStack.getStackOrder().get(0));

			for (int i = distance; i < origStack.size(); i++) {
				cutBottom.add(origStack.get(i));
			}

			ogStack.setStackOrder(cutBottom);
			ogStack.setBackground(ogStack.getStackOrder().get(0));
		}

	}

	// Checks to see if a move is legal. Will be used mainly for the computer
	// players to ensure they play within the board space.
	public boolean isLegal(Stack ogStack, Stack newStack) {

		int[] ogCoors = ogStack.getCoords();
		int[] newCoors = ogStack.getCoords();
		int height = ogStack.getStackHeight();
		int distance = Math.abs((newCoors[0] - ogCoors[0])) + Math.abs((newCoors[1] - ogCoors[1]));

		if (distance > height) {
			return false;
		}

		if ((newCoors[0] < 0 || newCoors[0] > boardGrid.length)
				|| (newCoors[1] < 0 || newCoors[1] > boardGrid.length)) {
			return false;
		}

		//// STILL NEED TO ACCOUNT FOR THE CORNER PIECES FOR AI MODE

		return true;
	}

	// checks to see if there is a piece to move and destination
	public boolean getCompleteMove() {
		return completeMove;
	}

	// Checks if height is higher than max
	private boolean heightCheck(Stack newStack) {
		if (newStack.getStackHeight() > 5) {
			return true;
		}

		return false;
	}

	// Method for dealing with captures and reserves
	private ArrayList<Color> capAndReserve(ArrayList<Color> stack) {

		ArrayList<Color> overflow = new ArrayList<>(); // for the bottom pieces
		ArrayList<Color> maxStack = new ArrayList<>(); // the top five pieces

		// populates the overflow array
		for (int i = 5; i < stack.size(); i++) {
			overflow.add(stack.get(i));
		}

		// Separates the captured and reserve pieces
		for (int i = 0; i < overflow.size(); i++) {
			if (overflow.get(i) == currPlayer.getColor()) {
				currPlayer.incrementReserve(); // if same color increment reserve
			} else {
				for (int j = 0; j < player.length; j++) { // iterates through players to see if matches
					if (overflow.get(i) == player[j].getColor()) {
						player[j].decrementTotalPieces();
						currPlayer.incrementCaptured();
					}
				}
			}
		}

		for (int i = 0; i < 5; i++) {
			maxStack.add(stack.get(i));
		}

		return maxStack;
	}

	// Players next turn.

	private void nextTurn(JLabel activePlayerLabel, JLabel[][] labels) {
		int activePlayer = (currPlayer.getPlayerNum() + 1) % 4;
		currPlayer = player[activePlayer];
		activePlayerLabel.setText("Active Player: Player " + (activePlayer + 1));
		activePlayerLabel.setForeground(currPlayer.getColor());
		for (int i = 0; i < player.length; i++) {
			labels[0][i].setText("Reserves: " + player[i].getReserve());
			labels[1][i].setText("Captured: " + player[i].getCapturedPieces());
			labels[2][i].setText("Total Pieces: " + player[i].getTotalPieces());

		}
	}

	// Move reserve piece

	public void moveReserve() {

		if ((currMove[0] == null) && (currPlayer.getReserve() > 0)) {
			Stack stack = new Stack(1000, 1000, currPlayer.getColor()); // creates a 'ghost' stack
			toMove(stack); // sets as the piece to move
			reserveAction = true; // sets the move status as a reserve action
			JPanel topPanel = board.getTopInfoPanel(); // updates the gui
			board.drawStackInfo(stack, topPanel);

		}

	}

}