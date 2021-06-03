import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class WGClient extends Thread{
	
	//Data members.
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;
    int portNumber;
    String ipAddress;
    
    
    //Stores the clients game.
    WordGuessInfo clientsGame;
    
    //For updating GUI.
    private Consumer<Serializable> callback;
    
    //Constructor
    WGClient(Consumer<Serializable> call, String ipAddress, String portNumber){
        this.callback = call;
        this.portNumber = Integer.valueOf(portNumber);
        this.clientsGame = new WordGuessInfo();
        this.ipAddress = new String(ipAddress);
    }
    
    public void run() 
    {
    	try 
        {
            //Open socket and  streams.
            socketClient = new Socket("127.0.0.1", this.portNumber);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);        
         }
         catch(Exception e){e.printStackTrace();}
        
        
        while(true) {
                //Read in game from server, and accept/update GUI.
        		try {
					clientsGame = (WordGuessInfo)in.readObject();
					callback.accept(new WordGuessInfo(clientsGame));
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
        }
    }
    
    //Used to send Game info to client thread in server.
    //Used to send Game info to client thread in server.
    public void send()
    {
        try
        {
        	out.reset();
            out.writeObject(new WordGuessInfo(clientsGame));
        }
	    catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}


//For storing game data.
class WordGuessInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	// neccessary to distinguish clients, only for gui printing purposes
	String userName;
	
	// For knowing the current category and current guess.
	int currentCategory;
	int guessNumber;

	// Keeps track of how many wins/losses
	int wins;
	int losses;

	// To know which categories are available.
	boolean category1;
	boolean category2;
	boolean category3;

	// To know if the current category is won.
	boolean wonCategory;
	boolean lostCategory;

	// To know if player chose a category.
	boolean choosingCategory;

	// Length of word that is being guesses, and word with guesses letters.
	String currentWordGuess;
	int wordLength;

	// The current guess.
	Character currentGuess;

	int categoryLosses1;
	int categoryLosses2;
	int categoryLosses3;
	
	// id number
	int count;

	// Is it a new game ? - boolean
	boolean newGame;
	
	// Did we win the round? - boolean
	boolean winRound;

	// Default Constructor.
	public WordGuessInfo() {

		currentCategory = 0;
		guessNumber = 0;

		wins = 0;
		losses = 0;

		category1 = false;
		category2 = false;
		category3 = false;

		wonCategory = false;
		lostCategory = false;
		choosingCategory = false;

		currentWordGuess = "";
		wordLength = 0;
		currentGuess = ' ';

		categoryLosses1 = 0;
		categoryLosses2 = 0;
		categoryLosses3 = 0;

		newGame = true;
		winRound = false;
		
		count = -1;
	}
	
	

	// Copy Constructor.
	public WordGuessInfo(WordGuessInfo other) {

		count = other.count;
		
		currentCategory = other.currentCategory;
		guessNumber = other.guessNumber;

		wins = other.wins;
		losses = other.losses;

		category1 = other.category1;
		category2 = other.category2;
		category3 = other.category3;

		wonCategory = other.wonCategory;
		lostCategory = other.lostCategory;
		choosingCategory = other.choosingCategory;

		currentWordGuess = other.currentWordGuess;
		wordLength = other.wordLength;
		currentGuess = other.currentGuess;

		categoryLosses1 = other.categoryLosses1;
		categoryLosses2 = other.categoryLosses2;
		categoryLosses3 = other.categoryLosses3;

		newGame = other.newGame;
		winRound = other.winRound;
	}

	// Evaluates the currentGuess.
	public void EvaluateGuess(String wordToGuess) {

		// Reset Flag.
		boolean correctGuess = false;

		// Convert to char arrays.
		char[] wtg = wordToGuess.toCharArray();
		char[] cg = currentWordGuess.toCharArray();

		for (int i = 0; i < wtg.length; i++) {

			// If current letter matches, update current word guess and flip flag.
			if (wtg[i] == currentGuess) {
				correctGuess = true;
				cg[i] = currentGuess;
			}
		}

		// If correct Guess
		if (correctGuess) {
			winRound = true;
			currentWordGuess = String.valueOf(cg);

			// If Word is fully guessed
			if (wordToGuess.equalsIgnoreCase(currentWordGuess)) {
				wonCategory = true;
				wins++;

				// Disable won category..
				if (currentCategory == 1) {
					category1 = true;
				} else if (currentCategory == 2) {
					category2 = true;
				} else if (currentCategory == 3) {
					category3 = true;
				}
			}
		}
		// Wrong Guess
		else {
			winRound = false;
			guessNumber++;
		}

		// Exhausted all guesses.
		if (guessNumber == 6) {
			losses++;

			if (currentCategory == 1) {
				categoryLosses1++;
			} else if (currentCategory == 2) {
				categoryLosses2++;
			} else if (currentCategory == 3) {
				categoryLosses3++;
			}

			lostCategory = true;
		}
	}

	// Sets the word length, and the currentWordGuess with blanks.
	public void setWord(String wordToGuess) {

		// Reset won/lost category.
		wonCategory = false;
		lostCategory = false;

		// Reset number of guesses.
		guessNumber = 0;

		// Set category and word length.
		wordLength = wordToGuess.length();

		// Set curent word Guess to blanks.
		char[] tmp = wordToGuess.toCharArray();

		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = '-';
		}

		currentWordGuess = String.valueOf(tmp);
	}
}
