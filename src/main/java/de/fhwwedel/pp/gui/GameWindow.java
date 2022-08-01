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

import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.game.PlayingField;
import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.game.*;
import de.fhwwedel.pp.util.special.Constants;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GameWindow extends Application implements Initializable, GameWindowHandler {

    /**
     * Time for an animation of an AI turn
     */
    private AnimationTime animationTime = AnimationTime.MIDDLE;

    public static GameWindow getGameWindow() {

        return gameWindow;
    }

    @Override
    public void updatePlayerHandIcons(int playerID, List<Token> tokens) {
        if (gameWindow == null) return;
        Platform.runLater(() -> {
            switch (playerID) {
                case 0 -> addTokenImagesForPlayer1(tokens);
                case 1 -> addTokenImagesForPlayer2(tokens);
                case 2 -> addTokenImagesForPlayer3(tokens);
                case 3 -> addTokenImagesForPlayer4(tokens);
                default -> throw new IllegalArgumentException("PlayerID must be between 0 and 3");
            }
        });
    }

    @Override
    public void notifyTurn(String playerName, int playerID) {
        Platform.runLater(() -> {
            var alert = new Alert(Alert.AlertType.INFORMATION,
                    "The Player: \"" + playerName + "\" with ID: \"" + playerID
                            + " is now your turn!");
            alert.setTitle("Next Turn");
            alert.setHeaderText("Next Players Turn");
            alert.showAndWait();
        });
    }

    @Override
    public void handVisibleSwitch(int playerID) {
        switch (playerID) {
            case 0 -> playerHandOne.setVisible(true);
            case 1 -> playerHandTwo.setVisible(true);
            case 2 -> playerHandThree.setVisible(true);
            case 3 -> playerHandFour.setVisible(true);
            default -> throw new IllegalArgumentException("Player ID not found");
        }
    }

    @Override
    public void performMoveUIUpdate(List<Player> players, TokenType[][] gameField) {

        for (var player : players)
            updatePlayerHandIcons(player.getPlayerID(), player.getTokens());
        Platform.runLater(() -> {
            //TODO: here update gameField based on grid
            /*
             *
             * hol dir die steine der map
             * geh alle map felder durch
             * erstelle ein neues grid und füge in das grid die für das feld passenden steinbilder ein
             * */
            for (int row = 0; row < Constants.GAMEGRID_ROWS; row++) {
                for (int column = 0; column < Constants.GAMEGRID_COLUMNS; column++) {
                    var token = gameField[row][column];
                    var image = new Image(token.getImagePath());
                    String id = "gridToken" + column + row;

                    var imageView = fieldImages.get(id);
                    imageView.setImage(image);
                }
            }
        });
    }

    @Override
    public void setCurrentPlayerText(String playerName) {
        Platform.runLater(() -> currentPlayerText.setText(playerName));

    }

    @Override
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

                imgNew.setFitWidth(cellWidth);
                imgNew.setFitHeight(cellHeight);
                imgNew.setPreserveRatio(false);
                imgNew.setSmooth(true);
                String id = "gridToken" + c + r;
                fieldImages.put(id, imgNew);
                imgNew.setId(id);

                Image img = new Image("/pictures/0none.png");
                imgNew.setImage(img);

                gridImages[r][c] = imgNew;
                gameGrid.add(imgNew, c, r);

                //the image shall resize when the cell resizes
                imgNew.fitWidthProperty().bind(gameGrid.widthProperty().divide(colcount));
                imgNew.fitHeightProperty().bind(gameGrid.heightProperty().divide(rowcount));

            }
        }
    }

    @Override
    public void moverAmountText() {
        Platform.runLater(() -> moverAmountText.setText(Integer.parseInt(GameWindow.getGameWindow().getMoverAmountText().getText()) + 1 + "")
        );
    }

    @Override
    public void swapperAmountText() {
        Platform.runLater(() -> swapperAmountText.setText(Integer.parseInt(GameWindow.getGameWindow().getSwapperAmountText().getText()) + 1 + ""));

    }

    @Override
    public void replacerAmountText() {
        Platform.runLater(() -> replacerAmountText.setText(Integer.parseInt(GameWindow.getGameWindow().getReplacerAmountText().getText()) + 1 + ""));
    }

    @Override
    public void removerAmountText() {
        Platform.runLater(() -> removerAmountText.setText(Integer.parseInt(GameWindow.getGameWindow().getRemoverAmountText().getText()) + 1 + "")
        );
    }

    @Override
    public void addTokenImagesForPlayer1(List<Token> tokens) {
        int cellWidth = (int) playerHandOne.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandOne.getHeight() / Constants.HAND_SIZE;
        playerHandOne.getChildren().clear();
        for (int i = 0; i < tokens.size(); i++) {
            var imageView = new ImageView(tokens.get(i).getTokenType().getImagePath());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            playerHandOne.add(imageView, i, 0);
        }
    }

    @Override
    public void addTokenImagesForPlayer2(List<Token> tokens) {
        int cellWidth = (int) playerHandTwo.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandTwo.getHeight() / Constants.HAND_SIZE;
        playerHandTwo.getChildren().clear();
        for (int i = 0; i < tokens.size(); i++) {
            var imageView = new ImageView(tokens.get(i).getTokenType().getImagePath());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            playerHandTwo.add(imageView, 0, i);
        }
    }

    @Override
    public void addTokenImagesForPlayer3(List<Token> tokens) {
        int cellWidth = (int) playerHandThree.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandThree.getHeight() / Constants.HAND_SIZE;
        playerHandThree.getChildren().clear();
        for (int i = 0; i < tokens.size(); i++) {
            var imageView = new ImageView(tokens.get(i).getTokenType().getImagePath());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            playerHandThree.add(imageView, i, 0);
        }
    }

    @Override
    public void addTokenImagesForPlayer4(List<Token> tokens) {
        int cellWidth = (int) playerHandFour.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandFour.getHeight() / Constants.HAND_SIZE;
        playerHandFour.getChildren().clear();
        for (int i = 0; i < tokens.size(); i++) {
            var imageView = new ImageView(tokens.get(i).getTokenType().getImagePath());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            playerHandFour.add(imageView, 0, i);

        }
    }

    private static GameWindow gameWindow;

    @Override
    public void gameWonNotifier(TeamType wonType, int points, boolean rowComplete) {
        Platform.runLater(() -> gameWonNotification(wonType, points, rowComplete));
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

    @Override
    public void showHand(boolean isAI, int playerID) {
        Platform.runLater(() -> {
            GameWindow.getGameWindow().getPlayerHandOne().setVisible(false);
            GameWindow.getGameWindow().getPlayerHandTwo().setVisible(false);
            GameWindow.getGameWindow().getPlayerHandThree().setVisible(false);
            GameWindow.getGameWindow().getPlayerHandFour().setVisible(false);
            if (isAI) {
                if (GameWindow.getGameWindow().getShowComputerHandButton().isSelected())
                    handVisibleSwitch(playerID);
            } else {
                handVisibleSwitch(playerID);
            }

        });


    }

    private Stage stage;

    public void start() {
        launch();
    }

    @FXML
    void changeAnimationSpeedFast(ActionEvent event) {
        animationTime = AnimationTime.FAST;
    }

    @FXML
    void changeAnimationSpeedLow(ActionEvent event) {
        animationTime = AnimationTime.SLOW;

    }

    @FXML
    void changeAnimationSpeedMedium(ActionEvent event) {
        animationTime = AnimationTime.MIDDLE;
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


    public void gameWonNotification(TeamType wonType, int points, boolean rowComplete) {
        String message;
        Team lost = null;

        if (wonType != null) {
            lost = wonType == TeamType.VERTICAL ?
                    Team.getHorizontalTeam() :
                    Team.getVerticalTeam();

        }
        if (rowComplete && wonType != null) {
            message = "Team: " + wonType.getTeamName()
                    + " won, because the hit a full line!";
        } else if (lost != null && points == lost.getPoints()) {
            message = "Draw! Both teams got the same amount of points (" + points + ")!";
        } else if (wonType != null) {
            message = "Team: " + wonType.getTeamName()
                    + " won, because they have more points (" + points
                    + ") than the other team (" + lost.getPoints() + ")!";
        } else {
            message = "No players in the game";
        }

        var alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Game finished");
        alert.setHeaderText("Game finished");
        alert.showAndWait();
        var window = new GameWindow();
        var player1 = new AI(0, true, "Player 1");
        player1.create();
        var player2 = new AI(1, true, "Player 2");
        player2.create();
        var player3 = new AI(2, false, "Player 3");
        player3.create();
        var player4 = new AI(3, false, "Player 4");
        player4.create();
        var game = new Game(new PlayingField(Constants.GAMEGRID_ROWS), new ArrayList<>(List.of(player1, player2, player3, player4)), window);
        Game.setGame(game);
        Game.getGame().setup(false);
        gameWindow.stage.close();
        new Thread(() -> Game.getGame().start()).start();
        Platform.runLater(window::start);
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

    public Map<String, ImageView> getFieldImages() {
        return fieldImages;
    }

    private final HashMap<String, ImageView> fieldImages = new HashMap<>();


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
