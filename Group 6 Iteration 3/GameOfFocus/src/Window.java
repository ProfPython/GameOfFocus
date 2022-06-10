import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;

public class Window extends JFrame{

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
    private JPanel contentPane;
    private CardLayout cardLayout;

    public Window(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        contentPane = new JPanel();
        cardLayout = new CardLayout();
        contentPane.setLayout(cardLayout);
        setContentPane(contentPane);
        new NewGameMenu(contentPane, cardLayout);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setResizable(false);
		setTitle("The Game of Focus");
		pack();
		setVisible(true);
    }

    
}
