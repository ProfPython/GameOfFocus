
/****
 * 
 * 
 * 
 * 	Player - holds information of the player such as colour, 
 * 	total pieces captured, total pieces remaining, 
 * 	and reserve pieces.
 * 
 * 
 */

import java.awt.Color;

public class Player {

	private int playerNum, reserve, totalPieces, capturedPieces;
	private Color color;
	private boolean isComp;

	public Player(int num, boolean comp) {
		totalPieces = 13;
		capturedPieces = 0;
		reserve = 1;
		playerNum = num;
		isComp = comp;
		setColor();
	}

	public int getReserve() {
		return reserve;
	}

	public void setReserve(int reserve) {
		this.reserve = reserve;
	}

	public int getTotalPieces() {
		return totalPieces;
	}

	public void setTotalPieces(int totalPieces) {
		this.totalPieces = totalPieces;
	}

	public Color getColor() {
		return color;
	}

	public void setColor() {
		if (playerNum == 0) {
			color = PLAYERCOLOR.PLAYERONE.getColor();
		}

		if (playerNum == 1) {
			color = PLAYERCOLOR.PLAYERTWO.getColor();
		}

		if (playerNum == 2) {
			color = PLAYERCOLOR.PLAYERTHREE.getColor();
		}

		if (playerNum == 3) {
			color = PLAYERCOLOR.PLAYERFOUR.getColor();
		}
	}

	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}

	public boolean getPlayerType() {
		return isComp;
	}

	public void incrementReserve() {
		reserve++;
		System.out.println("Player " + (1 + playerNum) + "'s Reserve:");
	}

	public void decrementReserve() {
		reserve--;
		System.out.println("Player " + (1 + playerNum) + "'s Reserve:");
	}

	public void decrementTotalPieces() {
		totalPieces--;
		System.out.println("Player " + (1 + playerNum) + "'s Pieces:");
	}

	public int getCapturedPieces() {
		return capturedPieces;
	}

	public void incrementCaptured() {
		capturedPieces++;
		System.out.println("Player " + (1 + playerNum) + "'s Captured Pieces: " + getCapturedPieces());
	}

}
