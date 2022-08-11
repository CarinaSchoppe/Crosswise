/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "GUIConnector" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.game;

import me.carinasophie.crosswise.util.constants.AnimationTime;
import me.carinasophie.crosswise.util.constants.TeamType;
import me.carinasophie.crosswise.util.constants.TokenType;

/**
 * Interface for the gui Connector, implemented by the FXGUI class and the FakeGUI class
 *
 * @author Carina Sophie Schoppe
 */
public interface GUIConnector {
    void resetSpecialTokenImages();

    void setupDragAndDropEvent();

    void startGamePopUp();

    void changeCurrentAnimationTime(AnimationTime time);

    void updatePlayerHandIcons(int playerID, TokenType[] tokens);

    void faultyAlert(Integer caseID);

    void showHand(boolean isAI, int playerID, boolean hideAll);

    void resetText();

    void notifyTurn(String playerName, int playerID);

    void handVisibleSwitch(int playerID);

    void gameWonNotifier(TeamType wonType, int points, boolean rowComplete);

    void performMoveUIUpdate(int[] players, TokenType[][] tokens,
                             TokenType[][] gameField, Integer[] pointsMap);

    void addTokenImagesForPlayer4(TokenType[] tokens);

    void addTokenImagesForPlayer3(TokenType[] tokens);

    void addTokenImagesForPlayer2(TokenType[] tokens);

    void addTokenImagesForPlayer1(TokenType[] tokens);

    void changeCurrentPlayerText(String playerName);

    void removerAmountText();

    void moverAmountText();

    void swapperAmountText();

    void replacerAmountText();

    void generateGrid();

    void updateSpecialTokenIcons(TokenType type);

    void placerAnimationFrame(int x, int y, TokenType type);

    void removerAnimationFrame(int x, int y);

    void showError(String message);
}
