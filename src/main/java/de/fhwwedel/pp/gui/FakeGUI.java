/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 8/1/22, 12:36 PM by Carina The Latest changes made by Carina on 8/1/22, 12:36 PM All contents of "FakeGUI" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.gui;

import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.game.TeamType;
import de.fhwwedel.pp.util.game.Token;
import de.fhwwedel.pp.util.game.TokenType;

import java.util.List;

public class FakeGUI implements GameWindowHandler {


    @Override
    public void updatePlayerHandIcons(int playerID, List<Token> tokens) {

    }

    @Override
    public void showHand(boolean isAI, int playerID) {

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
    public void performMoveUIUpdate(List<Player> players, TokenType[][] gameField) {

    }

    @Override
    public void addTokenImagesForPlayer4(List<Token> tokens) {

    }

    @Override
    public void addTokenImagesForPlayer3(List<Token> tokens) {

    }

    @Override
    public void addTokenImagesForPlayer2(List<Token> tokens) {

    }

    @Override
    public void addTokenImagesForPlayer1(List<Token> tokens) {

    }

    @Override
    public void setCurrentPlayerText(String playerName) {

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
}
