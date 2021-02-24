package strategies;

import game.AbstractPlayer;
import game.Game2;
import game.GameField;
import game.Token;
import services.IAmSureThisWillNotHappenException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DefensiveStrategy extends AbstractPlayer {

    public DefensiveStrategy(int identifier, int startPos, Game2 game2, Token[] winSpots, ArrayList<Token> tokens) {
        super(identifier, startPos, game2, winSpots, tokens);
    }

    @Override
    public void turn(){
        int diceCount = 0; // number of
        int diceNumber = 0; // Zahl auf dem Würfel
        do {
            if(game2.end)
                return;
            diceNumber = rollDice();
            if(isAnyTokenOnStartspot()){ // if token on start spot, it has to be moved
                game2.gamefield.setTokenToField (getTokenOnStartspot(), diceNumber);
                diceCount++;
                continue;
            }
            ArrayList<Integer> avaibleOptions = getAvaiableOptions(diceNumber);
            if(avaibleOptions.contains(0) && diceNumber == 6){ // if dice shows a 6 and there are still tokens in the house, a token has to be set on starting position
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
            if(avaibleOptions.contains(3)){
                game2.gamefield.moveInWinBase(avaibleTokensInWinBase.get(0), diceNumber);
                diceCount++;
                continue;
            }
            if (avaibleOptions.contains(1)) {
                ArrayList<Token> tokensInDangerSaveToMove = new ArrayList<>();
                ArrayList<Token> tokensInDangerNotSaveToMove = new ArrayList<>();
                ArrayList<Token> tokensNotInDangerSaveToMove = new ArrayList<>();
                ArrayList<Token> tokensNotInDangerNotSaveToMove = new ArrayList<>();
                for (Token token : avaibleTokesOnField) {
                    if (token.stepsFromEnemy < 7) {
                        if (token.stepsFromEnemy + diceNumber > 6 && token.position + diceNumber <= token.stepsToEnemy) {
                            tokensInDangerSaveToMove.add(token);
                        } else {
                            tokensInDangerNotSaveToMove.add(token);
                        }
                    } else {
                        if (token.position + diceNumber <= token.stepsToEnemy) {
                            tokensNotInDangerSaveToMove.add(token);
                        } else {
                            tokensNotInDangerNotSaveToMove.add(token);
                        }
                    }
                }
                if (tokensInDangerSaveToMove.size() > 0) {
                    Collections.sort(tokensInDangerSaveToMove, Comparator.comparing(Token::getStepsToWinBase));
                    if(game2.gamefield != null){
                        game2.gamefield.setTokenToField(tokensInDangerSaveToMove.get(0), diceNumber);
                        diceCount++;
                        continue;
                    }
                }
                else if (tokensNotInDangerSaveToMove.size() > 0) {
                    Collections.sort(tokensNotInDangerSaveToMove, Comparator.comparing(Token::getStepsToWinBase));
                    if(game2.gamefield != null){
                        game2.gamefield.setTokenToField(tokensNotInDangerSaveToMove.get(0), diceNumber);
                        diceCount++;
                        continue;
                    }
                } else if (tokensInDangerNotSaveToMove.size() > 0) {
                    Collections.sort(tokensInDangerNotSaveToMove, Comparator.comparing(Token::getStepsToWinBase));
                    if(game2.gamefield != null){
                        game2.gamefield.setTokenToField(tokensInDangerNotSaveToMove.get(0), diceNumber);
                        diceCount++;
                        continue;
                    }
                } else if (tokensNotInDangerNotSaveToMove.size() > 0) {
                    Collections.sort(tokensNotInDangerNotSaveToMove, Comparator.comparing(Token::getStepsToWinBase));
                    if(game2.gamefield != null){
                        game2.gamefield.setTokenToField(tokensNotInDangerNotSaveToMove.get(0), diceNumber);
                        diceCount++;
                        continue;
                    }
                }

            }
            diceCount++;
            // Bedingung um nochmal zu w?rfeln
        }while(diceNumber == 6 || (this.getAmountOfTokenAtHome() == (4 - this.getAmountOfTokenInWinSpot()) && diceCount < 3));
        //System.out.println("Spieler "+this.id+"| Game "+Game2.game+"| Zug:"+Game2.zuge[this.id]);
        Game2.zuge[this.id]++;;
    }
}
