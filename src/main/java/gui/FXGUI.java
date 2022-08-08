package gui;

import javafx.application.Platform;
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
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * FX-Class for Hanlding GUI changes of the current scene "GameWindow"
 *
 * @author Jacob Klövekorn
 */
public class FXGUI implements GUIConnector {

    //--------------------------------------------------FX-Objects------------------------------------------------------
    private final GridPane playerHandOne;
    private final GridPane playerHandTwo;
    private final GridPane playerHandThree;
    private final GridPane playerHandFour;
    private final HashMap<String, ImageView> fieldImages;
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
    private final GridPane innerGrid;
    private final ImageView imageSwapper;
    private final ImageView imageMover;
    private final ImageView imageReplacer;
    private final ImageView imageRemover;

    //-------------------------------------------------Attributes-------------------------------------------------------

    private ClickEventSave clickEventSave = null;
    private AnimationTime animationTime = AnimationTime.MIDDLE;
    private ImageView[][] gridImages;
    private final ImageView[][] handImages = new ImageView[Constants.PLAYER_COUNT][Constants.HAND_SIZE];
    private final Map<ImageView, TokenType> gridImagesTokens;
    private final Map<ImageView, TokenType> handImagesTokens;
    private boolean checkForMouse = false;
    private String clickToken;
    private Integer clickXOrigin;
    private Integer clickYOrigin;
    private boolean disableGUI = false;

