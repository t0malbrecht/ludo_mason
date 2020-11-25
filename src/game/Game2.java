package game;

import models.GameField;
import models.Player;
import sim.app.pacman.*;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.field.grid.IntGrid2D;
import sim.util.Double2D;
import sim.util.MutableDouble2D;
import sim.util.TableLoader;

import java.util.List;

public class Game2 extends SimState
{
    private static final long serialVersionUID = 1;

    private int countOfPlayers = 4;
    private int selectedPlayerIndex = 0;
    private Player[] players = new Player[4];
    private int[] playerLocations = {0, 39, 10, 9, 20, 19, 30, 29};
    private Player selectedPlayer;
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

    /** Resets the scores, loads the maze, creates the fields, adds the dots and energizers, and resets the Pac and Ghosts. */
    public void start()
    {
        super.start();

        resetAgents();
    }

    public void resetAgents()
    {
        players = new Player[4];
        schedule.clear();

        // make arrays
        actions = new int[] { Agent.NOTHING , Agent.NOTHING };

        // add Player1 (Strategy)
        Player player1 = new Player(1,0 );
        players[0] = player1;

        // add Player2 (Strategy)
        Player player2 = new Player(2,10 );
        players[1] = player2;

        // add Player3 (Strategy)
        Player player3 = new Player(3,20 );
        players[2] = player3;

        // add Player4 (Strategy)
        Player player4 = new Player(4,30);
        players[3] = player4;

        resetField();
    }

    public void resetField()
    {
        gameField = new GameField(players);

        resetToken();
    }

    public void resetToken()
    {

    }

    public static void main(String[] args)
    {
        doLoop(Game2.class, args);
        System.exit(0);
    }
}

