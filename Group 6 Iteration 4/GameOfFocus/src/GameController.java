
/***
 * 
 * 	Game Controller - the brain of the game. 
 * 
 * Creates Player objects
 * This class handles the events of the game, such as 
 * whose turn it is and if a player has won the game.
 * 
 */

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JLabel;

public class GameController implements java.io.Serializable {

	private String difficulty;
	private GameGUI gui;
	private boolean[] isComputer;
	private Board board;
	private Player[] player;
	private Player currPlayer;
	private JLabel[][] labels;
	private JLabel activePlayerLabel;
	private MoveController move;

	private AudioController audio;

	public GameController(boolean[] cpuArr, String diff, Board board, JLabel[][] labels, JLabel activePlayer,
			AudioController audio) throws InterruptedException {

		difficulty = diff; // to be used for AI logic.
		isComputer = cpuArr;
		this.audio = audio;

		player = new Player[4];
		initPlayers();
		currPlayer = player[0];

//		completeMove = false;

		activePlayerLabel = activePlayer;

		this.board = board;
		this.labels = labels;

		move = new MoveController(this.board, player, difficulty, audio);
		move.setCurrPlayer(currPlayer);

	}

	public void setAudio(AudioController audio) {
		this.audio = audio;
		move.setAudio(audio);
	}

	// SET THE CONTROLLERS

	public void setMoveController(MoveController move) {
		this.move = move;
	}

	// GUI METHODS

	public void setActivePlayerLabel(JLabel label) {
		activePlayerLabel = label;
	}

	public void setLabels(JLabel[][] labels) {
		this.labels = labels;
	}

	// INIT METHODS

	private void initPlayers() {

		for (int i = 0; i < isComputer.length; i++) {
			player[i] = new Player(i, isComputer[i]);
		}
	}

	public void playFX(int i) {
		try {
			audio.playSoundEffect(i);
		} catch (IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// GETTERS

	// gets the entire players array
	public Player[] getPlayer() {
		return player;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public boolean[] getIsComputer() {
		return isComputer;
	}

	public Player getCurrPlayer() {
		return currPlayer;
	}

	public boolean isCompTurn() {

		if (currPlayer.isComputer()) {
			return true;
		}

		return false;
	}

	public boolean checkWin() {

		if (currPlayer.getCapturedPieces() >= 10) {
			return true;
		}

		else {

			Stack[][] gameBoard = board.getBoard();

			for (int i = 0; i < gameBoard.length; i++) {

				for (int j = 0; j < gameBoard.length; j++) {

					Stack stack = gameBoard[i][j];

					if ((stack.getStackOrder().size() == 0)) {
						continue;
					} else if ((stack.getStackOrder().get(0).equals(currPlayer.getColor()) || (!stack.isActive()))) {
						continue;
					}

					else {
						return false;
					}
				}
			}

			return true;
		}

	}

	public boolean currentState() {
		ArrayList<Stack> stackList = new ArrayList<Stack>();
		for (int i = 0; i < board.getBoard().length; i++) {
			for (int j = 0; j < board.getBoard().length; j++) {

				Stack stack = board.getBoard()[i][j];

				if ((stack.isEmpty()) || (!stack.isActive())) {
					continue;
				}

				if ((stack.topColor().equals(currPlayer.getColor())) && (stack.getStackHeight() != 0)
						&& (!stack.topColor().equals(null))) {

					stackList.add(stack);
				}
			}
		}

		if (stackList.size() == 0) {
			System.out.println("PASSING TURN");
			return true;
		}

		System.out.println("The length of the stack list: " + stackList.size());
		return false;
	}

	// Players next turn.

	public void nextTurn() throws InterruptedException {
		int activePlayer = (currPlayer.getPlayerNum() + 1) % 4;
		currPlayer = player[activePlayer];
		activePlayerLabel.setText("Active Player: Player " + (activePlayer + 1));
		activePlayerLabel.setForeground(currPlayer.getColor());
		move.setCurrPlayer(currPlayer);

		// Update the labels
		for (int i = 0; i < player.length; i++) {
			labels[0][i].setText("Reserve: " + player[i].getReserve());
			labels[1][i].setText("Captured: " + player[i].getCapturedPieces());
			labels[2][i].setText("Total Pieces: " + player[i].getTotalPieces());
		}
		board.clearInfoPanels("full");
		move.initMove();
		Stack[][] gameBoard = board.getBoard();
		if (currPlayer.isComputer()) {
			for (int x = 0; x < gameBoard.length; x++) {
				for (int y = 0; y < gameBoard[x].length; y++) {
					if (!gameBoard[x][y].isActive()) {
						continue;
					} else {
						gameBoard[x][y].setEnabled(false);
					}
				}
			}
		} else {
			for (int x = 0; x < gameBoard.length; x++) {
				for (int y = 0; y < gameBoard[x].length; y++) {
					if (!gameBoard[x][y].isActive()) {
						continue;
					} else {
						gameBoard[x][y].setEnabled(true);
					}
				}
			}
		}
	}

	public MoveController getMoveController() {
		return move;
	}

}