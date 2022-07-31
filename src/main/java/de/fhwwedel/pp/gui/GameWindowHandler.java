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
        switch (player.getPlayerID()) {
            case 0 -> addTokenImagesForPlayer1(player);
            case 1 -> addTokenImagesForPlayer2(player);
            case 2 -> addTokenImagesForPlayer3(player);
            case 3 -> addTokenImagesForPlayer4(player);
        }


    }

    private void addTokenImagesForPlayer4(Player player) {
        int cellWidth = (int) gameWindow.getPlayerHandFour().getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) gameWindow.getPlayerHandFour().getHeight() / Constants.HAND_SIZE;
        gameWindow.getPlayerHandFour().getChildren().clear();
        for (int i = 0; i < player.getTokens().size(); i++) {
            var imageView = new ImageView(player.getTokens().get(i).getTokenType().getImagePath());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            gameWindow.getPlayerHandFour().getChildren().add(imageView);
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
            gameWindow.getPlayerHandThree().getChildren().add(imageView);
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
            gameWindow.getPlayerHandTwo().getChildren().add(imageView);
        }
    }

    private void addTokenImagesForPlayer1(Player player) {
        int cellWidth = (int) gameWindow.getPlayerHandOne().getWidth() / Constants.HAND_SIZE;
        int cellHeight = (int) gameWindow.getPlayerHandOne().getHeight() / Constants.HAND_SIZE;
        gameWindow.getPlayerHandOne().getChildren().clear();
        for (int i = 0; i < player.getTokens().size(); i++) {
            var imageView = new ImageView(player.getTokens().get(i).getTokenType().getImagePath());
            imageView.setFitHeight(cellHeight);
            imageView.setFitWidth(cellWidth);
            gameWindow.getPlayerHandOne().getChildren().add(imageView);
        }
    }
}
