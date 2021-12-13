package com.example.iitgv10.Controller;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Controller {
    public ImageView imgWheel;
    public AnchorPane paneGame;
    public Label lblInformationDialog;
    public ImageView imgCurrentPlace;
    public Label lblContinue;
    public CheckBox ckb3Players;
    public CheckBox ckb4Players;
    public AnchorPane paneBackground;
    public ImageView imgBackground;
    public ImageView imgCurrentPlayer;
    public ImageView imgEaserEgg;
    public ImageView imgBackGround_welcome;
    ArrayList<Circle> movingCircle;
    //variables
    ArrayList<Player> players;
    Game game = new Game();
    Items items = new Items();
    Player activePlayer = new Player();
    Reader reader = new Reader();
    Img img = new Img();
    Boolean listenForButtonClick = false;
    int AMOUNT_OF_POSITIONS = 28;
    int AMOUNT_OF_PLAYERS = 4;
    int lastScore = 0;
    ArrayList<Label> positionLabels;
    Circle mc = new Circle();
    Scene scene;
    Image standardWheelImage;

    public Button btnEnterGame;
    public AnchorPane paneDescription;

    public void initialize(){
        //this will execute at the beginning of the program
        game.setUp(this, reader, imgWheel);
        reader.setup("src/main/information.csv"); //read data from file
        items.setUpItems();

        standardWheelImage = imgWheel.getImage();
        //imgBackground.setFitWidth(paneGame.getWidth());
        //imgBackground.setFitHeight(paneGame.getHeight());
        //img.readImages();

        //set the panes right
        paneDescription.setVisible(true);
        paneGame.setVisible(false);
        //set variables right
        //imgTrace.setVisible(false);
//        imgCurrentPlace.setImage(new Image(this.getClass().getResourceAsStream("Images/wheel.png")));

    }

    public void setPlayerTurn(int player){
        System.out.println(imgCurrentPlayer.getImage().getUrl());
        Image img = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/com/example/iitgv10/Images/player_images/gamepad"+player+".png")));
        imgCurrentPlayer.setImage(img);
        Player p = players.get(player-1);
        activePlayer = p;
    }

    public void createPlayers(){
        //here we create 4 new players
        players = new ArrayList<>();
        Player player1 = new Player(); //create a new player
        player1.setName("Player 1"); //give player  name
        player1.setPosition(0);     //give starting position (0 = start)
        player1.setColor(Color.RED); //give each player a color
        player1.setPlayerNum(1);     //set player number (to use easier)
        Player player2 = new Player(); //repeat.....
        player2.setName("Player 2");
        player2.setPosition(0);
        player2.setColor(Color.BLUE);
        player2.setPlayerNum(2);
        Player player3 = new Player();
        player3.setName("Player 3");
        player3.setPosition(0);
        player3.setColor(Color.GREEN);
        player3.setPlayerNum(3);
        players.add(player1);
        players.add(player2);
        players.add(player3);
        Player player4 = new Player();
        if(AMOUNT_OF_PLAYERS ==4){
            player4.setName("Player 4");
            player4.setPosition(0);
            player4.setColor(Color.YELLOW);
            player4.setPlayerNum(4);
            players.add(player4);
        }
    }

    public void EnterGame() {
        //The user has clicked to enter the game
        paneDescription.setVisible(false); //set the description invisible
        paneGame.setVisible(true); //and set the playing pane visible
        createPlayers();
        setPlayerTurn(1);
    }

    public void spinWheel() {
        scene = lblInformationDialog.getScene();
        lastScore = activePlayer.getPosition();
        game.spinWheel();
    }

    public void setPlayerScore(int score){
        if(activePlayer.getPosition() + score > AMOUNT_OF_POSITIONS){
            activePlayer.setPosition(activePlayer.getPosition() + score - AMOUNT_OF_POSITIONS);
        }else{
            activePlayer.setPosition(activePlayer.getPosition() + score);
        }

        boolean action = !reader.positionRules.get(activePlayer.getPosition()).contains("pos");

        System.out.println(activePlayer.getName() + ": " + activePlayer.getPosition());

        //////////////////////////////////
        // change image and information //
        //////////////////////////////////
        lblInformationDialog.setText(reader.getData('p', activePlayer.getPosition()+1));

        if(!action){
            listenForButtonClick = true;
            listenForKeyPressed(score);
        }else{
            game.getPlayerAction();
            game.playerScore = score;
        }
    }

    public void listenForKeyPressed(int score){
        if(score == -2){
            //Jatten functie!
            scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if(listenForButtonClick && keyEvent.getCode().equals(KeyCode.SPACE)){
                    listenForButtonClick = false;
                    game.spinWheelForJatten();
                    //spin wheel
                }
            });
        }
        else if(score == -1){
            lblContinue.setText("Press enter to continue!");
            positionLabels = drawPositions(score, "forwards");
            //imgTrace.setVisible(true);
            movingCircle =  moveCircle(game.playerScore, "forwards");
            lblContinue.setVisible(true);
            listenForButtonClick = true;

            scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if(listenForButtonClick && keyEvent.getCode().equals(KeyCode.ENTER)){
                    //switch to different player
                    //imgTrace.setVisible(false);

                    lblContinue.setVisible(false);
                    listenForButtonClick = false;

                    lblInformationDialog.setText("");
                    if(activePlayer.getPlayerNum() == AMOUNT_OF_PLAYERS){
                        setPlayerTurn(1); // player 1 again
                        paneGame.getChildren().removeAll(positionLabels);
                        paneGame.getChildren().removeAll(movingCircle);
                        System.out.println("Removed " + movingCircle.get(0).getId());
                    }else{
                        setPlayerTurn(activePlayer.getPlayerNum() + 1);
                        paneGame.getChildren().removeAll(positionLabels);
                        paneGame.getChildren().removeAll(movingCircle);
                        System.out.println("Removed " + movingCircle.get(0).getId());
                    }
                }
            });
        }else{
            lblContinue.setVisible(true);
            positionLabels = drawPositions(score, "forwards");

            movingCircle =  moveCircle(score, "forwards");

            scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if(listenForButtonClick && keyEvent.getCode().equals(KeyCode.ENTER)){
                    lblInformationDialog.setText("");

                    //imgTrace.setVisible(false);

                    imgWheel.setDisable(false);
                    lblContinue.setVisible(false);
                    listenForButtonClick = false;

                    //switch to different player
                    if(activePlayer.getPlayerNum() == 4){
                        setPlayerTurn(1); // player 1 again
                        paneGame.getChildren().removeAll(positionLabels);
                        paneGame.getChildren().removeAll(movingCircle);
                        System.out.println("Removed " + movingCircle.get(0).getId());
                    }else{
//                    setPlayerTurn(1); // player 1 again
                        setPlayerTurn(activePlayer.getPlayerNum() + 1);
                        paneGame.getChildren().removeAll(positionLabels);
                        paneGame.getChildren().removeAll(movingCircle);
                        System.out.println("Removed " + movingCircle.get(0).getId());
                    }
                    if(activePlayer.isSkip()){
                        activePlayer.setSkip(false);
                        if(activePlayer.getPlayerNum() == AMOUNT_OF_PLAYERS){
                            setPlayerTurn(1);
                        }else{
                            setPlayerTurn(activePlayer.getPlayerNum() + 1);
                        }
                    }
                }
            });
        }
    }

    public ArrayList<Label> drawPositions(int score, String direction){
        ArrayList<Label> labels = new ArrayList<>();

//        int x = (int)imgTrace.getLayoutX() + 55, y = (int)imgTrace.getLayoutY() + 40;
        int x = 125,y = 760;
        if(direction.equals("backwards")){
            for(int i = 6; i >= 0; i--){
                Label l = new Label();

                if(i == 0) {
                    l.setText(lastScore + ""); }
                else{
                    if(lastScore-i < 0){
                        l.setText((AMOUNT_OF_POSITIONS - i + lastScore) + "");
                    }else{
                        l.setText((lastScore - i) + "");
                    }
                }

                l.setLayoutX(x + (5*100) - (i-1)*100);
                l.setLayoutY(y);
                l.setFont(new Font("Rockwell Nova Bold", 30));
                labels.add(l);
            }
        }
        else{
            for(int i = 1; i <= 7; i++){
                Label l = new Label();

                int pos = activePlayer.getPosition();

                if(direction.equals("forwards")){
                    if(lastScore <= AMOUNT_OF_POSITIONS){
                        if(lastScore == 0){
                            l.setText(i-1 + "");
                        }else if(pos+lastScore <= 6){
                            l.setText((lastScore + i-1) + "");
                        }
                        else if(pos < AMOUNT_OF_POSITIONS-6){
                            l.setText((lastScore + i-1)+"");
                        }else{
                            if(lastScore+i-1 <= AMOUNT_OF_POSITIONS){
                                l.setText(lastScore+i-1 + "");
                            }else{
                                l.setText(lastScore+i-1-AMOUNT_OF_POSITIONS + "");
                            }
                        }
                    }

                    l.setLayoutX(x + (i-1)*104);
                    l.setLayoutY(y);
                    l.setFont(new Font("Rockwell Nova Bold", 30));
                    labels.add(l);
                }
            }
        }



        //imgTrace.setVisible(true);
        paneGame.getChildren().addAll(labels);

        return labels;
    }

    public ArrayList<Circle> moveCircle(int score, String direction){
        //used for traceability
        Circle movingCircle = new Circle();
        movingCircle.setRadius(15);

        movingCircle.setLayoutY(600 + 40);
        movingCircle.setFill(activePlayer.getColor());

        TranslateTransition tt = new TranslateTransition();

        if(direction.equals("forwards")){
            movingCircle.setLayoutX(100 + 55);
            tt.setByX(score * 104);
        }else{
            movingCircle.setLayoutX(100 + 600 - 55);
            tt.setByX(-(score * 104));
        }
        paneGame.getChildren().add(movingCircle);

        tt.setNode(movingCircle);
        tt.setDuration(Duration.millis(1000+score*500));
        tt.setCycleCount(0);
        tt.setAutoReverse(false);
        tt.playFromStart();

        System.out.println(movingCircle.getId());
        ArrayList<Circle> list = new ArrayList<>();
        list.add(movingCircle);
        return list;
    }

    //region changeAmountOfPlayers
    public void set3players(ActionEvent actionEvent) {
        ckb4Players.setSelected(false);
        AMOUNT_OF_PLAYERS = 3;
    }

    public void set4players(ActionEvent actionEvent) {
        ckb3Players.setSelected(false);
        AMOUNT_OF_PLAYERS = 4;
    }
    //endregion

    public void backToWelcomeWords(MouseEvent mouseEvent) {
        paneDescription.setVisible(true); //set the description invisible
        paneGame.setVisible(false); //and set the playing pane visible
        imgWheel.setRotate(0);
        lblInformationDialog.setText("");
        createPlayers();
        imgWheel.setDisable(false);
    }

    public void btnEnterGame(MouseEvent mouseEvent) {
        EnterGame();
    }

    public void easerEgg(MouseEvent mouseEvent) {
        System.out.println("Clicked");
        imgBackGround_welcome.setVisible(false);
        ckb3Players.setVisible(false);
        ckb3Players.setDisable(true);
        ckb4Players.setVisible(false);
        ckb4Players.setDisable(true);
    }
}
