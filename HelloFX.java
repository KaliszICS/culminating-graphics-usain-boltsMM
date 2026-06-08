/**
        * File: Basketball Jam
        * Author: Mansoor Muhammad
        * Date Created: May 1, 2026
        * Date Last Modified: May 7, 2026
        */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
    StackPane menu = new StackPane();

        Image courtImage = new Image("icons/basketballCourtsideView.png");
        ImageView court = new ImageView(courtImage);
        court.setFitWidth(1280);
        court.setFitHeight(720);

        Label title = new Label("NBA JAM");
        title.setLayoutX(520);
        title.setLayoutY(120);
        title.setStyle("-fx-font-size: 70px; -fx-text-fill: orange; -fx-font-weight: bold;");

        Button startButton = new Button("START");
        startButton.setLayoutX(1280);
        startButton.setLayoutY(720);
        startButton.setPrefWidth(150);
        startButton.setPrefHeight(50);

        Button exitButton = new Button("EXIT");
        exitButton.setLayoutX(565);
        exitButton.setLayoutY(380);
        exitButton.setPrefWidth(150);
        exitButton.setPrefHeight(50);

        Button musicButton = new Button("Music: ✓");
        musicButton.setLayoutX(50);
        musicButton.setLayoutY(500);
        musicButton.setPrefWidth(75);
        musicButton.setPrefHeight(25);
        /*
        musicButton.setOnAction(e -> {
            music = !music;

            if (music) {
                musicButton.setText("Music: ✓");
            } else {
                musicButton.setText("Music: X");
            }
        });
        */

        
        menu.getChildren().add(court);
        menu.getChildren().add(title);
        menu.getChildren().add(startButton);
        menu.getChildren().add(exitButton);
        menu.getChildren().add(musicButton);

        Scene scene = new Scene(menu, 1280, 720);

        startButton.setOnAction(e -> showGame(stage));
        exitButton.setOnAction(e -> stage.close());

        stage.setTitle("NBA JAM");
        stage.setScene(scene);
        stage.show();
    }

    public void showGame(Stage stage) {
        Pane game = new Pane();

        Image courtImage = new Image("icons/basketballCourtsideView.png");
        ImageView court = new ImageView(courtImage);
        court.setFitWidth(1280);
        court.setFitHeight(720);

        Image ballImage = new Image("icons/ball.png");
        ImageView ball = new ImageView(ballImage);
        ball.setFitWidth(50);
        ball.setFitHeight(50);
        ball.setX(150);
        ball.setY(500);

        Label instructions = new Label("Hold W and release when the player reaches the peak of his shot.");
        instructions.setLayoutX(300);
        instructions.setLayoutY(40);
        instructions.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        Label musicStatus = new Label();

        if (false) {
            musicStatus.setText("Music is ON");
        } else {
            musicStatus.setText("Music is OFF");
        }

        musicStatus.setLayoutX(20);
        musicStatus.setLayoutY(20);
        musicStatus.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");

        game.getChildren().add(court);
        game.getChildren().add(ball);
        game.getChildren().add(instructions);
        game.getChildren().add(musicStatus);

        Scene scene = new Scene(game, 1280, 720);
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }

}