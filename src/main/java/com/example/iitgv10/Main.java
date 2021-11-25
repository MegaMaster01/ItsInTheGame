package com.example.iitgv10;

import com.example.iitgv10.Controller.Reader;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), stage.getWidth(), stage.getHeight());
        stage.setTitle("It's In The Game!");
        stage.setScene(scene);
        stage.setFullScreen(true);

        stage.show();

//        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent keyEvent) {
//                System.out.println("Key pressed " + keyEvent.getCode());
//            }
//        });

        //Reader r = new Reader();

//        r.setup("src/main/information.csv"); //read data from file

        System.out.println(stage.getWidth() + " x " + stage.getHeight());

        System.out.println(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}