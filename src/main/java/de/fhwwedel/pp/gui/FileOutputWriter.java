/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 3:59 PM by Carina The Latest changes made by Carina on 7/27/22, 3:59 PM All contents of "FileOutputWrite" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.util.game.Player;
import de.fhwwedel.pp.util.game.Position;
import de.fhwwedel.pp.util.game.Token;
import de.fhwwedel.pp.util.game.json.GameData;
import de.fhwwedel.pp.util.game.json.PlayerData;
import de.fhwwedel.pp.util.special.Constants;
import javafx.scene.Scene;

import java.util.List;

public class FileOutputWriter {

    private FileOutputWriter() {
    }

    public static void writeJSON(Scene scene) {
        var file = FileInputReader.selectFile(scene);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        var json = gson.toJson(generateGameData(Game.getGame().getPlayers(), Game.getGame().getCurrentPlayer().getPlayerID(), Game.getGame().getPlayingField().getSize(), Game.getGame().getPlayingField().getFieldMap(), Game.getGame().getUsedActionTokens()));
        //write the json to a file
        try (var writer = new java.io.PrintWriter(file)) {
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static GameData generateGameData(List<Player> gamePlayers, int currentPlayerID, int playingFieldSize, Position[][] playingFieldMap, List<Token> usedActionTokens) {

        PlayerData[] players = new PlayerData[gamePlayers.size()];
        for (int i = 0; i < gamePlayers.size(); i++) {
            players[i] = generatePlayerData(gamePlayers.get(i).getTokens(), gamePlayers.get(i).getName(), gamePlayers.get(i) instanceof AI, gamePlayers.get(i).isActive());
        }
        return new GameData(players, currentPlayerID, generateCorrespondingPlayingField(playingFieldSize, playingFieldMap), generateUsedActionTilesArray(usedActionTokens));

    }

    private static PlayerData generatePlayerData(List<Token> tokens, String playerName, boolean isAI, boolean isActive) {
        var hand = new int[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            hand[i] = tokens.get(i).getTokenType().getValue();
        }
        return new PlayerData(playerName, isActive, isAI, hand);
    }

    private static int[][] generateCorrespondingPlayingField(int playingFieldSize, Position[][] fieldMap) {
        int[][] field = new int[playingFieldSize][playingFieldSize];
        for (int i = 0; i < playingFieldSize; i++) {
            for (int j = 0; j < playingFieldSize; j++) {
                field[i][j] = fieldMap[i][j].getToken().getTokenType().getValue();
            }
        }
        return field;
    }

    private static int[] generateUsedActionTilesArray(List<Token> usedActionTokens) {
        int[] usedActionTiles = new int[Constants.UNIQUE_ACTION_TOKENS];
        for (int i = 0; i < Constants.UNIQUE_ACTION_TOKENS; i++) {
            usedActionTiles[i] = 0;
        }
        for (var token : usedActionTokens) {
            switch (token.getTokenType()) {
                case REMOVER -> usedActionTiles[0] += 1;
                case MOVER -> usedActionTiles[1] += 1;
                case SWAPPER -> usedActionTiles[2] += 1;
                case REPLACER -> usedActionTiles[3] += 1;
                default -> throw new IllegalArgumentException("TokenType not supported");
            }
        }
        return usedActionTiles;
    }
}
