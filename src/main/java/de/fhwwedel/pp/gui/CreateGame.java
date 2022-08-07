package de.fhwwedel.pp.gui;

import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.util.game.GUIConnector;
import de.fhwwedel.pp.util.game.Player;
import de.fhwwedel.pp.util.game.TokenType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CreateGame implements Initializable {


    private final GUIConnector guiConnector;

    public CreateGame(GUIConnector guiConnector) {
        this.guiConnector = guiConnector;
    }

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
    void createGame(ActionEvent event) {
        var playerID = 0;
        Player playerOne;
        if (playerOneAI.isSelected()) {
            playerOne = new AI(0, playerOneActive.isSelected(), playerOneField.getText());
        } else {
            playerOne = new Player(0, playerOneActive.isSelected(), playerOneField.getText());
        }
        Player playerTwo;
        if (playerTwoAI.isSelected()) {
            playerTwo = new AI(1, playerTwoActive.isSelected(), playerTwoField.getText());
        } else {
            playerTwo = new Player(1, playerTwoActive.isSelected(), playerTwoField.getText());
        }
        Player playerThree;
        if (playerThreeAI.isSelected()) {
            playerThree = new AI(2, playerThreeActive.isSelected(), playerThreeField.getText());
        } else {
            playerThree = new Player(2, playerThreeActive.isSelected(), playerThreeField.getText());
        }
        Player playerFour;
        if (playerFourAI.isSelected()) {
            playerFour = new AI(3, playerFourActive.isSelected(), playerFourField.getText());
        } else {
            playerFour = new Player(3, playerFourActive.isSelected(), playerFourField.getText());
        }
        //close the current window
        ((Stage) createGameButton.getScene().getWindow()).close();
        Game.createNewGame(List.of(playerOne, playerTwo, playerThree, playerFour), guiConnector, false);
        playerOne.create();
        playerTwo.create();
        playerThree.create();
        playerFour.create();

        guiConnector.showGUIElements();
        TokenType[][] placeholder = {{}};
        guiConnector.generateGrid(true, placeholder);
        guiConnector.resetText();
        guiConnector.setupDragAndDropEvent();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialize();
    }
}