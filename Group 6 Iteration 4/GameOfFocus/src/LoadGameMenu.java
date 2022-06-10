
/****
 * 
 * 
 * This class handles the UI for the load functionality 
 * where a player can load or delete saved games.
 * 
 * 
 */



import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class LoadGameMenu extends JPanel implements ActionListener {
	private JPanel loadMenu, middleSection, topSection;
	private CardLayout layout;
	private ArrayList<GameSession> gameSessions;

	public LoadGameMenu(JPanel contentPane, CardLayout cardLayout, ArrayList<GameSession> gameSessions) {
		this.loadMenu = contentPane;
		this.layout = cardLayout;
		this.gameSessions = gameSessions;
		initMenuBar();
		initTopSection();
		initMiddleSection();
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		add(topSection, BorderLayout.NORTH);
		add(middleSection, BorderLayout.CENTER);
		loadMenu.add(this, "LoadGameMenu");
	}

	private void initTopSection() {
		topSection = new JPanel();
		JLabel menuTitle = new JLabel("Load Game");
		menuTitle.setFont(new Font("Lucida Console", Font.BOLD, 50));
		topSection.add(menuTitle);
		topSection.setBackground(Color.white);
	}

	private void initMiddleSection() {
		middleSection = new JPanel();
		middleSection.setLayout(new BoxLayout(middleSection, BoxLayout.Y_AXIS));
		middleSection.setBorder(new EmptyBorder(50, 10, 10, 10));
		middleSection.setBackground(Color.white);
		for (GameSession gameSession : gameSessions) {
			Date date = new Date(gameSession.getId());
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String strDate = dateFormat.format(date);
			JButton button = new JButton("Game Saved at " + strDate);
			button.setBackground(Color.WHITE);
			button.setFont(new Font("Lucida Console", Font.BOLD, 20));
			button.setMargin(new Insets(20, 50, 20, 50));
			button.setOpaque(true);
			button.setName(String.valueOf(gameSession.getId()));
			button.addActionListener(this);
			middleSection.add(button);
		}
	}

	public JButton button(String text, Color color) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Lucida Console", Font.BOLD, 20));
		btn.addActionListener(this);
		btn.setBackground(color);
		btn.setForeground(Color.WHITE);
		btn.setOpaque(true);
		btn.setBorder(new EmptyBorder(10, 10, 10, 10));
		return btn;
	}

	public int getJOption(String text) {
		return JOptionPane.showOptionDialog(middleSection, text, "Confirmation", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, null, null);
	}

	private JMenu createOptionMenu() {
        JMenu editMenu = new JMenu("Options");
		JMenuItem deleItem = new JMenuItem("Remove Saved Games");
		deleItem.addActionListener(this);
		editMenu.add(deleItem);
        return editMenu;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Back");
        fileMenu.add(openItem);
		openItem.addActionListener(this);
        JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(this);
        fileMenu.add(exitItem);
        return fileMenu;
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createOptionMenu());
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(loadMenu);
		topFrame.setJMenuBar(menuBar);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object selected = e.getSource();
		if (selected instanceof JMenuItem) {
			JMenuItem menuItem = (JMenuItem) selected;
			if (menuItem.getText().equals("Back")) {
					new EntranceMenu(loadMenu, layout);
					layout.next(loadMenu);
					loadMenu.remove(this);
			} else if (menuItem.getText().equals("Remove Saved Games")) {
				if (getJOption("Are you sure you want to delete all the saved games?") == JOptionPane.YES_OPTION) {
					try {
						ArrayList<GameSession> session = new ArrayList<GameSession>();
						FileManager.save(session);
						new EntranceMenu(loadMenu, layout);
						layout.next(loadMenu);
						loadMenu.remove(this);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			} 
		}else if (selected instanceof JButton) {
			JButton button = (JButton) selected;
			long id = Long.parseLong(button.getName());
				for (GameSession gameSession : gameSessions) {
					if (gameSession.getId() == id) {
						if (getJOption("Are you sure you want to load this game?") == JOptionPane.YES_OPTION) {

							GameGUI newGame;
							try {
								newGame = new GameGUI(loadMenu, layout, gameSession.getColorMode(),
										gameSession.getBrain().getIsComputer(), gameSession.getBrain().getDifficulty(),
										false);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								newGame = null;
							}
							newGame.loadGame(gameSession);
							layout.next(loadMenu);
							loadMenu.remove(this);
						}
					}
				}
		}
	}

}