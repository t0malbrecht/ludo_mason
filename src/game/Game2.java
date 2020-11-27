package game;

import models.AbstractPlayer;
import models.GameField;
import models.EasyPlayer;
import sim.engine.IterativeRepeat;
import sim.engine.SimState;

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
            AbstractPlayer player1 = new EasyPlayer(0, 0, gameField, this);
            players[0] = player1;

            // add Player2 (Strategy)
            AbstractPlayer player2 = new EasyPlayer(1, 10, gameField, this);
            players[1] = player2;

            // add Player3 (Strategy)
            AbstractPlayer player3 = new EasyPlayer(2, 20, gameField, this);
            players[2] = player3;

            // add Player4 (Strategy)
            AbstractPlayer player4 = new EasyPlayer(3, 30, gameField, this);
            players[3] = player4;
            gameField.setPlayers(players);

            player1stoppable = schedule.scheduleRepeating(player1, 0, 1.0);
            player2stoppable = schedule.scheduleRepeating(player2, 1, 1.0);
            player3stoppable = schedule.scheduleRepeating(player3, 2, 1.0);
            player4stoppable = schedule.scheduleRepeating(player4, 3, 1.0);

            /**schedule.scheduleOnce(player1, 0);
            schedule.scheduleOnce(player2, 1);
            schedule.scheduleOnce(player3, 2);
            schedule.scheduleOnce(player4, 3);**/
    }
    public void gameFinish(){
        this.end = true;
        this.finish();
        gameCounter++;
        player1stoppable.stop();
        player2stoppable.stop();
        player3stoppable.stop();
        player4stoppable.stop();
        schedule.clear();
        if(gameCounter < 1000000)
            start();
        else
            this.printStats();
    }

    //call finish() to terminate gracefully
    @Override
    public void finish() {
        super.finish();
    }

    public void printStats(){
        System.out.println("Roundlength = "+this.round/4/1000000);
        System.out.println("Player 1:"+this.winsOfPlayer[0]+" Wins");
        System.out.println("Player 3:"+this.winsOfPlayer[1]+" Wins");
        System.out.println("Player 2:"+this.winsOfPlayer[2]+" Wins");
        System.out.println("Player 4:"+this.winsOfPlayer[3]+" Wins");
    }


    public static void main(String[] args)
    {
        doLoop(Game2.class, args);
        System.exit(0);
    }

}

