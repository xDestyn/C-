
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class WGServer extends Thread {

	int count = 1;
	int portNumber;

	ArrayList<String> food;
	ArrayList<String> sports;
	ArrayList<String> cars;

	Random rand = new Random();

	// Server Thread.
	TheServer server;

	// Consumer for updating GUI.
	private Consumer<Serializable> callback;

	// For keeping track of clients.
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();

	// Constructor
	WGServer(Consumer<Serializable> call, String portNum) {

		food = new ArrayList<String>(Arrays.asList("taco", "curry", "salad", "steak", "sandwich", "oatmeal", "plum",
				"spaghetti", "hamburger"));
		sports = new ArrayList<String>(Arrays.asList("football", "soccer", "archery", "boxing", "cricket",
				"weightlifting", "running", "golf", "baseball"));
		cars = new ArrayList<String>(
				Arrays.asList("ford", "toyota", "hyundai", "fiat", "tesla", "kia", "buick", "bentley", "audi"));

		portNumber = Integer.valueOf(portNum);
		callback = call;

		// Initialize and start server.
		server = new TheServer();
		server.start();
	}

	public class TheServer extends Thread {

		public void run() {

			try (ServerSocket mysocket = new ServerSocket(portNumber);) {

				
				System.out.println("Server is waiting for a client!");

				// Constantly checks for new clients.
				while (true) {

					// Wait for player connections, and add to client array.
					ClientThread c = new ClientThread(mysocket.accept(), count);

					System.out.println("New Client# " + count);
					
					WordGuessInfo temp = new WordGuessInfo();
					temp.count = count;
					callback.accept(temp);
					
					// Add and start new client thread.
					clients.add(c);
					count++;
					c.start();
					

				}
			} // end of try
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class ClientThread extends Thread {

		Socket connection;
		int clientNumber;
		ObjectInputStream in;
		ObjectOutputStream out;

		WordGuessInfo clientGame;
		String currentWord;
		ArrayList<String> wordsGiven;

		// Default constructor.
		ClientThread(Socket s, int currentCount) {
			this.connection = s;
			clientNumber = currentCount;

			clientGame = new WordGuessInfo();
			wordsGiven = new ArrayList<String>();
		}

		// Selects a word from one of the 3 categories.
		public void getWord() {

			ArrayList<String> currentCategory = null;
			String wordToAdd;

			// Select first word.
			if (clientGame.currentCategory == 1) {
				currentCategory = food;
			} else if (clientGame.currentCategory == 2) {
				currentCategory = sports;
			} else if (clientGame.currentCategory == 3) {
				currentCategory = cars;
			}

			do {

				wordToAdd = currentCategory.get(rand.nextInt(9));

			} while (wordsGiven.contains(wordToAdd));

			wordsGiven.add(wordToAdd);
			currentWord = wordToAdd;
		}

		public void run() {

			try {
				// Open streams.
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			} catch (Exception e) {
				System.out.println("Streams not open");
			}

			while (true) {
				try {

					// Read in clients game.
					clientGame = (WordGuessInfo) in.readObject();
					System.out.println("Caught client in servers client thread");
					clientGame.count = this.clientNumber;
					
					// Check for new game.
					if (clientGame.newGame) {
						wordsGiven = new ArrayList<String>();
						//callback.accept(clientGame);
						clientGame.newGame = false;
					}

					// Check to see if the client is choosing new category.
					if (clientGame.choosingCategory) {
						System.out.println("CHOOSING CATEGORY");
						getWord();
						clientGame.setWord(currentWord);
						//callback.accept(clientGame);
						clientGame.choosingCategory = false;
						
						
					}
					// In a current round.
					else {
						// Evaluate
						clientGame.EvaluateGuess(currentWord);
						// Update Server Gui.
						callback.accept(clientGame);
					}

					
 
					// Send game back to client
					out.reset();
					System.out.println("Sending client back");
					out.writeObject(new WordGuessInfo(clientGame));
				} catch (ClassNotFoundException | IOException e) {
					System.out.println("Client: " + clientNumber + " has disconnected.");
					clients.remove(this);
					break;
				}
			}
		}// end of run
	}// end of client thread
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

	// Is this a new game? - boolean
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
	public void EvaluateGuess(String wordToGuess) 
	{

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

		// Set current word Guess to blanks.
		char[] tmp = wordToGuess.toCharArray();

		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = '-';
		}

		currentWordGuess = String.valueOf(tmp);
	}
}
