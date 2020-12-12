package models;

import game.Game2;

import java.util.Random;

public class MixedStrategy extends AbstractPlayer {

    public MixedStrategy (int identifier, int startPos, GameField gameField, Game2 game2) {
        super(identifier, startPos, gameField, game2);
    }

    @Override
    public void turn(){

        int rnd = new Random().nextInt(3);

        if (rnd == 0) {
            DefensiveStrategy def = new DefensiveStrategy(id, startPos, gameField, game2);
            def.turn();
            System.out.println("Strategie: Defensive");
        }

        if (rnd == 1) {
            MoveFirstStrategy mfi = new MoveFirstStrategy(id, startPos, gameField, game2);
            mfi.turn();
            System.out.println("Strategie: Move first");
        }

        if (rnd == 2) {
            MoveLastStrategy mla = new MoveLastStrategy(id, startPos, gameField, game2);
            mla.turn();
            System.out.println("Strategie: Move last");
        }
    }
}
