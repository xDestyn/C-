import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

import javafx.application.Platform;

public class Client extends Thread 
{
    //Data members.
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;
    int portNumber;
    String ipAddress;
    
    //Storing clients game.
    MorraInfo clientsGame;
    
    //Consumers for manipulating glient gui.
    private Consumer<Serializable> callback;
    private Consumer<Serializable> callback2;
    private Consumer<Serializable> callback3;
    private Consumer<Serializable> callback4;
    
    //Constructor
    Client(Consumer<Serializable> call, Consumer<Serializable> call2, Consumer<Serializable> call3, Consumer<Serializable> call4, String iPAddress, String portNumber)
    {
        //Initilaize all data members.
        this.callback = call;
        this.callback2 = call2;
        this.callback3 = call3;
        this.callback4 = call4;
        this.portNumber = Integer.valueOf(portNumber);
        this.ipAddress = iPAddress;
        this.clientsGame = new MorraInfo();
    }
    
    public void run() 
    {
    	try 
        {
            //Open socket and  streams.
            socketClient = new Socket(this.ipAddress, this.portNumber);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);        
         }
         catch(Exception e){e.printStackTrace();}
        
        //Read in first flag to know how many players present.
    	MorraInfo check = new MorraInfo();
		try {
			check = (MorraInfo)in.readObject();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	//Wait till a player 2 connects.
        while(check.onlyPlayer)
        {
            try
            {
            	check = (MorraInfo)in.readObject();

            }
            catch(Exception e) { }
        }
        callback2.accept(check);
        
        
        while(true) {
                //Read in game from server.
	        	try {
					clientsGame = (MorraInfo)in.readObject();
					
					if(clientsGame.onlyPlayer) {
					    //Other player left, change scene.
		        		callback4.accept(clientsGame);
		        	}
					else {
					    //Update gui with game info.
						callback3.accept(clientsGame);
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
    }
    
    //Used to send Mora info to client thread in server.
    public void send(MorraInfo toSend)
    {
        try
        {
            out.writeObject(new MorraInfo(toSend));
            
            while (true) {
                this.clientsGame = (MorraInfo)in.readObject();
                break;
            }
        }
        catch(IOException e) {e.printStackTrace();} 
        catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
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
    

 

    
    
    
    
  



	
	

	

	

	

    
    
    
    
    
    
    
    