package models;

public class GameField {

	// properties
	public Token[] gameFields = new Token[40];
	private AbstractPlayer[] players;
	
	/**
	 * Spielfeld-Konstruktor. Es werden lediglich die Spieler benötigt.
	 */
	public GameField () {
	}

	public void setPlayers(AbstractPlayer[] players){
		this.players = players;
	}
	
	
	/**
	 * Erzeugung eines gespiegelten Spielfelds.
	 */
	public void getFieldMirror() {
		this.gameFields = new Token[40];
		
		for (AbstractPlayer player : players) {
			for(Token token : player.tokens) {
				if(!token.isHome() && !token.isInWinSpot()) {
					gameFields[token.getPos()] = token;
				}
			}
		}
	}

	public void setInWinBase(Token token, int diceNumber){
		int oldPos = token.getPos();
		//System.out.println("WIN | Player: "+token.player.id+"Von: "+oldPos+" Mit "+(diceNumber-token.stepsToWinBase));
		gameFields[oldPos] = null;
		token.player.freeWinSpots.remove(Integer.valueOf(diceNumber-token.stepsToWinBase));
		token.tokenWin();
	}

	public boolean isAnotherTokenFromSamePlayerOnSpot(Token token, int diceNumber){
		int oldPos = token.getPos();
		int newPos = oldPos + diceNumber;
		// von der letzten position auf die erste
		if (newPos > 39) {
			newPos = newPos - 39;
		}

		if(gameFields[newPos] != null) {
			if (gameFields[newPos].player.id == token.player.id) {
				return true;
			}
		}
		return false;
		}

	/**
	 * Bewegen der Spielerfiguren auf ein gespiegeltes Spielfeld.
	 */
	public void setTokenToField(Token token, int diceNumber) {
		// deklaration
		int oldPos = token.getPos();
		int newPos = oldPos + diceNumber;
		// von der letzten position auf die erste
		if (newPos > 39) {
			newPos = newPos - 39;
		}
		//System.out.println("Player: "+token.player.id+" Von: "+oldPos+" Zu "+newPos);
		if (gameFields[newPos] != null) {
				gameFields[newPos].kick(token);
		}
		gameFields[oldPos] = null;
		gameFields[newPos] = token;
		token.updatePos(newPos);
		
		// neues Feld generieren.
		this.getFieldMirror();
	}



	/**
	 * Ausgabe des Spielfelds.
	 */
	public void print(int[] locations) {
		System.out.println();

		for (Token token : gameFields) {
			if(token != null) {
				System.out.print(token.getId() + " ");
			}
			else {
				System.out.print("o ");
			}

		}

		System.out.println();
	}



}
