package logic;

import javafx.application.Platform;

import java.util.*;

/**
 * Class for a game instance of the game Crosswise
 *
 * @author Jacob Klövekorn
 */
@SuppressWarnings("ALL")
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

    private boolean start;

    //----------------------------------------------------------------------------------------------

    /**
     * Constructor
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

    public static void createNewGame(List<Player> players, GUIConnector connector, boolean fileSetup) {
        Game game = new Game(new PlayingField(Constants.GAMEGRID_SIZE), players, connector);
        createStuff(game, fileSetup);
    }

    /**
     * Method to extract code duplication
     */
    private static void createStuff(Game game, boolean fileSetup) {
        if (Game.getGame() != null) {
            Game.getGame().cancel();
        }

        Thread thread = new Thread(() -> {
            while (!game.start) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            game.setup(fileSetup);
            game.start();
        });
        Game.setGame(game, thread);
        if (!fileSetup) {
            Game.game.guiConnector.startGamePopUp();
            thread.start();
        }
    }


    public static void createNewGame(List<Player> players, GUIConnector connector, boolean fileSetup, PlayingField field) {
        Game game = new Game(field, players, connector);
        createStuff(game, fileSetup);
    }

    /**
     * shows error message while trying to create a game with wrong parameters
     */
    public void faultyStartup(Integer caseID) {
        Platform.runLater(() -> {
            guiConnector.showHand(true, 0, true);
            guiConnector.faultyAlert(caseID);
        });
        game.cancel();
    }

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
        for (TokenType token : TokenType.values()) {
            if (token == TokenType.NONE) continue;
            if (token.isSpecial()) {
                for (int i = 0; i < Constants.AMOUNT_ACTION_TOKENS; i++) { //12 tokens
                    tokenDrawPile.add(new Token(token));
                }
            } else {
                for (int i = 0; i < Constants.AMOUNT_NORMAL_TOKENS; i++) { //42 tokens
                    tokenDrawPile.add(new Token(token));
                }
            }
        }
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
        if (Team.getHorizontalTeam().getPlayers().isEmpty() && Team.getVerticalTeam().getPlayers().isEmpty()) {
            System.out.println("hiereee");
            faultyStartup(0);
            return;
        }

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

        Platform.runLater((() -> {
            guiConnector.showHand(currentPlayer instanceof AI, currentPlayer.getPlayerID(), false);
            guiConnector.changeCurrentPlayerText(currentPlayer.getName());
        }));

        if (CrossWise.DEBUG) {
            System.out.println("Current player is: " + currentPlayer.getName() + " with ID: " + currentPlayer.getPlayerID());
        }
        //if the player is an AI player, let the AI make their move, otherwise notify the next player
        if (currentPlayer instanceof AI ai) {
            Platform.runLater(ai::makeMove);
        } else {
            guiConnector.notifyTurn(currentPlayer.getName(), currentPlayer.getPlayerID());
        }

    }

    public void playerSymbolTokenMove(String tokenString, Integer x, Integer y) {
        currentPlayer.normalTokenTurn(currentPlayer.getCorrespondingToken(tokenString), new Position(x, y));
        turnDone();
    }

    public void playerRemoverTokenMove(Integer x, Integer y) {
        currentPlayer.removerTokenTurn(currentPlayer.getCorrespondingToken("REMOVER"), new Position(x, y));
        turnDone();
    }

    public void playerMoverTokenMove(Integer fromX, Integer fromY, Integer toX, Integer toY) {
        currentPlayer.moverTokenTurn(currentPlayer.getCorrespondingToken("MOVER"), new Position(fromX, fromY), new Position(toX, toY));
        turnDone();
    }

    public void playerSwapperTokenMove(Integer fromX, Integer fromY, Integer toX, Integer toY) {
        currentPlayer.swapperTokenTurn(currentPlayer.getCorrespondingToken("SWAPPER"), new Position(fromX, fromY), new Position(toX, toY));
        turnDone();
    }

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
            System.out.println("No players left!");
            GameLogger.saveLogToFile(Constants.LOG_FILE_NAME);
            return true;
        } else if (over.containsKey(true)) {
            Team team = over.get(true);
            if (team == null) {
                System.out.println("Game is over, but no team has won!");
                guiConnector.gameWonNotifier(null, 0, false);
            } else {
                guiConnector.gameWonNotifier(team.getTeamType(), team.getPoints(), team.isRowWin());
                if (CrossWise.DEBUG)
                    System.out.println(Team.getVerticalTeam().getPoints() + " " + Team.getHorizontalTeam().getPoints());
                System.out.println("Game is over, team " + team.getTeamType().getTeamName() + " has won!");
            }
            System.out.println(System.currentTimeMillis() - CrossWise.time);
            GameLogger.saveLogToFile(Constants.LOG_FILE_NAME);

            return true;
        }
        return false;
    }

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
            ai.makeMove();
        } else {
            guiConnector.notifyTurn(currentPlayer.getName(), currentPlayer.getPlayerID());
        }
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

    public void addNewActionTile(Token token) {
        usedSpecialTokens.add(token);
    }

    public void removeTokenDrawPileToken(Token token) {
        tokenDrawPile.remove(token);
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

    public GUIConnector getGUIConnector() {
        return guiConnector;
    }

    public void startGame() {
        synchronized (this) {
            start = true;
        }
    }

    public Integer[] pointsArray() {
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        System.out.println(map);
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
                if (field.getFieldMap()[i][j].getToken().getTokenType() == TokenType.NONE) {
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


    @SuppressWarnings("DuplicatedCode")
    private boolean checkRows(final PlayingField field) {
        TokenType current = null;
        for (int i = 0; i < field.getSize(); i++) { //get horizontal
            boolean equal = true;
            for (int j = 0; j < field.getSize(); j++) { // get field on row
                if (current == null) {
                    if (field.getFieldMap()[i][j].getToken().getTokenType() != TokenType.NONE) {
                        current = field.getFieldMap()[i][j].getToken().getTokenType();
                    } else {
                        equal = false;
                        break;
                    }
                } else {
                    if (!(field.getFieldMap()[i][j].getToken().getTokenType() == TokenType.NONE || field.getFieldMap()[i][j].getToken().getTokenType() != current))
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

    @SuppressWarnings("DuplicatedCode")
    private boolean checkColumns(final PlayingField field) {
        TokenType current = null;

        for (int i = 0; i < field.getSize(); i++) {
            boolean equal = true;
            for (int j = 0; j < field.getSize(); j++) {
                if (current == null) {
                    if (field.getFieldMap()[j][i].getToken().getTokenType() != TokenType.NONE) {
                        current = field.getFieldMap()[j][i].getToken().getTokenType();
                    } else {
                        equal = false;
                        break;
                    }
                } else {
                    if (!(field.getFieldMap()[j][i].getToken().getTokenType() == TokenType.NONE || field.getFieldMap()[j][i].getToken().getTokenType() != current))
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


    public Thread getThread() {
        return thread;
    }

    public void activate() {
        start = true;
    }
}
