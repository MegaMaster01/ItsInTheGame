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
    int i = 1;
    int current_jatten_player = 1;
    int casino_roll;

    public void setUp (Controller _c, Reader _r, ImageView imgWheel){
        c = _c;
        r = _r;
        imageView = imgWheel;
    }

    public int spinWheel(){

        int randomNum = ThreadLocalRandom.current().nextInt(1, 6 + 1);
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
                tringtring();
                break;
            case "jatten":
                jatten();
                break;
            case "hoo wat is het":
                hooWatIsHet();
                break;
            case "rechtzaak":
                rechtzaak();
                break;
            case "casino":
                casino();
                break;
            default:
                c.listenForButtonClick = true;
                c.setListenToFunction("no_action", p_score);
                break;
        }
    }

    //region tringtring
    public void tringtring(){
        c.lblInformationDialog.setText(
                c.lblInformationDialog.getText() + "\n\n"
                + "Je moet het volgende item inleveren: " + c.items.getRandomItem() + "."
                + "\n\nHeb je dit item niet in je bezit? Dan heb je geluk gehad!"
        );
        c.listenForButtonClick = true;
        c.setListenToFunction("no_action", p_score);
    }
    //endregion

    //region jatten
    public void jatten(){
        c.lblInformationDialog.setText(
                c.lblInformationDialog.getText() + "\n" //newline
                + "Je mag jatten van een speler naar keuze!" + "\n"
                + "Speler " + c.activePlayer.getPlayerNum() + " gooit eerst."
        );
        c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n" +
                "Druk op enter om te gooien.");
        c.listenForButtonClick = true;
        c.setListenToFunction("jat_o_hell", p_score);
    }

    int score1 = 0;
    int score2 = 0;

    public void spinWheelForJatten(){
        int randomNum = ThreadLocalRandom.current().nextInt(1, 6 + 1);

        int counter = lastRoll;

        if(current_jatten_player == 1){
            score1 = randomNum;
        }else{
            score2 = randomNum;
        }

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
        rt.setByAngle((1080 + (60*test_steps))); //1080 +
        rt.setCycleCount(0);
        rt.setOnFinished(actionEvent -> {
            if(current_jatten_player == 1){
                c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n\n" +
                        "Tegenstander: druk op enter om te gooien.");
                current_jatten_player++;
                c.listenForButtonClick = true;
                c.setListenToFunction("jat_o_hell", p_score);
            }else{
                current_jatten_player = 1;
                System.out.println("Jatten uitslag:");

                if(score1 > score2){
                    c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n\n" +
                            "Speler "+c.activePlayer.getPlayerNum()+" wint en mag stelen!");
                }else if(score2 > score1){
                    c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n\n" +
                            "Oh nee!... Je tegenstander wint en mag iets van jou jatten...");
                }else if(score1 == score2){
                    c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n\n" +
                            "Het is gelijkspel. Er mag niet gejat worden.");
                }
                c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n\n" +
                        "Score speler "+c.activePlayer.getPlayerNum()+": "+score1+"  Score tegenstander: "+score2);
                c.listenForButtonClick = true;
                c.setListenToFunction("no_action", p_score);
            }

        });
        rt.play();

        lastRoll = lastRoll + test_steps;
        if(lastRoll > 6) {
            lastRoll -= 6;
        }
    }

    //endregion

    //region hooWatIsHet (UNFINISHED)
    public void hooWatIsHet(){
        c.lblInformationDialog.setText("Je moet een kaart trekken!" + "\n"+
                "Druk op enter");
        c.listenForButtonClick = true;
        c.setListenToFunction("hoo_wat_is_het", p_score);
        //nog maken!
    }
    public void drawCard(){
//        Card card = c.reader.getRandomCard();
        Card card = c.reader.cards.get(0);

        switch (card.rule){
            case "stappen_vooruit":
                stappenVooruit();
                break;
            case "deurwaarder":
                deurwaarder();
                break;
            case "jat-o-hell":
                jat_o_hell();
                break;
            case "goto_start":
                gotoStart();
                break;
            case "stappen_achteruit":
                stappenAchteruit();
                break;
            default:
                break;
        }
    }

    public void stappenVooruit(){
        c.listenForButtonClick = true;
        c.setListenToFunction("stappen_vooruit", p_score); //verwijs naar stappen vooruit
    }
    public void deurwaarder(){
        tringtring();
    }
    public void jat_o_hell(){
        c.lblInformationDialog.setText(c.lblInformationDialog.getText()+"\n"+
                "Jat-O-Hell! Je mag een willekeurig item van een willekeurig persoon jatten!\n"
                +"Druk op enter om door te gaan!"
        );
        c.setListenToFunction("no_action", p_score);
    }
    public void gotoStart(){
        c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n\n" +
                "Ga naar start!\nDruk op enter.");
        c.listenForButtonClick = true;
        c.setListenToFunction("goto_start", p_score);
    }
    public void stappenAchteruit(){
        c.listenForButtonClick = true;
        c.setListenToFunction("stappen_achteruit", p_score); //verwijs naar stappen achteruit
    }

    public void nextPlayer(){
        c.lblInformationDialog.setText("");
        imageView.setDisable(false);

        if(c.activePlayer.getPlayerNum() == 4){
            c.setPlayerTurn(1); // player 1 again
        }else{
            c.setPlayerTurn(c.activePlayer.getPlayerNum() + 1);
        }
    }
    //endregion

    //region rechtzaak
    public void rechtzaak(){
        c.activePlayer.setSkip(true);
        c.listenForButtonClick = true;
        c.setListenToFunction("no_action", p_score);
    }
    //endregion

    //region casino
    public void casino(){
        listenForSpace = true;
        casino_roll = ThreadLocalRandom.current().nextInt(1, 6 + 1);

        c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n" + "Gooi "+casino_roll+" om te winnen! Druk op spatie!");
        c.scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent ->{
            if(keyEvent.getCode().equals(KeyCode.SPACE) && listenForSpace){
                listenForSpace = false;
                rollAgain(casino_roll);
            }
        });
    }

    public void rollAgain(int whatToRoll){
        int randomNum = ThreadLocalRandom.current().nextInt(1, 6 + 1);
//        int randomNum = whatToRoll;

        System.out.println(lastRoll);
        int counter = lastRoll;

        int test_steps = 0;
        while(counter != randomNum){
            counter++;
            if(counter == 7){
                counter = 1;
            }
            test_steps++;
        }
        System.out.println(test_steps);

        imageView.setDisable(true);
        RotateTransition rt = new RotateTransition(Duration.millis(3000), imageView); //was 3000
        //int steps = randomNum - 1;
        rt.setByAngle((1080 + (60*test_steps))); //1080 +
        rt.setCycleCount(0);
        rt.setOnFinished(actionEvent -> {
            if(randomNum == whatToRoll){
                //.out.println("Won!");
                c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n" +
                        "Je hebt gewonnen! Je mag een item naar keuze uitzoeken!");
                c.listenForButtonClick = true;
                c.setListenToFunction("min_one", -1);

                //let player know he won!
            }else{
                //System.out.println("Did not win");
                c.lblInformationDialog.setText(c.lblInformationDialog.getText() + "\n" +
                        "Je hebt helaas verloren.");
                c.listenForButtonClick = true;
                c.setListenToFunction("min_one", -1);
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
