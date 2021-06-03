import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WordGuessClient extends Application {

	
	WGClient clientConnection;
	
	ListView<String> status;
	
	Button guess;
	
	TextField tf;
	
	

	
	// objects needed for scene 1, enter the ip and port to connect;
	Scene PortIPScene;
	Pane PortIPPane;
	TextField portEntryTF, ipAddressTF, errorMsgTF;
	Button sendPortIP;
	Label userNameEntryLabel, IPEntryLabel, PortEntryLabel;
	
	// objects needed for scene 2. choose category
	Scene ChooseCategoryScene;
	Pane ChooseCategoryPane;
	Label ChooseCategoryLabel;
	Label LeftLabel;
	TextField numRemaining1, numRemaining2, numRemaining3;
	
	Button category1, category2, category3;
	Label category1Label, category2Label, category3Label;
	Image image1, image2, image3;
	ImageView imageView1, imageView2, imageView3;
	
	// Objects needed for scene 3, displaying correct/incorrect 
	Pane thirdPane;
	Label enterLetterLabel, numberGuessesLabel, wordBeingMade, resultLabel;
	Button sendButton, categoryButton;
	TextField characterTF, errorMsg2TF;
	Label guessesUsedLabel;
	Image paneImage;
	BackgroundSize backgroundSize;

	

	// Objects needed for scene 4, displaying Play Again/Quit Game
	Pane fourthPane;
	Label gameResultLabel;
	Button playAgainButton, quitGameButton;
	
	// Initializes the objects needed for Scene 3
	void initThirdScene() 
	{
			
		//		resultLabel = new Label(); // This will display the result
		//	    resultLabel.relocate(500, resultLabel.getLayoutY());
		//	    resultLabel.setStyle("-fx-font-size: 32");
		guessesUsedLabel = new Label("Guesses Remaining: ");
		guessesUsedLabel.relocate(400, 400);
		guessesUsedLabel.setStyle("-fx-font-size: 48; -fx-text-fill:black;");
		
		enterLetterLabel = new Label("Enter a letter below");
		enterLetterLabel.relocate(480, 150);
		enterLetterLabel.setStyle("-fx-font-size: 32; -fx-border-color:black; -fx-text-fill:black; -fx-padding:3px");
		
		numberGuessesLabel = new Label(); // This is going to be updated by the updateGUI
		numberGuessesLabel.relocate(835, 405);
		numberGuessesLabel.setStyle("-fx-font-size: 48; -fx-text-fill:black");
		
		wordBeingMade = new Label(); //  This is going to be updated by the updateGUI 
		wordBeingMade.relocate(400, 500);
		wordBeingMade.setStyle("-fx-font-size: 48; -fx-text-fill:black");
		
		
		
		characterTF = new TextField();
		characterTF.setPrefWidth(100);
		characterTF.relocate(570, 250);
		
		errorMsg2TF = new TextField();
		errorMsg2TF.setDisable(true);
		errorMsg2TF.relocate(525, 290);
		errorMsg2TF.setPrefWidth(200);
		errorMsg2TF.setOpacity(100);

		    
	}
	
	// Retrieves the third scene 
	Scene getThirdScene() 
	{
	    
	    thirdPane = new Pane();
		paneImage = new Image("file:src/main/resources/guess.jpg");
		backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		thirdPane.setBackground(new Background(new BackgroundImage(paneImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize)));
	    
	    thirdPane.getChildren().addAll(guessesUsedLabel, wordBeingMade, enterLetterLabel, numberGuessesLabel, sendButton, characterTF, errorMsg2TF, categoryButton);
	    return new Scene(thirdPane, 1300, 600);
	
	}
	
	// Initializes the objects needed for the fourth scene
	void initFourthScene() 
	{
		
		gameResultLabel = new Label();
		gameResultLabel.setStyle("-fx-font-size: 32px;");
		gameResultLabel.relocate(275, 200);
	    
	   playAgainButton = new Button("Play again");
	    playAgainButton.relocate(250, 300);
	    
	    quitGameButton = new Button("Quit game");
	    quitGameButton.relocate(350, 300);
	    
	
	}
	
	// Retrieves the fourth scene
	Scene getFourthScene()
	{
	    
	    fourthPane = new Pane();
	    fourthPane.getChildren().addAll(gameResultLabel, playAgainButton, quitGameButton);
	    
	    return new Scene(fourthPane, 700, 700);
	}
	
	// Initializes the objects needed for the first scene 
	void initPortIPScene()
	{
		
		// init the port entry field
		portEntryTF = new TextField();
		portEntryTF.setDisable(false);
		portEntryTF.setOpacity(100);
		portEntryTF.relocate(0, 150);
		
		// init ip entry field
		ipAddressTF = new TextField("127.0.0.1");
		ipAddressTF.setDisable(false);
		ipAddressTF.setOpacity(100);
		ipAddressTF.setPrefWidth(150);
		ipAddressTF.relocate(0, 200);
	
		
		// init Error msg field
		errorMsgTF = new TextField("Enter Port Number and IP address");
		errorMsgTF.setDisable(true);
		errorMsgTF.setOpacity(100);
		errorMsgTF.setPrefWidth(250);
		errorMsgTF.relocate(0, 250);
		 
		sendPortIP = new Button("Send Info!");
		sendPortIP.relocate(0, 300);
		
		IPEntryLabel = new Label("Enter IP Address");
		IPEntryLabel.setStyle("-fx-color: white");
		IPEntryLabel.relocate(ipAddressTF.getScaleX()+150, 200);
		
		PortEntryLabel = new Label("Enter Port Number");
		PortEntryLabel.setStyle("-fx-color: white");
		PortEntryLabel.relocate(portEntryTF.getScaleX()+150, 150);
	}
	
	// Retrieves the first scene 
	Scene getPortIPScene() 
	{
		PortIPPane= new Pane();
		PortIPPane.getChildren().addAll(IPEntryLabel, PortEntryLabel,
				 portEntryTF, ipAddressTF, errorMsgTF, sendPortIP);
		PortIPScene = new Scene(PortIPPane, 600, 700);
		return PortIPScene;
	}
	
	// Initializes the objects needed for the second scene 
	void initCategoryScene() 
	{
		
		ChooseCategoryLabel = new Label("Choose A Category!");
		
		ChooseCategoryLabel.setStyle("-fx-font-size: 48");
		ChooseCategoryLabel.relocate(550, ChooseCategoryLabel.getLayoutY());
		
		
		numRemaining1 = new TextField("3 tries Remaining!");
		numRemaining2 = new TextField("3 tries Remaining!");
		numRemaining3 = new TextField("3 tries Remaining!");
		
		numRemaining1.setDisable(true);
		numRemaining1.setOpacity(100);
		
		numRemaining2.setDisable(true);
		numRemaining2.setOpacity(100);
		
		numRemaining3.setDisable(true);
		numRemaining3.setOpacity(100);
		
		image1 = new Image("file:src/main/resources/food.jpg");  // serve as choice 1
	    image2 = new Image("file:src/main/resources/sports.jpg");  // serve as choice 2
	    image3 = new Image("file:src/main/resources/cars.jpg");  // serve as choice 3
	    
	    imageView1 = new ImageView(image1);
	    imageView2 = new ImageView(image2);
	    imageView3 = new ImageView(image3);
	    
	    imageView1.setFitWidth(250);
	    imageView1.setFitHeight(250);
	    imageView1.setPreserveRatio(true);
	    
	    imageView2.setFitWidth(250);
	    imageView2.setFitHeight(250);
	    imageView2.setPreserveRatio(true);
	    
	    imageView3.setFitWidth(280);
	    imageView3.setFitHeight(280);
	    imageView3.setPreserveRatio(true);
	    
	    numRemaining1.relocate(310, 410);
	    numRemaining2.relocate(700, 410);
	    numRemaining3.relocate(1040, 410);
	    
	    category1Label = new Label("Food");
	    category2Label = new Label("Sports"); 
	    category3Label = new Label("Cars");
	    
	    
	    category1Label.setPrefWidth(300);
	    category1Label.relocate(200, 400);
	    category1Label.setStyle("-fx-font-size: 32");
	    
	    category2Label.setPrefWidth(300);
	    category2Label.relocate(590, 400);
	    category2Label.setStyle("-fx-font-size: 32");
	   
	    category3Label.setPrefWidth(300);
	    category3Label.relocate(950, 400);
	    category3Label.setStyle("-fx-font-size: 32");
	    
		
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		launch(args);
	}
	
	// Retrieves the second scene 
	Scene getCategoryScene() 
	{
		ChooseCategoryPane = new Pane();
		
		ChooseCategoryPane.getChildren().addAll(category1, category2, category3,
				 numRemaining1, numRemaining2, numRemaining3);
		ChooseCategoryPane.getChildren().addAll(ChooseCategoryLabel,
				category1Label, category2Label, category3Label);
		
		ChooseCategoryScene = new Scene(ChooseCategoryPane,1300,600);
		return ChooseCategoryScene;
	}
	
	// Updates all our Client GUI components based on the response of the server 
	void updateGui(WordGuessInfo current, Stage stage) 
	{
		
		
		numberGuessesLabel.setText(String.valueOf(6- current.guessNumber));
		wordBeingMade.setText("Your Guess so far is: " + current.currentWordGuess);
		
//		if(current.winRound)
//		{
//		    resultLabel.setText("Correct guess!");
//		}
//		 
//		else if(!current.winRound && current.guessNumber > 0 )
//		{
//		    resultLabel.setText("Incorrect guess!");
//		}
        // Checks to see if any other categories are won.
			// If yes, disables that category 
			if(current.category1) {
				category1.setDisable(true);
				numRemaining1.setText("You have won!");
				
			}
			else if (current.categoryLosses1 != 0) {
				numRemaining1.setText((3 - current.categoryLosses1) + " tries remaining!");
			}
			
			if(current.category2) {
				category2.setDisable(true);
				numRemaining2.setText("You have won!");
			}
			else if(current.categoryLosses2 != 0) {
				numRemaining2.setText((3 - current.categoryLosses2) + " tries remaining!");
			}

			if(current.category3) {
				category3.setDisable(true);
				numRemaining3.setText("You have won!");
			}
			else if(current.categoryLosses3 != 0) {
				numRemaining3.setText((3 - current.categoryLosses3) + " tries remaining!");
			}
			
			// Updates the number of attempts remaining for the categories in the Category Scene 
			
			
			
			
			
        
		
		// Player wins the whole game - should shift to win scene 
		if(current.wins == 3) {
			
			gameResultLabel.setText("You won!");
			stage.setScene(getFourthScene());
			stage.show();
			
		}
		
		// Player loses the whole game - should shift to lose scene 
		else if (current.losses == 3) {
			
			gameResultLabel.setText("You lost!");
			stage.setScene(getFourthScene());
			stage.show();
			
		}
		
		
		
		// Player wins category, returns back to category scene and disables the winning category
		else if (current.wonCategory) {
		
			errorMsg2TF.setText("You guessed it!!!");
			characterTF.setDisable(true);
			sendButton.setDisable(true);
			categoryButton.setDisable(false);
		
		}
		
		// Player loses category, returns back to category scene and decrements the number of attempts for that category
		else if (current.lostCategory) {
			
			errorMsg2TF.setText("You didn't guess it... =[");
			characterTF.setDisable(true);
			sendButton.setDisable(true);
			categoryButton.setDisable(false);
		
		}
	}
	

	//feel free to remove the starter code from this method
	@SuppressWarnings("restriction")
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(Client) Word Guess!!!");
		
		// Initialize all our objects needed for all our scenes 
		initPortIPScene();
		initCategoryScene();
		initThirdScene();
		initFourthScene();
		
		// Initialize category button 1
		category1 = new Button();
		category1.setGraphic(imageView1);
		category1.relocate(200, 200);
		
		// Initialize category button 2
		category2 = new Button();
		category2.setGraphic(imageView2);
		category2.relocate(590, 200);
		
		// Initialize category button 3
		category3 = new Button();
		category3.setGraphic(imageView3);
		category3.relocate(950, 200);
		
		// Initialize send button 
		sendButton = new Button("Send Guess");
		sendButton.relocate(583, 330);
		    
		// Initialize category button
		categoryButton = new Button("Back to categories");
		categoryButton.relocate(567, 380);
		
		
		sendPortIP.setOnAction(e->{
           
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
           
           System.out.println("ip is " + ipAddressTF.getText());
           
           // start client
           clientConnection = new WGClient(y -> {
        	   Platform.runLater(() -> {
        		   updateGui((WordGuessInfo)y, primaryStage);
        		   });
   		   }, ipAddressTF.getText(), portEntryTF.getText());
   		
           clientConnection.start();
           
           primaryStage.setScene(getCategoryScene());
           primaryStage.show();
	    });
	
		
		// Action Handler for our Category One button
		category1.setOnAction(x -> {
			
			errorMsg2TF.clear();
			categoryButton.setDisable(true);
			clientConnection.clientsGame.currentCategory = 1;
			clientConnection.clientsGame.choosingCategory = true;
			clientConnection.send();
			initThirdScene();
			primaryStage.setScene(getThirdScene());
			primaryStage.show();
		});
		
		
		// Action Handler for our Category Two button
		category2.setOnAction(x -> {
			
			
			errorMsg2TF.clear();
			categoryButton.setDisable(true);
			clientConnection.clientsGame.currentCategory = 2;
			clientConnection.clientsGame.choosingCategory = true;
			
			clientConnection.send();
			initThirdScene();
			primaryStage.setScene(getThirdScene());
			primaryStage.show();
		});
		
		
		// Action Handler for our Category Three button
		category3.setOnAction(x -> {
			
		
			errorMsg2TF.clear();
			categoryButton.setDisable(true);
			clientConnection.clientsGame.currentCategory = 3;
			clientConnection.clientsGame.choosingCategory = true;
			
			clientConnection.send();	
			initThirdScene();
			primaryStage.setScene(getThirdScene());
			primaryStage.show();
		});
		
		// Action Handler for our send guess button
		sendButton.setOnAction(e-> {
			
			if(characterTF.getText().trim().isEmpty() || (!Character.isLetter((characterTF.getText()).charAt(0))))
			{
				errorMsg2TF.setText("Single character must be entered.");
				return;
			}
			
			String input;
			input = characterTF.getText().toLowerCase();
			clientConnection.clientsGame.currentGuess = input.charAt(0);
			clientConnection.send();
			
			characterTF.clear();
			
			primaryStage.setScene(getThirdScene());
			primaryStage.show();
			
			
		});
		
		// Action Handler for our category button
		categoryButton.setOnAction(e-> {
			
			//resultLabel.setText("");
			sendButton.setDisable(false);
			characterTF.setDisable(false);
			primaryStage.setScene(getCategoryScene());
			primaryStage.show();
			
			
		});
		
		//Action Handler for our play again button
		playAgainButton.setOnAction(e->{
			clientConnection.clientsGame = new WordGuessInfo();
			initCategoryScene();
			primaryStage.setScene(getCategoryScene());
			category1.setDisable(false);
			category2.setDisable(false);
			category3.setDisable(false);
		});
		
		// Action Handler for our quit button
		quitGameButton.setOnAction(e->{
			System.exit(0);
		});
		
		
		primaryStage.setScene(getPortIPScene());
		primaryStage.show();
	}
	
	
}
	