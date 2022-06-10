
/****
 * 
 * 
 * 
 *This class handles the UI that gives the user 
 *to start a new game, load an existing game or simply exit.
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
import java.util.ArrayList;

import javax.swing.ImageIcon;
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

public class EntranceMenu extends JPanel implements ActionListener {
	private JPanel mainMenu, middleSection, topSection;
	private CardLayout layout;

	public EntranceMenu(JPanel contentPane, CardLayout cardLayout) {
		this.mainMenu = contentPane;
		this.layout = cardLayout;
		initMenuBar();
		initTopSection();
		initMiddleSection();
		setLayout(new BorderLayout());
		add(topSection, BorderLayout.NORTH);
		add(middleSection, BorderLayout.CENTER);

		mainMenu.add(this, "MainMenu");
	}

	public void initTopSection() {
		ImageIcon title = new ImageIcon(getClass().getResource("newGameTitle.png"));
		topSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
		topSection.setLayout(new GridLayout(1, 2));
		JLabel menuTitle = new JLabel(title);
		topSection.add(menuTitle);
		topSection.setBorder(new EmptyBorder(35, 10, 35, 10));
		topSection.setBackground(Color.white);
	}

	public void initMiddleSection() {
		middleSection = new JPanel();
		middleSection.setLayout(new GridLayout(2, 1));
		JPanel topBtns = new JPanel(new FlowLayout(FlowLayout.CENTER));
		topBtns.setBorder(new EmptyBorder(110, 10, 0, 10));
		topBtns.setBackground(Color.WHITE);
		JButton start = button("Start", PLAYERCOLOR.PLAYERONE.getColor());
		JButton load = button("Load", PLAYERCOLOR.PLAYERTWO.getColor());
		topBtns.add(start);
		topBtns.add(load);
		middleSection.add(topBtns);
		JPanel bottomBtns = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton exit = button("Exit", PLAYERCOLOR.PLAYERTHREE.getColor());
		bottomBtns.add(exit);
		bottomBtns.setBackground(Color.WHITE);
		middleSection.add(bottomBtns);
	}

	public JButton button(String text, Color color) {
		JButton btn = new JButton(text);
		btn.setPreferredSize(new Dimension(250, 100));
		btn.setFont(new Font("Lucida Console", Font.BOLD, 30));
		btn.addActionListener(this);
		btn.setBackground(color);
		btn.setForeground(Color.WHITE);
		btn.setOpaque(true);
		btn.setBorder(new EmptyBorder(0, 20, 0, 20));
		return btn;
	}

	public int getJOption(String text) {
		return JOptionPane.showOptionDialog(middleSection, text, "Confirmation", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, null, null);
	}

	private JMenu createOptionMenu() {
        JMenu editMenu = new JMenu("Options");
		editMenu.setEnabled(false);
        return editMenu;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Load");
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
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainMenu);
		topFrame.setJMenuBar(menuBar);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object selected = e.getSource();

		if((selected instanceof JButton && ((JButton) selected).getText().equals("Load")) 
		|| (selected instanceof JMenuItem && ((JMenuItem) selected).getText().equals("Load"))){
			try {
				ArrayList<GameSession> sessions = FileManager.load();
				if (sessions.isEmpty()) {
					JOptionPane.showMessageDialog(middleSection, "No saved games found.");
				} else {
					if (getJOption("Are you sure you want to load a game?") == JOptionPane.YES_OPTION) {
						new LoadGameMenu(mainMenu, layout, sessions);
						layout.next(mainMenu);
						mainMenu.remove(this);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if((selected instanceof JButton && ((JButton) selected).getText().equals("Exit")) 
		|| (selected instanceof JMenuItem && ((JMenuItem) selected).getText().equals("Exit"))){
			System.exit(0);
		}
		else if (selected instanceof JButton) {
			JButton button = (JButton) selected;
			if (button.getText().equals("Start")) {
				if (getJOption("Are you sure you want to start a new game?") == JOptionPane.YES_OPTION) {
					new NewGameMenu(mainMenu, layout);
					layout.next(mainMenu);
					mainMenu.remove(this);
				}
			}
		}
	}

}