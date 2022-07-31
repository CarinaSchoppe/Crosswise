/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/31/22, 12:41 PM by Carina The Latest changes made by Carina on 7/31/22, 12:41 PM All contents of "GameWindowHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.gui;

import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.special.Constants;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public record GameWindowHandler(GameWindow gameWindow) {

    private static GameWindowHandler handler;

    public static void setHandler(GameWindowHandler handler) {
        GameWindowHandler.handler = handler;
    }

    public static GameWindowHandler getGameWindowHandler() {
        return handler;
    }

    public void updatePlayerHandIcons(Player player) {
        Platform.runLater(() -> {
            switch (player.getPlayerID()) {
                case 0 -> addTokenImagesForPlayer1(player);
                case 1 -> addTokenImagesForPlayer2(player);
                case 2 -> addTokenImagesForPlayer3(player);
                case 3 -> addTokenImagesForPlayer4(player);
            }
        });


    }

    private void addTokenImagesForPlayer4(Player player) {
        int cellWidth = (int) gameWindow.getPlayerHandFour().getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) gameWindow.getPlayerHandFour().getHeight() / Constants.HAND_SIZE;
        gameWindow.getPlayerHandFour().getChildren().clear();
        for (int i = 0; i < player.getTokens().size(); i++) {
            var imageView = new ImageView(player.getTokens().get(i).getTokenType().getImagePath());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            gameWindow.getPlayerHandFour().add(imageView, 0, i);

        }
    }

    private void addTokenImagesForPlayer3(Player player) {
        int cellWidth = (int) gameWindow.getPlayerHandThree().getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) gameWindow.getPlayerHandThree().getHeight() / Constants.HAND_SIZE;
        gameWindow.getPlayerHandThree().getChildren().clear();
        for (int i = 0; i < player.getTokens().size(); i++) {
            var imageView = new ImageView(player.getTokens().get(i).getTokenType().getImagePath());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            gameWindow.getPlayerHandThree().add(imageView, i, 0);
        }
    }

    private void addTokenImagesForPlayer2(Player player) {
        int cellWidth = (int) gameWindow.getPlayerHandTwo().getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) gameWindow.getPlayerHandTwo().getHeight() / Constants.HAND_SIZE;
        gameWindow.getPlayerHandTwo().getChildren().clear();
        for (int i = 0; i < player.getTokens().size(); i++) {
            var imageView = new ImageView(player.getTokens().get(i).getTokenType().getImagePath());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            gameWindow.getPlayerHandTwo().add(imageView, 0, i);
        }
    }

    private void addTokenImagesForPlayer1(Player player) {
        int cellWidth = (int) gameWindow.getPlayerHandOne().getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) gameWindow.getPlayerHandOne().getHeight() / Constants.HAND_SIZE;
        gameWindow.getPlayerHandOne().getChildren().clear();
        for (int i = 0; i < player.getTokens().size(); i++) {
            System.out.println(player.getTokens().get(i).getTokenType());
            var imageView = new ImageView(player.getTokens().get(i).getTokenType().getImagePath());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            gameWindow.getPlayerHandOne().add(imageView, i, 0);
        }
    }


    public void generateGrid() {
        gameWindow.setGridImages(new ImageView[Constants.GAMEGRID_ROWS][Constants.GAMEGRID_COLUMNS]);
        int colcount = Constants.GAMEGRID_COLUMNS;
        gameWindow.getGameGrid().getChildren().clear();
        int rowcount = Constants.GAMEGRID_ROWS;
        for (int r = 0; r < Constants.GAMEGRID_ROWS; r++) {
            for (int c = 0; c < Constants.GAMEGRID_COLUMNS; c++) {
                ImageView imgNew = new ImageView();
                int cellWidth = (int) gameWindow.getGameGrid().getWidth() / colcount;
                int cellHeight = (int) gameWindow.getGameGrid().getHeight() / rowcount;

               /* System.out.println("grdPn.getHeight() = " + gameWindow.getGameGrid().getHeight());
                System.out.println("grdPn.getWidth() = " + gameWindow.getGameGrid().getWidth());
                System.out.println("cellHeight = " + cellHeight);
                System.out.println("cellWidth = " + cellWidth);*/
                imgNew.setFitWidth(cellWidth);
                imgNew.setFitHeight(cellHeight);
                imgNew.setPreserveRatio(false);
                imgNew.setSmooth(true);
                String id = "gridToken" + c + r;
                gameWindow.getFieldImages().put("" + c + "r", imgNew);
                imgNew.setId(id);
/*
                System.out.println(imgNew.getId());*/

                Image img = new Image("/pictures/1sun.png");
                imgNew.setImage(img);

                gameWindow.getGridImages()[r][c] = imgNew;
                gameWindow.getGameGrid().add(imgNew, c, r);

                //the image shall resize when the cell resizes
                imgNew.fitWidthProperty().bind(gameWindow.getGameGrid().widthProperty().divide(colcount));
                imgNew.fitHeightProperty().bind(gameWindow.getGameGrid().heightProperty().divide(rowcount));

            }
        }
    }
}
