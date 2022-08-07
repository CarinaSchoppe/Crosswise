package gui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import logic.*;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FXGUI implements GUIConnector {

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
    private ClickEventSave clickEventSave = null;
    private AnimationTime animationTime = AnimationTime.MIDDLE;
    private ImageView[][] gridImages;
    private final ImageView[][] handImages = new ImageView[Constants.PLAYER_COUNT][Constants.HAND_SIZE];
    private final HashMap<ImageView, TokenType> gridImagesTokens;
    private final HashMap<ImageView, TokenType> handImagesTokens;
    private boolean checkForMouse = false;
    private String clickToken;
    private Integer clickXOrigin;
    private Integer clickYOrigin;
    private boolean disableGUI = false;


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


    private void makeImageViewNormal(int x, int y, TokenType type) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            Image image = new Image(type.getImagePathNormal());
            String id = "gridToken:" + x + ":" + y;
            ImageView imageView = fieldImages.get(id);
            gridImagesTokens.put(imageView, type);
            imageView.setImage(image);
        }, animationTime.getTime() * Constants.ANIMATION_TIME, TimeUnit.SECONDS);
        scheduler.shutdown();
    }

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


    @Override
    public void updateSpecialTokenIcons(TokenType type) {
        var imageView = switch (type) {
            case SWAPPER -> imageSwapper;
            case MOVER -> imageMover;
            case REPLACER -> imageReplacer;
            case REMOVER -> imageRemover;
            default -> throw new NoTokenException("Unknown token type!");
        };
        imageView.setImage(new Image(type.getImagePathGolden()));
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> imageView.setImage(new Image(type.getImagePathNormal())), animationTime.getTime() * Constants.ANIMATION_TIME, TimeUnit.SECONDS);
        scheduler.shutdown();
    }

    @Override
    public void removerAnimationFrame(int y, int x) {
        gridImages[x][y].setImage(new Image(TokenType.NONE.getImagePathGolden()));
        makeImageViewNormal(x, y, TokenType.NONE);

    }

    @Override
    public void updatePlayerHandIcons(int playerID, List<Token> tokens) {
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Player: \"" + playerName + "\" with ID: \"" + playerID + ", it's your turn!");
            alert.setTitle("Next Turn");
            alert.setHeaderText("Next Players Turn");
            alert.show();
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
    public void changeCurrentAnimationTime(AnimationTime time) {
        this.animationTime = time;
    }

    @Override
    public void performMoveUIUpdate(List<Player> players, TokenType[][] gameField, Integer[] pointsMap) {
        for (Player player : players) {
            updatePlayerHandIcons(player.getPlayerID(), player.getHandTokens());
        }
        for (int row = 0; row < Constants.GAMEGRID_SIZE; row++) {
            for (int column = 0; column < Constants.GAMEGRID_SIZE; column++) {

                TokenType token = gameField[row][column];
                Image image = new Image(token.getImagePathNormal());
                String id = "gridToken:" + column + ":" + row;
                ImageView imageView =
                        fieldImages.get(id);
                gridImagesTokens.put(imageView, token);
                imageView.setImage(image);
            }
        }
        Platform.runLater(() -> updatePointsGrid(pointsMap));
    }


    @Override
    public void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.showAndWait();
        });

    }

    @Override
    public void changeCurrentPlayerText(String playerName) {
        Platform.runLater(() -> currentPlayerText.setText(playerName));
    }

    @Override
    public void resetText() {
        currentPlayerText.setText("");
        moverAmountText.setText("0");
        swapperAmountText.setText("0");
        replacerAmountText.setText("0");
        removerAmountText.setText("0");
    }

    @Override
    public void generateGrid(boolean newGrid, TokenType[][] gameField) {

        gridImages = new ImageView[Constants.GAMEGRID_SIZE][Constants.GAMEGRID_SIZE];
        gameGrid.getChildren().clear();
        //add a white border line around the grid

        for (int rows = 0; rows < Constants.GAMEGRID_SIZE; rows++) {
            for (int columns = 0; columns < Constants.GAMEGRID_SIZE; columns++) {
                ImageView imgNew = new ImageView();

                int cellWidth = (int) gameGrid.getWidth() / Constants.GAMEGRID_SIZE;
                int cellHeight = (int) gameGrid.getHeight() / Constants.GAMEGRID_SIZE;
                imgNew.setFitWidth(cellWidth);
                imgNew.setFitHeight(cellHeight);
                imgNew.setPreserveRatio(true);
                imgNew.setSmooth(true);
                imgNew.setCache(true);
                String id = "gridToken:" + columns + ":" + rows;
                fieldImages.put(id, imgNew);
                imgNew.setId(id);
                Image img;
                if (newGrid) {
                    img = new Image(TokenType.NONE.getImagePathNormal());
                } else {
                    img = new Image(gameField[rows][columns].getImagePathNormal());
                }

                imgNew.setImage(img);
                gridImagesTokens.put(imgNew, TokenType.NONE);

                gridImages[rows][columns] = imgNew;
                Pane pane = new Pane(imgNew);
                pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                gameGrid.add(imgNew, columns, rows);

                //the image shall resize when the cell resizes
                pane.prefWidthProperty().bind(gameGrid.widthProperty().divide(Constants.GAMEGRID_SIZE));
                pane.prefHeightProperty().bind(gameGrid.heightProperty().divide(Constants.GAMEGRID_SIZE));

                imgNew.fitWidthProperty().bind(gameGrid.widthProperty().divide(Constants.GAMEGRID_SIZE).subtract(12/Constants.GAMEGRID_SIZE));
                imgNew.fitHeightProperty().bind(gameGrid.heightProperty().divide(Constants.GAMEGRID_SIZE).subtract(12/Constants.GAMEGRID_SIZE));
            }
        }
        if (newGrid) {
            generatePointsGrids();
        }
    }

    private void generatePointsGrids() {
        verticalPointsGrid.setVgap(-1);
        verticalPointsGrid.setHgap(-1);
        //Horizontal Team
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

            String idVert = "verPoints:" + rows;
            vertLabel.setId(idVert);
            vertLabel.setText("0");


            horiLabel.setStyle("-fx-background-color:grey; -fx-padding:5");
            vertLabel.setStyle("-fx-background-color:grey; -fx-padding:5");


            vertLabel.setPrefSize(verticalPointsGrid.getWidth(), verticalPointsGrid.getHeight() / Constants.GAMEGRID_SIZE);
            horiLabel.setPrefSize(horizontalPointsGrid.getWidth() / Constants.GAMEGRID_SIZE, horizontalPointsGrid.getHeight());

            vertLabel.prefWidthProperty().bind(verticalPointsGrid.widthProperty().divide(Constants.GAMEGRID_SIZE));
            vertLabel.prefHeightProperty().bind(verticalPointsGrid.heightProperty());
            horiLabel.prefHeightProperty().bind(horizontalPointsGrid.heightProperty().divide(Constants.GAMEGRID_SIZE));
            horiLabel.prefWidthProperty().bind(horizontalPointsGrid.widthProperty());


            this.horizontalPointsGrid.add(horiLabel, 0, rows);
            this.verticalPointsGrid.add(vertLabel, rows, 0);

        }
        for (int i = 0; i < Constants.GAMEGRID_SIZE; i++) {
            ColumnConstraints con = new ColumnConstraints();
            con.setPercentWidth(16.5);
            verticalPointsGrid.getColumnConstraints().add(con);
        }
    }


    private void updatePointsGrid(Integer[] pointsMap) {
        //Horizontal Team Points
        int counterH = Constants.GAMEGRID_SIZE - 1;
        int sumHorizontal = 0;
        for (Node child : this.horizontalPointsGrid.getChildren()) {
            Label currLabel = (Label) child;
            if (pointsMap[counterH] < -100) {
                currLabel.setText("Sieg");
            } else {
                currLabel.setText(pointsMap[counterH].toString());
            }

            sumHorizontal = sumHorizontal + pointsMap[counterH];
            counterH--;
        }
        if (sumHorizontal > 100) {
            this.sumPointsHorizontalTeam.setText("Sieg");
        } else {
            this.sumPointsHorizontalTeam.setText(Integer.toString(sumHorizontal));
        }

        //Vertical Team Points
        int sumVertical = 0;
        int counterV = Constants.GAMEGRID_SIZE;
        for (Node child : this.verticalPointsGrid.getChildren()) {
            Label currLabel = (Label) child;
            if (pointsMap[counterV] < -100) {
                currLabel.setText("Sieg");
            } else {
                currLabel.setText(pointsMap[counterV].toString());
            }
            sumVertical = sumVertical + pointsMap[counterV];
            counterV++;
        }
        if (sumVertical < -100) {
            this.sumPointsVerticalTeam.setText("Sieg");
        } else {
            this.sumPointsVerticalTeam.setText(Integer.toString(sumVertical));
        }


    }

    public void showGUIElements() {
        this.innerGrid.setVisible(true);
    }


    //TODO: here?
    public void disableGUIElementes() {
        this.disableGUI = true;
    }

    public void enableGUIElements() {
        this.disableGUI = false;
    }

    @Override
    public void moverAmountText() {
        Platform.runLater(() -> moverAmountText.setText(Integer.parseInt(moverAmountText.getText()) + 1 + ""));
    }

    @Override
    public void swapperAmountText() {
        Platform.runLater(() -> swapperAmountText.setText(Integer.parseInt(swapperAmountText.getText()) + 1 + ""));

    }

    @Override
    public void replacerAmountText() {
        Platform.runLater(() -> replacerAmountText.setText(Integer.parseInt(replacerAmountText.getText()) + 1 + ""));
    }

    @Override
    public void removerAmountText() {
        Platform.runLater(() -> removerAmountText.setText(Integer.parseInt(removerAmountText.getText()) + 1 + ""));
    }

    @Override
    public void addTokenImagesForPlayer1(List<Token> tokens) {
        int cellWidth = (int) playerHandOne.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandOne.getHeight() / Constants.HAND_SIZE;
        playerHandOne.getChildren().clear();

        for (int i = 0; i < tokens.size(); i++) {
            ImageView imageView = new ImageView(tokens.get(i).getTokenType().getImagePathNormal());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);

            handImages[0][i] = imageView;
            playerHandOne.add(imageView, i, 0);
            handImagesTokens.put(imageView, tokens.get(i).getTokenType());

            imageView.fitWidthProperty().bind(playerHandOne.widthProperty().divide(Constants.GAMEGRID_SIZE));
            imageView.fitHeightProperty().bind(playerHandOne.heightProperty());
        }
        setDragEventsForPlayerHand(playerHandOne);
    }

    @Override
    public void addTokenImagesForPlayer2(List<Token> tokens) {
        int cellWidth = (int) playerHandTwo.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandTwo.getHeight() / Constants.HAND_SIZE;
        playerHandTwo.getChildren().clear();
        for (int i = 0; i < tokens.size(); i++) {
            ImageView imageView = new ImageView(tokens.get(i).getTokenType().getImagePathNormal());
            imageView.setId("token2:" + i);
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            handImages[1][i] = imageView;

            playerHandTwo.add(imageView, 0, i);
            handImagesTokens.put(imageView, tokens.get(i).getTokenType());

            imageView.fitWidthProperty().bind(playerHandTwo.widthProperty());
            imageView.fitHeightProperty().bind(playerHandTwo.heightProperty().divide(Constants.GAMEGRID_SIZE));
        }
        setDragEventsForPlayerHand(playerHandTwo);
    }

    @Override
    public void addTokenImagesForPlayer3(List<Token> tokens) {
        int cellWidth = (int) playerHandThree.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandThree.getHeight() / Constants.HAND_SIZE;
        playerHandThree.getChildren().clear();
        for (int i = 0; i < tokens.size(); i++) {
            ImageView imageView = new ImageView(tokens.get(i).getTokenType().getImagePathNormal());
            imageView.setId("token3:" + i);
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            handImages[2][i] = imageView;

            playerHandThree.add(imageView, i, 0);
            handImagesTokens.put(imageView, tokens.get(i).getTokenType());

            imageView.fitWidthProperty().bind(playerHandThree.widthProperty().divide(Constants.GAMEGRID_SIZE));
            imageView.fitHeightProperty().bind(playerHandThree.heightProperty());
        }
        setDragEventsForPlayerHand(playerHandThree);
    }

    @Override
    public void addTokenImagesForPlayer4(List<Token> tokens) {
        int cellWidth = (int) playerHandFour.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandFour.getHeight() / Constants.HAND_SIZE;
        playerHandFour.getChildren().clear();
        for (int i = 0; i < tokens.size(); i++) {
            ImageView imageView = new ImageView(tokens.get(i).getTokenType().getImagePathNormal());
            imageView.setId("token4:" + i);
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            handImages[3][i] = imageView;
            playerHandFour.add(imageView, 0, i);
            handImagesTokens.put(imageView, tokens.get(i).getTokenType());

            imageView.fitWidthProperty().bind(playerHandFour.widthProperty());
            imageView.fitHeightProperty().bind(playerHandFour.heightProperty().divide(Constants.GAMEGRID_SIZE));
        }
        setDragEventsForPlayerHand(playerHandFour);
    }


    @Override
    public void gameWonNotifier(TeamType wonType, int points, boolean rowComplete) {
        gameWonNotification(wonType, points, rowComplete);
    }


    public void gameWonNotification(TeamType wonType, int points, boolean rowComplete) {
        String message;
        Team lost = null;

        if (wonType != null) {
            lost = wonType == TeamType.VERTICAL ? Team.getHorizontalTeam() : Team.getVerticalTeam();

        }
        if (rowComplete && wonType != null) {
            message = "Team: " + wonType.getTeamName() + " won, because the hit a full line!";
        } else if (lost != null && points == lost.getPoints()) {
            message = "Draw! Both teams got the same amount of points (" + points + ")!";
        } else if (wonType != null) {
            assert lost != null;
            message = "Team: " + wonType.getTeamName() + " won, because they have more points (" + points + ") than the other team (" + lost.getPoints() + ")!";
        } else {
            message = "No players in the game";
        }

        playerHandOne.setVisible(false);
        playerHandTwo.setVisible(false);
        playerHandThree.setVisible(false);
        playerHandFour.setVisible(false);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
            alert.setTitle("Game finished");
            alert.setHeaderText("Game finished");
            alert.showAndWait();
        });
    }

    @Override
    public void faultyAlert(Integer caseID) {
        Alert alert = null;
        switch (caseID) {
            case 0: {
                alert = new Alert(Alert.AlertType.INFORMATION, "The game cannot be started with 0 Players!");
                break;
            }
            case 1: {
                alert = new Alert(Alert.AlertType.INFORMATION, "The teams need to be of equal size!");
                break;
            }
            case 2: {
                alert = new Alert(Alert.AlertType.INFORMATION, "The game that should be loaded is not allowed to be loaded!");
                break;
            }

        }
        alert.setTitle("Wrong configuration");
        alert.setHeaderText("Wrong configuration!");
        alert.showAndWait();
    }

    @Override
    public void showHand(boolean isAI, int playerID, boolean hideAll) {
        playerHandOne.setVisible(false);
        playerHandTwo.setVisible(false);
        playerHandThree.setVisible(false);
        playerHandFour.setVisible(false);
        if (!hideAll) {
            if (isAI) {
                if (showComputerHandButton.isSelected()) handVisibleSwitch(playerID);
            } else {
                handVisibleSwitch(playerID);
            }
        }
    }

    @Override
    public void startGamePopUp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Game is ready");
        alert.setTitle("Start the game");
        alert.setHeaderText("Start the game");
        alert.showAndWait();
        Game.getGame().startGame();
    }

    public void setupDragAndDropEvent() {
        setDragEventsForPlayerHand(playerHandOne);
        setDragEventsForPlayerHand(playerHandTwo);
        setDragEventsForPlayerHand(playerHandThree);
        setDragEventsForPlayerHand(playerHandFour);

        ImageView[][] imageGrid = gridImages;
        for (int i = 0; i < imageGrid.length; i++) {
            for (int j = 0; j < imageGrid[i].length; j++) {
                ImageView curr = imageGrid[i][j];

                int finalI = i;
                int finalJ = j;
                curr.setOnDragOver((DragEvent event) -> {
                    String input = event.getDragboard().getString();


                    //field empty
                    if (this.gridImagesTokens.get(curr) == TokenType.NONE) {
                        if ("SUN".equals(input) || "CROSS".equals(input) || "TRIANGLE".equals(input) || "SQUARE".equals(input) || "PENTAGON".equals(input) || "STAR".equals(input)) {
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        }
                    } else {
                        if ("REMOVER".equals(input) || "MOVER".equals(input) || "SWAPPER".equals(input) || "REPLACER".equals(input)) {
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        }
                    }

                    event.consume();
                });

                curr.setOnDragDropped((DragEvent event) -> {

                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    String input = db.getString();

                    switch (input) {
                        case "SUN", "CROSS", "TRIANGLE", "SQUARE", "PENTAGON", "STAR" -> {
                            Game.getGame().playerSymbolTokenMove(input, finalI, finalJ);
                        }
                        case "REMOVER" ->
                            Game.getGame().playerRemoverTokenMove(finalI, finalJ);

                        case "MOVER", "SWAPPER", "REPLACER" -> {
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

                curr.setOnDragEntered((DragEvent event) -> {
                    //
                });

                curr.setOnDragExited((DragEvent event) -> {

                    // Welche Information kann auf diesem Target-Objekt abgelegt werden?
                    //  hier: eine die einen String liefert und nicht von dem Node selbst stammt
                    if (!event.isDropCompleted() && event.getGestureSource() != curr && event.getDragboard().hasString()) {
                        // setzte das visuelles Feedback (im DragEntered Handler gesetzt) zurück
                        //imageGrid[i][j].doSomething(...);
                    }
                    event.consume();
                });

                curr.setOnMouseClicked((MouseEvent event) -> {
                    if (this.checkForMouse) {
                        this.clickEventSave = new ClickEventSave(finalI, finalJ);
                        if (currentClickIsValid()) {
                            specialClickAction();
                            this.checkForMouse = false;
                        }
                    }
                });
            }
        }
    }


    private synchronized void setDragEventsForPlayerHand(GridPane hand) {
        int counter = 0;

        for (Node child : hand.getChildren()) {
            child.setOnDragDetected((MouseEvent event) -> {
                if (!this.disableGUI) {
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
                    System.out.println("gui disabled");
                }
            });
            child.setOnDragDone((DragEvent event) -> {
                // wenn die Informationen wegbewegt wurden entferne sie aus dem Source-Objekt
                if (event.getTransferMode() == TransferMode.MOVE) {
                    //TODO:  //source.setText("");
                }
                event.consume();
            });

            final int count = counter;
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

    private void specialClickAction() {
        if (this.clickToken.equals("MOVER")) {
            Game.getGame().playerMoverTokenMove(clickXOrigin, clickYOrigin, this.clickEventSave.getPosX(), this.clickEventSave.getPosY());
        } else if (this.clickToken.equals("SWAPPER")) {
            Game.getGame().playerSwapperTokenMove(clickXOrigin, clickYOrigin, this.clickEventSave.getPosX(), this.clickEventSave.getPosY());
        } else {
            Game.getGame().playerReplacerTokenMove(clickXOrigin, clickYOrigin, this.clickEventSave.getHandPosition());
        }
    }

    private boolean currentClickIsValid() {
        switch (this.clickToken) {
            case "MOVER" -> {
                //must be a token on the grid, must be an empty token
                return this.clickEventSave.isGrid() &&
                        this.gridImagesTokens.get(this.gridImages[this.clickEventSave.getPosX()]
                                [this.clickEventSave.getPosY()]) == TokenType.NONE;
            }

            case "SWAPPER" -> {
                //must be a token on the grid, must be a non empty field, must not be the same field as the original one
                return this.clickEventSave.isGrid() &&
                        this.gridImagesTokens.get(this.gridImages[this.clickEventSave.getPosX()]
                                [this.clickEventSave.getPosY()]) != TokenType.NONE &&
                        !(this.clickEventSave.getPosX() == this.clickXOrigin &&
                                this.clickEventSave.getPosY() == this.clickYOrigin);
            }
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
