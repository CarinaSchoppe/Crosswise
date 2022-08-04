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

import de.fhwwedel.pp.util.game.*;

import java.util.List;

public interface GUIConnector {

    void startGamePopUp();

    void changeCurrentAnimationTime(AnimationTime time);

    void updatePlayerHandIcons(int playerID, List<Token> tokens);

    void showHand(boolean isAI, int playerID);

    void notifyTurn(String playerName, int playerID);

    void handVisibleSwitch(int playerID);

    void gameWonNotifier(TeamType wonType, int points, boolean rowComplete);

    void performMoveUIUpdate(List<Player> players, TokenType[][] gameField);

    void addTokenImagesForPlayer4(List<Token> tokens);

    void addTokenImagesForPlayer3(List<Token> tokens);

    void addTokenImagesForPlayer2(List<Token> tokens);

    void addTokenImagesForPlayer1(List<Token> tokens);

    void changeCurrentPlayerText(String playerName);

    void removerAmountText();

    void moverAmountText();

    void swapperAmountText();


    void replacerAmountText();

    void generateGrid();
}
