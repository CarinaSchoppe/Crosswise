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

import de.fhwwedel.pp.util.game.AnimationTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLController implements Initializable {


    private GUIConnector guiConnector;

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
    @FXML
    private Label sumPointsVerticalTeam;
    @FXML
    private Label sumPointsHorizontalTeam;
    @FXML
    private HBox hBoxWrappingVBox;
    @FXML
    private VBox vBoxWrappingGrdPn;
    @FXML
    private GridPane playingGrid;
    @FXML
    private Pane hand3BorderPane;
    @FXML
    private Pane gameGridPaneBorder;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.innerGrid.setVisible(false);
        fitHVBox();
        this.guiConnector = new FXGUI(showComputerHandButton, playerHandOne, playerHandTwo, playerHandThree,
                playerHandFour, currentPlayerText, gameGrid, moverAmountText, swapperAmountText, replacerAmountText,
                removerAmountText, horizontalPointsGrid, verticalPointsGrid, sumPointsVerticalTeam,
                sumPointsHorizontalTeam, innerGrid, hand3BorderPane);

        var createGame = new CreateGame(guiConnector);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/CreateGame.fxml"));
        fxmlLoader.setController(createGame);
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("CrossWise Create Game");
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);

        stage.setScene(new Scene((Parent) root));
        stage.show();

    }

    private void fitHVBox() {
        playingGrid.prefWidthProperty().bind(vBoxWrappingGrdPn.widthProperty());
        playingGrid.prefHeightProperty().bind(vBoxWrappingGrdPn.widthProperty());

        vBoxWrappingGrdPn.prefWidthProperty().bind(hBoxWrappingVBox.heightProperty());
    }

    @FXML
    void changeAnimationSpeedFast(ActionEvent event) {
        guiConnector.changeCurrentAnimationTime(AnimationTime.FAST);
    }

    @FXML
    void changeAnimationSpeedLow(ActionEvent event) {
        guiConnector.changeCurrentAnimationTime(AnimationTime.SLOW);
    }

    @FXML
    void changeAnimationSpeedMedium(ActionEvent event) {
        guiConnector.changeCurrentAnimationTime(AnimationTime.MIDDLE);
    }

    @FXML
    void clickEndGameButton(ActionEvent event) {
        Stage thisStage = (Stage) this.masterGrid.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    void clickLoadGameButton(ActionEvent event) {
        //get current scene from the event
        Scene scene = gameGrid.getScene();
        try {
            FileInputReader.readFile(FileInputReader.selectFile(scene), guiConnector);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void clickNewGameButton(ActionEvent event) {

        try {
            var createGame = new CreateGame(guiConnector);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/CreateGame.fxml"));
            fxmlLoader.setController(createGame);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("CrossWise Create Game");
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @FXML
    void clickSaveGameButton(ActionEvent event) {
        Scene scene = gameGrid.getScene();
        FileOutputWriter.writeJSON(scene);
    }

    @FXML
    void pointsPerTeamButton() {

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
}
