/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 12:59 PM by Carina The Latest changes made by Carina on 7/27/22, 12:59 PM All contents of "GameData" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwWedel.pp.util.game.json;

public class GameData {

    private final PlayerData[] players;
    private final int currentPlayer;
    private final int[][] field;
    private final int[] usedActionTiles;

    public GameData(PlayerData[] players, int currentPlayer, int[][] field, int[] usedActionTiles) {
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.field = field;
        this.usedActionTiles = usedActionTiles;
    }

    public PlayerData[] getPlayers() {
        return players;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int[][] getField() {
        return field;
    }

    public int[] getUsedActionTiles() {
        return usedActionTiles;
    }
}
