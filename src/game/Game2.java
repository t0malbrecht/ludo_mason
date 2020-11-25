package game;

import models.AbstractPlayer;
import models.GameField;
import models.EasyPlayer;
import sim.app.pacman.*;
import sim.engine.SimState;

public class Game2 extends SimState
{
    private static final long serialVersionUID = 1;

    private int countOfPlayers = 4;
    private int selectedPlayerIndex = 0;
    private AbstractPlayer[] players = new EasyPlayer[4];
    private int[] playerLocations = {0, 39, 10, 9, 20, 19, 30, 29};
    private AbstractPlayer selectedPlayer;
    private GameField gameField;
    private int places = 1;

    /** Desired actions from the user.  Presently only actions[0] used.  */
    public int[] actions;

    /** The current Round.  */
    public int round = 1;

    /** Creates a PacMan simulation with the given random number seed. */
    public Game2(long seed)
    {
        super(seed);
    }

    @Override
    public void start() {
        super.start();
        GameField gameField = new GameField();
        //make sure you understand the different version of the scheduleOnce() und scheduleRepeating() methods (read documentation)
        //agent order is random if agents with same ordering are called at the same time
        // add Player1 (Strategy)
        AbstractPlayer player1 = new EasyPlayer(1,0 , gameField );
        players[0] = player1;

        // add Player2 (Strategy)
        AbstractPlayer player2 = new EasyPlayer(2,10, gameField );
        players[1] = player2;

        // add Player3 (Strategy)
        AbstractPlayer player3 = new EasyPlayer(3,20, gameField );
        players[2] = player3;

        // add Player4 (Strategy)
        AbstractPlayer player4 = new EasyPlayer(4,30, gameField);
        players[3] = player4;
        gameField.setPlayers(players);

        schedule.scheduleRepeating(player1, 0, 1.0);
        schedule.scheduleRepeating(player2, 1, 1.0);
        schedule.scheduleRepeating(player3, 2, 1.0);
        schedule.scheduleRepeating(player4, 3, 1.0);
    }

    //call finish() to terminate gracefully
    @Override
    public void finish() {
        super.finish();
        System.out.println("simulation finished");
    }


    public static void main(String[] args)
    {
        doLoop(Game2.class, args);
        System.exit(0);
    }


    /**
     * Check Winning Condition
     */
    private void checkWin() {
        if(selectedPlayer.tokenInWinSpot() == 4 && selectedPlayer.winningPlace == -1) {
            System.out.println("Spieler " + selectedPlayer.id + " hat gewonnen und ist auf Platz "+ places + "!");
            selectedPlayer.winningPlace = places;
            places++;
            return;
        }
    }

}

