package game;

import java.util.ArrayList;

public class GameField {

	// properties
	public Token[] gameFields = new Token[40];

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
		int oldPos = token.position;
		//System.out.println("WIN | Player: "+token.player.id+"Von: "+oldPos+" Mit "+(diceNumber-token.stepsToWinBase));
		gameFields[oldPos] = null;
		token.player.winSpots[diceNumber-token.stepsToWinBase-1] = token;
		token.setInWinspot();
	}

	public boolean isAnotherTokenFromSamePlayerOnSpot(Token token, int diceNumber){
		int oldPos = token.position;
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
		int oldPos = token.position;
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
		int oldPos = token.position;
		int newPos = oldPos + diceNumber;
		// von der letzten position auf die erste
		if (newPos > 39) {
			newPos = newPos - 40;
		}
		//System.out.println("Player: "+token.player.id+" Von: "+oldPos+" Zu "+newPos);
		if (gameFields[newPos] != null) {
				gameFields[newPos].gotKicked(token);
		}
		gameFields[oldPos] = null;
		gameFields[newPos] = token;
		token.position = newPos;


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
			if (token != null && token.player.id != myToken.player.id) {
				positions.add(token.position);
			}
		}
		return positions;
	}

	/**
	 * Gibt f�r einen Token die Schritte bis zum n�chstgelegenen gegnerischen Token an.
	 *
	 * @param myToken Token, f�r den die Schritte zum Gegner berechnet werden sollen
	 * @return Schritte zum n�chstgelegenen gegnerischen Token
	 */
	public int stepsToNearestEnemyToken(Token myToken) {
		ArrayList<Integer> positions = getEnemyPositions(myToken);
		int stepsToEnemy = 40;
		for (int position : positions) {
			if (position == -1 || position == -2) {
				continue;
			}
			else if (position > myToken.position) {
				int steps = position - myToken.position;
				if(steps < stepsToEnemy) {
					stepsToEnemy = steps;
				}
			} else {
				int steps = 40 - myToken.position - position;
				if(steps < stepsToEnemy) {
					stepsToEnemy = steps;
				}
			}
		}
		return stepsToEnemy;
	}

	/**
	 * Gibt f�r einen Token an, wie viele Schritte er vor dem direkt hinter ihm gelegenen Gegner steht.
	 *
	 * @param myToken Token, f�r den die Schritte zum Gegner berechnet werden sollen
	 * @return Schritte, die Token vor einem Gegner steht
	 */
	public int stepsInFrontOfEnemyToken(Token myToken) {
		ArrayList<Integer> positions = getEnemyPositions(myToken);
		int stepsFromEnemy = 40;
		for (int position : positions) {
			if (position == -1 || position == -2) {
				continue;
			}
			else if (myToken.position > position) {
				int steps = myToken.position - position;
				if (steps < stepsFromEnemy) {
					stepsFromEnemy = steps;
				}
			} else {
				int steps = 40 - position - myToken.position;
				if (steps < stepsFromEnemy) {
					stepsFromEnemy = steps;
				}
			}
		}
		return stepsFromEnemy;
	}

}
