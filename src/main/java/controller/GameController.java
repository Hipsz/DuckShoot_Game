package controller;

import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import model.*;
import view.WindowGame;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameController {

    private static final int GAME_FPS = 50;
    private static final int GAME_LIVES_COUNT = 10;

    private final GameLevel gameLevel;
    private final WindowGame windowGame;
    private final List<MovableObject> objects = new ArrayList<>();
    private final Random random = new Random();
    private final Date startDate = new Date();
    private int stepCounter = 0;
    private int livesCounter = GAME_LIVES_COUNT;

    public GameController(GameLevel gameLevel, WindowGame windowGame) {
        this.gameLevel = gameLevel;
        this.windowGame = windowGame;
        windowGame.updateLives(livesCounter);
    }

    public void step() {
        checkForNewObjects();
        moveAndAnimateObjects();
        checkOutedObjects();
        windowGame.updateTimer(gameDurationToString(getGameDurationMs()));
        stepCounter++;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public void hitDuck(Duck duck) {
        duck.hit();
        if (duck.isKilled()) {
            windowGame.playSoundBird();
        } else {
            windowGame.playSoundShoot();
        }
    }

    public void hitCloud() {
        windowGame.playSoundShoot();
    }

    public void hitBackground() {
        windowGame.playSoundShoot();
    }

    public int getFPS() {
        return GAME_FPS;
    }

    public static String gameDurationToString(long gameDurationMs) {
        long durationSeconds = gameDurationMs / 1000;
        long hours = durationSeconds / 3600;
        long minutes = (durationSeconds % 3600) / 60;
        long seconds = durationSeconds % 60;
        long millis = gameDurationMs % 1000;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }

    private void removeObject(MovableObject object) {
        windowGame.removeObjectImage(object);
        objects.remove(object);
    }

    private void moveAndAnimateObjects() {
        for (MovableObject object : objects) {
            object.move(gameLevel.getSpeedRatio());
            object.animate();
            windowGame.updateObjectPosition(object);
        }
    }

    private void checkOutedObjects() {
        Pane paneGame = windowGame.getPaneGame();
        Bounds windowBounds = new BoundingBox(0, 0, paneGame.getWidth(), paneGame.getHeight());
        for (MovableObject object : objects) {
            if (!object.isInsideBox(windowBounds)) {
                outedObject(object);
                Platform.runLater(() -> removeObject(object));
            }
        }
    }

    private void outedObject(MovableObject object) {
        if (object instanceof Duck) {
            Duck duck = (Duck) object;
            if (duck.isKilled()) {
                System.out.println("Outed killed duck");
            } else {
                System.out.println("Outed living duck");
                livesCounter--;
                if (livesCounter > 0) {
                    windowGame.updateLives(livesCounter);
                } else {
                    windowGame.youDie(getGameDurationMs());
                }
            }
        }
    }

    private void checkForNewObjects() {
        if (stepCounter % gameLevel.getNewDuckBySteps() == 0) {
            createNewRandomDuck();
        }
        if (stepCounter % gameLevel.getNewCloudBySteps() == 0) {
            createNewRandomCloud();
        }
    }

    private void createNewRandomDuck() {
        DuckType duckType = getRandomDuckType();
        boolean flipped = random.nextBoolean();
        Duck duck = new Duck(duckType, flipped);
        windowGame.createImageDuck(duck);
        duck.setPosition(getObjectRandomStartPosition(duck));
        windowGame.updateObjectPosition(duck);
        objects.add(duck);
    }

    private void createNewRandomCloud() {
        boolean flipped = random.nextBoolean();
        Cloud cloud = new Cloud(flipped);
        windowGame.createImageCloud(cloud);
        cloud.setPosition(getObjectRandomStartPosition(cloud));
        windowGame.updateObjectPosition(cloud);
        objects.add(cloud);
    }

    private DuckType getRandomDuckType() {
        int duckTypeIndex = random.nextInt(DuckType.values().length);
        return DuckType.values()[duckTypeIndex];
    }

    private Point2D getObjectRandomStartPosition(MovableObject object) {
        Pane paneGame = windowGame.getPaneGame();
        return new Point2D(
                object.isFlipped() ? paneGame.getWidth() : -object.getWidth(),
                random.nextDouble() * paneGame.getHeight() * 0.7);
    }

    private long getGameDurationMs() {
        Date now = new Date();
        return now.getTime() - startDate.getTime();
    }

}
