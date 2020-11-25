package models;

import java.util.ArrayList;

public class Player {

	// properties
	private final int id; 
	private final int startPos; 
	private final int winPos; 
	private Token[] tokens = new Token[4];
	// pseudo-prüfung, ob ein Spieler gewonnen hat
	private int winningPlace = -1;
	// stats
	private int roundCount = 0;
	
	
	/**
	 * Spieler-Konstruktor. Spielkennung durch Ganzzahl (1,2,3,4) mit einer berechneten St.
	 */
	public Player (int identifier, int startPos) {
		this.id = identifier;
		this.startPos = startPos;
		this.winPos = (this.startPos -1) < 0 ? 39 : this.startPos -1;
		this.newTokens();
	}
	
	/**
	 * Generiert alle Spielerfiguren.
	 */
	private void newTokens() {
		for(int i = 0; i < tokens.length; i++) {
			tokens[i] = new Token(id, startPos, winPos);
		}
	}
	

	/**
	 * Prüft, wie viele Figuren im Starthaus sind.
	 * @return Anzahl der Figuren.
	 */
	public int tokenAtHome() {
		int count = 0;
		for (Token token : tokens) {
			count = count + (token.isHome() ? 1 : 0);
		}
		return count; 
	}
	
	/**
	 * Prüft, wie viele Figuren im Zielhaus sind.
	 * @return Anzahl der Figuren.
	 */
	public int tokenInWinSpot() {
		int count = 0;
		for (Token token : tokens) {
			count = count + (token.isInWinSpot() ? 1 : 0);
		}
		return count; 
	}
	
	/**
	 * Prüft, welche Spielfiguren überhaupt bewegt werden dürfen.
	 * @return Array der Ids.
	 */
	public ArrayList<Integer> getFieldTokenIds(boolean withHomeTokens) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i = 0; i < tokens.length; i++) {
			if(!tokens[i].isInWinSpot() && (!tokens[i].isHome() || withHomeTokens)) {
				result.add(i);
			}
			
		}
		
		return result;
	}
	
	/**
	 * Gibt einen "Home"-Token (Spielerfigur) zurück.
	 * Setzt in diesem Zuge alle relevanten Informationen.
	 * @return
	 */
	public Token getHomeToken() {
		for (Token token : tokens) {
			if(token.isHome()) {
				token.setHome(false);
				return token;
			}
		}
		return null;
	}
	
	/**
	 * Methode für die Statistik.
	 * Inkrementierung der Spielrunden für einen Spieler.
	 */
	public void addRoundCount() {
		this.roundCount++;
	}
	
	/**
	 * Gibt die Spielerrunden zurück.
	 * @return Spielerrunden (Integer)
	 */
	public int getRoundCount() {
		return this.roundCount;
	}
	
	/**
	 * Gibt alle Spielfiguren vom Spieler zurück.
	 * @return Array von Spielfiguren.
	 */
	public Token[] getTokens() {
		return tokens;
	}
	
	/**
	 * Gibt die Indentifikation des Spielers zurück.
	 * @return ID (Integer)
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gibt die gesetzte Gewinnposition zurück.
	 * @return Gewinnposition (Integer)
	 */
	public int getWinPos() {
		return this.winPos;
	}
	/**
	 * Gibt die gesetzte Startposition zurück.
	 * @return Startposition (Integer)
	 */
	public int getStartPos() {
		return this.startPos;
	}
	
	/**
	 * Setzt den Platz für die Siegerehrung.
	 * @param value: Platz des Spielers.
	 */
	public void setWinningPlace(int value) {
		this.winningPlace = value;
	}
	
	/**
	 * Gibt den Platz für die Siegerehrung zurück.
	 * @return Integer (ID)
	 */
	public int getWinningPlace() {
		return this.winningPlace;
	}
	
}
