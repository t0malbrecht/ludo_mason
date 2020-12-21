package game;

public class Token {
	
	// properties
	public Integer position;
	public boolean isHome;
	public boolean inWinSpot;
	public AbstractPlayer player;
	public int stepsToEnemy;
	public int stepsFromEnemy;
	public Integer stepsToWinBase;
	public Integer canGoInWinBaseWith;
	public boolean canHitOtherToken;
	public boolean isCloseToEnemies;
	
	/**
	 * Konstruktor einer Spielerfigur.
	 */
	public Token(AbstractPlayer player) {
		this.position = -2;
		this.isHome = true;
		this.player = player;
		this.canGoInWinBaseWith = 0;
		this.canHitOtherToken = true;
		this.isCloseToEnemies = false;
	}
	
	/**
	 * Wird vom Spielfeld entfernt.
	 */
	public void gotKicked(Token kickedBy) {
		setHome();
		Game2.KicksGotten[this.player.id]++;
		Game2.KicksMade[kickedBy.player.id]++;
	}
	
	/**
	 * Wird auf das Spielfeld gesetzt.
	 */
	public void setOnField() {
		this.isHome = false;
		this.position = this.player.startPos;
	}
	
	/**
	 * Spielfigur l�uft in das Ziel ein.
	 */
	public void setInWinspot() {
		this.inWinSpot = true;
		this.canGoInWinBaseWith = 0;
		this.position = -1;
		this.player.checkWinningCondition();
	}

	/**
	 * Deklaration zur R�ckkehr in das Starthaus.
	 */
	public void setHome() {
		this.position = -1;
		this.isHome = true;
	}

	public boolean isOnStartspot() { return position == this.player.startPos;}

	public Integer getStepsToWinBase () {
		return stepsToWinBase;
	}
}
