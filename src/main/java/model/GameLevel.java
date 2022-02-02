package model;

import java.io.Serializable;

public enum GameLevel implements Serializable {
    EASY("Easy", 0.5, 100, 200),
    MEDIUM("Medium", 0.7, 75, 150),
    HARD("Hard", 0.9, 50, 100);

    private final String levelName;
    private final double speedRatio;
    private final int newDuckBySteps;
    private final int newCloudBySteps;

    GameLevel(String levelName, double speedRatio, int newDuckBySteps, int newCloudBySteps) {
        this.levelName = levelName;
        this.speedRatio = speedRatio;
        this.newDuckBySteps = newDuckBySteps;
        this.newCloudBySteps = newCloudBySteps;
    }

    public String getLevelName() {
        return levelName;
    }

    public double getSpeedRatio() {
        return speedRatio;
    }

    public int getNewDuckBySteps() {
        return newDuckBySteps;
    }

    public int getNewCloudBySteps() {
        return newCloudBySteps;
    }

}
