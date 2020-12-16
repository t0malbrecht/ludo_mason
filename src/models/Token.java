package models;

public class Token {
	
	// properties
	private final int id;
	private final int startPos;
	private final int winPos;
	private Integer pos;
	private boolean isHome;
	private boolean inWinSpot;
	public AbstractPlayer player;
	public GameField gameField; // create methods for stepsToEnemy and stepsFromEnemy
	public int stepsToEnemy;
	public int stepsFromEnemy;
	public Integer stepsToWinBase;
	public Integer canGoInWinBaseWith;
	public boolean canHitOtherToken;
	public boolean itsNearByEnemies;
	public Integer stepsToUnfriendlyToken;

	
	/**
	 * Konstruktor einer Spielerfigur.
	 * @param id: Spielerid zur Anzeige und Prüfung
	 * @param startPos: Startposition einer Spielerfigur
	 * @param winPos: Gewinnposition einer Spielerfigur
	 */
	public Token(int id, int startPos, int winPos, AbstractPlayer player) {
		this.id = id;
		this.pos = -2;
		this.startPos = startPos;
		this.winPos = winPos;
		this.isHome = true;
		this.player = player;
		this.canGoInWinBaseWith = 0;
		this.canHitOtherToken = true;
		this.itsNearByEnemies = false;
	}
	
	/**
	 * Wird vom Spielfeld entfernt.
	 */
	public void kick(Token token) {
		this.pos = -1;
		setHome(true);
		player.game2.KicksGotten[id]++;
		player.game2.KicksMade[token.player.id]++;
	}
	
	/**
	 * Wird auf das Spielfeld gesetzt.
	 */
	public void out() {
		setHome(false);
		//System.out.println("Startpos:"+startPos+"_Player:"+player.id);
		updatePos(startPos);
	}
	
	/**
	 * Spielfigur läuft in das Ziel ein.
	 */
	public void tokenWin() {
		//System.out.println("Player: "+player.id+" | Token im Ziel");
		this.inWinSpot = true;
		this.canGoInWinBaseWith = 0;
		this.updatePos(-1);
		this.player.checkWin();
	}
	
	/**
	 * Spielfigur-Position wird aktualisiert.
	 * @param newPos: Neue Position
	 */
	public void updatePos (int newPos) {
		this.pos = newPos;
	}
	
	/**
	 * Liefert die gesetzte Startposition.
	 * @return: Startposition
	 */
	public int getStartPos() {
		return startPos;
	}
	
	/**
	 * Liefert die gesetzte Gewinnposition.
	 * @return: Gewinnposition
	 */
	public int getWinPos() {
		return winPos;
	}
	
	/**
	 * Liefert die aktuelle Position der Spielfigur.
	 * @return: Aktuelle Position
	 */
	public Integer getPos () {
		return pos;
	}

	/**
	 * Liefert die aktuelle Entfernung der Spielfigur zur Winbasis.
	 * @return: Aktuelle Position
	 */
	public Integer getStepsToWinBase () {
		return stepsToWinBase;
	}
	
	/**
	 * Liefert die gesetzte Spieler-ID.
	 * @return: ID des Spielers
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Deklaration zur Rückkehr oder Abkehr aus dem Starthaus.
	 * @param value: Boolean
	 */
	public void setHome(boolean value) {
		if(!value) {
			this.updatePos(startPos);
		} else {
			this.updatePos(-1);
		}
		this.isHome = value;
	}
	
	/**
	 * Gibt an, ob die Spielerfigur sich im Starthaus befindet.
	 * @return Boolean
	 */
	public boolean isHome() {
		return isHome;
	}

	public boolean isOnStartspot() { return pos == startPos;}
	
	/**
	 * Gibt an, ob die Spielerfigur sich im Zielhaus befindet.
	 * @return Boolean
	 */
	public boolean isInWinSpot() {
		return inWinSpot;
	}

	public int getStepsToEnemy() { return stepsToEnemy; }

	public int getStepsFromEnemy() { return stepsFromEnemy; }


}
