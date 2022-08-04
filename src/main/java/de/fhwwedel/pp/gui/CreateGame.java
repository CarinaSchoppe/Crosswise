/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/29/22, 12:03 PM by Carina The Latest changes made by Carina on 7/29/22, 12:03 PM All contents of "CreateGame" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.gui;

import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.game.PlayingField;
import de.fhwwedel.pp.util.game.Player;
import de.fhwwedel.pp.util.special.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateGame {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button createGameButton;

    @FXML
    private CheckBox playerFourAI;

    @FXML
    private CheckBox playerFourActive;

    @FXML
    private TextField playerFourField;

    @FXML
    private CheckBox playerOneAI;

    @FXML
    private CheckBox playerOneActive;

    @FXML
    private TextField playerOneField;

    @FXML
    private CheckBox playerThreeAI;

    @FXML
    private CheckBox playerThreeActive;

    @FXML
    private TextField playerThreeField;

    @FXML
    private CheckBox playerTwoAI;

    @FXML
    private CheckBox playerTwoActive;

    @FXML
    private TextField playerTwoField;

    @FXML
    void createGame(ActionEvent event) throws IOException {
        var field = new PlayingField(Constants.GAMEGRID_ROWS);
        Player playerOne;
        if (playerOneAI.isSelected())
            playerOne = new AI(0, playerOneActive.isSelected(), playerOneField.getText());
        else
            playerOne = new Player(0, playerOneActive.isSelected(), playerOneField.getText());
        playerOne.create();
        Player playerTwo;
        if (playerTwoAI.isSelected())
            playerTwo = new AI(1, playerTwoActive.isSelected(), playerTwoField.getText());
        else
            playerTwo = new Player(1, playerTwoActive.isSelected(), playerTwoField.getText());
        playerTwo.create();
        Player playerThree;
        if (playerThreeAI.isSelected())
            playerThree = new AI(2, playerThreeActive.isSelected(), playerThreeField.getText());
        else
            playerThree = new Player(2, playerThreeActive.isSelected(), playerThreeField.getText());
        playerThree.create();
        Player playerFour;
        if (playerFourAI.isSelected())
            playerFour = new AI(3, playerFourActive.isSelected(), playerFourField.getText());
        else
            playerFour = new Player(3, playerFourActive.isSelected(), playerFourField.getText());
        playerFour.create();
        var window = new GameWindow();
        Game game = new Game(field, new ArrayList<>(List.of(playerOne, playerTwo, playerThree, playerFour)), window);
        // get the current stage
        Game.getGame().cancel();
        window.start(new Stage());
        new Thread(() -> {
            game.setup(false);
            Game.setGame(game);
            game.start();
        }).start();
    }

    public void start(Stage primaryStage) throws IOException {

        var loader = new FXMLLoader(getClass().getResource("/gui/CreateGame.fxml"));
        loader.setController(this);
        var root = (Parent) loader.load();
        primaryStage.setTitle("Create Game CrossWise");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        initialize();
        primaryStage.show();
    }

    @FXML
    void initialize() {
        assert createGameButton != null : "fx:id=\"createGameButton\" was not injected: check your FXML file 'CreateGame.fxml'.";
        assert playerFourAI != null : "fx:id=\"playerFourAI\" was not injected: check your FXML file 'CreateGame.fxml'.";
        assert playerFourActive != null : "fx:id=\"playerFourActive\" was not injected: check your FXML file 'CreateGame.fxml'.";
        assert playerFourField != null : "fx:id=\"playerFourField\" was not injected: check your FXML file 'CreateGame.fxml'.";
        assert playerOneAI != null : "fx:id=\"playerOneAI\" was not injected: check your FXML file 'CreateGame.fxml'.";
        assert playerOneActive != null : "fx:id=\"playerOneActive\" was not injected: check your FXML file 'CreateGame.fxml'.";
        assert playerOneField != null : "fx:id=\"playerOneField\" was not injected: check your FXML file 'CreateGame.fxml'.";
        assert playerThreeAI != null : "fx:id=\"playerThreeAI\" was not injected: check your FXML file 'CreateGame.fxml'.";
        assert playerThreeActive != null : "fx:id=\"playerThreeActive\" was not injected: check your FXML file 'CreateGame.fxml'.";
        assert playerThreeField != null : "fx:id=\"playerThreeField\" was not injected: check your FXML file 'CreateGame.fxml'.";
        assert playerTwoAI != null : "fx:id=\"playerTwoAI\" was not injected: check your FXML file 'CreateGame.fxml'.";
        assert playerTwoActive != null : "fx:id=\"playerTwoActive\" was not injected: check your FXML file 'CreateGame.fxml'.";
        assert playerTwoField != null : "fx:id=\"playerTwoField\" was not injected: check your FXML file 'CreateGame.fxml'.";
    }
}