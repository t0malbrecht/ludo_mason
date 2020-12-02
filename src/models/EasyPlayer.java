package models;

import game.Game2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
		printTokenPosition();
		game2.round++;
		int diceCount = 0;
		int diceNumber = 0;
		while(diceNumber == 6 || diceCount < 3){
			if(game2.end)
				return;
			diceNumber = rollDice();
			if(tokenOnStartspot()){
				gameField.setTokenToField (getTokenOnStartspot(), diceNumber);
				diceCount++;
				continue;
			}
			ArrayList<Integer> avaibleOptions = getAvaiableOptions(diceNumber);
			if(avaibleOptions.contains(0) && diceNumber == 6){ //Pflicht bei 6 raussetzen
				getTokenAtHome().out();
				diceCount++;
				continue;
			}
			if(avaibleOptions.contains(2)){
				Token tempToken = null;
				try {
					int finalDiceNumber = diceNumber;
					tempToken = getFieldToken(false).stream().filter(o -> o.canGoInWinBaseWith == finalDiceNumber).findFirst().orElseThrow(IAmSureThisWillNotHappenException::new);
				} catch (IAmSureThisWillNotHappenException e) {
					e.printStackTrace();
				}
				gameField.setInWinBase(tempToken, diceNumber);
				diceCount++;
				continue;
			}
			if(avaibleOptions.contains(1)){
				ArrayList<Token> sortedTokens = avaibleTokes;
				int finalDiceNumber1 = diceNumber;
				sortedTokens.removeIf(obj -> obj.stepsToWinBase - finalDiceNumber1 < 0);
				Collections.sort(sortedTokens,
						Comparator.comparing(Token::getStepsToWinBase));
				if(sortedTokens.size() > 0 && gameField != null){
					gameField.setTokenToField(sortedTokens.get(0), diceNumber);
				}
				diceCount++;
				continue;
			}
			diceCount++;
			// Bedingung um nochmal zu w?rfeln
		}

		printTokenPosition();
	}
}
