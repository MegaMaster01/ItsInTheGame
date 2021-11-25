package com.example.iitgv10.Controller;

import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    int lastRoll = 1;

    public int spinWheel(ImageView imageView, Label l, Controller c){
        int currentlyRolled = 0;
        int randomNum = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        int steps = 0;
        //System.out.println(lastRoll + "     " + randomNum);
        l.setText("The dice rolled: " + randomNum);
        currentlyRolled = randomNum;

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
        rt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                imageView.setDisable(false);
                c.setPlayerScore(randomNum);
            }
        });
        rt.play();

        lastRoll = lastRoll + test_steps;
        if(lastRoll > 6){
            lastRoll -= 6;
        }
        return currentlyRolled;
    }
}
