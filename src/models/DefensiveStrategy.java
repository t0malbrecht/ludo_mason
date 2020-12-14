package models;

import game.Game2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DefensiveStrategy extends AbstractPlayer{

    public DefensiveStrategy(int identifier, int startPos, GameField gameField, Game2 game2) {
        super(identifier, startPos, gameField, game2);
    }

    @Override
    public void turn(){
        Game2.TokensSetToWin[id]++;
        printTokenPosition();
        Game2.round++;
        int diceCount = 0; // number of
        int diceNumber = 0; // Zahl auf dem Würfel
        do {
            if(game2.end)
                return;
            diceNumber = rollDice();
            if(tokenOnStartspot()){ // if token on start spot, it has to be moved
                gameField.setTokenToField (getTokenOnStartspot(), diceNumber);
                diceCount++;
                continue;
            }
            ArrayList<Integer> avaibleOptions = getAvaiableOptions(diceNumber);
            if(avaibleOptions.contains(0) && diceNumber == 6){ // if dice shows a 6 and there are still tokens in the house, a token has to be set on starting position
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
            if(avaibleOptions.contains(3)){
                gameField.moveInWinBase(avaibleTokensMoveWinBase.get(0), diceNumber);
                diceCount++;
                continue;
            }
            if (avaibleOptions.contains(1)) {
                ArrayList<Token> tokensInDangerSaveToMove = new ArrayList<>();
                ArrayList<Token> tokensInDangerNotSaveToMove = new ArrayList<>();
                ArrayList<Token> tokensNotInDangerSaveToMove = new ArrayList<>();
                ArrayList<Token> tokensNotInDangerNotSaveToMove = new ArrayList<>();
                for (Token token : avaibleTokes) {
                    if (token.getStepsFromEnemy() < 7) {
                        if (token.getStepsFromEnemy() + diceNumber > 6 && token.getPos() + diceNumber <= token.getStepsToEnemy()) {
                            tokensInDangerSaveToMove.add(token);
                        } else {
                            tokensInDangerNotSaveToMove.add(token);
                        }
                    } else {
                        if (token.getPos() + diceNumber <= token.getStepsToEnemy()) {
                            tokensNotInDangerSaveToMove.add(token);
                        } else {
                            tokensNotInDangerNotSaveToMove.add(token);
                        }
                    }
                }
                if (tokensInDangerSaveToMove.size() > 0) {
                    Collections.sort(tokensInDangerSaveToMove, Comparator.comparing(Token::getStepsToWinBase));
                    if(gameField != null){
                        gameField.setTokenToField(tokensInDangerSaveToMove.get(0), diceNumber);
                        diceCount++;
                        continue;
                    }
                }
                else if (tokensNotInDangerSaveToMove.size() > 0) {
                    Collections.sort(tokensNotInDangerSaveToMove, Comparator.comparing(Token::getStepsToWinBase));
                    if(gameField != null){
                        gameField.setTokenToField(tokensNotInDangerSaveToMove.get(0), diceNumber);
                        diceCount++;
                        continue;
                    }
                } else if (tokensInDangerNotSaveToMove.size() > 0) {
                    Collections.sort(tokensInDangerNotSaveToMove, Comparator.comparing(Token::getStepsToWinBase));
                    if(gameField != null){
                        gameField.setTokenToField(tokensInDangerNotSaveToMove.get(0), diceNumber);
                        diceCount++;
                        continue;
                    }
                } else if (tokensNotInDangerNotSaveToMove.size() > 0) {
                    Collections.sort(tokensNotInDangerNotSaveToMove, Comparator.comparing(Token::getStepsToWinBase));
                    if(gameField != null){
                        gameField.setTokenToField(tokensNotInDangerNotSaveToMove.get(0), diceNumber);
                        diceCount++;
                        continue;
                    }
                }

            }
            diceCount++;
            // Bedingung um nochmal zu w?rfeln
        }while(diceNumber == 6 || (this.tokenAtHome() == (4 - this.tokenInWinSpot()) && diceCount < 3));

        printTokenPosition();
    }
}
