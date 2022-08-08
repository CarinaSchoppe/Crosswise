package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.GUIConnector;
import logic.Game;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Create-Game window for the game Crosswise.
 *
 * @author Jacob Klövekorn
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

        var playerNames = new ArrayList<String>();
        var isActive = new ArrayList<Boolean>();
        var isAI = new ArrayList<Boolean>();

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


        if (playerNames.isEmpty() || isActive.isEmpty() || isAI.isEmpty())
            return;

        //close the current window
        ((Stage) createGameButton.getScene().getWindow()).close();
        Game.createNewGame(playerNames, isAI, isActive, guiConnector, false, null);


        //Setting up GUI elements of the game and creating drag and drop functions
        guiConnector.showGUIElements();
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
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialize();
    }
}