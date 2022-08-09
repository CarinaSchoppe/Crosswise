package logic;

import javafx.application.Platform;
import logic.util.Constants;
import logic.util.Position;
import logic.util.TeamType;
import logic.util.TokenType;

import java.util.*;

/**
 * Class for a game instance of the game Crosswise
 *
 * @author Jacob Klövekorn
 */
@SuppressWarnings("unchecked")
public class Game {

    /**
     * private GameThread for the game
     */
    private Thread thread;
    /**
     * Players, that are playing this game round
     */
    private final ArrayList<Player> players;
    /**
     * Static attribut for the current running game
     */
    private static Game game;
    /**
     * Playing field of the game
     */
    private final PlayingField playingField;
    private final GUIConnector guiConnector;
    /**
     * Array of amount of used special tokens
     */
    private final ArrayList<Token> usedSpecialTokens = new ArrayList<>();
    /**
     * Draw pile of Tokens the players can draw from
     */
    private ArrayList<Token> tokenDrawPile = new ArrayList<>();

    /**
     * Current player
     */
    private Player currentPlayer = null;
    /**
     * Boolean to stop the current game
     */
    private boolean stop;


    //----------------------------------------------------------------------------------------------

    /**
     * Constructor, initialize parameters
     *
     * @param playingField playing field, consisting out of Positions with Tokens
     * @param players list of players playing the game
     * @param guiConnector gui connector for the GUI representation
     */
    private Game(PlayingField playingField, List<Player> players, GUIConnector guiConnector) {
        this.playingField = playingField;
        this.players = new ArrayList<>(players);
        if (guiConnector == null)
            throw new IllegalArgumentException("gameWindowHandler must not be null");
        this.guiConnector = guiConnector;
    }

    /**
     * Remove already used tokens from new drawPile
     */
    public static void removeUsedTokensFromPile() {
        //removes tokens, that are laying on the playing field
        EnumMap<TokenType, Integer> map = new EnumMap<>(TokenType.class);
        for (int row = 0; row < game.getPlayingField().getFieldMap().length; row++) {
            for (int col = 0; col < game.getPlayingField().getFieldMap()[row].length; col++) {
                Token token = game.getPlayingField().getFieldMap()[row][col].getToken();
                if (token.tokenType() == TokenType.NONE) {
                    continue;
                }
                if (map.containsKey(token.tokenType())) {
                    map.put(token.tokenType(), map.get(token.tokenType()) + 1);
                } else {
                    map.put(token.tokenType(), 1);
                }
            }
        }
        //removes action tokens
        for (Token used : game.getUsedActionTokens()) {
            if (map.containsKey(used.tokenType())) {
                map.put(used.tokenType(), map.get(used.tokenType()) + 1);
            } else {
                map.put(used.tokenType(), 1);
            }
        }
        //Removes tokens on the players hand
        for (Player player : game.getPlayers()) {
            for (Token token : player.getHandTokens()) {
                if (map.containsKey(token.tokenType())) {
                    map.put(token.tokenType(), map.get(token.tokenType()) + 1);
                } else {
                    map.put(token.tokenType(), 1);
                }
            }
        }
        //removes all tokens in the map from the draw pile of the game
        for (Map.Entry<TokenType, Integer> entry : map.entrySet()) {
            TokenType token = entry.getKey();
            for (int i = 0; i < map.get(token); i++) {
                for (Token tokenPileToken : game.getTokenDrawPile()) {
                    if (tokenPileToken.tokenType() != token) {
                        continue;
                    }
                    game.removeTokenDrawPileToken(tokenPileToken);
                    break;
                }
            }
        }
    }

    /**
     * Creates the actual game
     *
     * @param field       playing field
     * @param isActives   List if the players are active
     * @param isAis       List if the players are AIs
     * @param playerNames List of player-names
     * @param connector   GuiConnector
     * @param fileSetup   If loaded from a file
     */
    public static void createNewGame(List<String> playerNames, List<Boolean> isAis, List<Boolean> isActives, GUIConnector connector, boolean fileSetup, PlayingField field) {
        if (field == null)
            field = new PlayingField(Constants.GAMEGRID_SIZE);
        var players = new ArrayList<Player>();
        for (int i = 0; i < playerNames.size(); i++) {
            var name = playerNames.get(i);
            var isAI = isAis.get(i);
            var isActive = isActives.get(i);
            if (Boolean.TRUE.equals(isAI)) {
                var ai = new AI(i, isActive, name);
                players.add(ai);
            } else {
                var player = new Player(i, isActive, name);
                players.add(player);
            }
        }

        Game game = new Game(field, players, connector);
        //setup game
        makeGameReady(game, fileSetup);
    }

