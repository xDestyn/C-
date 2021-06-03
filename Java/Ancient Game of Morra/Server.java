import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;


public class Server{

    //Private MoraInfo game;
    int count = 1;
    int portNumber;
    
    //Latches for thread concurrency.
    CountDownLatch latch;
    CountDownLatch evalLatch;
    CountDownLatch playerLatch;
    
    //MorraInfos for clients and the server.
    MorraInfo serverGame; 
    MorraInfo client1Game;
    MorraInfo client2Game;
    
    //Arraylist for client threads
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();

    //Server, and consumer for manipulating gui.
    TheServer server;
    private Consumer<Serializable> callback;
    private Consumer<Serializable> callbackForP1;
    private Consumer<Serializable> callbackForP2;
    private Consumer<Serializable> callbackStatus;
    
    //Constructor
	Server(Consumer<Serializable> call, Consumer<Serializable> callp1, Consumer<Serializable> callp2,  Consumer<Serializable> callStat ,String portNum){
	
    portNumber = Integer.valueOf(portNum);
    
		callback = call;
		callbackForP1 = callp1;
		callbackForP2 = callp2;
		callbackStatus = callStat;
		
		//Initialize server.
		server = new TheServer();
		server.start();

		//Initialize laches.
		latch = new CountDownLatch(2);
		evalLatch = new CountDownLatch(2);
		playerLatch = new CountDownLatch(2);
	}
	
	
	public class TheServer extends Thread{

		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(portNumber);){
		    System.out.println("Server is waiting for a client!");
		  
		    //Only take in two clients(For a game of morra).
		    while(count <= 2) {
				
                //Wait for player1 connection, and add to client array.
				ClientThread c = new ClientThread(mysocket.accept(), count);
				
				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				
				c.start();
				count++;
				
			    }  
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			
		    //Decides when to evaluate game info between clients.
			while(true) {
				if(evalLatch.getCount() == 0) {
					Evaluate();
					sendBack();
					evalLatch = new CountDownLatch(2);
				}
			}
		}
	}
	
	//Sends clients update Morrainfos with update scores and data.
	public synchronized void sendBack() {
		if(evalLatch.getCount() == 0) {
			for(int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				try {
				 t.out.reset();
				 t.out.writeObject(t.clientGame);
				 System.out.println("sent it from send back");
				}
				catch(Exception e) {e.printStackTrace();}
			}
			evalLatch = new CountDownLatch(0);
		}
		
	}
	
	//Evaluates client side data.
	public synchronized void Evaluate() {
		if(evalLatch.getCount() == 0) {
			
			//Get client games.
			MorraInfo client1Game = clients.get(0).clientGame;
			MorraInfo client2Game = clients.get(1).clientGame;
			
			//Reset flags.
			client1Game.didWin = false;
			client2Game.didWin = false;
			client1Game.did2Points = false;
			client2Game.did2Points = false;
			
			//Decide who won.
			int totalHandValue = client1Game.Hand + client2Game.Hand;
			boolean p1Right = client1Game.Guess == totalHandValue;
			boolean p2Right = client2Game.Guess == totalHandValue;

			
			// no one wins but both guessed correct
			if(p1Right && p2Right) {
				callbackStatus.accept("Tie! Both Players Guessed Correctly.  No one Wins the turn.");
				callback.accept("Tie! Both Players Guessed Correctly.  No one Wins the turn.");	
			}
			// no one wins and both guessed wrong
			else if(!p1Right && !p2Right) {
				callbackStatus.accept("Both Players Guessed Incorrectly.  No one Wins.");
				callback.accept("Tie! Both Players Guessed Correctly.  No one Wins the turn.");
			}
			// p1 wins
			else if(p1Right && !p2Right) {
				client1Game.Points += 1;
				client1Game.didWin = true;
				client2Game.didWin = false;
				callbackForP1.accept(client1Game);
				String msg =  client1Game.Name + " has won the turn! ";
				
				if(client1Game.Points == 2) {
					client1Game.did2Points = true;
					client2Game.did2Points = true;
					msg += "And they have 2 points! They Win the Game!";
				}
				callback.accept(msg);
				callbackStatus.accept(msg);
				
			}
			// p2 wins
			else if(!p1Right && p2Right) {
				client2Game.Points += 1;
				client2Game.didWin = true;
				client1Game.didWin = false;
	    		callbackForP2.accept(client2Game);
	    		String msg = client2Game.Name + " has won the turn! ";
				
				if( client2Game.Points== 2) {
					client1Game.did2Points = true;
					client2Game.did2Points = true;
					msg += "And they have 2 points! They Win the Game!";
				}
				callback.accept(msg);
				callbackStatus.accept(msg);
			}
			else {
				System.out.println("ERROR IN EVALUATE, ONE OF 4 SHOULD HAVE OCCURED");
			}
			
			//Upate corresponsing client games to be sent back.
			client1Game.setOtherName(client2Game.Name);
			client1Game.setOtherPoints(client2Game.Points);
			client1Game.setOtherHand(client2Game.Hand);
			client1Game.setOtherGuess(client2Game.Guess);
			
			client2Game.setOtherName(client1Game.Name);
			client2Game.setOtherPoints(client1Game.Points);
			client2Game.setOtherHand(client1Game.Hand);
			client2Game.setOtherGuess(client1Game.Guess);
			
			//Update clients games.
			clients.get(0).clientGame = client1Game;
			clients.get(1).clientGame = client2Game;
		}
	}
	

		class ClientThread extends Thread{
			
		
			Socket connection;
			int clientNumber;
			ObjectInputStream in;
			ObjectOutputStream out;
			MorraInfo clientGame;

			
			ClientThread(Socket s, int currentCount){
				this.connection = s;
				clientNumber = currentCount;	
				clientGame = null;
			}
			
			//Used for sending data to all clients.
			public synchronized void updateClients(MorraInfo message) {
					
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
					t.out.reset();
					 t.out.writeObject(message);
					}
					catch(Exception e) {e.printStackTrace();}
				}
			}
			

			public void run(){
				
				//Open streams.
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				
				
				//Create a flag to represent a player being alone.
				MorraInfo check = new MorraInfo();
				check.onlyPlayer = true;
				
				//Send only player flag
				try {
					out.reset();
					out.writeObject(new MorraInfo(check));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//drop latch, and wait for another client to connect.
				playerLatch.countDown();
				
				try {
					playerLatch.await();
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				//Change flag.
				check.onlyPlayer = false;
				
				//Notify client that 2nd player has connected.
				try {
					out.reset();
					out.writeObject(new MorraInfo(check));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				while(true) {
					    try {
                            
                            //Read in clients game.
					    	clientGame = (MorraInfo)in.readObject();

					    	//Decides if a new game should be started.
					    	if(clientGame.updateCont){
					    	    if(clientNumber == 1){
					    	        callbackForP1.accept(clientGame);
					    	    }
					    	    else if(clientNumber == 2){
					    	        callbackForP2.accept(clientGame);
					    	    }
					    	    //Don't do any processing.
					    	    continue;
					    	}

					    	//Update data to server gui.
					    	callback.accept(clientGame.Name + ": " + " hand: " + clientGame.Hand + " guess: " + clientGame.Guess);
					    	
					    	//Decide whose client game to update.
					    	if(clientNumber == 1) {
					    		callbackForP1.accept(clientGame);
					    		evalLatch.countDown();
					    	}
					    	else if(clientNumber == 2) {
					    		callbackForP2.accept(clientGame);
					    		evalLatch.countDown();
					    	}
				    	}
					    catch(Exception e) {
					    	//Update server gui with client disconnect, and decrement number of clients.
					    	callback.accept("Client: " + clientNumber + " has disconnected.");
					    	count--;
					    	
					    	//Notify other client of disconnection.
					    	MorraInfo flag = new MorraInfo(clientGame);
					    	flag.onlyPlayer = true;
					    	clients.remove(this);
					    	updateClients(flag);
					    	break;
					    }
					}
				}//end of run
		}//end of client thread
}

//MorraInfo for storing game data.
class MorraInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    String Name;
    Integer Points;
    Integer Hand;
    Integer Guess;
    
    String OtherName;
    Integer OtherPoints;
    Integer OtherHand;
    Integer OtherGuess;
    
    //Flags for handling events.
    boolean didWin;
    boolean did2Points;
    boolean goAgain;
    boolean updateCont;
    boolean onlyPlayer;
    
    //default Constructor.
    public MorraInfo() {
        didWin = false;
        did2Points = false;
        goAgain = false;
        updateCont = false;
        onlyPlayer = false;
        
    	Points = new Integer(0); 
        Guess = new Integer(0); 
        Hand = new Integer(0); 
        Name = new String("Fresh MorraInfo");
        
        OtherPoints = new Integer(0); 
        OtherGuess = new Integer(0); 
        OtherHand = new Integer(0); 
        OtherName = new String("Fresh MorraInfo");
    }
    
    //Copy Constructor.
    public MorraInfo(MorraInfo other) {
    	onlyPlayer = other.onlyPlayer;
    	
    	Points = new Integer(other.Points);
    	Guess = new Integer(other.Guess);
    	Hand = new Integer(other.Hand);
    	Name = new String(other.Name);
    	
    	OtherPoints = new Integer(other.OtherPoints);
    	OtherGuess = new Integer(other.OtherGuess);
    	OtherHand = new Integer(other.OtherHand);
    	OtherName = new String(other.OtherName);
    }
    
    //Setters.
    public void setName(String Name){
        this.Name = new String(Name);
    }
    
    public void setPoints(Integer Points){
        this.Points = new Integer(Points);
    }
    
    public void setHand(Integer Hand){
        this.Hand = new Integer(Hand);
    }
    
    public void setGuess(Integer Guess){
        this.Guess = new Integer(Guess);
    }
    
    public void setOtherName(String OtherName){
        this.OtherName = new String(OtherName);
    }
    
    public void setOtherPoints(Integer OtherPoints){
        this.OtherPoints = new Integer(OtherPoints);
    }
    
    public void setOtherHand(Integer OtherHand){
        this.OtherHand = new Integer(OtherHand);
    }
    
    public void setOtherGuess(Integer OtherGuess){
        this.OtherGuess = new Integer(OtherGuess);
    }
    
}
    

 

    
    
    
    
  



	
	

	

	

	
