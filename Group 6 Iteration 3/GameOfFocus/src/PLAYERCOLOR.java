import java.awt.Color;

// Default player colors 
public enum PLAYERCOLOR {
	PLAYERONE(new Color(52, 189, 89)), PLAYERTWO(new Color(255, 204, 0)), PLAYERTHREE(new Color(255, 59, 48)),
	PLAYERFOUR(new Color(50, 173, 230));

	private Color color;

	PLAYERCOLOR(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}