    /**
     * Constructor for the FX gui class
     *
     * @param showComputerHandButton  toggleButton, to show the AI hands during their turn
     * @param playerHandOne           Hand of player 1
     * @param playerHandTwo           Hand of player 2
     * @param playerHandThree         Hand of player 3
     * @param playerHandFour          Hand of player 4
     * @param currentPlayerText       Current player text field
     * @param gameGrid                Playing field grid
     * @param moverAmountText         Amount of used mover Tokens textbox
     * @param swapperAmountText       Amount of used swapper Tokens textbox
     * @param replacerAmountText      Amount of used replacer Tokens textbox
     * @param removerAmountText       Amount of used remover Tokens textbox
     * @param horizontalPointsGrid    grid points for single line for the horizontal team
     * @param verticalPointsGrid      grid points for single line for the vertical team
     * @param sumPointsVerticalTeam   label for points of vertical team
     * @param sumPointsHorizontalTeam label for points of horizontal team
     * @param innerGrid               Inner grid, for toggling display of UI elemtns
     * @param imageSwapper            Image of the swapper special token counter
     * @param imageMover              Image of the mover special token counter
     * @param imageReplacer           Image of the replacer special token counter
     * @param imageRemover            Image of the remover special token counter
     */
    public FXGUI(CheckMenuItem showComputerHandButton, GridPane playerHandOne, GridPane playerHandTwo,
                 GridPane playerHandThree, GridPane playerHandFour, Label currentPlayerText, GridPane gameGrid,
                 Label moverAmountText, Label swapperAmountText, Label replacerAmountText, Label removerAmountText,
                 GridPane horizontalPointsGrid, GridPane verticalPointsGrid, Label sumPointsVerticalTeam,
                 Label sumPointsHorizontalTeam, GridPane innerGrid, ImageView imageSwapper, ImageView imageMover,
                 ImageView imageReplacer, ImageView imageRemover) {
        this.playerHandOne = playerHandOne;
        this.playerHandTwo = playerHandTwo;
        this.playerHandThree = playerHandThree;
        this.playerHandFour = playerHandFour;
        this.currentPlayerText = currentPlayerText;
        this.gameGrid = gameGrid;
        this.moverAmountText = moverAmountText;
        this.swapperAmountText = swapperAmountText;
        this.replacerAmountText = replacerAmountText;
        this.removerAmountText = removerAmountText;
        this.showComputerHandButton = showComputerHandButton;
        this.horizontalPointsGrid = horizontalPointsGrid;
        this.verticalPointsGrid = verticalPointsGrid;
        this.sumPointsVerticalTeam = sumPointsVerticalTeam;
        this.sumPointsHorizontalTeam = sumPointsHorizontalTeam;
        this.innerGrid = innerGrid;
        this.imageSwapper = imageSwapper;
        this.imageMover = imageMover;
        this.imageReplacer = imageReplacer;
        this.imageRemover = imageRemover;

        fieldImages = new HashMap<>();
        gridImagesTokens = new HashMap<>();
        handImagesTokens = new HashMap<>();
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
     * Remove the highlighted SpecialToken image below it's counter after the animation time
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
        //create, start and end scheduler which will replace the image with the normal image after the animation time
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> imageView.setImage(new Image(type.getImagePathNormal())), animationTime.getTime()
                * Constants.ANIMATION_TIME, TimeUnit.SECONDS);
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
        //let current thread show an alert with the name and Id of the current player
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Player: \"" + playerName + "\" with ID: \"" + playerID + ", it's your turn!");
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
        //sets visibility to true of the player whos hand will be shown
        switch (playerID) {
            case 0 -> playerHandOne.setVisible(true);
            case 1 -> playerHandTwo.setVisible(true);
            case 2 -> playerHandThree.setVisible(true);
            case 3 -> playerHandFour.setVisible(true);
            default -> throw new IllegalArgumentException("Player ID not found");
        }
    }

    /**
     * Chnage the current Animation-Time for the AI move highlights
     *
     * @param time New animation time
     */
    @Override
    public void changeCurrentAnimationTime(AnimationTime time) {
        this.animationTime = time;
    }

    /**
     * Update the UI of the game
     *
     * @param players   List of players
     * @param gameField new game field
     * @param pointsMap new points for each line
     */
    @Override
    public void performMoveUIUpdate(int[] players, TokenType[][] tokens, TokenType[][] gameField, Integer[] pointsMap) {
        for (var player : players) {
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
                imgNew.fitWidthProperty().bind(gameGrid.widthProperty().divide(Constants.GAMEGRID_SIZE).subtract(12 / Constants.GAMEGRID_SIZE));
                imgNew.fitHeightProperty().bind(gameGrid.heightProperty().divide(Constants.GAMEGRID_SIZE).subtract(12 / Constants.GAMEGRID_SIZE));
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
        this.horizontalPointsGrid.getChildren().clear();
        this.verticalPointsGrid.getChildren().clear();

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

            vertLabel.prefWidthProperty().bind(verticalPointsGrid.widthProperty().divide(Constants.GAMEGRID_SIZE).add(20));
            vertLabel.prefHeightProperty().bind(verticalPointsGrid.heightProperty());
            horiLabel.prefHeightProperty().bind(horizontalPointsGrid.heightProperty().divide(Constants.GAMEGRID_SIZE));
            horiLabel.prefWidthProperty().bind(horizontalPointsGrid.widthProperty());

            vertLabel.setAlignment(Pos.CENTER);
            horiLabel.setAlignment(Pos.CENTER);



            this.horizontalPointsGrid.add(horiLabel, 0, rows);
            this.verticalPointsGrid.add(vertLabel, rows, 0);
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
        for (Node child : this.horizontalPointsGrid.getChildren()) {
            Label currLabel = (Label) child;
            if (pointsMap[counterH] < -100) {
                currLabel.setText("Win");
            } else {
                currLabel.setText(pointsMap[counterH].toString());
            }
            sumHorizontal = sumHorizontal + pointsMap[counterH];
            counterH--;
        }
        if (sumHorizontal < -100) {
            this.sumPointsHorizontalTeam.setText("Win");
        } else {
            this.sumPointsHorizontalTeam.setText(Integer.toString(sumHorizontal));
        }

        //Vertical Team Points
        int sumVertical = 0;
        int counterV = Constants.GAMEGRID_SIZE;
        for (Node child : this.verticalPointsGrid.getChildren()) {
            Label currLabel = (Label) child;
            if (pointsMap[counterV] < -100) {
                currLabel.setText("Win");
            } else {
                currLabel.setText(pointsMap[counterV].toString());
            }
            sumVertical = sumVertical + pointsMap[counterV];
            counterV++;
        }
        if (sumVertical < -100) {
            this.sumPointsVerticalTeam.setText("Win");
        } else {
            this.sumPointsVerticalTeam.setText(Integer.toString(sumVertical));
        }
    }

    /**
     * Make GUI elements visible
     */
    public void showGUIElements() {
        this.innerGrid.setVisible(true);
    }

    /**
     * Disable GUI Elements
     */
    public void disableGUIElementes() {
        this.disableGUI = true;
    }

    /**
     * Enable Gui Elements
     */
    public void enableGUIElements() {
        this.disableGUI = false;
    }

    /**
     * Change Mover-Amount text and make it grey, once all tokens have been used
     */
    @Override
    public void moverAmountText() {
        Platform.runLater(() -> {
            moverAmountText.setText(Integer.parseInt(moverAmountText.getText()) + 1 + "");
            if (Objects.equals(moverAmountText.getText(), Integer.toString(Constants.AMOUNT_ACTION_TOKENS))) {
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
            if (Objects.equals(swapperAmountText.getText(), Integer.toString(Constants.AMOUNT_ACTION_TOKENS))) {
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
            if (Objects.equals(replacerAmountText.getText(), Integer.toString(Constants.AMOUNT_ACTION_TOKENS))) {
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
            if (Objects.equals(removerAmountText.getText(), Integer.toString(Constants.AMOUNT_ACTION_TOKENS))) {
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
        if (tokens == null) return;

        int cellWidth = (int) playerHandOne.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandOne.getHeight() / Constants.HAND_SIZE;
        //remove all previous images
        playerHandOne.getChildren().clear();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == null) return;
            ImageView imageView = new ImageView(tokens[i].getImagePathNormal());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);

            handImages[0][i] = imageView;
            playerHandOne.add(imageView, i, 0);
            handImagesTokens.put(imageView, tokens[i]);
            //make the tokens resizable and fit the game grid
            imageView.fitWidthProperty().bind(playerHandOne.widthProperty().divide(Constants.GAMEGRID_SIZE));
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
        if (tokens == null) return;

        int cellWidth = (int) playerHandTwo.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandTwo.getHeight() / Constants.HAND_SIZE;
        //remove all previous images
        playerHandTwo.getChildren().clear();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == null) return;
            ImageView imageView = new ImageView(tokens[i].getImagePathNormal());
            imageView.setId("token2:" + i);
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            handImages[1][i] = imageView;

            playerHandTwo.add(imageView, 0, i);
            handImagesTokens.put(imageView, tokens[i]);
            //make the tokens resizable and fit the game grid
            imageView.fitWidthProperty().bind(playerHandTwo.widthProperty());
            imageView.fitHeightProperty().bind(playerHandTwo.heightProperty().divide(Constants.GAMEGRID_SIZE));
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
        if (tokens == null) return;

        int cellWidth = (int) playerHandThree.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandThree.getHeight() / Constants.HAND_SIZE;
        //remove all previous images
        playerHandThree.getChildren().clear();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == null) return;

            ImageView imageView = new ImageView(tokens[i].getImagePathNormal());
            imageView.setId("token3:" + i);
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            handImages[2][i] = imageView;

            playerHandThree.add(imageView, i, 0);
            handImagesTokens.put(imageView, tokens[i]);
            //make the tokens resizable and fit the game grid
            imageView.fitWidthProperty().bind(playerHandThree.widthProperty().divide(Constants.GAMEGRID_SIZE));
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
        if (tokens == null) return;
        int cellWidth = (int) playerHandFour.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandFour.getHeight() / Constants.HAND_SIZE;
        //remove all previous images
        playerHandFour.getChildren().clear();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == null) return;
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
        } else if (lost != null && points == lost.getPoints()) {
            //if no team won, because if equal points
            message = "Draw! Both teams got the same amount of points (" + points + ")!";
        } else if (wonType != null) {
            assert lost != null;
            //if a specific team won, display the team and the points of both teams
            message = "Team: " + wonType.getTeamName() + " won, because they have more points (" + points + ") than the other team (" + lost.getPoints() + ")!";
        } else {
            //error message
            message = "No players in the game";
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
        }
        //Set alert title and show the alert
        assert alert != null;
        alert.setTitle("Wrong configuration");
        alert.setHeaderText("Wrong configuration!");
        alert.showAndWait();
    }

    /**
     * Shows hand of a specific player and hides all other hands, only shows hand of AI if option is selected to do so
     *
     * @param isAI     boolean, if player is AI
     * @param playerID Player, whose hand should be shown
     * @param hideAll  wont show any hands
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
                    Platform.runLater((() -> {
                        handVisibleSwitch(playerID);
                        try {
                            Thread.sleep(Constants.AI_TURN_TIME * 2);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }));
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
    public void setupDragAndDropEvent() {
        //Setup drag and drop events for player hands
        setDragEventsForPlayerHand(playerHandOne);
        setDragEventsForPlayerHand(playerHandTwo);
        setDragEventsForPlayerHand(playerHandThree);
        setDragEventsForPlayerHand(playerHandFour);

        //create drag and drop events for every position on the gamegrid
        for (int i = 0; i < gridImages.length; i++) {
            for (int j = 0; j < gridImages[i].length; j++) {
                ImageView curr = gridImages[i][j];
                //x and y coordinates of the current imageview in the game grid
                int finalI = i;
                int finalJ = j;
                //Set actions on a drag over action

                curr.setOnDragOver((DragEvent event) -> {
                    String input = event.getDragboard().getString();

                    //field empty
                    if (this.gridImagesTokens.get(curr) == TokenType.NONE) {
                        if ("SUN".equals(input) || "CROSS".equals(input) || "TRIANGLE".equals(input) || "SQUARE".equals(input) || "PENTAGON".equals(input) || "STAR".equals(input)) {
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        }
                    } else {
                        var replacerAllowed = false;
                        var amount = 0;
                        if ("REPLACER".equals(input)) {
                            //go through the handTokens of the player and check if one number is between 1 and 6
                            for (var token : Game.getGame().getCurrentPlayer().getHandTokens()) {
                                if (token.tokenType().getValue() >= 1 && token.tokenType().getValue() <= 6) {
                                    replacerAllowed = true;
                                    break;
                                }
                            }
                        } else if ("SWAPPER".equals(input)) {
                            //check if the gamefield has less than 2 tokens (when using swapper)
                            for (var row : Game.getGame().getPlayingField().convertToTokenTypeArray()) {
                                for (TokenType token : row) {
                                    if (token != TokenType.NONE) {
                                        amount += 1;
                                        if (amount >= 2)
                                            break;
                                    }
                                }
                            }
                        }

                        if ("REMOVER".equals(input) || "MOVER".equals(input) || ("SWAPPER".equals(input) && amount >= 2) || (replacerAllowed && "REPLACER".equals(input))) {
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
                    var tokenType = TokenType.getTokenTypeFromString(input);

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
                            this.checkForMouse = true;
                            this.clickToken = input;
                            this.clickXOrigin = finalI;
                            this.clickYOrigin = finalJ;
                        }
                        default -> throw new RuntimeException("Invalid token type");
                    }

                    /* teile mit, ob der Drag&Drop erfolgreich war */
                    event.setDropCompleted(success);

                    event.consume();
                });
                //set actions for a drag entered action
                curr.setOnDragEntered((DragEvent event) -> {
                    //TODO
                });
                //set actions for a drag exited action
                curr.setOnDragExited((DragEvent event) -> {
                    //TODO
                    // Welche Information kann auf diesem Target-Objekt abgelegt werden?
                    //  hier: eine die einen String liefert und nicht von dem Node selbst stammt
                    if (!event.isDropCompleted() && event.getGestureSource() != curr && event.getDragboard().hasString()) {
                        //TODO: hier @ Jacob: was ist das?
                        // setzte das visuelles Feedback (im DragEntered Handler gesetzt) zurück
                        //imageGrid[i][j].doSomething(...);
                    }
                    event.consume();
                });
                //set actions for a mouse click
                curr.setOnMouseClicked((MouseEvent event) -> {
                    //game needs to listen to a mouse click input
                    if (this.checkForMouse) {
                        this.clickEventSave = new ClickEventSave(finalI, finalJ);
                        //check if the click is valid with the configuration of the game
                        if (currentClickIsValid()) {
                            specialClickAction();
                            this.checkForMouse = false;
                        }
                    }
                });
            }
        }
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
                } else {
                    if (CrossWise.DEBUG)
                        System.out.println("gui disabled");
                }
            });
            //setup action on drag done event
            child.setOnDragDone((DragEvent event) -> {
                // wenn die Informationen wegbewegt wurden entferne sie aus dem Source-Objekt
                if (event.getTransferMode() == TransferMode.MOVE) {
                    //TODO: hier @ Jacob: was ist das?
                    //TODO:  //source.setText("");
                }
                event.consume();
            });

            final int count = counter;
            //setup action on mouse clicked event
            child.setOnMouseClicked((MouseEvent event) -> {

                if (this.checkForMouse) {
                    this.clickEventSave = new ClickEventSave(count);
                    if (currentClickIsValid()) {
                        specialClickAction();
                        this.checkForMouse = false;
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
        if (this.clickToken.equals("MOVER")) {
            Game.getGame().playerMoverTokenMove(clickXOrigin, clickYOrigin, this.clickEventSave.getPosX(), this.clickEventSave.getPosY());
        } else if (this.clickToken.equals("SWAPPER")) {
            Game.getGame().playerSwapperTokenMove(clickXOrigin, clickYOrigin, this.clickEventSave.getPosX(), this.clickEventSave.getPosY());
        } else {
            Game.getGame().playerReplacerTokenMove(clickXOrigin, clickYOrigin, this.clickEventSave.getHandPosition());
        }
    }

    /**
     * Checks if the current click was valid
     *
     * @return true, if it was valid, false if it wasn't
     */
    private boolean currentClickIsValid() {
        switch (this.clickToken) {
            //for mover action
            case "MOVER" -> {
                //must be a token on the grid, must be an empty token
                return this.clickEventSave.isGrid() &&
                        this.gridImagesTokens.get(this.gridImages[this.clickEventSave.getPosX()]
                                [this.clickEventSave.getPosY()]) == TokenType.NONE;
            }
            //for swapper action
            case "SWAPPER" -> {
                //must be a token on the grid, must be a non empty field, must not be the same field as the original one
                return this.clickEventSave.isGrid() &&
                        this.gridImagesTokens.get(this.gridImages[this.clickEventSave.getPosX()]
                                [this.clickEventSave.getPosY()]) != TokenType.NONE &&
                        !(this.clickEventSave.getPosX().equals(this.clickXOrigin) &&
                                this.clickEventSave.getPosY().equals(this.clickYOrigin));
            }
            //for replacer action
            case "REPLACER" -> {
                //must be a token on the hand, must be a SymbolToken
                return !this.clickEventSave.isGrid() &&
                        !this.handImagesTokens.get(this.handImages[Game.getGame().getCurrentPlayer().getPlayerID()]
                                [this.clickEventSave.getHandPosition()]).isSpecial();
            }
            default -> throw new RuntimeException("Invalid token type");
        }
    }
}
