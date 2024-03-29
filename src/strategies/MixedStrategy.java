package strategies;

import game.AbstractPlayer;
import game.Game2;
import game.GameField;
import game.Token;

import java.util.ArrayList;
import java.util.Random;

public class MixedStrategy extends AbstractPlayer {

    public ArrayList<Token> tokens = new ArrayList<>();
    public Token[] winSpots;

    public MixedStrategy(int identifier, int startPos, Game2 game2, Token [] winSpots, ArrayList<Token> tokens) {
        super(identifier, startPos, game2, winSpots, tokens);
    }

    @Override
    public void turn() {
        for (int i = 0; i < super.tokens.size(); i++) {
            Token t = super.tokens.get(i);
            this.tokens.add(t);
        }

        this.winSpots = super.winSpots;

        int rnd = new Random().nextInt(5);

        if (rnd == 0) {
            DefensiveStrategy def = new DefensiveStrategy(this.id, this.startPos, this.game2, this.winSpots, this.tokens);
            def.turn();
        }

        if (rnd == 1) {
            MoveFirstStrategy mfi = new MoveFirstStrategy(this.id, this.startPos, this.game2, this.winSpots, this.tokens);
            mfi.turn();
        }

        if (rnd == 2) {
            MoveLastStrategy mla = new MoveLastStrategy(this.id, this.startPos, this.game2, this.winSpots, this.tokens);
            mla.turn();
        }
        
        if (rnd == 3) {
            AggressiveStrategy agg = new AggressiveStrategy(this.id, this.startPos, this.game2, this.winSpots, this.tokens);
            agg.turn();
        }

        if (rnd == 4) {
            RandomStrategy ran = new RandomStrategy(this.id, this.startPos, this.game2, this.winSpots, this.tokens);
            ran.turn();
        }
    }
}