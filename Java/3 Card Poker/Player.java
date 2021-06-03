import java.util.ArrayList;

public class Player {

	public ArrayList<Card> hand;
	public int anteBet;
	public int playBet;
	public int pairPlusBet;
	public int totalWinnings;
	
	public Player() {
		//Initalize all values to zero.
		anteBet = 0;
		playBet = 0;
		pairPlusBet = 0;
		totalWinnings = 0;
	}
	
}
