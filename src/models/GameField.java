package models;

import java.util.ArrayList;

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
	public void moveInWinBase(Token token, int diceNumber){
		int oldPos = 0;
		for(int i=0;i<4;i++){
			if(token.player.winSpots[i] == token){
				token.player.winSpots[i] = null;
				oldPos = i+1;
			}
		}
		token.player.winSpots[oldPos+diceNumber] = token;
	}

	public void setInWinBase(Token token, int diceNumber){
		int oldPos = token.getPos();
		//System.out.println("WIN | Player: "+token.player.id+"Von: "+oldPos+" Mit "+(diceNumber-token.stepsToWinBase));
		gameFields[oldPos] = null;
		token.player.winSpots[diceNumber-token.stepsToWinBase-1] = token;
		token.tokenWin();
	}

	public boolean isAnotherTokenFromSamePlayerOnSpot(Token token, int diceNumber){
		int oldPos = token.getPos();
		int newPos = oldPos + diceNumber;
		// von der letzten position auf die erste
		if (newPos > 39) {
			newPos = newPos - 40;
		}

		if(gameFields[newPos] != null) {
			if (gameFields[newPos].player.id == token.player.id) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isAnotherTokenFromDifferentPlayerOnSpot(Token token, int diceNumber){
		int oldPos = token.getPos();
		int newPos = oldPos + diceNumber;
		// von der letzten position auf die erste
		if (newPos > 39) {
			newPos = newPos - 40;
		}

		if(gameFields[newPos] != null) {
			if (gameFields[newPos].player.id != token.player.id) {
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
			newPos = newPos - 40;
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

	/**
	 * Gibt ein Array mit allen Tokens wieder.
	 *
	 * @return Array mit allen Tokens
	 */
	public Token[] getTokens() {
		Token[] tokens = new Token[16];
		int i = 0;
		for (Token token : gameFields) {
			if (token != null) {
				tokens[i] = token;
				i++;
			}
		}
		return tokens;
	}

	/**
	 * Gibt die Positionen der gegnerischen Spieler aus.
	 *
	 * @param myToken Token des Spielers
	 * @return ArrayList mit Positionen der gegnerischen Spieler
	 */
	public ArrayList<Integer> getEnemyPositions(Token myToken) {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (Token token : gameFields) {
			if (token != null && token.getId() != myToken.getId()) {
				positions.add(token.getPos());
			}
		}
		return positions;
	}

	/**
	 * Gibt für einen Token die Schritte bis zum nächstgelegenen gegnerischen Token an.
	 *
	 * @param myToken Token, für den die Schritte zum Gegner berechnet werden sollen
	 * @return Schritte zum nächstgelegenen gegnerischen Token
	 */
	public int stepsToNearestEnemyToken(Token myToken) {
		ArrayList<Integer> positions = getEnemyPositions(myToken);
		int stepsToEnemy = 40;
		for (int position : positions) {
			if (position == -1 || position == -2) {
				continue;
			}
			else if (position > myToken.getPos()) {
				int steps = position - myToken.getPos();
				if(steps < stepsToEnemy) {
					stepsToEnemy = steps;
				}
			} else {
				int steps = 40 - myToken.getPos() - position;
				if(steps < stepsToEnemy) {
					stepsToEnemy = steps;
				}
			}
		}
		return stepsToEnemy;
	}

	/**
	 * Gibt für einen Token an, wie viele Schritte er vor dem direkt hinter ihm gelegenen Gegner steht.
	 *
	 * @param myToken Token, für den die Schritte zum Gegner berechnet werden sollen
	 * @return Schritte, die Token vor einem Gegner steht
	 */
	public int stepsInFrontOfEnemyToken(Token myToken) {
		ArrayList<Integer> positions = getEnemyPositions(myToken);
		int stepsFromEnemy = 40;
		for (int position : positions) {
			if (position == -1 || position == -2) {
				continue;
			}
			else if (myToken.getPos() > position) {
				int steps = myToken.getPos() - position;
				if (steps < stepsFromEnemy) {
					stepsFromEnemy = steps;
				}
			} else {
				int steps = 40 - position - myToken.getPos();
				if (steps < stepsFromEnemy) {
					stepsFromEnemy = steps;
				}
			}
		}
		return stepsFromEnemy;
	}

}
