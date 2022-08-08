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
     * @param file         File, which contains the game data
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
        var playerNames = new ArrayList<String>();
        var isAI = new ArrayList<Boolean>();
        var isActive = new ArrayList<Boolean>();

        for (Player value : players) {
            playerNames.add(value.getName());
            isAI.add(value instanceof AI);
            isActive.add(value.isActive());
        }


        Game.createNewGame(playerNames, isAI, isActive, guiConnector, true, new PlayingField(gameData.getField().length));
        players.forEach(Player::create);

        //add tokens to playerhands
        for (var playerItem : players) {
            for (var player : Game.getGame().getPlayers()) {
                if (player.getPlayerID() == playerItem.getPlayerID()) {
                    for (var token : playerItem.getHandTokens()) {
                        player.addTokenToHand(token);
                    }
                    break;
                }
            }
        }   //Create new game and setup parameters
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
        Game.removeUsedTokensFromPile();
        //start the game
        //convert the player ids into an array of int
        var playerIDs = new int[Constants.PLAYER_COUNT];
        for (int i = 0; i < Constants.PLAYER_COUNT; i++) {
            playerIDs[i] = players.get(i).getPlayerID();
        }
        var playerHands = new TokenType[Constants.PLAYER_COUNT][Constants.HAND_SIZE];
        for (int i = 0; i < Constants.PLAYER_COUNT; i++) {
            for (int j = 0; j < Game.getGame().getPlayers().get(i).getHandTokens().size(); j++) {
                playerHands[i][j] = players.get(i).getHandTokens().get(j).tokenType();
            }
        }

        guiConnector.showGUIElements();
        guiConnector.generateGrid();
        guiConnector.resetText();
        guiConnector.setupDragAndDropEvent();

        guiConnector.performMoveUIUpdate(playerIDs, playerHands, Game.getGame().getPlayingField().convertToTokenTypeArray(), Game.getGame().pointsArray());
        guiConnector.startGamePopUp();
    }

    /**
     * Checks for an invalid config in the loaded GameData class
     *
     * @param gameData
     * @return
     */
    private static boolean checkInvalidConfig(final GameData gameData) {
        //invalid player config, cant be 0 and needs to load 4 players
        if (gameData.getPlayers().length != 4) return true;
        if (gameData.getCurrentPlayer() > 3 || gameData.getCurrentPlayer() < 0)
            return true;

        //turned off number of active players
        int ids = 0;
        int active = 0;
        for (int i = 0; i < gameData.getPlayers().length; i++) {
            if (gameData.getPlayers()[i].isActive()) {
                ids += (i + 1);
                active += 1;
            }
        }
        //invalid if no players are active
        if (active == 0 || ids % 2 == 0 && active != 4) return true;

        //check the minimum size of a game
        if (gameData.getField().length < 2) return true;
        //check rows and column size equal
        for (var row : gameData.getField()) {
            if (row.length != gameData.getField()[0].length || row.length != gameData.getField().length) return true;
        }

        //invalid if hands of players are 0 or above 4 (interpreted, that it should be possible to load players with less hand tokens and fill them up
        if (Arrays.stream(gameData.getPlayers()).anyMatch(player -> player.isActive() && player.getHand().length == 0 || !player.isActive() && player.getHand().length > 0)) return true;
        //Invalid if the IDs of the tokens are below 0 or above 10 (would need to be changed if the game should be started with less or more unique tokens)
        if (Arrays.stream(gameData.getPlayers()).anyMatch(player -> Arrays.stream(player.getHand()).anyMatch(token -> token < 0 || token > 10))) return true;
        //same as above for the game field tokens
        if (Arrays.stream(gameData.getField()).anyMatch(fieldRow -> Arrays.stream(fieldRow).anyMatch(token -> token < 0 || token > 10))) return true;
        //hand must contain 4 tokens
        if (Arrays.stream(gameData.getPlayers()).anyMatch(player -> player.getHand().length > Constants.HAND_SIZE)) return true;
        //special tokens need to fit the game rules
        return Arrays.stream(gameData.getUsedActionTiles()).anyMatch(special -> special > Constants.AMOUNT_ACTION_TOKENS) || gameData.getUsedActionTiles().length > Constants.UNIQUE_ACTION_TOKENS;
    }

    /**
     * Loads up players from GameData class
     *
     * @param gameData GameData, from which the players are loaded
     * @return ArrayList of players
     */
    private static ArrayList<Player> getPlayersFromFile(GameData gameData) {
        ArrayList<Player> players = new ArrayList<>();
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


}