    /**
     * Sets up the game and starts all necessary methods to begin
     *
     * @param game      Current game
     * @param fileSetup boolean if the game was loaded with a file
     */
    private static void makeGameReady(Game game, boolean fileSetup) {
        if (Game.getGame() != null) {
            Game.getGame().cancel();
        }
        //create new game thread
        Thread thread = new Thread(() -> {
            //make thread sleep
            game.setup(fileSetup);
            game.start();

        });
        //setup game with the running thread
        game.players.forEach(Player::create);
        setGame(game, thread);

        //if the game wasn't loaded from a file, start it here
        Game.getGame().guiConnector.resetSpecialTokenImages();
        if (!fileSetup) {
            Game.game.guiConnector.startGamePopUp();

        }
        //reset the specialTokensImages
    }


    /**
     * shows error message while trying to create a game with wrong parameters
     *
     * @param caseID error type id
     */
    public void faultyStartup(Integer caseID) {
        //makes thread hide all hands and create an alert
        //try catch block for tests, that cant run "run-later"
        try {
            Platform.runLater(() -> {
                guiConnector.showHand(true, 0, true);
                guiConnector.faultyAlert(caseID);
            });
        } catch (Exception ignored) {
            guiConnector.showHand(true, 0, true);
            guiConnector.faultyAlert(caseID);
        }
        //cancel the current game
        game.cancel();
    }

    /**
     * Set the current game
     *
     * @param game   current game
     * @param thread thread on which the game runs
     */
    public static void setGame(Game game, Thread thread) {
        Game.game = game;
        Game.game.thread = thread;

    }

    /**
     * Creates a new DrawPile and fills it with tokens
     */
    private void fillPile() {
        if (stop) {
            handleOver();
            return;
        }
        this.tokenDrawPile = new ArrayList<>();
        //iterate through all different types of tokens
        for (TokenType token : TokenType.values()) {
            if (token == TokenType.NONE) continue;
            if (token.isSpecial()) {
                //Creates all special tokens and adds them to the draw pile
                for (int i = 0; i < Constants.AMOUNT_ACTION_TOKENS; i++) { //12 tokens
                    tokenDrawPile.add(new Token(token));
                }
            } else {
                //creates all symbol tokens and adds them to the draw pile
                for (int i = 0; i < Constants.AMOUNT_NORMAL_TOKENS; i++) { //42 tokens
                    tokenDrawPile.add(new Token(token));
                }
            }
        }
        //shuffle the draw pile
        for (int i = 0; i < tokenDrawPile.size(); i++) {
            int randomIndex = new Random().nextInt(tokenDrawPile.size());
            Token temp = tokenDrawPile.get(i);
            tokenDrawPile.set(i, tokenDrawPile.get(randomIndex));
            tokenDrawPile.set(randomIndex, temp);
        }
    }

    /**
     * Makes every Player draw Tokens, until their hands are full
     */
    private void playerPileSetup() {
        if (stop) {
            handleOver();
            return;
        }
        //For each player, let them draw as many tokens as big their hand size is supposed to be
        for (Player player : players.stream().filter(Player::isActive).toList()) {
            for (int i = 0; i < Constants.HAND_SIZE; i++) {
                player.drawToken();
            }
        }
    }

    /**
     * Creates new gameLogic, creates a drawPile and makes all players draw tokens, until their
     * hands are full. If the game is loaded from a file, skip the creating and drawing
     *
     * @param fileLoaded was the game loaded from a file
     */
    public void setup(boolean fileLoaded) {
        if (handleOver()) {
            return;
        }


        if (stop)
            return;
        //check, if the game was started with 0 players
        if (Team.getHorizontalTeam().getPlayers().isEmpty() && Team.getVerticalTeam().getPlayers().isEmpty()) {
            faultyStartup(0);
            if (CrossWise.UI)
                return;
            else
                throw new IllegalArgumentException("No players");
        }
        //create draw pile and if it wasn't loaded from a file, let the players draw their tokens
        fillPile();

        if (!fileLoaded) {
            playerPileSetup();
        }


        //if the current player has already been set
        if (currentPlayer == null)
            currentPlayer = players.stream().filter(Player::isActive).toList().get(0);

        guiConnector.changeCurrentPlayerText(currentPlayer.getName());
    }


