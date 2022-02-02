package view;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import controller.GameController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;

public class WindowGame extends Stage {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int BOTTOM_PANE_HEIGHT = 100;

    private static final double DUCK_FIT_WIDTH = 60;
    private static final double CLOUD_FIT_WIDTH = 80;

    private static final String IMAGE_PATH_BIRD = "/images/bird.gif";
    private static final String IMAGE_PATH_CLOUD = "/images/cloud.png";
    private static final String SOUND_PATH_SHOOT = "/sounds/shoot.mp3";
    private static final String SOUND_PATH_BIRD = "/sounds/bird.mp3";

    private final WindowMain windowMain;
    private final Pane paneGame;
    private final Label labelTimer;
    private final Label labelLivesCount;
    private final GameController gameController;
    private final Image imageDuck;
    private final Image imageCloud;
    private final Timeline timeline;
//    private final Media mediaPlayerShoot;
//    private final Media mediaPlayerBird;

    public WindowGame(WindowLevel windowLevel, WindowMain windowMain, GameLevel gameLevel) {

        this.windowMain = windowMain;

        imageDuck = new Image(this.getClass().getResource(IMAGE_PATH_BIRD).toExternalForm());
        imageCloud = new Image(this.getClass().getResource(IMAGE_PATH_CLOUD).toExternalForm());

//        mediaPlayerShoot = new MediaPlayer(new Media(this.getClass().getResource(SOUND_PATH_SHOOT).toExternalForm()));
//        mediaPlayerBird = new MediaPlayer(new Media(this.getClass().getResource(SOUND_PATH_BIRD).toExternalForm()));

        BorderPane pane = new BorderPane();

        paneGame = new Pane();
        paneGame.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setCenter(paneGame);

        BorderPane paneBottom = new BorderPane();
        paneBottom.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        paneBottom.setPrefHeight(BOTTOM_PANE_HEIGHT);
        pane.setBottom(paneBottom);

        labelTimer = new Label();
        labelTimer.setAlignment(Pos.CENTER);
        labelTimer.setFont(new Font(30));
        labelTimer.setPrefSize(WINDOW_WIDTH / 2.0, BOTTOM_PANE_HEIGHT);
        paneBottom.setLeft(labelTimer);

        labelLivesCount = new Label();
        labelLivesCount.setAlignment(Pos.CENTER);
        labelLivesCount.setFont(new Font(30));
        labelLivesCount.setPrefSize(WINDOW_WIDTH / 2.0, BOTTOM_PANE_HEIGHT);
        paneBottom.setRight(labelLivesCount);

        setTitle(windowMain.getStage().getTitle() + " | Level: " + gameLevel.getLevelName());
        initModality(Modality.WINDOW_MODAL);
        initOwner(windowMain.getStage());
        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.isControlDown() && keyEvent.isShiftDown() && keyEvent.getCode() == KeyCode.Q) {
                exitGame();
            }
        });
        scene.setCursor(Cursor.CROSSHAIR);
        setScene(scene);
        setResizable(false);

        gameController = new GameController(gameLevel, this);

        paneGame.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                gameController.hitBackground();
                mouseEvent.consume();
            }
        });

        timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(1000.0 / gameController.getFPS()),
                        event -> gameController.step()
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);

        setOnShown(windowEvent -> timeline.play());
        setOnCloseRequest(windowEvent -> exitGame());
        show();
    }

    public void createImageDuck(Duck duck) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(duck.getType().getImageEffectHue());

        ImageView imageView = new ImageView();
        imageView.setImage(imageDuck);
        imageView.setScaleX(duck.isFlipped() ? -1 : 1);
        imageView.setEffect(colorAdjust);
        imageView.setFitWidth(DUCK_FIT_WIDTH);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        imageView.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                gameController.hitDuck(duck);
                mouseEvent.consume();
            }
        });

        duck.setImageView(imageView);

        paneGame.getChildren().add(imageView);
        imageView.toBack();
    }

    public void createImageCloud(Cloud cloud) {
        ImageView imageView = new ImageView();
        imageView.setImage(imageCloud);
        imageView.setScaleX(cloud.isFlipped() ? -1 : 1);
        imageView.setFitWidth(CLOUD_FIT_WIDTH);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        imageView.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                gameController.hitCloud();
                mouseEvent.consume();
            }
        });

        cloud.setImageView(imageView);

        paneGame.getChildren().add(imageView);
    }

    public void removeObjectImage(MovableObject object) {
        paneGame.getChildren().remove(object.getImageView());
    }

    public void updateObjectPosition(MovableObject object) {
        ImageView imageView = object.getImageView();
        imageView.setX(object.getPosition().getX());
        imageView.setY(object.getPosition().getY());
    }

    public void updateLives(int lives) {
        labelLivesCount.setText("Lives: " + lives);
    }

    public void updateTimer(String gameDurationString) {
        labelTimer.setText("Time: " + gameDurationString);
    }

    public void youDie(long gameDuration) {
        exitGame();
        Platform.runLater(() -> {
            GameResult gameResult = windowMain.createNewGameResult(gameController.getGameLevel(), gameDuration);
            if (gameResult != null) {
                windowMain.showHighScores(gameResult);
            }
        });
    }

    public void playSoundShoot() {
//        mediaPlayerShoot.play();
        System.out.println("playSoundShoot");
    }

    public void playSoundBird() {
//        mediaPlayerBird.play();
        System.out.println("playSoundBird");
    }

    private void exitGame() {
        timeline.stop();
        close();
    }

    public Pane getPaneGame() {
        return paneGame;
    }
}
