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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameWindow extends Application implements Initializable {


    private Stage stage;
    private static GameWindow gameWindow;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Label currentPlayerText;
    @FXML
    private MenuItem endGameButton;
    @FXML
    private RadioMenuItem fastAnimationSpeedButton;
    @FXML
    public GridPane grdPn;
    @FXML
    private HBox hBoxWrappingVBox;
    @FXML
    private MenuItem loadGameButton;
    @FXML
    private RadioMenuItem lowAnimationSpeedButton;
    @FXML
    private RadioMenuItem mediumAnimationSpeedButton;
    @FXML
    private Menu menuComputer;
    @FXML
    private Menu menuPunkte;
    @FXML
    private Label moverAmountText;
    @FXML
    private MenuItem newGameButton;
    @FXML
    private CheckMenuItem pointsPerRowColumnButton;
    @FXML
    private CheckMenuItem pointsPerTeamButton;
    @FXML
    private Label removerAmountText;
    @FXML
    private Label replacerAmountText;
    @FXML
    private MenuItem saveGameButton;
    @FXML
    private CheckMenuItem showComputerHandButton;
    @FXML
    private Label swapperAmountText;
    @FXML
    private VBox vBoxWrappingGrdPn;

    public static void start() {
        launch();
    }

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
        GameWindow.gameWindow.stage.close();
        GameWindow.start();

    }

    @FXML
    void initialize() {
        assert currentPlayerText != null : "fx:id=\"currentPlayerText\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert endGameButton != null : "fx:id=\"endGameButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert fastAnimationSpeedButton != null : "fx:id=\"fastAnimationSpeedButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert grdPn != null : "fx:id=\"grdPn\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert hBoxWrappingVBox != null : "fx:id=\"hBoxWrappingVBox\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert loadGameButton != null : "fx:id=\"loadGameButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert lowAnimationSpeedButton != null : "fx:id=\"lowAnimationSpeedButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert mediumAnimationSpeedButton != null : "fx:id=\"mediumAnimationSpeedButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert menuComputer != null : "fx:id=\"menuComputer\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert menuPunkte != null : "fx:id=\"menuPunkte\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert moverAmountText != null : "fx:id=\"moverAmountText\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert newGameButton != null : "fx:id=\"newGameButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert pointsPerRowColumnButton != null : "fx:id=\"pointsPerRowColumnButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert pointsPerTeamButton != null : "fx:id=\"pointsPerTeamButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert removerAmountText != null : "fx:id=\"removerAmountText\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert replacerAmountText != null : "fx:id=\"replacerAmountText\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert saveGameButton != null : "fx:id=\"saveGameButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert showComputerHandButton != null : "fx:id=\"showComputerHandButton\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert swapperAmountText != null : "fx:id=\"swapperAmountText\" was not injected: check your FXML file 'GameWindow.fxml'.";
        assert vBoxWrappingGrdPn != null : "fx:id=\"vBoxWrappingGrdPn\" was not injected: check your FXML file 'GameWindow.fxml'.";
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
        primaryStage.setScene(new Scene(root, 1000, 800));
        initialize();
        stage = primaryStage;
        primaryStage.show();

    }

    public ImageView[][] initImages(final GridPane grdPn) {
        int colcount = Constants.GAMEGRID_COLUMNS;
        int rowcount = Constants.GAMEGRID_ROWS;
        ImageView[][] imageViews = new ImageView[colcount][rowcount];
        int cellWidth = (int) grdPn.getWidth() / colcount;
        int cellHeight = (int) grdPn.getHeight() / rowcount;
        // bind each Imageview to a cell of the gridpane
        for (int x = 0; x < colcount; x++) {
            for (int y = 0; y < rowcount; y++) {
                //creates an empty imageview
                imageViews[x][y] = new ImageView();
                imageViews[x][y].setImage(new Image("/pictures/1 - sun.png"));

                //image has to fit a cell and mustn't preserve ratio
                imageViews[x][y].setFitWidth(cellWidth);
                imageViews[x][y].setFitHeight(cellHeight);
                imageViews[x][y].setPreserveRatio(false);
                imageViews[x][y].setSmooth(true);
                //add the imageview to the cell
                grdPn.add(imageViews[x][y], x, y);

                //the image shall resize when the cell resizes
                imageViews[x][y].fitWidthProperty().bind(grdPn.widthProperty().
                        divide(colcount).subtract(grdPn.getHgap()));
                imageViews[x][y].fitHeightProperty().bind(grdPn.heightProperty().
                        divide(rowcount).subtract(grdPn.getVgap()));
            }
        }
        return imageViews;
    }

    public void generateGrid() {
        gridImages = new ImageView[Constants.GAMEGRID_ROWS][Constants.GAMEGRID_COLUMNS];
        this.grdPn.getChildren().clear();
        for (int r = 0; r < Constants.GAMEGRID_ROWS; r++) {
            for (int c = 0; c < Constants.GAMEGRID_COLUMNS; c++) {
                ImageView imgNew = new ImageView();

                imgNew.fitHeightProperty().bind(this.grdPn.widthProperty().divide(Constants.GAMEGRID_COLUMNS).subtract(grdPn.getVgap()));
                imgNew.fitWidthProperty().bind(this.grdPn.widthProperty().divide(Constants.GAMEGRID_ROWS).subtract(grdPn.getHgap()));

                Label label = new Label();
                label.setText("test");
                String id = "gridToken" + c + r;
                imgNew.setId(id);

                System.out.println(imgNew.getId());

                Image img = new Image("/pictures/1 - sun.png");
                imgNew.setImage(img);

                this.gridImages[r][c] = imgNew;
                this.grdPn.add(label, c, r);
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
