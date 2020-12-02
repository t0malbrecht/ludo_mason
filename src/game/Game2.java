package game;

import ec.util.MersenneTwisterFast;
import models.AbstractPlayer;
import models.GameField;
import models.EasyPlayer;
import models.Stats;
import sim.engine.IterativeRepeat;
import sim.engine.SimState;

import java.util.ArrayList;

public class Game2 extends SimState
{
    private static final long serialVersionUID = 1;
    public int gameCounter = 1;

    private int countOfPlayers = 4;
    private int selectedPlayerIndex = 0;
    private AbstractPlayer[] players = new EasyPlayer[4];
    private int[] playerLocations = {0, 39, 10, 9, 20, 19, 30, 29};
    public int[] winsOfPlayer = {0, 0, 0, 0};
    private AbstractPlayer selectedPlayer;
    private GameField gameField;
    private int places = 1;
    public boolean end = false;
    public MersenneTwisterFast generator;
    public Stats stats;

    IterativeRepeat player1stoppable;
    IterativeRepeat player2stoppable;
    IterativeRepeat player3stoppable;
    IterativeRepeat player4stoppable;

    /** Desired actions from the user.  Presently only actions[0] used.  */
    public int[] actions;

    /** The current Round.  */
    public int round = 1;

    /** Creates a PacMan simulation with the given random number seed. */
    public Game2(long seed)
    {
        super(seed);
        this.generator = new MersenneTwisterFast(seed);
        stats = new Stats();

    }

    public void resetGame(){
        this.end = false;
    }

    @Override
    public void start() {
        for(int i=0;i<100000; i++) {
            //super.start();
            resetGame();
            GameField gameField = new GameField();
            //make sure you understand the different version of the scheduleOnce() und scheduleRepeating() methods (read documentation)
            //agent order is random if agents with same ordering are called at the same time
            // add Player1 (Strategy)
            AbstractPlayer player1 = new EasyPlayer(0, 30, gameField, this);
            players[0] = player1;

            // add Player2 (Strategy)
            AbstractPlayer player2 = new EasyPlayer(1, 0, gameField, this);
            players[1] = player2;

            // add Player3 (Strategy)
            AbstractPlayer player3 = new EasyPlayer(2, 10, gameField, this);
            players[2] = player3;

            // add Player4 (Strategy)
            AbstractPlayer player4 = new EasyPlayer(3, 20, gameField, this);
            players[3] = player4;
            gameField.setPlayers(players);

            while (!end) {
                player1.turn();
                player2.turn();
                player3.turn();
                player4.turn();
            }
        }
        printStats();


            //player1stoppable = schedule.scheduleRepeating(player1, 0, 1.0);
            //player2stoppable = schedule.scheduleRepeating(player2, 1,1.0);
            //player3stoppable = schedule.scheduleRepeating(player3, 2, 1.0);
            //player4stoppable = schedule.scheduleRepeating(player4, 3, 1.0);

            /**schedule.scheduleOnce(player1, 0);
            schedule.scheduleOnce(player2, 1);
            schedule.scheduleOnce(player3, 2);
            schedule.scheduleOnce(player4, 3);**/
    }
    public void gameFinish(){
        this.end = true;
        //this.finish();
        //gameCounter++;
        //schedule.clear();
    }

    //call finish() to terminate gracefully
    @Override
    public void finish() {
        super.finish();
    }

    public void printStats(){
        System.out.println("Roundlength = "+this.round/4/10000);
        System.out.println("Player 1:"+this.winsOfPlayer[0]+" Wins");
        System.out.println("Player 1_KicksGotten:"+this.stats.KicksGotten[0]+" KicksMade:"+this.stats.KicksMade[0]+" TokenSetToWin:"+this.stats.TokensSetToWin[0]);
        System.out.println("Player 2:"+this.winsOfPlayer[1]+" Wins");
        System.out.println("Player 2_KicksGotten:"+this.stats.KicksGotten[1]+" KicksMade:"+this.stats.KicksMade[1]+" TokenSetToWin:"+this.stats.TokensSetToWin[1]);
        System.out.println("Player 3:"+this.winsOfPlayer[2]+" Wins");
        System.out.println("Player 3_KicksGotten:"+this.stats.KicksGotten[2]+" KicksMade:"+this.stats.KicksMade[2]+" TokenSetToWin:"+this.stats.TokensSetToWin[2]);
        System.out.println("Player 4:"+this.winsOfPlayer[3]+" Wins");
        System.out.println("Player 4_KicksGotten:"+this.stats.KicksGotten[3]+" KicksMade:"+this.stats.KicksMade[3]+" TokenSetToWin:"+this.stats.TokensSetToWin[3]);
    }


    public static void main(String[] args)
    {
        new Game2((long) 165165465).start();
        //doLoop(Game2.class, args);
        //System.exit(0);
    }

}

