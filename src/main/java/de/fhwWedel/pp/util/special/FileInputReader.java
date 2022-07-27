/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 12:10 PM by Carina The Latest changes made by Carina on 7/27/22, 11:49 AM All contents of "FileInputReader" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwWedel.pp.util.special;

import com.google.gson.Gson;
import de.fhwWedel.pp.ai.AI;
import de.fhwWedel.pp.game.Game;
import de.fhwWedel.pp.game.PlayingField;
import de.fhwWedel.pp.player.Player;
import de.fhwWedel.pp.util.game.Token;
import de.fhwWedel.pp.util.game.TokenType;
import de.fhwWedel.pp.util.game.json.GameData;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FileInputReader {

    private static File selectFile(@NotNull Scene scene) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        return chooser.showOpenDialog(scene.getWindow());
    }

    public static void readFile(@NotNull File file) throws FileNotFoundException {
        //read in the lines from file
        FileReader reader = new FileReader(file);
        Gson gson = new Gson();
        var gameData = gson.fromJson(reader, GameData.class);

        //create a new game
        var players = getPlayersFromFile(gameData);

        Game game = new Game(new PlayingField(gameData.getField().length), players);
        var currentPlayer = game.getPlayers().stream().filter(player -> player.getPlayerID() == gameData.getCurrentPlayer()).findFirst().orElse(null);
        game.setCurrentPlayer(currentPlayer);
        game.getPlayingField().addDataFromJSON(gameData.getField());

        for (int actionTileID = 0; actionTileID < gameData.getUsedActionTiles().length; actionTileID++) {
            TokenType token = switch (actionTileID) {
                case 0 -> TokenType.Remover;
                case 1 -> TokenType.Mover;
                case 2 -> TokenType.Swapper;
                case 3 -> TokenType.Replacer;
                default -> throw new IllegalArgumentException("Invalid action tile ID");
            };

            for (int amount = 0; amount < gameData.getUsedActionTiles()[actionTileID]; amount++) {
                game.getUsedActionTokens().add(new Token(token));
            }
        }
        removeUsedTokensFromPile(game);

        Game.setGame(game);

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
        var map = new HashMap<TokenType, Integer>();
        for (int row = 0; row < game.getPlayingField().getFieldMap().length; row++) {
            for (int col = 0; col < game.getPlayingField().getFieldMap()[row].length; col++) {
                var token = game.getPlayingField().getFieldMap()[row][col].getToken();
                if (token.getTokenType() != TokenType.None) {
                    if (map.containsKey(token.getTokenType())) {
                        map.put(token.getTokenType(), map.get(token.getTokenType()) + 1);
                    } else {
                        map.put(token.getTokenType(), 1);
                    }
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

        for (var token : map.keySet()) {
            for (int i = 0; i < map.get(token); i++) {
                for (var tokenPileToken : game.getTokenDrawPile()) {
                    if (tokenPileToken.getTokenType() == token) {
                        game.getTokenDrawPile().remove(tokenPileToken);
                        break;
                    }
                }
            }
        }
    }
}
