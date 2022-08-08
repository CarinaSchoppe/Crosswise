package logic;

import javafx.application.Platform;

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
     * @param players
     * @param guiConnector
     */
    private Game(PlayingField playingField, List<Player> players, GUIConnector guiConnector) {
        this.playingField = playingField;
        this.players = new ArrayList<>(players);
        if (guiConnector == null)
            throw new IllegalArgumentException("gameWindowHandler must not be null");
        this.guiConnector = guiConnector;
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
        players.forEach(Player::create);

        Game game = new Game(field, players, connector);
        //setup game
        createStuff(game, fileSetup);
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
     * @param isActives   List if the players are active
     * @param isAis       List if the players are AIs
     * @param playerNames List of player-names
     * @param connector   GuiConnector
     * @param fileSetup   If loaded from a file
     */
    public static void createNewGame(List<String> playerNames, List<Boolean> isAis, List<Boolean> isActives, GUIConnector connector, boolean fileSetup) {
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
        players.forEach(Player::create);

        Game game = new Game(new PlayingField(Constants.GAMEGRID_SIZE), players, connector);
        //setup game
        createStuff(game, fileSetup);
    }

    /**
     * Sets up the game and starts all neccesary methods to begin
     *
     * @param game      Current game
     * @param fileSetup boolean, if the game was loaded with a file
     */
    private static void createStuff(Game game, boolean fileSetup) {
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
        Game.setGame(game, thread);
        //if the game wasnt loaded from a file, start it here
        if (!fileSetup) {
            Game.game.guiConnector.startGamePopUp();
            Game.getGame().startGame();
        }
        //reset the specialTokensImages
        Game.getGame().guiConnector.resetSpecialTokenImages();
    }


    /**
     * shows error message while trying to create a game with wrong parameters
     *
     * @param caseID error type id
     */
    public void faultyStartup(Integer caseID) {
        //makes thread hide all hands and create an alert
        Platform.runLater(() -> {
            guiConnector.showHand(true, 0, true);
            guiConnector.faultyAlert(caseID);
        });
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
                try {
                    player.drawToken();
                } catch (NoTokenException e) {
                    throw new NoTokenException("No more tokens left in the Pile while startup! "
                            + "Configuration error!");
                }
            }
        }
    }

    /**
     * Creates new gameLogic, creates a drawPile and makes all players draw tokens, until their
     * hands are full. If the game is loaded from a file, skip the creating and drawing
     *
     * @param fileLoaded was the game loaded from a file
     */
    private void setup(boolean fileLoaded) {
        if (stop) {
            handleOver();
            return;
        }
        handleOver();
        //check, if the game was started with 0 players
        if (Team.getHorizontalTeam().getPlayers().isEmpty() && Team.getVerticalTeam().getPlayers().isEmpty()) {
            faultyStartup(0);
            return;
        }
        //create draw pile and if it wasnt loaded from a file, let the players draw their tokens
        fillPile();
        if (!fileLoaded) {
            playerPileSetup();
        }
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
        Platform.runLater((() -> {
            guiConnector.showHand(currentPlayer instanceof AI, currentPlayer.getPlayerID(), false);
            guiConnector.changeCurrentPlayerText(currentPlayer.getName());
        }));

        if (CrossWise.DEBUG) {
            System.out.println("Current player is: " + currentPlayer.getName() + " with ID: " + currentPlayer.getPlayerID());
        }
        //if the player is an AI player, let the AI make their move, otherwise notify the next player
        if (currentPlayer instanceof AI ai) {
            //add the new move to the thread, so the player move will be finished until the ai move starts
            Platform.runLater(ai::makeMove);
        } else {
            //notifies the next player with an alert
            guiConnector.notifyTurn(currentPlayer.getName(), currentPlayer.getPlayerID());
        }
    }

    /**
     * Handle symbol token move done by a player
     *
     * @param tokenString type of the token
     * @param x           x coordinate of the position on the board
     * @param y           y coordinate of the position on the board
     */
    public void playerSymbolTokenMove(String tokenString, Integer x, Integer y) {
        currentPlayer.normalTokenTurn(currentPlayer.getCorrespondingToken(tokenString), new Position(x, y));
        turnDone();
    }

    /**
     * Handle remover token move done by a player
     *
     * @param x x coordinate of the position on the board
     * @param y y coordinate of the position on the board
     */
    public void playerRemoverTokenMove(Integer x, Integer y) {
        currentPlayer.removerTokenTurn(currentPlayer.getCorrespondingToken("REMOVER"), new Position(x, y));
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
        currentPlayer.moverTokenTurn(currentPlayer.getCorrespondingToken("MOVER"), new Position(fromX, fromY), new Position(toX, toY));
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
        currentPlayer.swapperTokenTurn(currentPlayer.getCorrespondingToken("SWAPPER"), new Position(fromX, fromY), new Position(toX, toY));
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
        currentPlayer.replacerTokenTurn(currentPlayer.getCorrespondingToken("REPLACER"), new Position(fromX, fromY), new Position(handIndex));
        turnDone();
    }

    /**
     * Computes logic for the ending of a game
     *
     * @return true, if the game is over
     */
    private boolean handleOver() {
        if (stop)
            return true;
        Map<Boolean, Team> over = isGameOver(playingField);
        if (players.isEmpty()) {
            if (CrossWise.DEBUG)
                System.out.println("No players left!");
            GameLogger.saveLogToFile(Constants.LOG_FILE_NAME);
            return true;
        } else if (over.containsKey(true)) {
            Team team = over.get(true);
            if (team == null) {
                if (CrossWise.DEBUG)
                    System.out.println("Game is over, but no team has won!");
                //handle game won with a draw
                guiConnector.gameWonNotifier(null, 0, false);
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
            GameLogger.saveLogToFile(Constants.LOG_FILE_NAME);
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
        handleOver();
        Team.setVerticalTeam(new Team(TeamType.VERTICAL));
        Team.setHorizontalTeam(new Team(TeamType.HORIZONTAL));
        Team.setDeactiveTeam(new Team(TeamType.DEACTIVE));
        //kill the this.thread
        if (thread.isAlive())
            thread.interrupt();
    }

    /**
     * Starts the Game
     */
    public void start() {
        //check for faulty setup of the players
        if (Team.getHorizontalTeam().getPlayers().isEmpty() && Team.getVerticalTeam().getPlayers().isEmpty()) {
            return;
        }
        if (Team.getHorizontalTeam().getPlayers().size() != Team.getVerticalTeam().getPlayers().size()) {
            faultyStartup(1);
            return;
        }
        if (players.stream().filter(Player::isActive).toList().size() < Constants.MIN_PLAYER_SIZE || players.stream().filter(Player::isActive).toList().size() % 2 != 0) {
            faultyStartup(2);
            return;
        }
        guiConnector.showHand(currentPlayer instanceof AI, currentPlayer.getPlayerID(), false);
        if (currentPlayer instanceof AI ai) {
            //add the new move to the thread, so the player move will be finished until the ai move starts
            Platform.runLater(ai::makeMove);
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

        //if the turn is over, do nothing
        if (handleOver()) {
            return;
        }
        //otherwise try to draw a token
        try {
            currentPlayer.drawToken();
        } catch (NoTokenException e) {
            if (CrossWise.DEBUG)
                System.out.println("No more tokens left in the Pile!");
        }
        try {
            if (CrossWise.UI)
                Thread.sleep(CrossWise.DELAY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        //Let the next player do their turn
        nextPlayer();
    }


    public void startGame() {
        thread.start();
    }

    public Integer[] pointsArray() {
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();

        SortedSet<Integer> keys = new TreeSet<>(map.keySet());

        Integer[] pointsArray = new Integer[Constants.GAMEGRID_SIZE * 2];
        int counter = 0;
        for (Integer key : keys) {
            pointsArray[counter] = map.get(key);
            counter++;
        }
        if (CrossWise.DEBUG) {
            System.out.println(map);
            System.out.println(Arrays.toString(pointsArray));
        }
        return pointsArray;

    }

    /**
     * Checks, if the game is over and which team won if it is
     *
     * @param field current playing field
     * @return Map, with the boolean if the game is over and a team, that won
     */
    public Map<Boolean, Team> isGameOver(PlayingField field) {
        Map<Boolean, Team> map = new HashMap<>();
        //if horizontal Team won via a full row
        if (checkRows(field)) {
            Team.getHorizontalTeam().setRowWin(true);
            map.put(true, Team.getHorizontalTeam());
            return map;
        }
        //if the vertical team won via a full collumn
        if (checkColumns(field)) {
            Team.getVerticalTeam().setRowWin(true);
            map.put(true, Team.getVerticalTeam());
            return map;
        }
        //check if there are still tokens missing on the playing field
        for (int i = 0; i < field.getSize(); i++) {
            for (int j = 0; j < field.getSize(); j++) {
                if (field.getFieldMap()[i][j].getToken().tokenType() == TokenType.NONE) {
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


    private boolean checkRows(final PlayingField field) {
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

    private boolean checkColumns(final PlayingField field) {
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

    public void activate() {
        thread.start();
    }

    public void addNewActionTile(Token token) {
        usedSpecialTokens.add(token);
    }

    public void removeTokenDrawPileToken(Token token) {
        tokenDrawPile.remove(token);
    }

    //-------------------------------------------Getter and Setter------------------------------------------------------

    public Thread getThread() {
        return thread;
    }

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
