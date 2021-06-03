import java.util.ArrayList;


public class Dealer {
	public Deck theDeck;
	public ArrayList<Card> dealersHand; //Holds Dealers hand in each game.
	
	// Initializes the deck
	public Dealer(){
		theDeck = new Deck();
	}
	
	// Method returns ArrayList<Card> containing 3 cards removed from theDeck
	public ArrayList<Card> dealHand() {
		
		// Reshuffles if Deck contains less than 34 cards 
		if (theDeck.size() <= 34) {
			theDeck.newDeck();
		}
		
		ArrayList<Card> dealtHand = new ArrayList<Card>();
		
		// Removes 3 cards from theDeck and adds it into myArray
		for(int i = 0; i < 3; i++) {
			Card cardRemoved = theDeck.remove(0);
			dealtHand.add(cardRemoved);
		}
		
		// Returns ArrayList<Card> containing 3 cards removed from theDeck
		return dealtHand;
	
	}
}
