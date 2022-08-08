package gui;

import com.google.gson.Gson;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import logic.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/**
 * Class for reading a json file and creating a game out of it
 *
 * @author Jacob Klövekorn
 */
public class FileInputReader {

    /**
     * Constructor
     */
    private FileInputReader() {
    }

    /**
     * Static method for selecting a file in a new window
     *
     * @param scene current scene
     * @return File, which was selected
     */
    public static File selectFile(Scene scene) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a file");
        //limits the files seen by the user to folders and JSON-files
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        //open the window, to choose a file
        return chooser.showOpenDialog(scene.getWindow());
    }

    /**
     * Static method for reading the contents of a file and creating a new game out of it, if possible
     *
     * @param file File, which contains the game data
     * @param guiConnector gui connector for the new game
     */
    public static void readFile(File file, GUIConnector guiConnector) {

        if (file == null) return;
        //read in the lines from file
        FileReader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        //create new GameData instance which holds the necessary information to load up a game
        final GameData gameData = gson.fromJson(reader, GameData.class);
        //checks in checkInvalidConfig method, if the given file creates a valid input
        if (checkInvalidConfig(gameData)) {
            guiConnector.showError("Config is not valid");
            return;
        }

        //create objects needed for the game
        ArrayList<Player> players = getPlayersFromFile(gameData);

        Game.createNewGame(players, guiConnector, true, new PlayingField(gameData.getField().length));
        players.forEach(Player::create);
        //Create new game and setup parameters
        Player currentPlayer = Game.getGame().getPlayers().stream().filter(player -> player.getPlayerID() == gameData.getCurrentPlayer()).findFirst().orElse(null);
        Game.getGame().setCurrentPlayer(currentPlayer);
        Game.getGame().getPlayingField().addDataFromJSON(gameData.getField());
        //add special tokens to game
        for (int actionTileID = 0; actionTileID < gameData.getUsedActionTiles().length; actionTileID++) {
            TokenType token = switch (actionTileID) {
                case 0 -> TokenType.REMOVER;
                case 1 -> TokenType.MOVER;
                case 2 -> TokenType.SWAPPER;
                case 3 -> TokenType.REPLACER;
                default -> throw new IllegalArgumentException("Invalid action tile ID");
            };

            for (int amount = 0; amount < gameData.getUsedActionTiles()[actionTileID]; amount++) {
                Game.getGame().addNewActionTile(new Token(token));
            }
        }
        removeUsedTokensFromPile(Game.getGame());
        //start the game
        Game.getGame().getGUIConnector().performMoveUIUpdate(Game.getGame().getPlayers(), Game.getGame().getPlayingField().convertToTokenTypeArray(), Game.getGame().pointsArray());
        Game.getGame().getGUIConnector().startGamePopUp();
        Game.getGame().startGame();
        Game.getGame().getThread().start();
    }

    /**
     * Checks for an invalid config in the loaded GameData class
     *
     * @param gameData
     * @return
     */
    private static boolean checkInvalidConfig(final GameData gameData) {
        //invalid player config, cant be 0 and needs to load 4 players
        if (gameData.getPlayers().length == 0 || gameData.getPlayers().length % 2 != 0)
            return true;

        //invalid  number of active players
        int current = 0;
        for (int i = 0; i < gameData.getPlayers().length; i++) {
            if (gameData.getPlayers()[i].isActive())
                current += i;
        }
        if (current % 2 == 0 && Arrays.stream(gameData.getPlayers()).noneMatch(PlayerData::isActive))
            return true;

        //invalid if no players are active
        if (Arrays.stream(gameData.getPlayers()).noneMatch(PlayerData::isActive))
            return true;
        //invalid if hands of players are 0 or above 4 (interpreted, that it should be possible to load players with less hand tokens and fill them up
        if (Arrays.stream(gameData.getPlayers()).anyMatch(player -> player.isActive() && player.getHand().length == 0 || !player.isActive() && player.getHand().length > 0))
            return true;
        //Invalid if the IDs of the tokens are below 0 or above 10 (would needed to be changed if the game should be started with less or more unique tokens)
        if (Arrays.stream(gameData.getPlayers()).anyMatch(player -> Arrays.stream(player.getHand()).anyMatch(token -> token < 0 || token > 10)))
            return true;
        //same as above for the game field tokens
        if (Arrays.stream(gameData.getField()).anyMatch(fieldRow -> Arrays.stream(fieldRow).anyMatch(token -> token < 0 || token > 10)))
            return true;
        //hand must contain 4 players
        if (Arrays.stream(gameData.getPlayers()).anyMatch(player -> player.getHand().length > Constants.HAND_SIZE))
            return true;
        //special tokens need to fit the game rules
        return Arrays.stream(gameData.getUsedActionTiles()).anyMatch(special -> special > Constants.AMOUNT_ACTION_TOKENS) ||
                gameData.getUsedActionTiles().length > Constants.UNIQUE_ACTION_TOKENS;
    }

    /**
     * Loads up players from GameData class
     *
     * @param gameData GameData, from which the players are loaded
     * @return ArrayList of players
     */
    private static ArrayList<Player> getPlayersFromFile(GameData gameData) {
        ArrayList<Player> players = new ArrayList<Player>();
        for (int i = 0; i < gameData.getPlayers().length; i++) {
            PlayerData playerData = gameData.getPlayers()[i];
            //checks if the player is an AI player or a normal player
            if (!playerData.isAI()) {
                Player player = new Player(i, playerData.isActive(), playerData.getName());
                Arrays.stream(playerData.getHand()).forEach(token -> player.addTokenToHand(new Token(TokenType.getTokenType(token))));
                players.add(player);
            } else {
                AI ai = new AI(i, playerData.isActive(), playerData.getName());
                Arrays.stream(playerData.getHand()).forEach(token -> ai.addTokenToHand(new Token(TokenType.getTokenType(token))));
                players.add(ai);
            }
        }
        return players;
    }

    /**
     * Remove already used tokens from new drawPile
     *
     * @param game Game
     */
    private static void removeUsedTokensFromPile(Game game) {
        //removes tokens, that are laying on the playing field
        EnumMap<TokenType, Integer> map = new EnumMap<>(TokenType.class);
        for (int row = 0; row < game.getPlayingField().getFieldMap().length; row++) {
            for (int col = 0; col < game.getPlayingField().getFieldMap()[row].length; col++) {
                Token token = game.getPlayingField().getFieldMap()[row][col].getToken();
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
        //removes action tokens
        for (Token used : game.getUsedActionTokens()) {
            if (map.containsKey(used.getTokenType())) {
                map.put(used.getTokenType(), map.get(used.getTokenType()) + 1);
            } else {
                map.put(used.getTokenType(), 1);
            }
        }
        //Removes tokens on the players hand
        for (Player player : game.getPlayers()) {
            for (Token token : player.getHandTokens()) {
                if (map.containsKey(token.getTokenType())) {
                    map.put(token.getTokenType(), map.get(token.getTokenType()) + 1);
                } else {
                    map.put(token.getTokenType(), 1);
                }
            }
        }

        for (Map.Entry<TokenType, Integer> entry : map.entrySet()) {
            TokenType token = entry.getKey();
            for (int i = 0; i < map.get(token); i++) {
                for (Token tokenPileToken : game.getTokenDrawPile()) {
                    if (tokenPileToken.getTokenType() != token) {
                        continue;
                    }
                    game.removeTokenDrawPileToken(tokenPileToken);
                    break;
                }
            }
        }
    }
}
