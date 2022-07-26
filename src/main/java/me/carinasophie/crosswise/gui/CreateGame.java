/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "CreateGame" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.carinasophie.crosswise.game.GUIConnector;
import me.carinasophie.crosswise.game.Game;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Create-Game window for the game Crosswise.
 *
 * @author Carina Sophie Schoppe
 */
public class CreateGame implements Initializable {
    /**
     * Gui-Connector Class
     */
    private final GUIConnector guiConnector;

    /**
     * Constructor
     *
     * @param guiConnector Gui-Connector
     */
    public CreateGame(GUIConnector guiConnector) {
        this.guiConnector = guiConnector;
    }

    //--------------------------------------------FXML Objects----------------------------------------------------------

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

    /**
     * Create game from the create Game button
     *
     * @param event Event isn't used in this method
     */
    @FXML
    void createGame(ActionEvent event) {

        List<String> playerNames = new ArrayList<>();
        List<Boolean> isActive = new ArrayList<>();
        List<Boolean> isAI = new ArrayList<>();

        playerNames.add(playerOneField.getText());
        playerNames.add(playerTwoField.getText());
        playerNames.add(playerThreeField.getText());
        playerNames.add(playerFourField.getText());

        isActive.add(playerOneActive.isSelected());
        isActive.add(playerTwoActive.isSelected());
        isActive.add(playerThreeActive.isSelected());
        isActive.add(playerFourActive.isSelected());

        isAI.add(playerOneAI.isSelected());
        isAI.add(playerTwoAI.isSelected());
        isAI.add(playerThreeAI.isSelected());
        isAI.add(playerFourAI.isSelected());

        //close the current window
        ((Stage) createGameButton.getScene().getWindow()).close();
        Game.createNewGame(playerNames, isAI, isActive, guiConnector, false, null);


        //Setting up GUI elements of the game and creating drag and drop functions
        guiConnector.generateGrid();
        guiConnector.resetText();
        guiConnector.setupDragAndDropEvent();
    }

    /**
     * Assertions to check, if all objects were loaded into the scene
     */
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

    /**
     * Initialize, not used here other than checking for assertion described in the other initialize method
     *
     * @param url not used here
     * @param resourceBundle not used here
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialize();
    }
}