import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ArrayList<Card> {
	
	public Deck(){
		newDeck();
	}
	
	public void newDeck(){
		
		//Clear all cards
		clear();

		ArrayList<Character> suits = new ArrayList<Character>();
		suits.add('C');
		suits.add('D');
		suits.add('H');
		suits.add('S');
		
		for(int i = 0; i < 4; i++) {
			for(int j = 2; j < 15; j++) {
				add(new Card(suits.get(i), j));
			}
		}

		
		Collections.shuffle(this);
		
	}
}
