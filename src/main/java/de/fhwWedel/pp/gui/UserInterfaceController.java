package de.fhwWedel.pp.gui;

import de.fhwWedel.pp.logic.game.GameLogic;
import de.fhwWedel.pp.logic.game.Player;
import de.fhwWedel.pp.logic.util.GUIConnector;
import de.fhwWedel.pp.util.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Main class for the user interface.
 *
 * @author mjo, cei
 */
public class UserInterfaceController implements Initializable {

    @FXML
    private GridPane gameFieldGrid;
    @FXML
    private GridPane handPlayer1;
    @FXML
    private GridPane handPlayer2;
    @FXML
    private GridPane handPlayer3;
    @FXML
    private GridPane handPlayer4;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private VBox usedActionTokensGrid;
    @FXML
    private MenuItem menuPunkte;
    @FXML
    private MenuItem menuComputer;
    @FXML
    private RadioMenuItem lowAnimationSpeed;
    @FXML
    private RadioMenuItem mediumAnimationSpeed;
    @FXML
    private RadioMenuItem fastAnimationSpeed;


    private GUIConnector guiConnector;

    private GameLogic gameLogic;


    @FXML
    private GridPane grdPn;

    @FXML
    private VBox vBoxWrappingGrdPn;

    @FXML
    private HBox hBoxWrappingVBox;


    /**
     * This is where you need to add code that should happen during
     * initialization and then change the java doc comment.
     *
     * @param location  probably not used
     * @param resources probably not used
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.guiConnector = new JavaFXGUI(this.gameFieldGrid,
                handPlayer1,
                handPlayer2,
                handPlayer3,
                handPlayer4,
                currentPlayerLabel,
                usedActionTokensGrid,
                menuPunkte,
                menuComputer);
        this.gameLogic = new GameLogic(this.guiConnector);

        grdPn.prefWidthProperty().bind(vBoxWrappingGrdPn.widthProperty());
        grdPn.prefHeightProperty().bind(vBoxWrappingGrdPn.widthProperty());

        vBoxWrappingGrdPn.prefWidthProperty().bind(hBoxWrappingVBox.heightProperty());

    }


    @FXML
    protected void clickNewGameButton() {
        //TODO extra Fenster mit Werten
        Player[] players = new Player[Constants.PLAYER_NUMBER];
        players[0] = new Player(0, "Tom", true, false);
        players[1] = new Player(1, "Jacob", true, false);
        players[2] = new Player(2, "Jonas", true, false);
        players[3] = new Player(3, "Simon", true, false);

        gameLogic.setupNewGame(players);

        System.out.println(Arrays.toString(players[0].getHand()));
        Image image = new Image(Token.getImagePathFromToken(players[0].getHand()[0]));

    }

    private void changeAnimationSpeed(int speedLevel) {
        switch (speedLevel) {
            case 0 -> {
                this.mediumAnimationSpeed.setSelected(false);
                this.fastAnimationSpeed.setSelected(false);
                this.guiConnector.setAnimationSpeed(Constants.LOW_SPEED);
            }
            case 1 -> {
                this.lowAnimationSpeed.setSelected(false);
                this.fastAnimationSpeed.setSelected(false);
                this.guiConnector.setAnimationSpeed(Constants.MED_SPEED);
            }
            case 2 -> {
                this.lowAnimationSpeed.setSelected(false);
                this.mediumAnimationSpeed.setSelected(false);
                this.guiConnector.setAnimationSpeed(Constants.FAST_SPEED);
            }
        }
    }

    @FXML
    private void clickSaveGameButton() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll
                (new FileChooser.ExtensionFilter("JSON files", "*.json"));

        File currDir = null;
        try {
            currDir = new File(UserInterfaceController.class.getProtectionDomain().
                    getCodeSource().getLocation().toURI());
        } catch (URISyntaxException ex) {
            //Exception handling
        }
        if (currDir != null) {
            fileChooser.setInitialDirectory(currDir.getParentFile());
        }

        fileChooser.setTitle("Laufendes Spiel speichern");

        File saveFile = fileChooser.showOpenDialog(this.gameFieldGrid.getScene().getWindow());
        if (saveFile != null) {
            gameLogic.saveGame(saveFile);
        }  //exception handling

    }

    @FXML
    private void clickLoadGameButton() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files", "*.json"));

        File currDir = null;
        try {
            currDir = new File(UserInterfaceController.class.getProtectionDomain().
                    getCodeSource().getLocation().toURI());
        } catch (URISyntaxException ex) {
            //Exception handling
        }
        if (currDir != null) {
            fileChooser.setInitialDirectory(currDir.getParentFile());
        }

        fileChooser.setTitle("Vorhandendes Spiel laden");

        File loadFile = fileChooser.showOpenDialog(this.gameFieldGrid.getScene().getWindow());
        if (loadFile != null) {
            gameLogic.loadGame(loadFile);
        }  //exception handling

    }

    @FXML
    private void clickEndGameButton() {
        Stage stage = (Stage) this.gameFieldGrid.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void changeAnimationSpeedLow() {
        changeAnimationSpeed(0);
    }

    @FXML
    private void changeAnimationSpeedMedium() {
        changeAnimationSpeed(1);
    }

    @FXML
    private void changeAnimationSpeedFast() {
        changeAnimationSpeed(2);
    }


    @FXML
    private void onGrdPnMouseClicked(MouseEvent mouseEvent) {
        System.out.printf("width/height: %5.1f/%5.1f%n", grdPn.getWidth(), grdPn.getHeight());
    }


}
