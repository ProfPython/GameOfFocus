/**
 * This class handles getting information about a particular game session
 * which aids the save and load functionality.
 */

public class GameSession implements java.io.Serializable {
	private String colorMode;
	private GameController brain;
	private Board board;
	private long id;

	private static final long serialVersionUID = 2L;

	public GameSession(GameController brain, Board board, String colorMode, long id) {
		this.brain = brain;
		this.board = board;
		this.colorMode = colorMode;
		this.id = id;
	}

	public String getColorMode() {
		return colorMode;
	}

	public GameController getBrain() {
		return brain;
	}

	public Board getBoard() {
		return board;
	}

	public long getId() {
		return id;
	}

}