    /**
     * Computes logic for having the next players turn
     */
    private void nextPlayer() {
        //gets all active players
        List<Player> allPlayers = this.players.stream().filter(Player::isActive).toList();
        //puts the first ever to play player to the one with the ID: 0
        if (currentPlayer == null && !allPlayers.isEmpty()) {
            currentPlayer = allPlayers.get(0);
        } else if (currentPlayer != null && !allPlayers.isEmpty()) {
            //set next player
            int index = allPlayers.indexOf(currentPlayer);
            if (index == allPlayers.size() - 1) {
                currentPlayer = allPlayers.get(0);
            } else {
                currentPlayer = allPlayers.get(index + 1);
            }
        } else {
            //handler for the fact that the game is over
            currentPlayer = null;
            handleOver();
            return;
        }

        //shows the hand of the next player
        try {
            Platform.runLater((() -> {
                guiConnector.showHand(currentPlayer instanceof AI, currentPlayer.getPlayerID(), false);
                guiConnector.changeCurrentPlayerText(currentPlayer.getName());
            }));
        } catch (Exception ignored) {
            guiConnector.showHand(currentPlayer instanceof AI, currentPlayer.getPlayerID(), false);
            guiConnector.changeCurrentPlayerText(currentPlayer.getName());
        }


        if (CrossWise.DEBUG) {
            System.out.println("Current player is: " + currentPlayer.getName() + " with ID: " + currentPlayer.getPlayerID());
        }
        //if the player is an AI player, let the AI make their move, otherwise notify the next player
        if (currentPlayer instanceof AI ai) {
            //add the new move to the thread, so the player move will be finished until the AI move starts
            try {
                Platform.runLater(ai::makeMove);
            } catch (Exception ignored) {
                ai.makeMove();
            }
        } else {
            //notifies the next player with an alert
            guiConnector.notifyTurn(currentPlayer.getName(), currentPlayer.getPlayerID());
        }
    }

    /**
     * Handle symbol token move done by a player
     *
     * @param type type of the token
     * @param x    x coordinate of the position on the board
     * @param y    y coordinate of the position on the board
     */
    public void playerSymbolTokenMove(TokenType type, Integer x, Integer y) {
        currentPlayer.normalTokenTurn(new Token(type), new Position(x, y));
        turnDone();
    }

    /**
     * Handle remover token move done by a player
     *
     * @param x x coordinate of the position on the board
     * @param y y coordinate of the position on the board
     */
    public void playerRemoverTokenMove(Integer x, Integer y) {
        currentPlayer.removerTokenTurn(currentPlayer.getCorrespondingToken(TokenType.REMOVER), new Position(x, y));
        turnDone();
    }

    /**
     * Handle mover token move done by a player
     *
     * @param fromX x coordinate of the start position on the board
     * @param fromY y coordinate of the start position on the board
     * @param toX   y coordinate of the end position on the board
     * @param toY   y coordinate of the end position on the board
     */
    public void playerMoverTokenMove(Integer fromX, Integer fromY, Integer toX, Integer toY) {
        currentPlayer.moverTokenTurn(currentPlayer.getCorrespondingToken(TokenType.MOVER), new Position(fromX, fromY), new Position(toX, toY));
        turnDone();
    }

    /**
     * Handle swapper token move done by a player
     *
     * @param fromX x coordinate of the start position on the board
     * @param fromY y coordinate of the start position on the board
     * @param toX   y coordinate of the end position on the board
     * @param toY   y coordinate of the end position on the board
     */
    public void playerSwapperTokenMove(Integer fromX, Integer fromY, Integer toX, Integer toY) {
        currentPlayer.swapperTokenTurn(currentPlayer.getCorrespondingToken(TokenType.SWAPPER), new Position(fromX, fromY), new Position(toX, toY));
        turnDone();
    }

    /**
     * Handle replacer token move done by a player
     *
     * @param fromX     x coordinate of the start position on the board
     * @param fromY     y coordinate of the start position on the board
     * @param handIndex hand index of position clicked on the hand
     */
    public void playerReplacerTokenMove(Integer fromX, Integer fromY, Integer handIndex) {
        currentPlayer.replacerTokenTurn(currentPlayer.getCorrespondingToken(TokenType.REPLACER), new Position(fromX, fromY), new Position(handIndex));
        turnDone();
    }

