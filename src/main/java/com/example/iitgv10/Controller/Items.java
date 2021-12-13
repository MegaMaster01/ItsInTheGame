package com.example.iitgv10.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class Items {
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> randomItems = new ArrayList<>();

    public void setUpItems(){
        items.add("Bureau");
        items.add("Bureaustoel");
        items.add("PC");
        items.add("Tapijt");
        items.add("Whiteboard");
        items.add("Dressoir");
        items.add("Bank");
        items.add("Koelkast");
        items.add("Plant");
        items.add("Salontafel");
    }

    public String getItem(int index){
        return items.get(index);
    }

    public String getRandomItem(){
        randomItems = items;
        Collections.shuffle(randomItems);
        return randomItems.get(ThreadLocalRandom.current().nextInt(1, items.size()));
    }
}
