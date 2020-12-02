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
	public int stepsToEnemy;
	public Integer stepsToWinBase;
	public Integer canGoInWinBaseWith;
	// stats
	private int kickIndicator = 0;
	private int outIndicator = 0;
	private int hasKickedIndicator = 0;
	
	/**
	 * Konstruktor einer Spielerfigur.
	 * @param id: Spielerid zur Anzeige und Pr�fung
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
	}
	
	/**
	 * Wird vom Spielfeld entfernt.
	 */
	public void kick(Token token) {
		this.pos = -1;
		hasKickedIndicator++;
		setHome(true);
		player.game2.stats.KicksGotten[id]++;
		player.game2.stats.KicksMade[token.player.id]++;
	}
	
	/**
	 * Wird auf das Spielfeld gesetzt.
	 */
	public void out() {
		outIndicator++;
		setHome(false);
		//System.out.println("Startpos:"+startPos+"_Player:"+player.id);
		updatePos(startPos);
	}
	
	/**
	 * Spielfigur l�uft in das Ziel ein.
	 */
	public void tokenWin() {
		//System.out.println("Player: "+player.id+" | Token im Ziel");
		this.inWinSpot = true;
		this.canGoInWinBaseWith = 0;
		this.updatePos(-1);
		this.player.checkWin();
		player.game2.stats.TokensSetToWin[id]++;
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
		return pos;
	}
	
	/**
	 * Liefert die gesetzte Spieler-ID.
	 * @return: ID des Spielers
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Deklaration zur R�ckkehr oder Abkehr aus dem Starthaus.
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


}
