import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/* server gui */

public class TheGameOfMorra extends Application {
    TextField p1Scores, p1Guesses, p1Hands; // player1 textfields for display score guess and hands
    TextField p2Scores, p2Guesses, p2Hands; // player2 textfields for display score guess and hands
    TextField p1Continue, p2Continue;  // player1 and player2 textfields for if plays chose to continue
    TextField status;  // display status like who won turn
    Text numberOfClients;
    
    // labels for various player data
    Label p1NameLabel, p2NameLabel, p1GuessLabel, p2GuessLabel, p1HandLabel, p2HandLabel, p1ScoresLabel, p2ScoresLabel, p1ContinueLabel, p2ContinueLabel;


    VBox startS; // Vertical box for intro scene in server
    Text portInput;  // display portNo
    TextField portEntry; // textfield for eneter port
    TextField errorPortEntry;  // textfield to display errors to use
    Button startServer; 
    //MorraInfo game;
    Server serverConnection;  // server object to manage game
    HashMap<String, Scene> sceneMap;  // hash map of scenes for easy changing
	ListView<String> listItems, listItems2;  // listviews to display game state
	Scene scene;

    Pane startingPane;  // Pane to use that is modified throught the game
    
    
        // function to manage initialization of textfields and set triggers
        public void initTextFields(){
         
        	
        numberOfClients	= new Text("Number of clients: 0");
        numberOfClients.relocate(0, 10);
        	
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
        
        /* player 1 output text fields in server gui */
        p1Scores = new TextField();
        p1Scores.relocate(200, 50);
        p1Scores.setPrefWidth(50);
        p1Scores.setDisable(true);
        p1Scores.setOpacity(100);
        p1Scores.setText("" + 0);
       
        p1Guesses = new TextField();
        p1Guesses.relocate(200, 150);
        p1Guesses.setPrefWidth(50);
        p1Guesses.setDisable(true);
        p1Guesses.setOpacity(100);

        p1Hands = new TextField();
        p1Hands.relocate(200, 250);
        p1Hands.setPrefWidth(50);
        p1Hands.setDisable(true);
        p1Hands.setOpacity(100);
        
        p1Continue = new TextField();
        p1Continue.relocate(200, 350);
        p1Continue.setPrefWidth(50);
        p1Continue.setDisable(true);
        p1Continue.setOpacity(100);
        
        /* correspoding for player 2 */
        p2Scores = new TextField();
        p2Scores.relocate(420, 50);
        p2Scores.setPrefWidth(50);
        p2Scores.setDisable(true);
        p2Scores.setOpacity(100);
        p2Scores.setText("" + 0);
        
        p2Guesses = new TextField();
        p2Guesses.relocate(420, 150);
        p2Guesses.setPrefWidth(50);
        p2Guesses.setDisable(true);
        p2Guesses.setOpacity(100);
        
        p2Hands = new TextField();
        p2Hands.relocate(420, 250);
        p2Hands.setPrefWidth(50);
        p2Hands.setDisable(true);
        p2Hands.setOpacity(100);
        
        p2Continue = new TextField();
        p2Continue.relocate(420, 350);
        p2Continue.setPrefWidth(50);
        p2Continue.setDisable(true);
        p2Continue.setOpacity(100);
        
        status = new TextField();
        status.relocate(200, 450);
        status.setPrefWidth(270);
        status.setDisable(true);
        status.setOpacity(100);
        status.setText("Welcome To Morra");
    }
        
    //function to be used by platform runlater to update servers P1 Information   
    public void updateP1TextFields(MorraInfo info) {
    	p1Scores.setText( "" + info.Points );
		p1Guesses.setText("" + info.Guess );
		p1Hands.setText("" + info.Hand );
		p1NameLabel.setText(info.Name);
		
		// display if the game is over, player chooeses to go again or not
        if(info.updateCont && info.goAgain){
            p1Continue.setText("Yes");
        }
        else if(info.updateCont){
            p1Continue.setText("No");
        }
        else {
        	p1Continue.setText("");
        }
       
	}
	
	
    //function to be used by platform runlater to update servers P2 Information 
    public void updateP2TextFields(MorraInfo info) {
    	
    	p2Scores.setText( "" + info.Points );
		p2Guesses.setText("" + info.Guess );
		p2Hands.setText("" + info.Hand );
		p2NameLabel.setText(info.Name);
		
		// display if the game is over, player chooeses to go again or not
		if(info.updateCont && info.goAgain){
            p2Continue.setText("Yes");
        }
        else if(info.updateCont){
            p2Continue.setText("No");
        }
        else {
        	p2Continue.setText("");
        }
	}
    