    /**
     * Computes logic for the ending of a game
     *
     * @return true, if the game is over
     */
    private boolean handleOver() {
        if (stop) return true;
        Map<Boolean, Team> over = isGameOver();
        //if there weren't any players in the game from the beginning
        if (players.isEmpty()) {
            if (CrossWise.DEBUG)
                System.out.println("No players left!");
            return true;
        } else if (over.containsKey(true)) {
            Team team = over.get(true);
            Team.givePoints();
            //hande game if there was a draw
            if (team == null) {
                if (CrossWise.DEBUG)
                    System.out.println("Game is over, but no team has won!");
                guiConnector.gameWonNotifier(null, Team.getHorizontalTeam().getPoints(), false);
            } else {
                //handle game won with a specific team won
                guiConnector.gameWonNotifier(team.getTeamType(), team.getPoints(), team.isRowWin());
                if (CrossWise.DEBUG) {
                    System.out.println(Team.getVerticalTeam().getPoints() + " " + Team.getHorizontalTeam().getPoints());
                    System.out.println("Game is over, team " + team.getTeamType().getTeamName() + " has won!");
                }
            }
            if (CrossWise.DEBUG)
                System.out.println(System.currentTimeMillis() - CrossWise.time);
            return true;
        }
        return false;
    }

    /**
     * Cancel the current game and stop its thread
     */
    public synchronized void cancel() {
        stop = true;
        players.clear();
        Team.setVerticalTeam(new Team(TeamType.VERTICAL));
        Team.setHorizontalTeam(new Team(TeamType.HORIZONTAL));
        Team.setDeactiveTeam(new Team(TeamType.DEACTIVE));
        handleOver();
        if (CrossWise.DEBUG)
            System.out.println("Game canceled!");
        //kill the this.thread
        thread.interrupt();
    }

    /**
     * Used for tests to simulate a started game
     *
     * @param fileLoaded boolean, if the game was loaded from a file
     */
    public void testStart(boolean fileLoaded) {
        setup(fileLoaded);
        start();
    }

    /**
     * Starts the Game
     */
    private void start() {
        if (handleOver())
            return;
        GameLogger.logGameSetupLog();
        //check for faulty setup of the players where both teams are empty
        if (Team.getHorizontalTeam().getPlayers().isEmpty() && Team.getVerticalTeam().getPlayers().isEmpty()) {
            return;
        }
        //check, if both teams have an equal amount of players, if not create an already and return
        if (Team.getHorizontalTeam().getPlayers().size() != Team.getVerticalTeam().getPlayers().size()) {
            faultyStartup(1);
            if (CrossWise.UI)
                return;
            else
                throw new IllegalArgumentException("Number of players in horizontal team is not equal to number of players in vertical team!");
        }
        //check, if the config has the right amount of active players in it and the required minimum
        if (players.stream().filter(Player::isActive).toList().size() < Constants.MIN_PLAYER_SIZE || players.stream().filter(Player::isActive).toList().size() % 2 != 0) {
            faultyStartup(2);
            if (CrossWise.UI)
                return;
            else
                throw new IllegalArgumentException("Not enough players or not even number of players!");

        }
        //shows hand of the current player
        guiConnector.showHand(currentPlayer instanceof AI, currentPlayer.getPlayerID(), false);
        if (currentPlayer instanceof AI ai) {
            //add the new move to the thread, so the current move will be finished until the AI move starts
            try {
                Platform.runLater(ai::makeMove);
            } catch (Exception e) {
                //for test purposes that cant handle run-later
                ai.makeMove();
            }
        } else {
            //notifies the next player with an alert
            guiConnector.notifyTurn(currentPlayer.getName(), currentPlayer.getPlayerID());
        }
    }

