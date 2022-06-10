/**
 * This class handles the AI functionality implemented in the game.
 * Contains the methods for implementing the easy and hard AI.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AIController implements java.io.Serializable {

	private ArrayList<Stack> stackList;
	private String difficulty;
	private Player currPlayer;
	private Stack[][] gameBoard;
	private LegalController legal;

	public AIController(String diff, Board board) {

		difficulty = diff;
		currPlayer = null;
		gameBoard = board.getBoard();
		stackList = new ArrayList<Stack>();
		legal = new LegalController(board);

	}

	// init stuff
	public void setCurrPlayer(Player player) {
		currPlayer = player;
	}

	public Stack[] doMove(Player player) {
		if (difficulty.equals("easy")) {
			return easyMove(player);
		}

		else if (difficulty.equals("hard")) {
			return hardMove(player);
		}

		return null;
	}

	public Stack[] easyMove(Player player) {

		// init stackList

		stackList.clear();

		Stack[] easyMove = new Stack[2];
		ArrayList<Stack> moves = new ArrayList<Stack>();

		currPlayer = player;
		currentState();

		easyMove[0] = pickRandomMove(stackList);
		moves = legal.setLegalMoves(easyMove[0]);
		easyMove[1] = pickRandomMove(moves);

		return easyMove;

	}

	private Stack[] hardMove(Player player) {

		// new move array | similar to the currMove array in moveController
		Stack[] hardMove = new Stack[2];

		// set the current player
		currPlayer = player;

		// the map which holds all the legals moves for an available piece
		// key is the stackID
		// value is the ArrayList of legal moves for that stack
		Map<Integer, ArrayList<Stack>> movesMap = new HashMap<Integer, ArrayList<Stack>>();

		// the map which organizes destination stack
		// key is the stackID
		// value is the ArrayList of legal moves for that stack

		Map<Integer, ArrayList<Stack>> maxMap = new HashMap<Integer, ArrayList<Stack>>();

		// init the maxMoves map
		for (int i = 0; i <= 5; i++) {
			maxMap.put(i, new ArrayList<Stack>());
		}

		// if the player has a reserve, do that move
		if (currPlayer.getReserve() > 0) {

			hardMove[0] = new Stack(100, 100, currPlayer.getColor());

			for (int i = 0; i < gameBoard.length; i++) {

				for (int j = 0; j < gameBoard.length; j++) {
					Stack stack = gameBoard[i][j];
					if (!stack.isActive()) {
						continue;
					} else {
						maxMap.get(stack.getStackHeight()).add(stack);
					}
				}
			}

			Integer maxMoveSize = 0;

			for (int i = 5; i >= 0; i--) {
				if (maxMap.get(i).size() == 0) {
					continue;
				}

				else {
					maxMoveSize = i;
					break;
				}
			}

			hardMove[1] = pickRandomMove(maxMap.get(maxMoveSize));

			return hardMove;

		}

		else {
			currentState();

			if (stackList.size() == 0) {
				return hardMove;
			}

			movesMap = createMovesMap();

			for (int i = 0; i <= 5; i++) {
				maxMap.put(i, new ArrayList<Stack>());
			}

			for (Map.Entry<Integer, ArrayList<Stack>> entry : movesMap.entrySet()) {
				Stack stack = getStackByID(entry.getKey());
				ArrayList<Stack> stackMoves = entry.getValue();
				Integer key = getMaxMove(stackMoves);
				maxMap.get(key).add(stack);
			}

			Integer maxMoveSize = 0;

			for (int i = 5; i >= 0; i--) {
				if (maxMap.get(i).size() == 0) {
					continue;
				}

				else {
					maxMoveSize = i;
					break;
				}
			}

			hardMove[0] = pickRandomMove(maxMap.get(maxMoveSize));

			ArrayList<Stack> secondStackMoves = movesMap.get(hardMove[0].getStackID());

			for (int i = 0; i < secondStackMoves.size(); i++) {
				Stack stack = secondStackMoves.get(i);
				if (stack.getStackHeight() == maxMoveSize) {
					hardMove[1] = stack;
					break;
				}
			}

			return hardMove;
		}
	}

	public Integer getMaxMove(ArrayList<Stack> moves) {

		Integer max = 0;

		for (int i = 0; i < moves.size(); i++) {
			if (moves.get(i).getStackHeight() > max) {
				max = moves.get(i).getStackHeight();
			}
		}
		return max;
	}

	public void endCompMove() {
		currPlayer = null;
		stackList.clear();
	}

	public Stack getStackByID(Integer id) {

		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard.length; j++) {
				Stack stack = gameBoard[i][j];

				if (stack.getStackID() == id) {
					return stack;
				}
			}
		}

		return null;
	}

	private Map<Integer, ArrayList<Stack>> createMovesMap() {

		Map<Integer, ArrayList<Stack>> movesMap = new HashMap<Integer, ArrayList<Stack>>();

		for (int i = 0; i < stackList.size(); i++) {
			Stack stack = stackList.get(i);
			ArrayList<Stack> legalMoves = new ArrayList<Stack>();
			legalMoves = legal.setLegalMoves(stack);
			movesMap.put(stackList.get(i).getStackID(), legalMoves);
		}

		return movesMap;
	}

	// get the current state of pieces on the board of the player color
	private void currentState() {

		stackList.clear();

		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard.length; j++) {

				Stack stack = gameBoard[i][j];

				if ((stack.isEmpty()) || (!stack.isActive())) {
					continue;
				}

				if ((stack.topColor().equals(currPlayer.getColor())) && (stack.getStackHeight() != 0)
						&& (!stack.topColor().equals(null))) {
					System.out.println("Adding to current state: \n");
					System.out.println(stack.toString());
					stackList.add(stack);
				}
			}
		}

		System.out.println("The length of the stack list: " + stackList.size());
	}

	// pick a random stack from the stacks list
	public Stack pickRandomMove(ArrayList<Stack> listOfStacks) {

		int rand = (int) Math.floor(Math.random() * listOfStacks.size());
		return listOfStacks.get(rand);

	}

	public ArrayList<Stack> firstMove(ArrayList<Stack> stackList) {

		return null;

	}

}
