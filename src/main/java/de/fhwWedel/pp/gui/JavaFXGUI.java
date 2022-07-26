package de.fhwWedel.pp.gui;

import de.fhwWedel.pp.logic.errorhandling.CrosswiseExceptionHandler;
import de.fhwWedel.pp.logic.errorhandling.ErrorType;
import de.fhwWedel.pp.logic.util.GUIConnector;
import de.fhwWedel.pp.util.Constants;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class JavaFXGUI implements GUIConnector {

    //---------------------------------GUI Elemente-------------------------------------------------
    private final GridPane gameFieldGrid;

    private final ImageView[][] gridImages = new ImageView[Constants.GAMEGRID_ROWS][Constants.GAMEGRID_COLUMNS];

    private final GridPane handPlayer1;

    private final GridPane handPlayer2;

    private final GridPane handPlayer3;

    private final GridPane handPlayer4;

    private final VBox usedActionTokensGrid;

    //---------------------------------------Variablen----------------------------------------------

    private int animationSpeed;

    //---------------------------------------Methoden-----------------------------------------------

    public JavaFXGUI(GridPane gameFieldGrid, GridPane handPlayer1, GridPane handPlayer2,
                     GridPane handPlayer3, GridPane handPlayer4, Label currentPlayerLabel,
                     VBox usedActionTokensGrid, MenuItem menuPunkte, MenuItem menuComputer) {
        this.gameFieldGrid = gameFieldGrid;
        this.handPlayer1 = handPlayer1;
        this.handPlayer2 = handPlayer2;
        this.handPlayer3 = handPlayer3;
        this.handPlayer4 = handPlayer4;
        this.usedActionTokensGrid = usedActionTokensGrid;


        this.animationSpeed = Constants.ANIMATION_DURATION;
        //test
        generateGrid();
        Token[] hand2 = new Token[]{Token.Star, Token.Sun, Token.Cross, Token.Star};
        generateHand(0, hand2);


    }

    @Override
    public void showPlayerHand(int ID, Token[] hand) throws CrosswiseExceptionHandler {
        switch (ID) {
            case 0 -> {
                updateHand(handPlayer4, hand);
                handPlayer1.setVisible(true);
            }
            case 1 -> {
                updateHand(handPlayer4, hand);
                handPlayer2.setVisible(true);
            }
            case 2 -> {
                updateHand(handPlayer4, hand);
                handPlayer3.setVisible(true);
            }
            case 3 -> {
                updateHand(handPlayer4, hand);
                handPlayer4.setVisible(true);
            }
            default -> throw new CrosswiseExceptionHandler(ErrorType.IdOutOfBounds);
        }
    }

    private void updateHand(GridPane player, Token[] hand) {
        for (int j = 0; j < Constants.HAND_SIZE; j++) {
            ImageView view = (ImageView) player.getChildren().get(j);
            view.setImage(new Image(Token.getImagePathFromToken(hand[j])));
        }
    }

    public void hidePlayerHand(int ID) {
        switch (ID) {
            case 0 -> handPlayer1.setVisible(false);
            case 1 -> handPlayer2.setVisible(false);
            case 2 -> handPlayer3.setVisible(false);
            case 3 -> handPlayer4.setVisible(false);
        }
    }

    public void generateHand(int ID, Token[] hand) {
        for (int i = 0; i < Constants.HAND_SIZE; i++) {
            ImageView imgNew = new ImageView();
            imgNew.setImage(new Image(Token.getImagePathFromToken(hand[i])));
            String id = "playerHand" + ID + "position" + i;
            imgNew.setId(id);

            imgNew.fitHeightProperty().bind(this.handPlayer1.widthProperty().divide(Constants.HAND_SIZE));
            imgNew.fitWidthProperty().bind(this.handPlayer1.widthProperty().divide(Constants.HAND_SIZE));

            switch (ID) {
                case 0 -> handPlayer1.add(imgNew, i, 0);
                case 1 -> handPlayer2.add(imgNew, 0, i);
                case 2 -> handPlayer3.add(imgNew, i, 0);
                case 3 -> handPlayer4.add(imgNew, 0, i);
            }
        }
    }

    public void updateUsedActionTiles(int index) {
        Label tileCounter = (Label) usedActionTokensGrid.getChildren().get(index);
        int currCounter = Integer.parseInt(tileCounter.getText());
        currCounter++;
        tileCounter.setText(Integer.toString(currCounter));
        //??
    }


    public void generateGrid() {
        this.gameFieldGrid.getChildren().clear();
        for (int r = 0; r < Constants.GAMEGRID_ROWS; r++) {
            for (int c = 0; c < Constants.GAMEGRID_COLUMNS; c++) {
                ImageView imgNew = new ImageView();

                imgNew.fitHeightProperty().bind(this.gameFieldGrid.widthProperty().divide(Constants.GAMEGRID_COLUMNS).subtract(gameFieldGrid.getVgap()));
                imgNew.fitWidthProperty().bind(this.gameFieldGrid.widthProperty().divide(Constants.GAMEGRID_ROWS).subtract(gameFieldGrid.getHgap()));

                String id = "gridToken" + c + r;
                imgNew.setId(id);

                System.out.println(imgNew.getId());

                Image img = new Image("\\pictures\\1 - sun.png");
                imgNew.setImage(img);

                this.gridImages[r][c] = imgNew;
                this.gameFieldGrid.add(imgNew, c, r);
            }
        }
    }

    public void setAnimationSpeed(float speed) {
        this.animationSpeed = (int) (Constants.ANIMATION_DURATION * speed);
    }


}
