import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TheGameOfMorra extends Application {

    //MorraInfo game;
    
	static int ID = 0;  
	
	VBox startS; // a vbox for helping design the starting scene
	
	// various labels to assists
    Label portInputLabel, ipAddressLabel, pickHandLabel, pickGuessLabel, 
          gameLabel, numberLabel, userNameLabel, displayUserLabel, 
          playerOnePointsLabel, playerTwoPointsLabel;
          
    // buttons to decide game flow      
    Button startClientButton, sendButton, sendInfoButton, continueButton, playAgainButton, quitButton, send;
    
    // client object to manage connection to server
    Client clientConnection;
    
    // simple map to help with scene changing
    HashMap<String, Scene> sceneMap;
    
    // a Pane to server as base of scene graph
    Pane pane1;
    
    // list view to display game state to clients
	ListView<String> listItems;
	
	// vbox to help design client gui
	VBox clientBox;
	
	// text fields to enter and display relevant information
	TextField   errorMsgTF, portEntryTF, ipAddressTF, totalGuessTF, 
	            errorMsg2TF, userNameTF, player,
	            playerOnePointsTF, playerTwoPointsTF; 
	
	// image and image views to server as clickables for user to decide 0 to 5 hand
	Image image, image0, image1, image2, image3, image4, image5;
	ImageView imageView0, imageView1, imageView2, imageView3, imageView4, imageView5;
	
	// variable to hold size data required for backroung
	BackgroundSize backgroundSize;
	
	// string to be used to hold username after it is entered
	String userName;
	
	// variable to hold what the used hand choice was
	int myPick = -1;
	Boolean newGame;
	
	
	// Initializing all Labels
	public void initLabel()
	{
		 portInputLabel = new Label("Enter port number"); // Label for Port Number
		 ipAddressLabel = new Label("Enter ip address"); // Label for IP Address
		 userNameLabel = new Label("Enter your username"); // Label for UserName
		 
		 
		 pickHandLabel = new Label("Click on one of the images below to play your hand"); // Label to pick hand
		 pickHandLabel.setStyle("-fx-font-size:32px; -fx-text-fill: black; -fx-background-color: lightgray; -fx-opacity: 0.70;");
		 pickHandLabel.relocate(150, 400);
		 pickHandLabel.setPrefWidth(730);
		 
		 pickGuessLabel = new Label("Enter your guess total below"); // Label to guess total
		 pickGuessLabel.setStyle("-fx-font-size:32px; -fx-text-fill: black; -fx-background-color: lightgray; -fx-opacity: 0.70;");
		 pickGuessLabel.relocate(300, 150);
		 pickGuessLabel.setPrefWidth(405);
		 
		 gameLabel = new Label("Playing the Ancient Game of Morra!"); // Welcome Label
		 gameLabel.setStyle("-fx-font-size: 44px; -fx-text-fill:black; -fx-background-color: lightgray; -fx-opacity: 0.70;");
		 gameLabel.relocate(180, 20);
		 gameLabel.setPrefWidth(700);
		 
		 numberLabel = new Label("(0-10)"); // 0-10 label for  what to input for guess
		 numberLabel.setStyle("-fx-font-size:32px; -fx-text-fill:black; -fx-background-color: lightgray; -fx-opacity: 0.70;");
		 numberLabel.relocate(450, 210);
		 numberLabel.setPrefWidth(90);
		 
		 displayUserLabel = new Label();  // label to display user name
		 displayUserLabel.setStyle("-fx-font-size:32px; -fx-text-fill: black; -fx-background-color: lightgray; -fx-opacity: 0.70;");
		 displayUserLabel.relocate(10, 100);
		 displayUserLabel.setPrefWidth(300);
		 
	   	 playerOnePointsLabel = new Label("P1 Points: ");  // label for p1 points
		 playerOnePointsLabel.relocate(50, 100);
		 playerOnePointsLabel.setPrefWidth(400);
		 
		 playerTwoPointsLabel = new Label("P2 Points: ");  // label for p2 points
		 playerTwoPointsLabel.relocate(800, 100);
		 playerTwoPointsLabel.setPrefWidth(400);
			 
	}
	
	// Initializing all Text Fields
	public void initTextField()
	{
		
        errorMsgTF = new TextField(); // TextField for error messages 
        errorMsgTF.setDisable(true);
        portEntryTF = new TextField(); // TextField for client to enter port number
        
        ipAddressTF = new TextField(); // TextField for IP Address
        
        totalGuessTF = new TextField(); // TextField for total Guess
        totalGuessTF.relocate(460, 270);
        totalGuessTF.setPrefWidth(50);
        totalGuessTF.setDisable(true);
        totalGuessTF.setOpacity(100);
       
        errorMsg2TF = new TextField();  // TextField for secondary error message
        errorMsg2TF.relocate(430, 360);
        errorMsg2TF.setDisable(true);
        errorMsg2TF.setPrefWidth(200);
        errorMsg2TF.setOpacity(100);
        errorMsg2TF.setText("First Press a Number !");
        errorMsg2TF.setStyle("-fx-opacity: 1.00");
        userNameTF = new TextField();
        
        playerOnePointsTF = new TextField();  // display player 1 points
        playerOnePointsTF.setDisable(true);  
        playerOnePointsTF.relocate(50, 150);
        playerOnePointsTF.setPrefWidth(50);
        
        playerTwoPointsTF = new TextField();  // display player 2 points
        playerTwoPointsTF.setDisable(true);
        playerTwoPointsTF.relocate(800, 150);
        playerTwoPointsTF.setPrefWidth(50);
	}
		
	// Initializing all Image/ImageView
	public void initImages()
	{
		image = new Image("file:src/main/resources/clock.jpg");
		
		image0 = new Image("zero.png");  // serve as choice 0
		imageView0 = new ImageView(image0);
    	imageView0.setFitHeight(150);
    	imageView0.setFitWidth(150);
    	imageView0.setPreserveRatio(true);
    	imageView0.setX(50);
    	imageView0.setY(500);
    	
    	
    	image1 = new Image("file:src/main/resources/one.png");  // serve as choice 1
    	imageView1 = new ImageView(image1);
    	imageView1.setFitHeight(150);
    	imageView1.setFitWidth(150);
    	imageView1.setPreserveRatio(true);
    	imageView1.setX(200);
    	imageView1.setY(500);
    	
    	image2 = new Image("file:src/main/resources/two.png");  // serve as choice 2
    	imageView2 = new ImageView(image2);
    	imageView2.setFitHeight(150);
    	imageView2.setFitWidth(150);
    	imageView2.setPreserveRatio(true);
    	imageView2.setX(350);
    	imageView2.setY(500);
    	
    	image3 = new Image("file:src/main/resources/three.png");  // serve as choice 3
    	imageView3 = new ImageView(image3);
    	imageView3.setFitHeight(150);
    	imageView3.setFitWidth(150);
    	imageView3.setPreserveRatio(true);
    	imageView3.setX(500);
    	imageView3.setY(500);
    	
    	image4 = new Image("file:src/main/resources/four.png");  // serve as choice 4
    	imageView4 = new ImageView(image4);
    	imageView4.setFitHeight(150);
    	imageView4.setFitWidth(150);
    	imageView4.setPreserveRatio(true);
    	imageView4.setX(650);
    	imageView4.setY(500);
    	
    	image5 = new Image("file:src/main/resources/five.png");  // server as choice 5
    	imageView5 = new ImageView(image5);
    	imageView5.setFitHeight(150);
    	imageView5.setFitWidth(150);
    	imageView5.setPreserveRatio(true);
    	imageView5.setX(850);
    	imageView5.setY(500);
    	
    	// upon click of 0, set pick variable, enable guess entry form and send buttons
    	imageView0.setOnMouseClicked(e -> {
										   myPick = 0; enableGuessField();	
		});
		
    	
    	// upon click of 1, set pick variable, enable guess entry form and send buttons
		imageView1.setOnMouseClicked(e -> {
											myPick = 1; enableGuessField();
		});
		
		
    	// upon click of 2, set pick variable, enable guess entry form and send buttons
		imageView2.setOnMouseClicked(e -> {
											myPick = 2; enableGuessField();
		});
		
		
    	// upon click of 3, set pick variable, enable guess entry form and send buttons
		imageView3.setOnMouseClicked(e -> {
											myPick = 3; enableGuessField();
		});
		
		
    	// upon click of 4, set pick variable, enable guess entry form and send buttons
		imageView4.setOnMouseClicked(e -> {
											myPick = 4; enableGuessField();
		});
		
		
    	// upon click of 5, set pick variable, enable guess entry form and send buttons
		imageView5.setOnMouseClicked(e -> {
											myPick = 5; enableGuessField();
		});
    	
	}
	
	// enable guess entry form and disable send button
	public void enableGuessField(){
	    errorMsg2TF.setText("Now Enter a Guess!");
	    totalGuessTF.setDisable(false);
	    sendInfoButton.setDisable(false);
	}
	
	
	// Creates our third scene 
	public Scene sceneSetUp()
	{
		pane1 = new Pane();
		
		// add gui elelemnts to the pane
	    pane1.getChildren().addAll(listItems, continueButton, playerOnePointsLabel, playerTwoPointsLabel, playerOnePointsTF, playerTwoPointsTF, quitButton, playAgainButton);

		
		listItems.setPrefHeight(400);
		listItems.setPrefWidth(400);
		listItems.relocate(285, 125);
		
		return new Scene(pane1,1000,700);
	}
	
	//Uses mora info to update listview.
	public void displayGameStats(MorraInfo game) {
		
		
		playerOnePointsLabel.setText(game.Name.toString() + "'s Points");
		playerTwoPointsLabel.setText(game.OtherName.toString() + "'s Points");
		
		
		listItems.getItems().add("You played: " + game.Hand);
		listItems.getItems().add("You Guessed: " + game.Guess);
		listItems.getItems().add("Your Points: " + game.Points);
		listItems.getItems().add(game.OtherName + " Played " + game.OtherHand);
		listItems.getItems().add(game.OtherName + " Guessed " + game.OtherGuess);
		listItems.getItems().add(game.OtherName + " Points " + game.OtherPoints);
		
		//Update points fifield
		playerTwoPointsTF.setText(game.OtherPoints.toString());
		playerOnePointsTF.setText(game.Points.toString());
		
		// display that user won round
		if(game.didWin) {
			listItems.getItems().add("You Won the Round!");
		}else {
			listItems.getItems().add("You Lost the Round");
		}
		
		// display user won the game
		if(game.did2Points){
		    
            if(game.didWin) {
                listItems.getItems().add("You Won the Game!");
            }else {
                listItems.getItems().add("You Lost the Game");
            }
		    
            // disable buttons to control flow
		    continueButton.setDisable(true);
		    playAgainButton.setDisable(false);
		    quitButton.setDisable(false);
		}
		
		return;
	}

  
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("(Client) Let's Play Morra!!!");
       
        listItems = new ListView<String>();
        
        initLabel();
        initTextField();
    	//initButton();
    	initImages();
    	
    	ipAddressTF.setText("127.0.0.1");
    	
    	startClientButton = new Button("Start Client"); // Button to start up client 
		
    	sendButton = new Button("Send"); // Button for client to send their information
		sendInfoButton = new Button("Send Info"); // Button for sending info
		sendInfoButton.setDisable(true);
		sendInfoButton.relocate(453, 320);
		
		send = new Button("Send");  // button to assist in sending
		send.setDisable(true);
		
		continueButton = new Button("Continue"); // button to do next turn
		continueButton.relocate(50, 300);
		
		playAgainButton = new Button("Play again");  // button that if game is over, play again
		playAgainButton.setDisable(true);
		playAgainButton.relocate(50, 400);
		playAgainButton.setPrefWidth(100);
		
		quitButton = new Button("Quit");  // button to quit game if desiered
		quitButton.relocate(50, 500);
		quitButton.setPrefWidth(100);
		quitButton.setDisable(true);
		
        
		// display to server that user wishes to end
        quitButton.setOnAction(e-> {
        	MorraInfo update = new MorraInfo(clientConnection.clientsGame);
			update.goAgain = false;
			update.updateCont = true;
        	
        	try {
				clientConnection.out.writeObject(update);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	System.exit(0);
        });
    	
        
    	playAgainButton.setOnAction(e-> {
    		
    		if(clientConnection.clientsGame.did2Points) {
 			   
    			// create fresh morra info and set flags to signify desire to go again
    			MorraInfo update = new MorraInfo(clientConnection.clientsGame);
    			update.goAgain = true;
    			update.updateCont = true;
    			
    			// notify server that client wishes to play again
				try {
					clientConnection.out.writeObject(update);
        			
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			
				// disable buttons so user cant mess anything up
    			playAgainButton.setDisable(true);
    			quitButton.setDisable(true);
    			continueButton.setDisable(false);   
 		   }
    	    
    		
    		// reset game
    	    clientConnection.clientsGame = new MorraInfo();
    	    clientConnection.clientsGame.setName(userName);
    	    listItems.getItems().clear();
    	    primaryStage.setScene(createClientGui());
    	    
    	});
    	
    	// button to start client connection
    	startClientButton.setOnAction(e -> {

	            // error handle user name
	            if(userNameTF.getText().trim().isEmpty() ){
	                
	                errorMsgTF.setText("Need to input a username.");
	                return;
	            }
	            
	            // error handle port number
	            if(portEntryTF.getText().trim().isEmpty() ){
	                errorMsgTF.setText("Need to input an integer port number");
	                return;
	            }
	            
	            // error handle ip entry
	            if(ipAddressTF.getText().trim().isEmpty()){
	                errorMsgTF.setText("Need to input an ip address.");
	                return;
	            }
	            
	            // set user name
	            userName = userNameTF.getText();
	            displayUserLabel.setText("Player: " + userName);
	            
	            
	            
	            //Entries valid startup client
	          //Take port number frm text field and create new server.
	            clientConnection = new Client(data -> {
	                Platform.runLater(()->{
	                	
	                	listItems.getItems().add(data.toString());
	                	
	                    });
	            }, flag -> {
	            	
	 	           Platform.runLater(() -> { // display client gui
	 	        	  primaryStage.setScene(sceneMap.get("client"));
	 	           });
	            	
	            }, mora -> {  // display morra info's contents
	            	Platform.runLater(() -> {
	            		displayGameStats((MorraInfo)mora);
	            		primaryStage.setScene(sceneSetUp());
	                    primaryStage.show();
	            	});
	            },(x -> {  // display if a player left the game
	            	
	            	Platform.runLater(() -> {
	            		primaryStage.setScene(playerLeft());
	                    primaryStage.show();
	            	});
	            	
	            }),ipAddressTF.getText(), portEntryTF.getText());  // add ip and port to client
	            
	            clientConnection.clientsGame.setName(userName);
	            
	            clientConnection.start();
	            
	            // tell client they are only one active
	            startClientButton.setDisable(true);
	            errorMsgTF.setText("You are the only player, please Wait for another player to connect.");
	            
	            // disable entry forms as to not reset data
	            userNameTF.setDisable(true);
	            portEntryTF.setDisable(true);
	            ipAddressTF.setDisable(true);   
        }); 
        
    	// button to send info off to the server
        sendInfoButton.setOnAction(x -> {
    		
        	// error handle guess entry
    		Integer TheGuess;
    		if(totalGuessTF.getText().trim().isEmpty())
    		{
    			errorMsg2TF.setText("Must enter an integer from 0 to 10 guess then press send info");
    			return;
    		}
    		
    		// parse guess from text
    		TheGuess = Integer.valueOf(totalGuessTF.getText());
    		
    		// error handle guess entry to make sure guess is within bounds
    		if(TheGuess < 0 || TheGuess > 10){
    		    errorMsg2TF.setText("Error guess must be between 0 and 10");
    		    return;
    		}
    		
    		// put guess and hand into morra info object
    		clientConnection.clientsGame.setGuess(TheGuess);
			clientConnection.clientsGame.setHand(myPick);
    	
			if(clientConnection.clientsGame == null) {
				System.out.println("BRUH ITS NULL");
			}
			
			// clear guess form after use
			Platform.runLater(()->{
        	    totalGuessTF.clear();
    		});
            
         
			// send client input to server
			try {
				clientConnection.out.writeObject(clientConnection.clientsGame);
				sendInfoButton.setDisable(true);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
    	   
    	   
			
			
			
    	   
    	});
        
        // button to go back to move making scene after state is observed
        continueButton.setOnAction(e-> {
            initImages();
            initTextField();
          
            // if game win criteria is met, decide if to go again or quit
		    if(clientConnection.clientsGame.did2Points) {
			   
			   MorraInfo update = new MorraInfo(clientConnection.clientsGame);
			   update.goAgain = true;
			   update.updateCont = true;
			   clientConnection.send(update);
			   
		   }
            
            
            primaryStage.setScene(createClientGui());
            primaryStage.show();
            
        });
        
        
        
        
        // preliminary scene set up
        startS = new VBox(10);
        startS.getChildren().addAll(portInputLabel, portEntryTF, ipAddressLabel, ipAddressTF, userNameLabel, userNameTF, startClientButton, errorMsgTF); 
        
        sceneMap = new HashMap<String, Scene>();
        
        sceneMap.put("client", createClientGui());
        
        Scene scene = new Scene(startS,600,600);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
  

  
	// function to assist in building client input scene
	 public Scene createClientGui()
	 {
    	
    	Pane pane = new Pane();
    	
    	pane.getChildren().addAll(displayUserLabel, errorMsg2TF, sendInfoButton, numberLabel,totalGuessTF, gameLabel, pickGuessLabel,pickHandLabel, imageView0, imageView1, imageView2, imageView3, imageView4, imageView5);
    	backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		pane.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize)));

    
        return new Scene(pane, 1000, 700);
	 }
	 
	 // display a new scenen upon if other player's connection cut
	 public Scene playerLeft() {
		 
		 BorderPane pain = new BorderPane();
		 Text left = new Text("Other Player Has Left!!! \n Please wait for \n another player,\n or restart the game \n to reastablish \n a new connection");
		 pain.setCenter(left);
		 
	        return new Scene(pain, 1000, 700);
		 
	 }
	

}

    