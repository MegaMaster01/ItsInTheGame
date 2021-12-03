package com.example.iitgv10.Controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class Img {
    int amountOfPositions = 30;
    int amountOfCards = 5;
    ArrayList<Image> positionImages = new ArrayList<>();
    ArrayList<Image> cardImages = new ArrayList<>();

    public void readImages() {
        // Image names named:
        // posImg0, posImg1, etc...
        // cardImg1, cardImg2, etc...

        for(int i = 0; i < amountOfPositions; i++){
            //read positionImages
            positionImages.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/iitgv10/Images/posImg"+i+".png"))));
        }
        for(int i = 1; i <= amountOfCards; i++){
            //read cardImages
            cardImages.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/iitgv10/Images/cardImg"+i+".png"))));
        }
    }

    public Image getPositionImage(int index){
        return positionImages.get(index);
    }
    public Image getCardImage(int index){
        return cardImages.get(index);
    }
}
