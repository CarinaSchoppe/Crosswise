/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "FileOutputWriter" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.util.filehandle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.Scene;
import me.carinasophie.crosswise.game.AI;
import me.carinasophie.crosswise.game.Game;
import me.carinasophie.crosswise.game.Player;
import me.carinasophie.crosswise.util.Position;
import me.carinasophie.crosswise.util.constants.Constants;
import me.carinasophie.crosswise.util.constants.Token;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 * Class for writing a save file for the game Crosswise
 *
 * @author Carina Sophie Schoppe
 */
public final class FileOutputWriter {
    /**
     * Constructor
     */
    private FileOutputWriter() {
    }

    /**
     * Write the current game into a file
     *
     * @param scene Scene to start the game selection window
     */
    public static void writeJSON(Scene scene) {
        File file = FileInputReader.selectFile(scene);

        if (file == null) {
            return;
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(generateGameData(Game.getGame().getPlayers(),
                Game.getGame().getCurrentPlayer().getPlayerID(),
                Game.getGame().getPlayingField().getSize(),
                Game.getGame().getPlayingField().getFieldMap(),
                Game.getGame().getUsedSpecialTokens()));
        //write the json to a file
        try (PrintWriter writer = new java.io.PrintWriter(file)) {
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate a GameData class
     *
     * @param gamePlayers List of all players
     * @param currentPlayerID currentPlayerID
     * @param playingFieldSize size of the playing field
     * @param playingFieldMap Game-field Array
     * @param usedActionTokens List of used action tokens
     * @return GameData instance with all the saved data from the current running game
     */
    private static GameData generateGameData(List<Player> gamePlayers, int currentPlayerID,
                                             int playingFieldSize, Position[][] playingFieldMap,
                                             List<Token> usedActionTokens) {

        PlayerData[] players = new PlayerData[gamePlayers.size()];
        for (int i = 0; i < gamePlayers.size(); i++) {
            players[i] = generatePlayerData(gamePlayers.get(i).getHandTokens(),
                    gamePlayers.get(i).getName(), gamePlayers.get(i) instanceof AI,
                    gamePlayers.get(i).isActive());
        }
        return new GameData(players, currentPlayerID,
                generateCorrespondingPlayingField(playingFieldSize, playingFieldMap),
                generateUsedActionTilesArray(usedActionTokens));

    }

    /**
     * Creates a PlayerData instance for a single player
     *
     * @param tokens Tokens in hand
     * @param playerName name of the player
     * @param isAI boolean, if the player is an AI player
     * @param isActive boolean, if the player is active
     * @return PlayerData instance for a single player
     */
    private static PlayerData generatePlayerData(List<Token> tokens, String playerName,
                                                 boolean isAI, boolean isActive) {
        int[] hand = new int[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            hand[i] = tokens.get(i).tokenType().getValue();
        }
        return new PlayerData(playerName, isActive, isAI, hand);
    }

    /**
     * Creates a game field of int Objects out of the current GamePlayingField
     *
     * @param playingFieldSize size of the playing field
     * @param fieldMap Game field of positions
     * @return int array translation out of the current game
     */
    private static int[][] generateCorrespondingPlayingField(int playingFieldSize,
                                                             Position[][] fieldMap) {
        int[][] field = new int[playingFieldSize][playingFieldSize];
        for (int i = 0; i < playingFieldSize; i++) {
            for (int j = 0; j < playingFieldSize; j++) {
                //translate Token from position to TokenType int value
                field[i][j] = fieldMap[i][j].getToken().tokenType().getValue();
            }
        }
        return field;
    }

    /**
     * Generate the used action tokens from a list into an array
     *
     * @param usedActionTokens List of used actionTokens
     * @return int array, where the number for each array field is the amount of used tokens for the
     * specific token
     */
    private static int[] generateUsedActionTilesArray(List<Token> usedActionTokens) {
        int[] usedActionTiles = new int[Constants.UNIQUE_ACTION_TOKENS];
        for (int i = 0; i < Constants.UNIQUE_ACTION_TOKENS; i++) {
            usedActionTiles[i] = 0;
        }
        //add tokens to the array, (needs to be changed, if the tokens or their order is changed)
        for (Token token : usedActionTokens) {
            switch (token.tokenType()) {
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
