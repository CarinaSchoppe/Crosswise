package gui;


import gui.fileHandle.FileInputReader;
import gui.fileHandle.FileOutputWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.ConstantsEnums.AnimationTime;
import logic.Game.GUIConnector;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the GameWindow FXML, only handles inputs and
 *
 * @author Jacob Klövekorn
 */
public class FXMLController implements Initializable {

    //-------------------------------------------------FX elements------------------------------------------------------
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
    private TextFlow teamPointsDisplay;


    /**
     * GuiConnector
     */
    private GUIConnector guiConnector;

    //---------------------------------------------Class methods--------------------------------------------------------

    /**
     * Initialize method, which creates a Game
     *
     * @param url not used here
     * @param rb  not used here
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //hide current window until the creation of game
        //fit the grid into the wrapped h-box and v-box setup
        fitHVBox();
        //create new FX-GUI and give it all the needed javafx elements, it needs to interact with
        this.guiConnector = new FXGUI(showComputerHandButton, playerHandOne, playerHandTwo, playerHandThree,
                playerHandFour, currentPlayerText, gameGrid, moverAmountText, swapperAmountText, replacerAmountText,
                removerAmountText, horizontalPointsGrid, verticalPointsGrid, sumPointsVerticalTeam,
                sumPointsHorizontalTeam, imageSwapper, imageMover, imageReplacer, imageRemover);
        //setup new createGame window when the game starts
        CreateGame createGame = new CreateGame(guiConnector);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/CreateGame.fxml"));
        fxmlLoader.setController(createGame);
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setupStage(root);
        initialize();
    }

    /**
     * Setup stage parameters and show the stage
     *
     * @param root Parent
     */
    private void setupStage(Parent root) {
        //create new Stage with name
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Create Game");
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setScene(new Scene(root));
        //show the stage
        stage.show();
    }

    /**
     * Fit the grid into the wrapped h-box and v-box setup
     */
    private void fitHVBox() {
        playingGrid.prefWidthProperty().bind(vBoxWrappingGrdPn.widthProperty());
        playingGrid.prefHeightProperty().bind(vBoxWrappingGrdPn.widthProperty());

        vBoxWrappingGrdPn.prefWidthProperty().bind(hBoxWrappingVBox.heightProperty());
    }

    /**
     * Event handler when the endgame button gets pressed
     */
    @FXML
    void clickEndGameButton() {
        Stage thisStage = (Stage) this.masterGrid.getScene().getWindow();
        //close the current stage
        thisStage.close();
    }

    /**
     * Event handler, when the load game button gets pressed
     */
    @FXML
    void clickLoadGameButton() {
        //get current scene from the event
        Scene scene = gameGrid.getScene();
        try {
            //Load file with current scene and guiConnector
            FileInputReader.readFile(FileInputReader.selectFile(scene), guiConnector);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Event handler, when the new game button gets pressed
     */
    @FXML
    void clickNewGameButton() {
        try {
            //Create a new createGame window
            CreateGame createGame = new CreateGame(guiConnector);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/CreateGame.fxml"));
            fxmlLoader.setController(createGame);
            Parent root = fxmlLoader.load();
            setupStage(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Event handler, when the save game button gets pressed
     */
    @FXML
    void clickSaveGameButton() {
        Scene scene = gameGrid.getScene();
        FileOutputWriter.writeJSON(scene);
    }

    /**
     * Handler for a toggle of the show-points-per-team-button
     */
    @FXML
    void clickPointsPerTeamButton() {
        teamPointsDisplay.setVisible(pointsPerTeamButton.isSelected());
    }

    /**
     * Handler for a toggle of the show-points-per-line-button
     */
    @FXML
    void clickPointsPerRowColumnButton() {
        horizontalPointsGrid.setVisible(pointsPerRowColumnButton.isSelected());
        verticalPointsGrid.setVisible(pointsPerRowColumnButton.isSelected());
    }

    /**
     * Handler for the change speed radio option: Fast
     */
    @FXML
    void changeAnimationSpeedFast() {
        changeAnimationSpeedHandler(AnimationTime.FAST);
    }

    /**
     * Handler for the change speed radio option: Slow
     */
    @FXML
    void changeAnimationSpeedLow() {
        changeAnimationSpeedHandler(AnimationTime.SLOW);
    }

    /**
     * Handler for the change speed radio option: Middle

     */
    @FXML
    void changeAnimationSpeedMedium() {
        changeAnimationSpeedHandler(AnimationTime.MIDDLE);
    }

    /**
     * Changes the current animation speed of the AI and checks the given radioMenuItem
     *
     * @param animationTime the new animation Time given by the specific button pressed
     */
    private void changeAnimationSpeedHandler(AnimationTime animationTime) {
        switch (animationTime) {
            case FAST -> {
                this.lowAnimationSpeedButton.setSelected(false);
                this.mediumAnimationSpeedButton.setSelected(false);
                this.fastAnimationSpeedButton.setSelected(true);
                guiConnector.changeCurrentAnimationTime(AnimationTime.FAST);
            }
            case MIDDLE -> {
                this.lowAnimationSpeedButton.setSelected(false);
                this.mediumAnimationSpeedButton.setSelected(true);
                this.fastAnimationSpeedButton.setSelected(false);
                guiConnector.changeCurrentAnimationTime(AnimationTime.MIDDLE);

            }
            case SLOW -> {
                this.lowAnimationSpeedButton.setSelected(true);
                this.mediumAnimationSpeedButton.setSelected(false);
                this.fastAnimationSpeedButton.setSelected(false);
                guiConnector.changeCurrentAnimationTime(AnimationTime.SLOW);
            }
        }
    }

    /**
     * Assertions, that check, if every FX object was correctly created
     */
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
