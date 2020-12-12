package game;

import ec.util.MersenneTwisterFast;
import models.*;
import sim.engine.SimState;

public class Game2 extends SimState
{
    private static final long serialVersionUID = 1;
    public int gameCounter = 1;

    private AbstractPlayer[] players = new AbstractPlayer[4];
    public boolean end = false;
    public MersenneTwisterFast generator;

    //stats
    public static int round = 1;
    public static int[] winsOfPlayer = {0, 0, 0, 0};
    public static int[] KicksGotten = {0,0,0,0};
    public static int[] KicksMade = {0,0,0,0};
    public static int[] TokensSetToWin = {0,0,0,0};


    /** Desired actions from the user.  Presently only actions[0] used.  */
    public int[] actions;

    /** The current Round.  */

    /** Creates a PacMan simulation with the given random number seed. */
    public Game2(long seed)
    {
        super(seed);
        this.generator = new MersenneTwisterFast(seed);
    }

    public void resetGame(){
        this.end = false;
    }

    @Override
    public void start() {
            super.start();
            resetGame();
            GameField gameField = new GameField();
            //make sure you understand the different version of the scheduleOnce() und scheduleRepeating() methods (read documentation)
            //agent order is random if agents with same ordering are called at the same time
            // add Player1 (Strategy)
            AbstractPlayer player1 = new DefensiveStrategy(0, 0, gameField, this);
            players[0] = player1;

            // add Player2 (Strategy)
            AbstractPlayer player2 = new MoveFirstStrategy(1, 10, gameField, this);
            players[1] = player2;

            // add Player3 (Strategy)
            AbstractPlayer player3 = new DefensiveStrategy(2, 20, gameField, this);
            players[2] = player3;

            // add Player4 (Strategy)
            AbstractPlayer player4 = new DefensiveStrategy(3, 30, gameField, this);
            players[3] = player4;
            gameField.setPlayers(players);

            schedule.scheduleRepeating(player1, 0, 1.0);
            schedule.scheduleRepeating(player2, 1,1.0);
            schedule.scheduleRepeating(player3, 2, 1.0);
            schedule.scheduleRepeating(player4, 3, 1.0);

        }
    public void gameFinish(){
        this.end = true;
        gameCounter++;
        this.finish();
    }

    //call finish() to terminate gracefully
    @Override
    public void finish() {
        printStats();
        super.finish();
    }

    public static void printStats(){
        System.out.println("Roundlength = "+round/4/10000);
        System.out.println("Player 1:"+winsOfPlayer[0]+" Wins");
        System.out.println("Player 1_KicksGotten:"+KicksGotten[0]+" KicksMade:"+KicksMade[0]+" TokenSetToWin:"+TokensSetToWin[0]);
        System.out.println("Player 2:"+winsOfPlayer[1]+" Wins");
        System.out.println("Player 2_KicksGotten:"+KicksGotten[1]+" KicksMade:"+KicksMade[1]+" TokenSetToWin:"+TokensSetToWin[1]);
        System.out.println("Player 3:"+winsOfPlayer[2]+" Wins");
        System.out.println("Player 3_KicksGotten:"+KicksGotten[2]+" KicksMade:"+KicksMade[2]+" TokenSetToWin:"+TokensSetToWin[2]);
        System.out.println("Player 4:"+winsOfPlayer[3]+" Wins");
        System.out.println("Player 4_KicksGotten:"+KicksGotten[3]+" KicksMade:"+KicksMade[3]+" TokenSetToWin:"+TokensSetToWin[3]);
    }


    public static void main(String[] args)
    {
        doLoop(Game2.class, args);
        System.exit(0);
    }

}

