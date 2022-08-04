package de.fhwwedel.pp.gui;

import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.util.game.*;
import de.fhwwedel.pp.util.special.Constants;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.List;

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
    private ClickEventSave clickEventSave = null;
    private AnimationTime animationTime = AnimationTime.MIDDLE;
    private ImageView[][] gridImages;
    private final HashMap<ImageView, TokenType> gridImagesTokens;
    private final HashMap<ImageView, TokenType> handImagesTokens;

    public FXGUI(CheckMenuItem showComputerHandButton, GridPane playerHandOne, GridPane playerHandTwo, GridPane playerHandThree, GridPane playerHandFour, Label currentPlayerText, GridPane gameGrid, Label moverAmountText, Label swapperAmountText, Label replacerAmountText, Label removerAmountText) {
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
        fieldImages = new HashMap<>();
        gridImagesTokens = new HashMap<>();
        handImagesTokens = new HashMap<>();
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
            var alert = new Alert(Alert.AlertType.INFORMATION, "The Player: \"" + playerName + "\" with ID: \"" + playerID + " is now your turn!");
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
    public void changeCurrentAnimationTime(AnimationTime time) {
        this.animationTime = time;
    }

    @Override
    public void performMoveUIUpdate(List<Player> players, TokenType[][] gameField) {
        for (var player : players)
            updatePlayerHandIcons(player.getPlayerID(), player.getTokens());
        Platform.runLater(() -> {

            for (int row = 0; row < Constants.GAMEGRID_SIZE; row++) {
                for (int column = 0; column < Constants.GAMEGRID_SIZE; column++) {
                    var token = gameField[row][column];
                    var image = new Image(token.getImagePath());
                    String id = "gridToken" + column + row;

                    var imageView =
                            fieldImages.get(id);
                    gridImagesTokens.put(imageView, token);
                    imageView.setImage(image);
                }
            }
        });
    }


    @Override
    public void showError(String message) {
        var alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.showAndWait();
    }

    @Override
    public void changeCurrentPlayerText(String playerName) {
        Platform.runLater(() -> currentPlayerText.setText(playerName));
    }

    @Override
    public void generateGrid() {

        gridImages = new ImageView[Constants.GAMEGRID_SIZE][Constants.GAMEGRID_SIZE];
        int colcount = Constants.GAMEGRID_SIZE;
        gameGrid.getChildren().clear();
        int rowcount = Constants.GAMEGRID_SIZE;
        for (int r = 0; r < Constants.GAMEGRID_SIZE; r++) {
            for (int c = 0; c < Constants.GAMEGRID_SIZE; c++) {
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
                gridImagesTokens.put(imgNew, TokenType.NONE);

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
            var imageView = new ImageView(tokens.get(i).getTokenType().getImagePath());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            playerHandOne.add(imageView, i, 0);
            handImagesTokens.put(imageView, tokens.get(i).getTokenType());
        }
        setDragEventsForPlayerHand(playerHandOne);
    }

    @Override
    public void addTokenImagesForPlayer2(List<Token> tokens) {
        int cellWidth = (int) playerHandTwo.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandTwo.getHeight() / Constants.HAND_SIZE;
        playerHandTwo.getChildren().clear();
        for (int i = 0; i < tokens.size(); i++) {
            var imageView = new ImageView(tokens.get(i).getTokenType().getImagePath());
            imageView.setId("token2:" + i);
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            playerHandTwo.add(imageView, 0, i);
            handImagesTokens.put(imageView, tokens.get(i).getTokenType());
        }
        setDragEventsForPlayerHand(playerHandTwo);
    }

    @Override
    public void addTokenImagesForPlayer3(List<Token> tokens) {
        int cellWidth = (int) playerHandThree.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandThree.getHeight() / Constants.HAND_SIZE;
        playerHandThree.getChildren().clear();
        for (int i = 0; i < tokens.size(); i++) {
            var imageView = new ImageView(tokens.get(i).getTokenType().getImagePath());
            imageView.setId("token3:" + i);
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            playerHandThree.add(imageView, i, 0);
            handImagesTokens.put(imageView, tokens.get(i).getTokenType());
        }
        setDragEventsForPlayerHand(playerHandThree);
    }

    @Override
    public void addTokenImagesForPlayer4(List<Token> tokens) {
        int cellWidth = (int) playerHandFour.getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) playerHandFour.getHeight() / Constants.HAND_SIZE;
        playerHandFour.getChildren().clear();
        for (int i = 0; i < tokens.size(); i++) {
            var imageView = new ImageView(tokens.get(i).getTokenType().getImagePath());
            imageView.setId("token4:" + i);
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            playerHandFour.add(imageView, 0, i);
            handImagesTokens.put(imageView, tokens.get(i).getTokenType());
        }
        setDragEventsForPlayerHand(playerHandFour);
    }


    @Override
    public void gameWonNotifier(TeamType wonType, int points, boolean rowComplete) {
        Platform.runLater(() ->
                gameWonNotification(wonType, points, rowComplete));
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

        var alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Game finished");
        alert.setHeaderText("Game finished");
        alert.showAndWait();
    }

    @Override
    public void showHand(boolean isAI, int playerID) {
        Platform.runLater(() -> {
            playerHandOne.setVisible(false);
            playerHandTwo.setVisible(false);
            playerHandThree.setVisible(false);
            playerHandFour.setVisible(false);
            if (isAI) {
                if (showComputerHandButton.isSelected()) handVisibleSwitch(playerID);
            } else {
                handVisibleSwitch(playerID);
            }

        });
    }

    @Override
    public void startGamePopUp() {
        Platform.runLater(() -> {
            var alert = new Alert(Alert.AlertType.INFORMATION, "Start game!");
            alert.setTitle("Start Game");
            alert.setHeaderText("Start Game");
            alert.showAndWait();
            Game.getGame().startGame();
        });
    }

    private void setupDragAndDropEvent() {
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
                    String in = event.getDragboard().getString();
                    String[] input = in.split("(?<=\\D)(?=\\d)");

                    //field empty
                    if (this.gridImagesTokens.get(curr) == TokenType.NONE) {
                        if ("SUN".equals(input[0]) || "CROSS".equals(input[0]) || "TRIANGLE".equals(input[0]) || "SQUARE".equals(input[0]) || "PENTAGON".equals(input[0]) || "STAR".equals(input[0])) {
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        }
                    } else {
                        if ("REMOVER".equals(input[0]) || "MOVER".equals(input[0]) || "SWAPPER".equals(input[0]) || "REPLACER".equals(input[0])) {
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        }
                    }

                    event.consume();
                });

                curr.setOnDragDropped((DragEvent event) -> {

                    Dragboard db = event.getDragboard();
                    boolean success = true;
                    String in = db.getString();
                    String[] input = in.split("(?<=\\D)(?=\\d)");

                    switch (input[0]) {
                        case "SUN", "CROSS", "TRIANGLE", "SQUARE", "PENTAGON", "STAR" -> Game.getGame().playerSymbolTokenMove(input[0], finalI, finalJ);
                        case "REMOVER" -> Game.getGame().playerRemoverTokenMove(finalI, finalJ);
                        case "MOVER", "SWAPPER", "REPLACER" -> {
                            boolean validInput = false;
                            while (!validInput) {
                                try {
                                    synchronized (this) {
                                        wait();
                                    }
                                } catch (InterruptedException ignored) {
                                    Thread.currentThread().interrupt();
                                }
                                if (currentClickIsValid(input[0])) {
                                    validInput = true;
                                }
                            }

                            if (input[0].equals("MOVER")) {
                                Game.getGame().playerMoverTokenMove(finalI, finalJ, this.clickEventSave.getPosX(), this.clickEventSave.getPosY());
                            } else if (input[0].equals("SWAPPER")) {
                                Game.getGame().playerSwapperTokenMove(finalI, finalJ, this.clickEventSave.getPosX(), this.clickEventSave.getPosY());
                            } else {
                                Game.getGame().playerReplacerTokenMove(finalI, finalJ, this.clickEventSave.getHandPosition());
                            }
                        }
                        default -> throw new RuntimeException("Invalid token type");
                    }

                    /* teile mit, ob der Drag&Drop erfolgreich war */
                    event.setDropCompleted(success);

                    event.consume();
                });

                curr.setOnDragEntered((DragEvent event) -> {

                    // Welche Information kann auf diesem Target-Objekt abgelegt werden?
                    //  hier: eine die einen String liefert und nicht von dem Node selbst stammt
                    if (event.getGestureSource() != curr && event.getDragboard().hasString()) {
                        // gebe ein visuelles Feedback, sodass der Nutzer weiß, dass die Information hier abgleget werden kann
                        //imageGrid[i][j].doSomething(...);
                    }
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
                    this.clickEventSave = new ClickEventSave(finalI, finalJ);
                    synchronized (this) {
                        notifyAll();
                    }

                });
            }
        }
    }


    private synchronized void setDragEventsForPlayerHand(GridPane hand) {
        int counter = 0;

        for (Node child : hand.getChildren()) {
            child.setOnDragDetected((MouseEvent event) -> {
                System.out.println("Click");
                /* lässt jeden Transfermode zu */
                Dragboard db = child.startDragAndDrop(TransferMode.ANY);

                /* legt einen String im Clipboard ab*/
                ClipboardContent content = new ClipboardContent();

                ImageView view = (ImageView) child;
                var tokenType = handImagesTokens.get(view);


                content.putString(tokenType.name());
                db.setContent(content);

                event.consume();
            });
            child.setOnDragDone((DragEvent event) -> {
                // wenn die Informationen wegbewegt wurden entferne sie aus dem Source-Objekt
                if (event.getTransferMode() == TransferMode.MOVE) {
                    //source.setText("");
                }
                event.consume();
            });

            final var count = counter;
            child.setOnMouseClicked((MouseEvent event) -> {
                this.clickEventSave = new ClickEventSave(count);
                synchronized (this) {
                    notifyAll();
                }
            });
            counter++;
        }
    }

    private boolean currentClickIsValid(String tokenType) {
        switch (tokenType) {
            case "MOVER": {
                //non empty field 2nd, not same third
                return this.clickEventSave.isGrid();
            }
            case "SWAPPER": {
                //empty field 2nd, not same third
                return false;
            }
            case "REPLACER": {
                //must be symbol token 2nd
                return false;
            }
            default -> throw new RuntimeException("Invalid token type");
        }
    }

    public void setAnimationTime(AnimationTime animationTime) {
        this.animationTime = animationTime;
    }
}
