package com.example.iitgv10.Controller;

import java.util.ArrayList;
import java.util.Collections;

public class Reader {
    CsvReader r;
    ArrayList<String> cardInformation = new ArrayList<>();
    ArrayList<String> positionInformation = new ArrayList<>();
    ArrayList<String> cardRules = new ArrayList<>();
    ArrayList<String> positionRules = new ArrayList<>();

    ArrayList<Card> cards = new ArrayList<>();

    public void setup(String path){
        r = new CsvReader(path);
        r.setSeparator(';');
        readData();
    }

    public void readData(){
        /*
        Position rule possibilities:
        tringtring
        jatten
        hoo wat is het
        casino
        rechtzaak


        Card rule possibilities:


         */
        r.skipRow();

        boolean saveData = false;
        char saveDataToList = ' ';

        while(r.loadRow()){
            boolean readData = true;
            if(saveData){
                String data = r.getString(0);
                if(saveDataToList == 'c'){
                    Card c = new Card();
                    c.setInformation(data.substring(4));
                    c.setRule(r.getString(1));
                    cards.add(c);
                    cardInformation.add(data.substring(4)); //skip first 4 spaces
                    cardRules.add(r.getString(1));
                }else if(saveDataToList == 'p'){
                    positionInformation.add(data.substring(4)); //skip first 4 spaces
                    positionRules.add(r.getString(1));
                }

                saveData = false;
                readData = false;
            }

            if(r.getString(0).contains("card") && readData){
                r.skipRow();
                saveData = true;
                saveDataToList = 'c';
            }
            else if(r.getString(0).contains("pos") && readData){
                r.skipRow();
                saveData = true;
                saveDataToList = 'p';
            }
            //System.out.println(r.getString(0));
        }
        //System.out.println(cardInformation.size() + " " + cardInformation.get(0));
       // System.out.println(positionInformation.size() + " " + positionInformation.get(0));
        //System.out.println(cardRules.size() + " " + cardRules.get(0));
        //System.out.println(positionRules.size() + " " + positionRules.get(0));
    }

    public String getData(char fromWhichList, int numberOfCardOrPosition){
        if(fromWhichList == 'c'){
            return cardInformation.get(numberOfCardOrPosition-1);
        }else if(fromWhichList == 'p'){
            return positionInformation.get(numberOfCardOrPosition-1);
        }else{
            return "";
        }
    }

    public Card getRandomCard(){
        Collections.shuffle(cards);
        return cards.get(0);
    }
}
