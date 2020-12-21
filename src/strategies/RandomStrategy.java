package strategies;

import game.AbstractPlayer;
import game.Game2;
import game.GameField;
import game.Token;
import services.IAmSureThisWillNotHappenException;

import java.util.ArrayList;
import java.util.Random;

public class RandomStrategy extends AbstractPlayer {

    public RandomStrategy(int identifier, int startPos, Game2 game2, Token[] winSpots, ArrayList<Token> tokens) {
        super(identifier, startPos, game2, winSpots, tokens);
    }

    @Override
    public void turn() {
        Game2.round++;
        int diceCount = 0; // number of
        int diceNumber = 0; // Zahl auf dem Würfel
        do {
            if (game2.end)
                return;
            diceNumber = rollDice();
            if (isAnyTokenOnStartspot()) {
                game2.gamefield.setTokenToField(getTokenOnStartspot(), diceNumber);
                diceCount++;
                continue;
            }
            ArrayList<Integer> avaibleOptions = getAvaiableOptions(diceNumber);
            if (avaibleOptions.contains(0) && diceNumber == 6) { //Pflicht bei 6 raussetzen
                getOneTokenAtHome().setOnField();
                diceCount++;
                continue;
            }
            for (int i = 0; i < avaibleOptions.size(); i++) {
                if (avaibleOptions.get(i) == 0) {
                    avaibleOptions.remove(i);
                }
            }
            if(avaibleOptions.size() > 0) {
                Integer rnd = avaibleOptions.get(new Random().nextInt(avaibleOptions.size()));
                if (rnd == 1) {
                    game2.gamefield.setTokenToField(avaibleTokesOnField.get(new Random().nextInt(avaibleTokesOnField.size())), diceNumber);
                    diceCount++;
                    continue;
                } else if (rnd == 2) {
                    Token tempToken = null;
                    try {
                        int finalDiceNumber = diceNumber;
                        tempToken = getAllTokenOnField(false).stream().filter(o -> o.canGoInWinBaseWith == finalDiceNumber).findFirst().orElseThrow(IAmSureThisWillNotHappenException::new);
                    } catch (IAmSureThisWillNotHappenException e) {
                        e.printStackTrace();
                    }
                    game2.gamefield.setInWinBase(tempToken, diceNumber);
                    Game2.TokensSetToWin[this.id]++;
                    diceCount++;
                    continue;
                } else if (rnd == 3) {
                    game2.gamefield.moveInWinBase(avaibleTokensInWinBase.get(0), diceNumber);
                    diceCount++;
                    continue;
                }
            }
            diceCount++;

        } while (diceNumber == 6 || (this.getAmountOfTokenAtHome() == (4 - this.getAmountOfTokenInWinSpot()) && diceCount < 3));
        //System.out.println("Spieler "+this.id+"| Game "+Game2.game+"| Zug:"+Game2.zuge[this.id]);
        Game2.zuge[this.id]++;
    }
}