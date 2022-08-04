/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 12:10 PM by Carina The Latest changes made by Carina on 7/27/22, 11:49 AM All contents of "FileInputReader" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.gui;

import com.google.gson.Gson;
import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.game.PlayingField;
import de.fhwwedel.pp.util.game.Player;
import de.fhwwedel.pp.util.game.Token;
import de.fhwwedel.pp.util.game.TokenType;
import de.fhwwedel.pp.util.game.json.GameData;
import de.fhwwedel.pp.util.game.json.PlayerData;
import de.fhwwedel.pp.util.special.Constants;
import javafx.scene.Scene;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;

public class FileInputReader {

    private FileInputReader() {
    }

    public static File selectFile(Scene scene) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        return chooser.showOpenDialog(scene.getWindow());
    }

    public static void readFile(File file, GUIConnector guiConnector) {

        if (file == null) return;
        //TODO: Fehlerhafte Config überprüfen!
        //read in the lines from file
        FileReader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Gson gson = new Gson();
        final var gameData = gson.fromJson(reader, GameData.class);
        if (checkInvalidConfig(gameData)) {
            guiConnector.showError("Config is not valid");
            return;
        }

        //create a new game
        var players = getPlayersFromFile(gameData);

        Game.createNewGame(players, guiConnector, true, new PlayingField(gameData.getField().length));
        players.forEach(Player::create);
        var currentPlayer = Game.getGame().getPlayers().stream().filter(player -> player.getPlayerID() == gameData.getCurrentPlayer()).findFirst().orElse(null);
        Game.getGame().setCurrentPlayer(currentPlayer);
        Game.getGame().getPlayingField().addDataFromJSON(gameData.getField());

        for (int actionTileID = 0; actionTileID < gameData.getUsedActionTiles().length; actionTileID++) {
            TokenType token = switch (actionTileID) {
                case 0 -> TokenType.REMOVER;
                case 1 -> TokenType.MOVER;
                case 2 -> TokenType.SWAPPER;
                case 3 -> TokenType.REPLACER;
                default -> throw new IllegalArgumentException("Invalid action tile ID");
            };

            for (int amount = 0; amount < gameData.getUsedActionTiles()[actionTileID]; amount++) {
                Game.getGame().getUsedActionTokens().add(new Token(token));
            }
        }
        removeUsedTokensFromPile(Game.getGame());
    }

    private static boolean checkInvalidConfig(final GameData gameData) {
        //check if all players are not active
        if (Arrays.stream(gameData.getPlayers()).noneMatch(PlayerData::isActive))
            return true;

        if (Arrays.stream(gameData.getPlayers()).anyMatch(player -> player.isActive() && player.getHand().length == 0 || !player.isActive() && player.getHand().length > 0))
            return true;

        if (Arrays.stream(gameData.getPlayers()).anyMatch(player -> Arrays.stream(player.getHand()).anyMatch(token -> token < 0 || token > 10)))
            return true;

        if (Arrays.stream(gameData.getField()).anyMatch(fieldRow -> Arrays.stream(fieldRow).anyMatch(token -> token < 0 || token > 10)))
            return true;

        return Arrays.stream(gameData.getPlayers()).anyMatch(player -> player.getHand().length > Constants.HAND_SIZE);
    }


    private static ArrayList<Player> getPlayersFromFile(GameData gameData) {
        var players = new ArrayList<Player>();
        for (int i = 0; i < gameData.getPlayers().length; i++) {
            var playerData = gameData.getPlayers()[i];
            if (!playerData.isAI()) {
                var player = new Player(i, playerData.isActive(), playerData.getName());
                Arrays.stream(playerData.getHand()).forEach(token -> player.getTokens().add(new Token(TokenType.getTokenType(token))));
                players.add(player);
            } else {
                var ai = new AI(i, playerData.isActive(), playerData.getName());
                Arrays.stream(playerData.getHand()).forEach(token -> ai.getTokens().add(new Token(TokenType.getTokenType(token))));
                players.add(ai);
            }
        }
        return players;
    }

    private static void removeUsedTokensFromPile(Game game) {
        EnumMap<TokenType, Integer> map = new EnumMap<>(TokenType.class);
        for (int row = 0; row < game.getPlayingField().getFieldMap().length; row++) {
            for (int col = 0; col < game.getPlayingField().getFieldMap()[row].length; col++) {
                var token = game.getPlayingField().getFieldMap()[row][col].getToken();
                if (token.getTokenType() == TokenType.NONE) {
                    continue;
                }
                if (map.containsKey(token.getTokenType())) {
                    map.put(token.getTokenType(), map.get(token.getTokenType()) + 1);
                } else {
                    map.put(token.getTokenType(), 1);
                }

            }
        }

        for (var used : game.getUsedActionTokens()) {
            if (map.containsKey(used.getTokenType())) {
                map.put(used.getTokenType(), map.get(used.getTokenType()) + 1);
            } else {
                map.put(used.getTokenType(), 1);
            }
        }

        for (var player : game.getPlayers()) {
            for (var token : player.getTokens()) {
                if (map.containsKey(token.getTokenType())) {
                    map.put(token.getTokenType(), map.get(token.getTokenType()) + 1);
                } else {
                    map.put(token.getTokenType(), 1);
                }
            }
        }

        for (var entry : map.entrySet()) {
            var token = entry.getKey();
            for (int i = 0; i < map.get(token); i++) {
                for (var tokenPileToken : game.getTokenDrawPile()) {
                    if (tokenPileToken.getTokenType() != token) {
                        continue;
                    }
                    game.getTokenDrawPile().remove(tokenPileToken);
                    break;
                }
            }
        }
    }
}
