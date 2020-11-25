package models;

import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.ArrayList;

public abstract class AbstractPlayer implements Steppable {
    private static final long serialVersionUID = 1;
    public int id;
    public int startPos;
    public int winPos;
    public Token[] tokens = new Token[4];
    public int roundCount;
    // pseudo-prüfung, ob ein Spieler gewonnen hat
    public int winningPlace = -1;
    public GameField gameField;

    /**
     * Spieler-Konstruktor. Spielkennung durch Ganzzahl (1,2,3,4) mit einer berechneten St.
     */
    public AbstractPlayer (int identifier, int startPos, GameField gameField) {
        this.id = identifier;
        this.startPos = startPos;
        this.winPos = (this.startPos -1) < 0 ? 39 : this.startPos -1;
        this.roundCount = 0;
        this.gameField = gameField;
        this.newTokens();
    }

    public int rollDice(){
        return (int) (Math.random() * 6) + 1;
    }

    //Options:
    //0 = Set Player out of Homebase
    //1 = Tokens can be set
    //2 = Token has to be set (stands on

    /**
     * Regel?
     * - Muss Startfeld freigemacht werden? Müssen alle Startfelder freigemacht haben oder nur das eigene
     * - Wenn 6 gewürfelt wurde, muss Token auf Spielfeld gesetzt werden oder nicht? (Also kann die 6 auch für Token auf dem Feld verwendet werden,
     *   obwohl er noch Figuren im Starthaus hat?
     */

    public ArrayList<Integer> getAvaiableOptions(){
        ArrayList<Integer> result = new ArrayList<Integer>();
        if(tokenAtHome() > 0){
            result.add(0);
        }
        if(getFieldTokenIds(false).size() > 0){
            //No FieldToken Avaible
            result.add(1);
        }
        return result;
    }

    public abstract void turn();




    /**
     * Generiert alle Spielerfiguren.
     */
    private void newTokens() {
        for(int i = 0; i < tokens.length; i++) {
            tokens[i] = new Token(id, startPos, winPos);
        }
    }

    /**
     * Prüft, wie viele Figuren im Starthaus sind.
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
     * Prüft, ob eigene Figur auf eigenem Startplatz ist
     * @return Anzahl der Figuren.
     */
    public boolean tokenOnStartspot() {
        for (Token token : tokens) {
            if(token.isOnStartspot()){
                return true;
            }
        }
        return false;
    }

    public Token getTokenOnStartspot() {
        for (Token token : tokens) {
            if(token.isOnStartspot()){
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
        turn();
        System.out.println("Player: "+this.id+" am Zug");
    }

    /**
     * Prüft, wie viele Figuren im Zielhaus sind.
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
     * @return Array der Ids.
     */
    public ArrayList<Integer> getFieldTokenIds(boolean withHomeTokens) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i = 0; i < tokens.length; i++) {
            if(!tokens[i].isInWinSpot() && (!tokens[i].isHome() || withHomeTokens)) {
                result.add(i);
            }

        }

        return result;
    }

    //_________________________________________________________________

    /**
     * Gibt einen "Home"-Token (Spielerfigur) zurück.
     * Setzt in diesem Zuge alle relevanten Informationen.
     * @return
     */
    public Token getHomeToken() {
        for (Token token : tokens) {
            if(token.isHome()) {
                token.setHome(false);
                return token;
            }
        }
        return null;
    }
}
