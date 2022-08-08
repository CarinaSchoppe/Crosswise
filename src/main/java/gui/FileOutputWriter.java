package gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.Scene;
import logic.*;

import java.io.File;
import java.util.List;

/**
 * Class for writing a save file for the game Crosswise
 *
 * @author Jacob Klövekorn
 */
public class FileOutputWriter {
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

        if (file == null) return;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(generateGameData(Game.getGame().getPlayers(),
                Game.getGame().getCurrentPlayer().getPlayerID(), Game.getGame().getPlayingField().getSize(),
                Game.getGame().getPlayingField().getFieldMap(), Game.getGame().getUsedActionTokens()));
        //write the json to a file
        try (var writer = new java.io.PrintWriter(file)) {
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
    private static GameData generateGameData(List<Player> gamePlayers, int currentPlayerID, int playingFieldSize, Position[][] playingFieldMap, List<Token> usedActionTokens) {

        PlayerData[] players = new PlayerData[gamePlayers.size()];
        for (int i = 0; i < gamePlayers.size(); i++) {
            players[i] = generatePlayerData(gamePlayers.get(i).getHandTokens(), gamePlayers.get(i).getName(), gamePlayers.get(i) instanceof AI, gamePlayers.get(i).isActive());
        }
        return new GameData(players, currentPlayerID, generateCorrespondingPlayingField(playingFieldSize, playingFieldMap), generateUsedActionTilesArray(usedActionTokens));

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
    private static PlayerData generatePlayerData(List<Token> tokens, String playerName, boolean isAI, boolean isActive) {
        var hand = new int[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            hand[i] = tokens.get(i).getTokenType().getValue();
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
    private static int[][] generateCorrespondingPlayingField(int playingFieldSize, Position[][] fieldMap) {
        int[][] field = new int[playingFieldSize][playingFieldSize];
        for (int i = 0; i < playingFieldSize; i++) {
            for (int j = 0; j < playingFieldSize; j++) {
                //translate Token from position to TokenType int value
                field[i][j] = fieldMap[i][j].getToken().getTokenType().getValue();
            }
        }
        return field;
    }

    /**
     * Generate the used action tokens from a list into an array
     *
     * @param usedActionTokens List of used actionTokens
     * @return int array, where the number for each array field is the amount of used tokens for the specific token
     */
    private static int[] generateUsedActionTilesArray(List<Token> usedActionTokens) {
        int[] usedActionTiles = new int[Constants.UNIQUE_ACTION_TOKENS];
        for (int i = 0; i < Constants.UNIQUE_ACTION_TOKENS; i++) {
            usedActionTiles[i] = 0;
        }
        //add tokens to the array, (needs to be changed, if the tokens or their order is changed)
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
