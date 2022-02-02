package model;

import controller.GameController;

import java.io.Serializable;
import java.util.Objects;

public class GameResult implements Serializable {
    private final GameLevel gameLevel;
    private final long gameDuration;
    private final String playerName;

    public GameResult(GameLevel gameLevel, long gameDuration, String playerName) {
        this.gameLevel = gameLevel;
        this.gameDuration = gameDuration;
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public long getGameDuration() {
        return gameDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameResult that = (GameResult) o;
        return gameDuration == that.gameDuration && playerName.equalsIgnoreCase(that.playerName) && gameLevel == that.gameLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, gameLevel, gameDuration);
    }

    @Override
    public String toString() {
        return String.format(
                "%s: %s %s",
                gameLevel.getLevelName(),
                GameController.gameDurationToString(gameDuration),
                playerName
        );
    }
}
