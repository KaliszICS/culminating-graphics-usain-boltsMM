/**
        * File: Basketball Jam
        * Author: Mansoor Muhammad
        * Date Created: May 1, 2026
        * Date Last Modified: May 2, 2026
        */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        StackPane stackPane = new StackPane();
        Rectangle court  = new rectangle(1280, 720,());

        Scene scene = new Scene(new StackPane(l), 1280, 720);
        stage.setScene(scene);
        stage.show();

        
    }

    public static void main(String[] args) {
        launch();
    }

}