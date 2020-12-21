package strategies;

import game.AbstractPlayer;
import game.Game2;
import game.GameField;
import game.Token;
import services.IAmSureThisWillNotHappenException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MoveLastStrategy extends AbstractPlayer {

	/**
	 * Spieler-Konstruktor. Spielkennung durch Ganzzahl (1,2,3,4) mit einer berechneten St.
	 *
	 * @param identifier
	 * @param startPos
	 */
	public MoveLastStrategy(int identifier, int startPos, Game2 game2, Token[] winSpots, ArrayList<Token> tokens) {
		super(identifier, startPos, game2, winSpots, tokens);
	}

	public void turn(){
		Game2.round++;
		int diceCount = 0;
		int diceNumber = 0;
		do {
			if(game2.end)
				return;
			diceNumber = rollDice();
			if(isAnyTokenOnStartspot()){
				game2.gamefield.setTokenToField (getTokenOnStartspot(), diceNumber);
				diceCount++;
				continue;
			}
			ArrayList<Integer> avaibleOptions = getAvaiableOptions(diceNumber);
			if(avaibleOptions.contains(0) && diceNumber == 6){ //Pflicht bei 6 raussetzen
				getOneTokenAtHome().setOnField();
				diceCount++;
				continue;
			}
			if(avaibleOptions.contains(2)){
				Token tempToken = null;
				try {
					int finalDiceNumber = diceNumber;
					tempToken = getAllTokenOnField(false).stream().filter(o -> o.canGoInWinBaseWith == finalDiceNumber).findFirst().orElseThrow(IAmSureThisWillNotHappenException::new);
				} catch (IAmSureThisWillNotHappenException e) {
					e.printStackTrace();
				}
				Game2.TokensSetToWin[this.id]++;
				game2.gamefield.setInWinBase(tempToken, diceNumber);
				diceCount++;
				continue;
			}
			//Hier bewege ich nicht im WInspot da ich immer die am weitesten hinten bewegen möchte
			if(avaibleOptions.contains(1)){
				ArrayList<Token> sortedTokens = avaibleTokesOnField;
				int finalDiceNumber1 = diceNumber;
				sortedTokens.removeIf(obj -> obj.stepsToWinBase - finalDiceNumber1 < 0);
				Collections.sort(sortedTokens,
						Comparator.comparing(Token::getStepsToWinBase));
				if(sortedTokens.size() > 0 && game2.gamefield != null){
					game2.gamefield.setTokenToField(sortedTokens.get(sortedTokens.size()-1), diceNumber);
				}
				diceCount++;
				continue;
			}
			diceCount++;
			// Bedingung um nochmal zu w?rfeln
		}while(diceNumber == 6 || (this.getAmountOfTokenAtHome() == (4 - this.getAmountOfTokenInWinSpot()) && diceCount < 3));
		//System.out.println("Spieler "+this.id+"| Game "+Game2.game+"| Zug:"+Game2.zuge[this.id]);
		Game2.zuge[this.id]++;
	}
}
