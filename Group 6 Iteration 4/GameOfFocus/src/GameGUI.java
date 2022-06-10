
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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class GameGUI extends JPanel implements ActionListener {
	private JPanel gameSession;
	private CardLayout layout;
	private String colorMode; // color blind mode to be implemented
	private GameController brain;
	private Board board;
	private JLabel activePlayer;
	private JLabel[][] labels;
	private long gameSessionID;
	private MoveController move;
	private AudioController audio;

	private JPanel leftPanel, topStackInfoPanel, bottomStackInfoPanel, confirmMovePanel, reservePanel;
	JButton confirmBtn, reserveBtn;
	public JPanel messagePanel;
	int counter = 0;

	public GameGUI(JPanel contentPane, CardLayout cardLayout, String color, boolean[] aiPlayers, String diff,
			boolean newGame) throws IOException {

		gameSession = contentPane;
		layout = cardLayout;
		colorMode = color;

		labels = new JLabel[3][4];
		activePlayer = new JLabel();

		Path file = Files.createTempFile(null, ".txt");
		String filepath = file.toString();
		try (InputStream stream = this.getClass().getResourceAsStream("/focus-theme.wav")) {
			Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
		}

		try {
			audio = new AudioController(filepath);
		} catch (UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
		}

		if (newGame) {
			board = new Board();
			try {
				brain = new GameController(aiPlayers, diff, board, labels, activePlayer, audio);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			initLayout();
			board.setMessagePanel(messagePanel); // message panel
			board.settopStackInfoPanel(topStackInfoPanel); // stack info panel top
			board.setbottomStackInfoPanel(bottomStackInfoPanel); // stack info panel bottom

			contentPane.add(this, "GameSession");

			move = brain.getMoveController();
			board.setMoveController(move); // set the GameController with board
			board.setGameController(brain);

			// for saving the game
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			gameSessionID = timestamp.getTime();
		}
	}

	public void playFX(int index) {
		try {
			audio.playSoundEffect(index);
		} catch (IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadGame(GameSession session) {
		brain = session.getBrain();
		brain.setLabels(labels);
		brain.setActivePlayerLabel(activePlayer);
		board = session.getBoard();
		gameSessionID = session.getId();
		initLayout();
		board.setMessagePanel(messagePanel);
		board.settopStackInfoPanel(topStackInfoPanel);
		board.setbottomStackInfoPanel(bottomStackInfoPanel);
		move = brain.getMoveController();
		board.setMoveController(move);
		board.setGameController(brain);
		brain.setAudio(audio);
		if (brain.getCurrPlayer().isComputer()) {
			confirmBtn.setText("Do AI Move");
			confirmBtn.setBackground(brain.getCurrPlayer().getColor());
			confirmBtn.setOpaque(true);
			confirmBtn.setBorder(new EmptyBorder(25, 0, 25, 0));
			confirmBtn.repaint();
		}

		gameSession.add(this, "GameSession");
	}

	private int getIconStartNo() {
		int start = 1;
		if (this.colorMode != null) {
			if (this.colorMode.equals("RED"))
				start = 5;
			else if (this.colorMode.equals("GREEN"))
				start = 9;
			else if (this.colorMode.equals("BLUE"))
				start = 13;
			else if (this.colorMode.equals("FULL"))
				start = 17;
		}
		return start;
	}
	// GUI INITIALIZATION

	public void initLayout() {
		// Set NESWC panels for BorderLayout
		leftPanel = new JPanel(new BorderLayout());
		topStackInfoPanel = new JPanel(new GridLayout(5, 0, 0, 2));
		bottomStackInfoPanel = new JPanel(new GridLayout(5, 0, 0, 2));
		confirmMovePanel = new JPanel(new BorderLayout());
		messagePanel = new JPanel(new BorderLayout());
		messagePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		reservePanel = new JPanel(new BorderLayout());

		reserveBtn = new JButton("No Reserves");
		reserveBtn.setPreferredSize(new Dimension(70, 70));
		reserveBtn.setBackground(PLAYERCOLOR.setColorByPlayer(brain.getCurrPlayer().getPlayerNum()));
		reserveBtn.setOpaque(true);
		reserveBtn.setBorder(new EmptyBorder(25, 0, 25, 0));
		reserveBtn.setFont(new Font("Lucida Console", Font.BOLD, 18));
		reserveBtn.setEnabled(false);
		reserveBtn.addActionListener(this);

		confirmBtn = new JButton("Confirm Move");
		confirmBtn.setFont(new Font("Lucida Console", Font.BOLD, 18));
		confirmBtn.setPreferredSize(new Dimension(70, 70));
		confirmBtn.setBackground(PLAYERCOLOR.setColorByPlayer(brain.getCurrPlayer().getPlayerNum()));
		confirmBtn.setOpaque(true);
		confirmBtn.setBorder(new EmptyBorder(25, 0, 25, 0));
		confirmBtn.addActionListener(this);

		confirmMovePanel.add(confirmBtn, BorderLayout.SOUTH);
		reservePanel.add(reserveBtn, BorderLayout.NORTH);

		// pane is a splitpane that consists of the reserve and message panel
		// pane2 is a splitpane that consists of the two stack info panels
		// pane3 is a splitpane that consists of pane and pane2
		// entireRightPanel is a splitpane that consists of pane3 and the confirm move
		// button
		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, reservePanel, messagePanel);
		pane.setEnabled(false);
		pane.setDividerSize(30);
		JSplitPane pane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, topStackInfoPanel, bottomStackInfoPanel);
		pane2.setEnabled(false);
		pane2.setDividerSize(30);
		JSplitPane pane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pane, pane2);
		pane3.setEnabled(false);
		pane3.setDividerSize(30);
		JSplitPane entireRightPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pane3, confirmMovePanel);
		entireRightPanel.setEnabled(false);
		entireRightPanel.setDividerSize(30);
		// MAIN LAYOUT STRUCTURE
		setLayout(new BorderLayout());

		add(leftPanel, BorderLayout.CENTER);
		leftPanel.setPreferredSize(new Dimension(800, 800));
		entireRightPanel.setPreferredSize(new Dimension(200, 200));
		entireRightPanel.setDividerLocation(600);
		pane3.setDividerLocation(300);
		pane.setDividerLocation(75);
		pane2.setDividerLocation(75);
		add(entireRightPanel, BorderLayout.LINE_END);
		Border compound;
		Border colour = BorderFactory.createLineBorder(Color.red, 2);
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border redline = BorderFactory.createCompoundBorder(colour, raisedbevel);
		compound = BorderFactory.createTitledBorder(redline, "Stack Information", TitledBorder.CENTER,
				TitledBorder.TOP);
		entireRightPanel.setBorder(compound);

		initPanel();
		initBoard();
		initJMenuBar();
		if (brain.getCurrPlayer().isComputer()) {
			confirmBtn.setText("Do AI Move");
			confirmBtn.setBackground(brain.getCurrPlayer().getColor());
			confirmBtn.setOpaque(true);
			confirmBtn.setBorder(new EmptyBorder(25, 0, 25, 0));
			confirmBtn.repaint();
		}
	}

	// MORE GUI

	public void initPanel() {
		JPanel playerBox = new JPanel(new GridLayout(1, 5));
		int start = getIconStartNo();
		for (int i = 0; i < 4; i++) {
			JPanel player = new JPanel(new GridLayout(1, 2));
			JPanel playerInfoPanel = new JPanel();
			playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
			playerInfoPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
			JLabel playerNum = new JLabel("Player " + (i + 1));
			// This should be changed
			JLabel reservePieces = new JLabel("Reserves: " + brain.getPlayer()[i].getReserve());
			JLabel capturePieces = new JLabel("Captured: " + brain.getPlayer()[i].getCapturedPieces());
			JLabel totalPieces = new JLabel("Total Pieces: " + brain.getPlayer()[i].getTotalPieces());
			String iconPath = "piece" + (i + start) + ".png";
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
			labels[0][i] = reservePieces;
			labels[1][i] = capturePieces;
			labels[2][i] = totalPieces;
		}

		leftPanel.add(playerBox, BorderLayout.PAGE_START);
	}

	// INIT THE BOARD

	public void initBoard() {
		leftPanel.add(board, BorderLayout.CENTER);
	}


	private void initJMenuBar() {
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(gameSession);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createOptionMenu());
		topFrame.setJMenuBar(menuBar);
	}

	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem newItem = new JMenuItem("Save & Quit");
		fileMenu.add(newItem);
		newItem.addActionListener(this);
		JMenuItem openItem = new JMenuItem("Load");
		fileMenu.add(openItem);
		openItem.addActionListener(this);
		JMenuItem qMenuItem = new JMenuItem("Quit Game");
		fileMenu.add(qMenuItem);
		qMenuItem.addActionListener(this);
		JMenuItem exiMenuItem = new JMenuItem("Exit");
		fileMenu.add(exiMenuItem);
		exiMenuItem.addActionListener(this);
		return fileMenu;
	}

	private JMenu createOptionMenu() {
		JMenu editMenu = new JMenu("Options");
		JMenu music = new JMenu("Music");
		JMenuItem on = new JMenuItem("On");
		JMenuItem off = new JMenuItem("Off");
		music.add(on);
		on.addActionListener(this);
		music.add(off);
		off.addActionListener(this);
		editMenu.add(music);
		return editMenu;
	}

	// MEANT FOR THE FUTURE IMPLEMENTATION OF COLOR MODE
	public String getColorMode() {
		return colorMode;
	}

	private void saveGame() {
		GameSession game = new GameSession(brain, board, colorMode, gameSessionID);
		ArrayList<GameSession> loadGames = null;
		try {
			loadGames = FileManager.load();
			if (loadGames == null) {
				loadGames = new ArrayList<GameSession>();
			} else {
				for (GameSession g : loadGames) {
					if (g.getId() == gameSessionID) {
						loadGames.remove(g);
						break;
					}
				}
				loadGames.add(game);
				FileManager.save(loadGames);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ACTION EVENTS

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source instanceof JButton) {
			JButton button = (JButton) source;

			// CONFIRM MOVE BUTTON
			if (button.getText().equals("Confirm Move")) {

				if (!move.isComplete()) {
					playFX(3);
					board.setMessage(
							"<html><p style=\"text-align: center; font-weight:300; \">Complete your move <br>before pressing confirm!</p></html>");
				}

				else {
					try {
						playFX(1);
						move.confirmMove(true);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (brain.checkWin()) {
						// TODO Auto-generated method stub
						handleWin();
					}

					else {
						boolean moveCheck = true;
						while (moveCheck) {

							handleEndTurn();

							boolean check = brain.currentState(); // checking to see if we have moves to make

							if (!check) {
								moveCheck = false;
							}
						}

					}
				}
			}

			else if (button.getText().equals("Do AI Move")) {
				try {
					playFX(0);
					move.doCompMove(brain.getCurrPlayer());
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (brain.checkWin()) {
					handleWin();
				}

				else {
					handleEndTurn();
				}

			}

			// MOVE RESERVE PIECE BUTTON
			else if (button.equals(reserveBtn)) {
				playFX(0);
				counter++;
				if (counter % 2 == 1) {
					move.moveReserve();
				} else {
					move.dontMoveReserve(counter);
				}
			}
		}

		if (source instanceof JMenuItem) {
			JMenuItem menuItem = (JMenuItem) source;
			if (menuItem.getText().equals("Load")) {
				audio.stopMusic();
				try {
					ArrayList<GameSession> sessions = FileManager.load();
					if (sessions.isEmpty()) {
						JOptionPane.showMessageDialog(gameSession, "No saved games found.");
					} else {
						if (JOptionPane.showOptionDialog(gameSession, "Are you sure you want to load a game?",
								"Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null,
								null) == JOptionPane.YES_OPTION) {
							new LoadGameMenu(gameSession, layout, sessions);
							layout.next(gameSession);
							gameSession.remove(this);
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (menuItem.getText().equals("Exit")) {
				if (JOptionPane.showOptionDialog(leftPanel, "Are you sure you want to save and quit the game?",
						"Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null,
						null) == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			} else if (menuItem.getText().equals("Save & Quit")) {
				if (JOptionPane.showOptionDialog(leftPanel, "Are you sure you want to save and quit the game?",
						"Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null,
						null) == JOptionPane.YES_OPTION) {
					saveGame();
					audio.stopMusic();
					new EntranceMenu(gameSession, layout);
					layout.next(gameSession);
					gameSession.remove(this);
				}
			} else if (menuItem.getText().equals("Quit Game")) {
				if (JOptionPane.showOptionDialog(leftPanel, "Are you sure you want to quit the game?", "Confirmation",
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null,
						null) == JOptionPane.YES_OPTION) {
					audio.stopMusic();
					new EntranceMenu(gameSession, layout);
					layout.next(gameSession);
					gameSession.remove(this);
				}
			} else if (menuItem.getText().equals("On")) {
				audio.startMusic();
			} else if (menuItem.getText().equals("Off")) {
				audio.stopMusic();
			}
		}
	}

	private void handleWin() {

		Object[] options = { "Play again", "Quit game" };
		int n = JOptionPane.showOptionDialog(leftPanel,
			"Player " + ((brain.getCurrPlayer().getPlayerNum()) + 1) + " won the game!", "End of game",
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		while(n == JOptionPane.CLOSED_OPTION){
			JOptionPane.showMessageDialog(leftPanel, "You have to choose to either play again or quit!");
			n = JOptionPane.showOptionDialog(leftPanel,
			"Player " + ((brain.getCurrPlayer().getPlayerNum()) + 1) + " won the game!", "End of game",
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		}
		if (n == JOptionPane.YES_OPTION) {
			audio.stopMusic();
			new NewGameMenu(gameSession, layout);
			layout.next(gameSession);
			gameSession.remove(this);
		} else if (n == JOptionPane.NO_OPTION) {
			audio.stopMusic();
			new EntranceMenu(gameSession, layout);
			layout.first(gameSession);
			gameSession.remove(this);
		}
	}

	private void handleEndTurn() {
		try {
			brain.nextTurn();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		board.setMessage("<html><p style=\"text-align: center; font-weight:300; \">Player "
				+ (brain.getCurrPlayer().getPlayerNum() + 1) + "'s turn!</p></html>");

		if (brain.isCompTurn()) {
			confirmBtn.setText("Do AI Move");
		}

		else {
			confirmBtn.setText("Confirm Move");
		}

		reserveBtn.setBackground(brain.getCurrPlayer().getColor());
		reserveBtn.setOpaque(true);
		reserveBtn.setBorder(new EmptyBorder(25, 0, 25, 0));
		confirmBtn.setBackground(brain.getCurrPlayer().getColor());
		confirmBtn.setOpaque(true);
		confirmBtn.setBorder(new EmptyBorder(25, 0, 25, 0));

		if (brain.getCurrPlayer().getReserve() == 0) {
			reserveBtn.setEnabled(false);
			reserveBtn.setText("No Reserves");
		} else {
			reserveBtn.setText("Move Reserve");
			reserveBtn.setEnabled(true);
		}

		confirmBtn.repaint();
	}
}
