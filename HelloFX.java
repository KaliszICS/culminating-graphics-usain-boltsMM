/**
 * File: Basketball Jam
 * Author: Mansoor Muhammad
 * Date Created: May 1, 2026
 * Date Last Modified: May 10, 2026
 */

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class HelloFX extends Application {

    boolean music = true;
    MediaPlayer musicPlayer;

    int score = 0;
    int level = 1;
    int timeLeft = 0;
    int scoreGoal = 0;
    int tutorialShots = 0;
    int bonusPracticeShots = 3;

    boolean holdingW = false;
    boolean ballMoving = false;
    boolean levelEnded = false;
    boolean bonusRound = false;
    boolean bonusPractice = false;

    double ballX = 450;
    double ballY = 500;
    double startBallX = 450;
    double startBallY = 500;

    double playerX = 450;
    double playerY = 500;

    double ballVelocityX = 0;
    double ballVelocityY = 0;
    double gravity = 0.4;
    double power = 0;
    double maxPower = 20.0;

    double obstacleY = 250;
    double obstacleDirection = 1;

    Stage mainStage;
    AnimationTimer gameLoop;

    ImageView ball;
    Rectangle player;
    Rectangle rim;
    Rectangle backboard;
    Rectangle scoreZone;
    Rectangle obstacle;

    Label scoreLabel;
    Label levelLabel;
    Label timerLabel;
    Label goalLabel;
    Label powerLabel;
    Label instructions;

    ArrayList<Circle> trajectoryDots = new ArrayList<Circle>();
    Random random = new Random();

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        showMenu();
    }

    public void showMenu() {
       stopGameLoop();

    StackPane menu = new StackPane();
    Pane menuItems = new Pane();

    Image courtImage = new Image("icons/basketballCourtsideView.png");
    ImageView court = new ImageView(courtImage);
    court.setFitWidth(1280);
    court.setFitHeight(720);

    Label title = new Label("NBA JAM");
    title.setLayoutX(520);
    title.setLayoutY(120);
    title.setStyle("-fx-font-size: 70px; -fx-text-fill: orange; -fx-font-weight: bold;");

    Button startButton = new Button("START");
    startButton.setLayoutX(565);
    startButton.setLayoutY(300);
    startButton.setPrefWidth(150);
    startButton.setPrefHeight(50);

    Button exitButton = new Button("EXIT");
    exitButton.setLayoutX(565);
    exitButton.setLayoutY(380);
    exitButton.setPrefWidth(150);
    exitButton.setPrefHeight(50);

    Button musicButton = new Button("Music: ✓");
    musicButton.setLayoutX(565);
    musicButton.setLayoutY(460);
    musicButton.setPrefWidth(150);
    musicButton.setPrefHeight(50);

    musicButton.setOnAction(e -> {
        music = !music;

        if (music) {
            musicButton.setText("Music: ✓");
            if (musicPlayer!=null){
            musicPlayer.play();
        } else {
            musicButton.setText("Music: X");

            if (musicPlayer!= null){
            musicPlayer.pause();
        }
    }
    }});

    startButton.setOnAction(e -> startLevel(1));
    exitButton.setOnAction(e -> mainStage.close());

    menuItems.getChildren().add(title);
    menuItems.getChildren().add(startButton);
    menuItems.getChildren().add(exitButton);
    menuItems.getChildren().add(musicButton);

    menu.getChildren().add(court);
    menu.getChildren().add(menuItems);

    Scene scene = new Scene(menu, 1280, 720);

    mainStage.setTitle("NBA JAM");
    mainStage.setScene(scene);
    mainStage.show();
}

    public void startLevel(int newLevel) {
        stopGameLoop();

        level = newLevel;
        score = 0;
        tutorialShots = 0;
        bonusPracticeShots = 3;

        holdingW = false;
        ballMoving = false;
        levelEnded = false;
        bonusRound = false;
        bonusPractice = false;

        power = 0;
        obstacle = null;
        obstacleY = 250;
        obstacleDirection = 1;

        if (level == 1) {
            scoreGoal = 3;
            timeLeft = 0;
        } else if (level == 2) {
            scoreGoal = 500;
            timeLeft = 180;
        } else if (level == 3) {
            scoreGoal = 700;
            timeLeft = 90;
        } else if (level == 4) {
            scoreGoal = 800;
            timeLeft = 60;
        } else if (level == 5) {
            scoreGoal = 900;
            timeLeft = 120;
        } else if (level == 6) {
            scoreGoal = 1000;
            timeLeft = 130;
        } else if (level == 7) {
            scoreGoal = 1100;
            timeLeft = 100;
        }

        playerX = 450;
        playerY = 500;

        startBallX = 450;
        startBallY = 500;

        ballX = startBallX;
        ballY = startBallY;

        showGame();
    }

    public void startBonusLevel() {
        stopGameLoop();

        level = 8;
        score = 0;
        scoreGoal = 1500;
        timeLeft = 120;
        bonusPracticeShots = 3;

        holdingW = false;
        ballMoving = false;
        levelEnded = false;
        bonusRound = true;
        bonusPractice = true;

        power = 0;
        obstacle = null;

        movePlayerForBonus();
        showGame();
    }

    public void showGame() {
        StackPane game = new StackPane();
        Pane gameItems = new Pane();

        Image courtImage = new Image("icons/basketballCourtsideView.png");
        ImageView court = new ImageView(courtImage);
        court.setFitWidth(1280);
        court.setFitHeight(720);

        Image ballImage = new Image("icons/ball.png");
        ball = new ImageView(ballImage);
        ball.setFitWidth(50);
        ball.setFitHeight(50);
        ball.setX(ballX);
        ball.setY(ballY);

        player = new Rectangle(40, 90);
        player.setX(playerX);
        player.setY(playerY);
        player.setFill(Color.DARKBLUE);

        rim = new Rectangle(940, 225, 70, 15);
        rim.setFill(Color.ORANGERED);

        backboard = new Rectangle(1010, 140, 15, 115);
        backboard.setFill(Color.WHITE);

        scoreZone = new Rectangle(955, 245, 40, 1);
        scoreZone.setFill(Color.TRANSPARENT);

        instructions = new Label(getInstructionsText());
        instructions.setLayoutX(300);
        instructions.setLayoutY(40);
        instructions.setStyle("-fx-font-size: 24px; -fx-text-fill: red;");

        Label musicStatus = new Label();

        if (music) {
            musicStatus.setText("Music is ON");
        } else {
            musicStatus.setText("Music is OFF");
        }

        musicStatus.setLayoutX(20);
        musicStatus.setLayoutY(20);
        musicStatus.setStyle("-fx-font-size: 22px; -fx-text-fill: black;");

        scoreLabel = new Label(getScoreText());
        scoreLabel.setLayoutX(20);
        scoreLabel.setLayoutY(55);
        scoreLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: black;");

        levelLabel = new Label(getLevelText());
        levelLabel.setLayoutX(20);
        levelLabel.setLayoutY(90);
        levelLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: black;");

        timerLabel = new Label(getTimerText());
        timerLabel.setLayoutX(20);
        timerLabel.setLayoutY(125);
        timerLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: black;");

        goalLabel = new Label(getGoalText());
        goalLabel.setLayoutX(20);
        goalLabel.setLayoutY(160);
        goalLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: black;");

        powerLabel = new Label("Power: 0");
        powerLabel.setLayoutX(20);
        powerLabel.setLayoutY(195);
        powerLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: black;");

        gameItems.getChildren().add(player);
        gameItems.getChildren().add(ball);
        gameItems.getChildren().add(instructions);
        gameItems.getChildren().add(musicStatus);
        gameItems.getChildren().add(rim);
        gameItems.getChildren().add(backboard);
        gameItems.getChildren().add(scoreZone);
        gameItems.getChildren().add(scoreLabel);
        gameItems.getChildren().add(levelLabel);
        gameItems.getChildren().add(timerLabel);
        gameItems.getChildren().add(goalLabel);
        gameItems.getChildren().add(powerLabel);

        if (level >= 5 && level <= 7) {
            obstacle = new Rectangle(15, 100);
            obstacle.setX(810);
            obstacle.setY(obstacleY);
            obstacle.setFill(Color.RED);
            gameItems.getChildren().add(obstacle);
        }

        if (bonusRound) {
            createTrajectoryDots(gameItems);
        }

        game.getChildren().add(court);
        game.getChildren().add(gameItems);

        Scene scene = new Scene(game, 1280, 720);
        mainStage.setScene(scene);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W && !ballMoving && !levelEnded) {
                holdingW = true;
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.W && holdingW && !ballMoving && !levelEnded) {
                holdingW = false;
                shootBall();
            }
        });

        startGameLoop();

        scene.getRoot().requestFocus();
    }

    public void startGameLoop() {
        gameLoop = new AnimationTimer() {
            long lastSecond = 0;
            long lastFrame = 0;

            @Override
            public void handle(long now) {
                if (lastFrame == 0) {
                    lastFrame = now;
                }

                double secondsPassed = (now - lastFrame) / 1000000000.0;
                lastFrame = now;

                updatePower();
                updateBall();
                updateObstacle(secondsPassed);
                updateTrajectory();

                if (lastSecond == 0) {
                    lastSecond = now;
                }

                if (now - lastSecond >= 1000000000) {
                    updateTimer();
                    lastSecond = now;
                }

                checkLevelCompleted();
            }
        };

        gameLoop.start();
    }

    public void updatePower() {
        if (holdingW && power < maxPower) {
            power = power + 0.2;

            if (power > maxPower) {
                power = maxPower;
            }

            powerLabel.setText("Power: " + (int) power);
        }
    }

    public void shootBall() {
        ballMoving = true;

        if (bonusRound) {
            double angle = getBonusAngle();
            ballVelocityX = Math.cos(angle) * power;
            ballVelocityY = -Math.sin(angle) * power;
        } else {
            ballVelocityX = power * 0.55;
            ballVelocityY = -power * 1.75;
        }

        if (power < 4) {
            ballVelocityX = 4;
            ballVelocityY = -4;
        }

        power = 0;
        powerLabel.setText("Power: 0");
    }

    public void updateBall() {
        if (ballMoving) {
            ballX = ballX + ballVelocityX;
            ballY = ballY + ballVelocityY;
            ballVelocityY = ballVelocityY + gravity;

            ball.setX(ballX);
            ball.setY(ballY);

            if (obstacle != null && ball.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                resetBall();
            }

            if (ball.getBoundsInParent().intersects(scoreZone.getBoundsInParent())) {
                madeShot();
            }

            if (ballY > 720 || ballX > 1280 || ballX < -50) {
                resetBall();
            }
        }
    }

    public void madeShot() {
        if (level == 1) {
            tutorialShots = tutorialShots + 1;
            scoreLabel.setText("Shots Made: " + tutorialShots);
        } else {
            score = score + 100;
            scoreLabel.setText("Score: " + score);
        }

        if (bonusPractice) {
            bonusPracticeShots = bonusPracticeShots - 1;

            if (bonusPracticeShots > 0) {
                instructions.setText("Practice with the yellow shot arch. Practice shots left: " + bonusPracticeShots);
            } else {
                bonusPractice = false;
                instructions.setText("Bonus Round started! Score 1500 points before time runs out.");
            }
        }

        resetBall();
    }

    public void resetBall() {
        ballX = startBallX;
        ballY = startBallY;
        ballVelocityX = 0;
        ballVelocityY = 0;
        ballMoving = false;
        holdingW = false;
        power = 0;

        if (bonusRound) {
            movePlayerForBonus();
            player.setX(playerX);
            player.setY(playerY);
            ballX = startBallX;
            ballY = startBallY;
        }

        ball.setX(ballX);
        ball.setY(ballY);
        powerLabel.setText("Power: 0");
    }

    public void updateObstacle(double secondsPassed) {
        if (obstacle == null) {
            return;
        }

        if (level == 5) {
            obstacle.setY(250);
            return;
        }

        double speed;

        if (level == 6) {
            speed = 50;
        } else {
            speed = 100;
        }

        obstacleY = obstacleY + obstacleDirection * speed * secondsPassed;

        if (obstacleY >= 430) {
            obstacleY = 430;
            obstacleDirection = -1;
        }

        if (obstacleY <= 150) {
            obstacleY = 150;
            obstacleDirection = 1;
        }

        obstacle.setY(obstacleY);
    }

    public void updateTimer() {
        if (level == 1 || bonusPractice || levelEnded) {
            return;
        }

        timeLeft = timeLeft - 1;
        timerLabel.setText("Time: " + timeLeft);

        if (timeLeft <= 0 && score < scoreGoal) {
            levelEnded = true;
            showFailureScreen();
        }
    }

    public void checkLevelCompleted() {
        if (levelEnded) {
            return;
        }

        if (level == 1 && tutorialShots >= 3) {
            levelEnded = true;
            showLevelCompleteScreen();
        } else if (bonusRound && !bonusPractice && score >= scoreGoal) {
            levelEnded = true;
            showGameCompletedScreen();
        } else if (!bonusRound && level >= 2 && score >= scoreGoal) {
            levelEnded = true;
            showLevelCompleteScreen();
        }
    }

    public void showLevelCompleteScreen() {
        stopGameLoop();

        StackPane screen = new StackPane();
        Pane items = new Pane();

        Image courtImage = new Image("icons/basketballCourtsideView.png");
        ImageView court = new ImageView(courtImage);
        court.setFitWidth(1280);
        court.setFitHeight(720);

        Label message = new Label("Level Complete!");
        message.setLayoutX(455);
        message.setLayoutY(180);
        message.setStyle("-fx-font-size: 60px; -fx-text-fill: lime; -fx-font-weight: bold;");

        Button nextButton = new Button("NEXT LEVEL");
        nextButton.setLayoutX(565);
        nextButton.setLayoutY(320);
        nextButton.setPrefWidth(160);
        nextButton.setPrefHeight(50);

        Button menuButton = new Button("MENU");
        menuButton.setLayoutX(565);
        menuButton.setLayoutY(390);
        menuButton.setPrefWidth(160);
        menuButton.setPrefHeight(50);

        if (level < 7) {
            nextButton.setOnAction(e -> startLevel(level + 1));
        } else {
            nextButton.setText("BONUS LEVEL");
            nextButton.setOnAction(e -> startBonusLevel());
        }

        menuButton.setOnAction(e -> showMenu());

        items.getChildren().add(message);
        items.getChildren().add(nextButton);
        items.getChildren().add(menuButton);

        screen.getChildren().add(court);
        screen.getChildren().add(items);

        Scene scene = new Scene(screen, 1280, 720);
        mainStage.setScene(scene);
    }

    public void showFailureScreen() {
        stopGameLoop();

        StackPane screen = new StackPane();
        Pane items = new Pane();

        Image courtImage = new Image("icons/basketballCourtsideView.png");
        ImageView court = new ImageView(courtImage);
        court.setFitWidth(1280);
        court.setFitHeight(720);

        Label message = new Label("You Failed!");
        message.setLayoutX(500);
        message.setLayoutY(180);
        message.setStyle("-fx-font-size: 60px; -fx-text-fill: red; -fx-font-weight: bold;");

        Button restartButton = new Button("RESTART");
        restartButton.setLayoutX(565);
        restartButton.setLayoutY(320);
        restartButton.setPrefWidth(160);
        restartButton.setPrefHeight(50);

        Button menuButton = new Button("MENU");
        menuButton.setLayoutX(565);
        menuButton.setLayoutY(390);
        menuButton.setPrefWidth(160);
        menuButton.setPrefHeight(50);

        restartButton.setOnAction(e -> {
            if (bonusRound) {
                startBonusLevel();
            } else {
                startLevel(level);
            }
        });

        menuButton.setOnAction(e -> showMenu());

        items.getChildren().add(message);
        items.getChildren().add(restartButton);
        items.getChildren().add(menuButton);

        screen.getChildren().add(court);
        screen.getChildren().add(items);

        Scene scene = new Scene(screen, 1280, 720);
        mainStage.setScene(scene);
    }

    public void showGameCompletedScreen() {
        stopGameLoop();

        StackPane screen = new StackPane();
        Pane items = new Pane();

        Image courtImage = new Image("icons/basketballCourtsideView.png");
        ImageView court = new ImageView(courtImage);
        court.setFitWidth(1280);
        court.setFitHeight(720);

        Label message = new Label("GAME COMPLETED");
        message.setLayoutX(380);
        message.setLayoutY(120);
        message.setStyle("-fx-font-size: 70px; -fx-text-fill: gold; -fx-font-weight: bold;");

        Polygon star = createStar(640, 340, 120, 55);
        star.setFill(Color.GOLD);
        star.setStroke(Color.WHITE);
        star.setStrokeWidth(4);

        Button menuButton = new Button("BACK TO MENU");
        menuButton.setLayoutX(550);
        menuButton.setLayoutY(540);
        menuButton.setPrefWidth(180);
        menuButton.setPrefHeight(50);

        menuButton.setOnAction(e -> showMenu());

        items.getChildren().add(message);
        items.getChildren().add(star);
        items.getChildren().add(menuButton);

        screen.getChildren().add(court);
        screen.getChildren().add(items);

        Scene scene = new Scene(screen, 1280, 720);
        mainStage.setScene(scene);
    }

    public Polygon createStar(double centerX, double centerY, double outerRadius, double innerRadius) {
        Polygon star = new Polygon();

        for (int i = 0; i < 10; i++) {
            double angle = Math.toRadians(-90 + i * 36);
            double radius;

            if (i % 2 == 0) {
                radius = outerRadius;
            } else {
                radius = innerRadius;
            }

            double x = centerX + Math.cos(angle) * radius;
            double y = centerY + Math.sin(angle) * radius;

            star.getPoints().add(x);
            star.getPoints().add(y);
        }

        return star;
    }

    public void createTrajectoryDots(Pane gameItems) {
        trajectoryDots.clear();

        for (int i = 0; i < 30; i++) {
            Circle dot = new Circle(4);
            dot.setFill(Color.YELLOW);
            trajectoryDots.add(dot);
            gameItems.getChildren().add(dot);
        }
    }

    public void updateTrajectory() {
        if (!bonusRound || ballMoving) {
            return;
        }

        double previewPower = power;

        if (previewPower < 4) {
            previewPower = 4;
        }

        double angle = getBonusAngle();
        double vx = Math.cos(angle) * previewPower;
        double vy = -Math.sin(angle) * previewPower;

        double x = startBallX;
        double y = startBallY;

        for (int i = 0; i < trajectoryDots.size(); i++) {
            x = x + vx;
            y = y + vy;
            vy = vy + gravity;

            trajectoryDots.get(i).setCenterX(x + 25);
            trajectoryDots.get(i).setCenterY(y + 25);
        }
    }

    public double getBonusAngle() {
        double distance = 955 - playerX;

        if (distance > 400) {
            return Math.toRadians(60);
        } else if (distance > 250) {
            return Math.toRadians(52);
        } else {
            return Math.toRadians(45);
        }
    }

    public void movePlayerForBonus() {
        playerX = 200 + random.nextInt(401);
        playerY = 500;

        startBallX = playerX;
        startBallY = playerY;

        ballX = startBallX;
        ballY = startBallY;
    }

    public String getInstructionsText() {
        if (level == 1) {
            return "Hold W and release when you think the power is sufficient to make a shot.";
        } else if (bonusRound) {
            return "Bonus: use the yellow shot arch. You get 3 practice shots first.";
        } else if (level >= 5) {
            return "Avoid the red block. Shoot above the block.";
        }

        return "Hold W to build power. Release W to shoot.";
    }

    public String getScoreText() {
        if (level == 1) {
            return "Shots Made: " + tutorialShots;
        }

        return "Score: " + score;
    }

    public String getLevelText() {
        if (bonusRound) {
            return "Bonus Level";
        }

        if (level == 1) {
            return "Level 1: Tutorial";
        }

        return "Level: " + level;
    }

    public String getTimerText() {
        if (level == 1) {
            return "Time: None";
        }

        return "Time: " + timeLeft;
    }

    public String getGoalText() {
        if (level == 1) {
            return "Goal: Make 3 shots";
        }

        return "Goal: " + scoreGoal;
    }

    public void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

   

    public static void main(String[] args) {
        launch();
    }
}