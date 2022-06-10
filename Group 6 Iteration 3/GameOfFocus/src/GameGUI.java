
/**
 * 
 * GameGUI - the main GUI class for Focus Game.
 * 
 * Creates the Board Class (another GUI class)
 * Creates GameController (the brain or main game engine)
 * 
 * 
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class GameGUI extends JPanel implements ActionListener {
	private JPanel gameSession;
	private CardLayout layout;
	private boolean colorMode; // color blind mode to be implemented
	private GameController brain;
	private Board board;
	private JLabel activePlayer;
	private JLabel[][] labels;

	private JPanel leftPanel, topStackInfoPanel, bottomStackInfoPanel, confirmMovePanel, reservePanel;
	JButton butt, reserveBtn;

	public GameGUI(JPanel contentPane, CardLayout cardLayout, boolean color, boolean[] aiPlayers, String diff) {
		gameSession = contentPane;
		layout = cardLayout;
		colorMode = color;
		initLayout();

		board.settopStackInfoPanel(topStackInfoPanel); // stack info panel top
		board.setbottomStackInfoPanel(bottomStackInfoPanel); // stack info panel bottom

		contentPane.add(this, "GameSession");
		brain = new GameController(aiPlayers, diff); // new game engine
		brain.setBoardClass(board); // to get methods from Board class
		brain.setBoard(board.getBoard()); // sets the game board for the GameController
		board.setController(brain); // set the GameController with board

	}

	// GUI INITIALIZATION

	public void initLayout() {
		// Set NESWC panels for BorderLayout

		leftPanel = new JPanel(new BorderLayout());
		topStackInfoPanel = new JPanel(new GridLayout(5, 0, 0, 2));
		bottomStackInfoPanel = new JPanel(new GridLayout(5, 0, 0, 2));
		confirmMovePanel = new JPanel(new BorderLayout());
		reservePanel = new JPanel(new BorderLayout());
		reserveBtn = new JButton("Move Reserve");
		reserveBtn.setPreferredSize(new Dimension(70, 70));
		reserveBtn.addActionListener(this);
		butt = new JButton("Confirm Move");
		butt.setPreferredSize(new Dimension(70, 70));
		butt.addActionListener(this);
		confirmMovePanel.add(butt, BorderLayout.SOUTH);
		reservePanel.add(reserveBtn, BorderLayout.NORTH);

		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, reservePanel, topStackInfoPanel);
		pane.setEnabled(false);
		pane.setDividerSize(30);
		JSplitPane pane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pane, bottomStackInfoPanel);
		pane2.setEnabled(false);
		pane2.setDividerSize(30);
		JSplitPane pane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pane2, confirmMovePanel);
		pane3.setEnabled(false);
		pane3.setDividerSize(30);
		// MAIN LAYOUT STRUCTURE
		setLayout(new BorderLayout());

		add(leftPanel, BorderLayout.LINE_START);
		leftPanel.setPreferredSize(new Dimension(800, 800));
		pane3.setPreferredSize(new Dimension(200, 200));
		pane3.setDividerLocation(620);
		pane2.setDividerLocation(350);
		add(pane3, BorderLayout.LINE_END);
		Border compound;
		Border colour = BorderFactory.createLineBorder(Color.red, 2);
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border redline = BorderFactory.createCompoundBorder(colour, raisedbevel);
		compound = BorderFactory.createTitledBorder(redline, "Stack Information", TitledBorder.CENTER,
				TitledBorder.TOP);
		pane3.setBorder(compound);

		initPanel();
		initBoard();
		initBottomPanel();
	}

	// MORE GUI

	public void initPanel() {
		JPanel playerBox = new JPanel(new GridLayout(1, 5));
		labels = new JLabel[3][4];
		for (int i = 1; i < 5; i++) {
			JPanel player = new JPanel(new GridLayout(1, 2));
			JPanel playerInfoPanel = new JPanel();
			playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
			JLabel playerNum = new JLabel("Player " + i);
			// This should be changed
			JLabel reservePieces = new JLabel("Reserves: 0");
			JLabel capturePieces = new JLabel("Captured: 0");
			JLabel totalPieces = new JLabel("Total Pieces: 13");
			String iconPath = "piece" + i + ".png";
			ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
			Image scaleImage = icon.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT);
			// Piece icon
			// WILL CHANGE TO DIFFERENT COLOR ICONS
			JLabel iconLabel = new JLabel(new ImageIcon(scaleImage));
			// Add to appropriate containers
			playerInfoPanel.add(playerNum);
			playerInfoPanel.add(reservePieces);
			playerInfoPanel.add(capturePieces);
			playerInfoPanel.add(totalPieces);
			player.add(iconLabel);
			player.add(playerInfoPanel);
			playerBox.add(player);
			labels[0][i - 1] = reservePieces;
			labels[1][i - 1] = capturePieces;
			labels[2][i - 1] = totalPieces;
		}

		leftPanel.add(playerBox, BorderLayout.PAGE_START);
	}

	// INIT THE BOARD

	public void initBoard() {
		board = new Board(colorMode);
		leftPanel.add(board, BorderLayout.CENTER);
	}

	// BOTTOM PANEL
	public void initBottomPanel() {
		JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(this);
		bottomPanel.add(quitButton);
		activePlayer = new JLabel("Active Player: Player 1");
		activePlayer.setFont(new Font("Lucida Console", Font.BOLD, 18));
		activePlayer.setForeground(PLAYERCOLOR.PLAYERONE.getColor());
		bottomPanel.add(activePlayer);
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		bottomPanel.add(saveButton);
		leftPanel.add(bottomPanel, BorderLayout.PAGE_END);
	}

	// MEANT FOR THE FUTURE IMPLEMENTATION OF COLOR MODE
	public boolean getColorMode() {
		return colorMode;
	}

	// ACTION EVENTS

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source instanceof JButton) {
			JButton button = (JButton) source;
			if (button.getText().equals("Quit")) {
				int choice = JOptionPane.showOptionDialog(leftPanel, "Are you sure you want to quit the game.",
						"Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
				if (choice == JOptionPane.YES_OPTION) {
					layout.first(gameSession);
				}
			} else if (button.getText().equals("Save")) {
				JOptionPane.showMessageDialog(leftPanel, "Not implemented.");
			}

			// CONFIRM MOVE BUTTON
			else if (button.getText().equals("Confirm Move")) {
				if (brain.getCompleteMove()) {
					brain.confirmMove(brain.getCurrMove()[0], brain.getCurrMove()[1], activePlayer, labels);
					topStackInfoPanel.removeAll();
					bottomStackInfoPanel.removeAll();
					topStackInfoPanel.repaint();
					bottomStackInfoPanel.repaint();
				}

			}

			// MOVE RESERVE PIECE BUTTON
			else if (button.equals(reserveBtn)) {
				brain.moveReserve();
			}
		}
	}

}