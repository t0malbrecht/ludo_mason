package models;

public class Token {
	
	// properties
	private final int id;
	private final int startPos;
	private final int winPos;
	private int pos;
	private boolean isHome;
	private boolean inWinSpot;
	// stats
	private int kickIndicator = 0;
	private int outIndicator = 0;
	private int hasKickedIndicator = 0;
	
	/**
	 * Konstruktor einer Spielerfigur.
	 * @param id: Spielerid zur Anzeige und Prüfung
	 * @param startPos: Startposition einer Spielerfigur
	 * @param winPos: Gewinnposition einer Spielerfigur
	 */
	public Token(int id, int startPos, int winPos) {
		this.id = id;
		this.pos = 0;
		this.startPos = startPos;
		this.winPos = winPos;
		this.isHome = true;
	}
	
	/**
	 * Wird vom Spielfeld entfernt.
	 */
	public void kick() {
		this.pos = -1;
		hasKickedIndicator++;
		setHome(true);
		System.out.println(id + ". Spieler: Deine Figur wurde leider in die Homebase gekickt.");
	}
	
	/**
	 * Wird auf das Spielfeld gesetzt.
	 */
	public void out() {
		outIndicator++;
		setHome(false);
		updatePos(startPos);
	}
	
	/**
	 * Spielfigur läuft in das Ziel ein.
	 */
	public void tokenWin() {
		this.inWinSpot = true;
		this.updatePos(-1);
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
	public int getPos () {
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
	
	
}
