package game;

import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.ArrayList;

public abstract class AbstractPlayer implements Steppable {
    private static final long serialVersionUID = 1;
    public int id;
    public int startPos;
    public int winPos;
    public ArrayList<Token> tokens = new ArrayList<>();
    public Token[] winSpots;
    public Game2 game2;
    public ArrayList<Token> avaibleTokesOnField;
    public ArrayList<Token> avaibleTokensInWinBase;

    /**
     * Spieler-Konstruktor. Spielkennung durch Ganzzahl (1,2,3,4) mit einer berechneten St.
     */
    public AbstractPlayer(int id, int startPos, Game2 game2, Token[] winSpots, ArrayList<Token> tokens) {
        this.game2 = game2;
        this.id = id;
        this.startPos = startPos;
        this.winPos = (this.startPos - 1) < 0 ? 39 : this.startPos - 1;
        if (winSpots == null && tokens == null) {
            this.newTokens();
            this.winSpots = new Token[4];
        } else {
            this.tokens = tokens;
            this.winSpots = winSpots;
        }
    }

    public int rollDice() {
        return game2.generator.nextInt(6) + 1;
    }

    //Options:
    //0 = Set Player out of Homebase
    //1 = Tokens can be set
    //2 = Token has to be set (stands on

    /**
     * Regel?
     * - Muss Startfeld freigemacht werden? Müssen alle Startfelder freigemacht haben oder nur das eigene
     * - Wenn 6 gewürfelt wurde, muss Token auf Spielfeld gesetzt werden oder nicht? (Also kann die 6 auch für Token auf dem Feld verwendet werden,
     * obwohl er noch Figuren im Starthaus hat?
     */

    //Kann Token anderen Token schlagen
    //Kann Token in Winbasis einlaufen
    //Kann Token
    public ArrayList<Integer> getAvaiableOptions(int diceNumber) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        avaibleTokesOnField = new ArrayList<>();
        avaibleTokensInWinBase = new ArrayList<>();
        for (Token token : tokens) {
            token.canGoInWinBaseWith = 0;
            token.canHitOtherToken = false;
            token.isCloseToEnemies = false;
            if (token.isHome) {
                result.add(0);
                continue;
            } else if (token.inWinSpot) {
                for (int i = 0; i < 4; i++) {
                    if (winSpots[i] == token) {
                        if (i + diceNumber < 3 && winSpots[i + diceNumber] == null) {
                            result.add(3);
                            avaibleTokensInWinBase.add(token);
                        }
                    }
                }
            } else {
                if (game2.gamefield.isAnotherTokenFromSamePlayerOnSpot(token, diceNumber)) {
                    continue;
                } else if (game2.gamefield.isAnotherTokenFromDifferentPlayerOnSpot(token, diceNumber)) {
                    token.canHitOtherToken = true;
                    result.add(4);
                } else {
                    if ((game2.gamefield.stepsInFrontOfEnemyToken(token) - diceNumber) < 5) {
                        token.isCloseToEnemies = true;
                        result.add(5);
                    }
                    if (token.player.winPos == token.position) {
                        token.stepsToWinBase = 0;
                    } else {
                        if (token.position < token.player.winPos) {
                            token.stepsToWinBase = token.player.winPos - token.position;
                        } else {
                            token.stepsToWinBase = 40 - token.position + token.player.winPos;
                        }
                    }
                    if (token.stepsToWinBase < diceNumber) {
                        int pos = 1;
                        for (Token winSpot : winSpots) {
                            if (winSpot == null) {
                                if (diceNumber - token.stepsToWinBase == pos) {
                                    //System.out.println("Player:"+id+"Steps:"+token.stepsToWinBase);
                                    token.canGoInWinBaseWith = diceNumber;
                                    result.add(2);
                                } else {
                                    avaibleTokesOnField.add(token);
                                    token.stepsFromEnemy = game2.gamefield.stepsInFrontOfEnemyToken(token);
                                    token.stepsToEnemy = game2.gamefield.stepsToNearestEnemyToken(token);
                                    result.add(1);
                                }
                            }
                            pos++;
                        }
                    } else {
                        avaibleTokesOnField.add(token);
                        token.stepsFromEnemy = game2.gamefield.stepsInFrontOfEnemyToken(token);
                        token.stepsToEnemy = game2.gamefield.stepsToNearestEnemyToken(token);
                        result.add(1);
                    }
                    //System.out.println("Player:"+id+"Pos:"+token.getPos()+" WinPos:"+ token.getWinPos()+" stepsToWinBase:"+token.stepsToWinBase);
                }
            }
        }
        return result;
    }


    public abstract void turn();


    /**
     * Generiert alle Spielerfiguren.
     */
    private void newTokens() {
        for (int i = 0; i < 4; i++) {
            tokens.add(new Token( this));
        }
    }

    /**
     * Prüft, wie viele Figuren im Starthaus sind.
     *
     * @return Anzahl der Figuren.
     */
    public int getAmountOfTokenAtHome() {
        int count = 0;
        for (Token token : tokens) {
            count = count + (token.isHome ? 1 : 0);
        }
        return count;
    }


    /**
     * Gibt einen Home-Token zurück
     *
     * @return Anzahl der Figuren.
     */
    public Token getOneTokenAtHome() {
        for (Token token : tokens) {
            if (token.isHome) {
                return token;
            }
        }
        return null;
    }

    /**
     * Prüft, ob eigene Figur auf eigenem Startplatz ist
     *
     * @return Anzahl der Figuren.
     */
    public boolean isAnyTokenOnStartspot() {
        for (Token token : tokens) {
            if (token.isOnStartspot()) {
                return true;
            }
        }
        return false;
    }

    public Token getTokenOnStartspot() {
        for (Token token : tokens) {
            if (token.isOnStartspot()) {
                return token;
            }
        }
        return null;
    }


    @Override
    public void step(SimState simState) {
        if (!game2.end) {
            turn();
        }
    }

    /**
     * Prüft, wie viele Figuren im Zielhaus sind.
     *
     * @return Anzahl der Figuren.
     */
    public int getAmountOfTokenInWinSpot() {
        int count = 0;
        for (Token token : tokens) {
            count = count + (token.inWinSpot ? 1 : 0);
        }
        return count;
    }

    /**
     * Prüft, welche Spielfiguren üdberhaupt bewegt werden dürfen.
     *
     * @return Array der Ids.
     */
    public ArrayList<Token> getAllTokenOnField(boolean withHomeTokens) {
        ArrayList<Token> result = new ArrayList<Token>();
        for (int i = 0; i < tokens.size(); i++) {
            if (!tokens.get(i).inWinSpot && (!tokens.get(i).isHome || withHomeTokens)) {
                result.add(tokens.get(i));
            }

        }

        return result;
    }

    /**
     * Check Winning Condition
     */
    public void checkWinningCondition() {
        if (getAmountOfTokenInWinSpot() == 4 && game2.end == false) {
            game2.winsOfPlayer[this.id]++;
            game2.gameFinish();
        }
    }
}
