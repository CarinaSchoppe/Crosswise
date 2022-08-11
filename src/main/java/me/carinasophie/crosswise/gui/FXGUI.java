/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "FXGUI" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import lombok.Data;
import me.carinasophie.crosswise.game.GUIConnector;
import me.carinasophie.crosswise.game.Game;
import me.carinasophie.crosswise.game.Team;
import me.carinasophie.crosswise.util.constants.*;
import me.carinasophie.crosswise.util.exceptions.NoTokenException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * FX-Class for Handling GUI changes of the current scene "GameWindow"
 *
 * @author Carina Sophie Schoppe, Jacob Klövekorn
 */
@Data
public class FXGUI implements GUIConnector {

    //--------------------------------------------------FX-Objects------------------------------------------------------
    private final GridPane playerHandOne;
    private final GridPane playerHandTwo;
    private final GridPane playerHandThree;
    private final GridPane playerHandFour;
    private final HashMap<String, ImageView> fieldImages = new HashMap<>();
    private final Label currentPlayerText;
    private final GridPane gameGrid;
    private final Label moverAmountText;
    private final Label swapperAmountText;
    private final Label replacerAmountText;
    private final Label removerAmountText;
    private final CheckMenuItem showComputerHandButton;
    private final GridPane verticalPointsGrid;
    private final GridPane horizontalPointsGrid;
    private final Label sumPointsVerticalTeam;
    private final Label sumPointsHorizontalTeam;
    private final ImageView imageSwapper;
    private final ImageView imageMover;
    private final ImageView imageReplacer;
    private final ImageView imageRemover;

    //-------------------------------------------------Attributes-------------------------------------------------------

    private ClickEventSave clickEventSave = null;
    private AnimationTime animationTime = AnimationTime.MIDDLE;
    private ImageView[][] gridImages;
    private ImageView[][] handImages = new ImageView[Constants.PLAYER_COUNT]
            [Constants.HAND_SIZE];
    private Map<ImageView, TokenType> gridImagesTokens = new HashMap<>();
    private Map<ImageView, TokenType> handImagesTokens = new HashMap<>();
    private boolean checkForMouse = false;
    private String clickToken;
    private Integer clickXOrigin;
    private Integer clickYOrigin;
    private boolean disableGUI = false;

    /**
     * Makes current player text blink in its color
     *
     * @param color Color of the team
     */
    private void blinkMethod(Color color) {
        KeyFrame kfP = new KeyFrame(Duration.seconds(1),
                new KeyValue(currentPlayerText.textFillProperty(), color));
        Timeline timer = new Timeline();
        timer.getKeyFrames().add(kfP);
        timer.setCycleCount(Animation.INDEFINITE);
        timer.setAutoReverse(true);
        timer.play();
    }

