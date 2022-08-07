package de.fhwwedel.pp.util.game.json;

import java.util.Arrays;

public final class GameData {
    private PlayerData[] players;
    private final int currentPlayer;
    private final int[][] field;
    private final int[] usedActionTiles;

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
        return players.clone();
    }

    public void setPlayers(PlayerData[] players) {
        this.players = players;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }


    public int[][] getField() {
        return field.clone();
    }


    public int[] getUsedActionTiles() {
        return usedActionTiles.clone();
    }

}
