/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 12:10 PM by Carina The Latest changes made by Carina on 7/27/22, 11:49 AM All contents of "FileInputReader" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.util.special;

import com.google.gson.Gson;
import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.game.PlayingField;
import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.game.Token;
import de.fhwwedel.pp.util.game.TokenType;
import de.fhwwedel.pp.util.game.json.GameData;
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

    public static void readFile(File file) {

        //TODO: Fehlerhafte Config überprüfen!
        //read in the lines from file
        FileReader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Gson gson = new Gson();
        var gameData = gson.fromJson(reader, GameData.class);

        //create a new game
        var players = getPlayersFromFile(gameData);

        Game game = new Game(new PlayingField(gameData.field().length), players, Game.getGame().getGameWindowHandler());
        var currentPlayer = game.getPlayers().stream().filter(player -> player.getPlayerID() == gameData.currentPlayer()).findFirst().orElse(null);
        game.setCurrentPlayer(currentPlayer);
        game.getPlayingField().addDataFromJSON(gameData.field());

        for (int actionTileID = 0; actionTileID < gameData.usedActionTiles().length; actionTileID++) {
            TokenType token = switch (actionTileID) {
                case 0 -> TokenType.REMOVER;
                case 1 -> TokenType.MOVER;
                case 2 -> TokenType.SWAPPER;
                case 3 -> TokenType.REPLACER;
                default -> throw new IllegalArgumentException("Invalid action tile ID");
            };

            for (int amount = 0; amount < gameData.usedActionTiles()[actionTileID]; amount++) {
                game.getUsedActionTokens().add(new Token(token));
            }
        }
        removeUsedTokensFromPile(game);
        game.setup(true);
        Game.setGame(game);

    }

    private static ArrayList<Player> getPlayersFromFile(GameData gameData) {
        var players = new ArrayList<Player>();
        for (int i = 0; i < gameData.players().length; i++) {
            var playerData = gameData.players()[i];
            if (!playerData.isAI()) {
                var player = new Player(i, playerData.isActive(), playerData.name());
                player.create();
                Arrays.stream(playerData.hand()).forEach(token -> player.getTokens().add(new Token(TokenType.getTokenType(token))));
                players.add(player);
            } else {
                var ai = new AI(i, playerData.isActive(), playerData.name());
                Arrays.stream(playerData.hand()).forEach(token -> ai.getTokens().add(new Token(TokenType.getTokenType(token))));
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
