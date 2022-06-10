
/***
 * 
 * 
 * 
 * Board - GUI class for the board
 * 
 * 
 * 
 * 
 * 
 */

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Board extends JPanel implements ActionListener {

	private Stack[][] board;
	private GameController brain;
	private JPanel topStackInfoPanel;
	private JPanel bottomStackInfoPanel;

	public Board(boolean colorBlind) {
		GridLayout layout = new GridLayout(8, 8);
		layout.setHgap(3);
		layout.setVgap(3);
		setLayout(layout);

		generateBoardConfig();
		generateBoard();
		cleanStacks(); // takes care of the corner pieces
	}

	private void generateBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null) {
					board[i][j].addActionListener(this);
					add(board[i][j]);
				} else {
					Stack btn = new Stack(i, j, Color.WHITE);
					// btn.setEnabled(false);
					add(btn);
				}
			}
		}
	}

	public Stack[][] getBoard() {
		return board;
	}

	// cleaning the corner inactive stacks
	private void cleanStacks() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j].getStackOrder().get(0) == Color.LIGHT_GRAY) {

					board[i][j].setText(null);
					board[i][j].setEnabled(false);
				}

			}
		}
	}

	public JPanel getTopInfoPanel() {
		return topStackInfoPanel;
	}

	public JPanel getBottomInfoPanel() {
		return bottomStackInfoPanel;
	}

	public void settopStackInfoPanel(JPanel panel) {
		topStackInfoPanel = panel;
	}

	public void setbottomStackInfoPanel(JPanel panel) {
		bottomStackInfoPanel = panel;
	}

	// displays stack information on side of window
	public void drawStackInfo(Stack stack, JPanel panel) {
		for (int i = 0; i < stack.getStackOrder().size(); i++) {
			JButton button = new JButton();
			button.setBackground(stack.getStackOrder().get(i));
			button.setOpaque(true);
			button.setBorder(new EmptyBorder(25, 0, 25, 0));
			panel.add(button);

		}
		panel.validate();
	}

	// Action events for drawing stack info on click.

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (source instanceof Stack) {
			Stack tmp = (Stack) source;
			brain.moveOrder(tmp);
			if (tmp == brain.getCurrMove()[0]) {
				drawStackInfo(tmp, topStackInfoPanel);
			} else if (tmp == brain.getCurrMove()[1]) {
				drawStackInfo(tmp, bottomStackInfoPanel);
			}
		}
	}

	// Will probably load this from a file in the future.
	private void generateBoardConfig() {
		board = new Stack[8][8];
		board[0][0] = new Stack(0, 0, Color.LIGHT_GRAY);
		board[0][1] = new Stack(0, 1, Color.LIGHT_GRAY);
		board[0][2] = new Stack(0, 2, PLAYERCOLOR.PLAYERONE.getColor());
		board[0][3] = new Stack(0, 3, PLAYERCOLOR.PLAYERONE.getColor());
		board[0][4] = new Stack(0, 4, PLAYERCOLOR.PLAYERTWO.getColor());
		board[0][5] = new Stack(0, 5, PLAYERCOLOR.PLAYERFOUR.getColor());
		board[0][6] = new Stack(0, 6, Color.LIGHT_GRAY);
		board[0][7] = new Stack(0, 7, Color.LIGHT_GRAY);

		board[1][0] = new Stack(1, 0, Color.LIGHT_GRAY);
		board[1][1] = new Stack(1, 1, PLAYERCOLOR.PLAYERFOUR.getColor());
		board[1][2] = new Stack(1, 2, PLAYERCOLOR.PLAYERFOUR.getColor());
		board[1][3] = new Stack(1, 3, PLAYERCOLOR.PLAYERFOUR.getColor());
		board[1][4] = new Stack(1, 4, PLAYERCOLOR.PLAYERTWO.getColor());
		board[1][5] = new Stack(1, 5, PLAYERCOLOR.PLAYERFOUR.getColor());
		board[1][6] = new Stack(1, 6, PLAYERCOLOR.PLAYERTWO.getColor());
		board[1][7] = new Stack(1, 7, Color.LIGHT_GRAY);

		board[2][0] = new Stack(2, 0, PLAYERCOLOR.PLAYERONE.getColor());
		board[2][1] = new Stack(2, 1, PLAYERCOLOR.PLAYERONE.getColor());
		board[2][2] = new Stack(2, 2, PLAYERCOLOR.PLAYERONE.getColor());
		board[2][3] = new Stack(2, 3, PLAYERCOLOR.PLAYERONE.getColor());
		board[2][4] = new Stack(2, 4, PLAYERCOLOR.PLAYERTWO.getColor());
		board[2][5] = new Stack(2, 5, PLAYERCOLOR.PLAYERFOUR.getColor());
		board[2][6] = new Stack(2, 6, PLAYERCOLOR.PLAYERTWO.getColor());
		board[2][7] = new Stack(2, 7, PLAYERCOLOR.PLAYERFOUR.getColor());

		board[3][0] = new Stack(3, 0, PLAYERCOLOR.PLAYERFOUR.getColor());
		board[3][1] = new Stack(3, 1, PLAYERCOLOR.PLAYERFOUR.getColor());
		board[3][2] = new Stack(3, 2, PLAYERCOLOR.PLAYERFOUR.getColor());
		board[3][3] = new Stack(3, 3, PLAYERCOLOR.PLAYERFOUR.getColor());
		board[3][4] = new Stack(3, 4, PLAYERCOLOR.PLAYERTWO.getColor());
		board[3][5] = new Stack(3, 5, PLAYERCOLOR.PLAYERFOUR.getColor());
		board[3][6] = new Stack(3, 6, PLAYERCOLOR.PLAYERTWO.getColor());
		board[3][7] = new Stack(3, 7, PLAYERCOLOR.PLAYERFOUR.getColor());

		board[4][0] = new Stack(4, 0, PLAYERCOLOR.PLAYERTHREE.getColor());
		board[4][1] = new Stack(4, 1, PLAYERCOLOR.PLAYERONE.getColor());
		board[4][2] = new Stack(4, 2, PLAYERCOLOR.PLAYERTHREE.getColor());
		board[4][3] = new Stack(4, 3, PLAYERCOLOR.PLAYERONE.getColor());
		board[4][4] = new Stack(4, 4, PLAYERCOLOR.PLAYERTHREE.getColor());
		board[4][5] = new Stack(4, 5, PLAYERCOLOR.PLAYERTHREE.getColor());
		board[4][6] = new Stack(4, 6, PLAYERCOLOR.PLAYERTHREE.getColor());
		board[4][7] = new Stack(4, 7, PLAYERCOLOR.PLAYERTHREE.getColor());

		board[5][0] = new Stack(5, 0, PLAYERCOLOR.PLAYERTHREE.getColor());
		board[5][1] = new Stack(5, 1, PLAYERCOLOR.PLAYERONE.getColor());
		board[5][2] = new Stack(5, 2, PLAYERCOLOR.PLAYERTHREE.getColor());
		board[5][3] = new Stack(5, 3, PLAYERCOLOR.PLAYERONE.getColor());
		board[5][4] = new Stack(5, 4, PLAYERCOLOR.PLAYERTWO.getColor());
		board[5][5] = new Stack(5, 5, PLAYERCOLOR.PLAYERTWO.getColor());
		board[5][6] = new Stack(5, 6, PLAYERCOLOR.PLAYERTWO.getColor());
		board[5][7] = new Stack(5, 7, PLAYERCOLOR.PLAYERTWO.getColor());

		board[6][0] = new Stack(6, 0, Color.LIGHT_GRAY);
		board[6][1] = new Stack(6, 1, PLAYERCOLOR.PLAYERONE.getColor());
		board[6][2] = new Stack(6, 2, PLAYERCOLOR.PLAYERTHREE.getColor());
		board[6][3] = new Stack(6, 3, PLAYERCOLOR.PLAYERONE.getColor());
		board[6][4] = new Stack(6, 4, PLAYERCOLOR.PLAYERTHREE.getColor());
		board[6][5] = new Stack(6, 5, PLAYERCOLOR.PLAYERTHREE.getColor());
		board[6][6] = new Stack(6, 6, PLAYERCOLOR.PLAYERTHREE.getColor());
		board[6][7] = new Stack(6, 7, Color.LIGHT_GRAY);

		board[7][0] = new Stack(7, 0, Color.LIGHT_GRAY);
		board[7][1] = new Stack(7, 1, Color.LIGHT_GRAY);
		board[7][2] = new Stack(7, 2, PLAYERCOLOR.PLAYERTHREE.getColor());
		board[7][3] = new Stack(7, 3, PLAYERCOLOR.PLAYERONE.getColor());
		board[7][4] = new Stack(7, 4, PLAYERCOLOR.PLAYERTWO.getColor());
		board[7][5] = new Stack(7, 5, PLAYERCOLOR.PLAYERTWO.getColor());
		board[7][6] = new Stack(7, 6, Color.LIGHT_GRAY);
		board[7][7] = new Stack(7, 7, Color.LIGHT_GRAY);

	}

	public void setController(GameController control) {
		brain = control;
	}
}