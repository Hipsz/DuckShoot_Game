package view;

import controller.GameController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.GameLevel;
import model.GameResult;
import model.HighScores;

public class WindowMain extends Application {

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 30;

    private static final String HIGH_SCORES_FILE_PATH = "highscores.dat";

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        VBox pane = new VBox();
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(20);

        Label label = new Label("Select option:");
        label.setPrefWidth(BUTTON_WIDTH);
        label.setAlignment(Pos.CENTER);
        pane.getChildren().add(label);

        Button buttonNewGame = new Button("New Game");
        buttonNewGame.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        buttonNewGame.setDefaultButton(true);
        buttonNewGame.setOnAction(actionEvent -> showWindowLevel());
        pane.getChildren().add(buttonNewGame);

        Button buttonHighScores = new Button("High Scores");
        buttonHighScores.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        buttonHighScores.setOnAction(actionEvent -> showHighScores(null));
        pane.getChildren().add(buttonHighScores);

        Button buttonExit = new Button("Exit");
        buttonExit.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        buttonExit.setCancelButton(true);
        buttonExit.setOnAction(actionEvent -> stage.close());
        pane.getChildren().add(buttonExit);

        Scene scene = new Scene(pane);
        stage.setTitle("Duck Shooter game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public void showHighScores(GameResult gameResult) {
        HighScores highScores = new HighScores(HIGH_SCORES_FILE_PATH);
        highScores.load();
        new WindowHighScores(stage, highScores, gameResult);
    }

    public GameResult createNewGameResult(GameLevel gameLevel, long gameDuration) {
        TextInputDialog dialog = new TextInputDialog("Anonymous");
        dialog.setTitle(stage.getTitle());
        dialog.setHeaderText(
                String.format(
                        "You finished game level '%s' in time %s.%n"
                        + "Please enter your name:",
                        gameLevel.getLevelName(),
                        GameController.gameDurationToString(gameDuration)
                )
        );
        String playerName = dialog.showAndWait().orElse("").trim();
        if (!playerName.isEmpty()) {
            GameResult gameResult = new GameResult(gameLevel, gameDuration, playerName);
            addNewGameResultToHighScores(gameResult);
            return gameResult;
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showWindowLevel() {
        new WindowLevel(this);
    }

    private void addNewGameResultToHighScores(GameResult gameResult) {
        HighScores highScores = new HighScores(HIGH_SCORES_FILE_PATH);
        highScores.load();
        highScores.add(gameResult);
        highScores.save();
    }

}
