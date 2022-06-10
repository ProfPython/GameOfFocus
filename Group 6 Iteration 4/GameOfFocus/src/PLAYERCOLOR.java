
/****
 * 
 * 
 * Defines the colour of the players.
 * 
 */


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
	public void setColor(Color color) {
		this.color = color;
	}

	public static Color setColorByPlayer(int player){
		Color pColor;
		if(player == 0){
			pColor = PLAYERONE.getColor();
		}else if(player == 1){
			pColor = PLAYERTWO.getColor();
		}else if(player == 2){
			pColor = PLAYERTHREE.getColor();
		}else{
			pColor = PLAYERFOUR.getColor();
		}
		return pColor;
	}
}