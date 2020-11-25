package game;

import java.util.ArrayList;
import java.util.Scanner;

import models.GameField;
import models.Player;
import models.Token;

public class Game{

	// properties
	private int countOfPlayers = 4;
	private int selectedPlayerIndex = 0; 
	private Player[] players = new Player[4];
	private int[] playerLocations = {0, 39, 10, 9, 20, 19, 30, 29};
	private Player selectedPlayer;
	private GameField gameField;
	private int places = 1;
	
	/**
	 * Konstruktor für das Spiel.
	 */
	public Game (int playersCount) {
		this.countOfPlayers = playersCount;
		this.init();
	
	}
	
	/**
	 * Initiale Einrichtung des Spiels.
	 */
	private void init () {
		for (int i = 0; i < countOfPlayers; i++) {
			// Initialisierung der Spieler mit Startposition (z. B. 2 * 10 = 20)
			this.players[i] = new Player(i, i * 10);
			// Spielfeld wird erstellt.
			this.gameField = new GameField(this.players);
		}
		selectedPlayer = players[selectedPlayerIndex];
		this.turn();
	}
	
	
	/**
	 * Ein neuer Spielzug (Sukzessiv für jeden Spieler)
	 */
	public void turn() {
		// deklaration
		int diceNumber = 0;
		int diceCount = 0;
		
		// zuweisung
		selectedPlayer = this.players[selectedPlayerIndex];
		
		// ausgaben - spielerblock
		println("\n\n+-+-+-+-+-+ NEUER Spielzug +-+-+-+-+-+");
		println(selectedPlayer.getId() +1 + ". Spieler: Du bist dran!");
		println("||||||||| Der AKTUELLE Stand |||||||||");
		println("+-+-+-+ Aktuelles Spielfeld +-+-+-+");
		gameField.print(playerLocations);
		this.printPlayerInformation();

		// W�rfellogik
		do {
			println("\n--- Aktion: "+ (diceCount +1) +". Würfeln (Taste dr�cken) ---");
			input();
			// kann ggf. als Klasse deklartiert werden
			diceNumber = (int) (Math.random() * 6) + 1;
			diceCount++;
			
			print("W�rfelzahl *: " + ((diceNumber == 6) ? "Hurra! Du hast eine #-6-# gewürfelt :-). Du darfst nochmal würfeln!" : "Du hast eine #-" + diceNumber + "-# gewürfelt!"));
	
			// wenn alle Figuren im Starthaus sind
			if(selectedPlayer.tokenAtHome() == 4 - (selectedPlayer.tokenInWinSpot()) && diceNumber != 6) {
				println("Hinweis: Alle Figuren sind in der Homebase und du hast keine 6 gewürfelt.");
				// wichtig es gibt die Müglichkeit drei mal zu würfeln
				if(diceCount != 3) {
					println("Hinweis: Du hast noch " + (3 - diceCount) + " Versuch(e) eine 6 zu würfeln!");
				} else {
					println("Hinweis: Leider hast Du es in deinen drei Versuchen nicht geschafft eine 6 zu würfeln :-(.");
				}
			} else {
				// aufführen der Bewegoptionen
				Token token = selectedPlayer.getTokens()[this.tokenOptionChoice(diceNumber)];
				if(token.isHome() && !token.isInWinSpot()) {
					// feindlichen Spieler entfernen
					token.out();
				}
				gameField.setTokenToField(token, diceNumber);
				
			}
			// Bedingung um nochmal zu würfeln
		}while(diceNumber == 6 || (selectedPlayer.tokenAtHome() == 4 - (selectedPlayer.tokenInWinSpot()) && diceCount != 3));
		
		
		println("\n\n\n||||||||| Deine NEUE Stand! |||||||||");
		println("+-+-+-+ Neues Spielfeld +-+-+-+");
		
		gameField.print(playerLocations);
		this.printPlayerInformation();
		
		
		// Gewinnprüfung
		checkWin();
		if(places == countOfPlayers -1) {
			println("Das Spiel ist vorbei!");
			this.printGameResults();
		} else {
			print("Spielzug wird beendet! (Taste drücken)");
			input();
			println("+-+-+-+-+-+ ENDE Spielzug +-+-+-+-+-+\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			// wenn Spielrunde vorbei ist, wird die nächste rekursiv gestartet
			
			this.nextPlayer();
			this.turn();
		}
		

		
	}
	
	/**
	 * W�hlt einen Spieler anhand eines Index und des Gewinn-Status.
	 */
	private void nextPlayer() {
		for(int i = selectedPlayerIndex +1; i <= countOfPlayers; i++) {
			if(i == countOfPlayers) {
				i = 0;
			}
			
			if(players[i].getWinningPlace() == -1) {
				selectedPlayerIndex = i;
				break;
			}
		}
		selectedPlayer = players[selectedPlayerIndex];
	}

	/**
	 * Pr�ft bei einem gewählten Spieler, ob sich alle vier Spielfiguren im Zielhaus befinden.
	 */
	private void checkWin() {
		if(selectedPlayer.tokenInWinSpot() == 4 && selectedPlayer.getWinningPlace() == -1) {
			println("Spieler " + selectedPlayer.getId() + " hat gewonnen und ist auf Platz "+ places + "!");
			selectedPlayer.setWinningPlace(places);
			places++;
			return;
		}
	}
	
	/**
	 * Die Methode gibt alle möglichen Bewegoptionen für das Würfelergebnis zurück.
	 * @param diceNumber
	 * @return Optionen zur Wahl einer Spielfigur
	 */
	private int tokenOptionChoice(int diceNumber) {
		ArrayList<Integer> fieldTokens = selectedPlayer.getFieldTokenIds(diceNumber == 6);
		
		println("\nAktion: Wähle aus einem deiner Token-Bewegoptionen: ");
		for (Integer option : fieldTokens) {
			System.out.print((option +1) + ", ");
		}
		println("Bitte Option wählen: ");
		String result = input();
		
		while(!fieldTokens.contains(parseToInt(result) -1)) {
			printRed("Bitte geben Sie eine valide Option an: ");
			result = input();
		}
		
		return parseToInt(result) -1;
	}
	
	
	/**
	 * Hilfsmethode zum geprüften Parsen von Strings zu Integern.
	 * @param value
	 * @return
	 */
	private int parseToInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			printRed("Bitte einen numerischen Wert wählen!");
		}
		return -1;
	}
	
	
	/**
	 * Gibt alle Spielergebnisse zurück. (Am Ende des Spiels)
	 */
	private void printGameResults() {
		println("-------- Ergebnisse --------");
		for (Player player : players) {
			println(player.getId() + ". Spieler - Platz " + (player.getWinningPlace() == -1 ? 4 : player.getWinningPlace()) + " - In " + player.getRoundCount() + " Spielrunden!");
		}
		println("--------- -------- ---------");
	}
	
	
	/**
	 * Gibt Spielerinformationen zum selektierten Spieler zurück.
	 */
	private void printPlayerInformation() { 
		Token[] tokens = selectedPlayer.getTokens();
		println("\n\n------- Spielerdaten " + (selectedPlayer.getId() +1) + " ------- ");
		println("Startfeld: "+ selectedPlayer.getStartPos());
		println("Starthaus: "+ selectedPlayer.tokenAtHome() + ". Spielfiguren im Starthaus");
		println("Zielfeld: "+ selectedPlayer.getWinPos());
		println("Zielhaus: "+ selectedPlayer.tokenInWinSpot() + ". Spielfiguren im Zielhaus");
		print("Positionen   ");
		for (int i = 0; i < tokens.length; i++) {
			print((i + 1) +". Figur: ( " + (tokens[i].isInWinSpot() ? "Gewonnen" :  (tokens[i].isHome() ? "Im Haus" : tokens[i].getPos())) + " )  ");
		}
		println("\n------------------");
	}
	
	/**
	 * Hilfsmethode zur Augabe in einer Zeile.
	 * @param text
	 */
	private void print(String text) {
		System.out.print(text);
	}
	
	/**
	 * Hilfsmethode zur Augabe in einer neuen Zeile.
	 * @param text
	 */
	private void println(String text) {
		System.out.println(text);
	}
	
	
	/**
	 * Hilfsmethode zur roten Augabe in einer neuen Zeile.
	 * @param text
	 */
	private void printRed(String text) {
		System.err.println(text);
	}
	
	
	/**
	 * Hilfsmethode zur Eingabe in einer Zeile.
	 * @param text
	 * @return Eingabe
	 */
	private String input() {
		Scanner scanner = new Scanner(System.in);
        String result = scanner.nextLine();
        return result;
	}
	
	/**
	 * Startmethode des Programms.
	 * @param args
	 */
	public static void main(String[] args) {
		new Game(4);
	}
	
}