    /**
     * Computes logic for turns, that are over
     */
    public void turnDone() {
        if (stop) {
            handleOver();
            return;
        }
        Team.givePoints();
        if (currentPlayer == null)
            game.cancel();

        //if the turn is over, do nothing
        if (handleOver()) {
            return;
        }
        //otherwise try to draw a token
        currentPlayer.drawToken();
        try {
            if (CrossWise.UI)
                Thread.sleep(CrossWise.DELAY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        //Let the next player do their turn
        nextPlayer();
    }

    /**
     * Start the game thread
     */
    public void startGame() {
        synchronized (this) {
            thread.start();
        }
    }

    /**
     * Creates an array representation of the current points for each line
     *
     * @return Integer array of points
     */
    public Integer[] pointsArray() {
        //get points for each line
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();

        SortedSet<Integer> keys = new TreeSet<>(map.keySet());

        Integer[] pointsArray = new Integer[Constants.GAMEGRID_SIZE * 2];
        int counter = 0;

        //Add results to new map

        for (Integer key : keys) {
            pointsArray[counter] = map.get(key);
            counter++;
        }
        //Debug
        if (CrossWise.DEBUG) {
            System.out.println(map);
            System.out.println(Arrays.toString(pointsArray));
        }
        return pointsArray;
    }

    /**
     * Checks, if the game is over and which team won if it is
     *
     * @return Map, with the boolean if the game is over and a team, that won
     */
    public Map<Boolean, Team> isGameOver() {
        Map<Boolean, Team> map = new HashMap<>();
        //if horizontal Team won via a full row

        if (checkRows(playingField)) {
            Team.getHorizontalTeam().setRowWin(true);
            map.put(true, Team.getHorizontalTeam());
            return map;
        }
        //if the vertical team won via a full column
        if (checkColumns(playingField)) {
            Team.getVerticalTeam().setRowWin(true);
            map.put(true, Team.getVerticalTeam());
            return map;
        }
        //check if there are still tokens missing on the playing field
        for (int i = 0; i < playingField.getSize(); i++) {
            for (int j = 0; j < playingField.getSize(); j++) {
                if (playingField.getFieldMap()[i][j].getToken().tokenType() == TokenType.NONE) {
                    map.put(false, null);
                    return map;
                }
            }
        }
        //if both teams have the same amount of points
        if (Team.getHorizontalTeam().getPoints() == Team.getVerticalTeam().getPoints()) {
            map.put(true, null);
        } else if (Team.getHorizontalTeam().getPoints() > Team.getVerticalTeam().getPoints()) {
            map.put(true, Team.getHorizontalTeam());
        } else {
            map.put(true, Team.getVerticalTeam());
        }
        return map;

    }


    /**
     * Check all rows for a game win
     *
     * @param field playing field
     * @return true, if game was won with 6 in a row
     */
    @SuppressWarnings("Duplicates")
    private boolean checkRows(PlayingField field) {
        TokenType current = null;
        for (int i = 0; i < field.getSize(); i++) { //get horizontal
            boolean equal = true;
            for (int j = 0; j < field.getSize(); j++) { // get field on row
                if (current == null) {
                    if (field.getFieldMap()[i][j].getToken().tokenType() != TokenType.NONE) {
                        current = field.getFieldMap()[i][j].getToken().tokenType();
                    } else {
                        equal = false;
                        break;
                    }
                } else {
                    if (!(field.getFieldMap()[i][j].getToken().tokenType() == TokenType.NONE || field.getFieldMap()[i][j].getToken().tokenType() != current))
                        continue;
                    equal = false;
                    current = null;
                    break;
                }
            }
            if (equal)
                return true;
        }
        return false;
    }

    /**
     * Check all columns for a game win
     *
     * @param field playing field
     * @return true, if game was won with 6 in a column
     */
    @SuppressWarnings("Duplicates")
    private boolean checkColumns(PlayingField field) {
        TokenType current = null;

        for (int i = 0; i < field.getSize(); i++) {
            boolean equal = true;
            for (int j = 0; j < field.getSize(); j++) {
                if (current == null) {
                    if (field.getFieldMap()[j][i].getToken().tokenType() != TokenType.NONE) {
                        current = field.getFieldMap()[j][i].getToken().tokenType();
                    } else {
                        equal = false;
                        break;
                    }
                } else {
                    if (!(field.getFieldMap()[j][i].getToken().tokenType() == TokenType.NONE || field.getFieldMap()[j][i].getToken().tokenType() != current))
                        continue;
                    equal = false;
                    current = null;
                    break;

                }
            }
            if (equal)
                return true;
        }
        return false;
    }

    public void addNewActionTile(Token token) {
        usedSpecialTokens.add(token);
    }

    public void removeTokenDrawPileToken(Token token) {
        tokenDrawPile.remove(token);
    }

    //-------------------------------------------Getter and Setter------------------------------------------------------


    public PlayingField getPlayingField() {
        return playingField;
    }

    public List<Player> getPlayers() {
        return (List<Player>) players.clone();
    }

    public List<Token> getUsedActionTokens() {
        return (List<Token>) usedSpecialTokens.clone();
    }

    public List<Token> getTokenDrawPile() {
        return (List<Token>) tokenDrawPile.clone();
    }

    public Player getCurrentPlayer() {
        return currentPlayer.copy();
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public static Game getGame() {
        return game;
    }

    public GUIConnector getGUIConnector() {
        return guiConnector;
    }

}
