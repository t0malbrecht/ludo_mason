package game;

import ec.util.MersenneTwisterFast;
import models.*;
import services.CSVWriter;
import services.Combinations;
import sim.engine.SimState;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Game2 extends SimState
{
    private static final long serialVersionUID = 1;
    public int gameCounter = 1;

    private AbstractPlayer[] players = new AbstractPlayer[4];
    public boolean end = false;
    public MersenneTwisterFast generator;

    //stats
    public static int round = 1;
    public static int game = 0;
    public static int[] winsOfPlayer = {0, 0, 0, 0};
    public static int[] KicksGotten = {0,0,0,0};
    public static int[] KicksMade = {0,0,0,0};
    public static int[] TokensSetToWin = {0,0,0,0};
    public static ArrayList<String[]> rowItems = new ArrayList<>();

    //strategy simulation run
    public static Class[] classes;
    static {
        try {
            classes = new Class[]{Class.forName("models.DefensiveStrategy"), Class.forName("models.MixedStrategy"), Class.forName("models.MoveFirstStrategy"), Class.forName("models.MoveLastStrategy"), Class.forName("models.RandomStrategy")};
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<int[]> combinations = Combinations.getCombinations();
    public static int strategyround = 0;



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
        game++;
        super.start();
        resetGame();
        GameField gameField = new GameField();

        // add Player1 (Strategy)
        AbstractPlayer player1 = null;
        AbstractPlayer player2 = null;
        AbstractPlayer player3 = null;
        AbstractPlayer player4 = null;

        try {
            Constructor constructorPlayer1 = classes[combinations.get(strategyround)[0]].getDeclaredConstructor(int.class, int.class, GameField.class, Game2.class, Token[].class, ArrayList.class);
            Constructor constructorPlayer2 = classes[combinations.get(strategyround)[1]].getDeclaredConstructor(int.class, int.class, GameField.class, Game2.class, Token[].class, ArrayList.class);
            Constructor constructorPlayer3 = classes[combinations.get(strategyround)[2]].getDeclaredConstructor(int.class, int.class, GameField.class, Game2.class, Token[].class, ArrayList.class);
            Constructor constructorPlayer4 = classes[combinations.get(strategyround)[3]].getDeclaredConstructor(int.class, int.class, GameField.class, Game2.class, Token[].class, ArrayList.class);
            player1 = (AbstractPlayer) constructorPlayer1.newInstance(0, 0, gameField, this, null, null);
            player2 = (AbstractPlayer) constructorPlayer2.newInstance(1, 10, gameField, this, null, null);
            player3 = (AbstractPlayer) constructorPlayer3.newInstance(2, 20, gameField, this, null, null);
            player4 = (AbstractPlayer) constructorPlayer4.newInstance(3, 30, gameField, this, null, null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        players[0] = player1;
        players[1] = player2;
        players[2] = player3;
        players[3] = player4;
        gameField.setPlayers(players);

        schedule.scheduleRepeating(player1, 0, 1.0);
        schedule.scheduleRepeating(player2, 1,1.0);
        schedule.scheduleRepeating(player3, 2, 1.0);
        schedule.scheduleRepeating(player4, 3, 1.0);

        }
    public void gameFinish(){
        if((game % 10) == 0){
            setRowItems();
            strategyround++;
            //printStats();
        }
        if(game == 6250){
            try {
                CSVWriter.print();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.end = true;
        gameCounter++;
        this.finish();
    }

    //call finish() to terminate gracefully
    @Override
    public void finish() {
        super.finish();
    }

    public static void setRowItems(){
       rowItems.add(new String[] {Integer.toString(strategyround),"Player 1", classes[combinations.get(strategyround)[0]].getName(), Integer.toString(winsOfPlayer[0]), Integer.toString(round), Integer.toString(KicksMade[0]), Integer.toString(KicksGotten[0]), Integer.toString(TokensSetToWin[0])});
        rowItems.add(new String[] {Integer.toString(strategyround),"Player 2", classes[combinations.get(strategyround)[1]].getName(), Integer.toString(winsOfPlayer[1]), Integer.toString(round), Integer.toString(KicksMade[1]), Integer.toString(KicksGotten[1]), Integer.toString(TokensSetToWin[1])});
        rowItems.add(new String[] {Integer.toString(strategyround),"Player 3", classes[combinations.get(strategyround)[2]].getName(), Integer.toString(winsOfPlayer[2]), Integer.toString(round), Integer.toString(KicksMade[2]), Integer.toString(KicksGotten[2]), Integer.toString(TokensSetToWin[2])});
        rowItems.add(new String[] {Integer.toString(strategyround),"Player 4", classes[combinations.get(strategyround)[3]].getName(), Integer.toString(winsOfPlayer[3]), Integer.toString(round), Integer.toString(KicksMade[3]), Integer.toString(KicksGotten[3]), Integer.toString(TokensSetToWin[3])});
       }

    public static void printStats(){
        System.out.println("Roundlength = "+round/4/1000);
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

