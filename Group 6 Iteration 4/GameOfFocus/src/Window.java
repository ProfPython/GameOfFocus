/***
 * Creates a new window for the game.
 * 
 */


import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Window extends JFrame {

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;

	public Window() {

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		final CardLayout cardLayout = new CardLayout();
		final JPanel contentPane = new JPanel();
		contentPane.setLayout(cardLayout);
		setContentPane(contentPane);
		new EntranceMenu(contentPane, cardLayout);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setResizable(false);
		setTitle("The Game of Focus");
		pack();
		setVisible(true);
	}

}
