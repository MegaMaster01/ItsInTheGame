package com.example.iitgv10.Controller;

import javafx.animation.RotateTransition;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.concurrent.ThreadLocalRandom;

public class Game {
    int lastRoll = 1;
    Controller c;
    Reader r;
    ImageView imageView;
    int p_score = 0;
    boolean listenForSpace = false;

    public void setUp (Controller _c, Reader _r, ImageView imgWheel){
        c = _c;
        r = _r;
        imageView = imgWheel;
    }

    public int spinWheel(){
        int randomNum = ThreadLocalRandom.current().nextInt(1, 6 + 1);
//        int randomNum = 2;
        //System.out.println(lastRoll + "     " + randomNum);

        int counter = 0;
        counter = lastRoll;


        int test_steps = 0;
        while(counter != randomNum){
            counter++;
            if(counter == 7){
                counter = 1;
            }
            test_steps++;
        }

        imageView.setDisable(true);
        RotateTransition rt = new RotateTransition(Duration.millis(3000), imageView); //was 3000
        //int steps = randomNum - 1;
        rt.setByAngle((1080 + (60*test_steps))); //1080 +
        rt.setCycleCount(0);
        rt.setOnFinished(actionEvent -> {

            c.setPlayerScore(randomNum);

        });
        rt.play();

        lastRoll = lastRoll + test_steps;
        if(lastRoll > 6) {
            lastRoll -= 6;
        }

        return randomNum;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    int playerScore = 0;
    public void getPlayerAction(){
        String rule = r.positionRules.get(c.activePlayer.getPosition());
        switch (rule){
            case "tringtring":
                break;
            case "jatten":
                break;
            case "hoo wat is het":
                break;
            case "rechtzaak":
                break;
            case "casino":
                casino();
                break;
            default:
                break;
        }
    }

    public void tringtring(){

    }
    public void jatten(){

    }
    public void hooWatIsHet(){

    }
    public void rechtzaak(){

    }

    //region casino
    public void casino(){
        listenForSpace = true;
        int r = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n" + "Throw "+r+" to win! Press spacebar!");
        c.scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent ->{
            if(keyEvent.getCode().equals(KeyCode.SPACE) && listenForSpace){
                listenForSpace = false;
                rollAgain(r);
            }
        });
    }

    public void rollAgain(int whatToRoll){
        int randomNum = ThreadLocalRandom.current().nextInt(1, 6 + 1);
//        int randomNum = 2;

        int counter = lastRoll;

        int test_steps = 0;
        while(counter != randomNum){
            counter++;
            if(counter == 7){
                counter = 1;
            }
            test_steps++;
        }

        imageView.setDisable(true);
        RotateTransition rt = new RotateTransition(Duration.millis(3000), imageView); //was 3000
        //int steps = randomNum - 1;
        rt.setByAngle((1080 + (60*test_steps))); //1080 +
        rt.setCycleCount(0);
        rt.setOnFinished(actionEvent -> {
            if(randomNum == whatToRoll){
                System.out.println("Won!");
                c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n" +
                        "You Won! Choose any item you can use!");
                c.listenForButtonClick = true;
                c.listenForKeyPressed(-1);
                imageView.setDisable(false);
                //let player know he won!
            }else{
                System.out.println("Did not win");
                c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n" +
                        "Unfortunately you lost!");
                c.listenForButtonClick = true;
                c.listenForKeyPressed(-1);
                imageView.setDisable(false);
            }
        });
        rt.play();

        lastRoll = lastRoll + test_steps;
        if(lastRoll > 6) {
            lastRoll -= 6;
        }
    }
    //endregion
}
