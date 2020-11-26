package models;

import game.Game2;

import java.util.ArrayList;

public class EasyPlayer extends AbstractPlayer{

	/**
	 * Spieler-Konstruktor. Spielkennung durch Ganzzahl (1,2,3,4) mit einer berechneten St.
	 *
	 * @param identifier
	 * @param startPos
	 */
	public EasyPlayer(int identifier, int startPos, GameField gameField, Game2 game2) {
		super(identifier, startPos, gameField, game2);
	}

	public void turn(){
		int diceCount = 1;
		int diceNumber;
		do {
			diceNumber = rollDice();
			if(tokenOnStartspot()){
				gameField.setTokenToField (getTokenOnStartspot(), diceNumber);
				return;
			}
			ArrayList<Integer>  avaibleOptions = getAvaiableOptions();
			if(avaibleOptions.contains(0) && diceNumber == 6){ //Pflicht bei 6 raussetzen
				getTokenAtHome().out();
				return;
			}
			if(avaibleOptions.contains(1)){
				gameField.setTokenToField (getFieldToken(false).get(0), diceNumber);
				return;
			}
			diceCount++;
			checkWin();
			// Bedingung um nochmal zu w?rfeln
		}while(diceNumber == 6 || (tokenAtHome() == 4 - (tokenInWinSpot())&& diceCount != 3));

	}
}
