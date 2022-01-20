package com.example.iitgv10.Controller;

import java.io.File;

import javafx.event.EventType;
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
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Controller {
    public ImageView imgWheel;
    public AnchorPane paneGame;
    public Label lblInformationDialog;
    public Label lblContinue;
    public CheckBox ckb3Players;
    public CheckBox ckb4Players;
    public AnchorPane paneBackground;
    public ImageView imgBackground;
    public ImageView imgCurrentPlayer;
    public ImageView imgEaserEgg;
    public ImageView imgBackGround_welcome;
    public ImageView imgCurrentplace;
    //ArrayList<Circle> movingCircle;
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
    //ArrayList<Label> positionLabels;
    Circle mc = new Circle();
    Scene scene;
    Image standardWheelImage;
    String buttonFunction;
    int currentScore;

    public Button btnEnterGame;
    public AnchorPane paneDescription;

    public void initialize(){
        //this will execute at the beginning of the program
        game.setUp(this, reader, imgWheel);
        reader.setup("src/main/information.csv"); //read data from file
        items.setUpItems();
        setImage("placeholder");

        standardWheelImage = imgWheel.getImage();
        lblInformationDialog.setText("Welkom bij Studio Rivals!\n\nKlik op het wiel om te beginnen!\n\nSpeler 1 mag beginnen.");
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
        //System.out.println(imgCurrentPlayer.getImage().getUrl());
        Image img = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/com/example/iitgv10/Images/player_images/gamepad"+player+".png")));
        imgCurrentPlayer.setImage(img);
        Player p = players.get(player-1);
        activePlayer = p;

        System.out.println("P1: " + players.get(0).getPosition());
        System.out.println("P2: " + players.get(1).getPosition());
        System.out.println("P3: " + players.get(2).getPosition());
        if(AMOUNT_OF_PLAYERS == 4){
            System.out.println("P4: " + players.get(3).getPosition());
        }
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
        scene = lblInformationDialog.getScene();
        createKeyListeners();
        createPlayers();
        setPlayerTurn(1);
    }

    public void spinWheel() {
        lastScore = activePlayer.getPosition();
        game.spinWheel();
    }

    public void setPlayerScore(int score){
        if(activePlayer.getPosition() + score >= AMOUNT_OF_POSITIONS){
            activePlayer.setPosition(activePlayer.getPosition() + score - AMOUNT_OF_POSITIONS);
        }else{
            activePlayer.setPosition(activePlayer.getPosition() + score);
        }

        boolean action = !reader.positionRules.get(activePlayer.getPosition()).contains("pos");

        //System.out.println(activePlayer.getName() + ": " + activePlayer.getPosition());

        //////////////////////////////////
        // change image and information //
        //////////////////////////////////
        lblInformationDialog.setText(reader.getData('p', activePlayer.getPosition()+1));

        if(!action){
            setListenToFunction("no_action", score);
        }else{
            game.getPlayerAction();
            game.playerScore = score;
        }
    }

    public int getNextPos(int currentPlace, int moveAmount, String direction){
        if(direction.equals("forwards")){
            if((currentPlace + moveAmount) < AMOUNT_OF_POSITIONS){
                return currentPlace + moveAmount;
            }else{
                return (currentPlace + moveAmount) - AMOUNT_OF_POSITIONS;
            }
        }else{
            if((currentPlace - moveAmount) >= 0){
                return currentPlace - moveAmount;
            }else{
                return (currentPlace - moveAmount) + AMOUNT_OF_POSITIONS;
            }
        }
    }

    int random;
    public void setListenToFunction(String function, int score){
        buttonFunction = function;
        currentScore = score;

        switch (buttonFunction){
            case "no_action":
                lblContinue.setVisible(true);
                //positionLabels = drawPositions(currentScore, "forwards");

                //movingCircle =  moveCircle(currentScore, "forwards");
                break;
            case "stappen_achteruit":
                random = ThreadLocalRandom.current().nextInt(1, 3+1);
                int pos = 0;

                if(activePlayer.getPosition() < random){
                    pos = activePlayer.getPosition() - random + AMOUNT_OF_POSITIONS;
                }else{
                    pos = activePlayer.getPosition() - random;
                }

                lblInformationDialog.setText(lblInformationDialog.getText() + "\n\n" +
                        "Ga " + random + " stappen achteruit (ga naar:"+pos+").\nDruk op enter.");
                listenForButtonClick = true;
                //positionLabels = drawPositions(getNextPos(activePlayer.getPosition(), pos, "backwards"), "backwards");

                //movingCircle =  moveCircle(getNextPos(activePlayer.getPosition(), pos, "backwards"), "backwards");
                break;
            case "stappen_vooruit":
                random = ThreadLocalRandom.current().nextInt(1, 3+1);
                int pos1 = 0;

                pos1 = activePlayer.getPosition() + random;
                if(pos1 >= 28){
                    pos1 -= AMOUNT_OF_POSITIONS;
                }

                lblInformationDialog.setText(lblInformationDialog.getText() + "\n\n" +
                        "Ga " + random + " stappen vooruit (ga naar:"+pos1+").\nDruk op enter.");
                listenForButtonClick = true;
                //positionLabels = drawPositions(getNextPos(activePlayer.getPosition(), pos1, "forwards"), "forwards");

                //movingCircle =  moveCircle(getNextPos(activePlayer.getPosition(), pos1, "forwards"), "forwards");
                break;
            case "min_one":
                lblContinue.setText("Druk op enter om door te gaan!");
                //positionLabels = drawPositions(score, "forwards");
                //imgTrace.setVisible(true);
                //movingCircle =  moveCircle(game.playerScore, "forwards");
                lblContinue.setVisible(true);
                listenForButtonClick = true;
                break;
            case "jat_o_hell":
                break;
            default:
                break;
        }
    }
    public void createKeyListeners(){
        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                switch (buttonFunction){
                    case "no_action":
                        buttonFunction = "";
                        no_action();
                        break;
                    case "goto_start":
                        buttonFunction = "";
                        goto_start();
                        break;
                    case "stappen_achteruit":
                        buttonFunction = "";
                        stappen_achteruit();
                        break;
                    case "stappen_vooruit":
                        buttonFunction = "";
                        stappen_vooruit();
                        break;
                    case "hoo_wat_is_het":
                        buttonFunction = "";
                        hoo_wat_is_het();
                        break;
                    case "jat_o_hell":
                        buttonFunction = "";
                        jat_o_hell();
                        break;
                    case "min_one":
                        buttonFunction = "";
                        min_one();
                        break;
                    case "continue":
                        buttonFunction = "";
                        System.out.println("Continue!");
                        break;
                    default:
                        break;
                }

                //paneGame.getChildren().removeAll(positionLabels);
                //paneGame.getChildren().removeAll(movingCircle);

                System.out.println("Enter pressed!");
            }
        });
    }

    public void no_action(){
        lblInformationDialog.setText("");

        //imgTrace.setVisible(false);

        imgWheel.setDisable(false);
        lblContinue.setVisible(false);
        listenForButtonClick = false;
        setImage("placeholder");

        //switch to different player
        if(activePlayer.getPlayerNum() == AMOUNT_OF_PLAYERS){
            setPlayerTurn(1); // player 1 again
            //paneGame.getChildren().removeAll(positionLabels);
            //paneGame.getChildren().removeAll(movingCircle);
            //System.out.println("Removed " + movingCircle.get(0).getId());
        }else{
//                    setPlayerTurn(1); // player 1 again
            setPlayerTurn(activePlayer.getPlayerNum() + 1);
            //paneGame.getChildren().removeAll(positionLabels);
            //paneGame.getChildren().removeAll(movingCircle);
            //System.out.println("Removed " + movingCircle.get(0).getId());
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
    public void goto_start(){
        listenForButtonClick = false;
        activePlayer.setPosition(0);
        game.nextPlayer();
    }
    public void stappen_achteruit(){
        listenForButtonClick = false;
        if(activePlayer.getPosition() < random){
            activePlayer.setPosition(activePlayer.getPosition() - random + AMOUNT_OF_POSITIONS);
        }else{
            activePlayer.setPosition(activePlayer.getPosition() - random);
        }
        check_next_position();
    }
    public void stappen_vooruit(){
        listenForButtonClick = false;
        activePlayer.setPosition(activePlayer.getPosition() + random);
        if(activePlayer.getPosition() >= 28){
            activePlayer.setPosition(activePlayer.getPosition() - AMOUNT_OF_POSITIONS);
        }
        check_next_position();
    }
    public void hoo_wat_is_het(){
        listenForButtonClick = false;
        game.drawCard();
    }
    public void jat_o_hell(){
        listenForButtonClick = false;
        game.spinWheelForJatten();
    }
    public void min_one(){
        imgWheel.setDisable(false);
        lblContinue.setVisible(false);
        listenForButtonClick = false;
        setImage("placeholder");

        lblInformationDialog.setText("");
        if(activePlayer.getPlayerNum() == AMOUNT_OF_PLAYERS){
            setPlayerTurn(1); // player 1 again
            //paneGame.getChildren().removeAll(positionLabels);
            //paneGame.getChildren().removeAll(movingCircle);
            //System.out.println("Removed " + movingCircle.get(0).getId());
        }else{
            setPlayerTurn(activePlayer.getPlayerNum() + 1);
            //paneGame.getChildren().removeAll(positionLabels);
            //paneGame.getChildren().removeAll(movingCircle);
            //System.out.println("Removed " + movingCircle.get(0).getId());
        }
    }
    public void check_next_position(){
        System.out.println(reader.getData('p', activePlayer.getPosition()+1) + "    " + activePlayer.getPosition());
        boolean action = !reader.positionRules.get(activePlayer.getPosition()).contains("pos");
        lblInformationDialog.setText(reader.getData('p', activePlayer.getPosition()+1));

        if(!action){
            setListenToFunction("no_action", 0);
            game.nextPlayer();
        }else{
            game.getPlayerAction();
            game.playerScore = 0;
        }
    }
    public void setImage(String function){
        Image img = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/com/example/iitgv10/Images/card_images/"+function+".png")));
        imgCurrentplace.setImage(img);
    }

    /*
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

        //System.out.println(movingCircle.getId());
        ArrayList<Circle> list = new ArrayList<>();
        list.add(movingCircle);
        return list;
    }

     */

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
        //System.out.println("Clicked");
        imgBackGround_welcome.setVisible(false);
        ckb3Players.setVisible(false);
        ckb3Players.setDisable(true);
        ckb4Players.setVisible(false);
        ckb4Players.setDisable(true);
    }
}
