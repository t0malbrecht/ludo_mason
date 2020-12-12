package models;

import game.Game2;

import java.util.ArrayList;
import java.util.Random;

public class RandomStrategy extends AbstractPlayer {

    public RandomStrategy(int identifier, int startPos, GameField gameField, Game2 game2) {
        super(identifier, startPos, gameField, game2);
    }

    @Override
    public void turn() {
        Game2.TokensSetToWin[id]++;
        printTokenPosition();
        Game2.round++;
        int diceCount = 0; // number of
        int diceNumber = 0; // Zahl auf dem Würfel
        do {
            if (game2.end)
                return;
            diceNumber = rollDice();
            if (tokenOnStartspot()) {
                gameField.setTokenToField(getTokenOnStartspot(), diceNumber);
                diceCount++;
                continue;
            }
            ArrayList<Integer> avaibleOptions = getAvaiableOptions(diceNumber);
            if (avaibleOptions.contains(0) && diceNumber == 6) { //Pflicht bei 6 raussetzen
                getTokenAtHome().out();
                diceCount++;
                continue;
            } else {
                for (int i=0; i<avaibleOptions.size(); i++) {
                    if (avaibleOptions.get(i) == 0) {
                        avaibleOptions.remove(i);
                    }
                }
                Integer rnd = avaibleOptions.get(new Random().nextInt(avaibleOptions.size()));
                System.out.println(avaibleOptions);
                System.out.println(rnd);

                if (rnd == 1 ) {
                    ArrayList<Token> aTokens = avaibleTokes;
                    System.out.println(aTokens);
                    gameField.setTokenToField(aTokens.get(new Random().nextInt(aTokens.size())), diceNumber);
                    diceCount++;
                    continue;
                }
                else if (rnd == 2) {
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
                else if (rnd == 3) {
                    gameField.moveInWinBase(avaibleTokensMoveWinBase.get(0), diceNumber);
                    diceCount++;
                    continue;
                }
                else if (rnd == 4) {
                    ArrayList<Token> aTokens = avaibleTokes;
                    System.out.println(aTokens);
                    gameField.setTokenToField(aTokens.get(new Random().nextInt(aTokens.size())), diceNumber);
                    diceCount++;
                    continue;

                }
            }
            diceCount++;

        } while (diceNumber == 6 || (this.tokenAtHome() == (4 - this.tokenInWinSpot()) && diceCount < 3));

        printTokenPosition();
    }
}