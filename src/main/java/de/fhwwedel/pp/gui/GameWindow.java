/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/28/22, 6:12 PM by Carina The Latest changes made by Carina on 7/28/22, 6:12 PM All contents of "GameWindow" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.gui;

import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.game.AnimationTime;
import de.fhwwedel.pp.util.game.Team;
import de.fhwwedel.pp.util.game.TeamType;
import de.fhwwedel.pp.util.special.Constants;
import de.fhwwedel.pp.util.special.FileInputReader;
import de.fhwwedel.pp.util.special.FileOutputWriter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GameWindow extends Application implements Initializable {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label currentPlayerText;

    private static GameWindow gameWindow;
    @FXML
    private GridPane dividerGrid;

    @FXML
    private RadioMenuItem fastAnimationSpeedButton;
    @FXML
    private MenuItem endGameButton;
    @FXML
    private GridPane gameGrid;
    @FXML
    private ImageView imageMover;
    @FXML
    private ImageView imageRemover;
    @FXML
    private ImageView imageReplacer;
    @FXML
    private ImageView imageSwapper;

    @FXML
    private MenuItem loadGameButton;

    @FXML
    private RadioMenuItem lowAnimationSpeedButton;
    @FXML
    private GridPane innerGrid;

    @FXML
    private RadioMenuItem mediumAnimationSpeedButton;
    @FXML
    private GridPane masterGrid;

    @FXML
    private Menu menuComputer;

    @FXML
    private Menu menuPunkte;

    @FXML
    private Label moverAmountText;

    @FXML
    private MenuItem newGameButton;
    @FXML
    private MenuBar menuBar;
    @FXML
    private GridPane playersStuffGrid;

    @FXML
    private CheckMenuItem pointsPerRowColumnButton;

    @FXML
    private CheckMenuItem pointsPerTeamButton;
    @FXML
    private Label playersTurnLabel;
    @FXML
    private GridPane pointsTableGridPane;
    @FXML
    private ImageView pointsTableImageView;

    @FXML
    private Label removerAmountText;

    @FXML
    private Label replacerAmountText;

    @FXML
    private MenuItem saveGameButton;

    @FXML
    private CheckMenuItem showComputerHandButton;
    @FXML
    private Label pointsTableLabel;
    @FXML
    private GridPane specialImagesGrid;
    @FXML
    private GridPane specialStuffGrid;

    @FXML
    private Label swapperAmountText;
    @FXML
    private GridPane specialUsedGrid;

    public static void start() {
        launch();
    }

    @FXML
    private Label usedSpacialLabel;
    private Stage stage;

    public static GameWindow getGameWindow() {
        return gameWindow;
    }

    @FXML
    void changeAnimationSpeedFast(ActionEvent event) {
        Game.getGame().setAnimationTime(AnimationTime.FAST);

    }

    @FXML
    void changeAnimationSpeedLow(ActionEvent event) {
        Game.getGame().setAnimationTime(AnimationTime.SLOW);

    }

    @FXML
    void changeAnimationSpeedMedium(ActionEvent event) {
        Game.getGame().setAnimationTime(AnimationTime.MIDDLE);
    }

    @FXML
    void clickEndGameButton(ActionEvent event) {

    }

    @FXML
    void clickLoadGameButton(ActionEvent event) {
        FileInputReader.readFile(FileInputReader.selectFile(stage.getScene()));
        //TODO: update UI

    }

    @FXML
    void clickNewGameButton(ActionEvent event) {

        try {
            new CreateGame().start(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clickSaveGameButton(ActionEvent event) {
        FileOutputWriter.writeJSON(stage.getScene());

    }

    @FXML
    void onGrdPnMouseClicked(MouseEvent event) {

    }

    public static void notifyTurn(Player player) {
        var alert = new Alert(Alert.AlertType.INFORMATION, "The Player: \"" + player.getName() + "\" with ID: \"" + player.getPlayerID() + " is now your turn!");
        alert.setTitle("Next Turn");
        alert.setHeaderText("Next Players Turn");
        alert.showAndWait();
    }

    public static void gameWonNotification(Team won, boolean rowComplete) {
        String message;
        Team lost = won.getTeamType() == TeamType.VERTICAL ? Team.getHorizontalTeam() : Team.getVerticalTeam();
        if (rowComplete) {
            message = "Team: " + won.getTeamType().getTeamName() + " won, because the hit a full line!";
        } else if (won.getPoints() == lost.getPoints()) {
            message = "Draw! Both teams got the same amount of points (" + won.getPoints() + ")!";
        } else {
            message = "Team: " + won.getTeamType().getTeamName() + " won, because they have more points (" + won.getPoints() + ") than the other team (" + lost.getPoints() + ")!";
        }

        var alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Game finished");
        alert.setHeaderText("Game finished");
        alert.showAndWait();
        Game.setGame(new Game(null, new ArrayList<>()));
        gameWindow.stage.close();
        GameWindow.start();

    }

    @FXML
    void initialize() {
        assert currentPlayerText != null : "fx:id=\"currentPlayerText\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert dividerGrid != null : "fx:id=\"dividerGrid\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert endGameButton != null : "fx:id=\"endGameButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert fastAnimationSpeedButton != null : "fx:id=\"fastAnimationSpeedButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert gameGrid != null : "fx:id=\"gameGrid\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert imageMover != null : "fx:id=\"imageMover\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert imageRemover != null : "fx:id=\"imageRemover\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert imageReplacer != null : "fx:id=\"imageReplacer\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert imageSwapper != null : "fx:id=\"imageSwapper\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert innerGrid != null : "fx:id=\"innerGrid\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert loadGameButton != null : "fx:id=\"loadGameButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert lowAnimationSpeedButton != null : "fx:id=\"lowAnimationSpeedButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert masterGrid != null : "fx:id=\"masterGrid\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert mediumAnimationSpeedButton != null : "fx:id=\"mediumAnimationSpeedButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert menuComputer != null : "fx:id=\"menuComputer\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert menuPunkte != null : "fx:id=\"menuPunkte\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert moverAmountText != null : "fx:id=\"moverAmountText\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert newGameButton != null : "fx:id=\"newGameButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playersStuffGrid != null : "fx:id=\"playersStuffGrid\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playersTurnLabel != null : "fx:id=\"playersTurnLabel\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert pointsPerRowColumnButton != null : "fx:id=\"pointsPerRowColumnButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert pointsPerTeamButton != null : "fx:id=\"pointsPerTeamButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert pointsTableGridPane != null : "fx:id=\"pointsTableGridPane\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert pointsTableImageView != null : "fx:id=\"pointsTableImageView\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert pointsTableLabel != null : "fx:id=\"pointsTableLabel\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert removerAmountText != null : "fx:id=\"removerAmountText\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert replacerAmountText != null : "fx:id=\"replacerAmountText\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert saveGameButton != null : "fx:id=\"saveGameButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert showComputerHandButton != null : "fx:id=\"showComputerHandButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert specialImagesGrid != null : "fx:id=\"specialImagesGrid\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert specialStuffGrid != null : "fx:id=\"specialStuffGrid\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert specialUsedGrid != null : "fx:id=\"specialUsedGrid\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert swapperAmountText != null : "fx:id=\"swapperAmountText\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert usedSpacialLabel != null : "fx:id=\"usedSpacialLabel\" was not injected: check your FXML file 'GameWindow.fxml'.";


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameWindow = this;
    }

    private ImageView[][] gridImages;

    @Override
    public void start(Stage primaryStage) throws IOException {

        var loader = new FXMLLoader(getClass().getResource("/gui/GameWindow.fxml"));
        loader.setController(this);
        var root = (Parent) loader.load();
        primaryStage.setTitle("Crosswise");
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(root, 800, 600));
        initialize();
        stage = primaryStage;
        primaryStage.show();
        generateGrid();
    }

    private final HashMap<String, ImageView> fieldImages = new HashMap<>();

    public void generateGrid() {
        gridImages = new ImageView[Constants.GAMEGRID_ROWS][Constants.GAMEGRID_COLUMNS];
        int colcount = Constants.GAMEGRID_COLUMNS;
        gameGrid.getChildren().clear();
        int rowcount = Constants.GAMEGRID_ROWS;
        for (int r = 0; r < Constants.GAMEGRID_ROWS; r++) {
            for (int c = 0; c < Constants.GAMEGRID_COLUMNS; c++) {
                ImageView imgNew = new ImageView();
                int cellWidth = (int) gameGrid.getWidth() / colcount;
                int cellHeight = (int) gameGrid.getHeight() / rowcount;

                System.out.println("grdPn.getHeight() = " + gameGrid.getHeight());
                System.out.println("grdPn.getWidth() = " + gameGrid.getWidth());
                System.out.println("cellHeight = " + cellHeight);
                System.out.println("cellWidth = " + cellWidth);
                imgNew.setFitWidth(cellWidth);
                imgNew.setFitHeight(cellHeight);
                imgNew.setPreserveRatio(false);
                imgNew.setSmooth(true);
                String id = "gridToken" + c + r;
                fieldImages.put("" + c + "r", imgNew);
                imgNew.setId(id);

                System.out.println(imgNew.getId());

                Image img = new Image("/pictures/1 - sun.png");
                imgNew.setImage(img);

                this.gridImages[r][c] = imgNew;
                this.gameGrid.add(imgNew, c, r);

                //the image shall resize when the cell resizes
                imgNew.fitWidthProperty().bind(gameGrid.widthProperty().
                        divide(colcount));
                imgNew.fitHeightProperty().bind(gameGrid.heightProperty().
                        divide(rowcount));

            }
        }
    }

    public Label getCurrentPlayerText() {
        return currentPlayerText;
    }

    public Label getMoverAmountText() {
        return moverAmountText;
    }

    public Label getRemoverAmountText() {
        return removerAmountText;
    }

    public Label getReplacerAmountText() {
        return replacerAmountText;
    }

    public Label getSwapperAmountText() {
        return swapperAmountText;
    }

    public Stage getStage() {
        return stage;
    }
}
