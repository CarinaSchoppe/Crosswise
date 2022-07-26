/*
 * Copyright Notice for CrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:29 PM by Carina The Latest changes made by Carina on 8/9/22, 12:51 PM All contents of "FakeGUI" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

import me.carinasophie.crosswise.game.GUIConnector;
import me.carinasophie.crosswise.util.constants.AnimationTime;
import me.carinasophie.crosswise.util.constants.TeamType;
import me.carinasophie.crosswise.util.constants.TokenType;

public class FakeGUI implements GUIConnector {

    @Override
    public void resetSpecialTokenImages() {

    }

    @Override
    public void setupDragAndDropEvent() {

    }

    @Override
    public void startGamePopUp() {

    }

    @Override
    public void changeCurrentAnimationTime(AnimationTime time) {

    }

    @Override
    public void updatePlayerHandIcons(int playerID, TokenType[] tokens) {

    }

    @Override
    public void faultyAlert(Integer caseID) {

    }

    @Override
    public void showHand(boolean isAI, int playerID, boolean hideAll) {

    }

    @Override
    public void resetText() {

    }

    @Override
    public void notifyTurn(String playerName, int playerID) {

    }

    @Override
    public void handVisibleSwitch(int playerID) {

    }

    @Override
    public void gameWonNotifier(TeamType wonType, int points, boolean rowComplete) {

    }

    @Override
    public void performMoveUIUpdate(int[] players, TokenType[][] tokens, TokenType[][] gameField, Integer[] pointsMap) {

    }

    @Override
    public void addTokenImagesForPlayer4(TokenType[] tokens) {

    }

    @Override
    public void addTokenImagesForPlayer3(TokenType[] tokens) {

    }

    @Override
    public void addTokenImagesForPlayer2(TokenType[] tokens) {

    }

    @Override
    public void addTokenImagesForPlayer1(TokenType[] tokens) {

    }

    @Override
    public void changeCurrentPlayerText(String playerName) {

    }

    @Override
    public void removerAmountText() {

    }

    @Override
    public void moverAmountText() {

    }

    @Override
    public void swapperAmountText() {

    }

    @Override
    public void replacerAmountText() {

    }

    @Override
    public void generateGrid() {

    }

    @Override
    public void updateSpecialTokenIcons(TokenType type) {

    }

    @Override
    public void placerAnimationFrame(int x, int y, TokenType type) {

    }

    @Override
    public void removerAnimationFrame(int x, int y) {

    }

    @Override
    public void showError(String message) {

    }
}
