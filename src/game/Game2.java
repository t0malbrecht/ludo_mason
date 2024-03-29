package game;

import ec.util.MersenneTwisterFast;
import services.CSVWriter;
import services.Combinations;
import sim.engine.SimState;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Game2 extends SimState {
    private static final long serialVersionUID = 1;

    private AbstractPlayer[] players = new AbstractPlayer[4];
    public GameField gamefield = new GameField();
    public boolean end = false;
    public MersenneTwisterFast generator;

    public static int[] zuge = {0, 0, 0, 0};
    public static int game = 0;
    public static int[] winsOfPlayer = {0, 0, 0, 0};
    public static int[] KicksGotten = {0, 0, 0, 0};
    public static int[] KicksMade = {0, 0, 0, 0};
    public static int[] TokensSetToWin = {0, 0, 0, 0};
    public static ArrayList<String[]> rowItems = new ArrayList<>();
    public static ArrayList<Integer> workedGames = new ArrayList<>();
    public static int gamesPerSimulation = 10000;
    public static int rowID = 0;

    //strategy simulation run
    public static Class[] classes;

    static {
        try {
            classes = new Class[]{Class.forName("strategies.DefensiveStrategy"), Class.forName("strategies.MixedStrategy"), Class.forName("strategies.MoveFirstStrategy"), Class.forName("strategies.MoveLastStrategy"), Class.forName("strategies.RandomStrategy"), Class.forName("strategies.AggressiveStrategy")};
            //classes = new Class[]{Class.forName("strategies.DefensiveStrategy"), Class.forName("strategies.AggressiveStrategy"), Class.forName("strategies.MoveFirstStrategy"), Class.forName("strategies.AggressiveStrategy"), Class.forName("strategies.AggressiveStrategy"), Class.forName("strategies.AggressiveStrategy")};
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<int[]> combinations = Combinations.getCombinations();
    public static int strategyRound = 0;

    public Game2(long seed) {
        super(seed);
        this.generator = new MersenneTwisterFast(seed);
    }

    public void resetGame() {
        zuge = new int[] {0, 0, 0, 0};
        winsOfPlayer = new int[] {0, 0, 0, 0};
        KicksGotten = new int[] {0, 0, 0, 0};
        KicksMade = new int[] {0, 0, 0, 0};
        TokensSetToWin = new int[] {0, 0, 0, 0};
        workedGames = new ArrayList<>();
    }

    @Override
    public void start() {
        this.end = false;
        game++;
        super.start();
        GameField gameField = new GameField();

        // add Player1 (Strategy)
        AbstractPlayer player1 = null;
        AbstractPlayer player2 = null;
        AbstractPlayer player3 = null;
        AbstractPlayer player4 = null;

        try {
            Constructor constructorPlayer1 = classes[combinations.get(strategyRound)[0]].getDeclaredConstructor(int.class, int.class, Game2.class, Token[].class, ArrayList.class);
            Constructor constructorPlayer2 = classes[combinations.get(strategyRound)[1]].getDeclaredConstructor(int.class, int.class, Game2.class, Token[].class, ArrayList.class);
            Constructor constructorPlayer3 = classes[combinations.get(strategyRound)[2]].getDeclaredConstructor(int.class, int.class, Game2.class, Token[].class, ArrayList.class);
            Constructor constructorPlayer4 = classes[combinations.get(strategyRound)[3]].getDeclaredConstructor(int.class, int.class, Game2.class, Token[].class, ArrayList.class);
            player1 = (AbstractPlayer) constructorPlayer1.newInstance(0, 0, this, null, null);
            player2 = (AbstractPlayer) constructorPlayer2.newInstance(1, 10, this, null, null);
            player3 = (AbstractPlayer) constructorPlayer3.newInstance(2, 20, this, null, null);
            player4 = (AbstractPlayer) constructorPlayer4.newInstance(3, 30, this, null, null);
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

        schedule.scheduleRepeating(player1, 0, 1.0);
        schedule.scheduleRepeating(player2, 1, 1.0);
        schedule.scheduleRepeating(player3, 2, 1.0);
        schedule.scheduleRepeating(player4, 3, 1.0);

    }

    public void gameFinish() {
        this.end = true;
        if (workedGames.contains(game))
            return;
        workedGames.add(game);
        if ((game % gamesPerSimulation) == 0) {
            setRowItems();
            strategyRound++;
            System.out.println(game);
            resetGame();
            try {
                CSVWriter.print();
            } catch (IOException e) {
                e.printStackTrace();
            }
            rowItems.clear();
        }
        this.finish();
    }

    //call finish() to terminate gracefully
    @Override
    public void finish() {
        super.finish();
    }

    public void setRowItems() {
        rowItems.add(new String[]{Integer.toString(rowID), Integer.toString(strategyRound), "Player 1", classes[combinations.get(strategyRound)[0]].getName(), Integer.toString(winsOfPlayer[0]), Integer.toString(zuge[0]/gamesPerSimulation), Double.toString((double) KicksMade[0]/gamesPerSimulation), Double.toString((double) KicksGotten[0]/gamesPerSimulation), Double.toString((double) TokensSetToWin[0]/gamesPerSimulation)});
        rowID++;
        rowItems.add(new String[]{Integer.toString(rowID), Integer.toString(strategyRound), "Player 2", classes[combinations.get(strategyRound)[1]].getName(), Integer.toString(winsOfPlayer[1]), Integer.toString(zuge[1]/gamesPerSimulation), Double.toString((double) KicksMade[1]/gamesPerSimulation), Double.toString((double) KicksGotten[1]/gamesPerSimulation), Double.toString((double) TokensSetToWin[1]/gamesPerSimulation)});
        rowID++;
        rowItems.add(new String[]{Integer.toString(rowID), Integer.toString(strategyRound), "Player 3", classes[combinations.get(strategyRound)[2]].getName(), Integer.toString(winsOfPlayer[2]), Integer.toString(zuge[2]/gamesPerSimulation), Double.toString((double) KicksMade[2]/gamesPerSimulation), Double.toString((double) KicksGotten[2]/gamesPerSimulation), Double.toString((double) TokensSetToWin[2]/gamesPerSimulation)});
        rowID++;
        rowItems.add(new String[]{Integer.toString(rowID), Integer.toString(strategyRound), "Player 4", classes[combinations.get(strategyRound)[3]].getName(), Integer.toString(winsOfPlayer[3]), Integer.toString(zuge[3]/gamesPerSimulation), Double.toString((double) KicksMade[3]/gamesPerSimulation), Double.toString((double) KicksGotten[3]/gamesPerSimulation), Double.toString((double) TokensSetToWin[3]/gamesPerSimulation)});
        rowID++;
    }


    public static void printStats() {
        System.out.println("Player 1:" + winsOfPlayer[0] + " Wins");
        System.out.println("Player 1_KicksGotten:" + KicksGotten[0] + " KicksMade:" + KicksMade[0] + " TokenSetToWin:" + TokensSetToWin[0]);
        System.out.println("Player 2:" + winsOfPlayer[1] + " Wins");
        System.out.println("Player 2_KicksGotten:" + KicksGotten[1] + " KicksMade:" + KicksMade[1] + " TokenSetToWin:" + TokensSetToWin[1]);
        System.out.println("Player 3:" + winsOfPlayer[2] + " Wins");
        System.out.println("Player 3_KicksGotten:" + KicksGotten[2] + " KicksMade:" + KicksMade[2] + " TokenSetToWin:" + TokensSetToWin[2]);
        System.out.println("Player 4:" + winsOfPlayer[3] + " Wins");
        System.out.println("Player 4_KicksGotten:" + KicksGotten[3] + " KicksMade:" + KicksMade[3] + " TokenSetToWin:" + TokensSetToWin[3]);
    }


    public static void main(String[] args) {
        doLoop(Game2.class, args);
        System.exit(0);
    }

}