    /**
     * Removes the golden frame from highlighted images after the animation time is over
     *
     * @param x    x-coordinate of game grid
     * @param y    y-coordinate of game grid
     * @param type TokenType of the token, that will be changed
     */
    private void makeImageViewNormal(int x, int y, TokenType type) {
        //create schedule-executor, who will change the picture after the given time
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            Image image = new Image(type.getImagePathNormal());
            String id = "gridToken:" + x + ":" + y;
            ImageView imageView = fieldImages.get(id);
            gridImagesTokens.put(imageView, type);
            imageView.setImage(image);
        }, animationTime.getTime() * Constants.ANIMATION_TIME, TimeUnit.SECONDS);
        //close the scheduler
        scheduler.shutdown();
    }

    /**
     * Change picture of specific grid token to a highlighted one
     *
     * @param y    y coordinate of game grid
     * @param x    x coordinate of game grid
     * @param type TokenType of the token, that will be changed
     */
    @Override
    public void placerAnimationFrame(int y, int x, TokenType type) {
        Image image = new Image(type.getImagePathGolden());
        String id = "gridToken:" + x + ":" + y;
        ImageView imageView =
                fieldImages.get(id);
        gridImagesTokens.put(imageView, type);
        imageView.setImage(image);
        makeImageViewNormal(x, y, type);
    }

    /**
     * Remove the highlighted SpecialToken image below the counter after the animation time
     */
    @Override
    public void updateSpecialTokenIcons(TokenType type) {
        ImageView imageView = switch (type) {
            case SWAPPER -> imageSwapper;
            case MOVER -> imageMover;
            case REPLACER -> imageReplacer;
            case REMOVER -> imageRemover;
            default -> throw new NoTokenException("Unknown token type!");
        };
        imageView.setImage(new Image(type.getImagePathGolden()));
        //create, start and end scheduler which will replace the image with the normal image after
        // the animation time
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> imageView.setImage(new Image(type.getImagePathNormal())),
                animationTime.getTime() * Constants.ANIMATION_TIME, TimeUnit.SECONDS);
        scheduler.shutdown();
    }

    /**
     * Highlights the new empty field with a golden border
     *
     * @param y y coordinate of game grid
     * @param x x coordinate of game grid
     */
    @Override
    public void removerAnimationFrame(int y, int x) {
        Image image = new Image(TokenType.NONE.getImagePathGolden());
        String id = "gridToken:" + x + ":" + y;
        ImageView imageView =
                fieldImages.get(id);
        gridImagesTokens.put(imageView, TokenType.NONE);
        imageView.setImage(image);
        makeImageViewNormal(x, y, TokenType.NONE);
        makeImageViewNormal(x, y, TokenType.NONE);
    }

    /**
     * Update a players hand icons
     *
     * @param playerID player to be updates
     * @param tokens   new hand tokens
     */
    @Override
    public void updatePlayerHandIcons(int playerID, TokenType[] tokens) {
        //update the images of a player in the thread
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

    /**
     * Creates alert, that a player can do their turn now
     *
     * @param playerName name of the player
     * @param playerID   ID of the player
     */
    @Override
    public void notifyTurn(String playerName, int playerID) {
        //let current thread show an alert with the name and ID of the current player
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Player: \""
                    + playerName + "\" with ID: \"" + playerID + ", it's your turn!");
            alert.setTitle("Next Turn");
            alert.setHeaderText("Next Players Turn");
            alert.show();
            enableGUIElements();
        });
    }

    /**
     * Shows hand of a single player
     *
     * @param playerID player id of the player, whose hand will be shown
     */
    @Override
    public void handVisibleSwitch(int playerID) {
        //sets visibility to true of the player whose hand will be shown
        switch (playerID) {
            case 0 -> playerHandOne.setVisible(true);
            case 1 -> playerHandTwo.setVisible(true);
            case 2 -> playerHandThree.setVisible(true);
            case 3 -> playerHandFour.setVisible(true);
            default -> throw new IllegalArgumentException("Player ID not found");
        }
        if (playerID % 2 == 0) {
            blinkMethod(Color.ORANGERED);

        } else {
            blinkMethod(Color.GREEN);
        }
    }

    /**
     * Change the current Animation-Time for the AI move highlights
     *
     * @param time New animation time
     */
    @Override
    public void changeCurrentAnimationTime(AnimationTime time) {
        animationTime = time;
    }

    /**
     * Update the UI of the game
     *
     * @param players   List of players
     * @param gameField new game field
     * @param pointsMap new points for each line
     */
    @Override
    public void performMoveUIUpdate(int[] players, TokenType[][] tokens, TokenType[][] gameField,
                                    Integer[] pointsMap) {
        for (int player : players) {
            updatePlayerHandIcons(player, tokens[player]);
        }
        //replace each frame with the corresponding image of the new game field
        for (int row = 0; row < Constants.GAMEGRID_SIZE; row++) {
            for (int column = 0; column < Constants.GAMEGRID_SIZE; column++) {

                TokenType token = gameField[row][column];
                Image image = new Image(token.getImagePathNormal());
                String id = "gridToken:" + column + ":" + row;
                //get imageview from ID
                ImageView imageView = fieldImages.get(id);
                gridImagesTokens.put(imageView, token);
                imageView.setImage(image);
            }
        }
        //update points of each line
        Platform.runLater(() -> updatePointsGrid(pointsMap));
    }

    /**
     * Creates alert for invalid config
     *
     * @param message Error message
     */
    @Override
    public void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.showAndWait();
        });
    }

    /**
     * Change the current player text
     *
     * @param playerName Current player name
     */
    @Override
    public void changeCurrentPlayerText(String playerName) {
        Platform.runLater(() -> currentPlayerText.setText(playerName));
    }

    /**
     * Resets all textFields to their default value
     */
    @Override
    public void resetText() {
        currentPlayerText.setText("");
        moverAmountText.setText("0");
        swapperAmountText.setText("0");
        replacerAmountText.setText("0");
        removerAmountText.setText("0");
        resetSpecialTokenImages();
    }

    /**
     * Generate GameGrid at the start of a game
     */
    @Override
    public void generateGrid() {
        gridImages = new ImageView[Constants.GAMEGRID_SIZE][Constants.GAMEGRID_SIZE];
        //Remove all previous images
        gameGrid.getChildren().clear();
        for (int rows = 0; rows < Constants.GAMEGRID_SIZE; rows++) {
            for (int columns = 0; columns < Constants.GAMEGRID_SIZE; columns++) {
                ImageView imgNew = new ImageView();
                //Set size of the imageview to its proportional space on the grid
                int cellWidth = (int) gameGrid.getWidth() / Constants.GAMEGRID_SIZE;
                int cellHeight = (int) gameGrid.getHeight() / Constants.GAMEGRID_SIZE;
                imgNew.setFitWidth(cellWidth);
                imgNew.setFitHeight(cellHeight);
                imgNew.setPreserveRatio(true);
                imgNew.setSmooth(true);
                imgNew.setCache(true);
                //Give each imageview a unique id
                String id = "gridToken:" + columns + ":" + rows;
                fieldImages.put(id, imgNew);
                imgNew.setId(id);
                Image img;
                //set empty token image
                img = new Image(TokenType.NONE.getImagePathNormal());
                imgNew.setImage(img);

                //insert imageview into the grid
                gridImagesTokens.put(imgNew, TokenType.NONE);
                gridImages[rows][columns] = imgNew;
                gameGrid.add(imgNew, columns, rows);

                //the image shall resize when the cell resizes
                imgNew.fitWidthProperty().bind(gameGrid.widthProperty().
                        divide(Constants.GAMEGRID_SIZE).subtract(12 / Constants.GAMEGRID_SIZE));
                imgNew.fitHeightProperty().bind(gameGrid.heightProperty().
                        divide(Constants.GAMEGRID_SIZE).subtract(12 / Constants.GAMEGRID_SIZE));
            }
        }
        //generate points grids
        generatePointsGrids();
    }

    /**
     * Generates Points Grids, one number for each row and each column, all starting with 0
     */
    private void generatePointsGrids() {
        verticalPointsGrid.setVgap(-1);
        verticalPointsGrid.setHgap(-1);
        horizontalPointsGrid.getChildren().clear();
        verticalPointsGrid.getChildren().clear();

        for (int rows = 0; rows < Constants.GAMEGRID_SIZE; rows++) {
            Label horiLabel = new Label();
            Label vertLabel = new Label();
            vertLabel.setTextAlignment(TextAlignment.JUSTIFY);
            vertLabel.setWrapText(true);
            horiLabel.setTextAlignment(TextAlignment.JUSTIFY);
            horiLabel.setWrapText(true);


            String idHor = "horPoints:" + rows;
            horiLabel.setId(idHor);
            horiLabel.setText("0");
            horiLabel.setFont(new Font("Arial", 30));

            String idVert = "verPoints:" + rows;
            vertLabel.setId(idVert);
            vertLabel.setText("0");
            vertLabel.setFont(new Font("Arial", 30));

            vertLabel.prefWidthProperty().bind(verticalPointsGrid.widthProperty().
                    divide(Constants.GAMEGRID_SIZE).add(20));
            vertLabel.prefHeightProperty().bind(verticalPointsGrid.heightProperty());
            horiLabel.prefHeightProperty().bind(horizontalPointsGrid.heightProperty().
                    divide(Constants.GAMEGRID_SIZE));
            horiLabel.prefWidthProperty().bind(horizontalPointsGrid.widthProperty());

            vertLabel.setAlignment(Pos.CENTER);
            horiLabel.setAlignment(Pos.CENTER);


            horizontalPointsGrid.add(horiLabel, 0, rows);
            verticalPointsGrid.add(vertLabel, rows, 0);
        }

        ColumnConstraints con = new ColumnConstraints();
        con.setPercentWidth(16);
        con.setHgrow(Priority.ALWAYS);
        verticalPointsGrid.getColumnConstraints().add(con);

    }

    /**
     * Update the points grids
     *
     * @param pointsMap Integer Array of new points numbers
     */
    private void updatePointsGrid(Integer[] pointsMap) {
        //Horizontal Team Points
        int counterH = Constants.GAMEGRID_SIZE - 1;
        int sumHorizontal = 0;
        for (Node child : horizontalPointsGrid.getChildren()) {
            Label currLabel = (Label) child;
            if (pointsMap[counterH] < Constants.OUT_OF_REACH) {
                currLabel.setText("Win");
            } else {
                currLabel.setText(pointsMap[counterH].toString());
            }
            sumHorizontal = sumHorizontal + pointsMap[counterH];
            counterH--;
        }
        if (sumHorizontal < Constants.OUT_OF_REACH) {
            sumPointsHorizontalTeam.setText("Win");
        } else {
            sumPointsHorizontalTeam.setText(Integer.toString(sumHorizontal));
        }

        //Vertical Team Points
        int sumVertical = 0;
        int counterV = Constants.GAMEGRID_SIZE;
        for (Node child : verticalPointsGrid.getChildren()) {
            Label currLabel = (Label) child;
            if (pointsMap[counterV] < Constants.OUT_OF_REACH) {
                currLabel.setText("Win");
            } else {
                currLabel.setText(pointsMap[counterV].toString());
            }
            sumVertical = sumVertical + pointsMap[counterV];
            counterV++;
        }
        if (sumVertical < Constants.OUT_OF_REACH) {
            sumPointsVerticalTeam.setText("Win");
        } else {
            sumPointsVerticalTeam.setText(Integer.toString(sumVertical));
        }
    }

    /**
     * Disable GUI Elements
     */
    public void disableGUIElementes() {
        disableGUI = true;
    }

    /**
     * Enable Gui Elements
     */
    public void enableGUIElements() {
        disableGUI = false;
    }

    /**
     * Change Mover-Amount text and make it grey, once all tokens have been used
     */
    @Override
    public void moverAmountText() {
        Platform.runLater(() -> {
            moverAmountText.setText(Integer.parseInt(moverAmountText.getText()) + 1 + "");
            if (Objects.equals(moverAmountText.getText(),
                    Integer.toString(Constants.AMOUNT_ACTION_TOKENS))) {
                imageMover.setOpacity(0.5);
            }
        });
    }

    /**
     * Change Swapper-Amount text and make it grey, once all tokens have been used
     */
    @Override
    public void swapperAmountText() {
        Platform.runLater(() -> {
            swapperAmountText.setText(Integer.parseInt(swapperAmountText.getText()) + 1 + "");
            if (Objects.equals(swapperAmountText.getText(),
                    Integer.toString(Constants.AMOUNT_ACTION_TOKENS))) {
                imageSwapper.setOpacity(0.5);
            }
        });
    }

    /**
     * Change Replacer-Amount text and make it grey, once all tokens have been used
     */
    @Override
    public void replacerAmountText() {
        Platform.runLater(() -> {
            replacerAmountText.setText(Integer.parseInt(replacerAmountText.getText()) + 1 + "");
            if (Objects.equals(replacerAmountText.getText(),
                    Integer.toString(Constants.AMOUNT_ACTION_TOKENS))) {
                imageReplacer.setOpacity(0.5);
            }
        });
    }

    /**
     * Change Remover-Amount text and make it grey, once all tokens have been used
     */
    @Override
    public void removerAmountText() {
        Platform.runLater(() -> {
            removerAmountText.setText(Integer.parseInt(removerAmountText.getText()) + 1 + "");
            if (Objects.equals(removerAmountText.getText(),
                    Integer.toString(Constants.AMOUNT_ACTION_TOKENS))) {
                imageRemover.setOpacity(0.5);
            }
        });
    }

    /**
     * Remove the grey out effect from all special token images
     */
    @Override
    public void resetSpecialTokenImages() {
        imageRemover.setOpacity(1);
        imageReplacer.setOpacity(1);
        imageSwapper.setOpacity(1);
        imageMover.setOpacity(1);
    }

    /**
     * Add Token Images for player1
     *
     * @param tokens List of hand tokens
     */
    @Override
    public void addTokenImagesForPlayer1(TokenType[] tokens) {
        if (tokens == null) {
            return;
        }

        int cellWidth = (int) playerHandOne.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandOne.getHeight() / Constants.HAND_SIZE;
        //remove all previous images
        playerHandOne.getChildren().clear();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == null) {
                break;
            }
            ImageView imageView = new ImageView(tokens[i].getImagePathNormal());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);

            handImages[0][i] = imageView;
            playerHandOne.add(imageView, i, 0);
            handImagesTokens.put(imageView, tokens[i]);
            //make the tokens resizable and fit the game grid
            imageView.fitWidthProperty().bind(playerHandOne.widthProperty().
                    divide(Constants.GAMEGRID_SIZE));
            imageView.fitHeightProperty().bind(playerHandOne.heightProperty());
        }
        setDragEventsForPlayerHand(playerHandOne);
    }

    /**
     * Add Token Images for player2
     *
     * @param tokens List of hand tokens
     */
    @Override
    public void addTokenImagesForPlayer2(TokenType[] tokens) {
        if (tokens == null) {
            return;
        }

        int cellWidth = (int) playerHandTwo.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandTwo.getHeight() / Constants.HAND_SIZE;
        //remove all previous images
        playerHandTwo.getChildren().clear();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == null) {
                break;
            }
            ImageView imageView = new ImageView(tokens[i].getImagePathNormal());
            imageView.setId("token2:" + i);
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            handImages[1][i] = imageView;

            playerHandTwo.add(imageView, 0, i);
            handImagesTokens.put(imageView, tokens[i]);
            //make the tokens resizable and fit the game grid
            imageView.fitWidthProperty().bind(playerHandTwo.widthProperty());
            imageView.fitHeightProperty().bind(playerHandTwo.heightProperty().
                    divide(Constants.GAMEGRID_SIZE));
        }
        setDragEventsForPlayerHand(playerHandTwo);
    }

    /**
     * Add Token Images for player3
     *
     * @param tokens List of hand tokens
     */
    @Override
    public void addTokenImagesForPlayer3(TokenType[] tokens) {
        if (tokens == null) {
            return;
        }

        int cellWidth = (int) playerHandThree.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandThree.getHeight() / Constants.HAND_SIZE;
        //remove all previous images
        playerHandThree.getChildren().clear();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == null) {
                break;
            }
            ImageView imageView = new ImageView(tokens[i].getImagePathNormal());
            imageView.setId("token3:" + i);
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            handImages[2][i] = imageView;
            playerHandThree.add(imageView, i, 0);
            handImagesTokens.put(imageView, tokens[i]);
            //make the tokens resizable and fit the game grid
            imageView.fitWidthProperty().bind(playerHandThree.widthProperty().
                    divide(Constants.GAMEGRID_SIZE));
            imageView.fitHeightProperty().bind(playerHandThree.heightProperty());
        }
        setDragEventsForPlayerHand(playerHandThree);
    }

    /**
     * Add Token Images for player4
     *
     * @param tokens List of hand tokens
     */
    @Override
    public void addTokenImagesForPlayer4(TokenType[] tokens) {
        if (tokens == null) {
            return;
        }
        int cellWidth = (int) playerHandFour.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandFour.getHeight() / Constants.HAND_SIZE;
        //remove all previous images
        playerHandFour.getChildren().clear();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == null) {
                break;
            }
            ImageView imageView = new ImageView(tokens[i].getImagePathNormal());
            imageView.setId("token4:" + i);
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            handImages[3][i] = imageView;
            playerHandFour.add(imageView, 0, i);
            handImagesTokens.put(imageView, tokens[i]);
            //make the tokens resizable and fit the game grid
            imageView.fitWidthProperty().bind(playerHandFour.widthProperty());
            imageView.fitHeightProperty().bind(playerHandFour.heightProperty().divide(Constants.GAMEGRID_SIZE));
        }

        setDragEventsForPlayerHand(playerHandFour);
    }


    /**
     * Create game won notification
     *
     * @param wonType     which team won
     * @param points      points of the winning team
     * @param rowComplete boolean, if the game was won with a full row/column
     */
    public void gameWonNotifier(TeamType wonType, int points, boolean rowComplete) {
        String message;
        Team lost = null;
        //check, which team won
        if (wonType != null) {
            lost = wonType == TeamType.VERTICAL ? Team.getHorizontalTeam() : Team.getVerticalTeam();
        }
        //if a team won because of a full line
        if (rowComplete && wonType != null) {
            message = "Team: " + wonType.getTeamName() + " won, because the hit a full line!";
        } else if (wonType != null) {
            assert lost != null;
            //if a specific team won, display the team and the points of both teams
            message = "Team: " + wonType.getTeamName() + " won, because they have more points "
                    + "(" + points + ") than the other team (" + lost.getPoints() + ")!";
        } else {
            //if draw
            message = "Draw! Both teams got the same amount of points (" + points + ")!";
        }
        //make all hands invisible
        playerHandOne.setVisible(false);
        playerHandTwo.setVisible(false);
        playerHandThree.setVisible(false);
        playerHandFour.setVisible(false);
        //let thread show alert message
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
            alert.setTitle("Game finished");
            alert.setHeaderText("Game finished");
            alert.showAndWait();
        });

    }

    /**
     * Create Alert for faulty setup
     *
     * @param caseID type of error
     */
    @Override
    public void faultyAlert(Integer caseID) {
        Alert alert = null;
        switch (caseID) {
            //Game was started with 0 players
            case 0 -> alert = new Alert(Alert.AlertType.INFORMATION, "The game cannot be started with 0 Players!");

            //Game was started with an unequal amount of players
            case 1 -> alert = new Alert(Alert.AlertType.INFORMATION, "The teams need to be of equal size!");

            //Game was started with another faulty config
            case 2 -> alert = new Alert(Alert.AlertType.INFORMATION, "The game that should be loaded is not allowed to be loaded!");
            default -> throw new IllegalArgumentException();
        }
        //Set alert title and show the alert
        alert.setTitle("Wrong configuration");
        alert.setHeaderText("Wrong configuration!");
        alert.showAndWait();
    }

    /**
     * Shows hand of a specific player and hides all other hands, only shows hand of AI if option is selected to do so
     *
     * @param isAI     boolean, if player is AI
     * @param playerID Player, whose hand should be shown
     * @param hideAll  won't show any hands
     */
    @Override
    public void showHand(boolean isAI, int playerID, boolean hideAll) {
        playerHandOne.setVisible(false);
        playerHandTwo.setVisible(false);
        playerHandThree.setVisible(false);
        playerHandFour.setVisible(false);
        if (!hideAll) {
            if (isAI) {
                if (showComputerHandButton.isSelected()) {
                    handVisibleSwitch(playerID);
                }
            } else {
                handVisibleSwitch(playerID);
            }
        }
    }

    /**
     * Creates popup for a starting game
     */
    @Override
    public void startGamePopUp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Game is ready");
        alert.setTitle("Start the game");
        alert.setHeaderText("Start the game");
        alert.showAndWait();
        Game.getGame().startGame();
    }

    /**
     * Setup Drag and Drop events for image views on the grid
     */
    @SuppressWarnings("Duplicates")
    public void setupDragAndDropEvent() {
        //Setup drag and drop events for player hands
        setDragEventsForPlayerHand(playerHandOne);
        setDragEventsForPlayerHand(playerHandTwo);
        setDragEventsForPlayerHand(playerHandThree);
        setDragEventsForPlayerHand(playerHandFour);

        //create drag and drop events for every position on the game grid
        for (int i = 0; i < gridImages.length; i++) {
            for (int j = 0; j < gridImages[i].length; j++) {
                ImageView curr = gridImages[i][j];
                //x and y coordinates of the current imageview in the game grid
                int finalI = i;
                int finalJ = j;
                //Set actions on a drag over action

                curr.setOnDragOver((DragEvent event) -> {
                    String input = event.getDragboard().getString();
                    TokenType tokenType = TokenType.getTokenTypeFromString(input);
                    //field empty
                    if (gridImagesTokens.get(curr) == TokenType.NONE) {
                        if (tokenType == TokenType.SUN || tokenType == TokenType.CROSS || tokenType == TokenType.TRIANGLE || tokenType == TokenType.SQUARE || tokenType == TokenType.PENTAGON || tokenType == TokenType.STAR) {
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        }
                    } else {
                        Map<Boolean, Integer> map = checkSpecial(tokenType);
                        boolean replacerAllowed = false;
                        int amount = 0;
                        for (Map.Entry<Boolean, Integer> entry : map.entrySet()) {
                            replacerAllowed = entry.getKey();
                            amount = entry.getValue();
                        }
                        if (tokenType == TokenType.REMOVER || tokenType == TokenType.MOVER || tokenType == TokenType.SWAPPER && amount >= 2 || replacerAllowed) {
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        }
                    }
                    event.consume();
                });
                //set actions for a dropped event
                curr.setOnDragDropped((DragEvent event) -> {

                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    String input = db.getString();
                    TokenType tokenType = TokenType.getTokenTypeFromString(input);

                    switch (tokenType) {
                        case SUN, CROSS, TRIANGLE, SQUARE, PENTAGON, STAR -> {
                            Game.getGame().playerSymbolTokenMove(tokenType, finalI, finalJ);
                            disableGUIElementes();
                        }
                        case REMOVER -> {
                            Game.getGame().playerRemoverTokenMove(finalI, finalJ);
                            disableGUIElementes();
                        }
                        case MOVER, SWAPPER, REPLACER -> {
                            checkForMouse = true;
                            clickToken = input;
                            clickXOrigin = finalI;
                            clickYOrigin = finalJ;
                        }
                        default -> throw new RuntimeException("Invalid token type");
                    }

                    /* teile mit, ob der Drag&Drop erfolgreich war */
                    event.setDropCompleted(success);

                    event.consume();
                });
                //set actions for a drag entered action
                curr.setOnDragEntered((DragEvent event) -> {
                    ImageView imgView = gridImages[finalI][finalJ];

                    String input = event.getDragboard().getString();
                    TokenType tokenType = TokenType.getTokenTypeFromString(input);
                    //field empty
                    if (gridImagesTokens.get(curr) == TokenType.NONE) {
                        if (tokenType == TokenType.SUN || tokenType == TokenType.CROSS || tokenType == TokenType.TRIANGLE || tokenType == TokenType.SQUARE || tokenType == TokenType.PENTAGON || tokenType == TokenType.STAR) {
                            Image image = new Image(gridImagesTokens.get(imgView).getImagePathHighlight());
                            imgView.setImage(image);
                        }
                    } else {
                        Map<Boolean, Integer> map = checkSpecial(tokenType);
                        boolean replacerAllowed = false;
                        int amount = 0;
                        for (Map.Entry<Boolean, Integer> entry : map.entrySet()) {
                            replacerAllowed = entry.getKey();
                            amount = entry.getValue();
                        }
                        if (tokenType == TokenType.REMOVER || tokenType == TokenType.MOVER || tokenType == TokenType.SWAPPER && amount >= 2 || replacerAllowed) {
                            Image image = new Image(gridImagesTokens.get(imgView).getImagePathHighlight());
                            imgView.setImage(image);
                        }
                    }
                });
                //set actions for a drag exited action
                curr.setOnDragExited((DragEvent event) -> {
                    ImageView imgView = gridImages[finalI][finalJ];
                    //set image back to normal
                    Image image = new Image(gridImagesTokens.get(imgView).getImagePathNormal());
                    imgView.setImage(image);
                    event.consume();
                });
                //set actions for a mouse click
                curr.setOnMouseClicked((MouseEvent event) -> {
                    //game needs to listen to a mouse click input
                    if (checkForMouse) {
                        clickEventSave = new ClickEventSave(finalI, finalJ);
                        //check if the click is valid with the configuration of the game
                        if (currentClickIsValid()) {
                            specialClickAction();
                            checkForMouse = false;
                        }
                    }
                });
            }
        }
    }

    /**
     * Small method for preventing code duplication
     *
     * @param tokenType TokenType
     * @return Map with a boolean connected to an int
     */
    private Map<Boolean, Integer> checkSpecial(TokenType tokenType) {
        Map<Boolean, Integer> map = new HashMap<>();
        boolean replacerAllowed = false;
        int amount = 0;
        if (tokenType == TokenType.REPLACER) {
            //go through the handTokens of the player and check if one number is between 1 and 6
            for (Token token : Game.getGame().getCurrentPlayer().getHandTokens()) {
                if (token.tokenType().getValue() >= 1 && token.tokenType().getValue() <= 6) {
                    replacerAllowed = true;
                    break;
                }
            }
        } else if (tokenType == TokenType.SWAPPER) {
            //check if the game field has less than 2 tokens (when using swapper)
            for (TokenType[] row : Game.getGame().getPlayingField().convertToTokenTypeArray()) {
                for (TokenType token : row) {
                    if (token != TokenType.NONE) {
                        amount += 1;
                        if (amount >= 2) {
                            break;
                        }
                    }
                }
            }
        }
        map.put(replacerAllowed, amount);
        return map;
    }

    /**
     * Setup drag and drop events for the hand of the player
     *
     * @param hand Grid pane of the hand of a specific player
     */
    private void setDragEventsForPlayerHand(GridPane hand) {
        int counter = 0;
        //setup actions for every had token
        for (Node child : hand.getChildren()) {
            //setup drag detected event for an imageview
            child.setOnDragDetected((MouseEvent event) -> {
                if (!disableGUI) {
                    /* lässt jeden Transfermode zu */
                    Dragboard db = child.startDragAndDrop(TransferMode.ANY);

                    /* legt einen String im Clipboard ab*/
                    ClipboardContent content = new ClipboardContent();

                    ImageView view = (ImageView) child;
                    TokenType tokenType = handImagesTokens.get(view);

                    content.putString(tokenType.name());
                    db.setContent(content);

                    event.consume();
                }

            });
            //setup action on drag done event
            child.setOnDragDone(Event::consume);

            final int count = counter;
            //setup action on mouse clicked event
            child.setOnMouseClicked((MouseEvent event) -> {

                if (checkForMouse) {
                    clickEventSave = new ClickEventSave(count);
                    if (currentClickIsValid()) {
                        specialClickAction();
                        checkForMouse = false;
                    }
                }
            });
            counter++;
        }
    }

    /**
     * handle click action of a used special token
     */
    private void specialClickAction() {
        disableGUIElementes();
        if (clickToken.equals("MOVER")) {
            Game.getGame().playerMoverTokenMove(clickXOrigin, clickYOrigin,
                    clickEventSave.getPosX(), clickEventSave.getPosY());
        } else if (clickToken.equals("SWAPPER")) {
            Game.getGame().playerSwapperTokenMove(clickXOrigin, clickYOrigin,
                    clickEventSave.getPosX(), clickEventSave.getPosY());
        } else {
            Game.getGame().playerReplacerTokenMove(clickXOrigin, clickYOrigin,
                    clickEventSave.getHandPosition());
        }
    }

    /**
     * Checks if the current click was valid
     *
     * @return true, if it was valid, false if it wasn't
     */
    private boolean currentClickIsValid() {
        switch (clickToken) {
            //for mover action
            case "MOVER" -> {
                //must be a token on the grid, must be an empty token
                return clickEventSave.isGrid()
                        && gridImagesTokens.get(gridImages[clickEventSave.getPosX()]
                                [clickEventSave.getPosY()]) == TokenType.NONE;
            }
            //for swapper action
            case "SWAPPER" -> {
                //must be a token on the grid, must be a non-empty field, must not be the same
                // field as the original one
                return clickEventSave.isGrid()
                        && gridImagesTokens.get(gridImages[clickEventSave.getPosX()]
                                [clickEventSave.getPosY()]) != TokenType.NONE
                        && !(clickEventSave.getPosX().equals(clickXOrigin)
                        && clickEventSave.getPosY().equals(clickYOrigin));
            }
            //for replacer action
            case "REPLACER" -> {
                //must be a token on the hand, must be a SymbolToken
                return !clickEventSave.isGrid()
                        && !handImagesTokens.get(handImages[Game.getGame().getCurrentPlayer().
                                getPlayerID()][clickEventSave.getHandPosition()]).isSpecial();
            }
            default -> throw new RuntimeException("Invalid token type");
        }
    }
}
