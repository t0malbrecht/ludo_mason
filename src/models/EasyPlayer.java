package models;

import java.util.ArrayList;

public class EasyPlayer extends AbstractPlayer{

	/**
	 * Spieler-Konstruktor. Spielkennung durch Ganzzahl (1,2,3,4) mit einer berechneten St.
	 *
	 * @param identifier
	 * @param startPos
	 */
	public EasyPlayer(int identifier, int startPos, GameField gameField) {
		super(identifier, startPos, gameField);
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
			if(avaibleOptions.contains(0) && diceNumber == 6){

			}
			diceCount++;
			// Bedingung um nochmal zu w?rfeln
		}while(diceNumber == 6 || (tokenAtHome() == 4 - (tokenInWinSpot())&& diceCount != 3));

	}
}
