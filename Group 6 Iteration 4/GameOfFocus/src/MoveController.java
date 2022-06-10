
/*** 
 * 
 * 	- Contains all of the move functionality
 * 	- Handles capturing opponents pieces and add to reserve
 * 
 */

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class MoveController implements java.io.Serializable {

	private Board board;
	private Stack[] currMove;
	private Player[] player;
	private Player currPlayer;
	private boolean reserveAction;
	private boolean completeMove;
	private boolean endOfTurn;
	private ArrayList<Stack> currLegalMoves;
	private AIController ai;
	private LegalController legal;
	private AudioController audio;

	private Border grayBorder = BorderFactory.createLineBorder(new Color(162, 176, 174), 4);

	public MoveController(Board board, Player[] players, String diff, AudioController audio) {
		this.audio = audio;
		currLegalMoves = new ArrayList<Stack>();
		currMove = new Stack[2];
		reserveAction = false;
		player = players;
		currPlayer = null;
		completeMove = false;
		this.board = board;
		legal = new LegalController(this.board);
		ai = new AIController(diff, board);

	}

	public void setAudio(AudioController audio) {
		this.audio = audio;
	}

	public void setCurrPlayer(Player player) {
		currPlayer = player;
	}

	public void moveReserve() {

		if (currMove[0] != null) {
			currMove[0].setBorder(null);
			currMove[0] = null;
		}
		if (currMove[1] != null) {
			currMove[1].setBorder(null);
			currMove[1] = null;
		}

		board.clearInfoPanels("full");

		if ((currMove[0] == null) && (currPlayer.getReserve() > 0)) {
			Stack stack = new Stack(1000, 1000, currPlayer.getColor()); // creates a 'ghost' stack
			toMove(stack); // sets as the piece to move
			currMove[0].setBorder(BorderFactory.createLineBorder(currPlayer.getColor(), 5));
			currMove[0].setHighlight();

			reserveAction = true; // sets the move status as a reserve action
			JPanel topPanel = board.getTopInfoPanel(); // updates the gui
			board.drawStackInfo(stack, topPanel);
			board.setMessage("<html>Select a destination</html>");
		}
	}

	public void dontMoveReserve(int counter) {

		if ((currMove[0] != null)) {
			reserveAction = false; // sets the move status as a reserve action
			board.clearInfoPanels("full");
			currMove[0] = null;
			if (currMove[1] != null) {
				currMove[1].setBorder(null);
				currMove[1] = null;
			}
			board.setMessage("<html>You just cancelled moving a reserve. Select a piece to move.</html>");
		}

	}

	public boolean isCompTurn() {

		if (currPlayer.isComputer()) {
			return true;
		}

		return false;
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
	public void confirmMove(boolean flag) throws InterruptedException {

		if (flag) {

			Stack ogStack = currMove[0];
			Stack newStack = currMove[1];

			// if the move is a reserve move
			if (reserveAction) {
				// deal with the color arrays of reserve stack and destination stack
				ArrayList<Color> reserveArray = new ArrayList<Color>();
				reserveArray.add(currMove[0].getStackOrder().get(0));

				newStack.getStackOrder().stream().forEach(n -> reserveArray.add(n));

				if (reserveArray.size() > 5) {
					ArrayList<Color> maxStack = capAndReserve(reserveArray);
					newStack.setStackOrder(maxStack);
				}

				else {
					newStack.setStackOrder(reserveArray);
				}
				newStack.setBackground(newStack.getStackOrder().get(0));

				// update height
				newStack.updateHeight();
				String newStackHeight = String.valueOf(newStack.getStackHeight());
				newStack.setText(newStackHeight);
				currPlayer.decrementReserve();
				reserveAction = false;
				endOfTurn = true;
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

			resetMove();
		}

	}

	public Stack[] getCurrMove() {
		return currMove;
	}

	public void resetMove() {

		currLegalMoves.forEach(n -> {
			n.setPreview(false);
			n.setBorder(null);
			n.repaint();
		});
		currLegalMoves.removeAll(currLegalMoves);

		board.clearInfoPanels("full");
		currMove[0].removeHighlight();
		currMove[1].removeHighlight();
		currMove[0].setBorder(null);
		currMove[1].setBorder(null);
		currMove[0] = null;
		currMove[1] = null;
		completeMove = false;
	}

	// Gets the move distance
	public int getDistance(int[] ogCoords, int[] newCoords) {
		int distance = Math.abs((ogCoords[0] - newCoords[0])) + Math.abs((ogCoords[1] - newCoords[1]));
		return distance;
	}

	// Handles predicting potential capture pieces
	public int predictCapture() {
		int distance = getDistance(currMove[0].getCoords(), currMove[1].getCoords());
		int captureNum = 0;
		int totalHeight = distance + currMove[1].getStackHeight();
		if (totalHeight > 5) {
			for (int i = (5 - distance); i < currMove[1].getStackHeight(); i++) {
				if (currPlayer.getColor() != currMove[1].getStackOrder().get(i)) {
					captureNum++;
				}
			}
		}
		return captureNum;
	}

	public int predictReserve() {
		int distance = getDistance(currMove[0].getCoords(), currMove[1].getCoords());
		int reserveNum = 0;
		int totalHeight = distance + currMove[1].getStackHeight();
		if (totalHeight > 5) {
			for (int i = (5 - distance); i < currMove[1].getStackHeight(); i++) {
				if (totalHeight > 5 && currPlayer.getColor() == currMove[1].getStackOrder().get(i)) {
					reserveNum++;
				}
			}
		}
		return reserveNum;
	}

	public int predictReserveMoveReserveNumber() {
		int reserveNum = 0;
		if (currMove[1].getStackHeight() == 5) {
			if (currPlayer.getColor() == currMove[1].getStackOrder().get(4)) {
				reserveNum++;
			}
		}
		return reserveNum;

	}

	public int predictReserveMoveCaptureNumber() {
		int captureNum = 0;
		if (currMove[1].getStackHeight() == 5) {
			if (currPlayer.getColor() != currMove[1].getStackOrder().get(4)) {
				captureNum++;
			}
		}
		return captureNum;

	}

	public void captureAndReservePrediction() {
		if (predictCapture() > 0 && predictReserve() == 0) {
			board.setMessage("<html>You can capture " + predictCapture()
					+ " piece(s) with this move. Cancel or confirm move.</html>");
		} else if (predictReserve() > 0 && predictCapture() == 0) {
			board.setMessage("<html>You can put " + predictReserve()
					+ " piece(s) in your reserve pile with this move. Cancel or confirm move.</html>");
		} else if (predictCapture() > 0 && predictReserve() > 0) {
			board.setMessage("<html>You can capture " + predictCapture() + " piece(s) and put" + predictReserve()
					+ " piece(s) in your reserve pile with this move. Cancel or confirm move.</html>");
		} else {
			board.setMessage("<html>Confirm or cancel your move</html>");
		}
	}

	public void captureAndReservePredictionForReserve() {
		if (predictReserveMoveCaptureNumber() > 0 && predictReserveMoveReserveNumber() == 0) {
			board.setMessage("<html>You can capture " + predictReserveMoveCaptureNumber()
					+ " piece(s) with this move. Cancel or confirm move.</html>");
		} else if (predictReserveMoveReserveNumber() > 0 && predictReserveMoveCaptureNumber() == 0) {
			board.setMessage("<html>You can put " + predictReserveMoveReserveNumber()
					+ " piece(s) in your reserve pile with this move. Cancel or confirm move.</html>");
		} else {
			board.setMessage("<html>Confirm or cancel your move</html>");
		}
	}

	// Handles the sequences of clicks to complete a move action.
	public void moveOrder(Stack stack) {

		// handles the canceling of a move if the initial Stack was clicked again
		if ((currMove[0] == stack)) {
			try {
				audio.playSoundEffect(3);
			} catch (IOException | LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currLegalMoves.forEach(n -> {
				n.setPreview(false);
				n.setBorder(null);
				n.repaint();
			});

			currLegalMoves.clear();

			// pass message to display
			board.setMessage(
					"<html><p style=\"text-align:center;\">You cancelled the entire move. <br><br>Select a piece to move.</html>");
			// set borders to null
			currMove[0].removeHighlight();
			currMove[0].setBorder(null);
			if (currMove[1] != null) {

				currLegalMoves.forEach(n -> {
					n.setPreview(true);
					n.setBorder(grayBorder);
					n.repaint();
				});

				currMove[1].removeHighlight();
				currMove[1].setBorder(null);
			}
			// set current move stack to null
			currMove[0] = null;
			currMove[1] = null;

			// clear the current legal moves array

			// repainting the GUI
			board.clearInfoPanels("full");
			stack.setBorder(null);

			completeMove = false;

		}

		// handles the canceling of only the second half of the move.
		else if (currMove[1] == stack) {

			currMove[1].setBorder(grayBorder);
			currMove[1].removeHighlight();

			currMove[1] = null;
			completeMove = false;

			// repaint the GUI
			board.getBottomInfoPanel().removeAll();
			board.getBottomInfoPanel().repaint();

			board.setMessage("<html>Select a destination</html>");
		}

		// if no button has been clicked yet
		else if ((currMove[0] == null) && (currMove[1] == null)) {

			// illegal selection of empty stack
			if (stack.getStackOrder().size() == 0) {
				try {
					audio.playSoundEffect(3);
				} catch (IOException | LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currMove[0] = null;
				board.setMessage("<html>Invalid selection!</html>");
			}

			// illegal player color
			else if (!(stack.getStackOrder().get(0) == (currPlayer.getColor()))) {
				try {
					audio.playSoundEffect(3);
				} catch (IOException | LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currMove[0] = null;
				board.setMessage("<html>Wrong colour chosen!</html>");
			}
			// put in first index of toMove array.
			else {

				toMove(stack);

				currLegalMoves.clear();
				currLegalMoves = legal.setLegalMoves(stack);

				currLegalMoves.forEach(n -> {
					n.setPreview(true);
					n.setBorder(grayBorder);
					n.repaint();
				});

				currMove[0].setBorder(BorderFactory.createLineBorder(currPlayer.getColor(), 5));
				currMove[0].setHighlight();
				System.out.println("Legal Length: " + currLegalMoves.size());
				board.setMessage("<html>Select a destination</html>");

			}
		}

		// deals with the second click
		else if ((currMove[0] != null) && (currMove[1] == null)) {

			if (reserveAction) {
				moveDest(stack);
				currMove[1].setBorder(BorderFactory.createLineBorder(currPlayer.getColor(), 5));
				currMove[1].setHighlight();
				captureAndReservePredictionForReserve();
			}

			// checks if legal move

			else {

				boolean isLegal = false;

				// checking to see if move is in the current legal moves array
				for (int i = 0; i < currLegalMoves.size(); i++) {
					if (currLegalMoves.get(i).equals(stack)) {
						isLegal = true;
						break;
					}
				}

				// if legal, add the move array
				if (isLegal) {
					moveDest(stack);
					currMove[1].setBorder((BorderFactory.createLineBorder(currPlayer.getColor(), 5)));
					currMove[1].setHighlight();
					captureAndReservePrediction();
					completeMove = true;
				}

				// not legal therefore null
				else {
					currMove[1] = null;
					board.getBottomInfoPanel().removeAll();
					board.getBottomInfoPanel().repaint();
					board.setMessage("<html>Invalid move!</html>");
				}
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

	public void doCompMove(Player player) throws InterruptedException {

		currMove = ai.doMove(currPlayer);

		// handling when an ai can't make a move
		if (currMove[0] == null) {
			confirmMove(false);
		} else {

			if ((currMove[0].getCoords()[0] == 100) && (currMove[0].getCoords()[1] == 100)) {
				reserveAction = true;
			}

			confirmMove(true);
		}

	}

	// Creates an array of legal moves after the first piece is selected.

	// checks to see if there is a piece to move and destination
	public boolean isEndOfTurn() {
		return endOfTurn;
	}

	public boolean isComplete() {
		return completeMove;
	}

	// Method for dealing with captures and reserves
	private ArrayList<Color> capAndReserve(ArrayList<Color> stack) {

		int reserves = 0;
		int captures = 0;
		ArrayList<Color> overflow = new ArrayList<>(); // for the bottom pieces
		ArrayList<Color> maxStack = new ArrayList<>(); // the top five pieces

		// populates the overflow array
		for (int i = 5; i < stack.size(); i++) {
			overflow.add(stack.get(i));
		}

		// Separates the captured and reserve pieces
		for (int i = 0; i < overflow.size(); i++) {
			if (overflow.get(i) == currPlayer.getColor()) {
				reserves++;
				currPlayer.incrementReserve(); // if same color increment reserve
			} else {
				for (int j = 0; j < player.length; j++) { // iterates through players to see if matches
					if (overflow.get(i) == player[j].getColor()) {
						player[j].decrementTotalPieces();
						currPlayer.incrementCaptured();
						captures++;
					}
				}
			}
		}

		for (int i = 0; i < 5; i++) {
			maxStack.add(stack.get(i));
		}
		try {
			audio.playSoundEffect(2);
		} catch (IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return maxStack;
	}

	public void initMove() {
		// TODO Auto-generated method stub
		currLegalMoves.clear();

	}
}