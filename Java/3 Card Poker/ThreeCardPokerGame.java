import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class ThreeCardPokerGame extends Application {

	static final int picHeight = 154;
	static final int picWidth = 215;
	static int playerMoney = 1000;
	
	private MenuBar menuBar;
	Menu options;
	MenuItem exit;
	MenuItem freshStart;
	MenuItem newLook;
	
	//Card back Image.
	private Image cardBack;
	
	
	//ImageViews for cards.
	private ImageView dealerCard1;
	private ImageView dealerCard2;
	private ImageView dealerCard3;
	private ImageView playerCard1;
	private ImageView playerCard2;
	private ImageView playerCard3;
	private ImageView player2Card1;
	private ImageView player2Card2;
	private ImageView player2Card3;
	
	//Bet textFields.
	TextField anteBet;
	TextField ppBet;
	TextField playBet;
	
	TextField anteBet2;
	TextField ppBet2;
	TextField playBet2;
	
	
	//Bet labels.
	Label anteLabel;
	Label ppLabel;
	Label playLabel;
	
	Label anteLabel2;
	Label ppLabel2;
	Label playLabel2;
	
	//Card labels'
	Text dealerLabel;
	Text playerLabel;
	Text player2Label;
	
	//Boxes for holding ante/pp bet label and textfield.
	VBox anteBox;
	VBox ppBox;
	VBox playBox;
	
	VBox anteBox2;
	VBox ppBox2;
	VBox playBox2;
	
	//Buttons for player choices.
	Button dealButton;
	Button bothPlayButton;
	Button bothFoldButton;
	Button p1FoldButton;
	Button p2FoldButton;
	
	//Box for holding player choice buttons.
	HBox choices;
	
	//Boxes for holding cards.
	HBox dealerBox;
	HBox playerBox;
	HBox playerBox2;
	
	//Box for the center of the border pane.
	VBox cenPane;
	
	//Image for pane background.
	Image background;
	
	//For Scene
	Scene scene;
	
	//For Money
	VBox vb;
	
	HashMap<String,ImageView> cardMap;
	HashMap<String, Scene> sceneMap; 
	//HashMap<Card,ImageView> cardMap;
	
	Text infoLabel;
	Text moneyLabel;
	Text winnings;
	Text winningsAmount;
	Text winnings2;
	
	
	HBox moneyBox;
	HBox wagers;
	HBox wagers2;
	
	VBox gameBox;
	
	PauseTransition pause0;
	PauseTransition pause1;
	PauseTransition pause2;
	PauseTransition pause3;
	PauseTransition pause4;
	PauseTransition pause5;
	
	Text p1LossPP;
	Text p2WinsPP;
	Text p1Wins;
	Text p2Loses;
	Text noDealerHigh;
	
	Text playerStatusLabel;
	Text player2StatusLabel;
	
	
	//Card Game classes
	private Dealer dealer;
	private Player player;
	private Player player2;

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Let's Play Three Card Poker!!!");
		
		//Initalize pauses for transitions.
		pause0 = new PauseTransition(Duration.seconds(0));
		pause1 = new PauseTransition(Duration.seconds(1));
		pause2 = new PauseTransition(Duration.seconds(1));
		pause3 = new PauseTransition(Duration.seconds(1));
		pause4 = new PauseTransition(Duration.seconds(1));
		pause5 = new PauseTransition(Duration.seconds(5));
		
		//Initalize card map.
		cardMap = new HashMap<String ,ImageView>();
		
		//Get Suits.
		ArrayList<Character> suits = new ArrayList<Character>();
		suits.add('C');
		suits.add('D');
		suits.add('H');
		suits.add('S');
		
		
		//Create All Card Image Views and save them in a Image Map.
		for(int i = 2; i < 15; i++) {
			for(int j = 0; j < 4; j++) {
				Image cardImage = new Image(Integer.toString(i) + Character.toString(suits.get(j))  + ".jpg");
				//ImageView cardImageView = new ImageView(cardImage);
				String suitAndValue = Character.toString(suits.get(j)) + Integer.toString(i);
				cardMap.put(suitAndValue, new ImageView(cardImage));
			}
		}
		 
		//Instantiate Dealer, and players.
		dealer = new Dealer();
		player = new Player();
		player2 = new Player();
		
		//Set up menu bar, menu, and menu items.
		menuBar = new MenuBar();
		options = new Menu("Options");
		
		exit = new MenuItem("Exit");
		freshStart = new MenuItem("Fresh Start");
		newLook = new MenuItem("NewLook");
		
		options.getItems().addAll(exit, freshStart, newLook);
		menuBar.getMenus().addAll(options);
		
		// Exits game
		exit.setOnAction(e-> {System.exit(0);});
		
		// Fresh Start
		freshStart.setOnAction(e-> { new Dealer();
							anteBet.setText("5"); ppBet.setText("5");
							anteBet2.setText("5"); ppBet2.setText("5");
							player.totalWinnings = 0; player2.totalWinnings = 0;
							primaryStage.setScene(dealPressed(0));
							anteBet.setDisable(false);
							ppBet.setDisable(false);
							anteBet2.setDisable(false);
							ppBet2.setDisable(false);
							dealButton.setVisible(true);});
		
		// Gives new game a look
		newLook.setOnAction(e-> {cardBack = new Image("purple_back.jpg");
							menuBar.setStyle("-fx-background-color: blue");
							options.setStyle("-fx-background-color: yellow");
							anteLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bolder");
							anteLabel2.setStyle("-fx-font-size: 18; -fx-font-weight: bolder");
							ppLabel2.setStyle("-fx-font-size: 18; -fx-font-weight: bolder");
							playLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bolder");
							playLabel2.setStyle("-fx-font-size: 18; -fx-font-weight: bolder");
							exit.setStyle("-fx-background-color: dark green");
							freshStart.setStyle("-fx-background-color: white");
							newLook.setStyle("-fx-background-color: light red");

							});
		
		//Set default card back.
		cardBack = new Image("Red_back.jpg");
		 
		//Create card Image Views.
		dealerCard1 = new ImageView(cardBack);
		dealerCard2 = new ImageView(cardBack);
		dealerCard3 = new ImageView(cardBack);
		playerCard1 = new ImageView(cardBack);
		playerCard2 = new ImageView(cardBack);
		playerCard3 = new ImageView(cardBack);
		player2Card1 = new ImageView(cardBack);
		player2Card2 = new ImageView(cardBack);
		player2Card3 = new ImageView(cardBack);
		
		//Set height, width, and ratio preservation for card Image Views.
		cardConfig(dealerCard1);
		cardConfig(dealerCard2);
		cardConfig(dealerCard3);
		cardConfig(playerCard1);
		cardConfig(playerCard2);
		cardConfig(playerCard3);
		cardConfig(player2Card1);
		cardConfig(player2Card2);
		cardConfig(player2Card3);
		
		//Initalize player 1 bet fields.
		anteBet = new TextField();
		anteBet.setText("5");
		ppBet = new TextField();
		ppBet.setText("5");
		playBet = new TextField();
		playBet.setDisable(true);
		
		//Initialize player 1 bet fields.
		anteBet2 = new TextField();
		anteBet2.setText("5");
		ppBet2 = new TextField();
		ppBet2.setText("5");
		playBet2 = new TextField();
		playBet2.setDisable(true);
		
		//Set up bet labels.
		anteLabel = new Label("P1 Ante\n$5-25");
		anteLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bolder");
		
		anteLabel2 = new Label("P2 Ante\n$5-25");
		anteLabel2.setStyle("-fx-font-size: 15; -fx-font-weight: bolder");
		
		ppLabel = new Label("P1 PP\n$5-25");
		ppLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bolder");
		
		ppLabel2 = new Label("P2 PP\n$5-25");
		ppLabel2.setStyle("-fx-font-size: 15; -fx-font-weight: bolder");
		
		playLabel = new Label("P1 Play\n");
		playLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bolder");
		
		playLabel2 = new Label("P2 Play\n");
		playLabel2.setStyle("-fx-font-size: 15; -fx-font-weight: bolder");
		
		//Create a Button for Dealing.
		dealButton = new Button("Deal");
		
		//Deal Button Action handler consisting of many pause action handlers for card transitions.
		dealButton.setOnAction(a->{
			
			//Check for Valid Wagers.
			if((Integer.valueOf(anteBet.getText()) <= 25 && Integer.valueOf(anteBet.getText()) >= 5) &&
				(Integer.valueOf(ppBet.getText()) <= 25 && Integer.valueOf(ppBet.getText()) >= 5) && 
				(Integer.valueOf(anteBet2.getText()) <= 25 && Integer.valueOf(anteBet2.getText()) >= 5) &&
				(Integer.valueOf(ppBet2.getText()) <= 25 && Integer.valueOf(ppBet2.getText()) >= 5)) {
				
				//Disable bets boxes.
				anteBet.setDisable(true);
				ppBet.setDisable(true);
				anteBet2.setDisable(true);
				ppBet2.setDisable(true);
				
				//Store bets into player classes.
				player.anteBet = Integer.valueOf(anteBet.getText());
				player.pairPlusBet = Integer.valueOf(ppBet.getText());
				player2.anteBet = Integer.valueOf(anteBet2.getText());
				player2.pairPlusBet = Integer.valueOf(ppBet2.getText());
				
				//Set stage transitions.
				primaryStage.setScene(dealPressed(0));
				pause1.setOnFinished(b->{
					primaryStage.setScene(dealPressed(1));
					pause2.setOnFinished(c->{
						primaryStage.setScene(dealPressed(2));
						pause3.setOnFinished(d->{
							primaryStage.setScene(dealPressed(3));
							pause4.setOnFinished(e->primaryStage.setScene(dealPressed(4)));
							pause4.play();
						});
						pause3.play();
					});
					pause2.play();
				});
				pause1.play();
			}
		});
		
		//EventHandler for showing the dealers cards.
		EventHandler<ActionEvent> showDealer = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent action) {
					primaryStage.setScene(showDealerCards(0));
					pause1.setOnFinished(b->{
						primaryStage.setScene(showDealerCards(1));
						pause2.setOnFinished(c->{
							primaryStage.setScene(showDealerCards(2));
							pause3.setOnFinished(d->{
								primaryStage.setScene(showDealerCards(3));
								pause4.setOnFinished(e->primaryStage.setScene(showDealerCards(4)));
								pause4.play();
							});
							pause3.play();
						});
						pause2.play();
					});
					pause1.play();
			}
		};
		
		//Button for when both players play.
		bothPlayButton = new Button("Both Play");
		bothPlayButton.setVisible(false);
		bothPlayButton.setOnAction(x->{
			
			//Set the play bet fields.
			playBet.setText(Integer.toString(player.anteBet));
			playBet2.setText(Integer.toString(player2.anteBet));
			
			//Set the play bets.
			player.playBet = player.anteBet;
			player2.playBet = player2.anteBet;
			
			//Show the dealer cards.
			pause0.setOnFinished(showDealer);
			pause0.play();
			
			pause5.setOnFinished(y-> {
				
				//Evaluate both players
				evaluatePlayer1();
				evaluatePlayer2();
				
				//Set new scene.
				primaryStage.setScene(sceneSetup());
			});
			pause5.play();
		});

		//Button for when both players fold.
		bothFoldButton = new Button("Both Fold");
		bothFoldButton.setVisible(false);
		
		bothFoldButton.setOnAction(x->{
			//Show dealer cards.
			pause0.setOnFinished(showDealer);
			pause0.play();
			
			
			pause5.setOnFinished(y-> {
				
				//Update Total Winnings and status label for both players.
				playerStatusLabel.setText("Player 1 Folded!\n -Ante - PPbet");
				player.totalWinnings = player.totalWinnings - player.anteBet - player.pairPlusBet;
				
				player2StatusLabel.setText("Player 2 Folded!\n -Ante - PPbet");
				player2.totalWinnings = player2.totalWinnings - player2.anteBet - player2.pairPlusBet;
				
				//Set new scene.
				primaryStage.setScene(sceneSetup());
				
			});
			pause5.play();
		});
		
		
		p1FoldButton = new Button("Player 1 Folds, Player 2 Plays");
		p1FoldButton.setOnAction(x->{
			playBet2.setText(anteBet2.getText());
			pause0.setOnFinished(showDealer);
			pause0.play();
			pause5.setOnFinished(y-> {
				
				//Evaluate Player 2 and update labels and winnings.
				evaluatePlayer2();
				playerStatusLabel.setText("Player 1 Folded!\n -Ante - PPbet");
				player.totalWinnings = player.totalWinnings - player.anteBet - player.pairPlusBet;
				
				//Set new scene.
				primaryStage.setScene(sceneSetup());
			});
			pause5.play();
		});
		p1FoldButton.setVisible(false);
		
		
		p2FoldButton = new Button("Player 2 Folds, Player 1 Plays");
		p2FoldButton.setOnAction(x->{
			playBet.setText(anteBet.getText());
			pause0.setOnFinished(showDealer);
			pause0.play();
			pause5.setOnFinished(y-> {
				
				//Evaluate Player 1 and update labels and winnings.
				evaluatePlayer1();
				player2StatusLabel.setText("Player 2 Folded!\n -Ante - PPbet");
				player2.totalWinnings = player2.totalWinnings - player2.anteBet - player2.pairPlusBet;
				
				//Set new scene.
				primaryStage.setScene(sceneSetup());
			});
			pause5.play();
		});
		p2FoldButton.setVisible(false);
		
		//Set up dealer and player Text labels.
		dealerLabel = new Text("Dealer");
		dealerLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bolder; ");
		dealerLabel.setStrokeWidth(2); 
	    dealerLabel.setStroke(Color.BLACK);   
		dealerLabel.setFill(Color.WHITE);
		
		playerLabel = new Text("Player 1");
		playerLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bolder;");
		playerLabel.setStrokeWidth(2); 
	    playerLabel.setStroke(Color.BLACK);  
		playerLabel.setFill(Color.WHITE);
		
		player2Label = new Text("Player 2");
		player2Label.setStyle("-fx-font-size: 40; -fx-font-weight: bolder;");
		player2Label.setStrokeWidth(2); 
	    player2Label.setStroke(Color.BLACK);  
		player2Label.setFill(Color.WHITE);
		
		playerStatusLabel = new Text("");
		playerStatusLabel.setStyle("-fx-font-size: 17; -fx-font-weight: bolder;");
		playerStatusLabel.setStrokeWidth(1); 
	    playerStatusLabel.setStroke(Color.BLACK);  
		playerStatusLabel.setFill(Color.WHITE);
		playerStatusLabel.setTextAlignment(TextAlignment.CENTER);
		//playerStatusLabel.setVisible(false);
		
		player2StatusLabel = new Text("");
		player2StatusLabel.setStyle("-fx-font-size: 17; -fx-font-weight: bolder;");
		player2StatusLabel.setStrokeWidth(1); 
	    player2StatusLabel.setStroke(Color.BLACK);  
		player2StatusLabel.setFill(Color.WHITE);
		player2StatusLabel.setTextAlignment(TextAlignment.CENTER);
		//player2StatusLabel.setVisible(false);
		
		winnings = new Text("  Total Winnings: " + player.totalWinnings);
		winnings.setStyle("-fx-font-size: 25; -fx-font-weight: bolder;");
		winnings.setStrokeWidth(1); 
		winnings.setStroke(Color.BLACK);   
		winnings.setFill(Color.WHITE);
		
		winnings2 = new Text("  Total Winnings: " + player2.totalWinnings);
		winnings2.setStyle("-fx-font-size: 25; -fx-font-weight: bolder;");
		winnings2.setStrokeWidth(1); 
		winnings2.setStroke(Color.BLACK);   
		winnings2.setFill(Color.WHITE);
		
		primaryStage.setScene(sceneSetup());
		primaryStage.show();
	}
	
	
	//Function for preferred settings on ImageViews.
	public void cardConfig(ImageView config) {
		config.setFitHeight(picHeight);
		config.setFitWidth(picWidth);
		config.setPreserveRatio(true);
	}
	
	//Adds preferred settings to VBox.
	public void vBConfig(VBox vb) {
		vb.setPrefWidth(75);
		vb.setAlignment(Pos.CENTER);
		vb.setStyle("-fx-border-color: black; -fx-background-color: DarkGreen;");
	}
	
	//Function for creating, setting, and showing scene after changes.
	public Scene sceneSetup() {
		
		//Update Winnings Totals
		winnings.setText("Total Winnings: " + Integer.toString(player.totalWinnings));
		winnings2.setText("Total Winnings: " +Integer.toString(player2.totalWinnings));
		
		//Boxes for play bets.
		playBox = new VBox(playLabel, playBet);
		vBConfig(playBox);
		
		playBox2 = new VBox(playLabel2, playBet2);
		vBConfig(playBox2);
		
		//Boxes for ante bets.
		anteBox = new VBox(anteLabel, anteBet);
		vBConfig(anteBox);
		
		anteBox2 = new VBox(anteLabel2, anteBet2);
		vBConfig(anteBox2);
		
		//Boxes for pp bets.
		ppBox = new VBox(ppLabel, ppBet);
		vBConfig(ppBox);
		
		ppBox2 = new VBox(ppLabel2, ppBet2);
		vBConfig(ppBox2);
		
		//Boxes for grouping all bets.
		wagers = new HBox(30, playBox, anteBox, ppBox);
		wagers.setAlignment(Pos.CENTER);
		
		wagers2 = new HBox(30, playBox2, anteBox2, ppBox2);
		wagers2.setAlignment(Pos.CENTER);
		
		//Box for buttons.
		choices = new HBox(30, p1FoldButton, bothPlayButton, dealButton, bothFoldButton, p2FoldButton);
		choices.setAlignment(Pos.CENTER);
		
		//Boxes for player/dealer cards.
		playerBox = new HBox(10, playerCard1, playerCard2, playerCard3);
		//playerBox.setPrefWidth(500);
		playerBox.setAlignment(Pos.CENTER);
		
		playerBox2 = new HBox(10, player2Card1, player2Card2, player2Card3);
		//playerBox2.setPrefWidth(500);
		playerBox2.setAlignment(Pos.CENTER);
		
		dealerBox = new HBox(10, dealerCard1, dealerCard2, dealerCard3);
		dealerBox.setAlignment(Pos.CENTER);
		
		//Create box for center and bottom of pane.
		VBox cenPane = new VBox(20, dealerLabel, dealerBox,choices);
		cenPane.setAlignment(Pos.CENTER);
		
		//All player 1 Items.
		VBox player1Complete = new VBox(10, wagers, playerBox, playerLabel, winnings, playerStatusLabel);
		player1Complete.setAlignment(Pos.CENTER);
		
		//All player 2 Items.
		VBox player2Complete = new VBox(10, wagers2, playerBox2, player2Label, winnings2, player2StatusLabel);
		player2Complete.setAlignment(Pos.CENTER);
		
		//All player Items.
		HBox allPlayers = new HBox(60, player1Complete, player2Complete);
		allPlayers.setAlignment(Pos.CENTER);
		
		gameBox = new VBox(20, cenPane, allPlayers);
		gameBox.setAlignment(Pos.TOP_CENTER);
		
		//Create border pane and add items./*
		BorderPane pane = new BorderPane();
		pane.setTop(menuBar);
		pane.setCenter(gameBox);
		pane.setStyle("-fx-background-image: url(\"pokerbg.jpg\");-fx-background-size: 1280, 720;-fx-background-repeat: no-repeat;");
		
		//returns the new scene to be set.
		return new Scene(pane, 1000,720);
	}
	
	//Function for setting up dealt cards transition.
	public Scene dealPressed(int i) {
		
		if(i == 0) {
			//Make all buttons invisible during transition.
			dealButton.setVisible(false);
			bothPlayButton.setVisible(false);
			bothFoldButton.setVisible(false);
			p1FoldButton.setVisible(false);
			p2FoldButton.setVisible(false);
			
			//Set all cards to cardback.
			dealerCard1 = new ImageView(cardBack);
			dealerCard2 = new ImageView(cardBack);
			dealerCard3 = new ImageView(cardBack);
			playerCard1 = new ImageView(cardBack);
			playerCard2 = new ImageView(cardBack);
			playerCard3 = new ImageView(cardBack);
			player2Card1 = new ImageView(cardBack);
			player2Card2 = new ImageView(cardBack);
			player2Card3 = new ImageView(cardBack);
			
			//Set height, width, and ratio preservation for card Image Views.
			cardConfig(dealerCard1);
			cardConfig(dealerCard2);
			cardConfig(dealerCard3);
			cardConfig(playerCard1);
			cardConfig(playerCard2);
			cardConfig(playerCard3);
			cardConfig(player2Card1);
			cardConfig(player2Card2);
			cardConfig(player2Card3);
			
			//Set status labels to empty.
			playerStatusLabel.setText("");
			player2StatusLabel.setText("");
			
			playBet.clear();
			playBet2.clear();
		}
		if(i == 1) {
			//Deal hands.
			dealer.dealersHand = dealer.dealHand();
			player.hand = dealer.dealHand();
			player2.hand = dealer.dealHand();
			
			//Map first cards of each player to corresponding imageView.
			playerCard1 = cardMap.get(Character.toString(player.hand.get(0).suit) + Integer.toString(player.hand.get(0).value));
			cardConfig(playerCard1);
			
			player2Card1 = cardMap.get(Character.toString(player2.hand.get(0).suit) + Integer.toString(player2.hand.get(0).value));
			cardConfig(player2Card1);
			
		}
		else if (i == 2) {
			//Map second cards of each player to corresponding imageView.
			playerCard2 = cardMap.get(Character.toString(player.hand.get(1).suit) + Integer.toString(player.hand.get(1).value));
			cardConfig(playerCard2);
			
			player2Card2 = cardMap.get(Character.toString(player2.hand.get(1).suit) + Integer.toString(player2.hand.get(1).value));
			cardConfig(player2Card2);
		}
		else if(i == 3) {
			//Map thirst cards of each player to corresponding imageView.
			playerCard3 = cardMap.get(Character.toString(player.hand.get(2).suit) + Integer.toString(player.hand.get(2).value));
			cardConfig(playerCard3);
			
			player2Card3 = cardMap.get(Character.toString(player2.hand.get(2).suit) + Integer.toString(player2.hand.get(2).value));
			cardConfig(player2Card3);
		}
		else if(i == 4) {
			//Bring back buttons for transitions.
			dealButton.setVisible(false);
			bothPlayButton.setVisible(true);
			bothFoldButton.setVisible(true);
			p1FoldButton.setVisible(true);
			p2FoldButton.setVisible(true);
		}
		
		//Return the new scene.
		return sceneSetup();
	}
	
	//Function for setting up dealer cards transition.
	public Scene showDealerCards(int i) {
		
		
		if(i == 0) {
			
			//Hide all buttons
			dealButton.setVisible(false);
			bothPlayButton.setVisible(false);
			bothFoldButton.setVisible(false);
			p1FoldButton.setVisible(false);
			p2FoldButton.setVisible(false);
		}
		if(i == 1) {
			//Map first dealer Card to ImageView.
			dealerCard1 = cardMap.get(Character.toString(dealer.dealersHand.get(0).suit) + Integer.toString(dealer.dealersHand.get(0).value));
			cardConfig(dealerCard1);
			
		}
		else if (i == 2) {
			//Map second dealer Card to ImageView.
			dealerCard2 = cardMap.get(Character.toString(dealer.dealersHand.get(1).suit) + Integer.toString(dealer.dealersHand.get(1).value));
			cardConfig(dealerCard2);
		}
		else if(i == 3) {
			//Map third dealer Card to ImageView.
			dealerCard3 = cardMap.get(Character.toString(dealer.dealersHand.get(2).suit) + Integer.toString(dealer.dealersHand.get(2).value));
			cardConfig(dealerCard3);
		}
		else if(i == 4) {
			//Configure Buttons and text fields.
			bothPlayButton.setVisible(false);
			bothFoldButton.setVisible(false);
			p1FoldButton.setVisible(false);
			p2FoldButton.setVisible(false);
			
			dealButton.setVisible(true);
			
			anteBet.setDisable(false);
			anteBet2.setDisable(false);
			ppBet.setDisable(false);
			ppBet2.setDisable(false);
		}
		
		//Return the new scene.
		return sceneSetup();
	}
	
	//Evaluate player1's hand updating player1's values and total earnings label.
	public void evaluatePlayer1() {
		
		String result1 = "";
		String ppResult = "";
		
		if(ThreeCardLogic.evalPPWinnings(player.hand, player.pairPlusBet) != 0)
			ppResult += " + PPBet";
		else
			ppResult += " - PPBet";
		
		//Eval player 1
		//Player 1 wins
		if (ThreeCardLogic.compareHands(dealer.dealersHand, player.hand) == 2) {
			player.totalWinnings = player.totalWinnings + (player.anteBet *2 ) + (player.playBet *2 ) - player.pairPlusBet + ThreeCardLogic.evalPPWinnings(player.hand, player.pairPlusBet);
			result1 += "Player 1 Wins! \n+ 2*Ante + 2*Play" + ppResult;
		}
		else if (ThreeCardLogic.compareHands(dealer.dealersHand, player.hand) == 1) {
			player.totalWinnings = player.totalWinnings - player.anteBet - player.playBet - player.pairPlusBet + ThreeCardLogic.evalPPWinnings(player.hand, player.pairPlusBet);
			result1 += "Player 1 Loses! \n- Ante - Play" + ppResult;
		}
		else if(ThreeCardLogic.compareHands(dealer.dealersHand, player.hand) == 0) {
			result1 += "Player 1 Ties! \n+ Ante + Play" + ppResult;
			player.totalWinnings = player.totalWinnings + player.anteBet + player.playBet - player.pairPlusBet + ThreeCardLogic.evalPPWinnings(player.hand, player.pairPlusBet);
			
		}
		
		playerStatusLabel.setText(result1);
	}
	
	//Evaluate player2's hand updating player2's values and total earnings label.
	public void evaluatePlayer2() {
		
		String result2 = "";
		String ppResult2 = "";
		
		if(ThreeCardLogic.evalPPWinnings(player2.hand, player2.pairPlusBet) != 0)
			ppResult2 += " + PPBet";
		else
			ppResult2 += " - PPBet";
		
		//Eval player 2
		//Player 2 wins
		if (ThreeCardLogic.compareHands(dealer.dealersHand, player2.hand) == 2) {
			player2.totalWinnings = player2.totalWinnings + (player2.anteBet *2 ) + (player2.playBet *2 ) - player2.pairPlusBet + ThreeCardLogic.evalPPWinnings(player2.hand, player2.pairPlusBet);
			result2 += "Player 2 Wins! \n+ 2*Ante + 2*Play" + ppResult2;
		}
		else if (ThreeCardLogic.compareHands(dealer.dealersHand, player2.hand) == 1) {
			player2.totalWinnings = player2.totalWinnings - player2.anteBet - player2.playBet - player2.pairPlusBet + ThreeCardLogic.evalPPWinnings(player2.hand, player2.pairPlusBet);
			result2 += "Player 2 Loses! \n- Ante - Play" + ppResult2;
		}
		else if(ThreeCardLogic.compareHands(dealer.dealersHand, player2.hand) == 0) {
			player2.totalWinnings = player2.totalWinnings + player2.anteBet + player2.playBet - player2.pairPlusBet + ThreeCardLogic.evalPPWinnings(player2.hand, player2.pairPlusBet);
			result2 += "Player 2 Ties! \n+ Ante + Play" + ppResult2;
		}
		
		player2StatusLabel.setText(result2);
	}
	
}
