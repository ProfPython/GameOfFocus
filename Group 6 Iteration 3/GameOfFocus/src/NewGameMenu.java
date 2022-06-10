import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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

	ArrayList<Color> colors;
	ArrayList<Color> altColors;

	Dimension dim;

	public NewGameMenu(JPanel contentPane, CardLayout cardLayout) {

		newGameMenu = contentPane;
		layout = cardLayout;

		// Game Logic Variables
		difficulty = new String();
		isComputer = new boolean[4];

		// Color arrays for modes

		colors = new ArrayList<Color>();
		colors.add(new Color(87, 255, 42));
		colors.add(new Color(252, 238, 33));
		colors.add(new Color(237, 28, 36));
		colors.add(new Color(0, 204, 255));

		altColors = new ArrayList<Color>();
		altColors.add(new Color(237, 123, 132));
		altColors.add(new Color(118, 236, 255));
		altColors.add(new Color(124, 149, 161));
		altColors.add(new Color(255, 209, 224));

		// Set NESWC panels for BorderLayout
		topSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
		middleSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bottomSection = new JPanel(new FlowLayout());

		// MAIN LAYOUT STRUCTURE
		setLayout(new BorderLayout());
		add(topSection, BorderLayout.NORTH);
		add(middleSection, BorderLayout.CENTER);
		add(bottomSection, BorderLayout.SOUTH);

		// TOP BAR LAYOUT

		ImageIcon title = new ImageIcon(getClass().getResource("newGameTitle.png"));
		JLabel menuTitle = new JLabel(title);
		topSection.add(menuTitle);
		topSection.setBorder(new EmptyBorder(35, 10, 35, 10));
		topSection.setBackground(Color.white);

		// MIDDLE SECTION WRAPPERS

		playerWrapper = new JPanel(new GridLayout(1, 4));
		playerWrapper.setBorder(new EmptyBorder(25, 10, 25, 10));
		playerWrapper.setBackground(Color.WHITE);

		optionsWrapper = new JPanel(new GridLayout(1, 2, 0, 0));
		optionsWrapper.setBorder(new EmptyBorder(25, 10, 45, 10));

		middleSection.add(playerWrapper);
		middleSection.add(optionsWrapper);
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
		startBtn.setBackground(colors.get(1));
		startBtn.setOpaque(true);
		startBtn.setBorder(new EmptyBorder(0, 0, 0, 0));

		bottomSection.add(startBtn);
		bottomSection.setBackground(Color.white);
		bottomSection.setBorder(new EmptyBorder(0, 0, 65, 0));

		newGameMenu.add(this, "newGameMenu");
	}

	public void initPlayers() {

		for (int i = 1; i < 5; i++) {

			// Panels for each player

			JPanel playerBox = new JPanel(new GridLayout(0, 1, 0, 0));
			PlayerToggleButton playerBtn = new PlayerToggleButton(i, colors.get(i - 1));

			playerBtn.addActionListener(this);
			playerBtn.setBackground(colors.get(i - 1));
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
		}
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

	// creates the review settings dialog

	private void reviewSettings() {
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
				// JOptionPane.showMessageDialog(middleSection, "This concludes our second
				// iteration demo!");
				new GameGUI(newGameMenu, layout, colorBlind, isComputer, difficulty);
				layout.next(newGameMenu);
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

	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

		Object selected = evt.getSource();

		// CHECK BOX EVENTS
		// MAYBE PUT IN OWN METHOD
		if (selected instanceof JCheckBox) {

			if (selected.equals(cbTrue)) {
				colorBlind = true;
				cbFalse.setSelected(false);
				// colorCheckBoxEmpty();
			}

			if (selected.equals(cbFalse)) {
				colorBlind = false;
				cbTrue.setSelected(false);
				// colorCheckBoxEmpty();
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
				reviewSettings();
			}

		}
	}

}
