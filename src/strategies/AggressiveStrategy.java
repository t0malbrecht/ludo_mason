package strategies;

import game.AbstractPlayer;
import game.Game2;
import game.GameField;
import game.Token;
import services.IAmSureThisWillNotHappenException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AggressiveStrategy extends AbstractPlayer {

	/**
	 * Spieler-Konstruktor. Spielkennung durch Ganzzahl (1,2,3,4) mit einer berechneten St.
	 *
	 * @param identifier
	 * @param startPos
	 */
	public AggressiveStrategy(int identifier, int startPos, Game2 game2, Token[] winSpots, ArrayList<Token> tokens) {
		super(identifier, startPos, game2, winSpots, tokens);
	}

	public void turn(){
		Token tempToken = null;
		int diceCount = 0;
		int diceNumber = 0;
		do {
			if(game2.end)
				return;

			// W�rfeln
			diceNumber = rollDice();
			diceCount++;
			
			if(isAnyTokenOnStartspot()){
				game2.gamefield.setTokenToField (getTokenOnStartspot(), diceNumber);
				continue;
			}
			
			ArrayList<Integer> avaibleOptions = getAvaiableOptions(diceNumber);
			if(avaibleOptions.contains(0) && diceNumber == 6){ //Pflicht bei 6 raussetzen
				getOneTokenAtHome().setOnField();
				continue;
			}

			if(avaibleOptions.contains(3)){
				game2.gamefield.moveInWinBase(avaibleTokensInWinBase.get(0), diceNumber);
				continue;
			}
			
			// Aggressive Spielart - Sobald der Spielstein bei einem Gegner ist oder dieser maximal 5 Schritte davon entfernt ist.
			if(avaibleOptions.contains(4)) {
				tempToken = null;
				try {
					tempToken = getAllTokenOnField(false).stream().filter(o -> o.canHitOtherToken).findFirst().orElseThrow(IAmSureThisWillNotHappenException::new);
				} catch (IAmSureThisWillNotHappenException e) {
					e.printStackTrace();
				}
				setToken(tempToken, diceNumber);
				continue;
			}
			
			// Wenn der Spieler maximal 5 Schritte vor dem Gegner ist.
			if(avaibleOptions.contains(5)) {
				tempToken = null;
				try {
					tempToken = getAllTokenOnField(false).stream().filter(o -> o.isCloseToEnemies).findFirst().orElseThrow(IAmSureThisWillNotHappenException::new);
				} catch (IAmSureThisWillNotHappenException e) {
					e.printStackTrace();
				}
				setToken(tempToken, diceNumber);
				continue;
			}
			
			// Normale Strategien
			if(avaibleOptions.contains(2)){
				tempToken = null;
				try {
					int finalDiceNumber = diceNumber;
					tempToken = getAllTokenOnField(false).stream().filter(o -> o.canGoInWinBaseWith == finalDiceNumber).findFirst().orElseThrow(IAmSureThisWillNotHappenException::new);
				} catch (IAmSureThisWillNotHappenException e) {
					e.printStackTrace();
				}
				Game2.TokensSetToWin[this.id]++;
				game2.gamefield.setInWinBase(tempToken, diceNumber);
				continue;
			}
			
			if(avaibleOptions.contains(1)){
				ArrayList<Token> sortedTokens = avaibleTokesOnField;
				int finalDiceNumber1 = diceNumber;
				sortedTokens.removeIf(obj -> obj.stepsToWinBase - finalDiceNumber1 < 0);
				Collections.sort(sortedTokens,
						Comparator.comparing(Token::getStepsToWinBase));
				if(sortedTokens.size() > 0 && game2.gamefield != null){
					game2.gamefield.setTokenToField(sortedTokens.get(0), diceNumber);
				}
				continue;
			}

		}while(diceNumber == 6 || (this.getAmountOfTokenAtHome() == (4 - this.getAmountOfTokenInWinSpot()) && diceCount < 3));
		//System.out.println("Spieler "+this.id+"| Game "+Game2.game+"| Zug:"+Game2.zuge[this.id]);
		Game2.zuge[this.id]++;
	}
	
	
	
	
	private void setToken(Token token, int diceNumber) {
		if(token != null && game2.gamefield != null)
		{
			game2.gamefield.setTokenToField(token, diceNumber);
		}
	}
}
