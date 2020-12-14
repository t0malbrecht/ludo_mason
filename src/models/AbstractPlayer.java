package models;

import game.Game2;
import sim.engine.SimState;
import sim.engine.Steppable;
import ec.util.MersenneTwisterFast;

import java.util.ArrayList;

public abstract class AbstractPlayer implements Steppable {
    private static final long serialVersionUID = 1;
    public int id;
    public int startPos;
    public int winPos;
    public ArrayList<Token> tokens = new ArrayList<>();
    public Token[] winSpots;
    public int roundCount;
    public GameField gameField;
    public Game2 game2;
    public ArrayList<Token> avaibleTokes;
    public ArrayList<Token> avaibleTokensMoveWinBase;

    public void printTokenPosition() {
         /**System.out.print("Player: "+id+" | ");
         System.out.print("inHomeBase: "+tokenAtHome()+" | ");
         for (Token token: getFieldToken(false)) {
         System.out.print("Token: "+token.getPos()+" | ");
         }
         System.out.println("-");**/

    }

    /**
     * Spieler-Konstruktor. Spielkennung durch Ganzzahl (1,2,3,4) mit einer berechneten St.
     */
    public AbstractPlayer(int identifier, int startPos, GameField gameField, Game2 game2, Token [] winSpots, ArrayList<Token> tokens) {
        this.game2 = game2;
        this.id = identifier;
        this.startPos = startPos;
        this.winPos = (this.startPos - 1) < 0 ? 39 : this.startPos - 1;
        this.roundCount = 0;
        this.gameField = gameField;
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
        avaibleTokes = new ArrayList<>();
        avaibleTokensMoveWinBase = new ArrayList<>();
        for (Token token : tokens) {
            token.canGoInWinBaseWith = 0;
            if (token.isHome()) {
                result.add(0);
                continue;
            } else if (token.isInWinSpot()) {
                    for(int i=0;i<4;i++){
                        if(winSpots[i] == token){
                            if(i+diceNumber <3 && winSpots[i+diceNumber] == null){
                                result.add(3);
                                avaibleTokensMoveWinBase.add(token);
                            }
                        }
                    }
            } else {
                if (gameField.isAnotherTokenFromSamePlayerOnSpot(token, diceNumber)) {
                    continue;
                } else {
                    if (token.getWinPos() == token.getPos()) {
                        token.stepsToWinBase = 0;
                    } else {
                        if (token.getPos() < token.getWinPos()) {
                            token.stepsToWinBase = token.getWinPos() - token.getPos();
                        } else {
                            token.stepsToWinBase = 40 - token.getPos() + token.getWinPos();
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
                                    avaibleTokes.add(token);
                                    token.stepsFromEnemy = gameField.stepsInFrontOfEnemyToken(token);
                                    token.stepsToEnemy = gameField.stepsToNearestEnemyToken(token);
                                    result.add(1);
                                }
                            }
                            pos++;
                        }
                    } else {
                        avaibleTokes.add(token);
                        token.stepsFromEnemy = gameField.stepsInFrontOfEnemyToken(token);
                        token.stepsToEnemy = gameField.stepsToNearestEnemyToken(token);
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
            tokens.add(new Token(id, startPos, winPos, this));
        }
    }

    /**
     * Prüft, wie viele Figuren im Starthaus sind.
     *
     * @return Anzahl der Figuren.
     */
    public int tokenAtHome() {
        int count = 0;
        for (Token token : tokens) {
            count = count + (token.isHome() ? 1 : 0);
        }
        return count;
    }


    /**
     * Gibt einen Home-Token zurück
     *
     * @return Anzahl der Figuren.
     */
    public Token getTokenAtHome() {
        int count = 0;
        for (Token token : tokens) {
            if (token.isHome()) {
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
    public boolean tokenOnStartspot() {
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

    /**
     * Methode für die Statistik.
     * Inkrementierung der Spielrunden für einen Spieler.
     */
    public void addRoundCount() {
        this.roundCount++;
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
    public int tokenInWinSpot() {
        int count = 0;
        for (Token token : tokens) {
            count = count + (token.isInWinSpot() ? 1 : 0);
        }
        return count;
    }

    /**
     * Prüft, welche Spielfiguren überhaupt bewegt werden dürfen.
     *
     * @return Array der Ids.
     */
    public ArrayList<Token> getFieldToken(boolean withHomeTokens) {
        ArrayList<Token> result = new ArrayList<Token>();
        for (int i = 0; i < tokens.size(); i++) {
            if (!tokens.get(i).isInWinSpot() && (!tokens.get(i).isHome() || withHomeTokens)) {
                result.add(tokens.get(i));
            }

        }

        return result;
    }

    /**
     * Check Winning Condition
     */
    public void checkWin() {
        if (tokenInWinSpot() == 4) {
            game2.winsOfPlayer[id]++;
            game2.gameFinish();
        }
    }

    //_________________________________________________________________

    /**
     * Gibt einen "Home"-Token (Spielerfigur) zurück.
     * Setzt in diesem Zuge alle relevanten Informationen.
     *
     * @return
     */
    public Token getHomeToken() {
        for (Token token : tokens) {
            if (token.isHome()) {
                token.setHome(false);
                return token;
            }
        }
        return null;
    }
}
