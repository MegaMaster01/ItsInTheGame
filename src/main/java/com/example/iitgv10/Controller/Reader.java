package com.example.iitgv10.Controller;

import java.util.ArrayList;

public class Reader {
    CsvReader r;
    ArrayList<String> cardInformation = new ArrayList<>();
    ArrayList<String> positionInformation = new ArrayList<>();
    ArrayList<String> cardRules = new ArrayList<>();

    public void setup(String path){
        r = new CsvReader(path);
        r.setSeparator(';');
        readData();
    }

    public void readData(){
        r.skipRow();

        boolean saveData = false;
        char saveDataToList = ' ';

        while(r.loadRow()){
            boolean readData = true;
            if(saveData){
                String data = r.getString(0);
                if(saveDataToList == 'c'){
                    cardInformation.add(data.substring(4)); //skip first 4 spaces
                    cardRules.add(r.getString(1));
                }else if(saveDataToList == 'p'){
                    positionInformation.add(data.substring(4)); //skip first 4 spaces
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
        System.out.println(cardInformation.size() + " " + cardInformation.get(0));
        System.out.println(positionInformation.size() + " " + positionInformation.get(0));
        System.out.println(cardRules.size() + " " + cardRules.get(0));
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
}