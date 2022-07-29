/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 3:59 PM by Carina The Latest changes made by Carina on 7/27/22, 3:59 PM All contents of "FileOutputWrite" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.util.special;

import com.google.gson.Gson;
import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.game.json.GameData;
import de.fhwwedel.pp.util.game.json.PlayerData;
import javafx.scene.Scene;

public class FileOutputWriter {

    public static void writeJSON(Scene scene) {
        var file = FileInputReader.selectFile(scene);
        var json = new Gson().toJson(generateGameData(Game.getGame()));
        //write the json to a file
        try (var writer = new java.io.PrintWriter(file)) {
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static GameData generateGameData(Game game) {

        PlayerData[] players = new PlayerData[game.getPlayers().size()];
        for (int i = 0; i < game.getPlayers().size(); i++) {
            players[i] = generatePlayerData(game.getPlayers().get(i));
        }
        return new GameData(players, game.getCurrentPlayer().getPlayerID(), generateCorrespondingPlayingField(game), generateUsedActionTilesArray(game));

    }

    public static PlayerData generatePlayerData(Player player) {
        var hand = new int[player.getTokens().size()];
        for (int i = 0; i < player.getTokens().size(); i++) {
            hand[i] = player.getTokens().get(i).getTokenType().getValue();
        }
        return new PlayerData(player.getName(), true, (player instanceof AI), hand);
    }

    private static int[][] generateCorrespondingPlayingField(Game game) {
        int[][] field = new int[game.getPlayingField().getSize()][game.getPlayingField().getSize()];
        for (int i = 0; i < game.getPlayingField().getSize(); i++) {
            for (int j = 0; j < game.getPlayingField().getSize(); j++) {
                field[i][j] = game.getPlayingField().getFieldMap()[i][j].getToken().getTokenType().getValue();
            }
        }
        return field;
    }

    private static int[] generateUsedActionTilesArray(Game game) {
        int[] usedActionTiles = new int[Constants.UNIQUE_ACTION_TOKENS];
        for (int i = 0; i < Constants.UNIQUE_ACTION_TOKENS; i++) {
            usedActionTiles[i] = 0;
        }
        for (var token : game.getUsedActionTokens()) {
            switch (token.getTokenType()) {
                case Remover -> usedActionTiles[0] += 1;
                case Mover -> usedActionTiles[1] += 1;
                case Swapper -> usedActionTiles[2] += 1;
                case Replacer -> usedActionTiles[3] += 1;
            }
        }
        return usedActionTiles;
    }
}
