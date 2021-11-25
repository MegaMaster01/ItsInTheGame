package com.example.iitgv10.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class Controller {
    public ImageView imgWheel;
    public Label lblTest;
    public Label lblPlayerToRoll;
    public AnchorPane paneGame;
    public Label lblInformationDialog;
    public ImageView imgCurrentplace;
    //variables
    ArrayList<Player> players;
    Game game = new Game();
    Player activePlayer = new Player();

    public Button btnEnterGame;
    public AnchorPane paneDescription;
    public AnchorPane paneLeft;

    public void initialize(){
        //this will execute at the beginning of the program
        //set the panes right
        paneDescription.setVisible(true);
        paneGame.setVisible(false);

        players = new ArrayList<>();
        createPlayers();

        setPlayerTurn(1);
    }

    public void setPlayerTurn(int player){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Player p = players.get(player-1);
        lblPlayerToRoll.setTextFill(p.getColor());
        lblPlayerToRoll.setText(p.getName() + "'s turn!"); // let people know who's first
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

    public void EnterGame(ActionEvent actionEvent) {
        //The user has clicked to enter the game
        paneDescription.setVisible(false); //set the description invisible
        paneGame.setVisible(true); //and set the playing pane visible
    }

    public void spinWheel(MouseEvent mouseEvent) {
        game.spinWheel(imgWheel, lblTest, this);
    }

    public void setPlayerScore(int score){
        activePlayer.setPosition(activePlayer.getPosition() + score);   //IF AMOUNT OF BOXES IS KNOWN, CREATE IF STATEMENT TO GO FROM (EXAMPLE) 30 --> 0
        System.out.println(activePlayer.getName() + ": " + activePlayer.getPosition());

        //////////////////////////////////
        // change image and information //
        //////////////////////////////////

        //switch to different player
        if(activePlayer.getPlayerNum() == 4){
            setPlayerTurn(1); // player 1 again
        }else{
            setPlayerTurn(activePlayer.getPlayerNum() + 1);
        }
    }
}
