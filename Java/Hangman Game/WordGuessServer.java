import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WordGuessServer extends Application {

	WGServer serverConnection;
	
    TextField status;  // display status like who won turn
    Text numberOfClients;
    


    VBox startS; // Vertical box for intro scene in server
    Text portInput;  // display portNo
    TextField portEntry; // textfield for eneter port
    TextField errorPortEntry;  // textfield to display errors to use
    Button startServer; 
    HashMap<String, Scene> sceneMap;  // hash map of scenes for easy changing
	ListView<String> listItems, listItems2;  // listviews to display game state
	Scene scene;

    Pane startingPane;  // Pane to use that is modified throught the game
    
    // function to manage initialization of textfields and set triggers
    public void initTextFields()
    {
         
        	
        portEntry = new TextField();
        portEntry.setPrefWidth(75);
        portEntry.relocate(100, portEntry.getLayoutY());
        
        errorPortEntry = new TextField();
        errorPortEntry.setText("Set port number and press start server");
        errorPortEntry.setDisable(true);
        errorPortEntry.setOpacity(100);
        errorPortEntry.relocate(errorPortEntry.getLayoutX(), 40);
        errorPortEntry.setPrefWidth(300);
        
        portInput = new Text ("Enter port number");
        portInput.relocate(180, portEntry.getLayoutY());
       
    }
        
    
    
    // function to initialize the various gui labels
    public void initLabels() 
    {
        
    	listItems.relocate(275, 60);
    	listItems.setPrefWidth(360);
    	listItems.setPrefHeight(480);
    
        
    }
    
    // function to help in scene management, in particular return startinfo scene
    public void initScenes()
    {
        startingPane = new Pane();
        startingPane.getChildren().addAll(portInput, startServer, portEntry, errorPortEntry);
        sceneMap = new HashMap<String, Scene>();
        
        sceneMap.put("server", createServerGui());
        
        scene = new Scene(startingPane, 600, 600);
    }
    
    // function to add all labels, textfields, etc to scene graph
    public Scene createServerGui() {
        
    	Pane pane = new Pane();
    	pane.setStyle("-fx-background-color: coral");
    	pane.getChildren().addAll(listItems);
    		

        return new Scene(pane, 900, 600);
    	
    }
   
    // Updates our Server GUI based on responses received by the client 
    public synchronized void  updateServerGUI(WordGuessInfo current) 
    {
    
    	// new game introduce player
    	if(current.newGame) {
    		listItems.getItems().add("New Client: P" + current.count+ " has entered the game!");
    		listItems.getItems().add("-----------------------------------------------------------------------");
    		return;
    	}
    
    	// get category Name
    	String categoryName;
    	if(current.currentCategory == 1) {
    		categoryName = "food";
		}
		else if(current.currentCategory == 2) {
			categoryName = "sports";
		}
		else  {
			categoryName = "cars";
		}
		
		listItems.getItems().add("Client: P" + current.count + " has chosen category: " + categoryName);
    	listItems.getItems().add("The form of their word is: " + current.currentWordGuess);
    	
    	// if guess is wrong
    	if(!current.winRound) {
    		listItems.getItems().add("Client: P" + current.count + "has incorrectly guessed: " + current.currentGuess);
    		//listItems.getItems().add("The form of their word is: " + current.currentWordGuess);
    		listItems.getItems().add("They have " + (6 - current.guessNumber) + " guesses remaining.");
    		
    		if(current.lostCategory) {
    			if(current.categoryLosses1 == 3 || current.categoryLosses2 == 3 || current.categoryLosses3 == 3) {
    				listItems.getItems().add("THEY LOST 3 TIMES IN A CATEGORY! CLIENT: P" + current.count + " LOSES THE GAME!");
    			}
    			else {
    				listItems.getItems().add("Oh No! They ran out of Guesses and still didnt get it, Must Pick Another Category");
    				listItems.getItems().add("Score: " + current.wins + " Total Wins and " + current.losses + "Total Losses" );
    				
    				listItems.getItems().add(current.categoryLosses1 + " losses in category: food");
    				listItems.getItems().add(current.categoryLosses2 + " losses in category: sports");
    				listItems.getItems().add(current.categoryLosses3 + " losses in category: cars");

    				if(current.category1) {
    					listItems.getItems().add("They have won category: food");
    				}
    				else {
    					listItems.getItems().add("They have not yet category: food");
    				}
    				
    				if(current.category2) {
    					listItems.getItems().add("They have won category: sports");
    				}
    				else {
    					listItems.getItems().add("They have not yet won category: sports");
    				}
    				
    				if(current.category3) {
    					listItems.getItems().add("They have won category: cars");
    				}
    				else {
    					listItems.getItems().add("They have not yet won category: cars");
    				}
    			}
    		}
    		
    		listItems.getItems().add("-----------------------------------------------------------------------");
    	}
    	// if guess is correct
    	else if(current.winRound) {
    		listItems.getItems().add("Client: P" + current.count + "has CORRECTLY guessed: " + current.currentGuess);
    		//listItems.getItems().add("The form of their word is: " + current.currentWordGuess);
    		listItems.getItems().add("They have " + (6 - current.guessNumber) + " guesses remaining.");
    		
    		if(current.wonCategory) {
    			listItems.getItems().add("They have won category: " + categoryName);
    		
    			if(current.category1  && current.category2 && current.category3) {
    				listItems.getItems().add("THEY WON IN EACH CATEGORY! CLIENT: P" + current.count + " WINS THE GAME!");
    			}
    			else {
    				listItems.getItems().add("Score: " + current.wins + " Total Wins and " + current.losses + "Total Losses" );
    				
    				listItems.getItems().add(current.categoryLosses1 + " losses in category: food");
    				listItems.getItems().add(current.categoryLosses2 + " losses in category: sports");
    				listItems.getItems().add(current.categoryLosses3 + " losses in category: cars");

    				if(current.category1) {
    					listItems.getItems().add("They have won category: food");
    				}
    				else {
    					listItems.getItems().add("They have not yet won category: food");
    				}
    				
    				if(current.category2) {
    					listItems.getItems().add("They have won category: sports");
    				}
    				else {
    					listItems.getItems().add("They have not yet won category: sports");
    				}
    				
    				if(current.category3) {
    					listItems.getItems().add("They have won category: cars");
    				}
    				else {
    					listItems.getItems().add("They have not yet won category: cars");
    				}
    			}
    		}
    		listItems.getItems().add("-----------------------------------------------------------------------");
    	}
    }
    

  
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("(Server) Let's Play Word Guessing Game!!");
        
        listItems = new ListView<String>();
  
      
        // Initializing TextFields and Labels
        initTextFields();
        
        initLabels();
        
        startServer = new Button("Start Server");
        
        startServer.setOnAction(e -> {
    		
    		 // error checking to make sure empty text field doesnt call null exception
        	 if(portEntry.getText().trim().isEmpty()){
                  errorPortEntry.setText("Error: Enter 4 digit integer and press start server");
                  return;
              }
              
              
            // if the previous was valid start the server gui!  
            primaryStage.setScene(sceneMap.get("server"));
            
            
            //Take port number frm text field and create new server.
            serverConnection = new WGServer(data -> {
                
                //Just to update the gui
                Platform.runLater(()->{
                   updateServerGUI((WordGuessInfo)data);
                 });
            }, 
            portEntry.getText()) ;
            
        });

        // helper functions to keep start function small, does all
        // busy work to prepare server gui scenes
        initScenes();
        
        primaryStage.setScene(scene);
        primaryStage.show();
	}

}
