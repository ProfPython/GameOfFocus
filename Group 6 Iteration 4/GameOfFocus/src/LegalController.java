
/****
 * 
 * This class deals with generating legal moves.
 * 
 * 
 */


import java.util.ArrayList;

public class LegalController implements java.io.Serializable {

	private int[][] legalActions;
	private Board board;

	public LegalController(Board board) {

		this.board = board;
		legalActions = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
	}

	public ArrayList<Stack> setLegalMoves(Stack stack) {

		ArrayList<Stack> currLegalMoves = new ArrayList<Stack>();

		int height = stack.getStackHeight();
		System.out.println("Looking at legal moves: \n " + stack.toString());

		for (int i = 0; i < 4; i++) {
			int[] coords = new int[2];
			System.arraycopy(stack.getCoords(), 0, coords, 0, 2);
			for (int j = 0; j < height; j++) {

				int newX = 0;
				int newY = 0;
				coords[0] += legalActions[i][0];
				coords[1] += legalActions[i][1];

				newX = coords[0];
				newY = coords[1];

				if ((newX < 0) || (newX >= board.getBoard().length) || (newY < 0)
						|| (newY >= board.getBoard().length)) {
					System.out.println("Illegal out of bounds");
					continue;
				}

				else if (!(board.getBoard()[newX][newY].isActive())) {
					System.out.println("Illegal inactive piece");
					continue;
				}

				else {
//					System.out.println("Adding to the stack:\n");
//					System.out.println(board.getBoard()[newX][newY].toString());
					currLegalMoves.add(board.getBoard()[newX][newY]);
				}

			}

		}
		return currLegalMoves;
	}

}
