package view;

import controller.GameController;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.GameResult;
import model.HighScores;

public class WindowHighScores extends Stage {

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 30;
    private static final int LIST_WIDTH = 300;
    private static final int LIST_HEIGHT = 400;

    public WindowHighScores(Stage parentStage, HighScores highScores, GameResult gameResultToShow) {

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20));

        Label label = new Label("High Scores:");
        label.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        label.setAlignment(Pos.CENTER);
        pane.setTop(label);
        BorderPane.setAlignment(label, Pos.CENTER);

       // ListView version:
//        ObservableList<GameResult> observableList = FXCollections.observableList(highScores.getList());
//        ListView<GameResult> listView = new ListView<>(observableList);
//        listView.setPrefSize(LIST_WIDTH, LIST_HEIGHT);
//        pane.setCenter(listView);
//        pane.setMargin(listView, new Insets(10, 0, 20, 0));

         //TableView version:
        ObservableList<GameResult> observableList = FXCollections.observableList(highScores.getList());
        TableView<GameResult> tableView = new TableView<>(observableList);
        TableColumn<GameResult, String> columnGameLevel = new TableColumn<>("Level");
        TableColumn<GameResult, String> columnGameDuration = new TableColumn<>("Time");
        TableColumn<GameResult, String> columnPlayerName = new TableColumn<>("Name");
        columnGameLevel.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getGameLevel().getLevelName()));
        columnGameDuration.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(GameController.gameDurationToString(cellData.getValue().getGameDuration())));
        columnPlayerName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPlayerName()));
        columnGameLevel.setPrefWidth(60);
        columnPlayerName.setPrefWidth(140);
        tableView.getColumns().add(columnGameLevel);
        tableView.getColumns().add(columnGameDuration);
        tableView.getColumns().add(columnPlayerName);
        tableView.setPlaceholder(new Label("No game results to display"));
        tableView.setPrefSize(LIST_WIDTH, LIST_HEIGHT);
        pane.setCenter(tableView);
        BorderPane.setMargin(tableView, new Insets(10, 0, 20, 0));

        Button buttonBack = new Button("Back");
        buttonBack.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        buttonBack.setCancelButton(true);
        buttonBack.setOnAction(actionEvent -> close());
        pane.setBottom(buttonBack);
        BorderPane.setAlignment(buttonBack, Pos.CENTER);

        setTitle(parentStage.getTitle());
        initModality(Modality.WINDOW_MODAL);
        initOwner(parentStage);

        Scene scene = new Scene(pane);
        setScene(scene);
        setOnShown(windowEvent -> {
            if (gameResultToShow != null) {
                tableView.getSelectionModel().select(gameResultToShow);
                tableView.scrollTo(gameResultToShow);
            }
        });

        show();
    }
}
