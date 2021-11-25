package com.example.iitgv10.Controller;

import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Controller {
    public ImageView imgWheel;
    public Label lblTest;
    public Label lblPlayerToRoll;
    public AnchorPane paneGame;
    public Label lblInformationDialog;
    public ImageView imgCurrentPlace;
    public Label lblContinue;
    public ImageView imgTrace;
    //variables
    ArrayList<Player> players;
    Game game = new Game();
    Player activePlayer = new Player();
    Reader reader = new Reader();
    Boolean listenForButtonClick = false;
    int AMOUNT_OF_POSITIONS = 30;
    int lastScore = 0;
    ArrayList<Label> positionLabels;
    Circle mc = new Circle();

    public Button btnEnterGame;
    public AnchorPane paneDescription;

    public void initialize(){
        //this will execute at the beginning of the program
        reader.setup("src/main/information.csv"); //read data from file

        //set the panes right
        paneDescription.setVisible(true);
        paneGame.setVisible(false);
        //set variables right
        imgTrace.setVisible(false);

        players = new ArrayList<>();
        createPlayers();

        setPlayerTurn(1);
    }

    public void setPlayerTurn(int player){
        Player p = players.get(player-1);
        lblPlayerToRoll.setTextFill(p.getColor());
        lblPlayerToRoll.setText(p.getName() + "'s turn!"); // let people know whose first
        activePlayer = p;
    }

    public void createPlayers(){
        //here we create 4 new players
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
        Player player4 = new Player();
        player4.setName("Player 4");
        player4.setPosition(0);
        player4.setColor(Color.YELLOW);
        player4.setPlayerNum(4);
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
    }

    public void EnterGame() {
        //The user has clicked to enter the game
        paneDescription.setVisible(false); //set the description invisible
        paneGame.setVisible(true); //and set the playing pane visible
    }

    public void spinWheel() {
        lastScore = activePlayer.getPosition();
        game.spinWheel(imgWheel, lblTest, this);
    }

    public void setPlayerScore(int score){
        activePlayer.setPosition(activePlayer.getPosition() + score);   //IF AMOUNT OF BOXES IS KNOWN, CREATE IF STATEMENT TO GO FROM (EXAMPLE) 30 --> 0
        System.out.println(activePlayer.getName() + ": " + activePlayer.getPosition());

        positionLabels = drawPositions(score);

        ArrayList<Circle> movingCircle =  moveCircle(score);

        //////////////////////////////////
        // change image and information //
        //////////////////////////////////
        lblInformationDialog.setText(reader.getData('p', score));

        listenForButtonClick = true;
        listenForKeyPressed(movingCircle);
    }

    public void listenForKeyPressed(ArrayList<Circle> movingCircle){
        lblContinue.setVisible(true);

        Scene scene = lblInformationDialog.getScene();
        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(listenForButtonClick && keyEvent.getCode().equals(KeyCode.ENTER)){

                imgTrace.setVisible(false);

                imgWheel.setDisable(false);
                lblContinue.setVisible(false);
                listenForButtonClick = false;

                //switch to different player
                if(activePlayer.getPlayerNum() == 4){
                    setPlayerTurn(1); // player 1 again
                    paneGame.getChildren().removeAll(positionLabels);
                    paneGame.getChildren().removeAll(movingCircle);
                }else{
                    //setPlayerTurn(1); // player 1 again
                    setPlayerTurn(activePlayer.getPlayerNum() + 1);
                    paneGame.getChildren().removeAll(positionLabels);
                    paneGame.getChildren().removeAll(movingCircle);
                }
            }
        });
    }

    public ArrayList<Label> drawPositions(int score){

        ArrayList<Label> labels = new ArrayList<>();

        int x = (int)imgTrace.getLayoutX() + 55, y = (int)imgTrace.getLayoutY() + 40;

        for(int i = 1; i <= 7; i++){
            Label l = new Label();

            int pos = activePlayer.getPosition();

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

        imgTrace.setVisible(true);
        paneGame.getChildren().addAll(labels);



        return labels;
    }

    public ArrayList<Circle> moveCircle(int score){
        //used for traceability
        Circle movingCircle = new Circle();
        movingCircle.setRadius(15);
        movingCircle.setLayoutX(imgTrace.getLayoutX() + 55);
        movingCircle.setLayoutY(imgTrace.getLayoutY() + 40);
        movingCircle.setFill(activePlayer.getColor());

        paneGame.getChildren().add(movingCircle);

        TranslateTransition tt = new TranslateTransition();
        tt.setNode(movingCircle);
        tt.setByX(score * 104);
        tt.setDuration(Duration.millis(score*500));
        tt.setCycleCount(0);
        tt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //reset ball to start
                TranslateTransition tt1 = new TranslateTransition();
                tt1.setNode(movingCircle);
                tt1.setByX(-score * 104);
                tt1.setDuration(Duration.millis(1));
                tt1.setCycleCount(0);
                tt1.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        tt.playFromStart();
                    }
                });
                tt1.playFromStart();
            }
        });
        tt.setAutoReverse(false);
        tt.playFromStart();

        ArrayList<Circle> list = new ArrayList<>();
        list.add(movingCircle);
        return list;
    }


}