    // functiopn to update status textfield after each turn
    public void updateStatus(String msg) {
    	status.setText(msg);
    }
    
    
    // function to initialize the various gui labels
     public void initLabels() {
        
    	listItems.relocate(530,listItems.getLayoutY());
    	listItems.setPrefWidth(360);
    	listItems.setPrefHeight(480);
        
    	p1NameLabel = new Label("P1 Name: ");
        p2NameLabel = new Label("P2 Name: ");
        p1HandLabel = new Label("P1 Hand: ");
        p2HandLabel = new Label("P2 Hand: ");
        p1GuessLabel = new Label("P1 Guess: ");
        p2GuessLabel = new Label("P2 Guess: ");
        p1ScoresLabel = new Label("P1 Scores: ");
        p2ScoresLabel = new Label("P2 Scores: ");
        p1ContinueLabel = new Label("P1 Continue?");
        p2ContinueLabel = new Label("P2 Continue?");
        
        p1NameLabel. relocate(140, 10);
        p1NameLabel.setPrefWidth(100);
        
        p1ScoresLabel.relocate(140, 50);
        p1ScoresLabel.setPrefWidth(100);
        
        p2ScoresLabel.relocate(360, 50);
        p2ScoresLabel.setPrefWidth(100);
        
        p1HandLabel.relocate(140, 150);
        p1HandLabel.setPrefWidth(100);
        
        p2NameLabel. relocate(360, 10);
        p2NameLabel.setPrefWidth(100);
        
        p2HandLabel.relocate(360, 150);
        p2HandLabel.setPrefWidth(100);
        
        p1GuessLabel.relocate(140, 250);
        p1GuessLabel.setPrefWidth(100);
        
        p2GuessLabel.relocate(360, 250);
        p2GuessLabel.setPrefWidth(120);
        
        p1ContinueLabel.relocate(125,350);
        p1ContinueLabel.setPrefWidth(100);
        
        p2ContinueLabel.relocate(345, 350);
        p2ContinueLabel.setPrefWidth(120);
        
    }
    
    // function to help in scene management, in particular return startinf scene
    public void initScenes(){
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
    	
    	pane.getChildren().addAll(p1ContinueLabel, p2ContinueLabel, p1ScoresLabel, p2ScoresLabel, 
    	p1NameLabel, p2NameLabel, p1HandLabel, p2HandLabel, p1GuessLabel, p2GuessLabel, p1Scores,
    	p1Guesses, p1Hands, p2Scores, p2Guesses, p2Hands, listItems, p1Continue, p2Continue,
    	status, numberOfClients );    	

        return new Scene(pane, 900, 500);
    	
    }
   

  
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("(Server) Let's Play Morra!!!");
        
        listItems = new ListView<String>();
        listItems2 = new ListView<String>();
        
      
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
            serverConnection = new Server(data -> {
                
                //Just to update the gui
                Platform.runLater(()->{
                    listItems.getItems().add(data.toString());
                    numberOfClients.setText("Number of clients: " + (serverConnection.count-1));
                    });
            }, 
            
            // consumer to update P1 data in server gui
            info->{
          	  Platform.runLater( ()->{
          		 updateP1TextFields( (MorraInfo) info );
          	 });
          	  
            },
            
            // consumer to update P2 data in server gui
            info->{
          	  Platform.runLater( ()->{
          		  updateP2TextFields( (MorraInfo) info );
          	  });
            },
            
            // consumer to update game satus in server gui
            msg->{
          	  Platform.runLater( ()->{
          		  updateStatus( (String) msg);
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