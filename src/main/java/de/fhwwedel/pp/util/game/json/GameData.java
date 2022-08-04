/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 12:59 PM by Carina The Latest changes made by Carina on 7/27/22, 12:59 PM All contents of "GameData" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.util.game.json;

import java.util.Arrays;

public final class GameData {
    private PlayerData[] players;
    private int currentPlayer;
    private int[][] field;
    private int[] usedActionTiles;

    public GameData(PlayerData[] players, int currentPlayer, int[][] field, int[] usedActionTiles) {
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.field = field;
        this.usedActionTiles = usedActionTiles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameData gameData)) return false;

        if (currentPlayer != gameData.currentPlayer) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(players, gameData.players)) return false;
        if (!Arrays.deepEquals(field, gameData.field)) return false;
        return Arrays.equals(usedActionTiles, gameData.usedActionTiles);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(players);
        result = 31 * result + currentPlayer;
        result = 31 * result + Arrays.deepHashCode(field);
        result = 31 * result + Arrays.hashCode(usedActionTiles);
        return result;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "players=" + Arrays.toString(players) +
                ", currentPlayer=" + currentPlayer +
                ", field=" + Arrays.toString(field) +
                ", usedActionTiles=" + Arrays.toString(usedActionTiles) +
                '}';
    }

    public PlayerData[] getPlayers() {
        return players;
    }

    public void setPlayers(PlayerData[] players) {
        this.players = players;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int[][] getField() {
        return field;
    }

    public void setField(int[][] field) {
        this.field = field;
    }

    public int[] getUsedActionTiles() {
        return usedActionTiles;
    }

    public void setUsedActionTiles(int[] usedActionTiles) {
        this.usedActionTiles = usedActionTiles;
    }
}
