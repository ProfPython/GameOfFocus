
/****
 * 
 * 
 * 
 *  NewGameMenu - this class handles the UI for creating a new game.
 * It gets preferred settings of the user that would be implemented in the game.
 * 
 * 
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class NewGameMenu extends JPanel implements ActionListener {
	private JPanel newGameMenu;
	private CardLayout layout;
	private JPanel middleSection, bottomSection, playerWrapper, optionsWrapper, topSection;
	private ImageIcon icon;
	private JButton startBtn;
	private JCheckBox cbTrue, cbFalse, diffEasy, diffHard;
	private String difficulty;
	private boolean colorBlind;
	private boolean[] isComputer;
	private ArrayList<PlayerToggleButton> playerToggleButtons;
	private ArrayList<JLabel> playerIconLabels;
	private boolean redBlind;
	private boolean greenBlind;
	private boolean blueYellow;
	private boolean fullColourBlind;
	private JMenuBar menubar;
	private Dimension dim;

	public NewGameMenu(JPanel contentPane, CardLayout cardLayout) {

		newGameMenu = contentPane;
		layout = cardLayout;
		this.menubar = menubar;

		// Game Logic Variables
		difficulty = "easy";
		isComputer = new boolean[4];
		initJMenuBar();
		updateColorTheme();

		// Set NESWC panels for BorderLayout
		topSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
		middleSection = new JPanel(new BorderLayout());
		bottomSection = new JPanel(new FlowLayout());

		// MAIN LAYOUT STRUCTURE
		setLayout(new BorderLayout());
		add(topSection, BorderLayout.NORTH);
		add(middleSection, BorderLayout.CENTER);
		add(bottomSection, BorderLayout.SOUTH);

		// TOP BAR LAYOUT

		JLabel menuTitle = new JLabel("New Game");
		menuTitle.setFont(new Font("Lucida Console", Font.BOLD, 50));
		topSection.add(menuTitle);
		topSection.setBorder(new EmptyBorder(35, 10, 35, 10));
		topSection.setBackground(Color.white);

		// MIDDLE SECTION WRAPPERS

		playerWrapper = new JPanel(new GridLayout(1, 4));
		playerWrapper.setBorder(new EmptyBorder(25, 10, 25, 10));
		playerWrapper.setBackground(Color.WHITE);

		optionsWrapper = new JPanel(new GridLayout(1, 2, 0, 0));
		optionsWrapper.setBorder(new EmptyBorder(25, 10, 45, 10));

		JLabel chooseUser = new JLabel();
		chooseUser.setHorizontalAlignment(JLabel.CENTER);
		chooseUser.setText("Click the buttons below to choose user type:");
		chooseUser.setFont(new Font("Lucida Console", Font.BOLD, 25));

		middleSection.add(chooseUser, BorderLayout.NORTH);

		middleSection.add(playerWrapper, BorderLayout.CENTER);
		middleSection.add(optionsWrapper, BorderLayout.SOUTH);
		middleSection.setBackground(Color.WHITE);

		// INIT METHODS

		initPlayers();
		initColourOptions();
		initDifficulty();

		// BOTTOM SECTION

		startBtn = new JButton("Start Game");

		startBtn.setPreferredSize(new Dimension(250, 100));
		startBtn.setFont(new Font("Lucida Console", Font.BOLD, 30));
		startBtn.addActionListener(this);
		startBtn.setBackground(PLAYERCOLOR.PLAYERONE.getColor());
		startBtn.setOpaque(true);
		startBtn.setBorder(new EmptyBorder(0, 0, 0, 0));
		startBtn.setForeground(Color.WHITE);
		bottomSection.add(startBtn);
		bottomSection.setBackground(Color.white);
		bottomSection.setBorder(new EmptyBorder(0, 0, 65, 0));

		newGameMenu.add(this, "newGameMenu");
	}

	public void initPlayers() {
		playerToggleButtons = new ArrayList<>();
		playerIconLabels = new ArrayList<>();
		for (int i = 1; i < 5; i++) {

			// Panels for each player

			JPanel playerBox = new JPanel(new GridLayout(0, 1, 0, 0));

			PlayerToggleButton playerBtn = new PlayerToggleButton(i);

			playerBtn.addActionListener(this);
			playerBtn.setBackground(PLAYERCOLOR.setColorByPlayer(i - 1));
			playerBtn.setOpaque(true);
			playerBtn.setBorder(new EmptyBorder(25, 0, 25, 0));
			String iconPath = "piece" + i + ".png";
			icon = new ImageIcon(getClass().getResource(iconPath));

			// Piece icon
			// WILL CHANGE TO DIFFERENT COLOR ICONS
			JLabel iconLabel = new JLabel(icon);

			// Add to appropriate containers
			playerBox.add(iconLabel);
			playerBox.add(playerBtn);
			playerBox.setBackground(Color.WHITE);
			playerBox.setBorder(new EmptyBorder(0, 10, 0, 10));
			playerWrapper.add(playerBox);
			playerToggleButtons.add(playerBtn);
			playerIconLabels.add(iconLabel);
		}
	}

	private void initJMenuBar() {
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(newGameMenu);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createOptionMenu());
		topFrame.setJMenuBar(menuBar);
	}

	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Load");
		fileMenu.add(openItem);
		openItem.addActionListener(this);
		JMenuItem saveItem = new JMenuItem("Exit");
		fileMenu.add(saveItem);
		saveItem.addActionListener(this);
		return fileMenu;
	}

	private JMenu createOptionMenu() {
		JMenu editMenu = new JMenu("Options");
		JMenu colorMenu = new JMenu("Color Mode");
		JMenuItem defaultColor = new JMenuItem("Classic Colours");
		defaultColor.addActionListener(this);
		JMenuItem colorJMenuItem = new JMenuItem("Colour Blind Accessible");
		colorJMenuItem.addActionListener(this);
		colorMenu.add(defaultColor);
		colorMenu.add(colorJMenuItem);
		editMenu.add(colorMenu);
		JMenu difJMenu = new JMenu("Difficulty");
		JMenuItem easy = new JMenuItem("Easy");
		easy.addActionListener(this);
		JMenuItem hard = new JMenuItem("Hard");
		hard.addActionListener(this);
		difJMenu.add(easy);
		difJMenu.add(hard);
		editMenu.add(difJMenu);
		return editMenu;
	}

	public void initDifficulty() {

		// Panels

		JPanel diffBox = new JPanel(new GridLayout(2, 1, 0, 0));
		JPanel diffChecks = new JPanel(new FlowLayout(FlowLayout.CENTER));

		// Section Title
		JLabel diffTitle = new JLabel("Select your Difficulty:");
		diffTitle.setFont(new Font("Lucida Console", Font.BOLD, 25));
		diffTitle.setHorizontalAlignment(JLabel.CENTER);

		// Check Boxes
		diffEasy = new JCheckBox("Easy Mode");
		diffHard = new JCheckBox("Hard Mode");

		diffEasy.setSelected(true);

		// Action Listener
		diffEasy.addActionListener(this);
		diffHard.addActionListener(this);

		// Fonts
		diffEasy.setFont(new Font("Lucida Console", Font.PLAIN, 16));
		diffHard.setFont(new Font("Lucida Console", Font.PLAIN, 16));

		// Add to appropriate containers
		diffChecks.add(diffEasy);
		diffChecks.add(diffHard);
		diffChecks.setBackground(Color.white);
		diffBox.add(diffTitle);
		diffBox.add(diffChecks);
		diffBox.setBackground(Color.white);
		optionsWrapper.add(diffBox);
		optionsWrapper.setBackground(Color.white);

	}

	public void initColourOptions() {

		JPanel colorBox = new JPanel(new GridLayout(2, 1, 0, 0));
		JPanel colorChecks = new JPanel(new FlowLayout(FlowLayout.CENTER));

		JLabel colorTitle = new JLabel("Select your Colour Mode:");
		colorTitle.setFont(new Font("Lucida Console", Font.BOLD, 25));
		colorTitle.setHorizontalAlignment(JLabel.CENTER);

		cbTrue = new JCheckBox("Colour Blind Accessible");
		cbFalse = new JCheckBox("Classic Colours");

		cbTrue.addActionListener(this);
		cbFalse.addActionListener(this);

		cbTrue.setFont(new Font("Lucida Console", Font.PLAIN, 16));
		cbFalse.setFont(new Font("Lucida Console", Font.PLAIN, 16));

		colorChecks.add(cbTrue);
		colorChecks.add(cbFalse);

		colorBox.add(colorTitle);
		colorBox.add(colorChecks);
		colorChecks.setBackground(Color.white);
		colorBox.setBackground(Color.white);
		optionsWrapper.add(colorBox);

//		// Set Classic Colors by default
		cbFalse.setSelected(true);
	}

	private void updateColorTheme() {
		if (this.colorBlind) {
			if (redBlind == true) {
				PLAYERCOLOR.PLAYERONE.setColor(new Color(000, 071, 142));
				PLAYERCOLOR.PLAYERTWO.setColor(new Color(247, 221, 000));
				PLAYERCOLOR.PLAYERTHREE.setColor(new Color(152, 177, 255));
				PLAYERCOLOR.PLAYERFOUR.setColor(new Color(165, 155, 124));
			} else if (greenBlind == true) {
				PLAYERCOLOR.PLAYERONE.setColor(new Color(233, 178, 042));
				PLAYERCOLOR.PLAYERTWO.setColor(new Color(111, 164, 253));
				PLAYERCOLOR.PLAYERTHREE.setColor(new Color(166, 149, 000));
				PLAYERCOLOR.PLAYERFOUR.setColor(new Color(251, 218, 212));
			} else if (blueYellow == true) {
				PLAYERCOLOR.PLAYERONE.setColor(new Color(255, 051, 050));
				PLAYERCOLOR.PLAYERTWO.setColor(new Color(166, 239, 255));
				PLAYERCOLOR.PLAYERTHREE.setColor(new Color(148, 109, 117));
				PLAYERCOLOR.PLAYERFOUR.setColor(new Color(062, 169, 182));
			}
		} else {
			PLAYERCOLOR.PLAYERONE.setColor(new Color(52, 189, 89));
			PLAYERCOLOR.PLAYERTWO.setColor(new Color(255, 204, 0));
			PLAYERCOLOR.PLAYERTHREE.setColor(new Color(255, 59, 48));
			PLAYERCOLOR.PLAYERFOUR.setColor(new Color(50, 173, 230));
		}
	}

	private void updatePlayerToggleButtons() {
		for (int i = 0; i < this.playerToggleButtons.size(); i++) {
			this.playerToggleButtons.get(i).setBackground(PLAYERCOLOR.setColorByPlayer(i));
		}
	}

	private void updatePlayerIconPanel() {
		int start = 1;
		if (this.colorBlind) {
			if (this.redBlind)
				start = 5;
			else if (this.greenBlind)
				start = 9;
			else if (this.blueYellow)
				start = 13;
		}

		for (int i = 0; i < this.playerIconLabels.size(); i++) {
			String iconPath = "piece" + (i + start) + ".png";
			icon = new ImageIcon(getClass().getResource(iconPath));
			this.playerIconLabels.get(i).setIcon(icon);
		}
	}

	// Array for dynamically handling the human / computer player confirmation
	// message
	private String[] isComputer() {

		String[] stringComp = new String[4];

		for (int i = 0; i < 4; i++) {
			if (isComputer[i] == true) {
				stringComp[i] = "Computer";
			} else {
				stringComp[i] = "Human";
			}
		}

		return stringComp;
	}

	private String getColorMode() {
		String colormode = null;
		if (this.colorBlind) {
			if (redBlind) {
				colormode = "RED";
			} else if (greenBlind) {
				colormode = "GREEN";
			} else if (blueYellow) {
				colormode = "BLUE";
			}
		}
		return colormode;
	}

	// creates the review settings dialog

	private void reviewSettings() throws IOException {
		String[] cpuStr = isComputer();
		String AI = null;
		String colourSetting;

		if (checkDiff() && checkColor()) {

			// Setting the message for the difficulty setting

			if (diffEasy.isSelected() || diffHard.isSelected()) {
				if (difficulty.equals("easy")) {
					AI = "AI Difficulty: Easy";
				}

				else if (difficulty.equals("hard")) {
					AI = "AI Difficulty: Hard";
				}
			}

			// Setting the message for the difficulty setting

			if (colorBlind == true) {
				colourSetting = "Using Colour Blind Friendly Mode";
			}

			else {
				colourSetting = "Using the Classic Colours";
			}

			// Create and populate the confirmation message.

			int choice = JOptionPane.showOptionDialog(middleSection,
					"Almost ready to play! Do you want to go ahead with these settings?\nPlayer 1: " + cpuStr[0]
							+ "	\nPlayer 2: " + cpuStr[1] + "	\nPlayer 3: " + cpuStr[2] + "	\nPlayer 4: "
							+ cpuStr[3] + "\n" + AI + "\n" + colourSetting + ".",
					"Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
			if (choice == JOptionPane.YES_OPTION) {
				new GameGUI(newGameMenu, layout, getColorMode(), isComputer, difficulty, true);
				layout.next(newGameMenu);
				newGameMenu.remove(this);
			}
		}

		// validity check for color settings
		else if (!checkColor()) {
			JOptionPane.showMessageDialog(middleSection, "Please select your Colour Settings");
		}

		// validity check for difficulty
		else if (!checkDiff()) {
			JOptionPane.showMessageDialog(middleSection, "Please select a Difficulty");
		}
	}

	// check if a color mode has been selected
	public boolean checkColor() {
		if ((!cbTrue.isSelected()) && (!cbFalse.isSelected())) {
			return false;
		}

		return true;
	}

	// check if a difficulty mode has been selected
	public boolean checkDiff() {
		if ((!diffEasy.isSelected()) && (!diffHard.isSelected())) {
			return false;
		}
		return true;
	}

	public void chooseDeficiency() {
		JCheckBox checkbox = new JCheckBox("Colour setting 1 (Red colour blind)");
		JCheckBox checkbox1 = new JCheckBox("Colour setting 2 (Green colour blind)");
		JCheckBox checkbox2 = new JCheckBox("Colour setting 3 (Blue-yellow colour blind)");

		String message = "Choose an accessible colour mode:";
		Object[] params = { message, checkbox, checkbox1, checkbox2 };
		int n = JOptionPane.showConfirmDialog(middleSection, params, "What type of colour deficiency do you have?",
				JOptionPane.OK_CANCEL_OPTION);
		redBlind = checkbox.isSelected();
		greenBlind = checkbox1.isSelected();
		blueYellow = checkbox2.isSelected();
		if ((redBlind || greenBlind || blueYellow || fullColourBlind) && n == JOptionPane.OK_OPTION) {
			updateColorTheme();
			updatePlayerToggleButtons();
			updatePlayerIconPanel();
			cbTrue.setSelected(true);
		} else if (!(blueYellow) && !(redBlind) && !(greenBlind) && !(fullColourBlind) && n == JOptionPane.OK_OPTION) {
			JOptionPane.showMessageDialog(middleSection, "Please select a type of colour deficiency");
			cbFalse.setSelected(true);
			cbTrue.setSelected(false);
		} else if (n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION) {
			cbFalse.setSelected(true);
			cbTrue.setSelected(false);
		}
		startBtn.setBackground(PLAYERCOLOR.PLAYERONE.getColor());
	}

	// TODO Auto-generated method stub
	@Override
	public void actionPerformed(ActionEvent evt) {
		Object selected = evt.getSource();
		// CHECK BOX EVENTS
		// MAYBE PUT IN OWN METHOD
		if (selected instanceof JCheckBox) {

			if (selected.equals(cbTrue)) {
				colorBlind = true;
				cbFalse.setSelected(false);
				chooseDeficiency();
			}

			if (selected.equals(cbFalse)) {
				colorBlind = false;
				cbTrue.setSelected(false);
				updateColorTheme();
				updatePlayerToggleButtons();
				updatePlayerIconPanel();
			}

			if (selected.equals(diffEasy)) {
				difficulty = "easy";
				diffHard.setSelected(false);
			}

			if (selected.equals(diffHard)) {
				difficulty = "hard";
				diffEasy.setSelected(false);
			}
		}

		if (selected instanceof JMenuItem) {
			JMenuItem menuItem = (JMenuItem) selected;
			if (menuItem.getText().equals("Load")) {
				try {
					ArrayList<GameSession> sessions = FileManager.load();
					if (sessions.isEmpty()) {
						JOptionPane.showMessageDialog(newGameMenu, "No saved games found.");
					} else {
						if (JOptionPane.showOptionDialog(middleSection, "Are you sure you want to load a game?",
								"Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null,
								null) == JOptionPane.YES_OPTION) {
							new LoadGameMenu(newGameMenu, layout, sessions);
							layout.next(newGameMenu);
							newGameMenu.remove(this);
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (menuItem.getText().equals("Exit")) {
				System.exit(0);
			} else if (menuItem.getText().equals("Easy")) {
				difficulty = "easy";
				diffEasy.setSelected(true);
				diffHard.setSelected(false);
			} else if (menuItem.getText().equals("Hard")) {
				difficulty = "hard";
				diffHard.setSelected(true);
				diffEasy.setSelected(false);
			} else if (menuItem.getText().equals("Colour Blind Accessible")) {
				colorBlind = true;
				cbFalse.setSelected(false);
				chooseDeficiency();
			} else if (menuItem.getText().equals("Classic Colours")) {
				cbFalse.setSelected(true);
				colorBlind = false;
				cbTrue.setSelected(false);
				updateColorTheme();
				updatePlayerToggleButtons();
				updatePlayerIconPanel();
			}
		}

		// JBUTTON ACTIONS
		// MAYBE PUT IN OWN METHOD
		if (selected instanceof PlayerToggleButton) {
			if (((PlayerToggleButton) selected).getText().equals("Computer Player")) {
				((PlayerToggleButton) selected).setText("Human Player");
				((PlayerToggleButton) selected).setPlayer(false);

				int index = ((PlayerToggleButton) selected).getPlayerNum();
				isComputer[index - 1] = false;
			} else if (((PlayerToggleButton) selected).getText().equals("Human Player")) {
				((PlayerToggleButton) selected).setText("Computer Player");
				((PlayerToggleButton) selected).setPlayer(true);
				int index = ((PlayerToggleButton) selected).getPlayerNum();
				isComputer[index - 1] = true;
			}
		}

		if (selected instanceof JButton) {
			if (selected.equals(startBtn)) {
				try {
					reviewSettings();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

}