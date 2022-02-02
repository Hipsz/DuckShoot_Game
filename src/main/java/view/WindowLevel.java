package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.GameLevel;

public class WindowLevel extends Stage {

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 30;

    private final WindowMain windowMain;

    public WindowLevel(WindowMain windowMain) {

        this.windowMain = windowMain;

        VBox pane = new VBox();
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(20);

        Label label = new Label("Select level:");
        label.setPrefWidth(BUTTON_WIDTH);
        label.setAlignment(Pos.CENTER);
        pane.getChildren().add(label);

        for (GameLevel gameLevel : GameLevel.values()) {
            Button buttonLevel = new Button(gameLevel.getLevelName());
            buttonLevel.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
            buttonLevel.setDefaultButton(gameLevel == GameLevel.EASY);
            buttonLevel.setOnAction(actionEvent -> {
                close();
                startGame(gameLevel);
            });
            pane.getChildren().add(buttonLevel);
        }

        Button buttonBack = new Button("Back");
        buttonBack.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        buttonBack.setCancelButton(true);
        buttonBack.setOnAction(actionEvent -> close());
        pane.getChildren().add(buttonBack);

        Stage parentStage = windowMain.getStage();

        setTitle(parentStage.getTitle());
        initModality(Modality.WINDOW_MODAL);
        initOwner(parentStage);

        Scene scene = new Scene(pane);
        setScene(scene);
        setResizable(false);
        show();
    }

    private void startGame(GameLevel gameLevel) {
        new WindowGame(this, windowMain, gameLevel);
    }

}
