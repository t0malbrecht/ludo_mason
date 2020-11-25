package models;

public class GameField {

	// properties
	private Token[] gameFields = new Token[40];
	private AbstractPlayer[] players;
	
	/**
	 * Spielfeld-Konstruktor. Es werden lediglich die Spieler benötigt.
	 */
	public GameField () {
	}

	public void setPlayers(AbstractPlayer[] players){
		this.players = players;
	}
	
	
	/**
	 * Erzeugung eines gespiegelten Spielfelds.
	 */
	public void getFieldMirror() {
		this.gameFields = new Token[40];
		
		for (AbstractPlayer player : players) {
			for(Token token : player.tokens) {
				if(!token.isHome() && !token.isInWinSpot()) {
					gameFields[token.getPos()] = token;
				}
			}
		}
	}
	
	/**
	 * Bewegen der Spielerfiguren auf ein gespiegeltes Spielfeld.
	 */
	public void setTokenToField (Token token, int diceNumber) {
		// deklaration
		int oldPos = token.getPos();
		int newPos = oldPos + diceNumber;
		
		// von der letzten position auf die erste
		if (newPos > 39) {
			newPos = newPos - 39;
		}
		
		// aufzeichnung von allen besuchten positionen
		int[] vFields = new int[diceNumber+1];
		int limitation = diceNumber+1;
		int c = 0;
		for(int i = oldPos; i <= oldPos + diceNumber; i++) {
			if (i > 39) {
				vFields[c] = (i - 39);
				i = 0;
				oldPos = c * (-1);
				
			}
			vFields[c] = i; 
			
			if(i == token.getWinPos()) {
				limitation = diceNumber - c;
				System.out.println("Deine Spielefigur ist ins Ziel eingelaufen!");
				token.tokenWin();
				break;
			}
			c++;
		}
		
		// differenz zwischen alter und neuer position
		for(int i = 0; i < limitation; i++) {
			if(gameFields[vFields[i]] != null) {
				if(gameFields[vFields[i]].getId() != token.getId()) {
					gameFields[vFields[i]].kick();
					System.out.print((token.getId() +1) + ". Spieler: Super Du hast eine gegnerische Spielerfigur gekickt!");
				}
			}
			
			
			if(i == vFields.length -1) {
				gameFields[vFields[i]] = token;
				token.updatePos(vFields[i]);
			}
		}
		
		// neues Feld generieren.
		this.getFieldMirror();
	}

	
	
	/**
	 * Ausgabe des Spielfelds.
	 */
	public void print(int[] locations) {
		System.out.println();
	
		for (Token token : gameFields) {
			if(token != null) {
				System.out.print(token.getId() + " ");
			}
			else {
				System.out.print("o ");
			}
			
		}
		
		System.out.println();
	}

	

}
