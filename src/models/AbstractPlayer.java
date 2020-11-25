package models;

import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.ArrayList;

public class AbstractPlayer implements Steppable {
    private static final long serialVersionUID = 1;
    public int id;
    public int startPos;
    public int winPos;
    public Token[] tokens = new Token[4];
    public int roundCount;
    // pseudo-prüfung, ob ein Spieler gewonnen hat
    public int winningPlace = -1;

    /**
     * Spieler-Konstruktor. Spielkennung durch Ganzzahl (1,2,3,4) mit einer berechneten St.
     */
    public AbstractPlayer (int identifier, int startPos) {
        this.id = identifier;
        this.startPos = startPos;
        this.winPos = (this.startPos -1) < 0 ? 39 : this.startPos -1;
        this.roundCount = 0;
        this.newTokens();
    }

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
     * Methode für die Statistik.
     * Inkrementierung der Spielrunden für einen Spieler.
     */
    public void addRoundCount() {
        this.roundCount++;
    }


    @Override
    public void step(SimState simState) {
        System.out.println("Player: "+this.id+" am Zug");
    }



    //_________________________________________________________________

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
