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

import de.fhwwedel.pp.CrossWise;
import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.game.AnimationTime;
import de.fhwwedel.pp.util.game.Team;
import de.fhwwedel.pp.util.game.TeamType;
import de.fhwwedel.pp.util.special.FileInputReader;
import de.fhwwedel.pp.util.special.FileOutputWriter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private static GameWindow gameWindow;

    public static void notifyTurn(Player player) {
        Platform.runLater(() -> {
            var alert = new Alert(Alert.AlertType.INFORMATION,
                    "The Player: \"" + player.getName() + "\" with ID: \"" + player.getPlayerID()
                            + " is now your turn!");
            alert.setTitle("Next Turn");
            alert.setHeaderText("Next Players Turn");
            alert.showAndWait();
        });
    }

    public void replacerAmountText() {
        if (GameWindow.getGameWindow() != null)
            Platform.runLater(() -> replacerAmountText.setText(Integer.parseInt(replacerAmountText.getText()) + 1 + ""));

    }


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label currentPlayerText;

    @FXML
    private GridPane dividerGrid;

    @FXML
    private MenuItem endGameButton;

    @FXML
    private RadioMenuItem fastAnimationSpeedButton;

    @FXML
    private GridPane gameGrid;

    @FXML
    private GridPane horizontalPointsGrid;

    @FXML
    private ImageView imageMover;

    @FXML
    private ImageView imageRemover;

    @FXML
    private ImageView imageReplacer;

    @FXML
    private ImageView imageSwapper;

    @FXML
    private GridPane innerGrid;

    @FXML
    private MenuItem loadGameButton;

    @FXML
    private RadioMenuItem lowAnimationSpeedButton;

    @FXML
    private GridPane masterGrid;

    @FXML
    private RadioMenuItem mediumAnimationSpeedButton;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu menuComputer;

    @FXML
    private Menu menuPunkte;

    @FXML
    private Label moverAmountText;

    @FXML
    private MenuItem newGameButton;

    @FXML
    private ImageView playerHand1IconFour;

    @FXML
    private ImageView playerHand1IconOne;

    @FXML
    private ImageView playerHand1IconThree;

    @FXML
    private ImageView playerHand1IconTwo;

    @FXML
    private ImageView playerHand2IconFour;

    @FXML
    private ImageView playerHand2IconOne;

    @FXML
    private ImageView playerHand2IconThree;

    @FXML
    private ImageView playerHand2IconTwo;

    @FXML
    private ImageView playerHand3IconFour;

    @FXML
    private ImageView playerHand3IconOne;

    @FXML
    private ImageView playerHand3IconThree;

    @FXML
    private ImageView playerHand3IconTwo;

    @FXML
    private ImageView playerHand4IconFour;

    @FXML
    private ImageView playerHand4IconOne;

    @FXML
    private ImageView playerHand4IconThree;

    @FXML
    private ImageView playerHand4IconTwo;


    @FXML
    private GridPane playerHandFour;

    @FXML
    private GridPane playerHandOne;

    @FXML
    private GridPane playerHandThree;

    @FXML
    private GridPane playerHandTwo;

    @FXML
    private GridPane playersStuffGrid;

    @FXML
    private Label playersTurnLabel;

    @FXML
    private CheckMenuItem pointsPerRowColumnButton;

    @FXML
    private CheckMenuItem pointsPerTeamButton;

    @FXML
    private GridPane pointsTableGridPane;

    @FXML
    private ImageView pointsTableImageView;

    @FXML
    private Label pointsTableLabel;

    @FXML
    private Label removerAmountText;

    @FXML
    private Label replacerAmountText;

    @FXML
    private MenuItem saveGameButton;

    @FXML
    private CheckMenuItem showComputerHandButton;

    @FXML
    private GridPane specialImagesGrid;

    @FXML
    private GridPane specialStuffGrid;

    @FXML
    private GridPane specialUsedGrid;

    @FXML
    private Label swapperAmountText;

    @FXML
    private Label usedSpacialLabel;

    @FXML
    private GridPane verticalPointsGrid;

    public static void start() {
        launch();
    }

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

    public void removerAmountText() {
        if (GameWindow.getGameWindow() != null)
            Platform.runLater(() -> removerAmountText.setText(Integer.parseInt(removerAmountText.getText()) + 1 + "")
            );

    }

    public static void gameWonNotification(Team won, boolean rowComplete) {
        String message;
        Team lost = won.getTeamType() == TeamType.VERTICAL ?
                Team.getHorizontalTeam() :
                Team.getVerticalTeam();
        if (rowComplete) {
            message = "Team: " + won.getTeamType().getTeamName()
                    + " won, because the hit a full line!";
        } else if (won.getPoints() == lost.getPoints()) {
            message = "Draw! Both teams got the same amount of points (" + won.getPoints() + ")!";
        } else {
            message = "Team: " + won.getTeamType().getTeamName()
                    + " won, because they have more points (" + won.getPoints()
                    + ") than the other team (" + lost.getPoints() + ")!";
        }

        var alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Game finished");
        alert.setHeaderText("Game finished");
        alert.showAndWait();
        Game.setGame(new Game(null, new ArrayList<>()));
        gameWindow.stage.close();
        CrossWise.setGameThread(new Thread(() -> Game.getGame().start()));

        GameWindow.start();
    }

    @FXML
    void initialize() {
        assert currentPlayerText != null : "fx:id=\"currentPlayerText\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert dividerGrid != null : "fx:id=\"dividerGrid\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert endGameButton != null : "fx:id=\"endGameButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert fastAnimationSpeedButton != null : "fx:id=\"fastAnimationSpeedButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert gameGrid != null : "fx:id=\"gameGrid\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert horizontalPointsGrid != null : "fx:id=\"horizontalPointsGrid\" was not injected: check your FXML file 'GameWindow.fxml'.";
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
        assert playerHand1IconFour != null : "fx:id=\"playerHand1IconFour\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand1IconOne != null : "fx:id=\"playerHand1IconOne\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand1IconThree != null : "fx:id=\"playerHand1IconThree\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand1IconTwo != null : "fx:id=\"playerHand1IconTwo\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand2IconFour != null : "fx:id=\"playerHand2IconFour\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand2IconOne != null : "fx:id=\"playerHand2IconOne\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand2IconThree != null : "fx:id=\"playerHand2IconThree\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand2IconTwo != null : "fx:id=\"playerHand2IconTwo\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand3IconFour != null : "fx:id=\"playerHand3IconFour\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand3IconOne != null : "fx:id=\"playerHand3IconOne\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand3IconThree != null : "fx:id=\"playerHand3IconThree\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand3IconTwo != null : "fx:id=\"playerHand3IconTwo\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand4IconFour != null : "fx:id=\"playerHand4IconFour\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand4IconOne != null : "fx:id=\"playerHand4IconOne\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand4IconThree != null : "fx:id=\"playerHand4IconThree\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHand4IconTwo != null : "fx:id=\"playerHand4IconTwo\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHandFour != null : "fx:id=\"playerHandFour\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHandOne != null : "fx:id=\"playerHandOne\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHandThree != null : "fx:id=\"playerHandThree\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert playerHandTwo != null : "fx:id=\"playerHandTwo\" was not injected: check your FXML file 'GameWindow.fxml'.";
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
        assert verticalPointsGrid != null : "fx:id=\"verticalPointsGrid\" was not injected: check your FXML file 'GameWindow.fxml'.";

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameWindow = this;
        GameWindowHandler.setHandler(new GameWindowHandler(this));
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
        GameWindowHandler.getGameWindowHandler().generateGrid();
    }

    public HashMap<String, ImageView> getFieldImages() {
        return fieldImages;
    }

    private final HashMap<String, ImageView> fieldImages = new HashMap<>();

    public void moverAmountText() {
        if (GameWindow.getGameWindow() != null)
            Platform.runLater(() -> moverAmountText.setText(Integer.parseInt(moverAmountText.getText()) + 1 + "")
            );


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

    public void swapperAmountText() {
        if (GameWindow.getGameWindow() != null) {
            Platform.runLater(() -> swapperAmountText.setText(Integer.parseInt(swapperAmountText.getText()) + 1 + ""));

        }
    }

    public GridPane getGameGrid() {
        return gameGrid;
    }


    public GridPane getPlayerHandOne() {
        return playerHandOne;
    }

    public GridPane getPlayerHandTwo() {
        return playerHandTwo;
    }

    public ImageView getPlayerHand1IconFour() {
        return playerHand1IconFour;
    }

    public ImageView getPlayerHand1IconOne() {
        return playerHand1IconOne;
    }

    public ImageView getPlayerHand1IconThree() {
        return playerHand1IconThree;
    }

    public ImageView getPlayerHand1IconTwo() {
        return playerHand1IconTwo;
    }

    public ImageView getPlayerHand2IconFour() {
        return playerHand2IconFour;
    }

    public ImageView getPlayerHand2IconOne() {
        return playerHand2IconOne;
    }

    public ImageView getPlayerHand2IconThree() {
        return playerHand2IconThree;
    }

    public ImageView getPlayerHand2IconTwo() {
        return playerHand2IconTwo;
    }

    public ImageView getPlayerHand3IconFour() {
        return playerHand3IconFour;
    }

    public ImageView getPlayerHand3IconOne() {
        return playerHand3IconOne;
    }

    public ImageView getPlayerHand3IconThree() {
        return playerHand3IconThree;
    }

    public ImageView getPlayerHand3IconTwo() {
        return playerHand3IconTwo;
    }

    public ImageView getPlayerHand4IconFour() {
        return playerHand4IconFour;
    }

    public ImageView getPlayerHand4IconOne() {
        return playerHand4IconOne;
    }

    public ImageView getPlayerHand4IconThree() {
        return playerHand4IconThree;
    }

    public ImageView getPlayerHand4IconTwo() {
        return playerHand4IconTwo;
    }

    public GridPane getPlayerHandFour() {
        return playerHandFour;
    }

    public GridPane getPlayerHandThree() {
        return playerHandThree;
    }

    public ImageView[][] getGridImages() {
        return gridImages;
    }

    public CheckMenuItem getShowComputerHandButton() {
        return showComputerHandButton;
    }

    public void setGridImages(ImageView[][] gridImages) {
        this.gridImages = gridImages;
    }
}
