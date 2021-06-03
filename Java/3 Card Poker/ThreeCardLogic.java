import java.util.ArrayList;
import java.util.Collections;

public class ThreeCardLogic {
	
	public static int evalHand(ArrayList<Card> hand) {
		
		ArrayList<Character> suits = new ArrayList<Character>();		
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		for (int i = 0; i < 3; i++) {
			suits.add(hand.get(i).suit);
			values.add(hand.get(i).value);
		}
		
		//Sort values
		Collections.sort(values);
		
		boolean isFlush = false;
		boolean isStraight = true; 
		
		//Check for straight
		for(int i = 0; i < 2; i++) {
			
			if(values.get(i) != values.get(i+1) - 1) {
				isStraight = false;
				break;
			}
				
		}
		
		//Check for Flush
		if(suits.get(0) == suits.get(1) && suits.get(1) == suits.get(2))
			isFlush = true;
		
		
		//Same Suit/Flush
		if(isFlush) {
			if(isStraight)
				return 1; //Straight Flush
			else
				return 4; //Flush
		}
		else if(isStraight) 
			return 3;
		//Three of a kind
		else if( values.get(0) == values.get(1) && values.get(1) == values.get(2) )
			return 2;
		//Two of a kind
		else if(values.get(0) == values.get(1) || values.get(0) == values.get(2) || values.get(2) == values.get(1))
			return 5;
		//High Card
		else
			return 0;
	}
	
	public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
		
		int handResult = evalHand(hand);
		
		// High Card Condition
		if(handResult == 0)
			return 0;
		
		// Pair Condition
		else if(handResult == 5)
			return bet + bet;
		
		// Flush Condition
		else if(handResult == 4)
			return bet + (3 * bet);
		
		// Straight Condition
		else if(handResult == 3)
			return bet + (6 * bet);
		
		// Three of a kind condition
		else if(handResult == 2)
			return bet + (30 * bet);
		
		// Straight Flush Condition
		else 
			return bet + (40 * bet);
	}
	
	public static int compareHands(ArrayList<Card> dealer, ArrayList<Card> player) {
		
	
		int dealerResult = evalHand(dealer);
		int playerResult = evalHand(player);
		
		ArrayList<Integer> dealerValues = new ArrayList<Integer>();
		ArrayList<Integer> playerValues = new ArrayList<Integer>();
		
		for(int i = 0; i < 3; i++) {
			dealerValues.add(dealer.get(i).value);
			playerValues.add(player.get(i).value);
		}
		
		Collections.sort(dealerValues);
		Collections.sort(playerValues);
		
		//Check Dealers high card.
		
		//Dealer has jackhigh or less
		if(dealerValues.get(2) < 12) {
			return 0;
		}
		//Both have high cards
		else if(playerResult == dealerResult) {

			//Check the high cards
 
			//Player Wins
			if(dealerValues.get(2) < playerValues.get(2)) 
				return 2;
			//Tied
			else if(dealerValues.get(2) == playerValues.get(2)) {
				if(dealerValues.get(1) == playerValues.get(1))
					// Tie
					if(dealerValues.get(0) == playerValues.get(0))
						return 0;
				// Player Wins
					else if (dealerValues.get(0) < playerValues.get(0))
						return 2;
				// Dealer wins
					else
						return 1;
				// Player Wins
				else if(dealerValues.get(1) < playerValues.get(1))
					return 2;
				// Dealer wins
				else 
					return 1;
			}
			//Dealer Wins
			else 
				return 1;
		}
		//Player has highcard, but dealer has better.
		else if (playerResult == 0)
			return 1;
		//Dealer has highcard, but player has better.
		else if (dealerResult == 0)
			return 2;
		//Dealer has better hand.
		else if (playerResult > dealerResult) {
			return 1; //Dealer Wins
		}
		//Player has better hand.
		else if (playerResult < dealerResult) {
			return 2; //Player Wins
		}
		//Same hand.
		else {
			return 0;
		}
		
	}
}
