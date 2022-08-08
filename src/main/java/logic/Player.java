package logic;

import javafx.application.Platform;

import java.util.*;

/**
 * Class for a player of the game Crosswise
 *
 * @author Jacob Klövekorn
 */
@SuppressWarnings("unchecked")
public class Player {
    /**
     * Tokens on the hand of the player
     */
    private final ArrayList<Token> handTokens = new ArrayList<>();
    /**
     * ID of the player
     */
    private final int playerID;
    /**
     * Boolean, if the player is active or inactive
     */
    private final boolean isActive;
    /**
     * Name of the player
     */
    private final String name;
    /**
     * The corresponding team of the player
     */
    private Team team;


    /**
     * Constructor
     *
     * @param playerID Player ID of that player
     * @param isActive Status, if the player is active
     * @param name     Name of the player
     */
    public Player(int playerID, boolean isActive, String name) {
        if (playerID < 0 || playerID > 3) {
            throw new IllegalArgumentException("Player ID must be between 0 and 3");
        }
        this.playerID = playerID;
        this.isActive = isActive;
        this.name = name;
    }


    /**
     * add the player to a team
     */
    public void create() {
        this.team = Team.addPlayerToTeam(this);
    }


    /**
     * Perform a turn for a Symbol-Token
     *
     * @param token    Token to perform the move with
     * @param position Position, where the token should be placed
     * @return true, if everything went correctly, otherwise false
     */
    public boolean normalTokenTurn(final Token token, final Position position) {
        //return false, if the token isn't in the hand of the player
        if (hasNotToken(token)) {
            return false;
        }
        Position field = Game.getGame().getPlayingField().getCorrespondingPlayingField(position);
        //return false, if the field is already occupied with a token
        if (field.getToken().tokenType() != TokenType.NONE) {
            return false;
        }
        handTokens.remove(getCorrespondingToken(token));
        field.setToken(token);
        if (CrossWise.DEBUG)
            System.out.println("player name: " + name + "player: " + Game.getGame().getCurrentPlayer().name);
        var playerIDs = new int[Constants.PLAYER_COUNT];
        for (int i = 0; i < Constants.PLAYER_COUNT; i++) {
            playerIDs[i] = Game.getGame().getPlayers().get(i).getPlayerID();
        }
        var playerHands = new TokenType[Constants.PLAYER_COUNT][Constants.HAND_SIZE];
        for (int i = 0; i < Constants.PLAYER_COUNT; i++) {
            for (int j = 0; j < Game.getGame().getPlayers().get(i).getHandTokens().size(); j++) {
                playerHands[i][j] = Game.getGame().getPlayers().get(i).getHandTokens().get(j).tokenType();
            }
        }
        Game.getGame().getGUIConnector().performMoveUIUpdate(playerIDs, playerHands,
                Game.getGame().getPlayingField().convertToTokenTypeArray(), Game.getGame().pointsArray());

        if (this instanceof AI) {
            Game.getGame().getGUIConnector().placerAnimationFrame(field.getX(), field.getY(), field.getToken().tokenType());
            GameLogger.logMove(this, token, position);
            Platform.runLater(() -> {
                /*
                try {
                    Thread.sleep(Constants.AI_TURN_TIME);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                */
            });
        }

        return true;
    }

    /**
     * Checks if the token is in the hand of the player
     *
     * @param token Token to be checked
     * @return true, if it isn't, otherwise false
     */
    public boolean hasNotToken(Token token) {
        for (Token t : handTokens) {
            if (t.equals(token)) return false;
        }
        return true;
    }


    public boolean removerTokenTurn(final Token token, final Position position) {
        if (token.tokenType() != TokenType.REMOVER) return false;

        if (hasNotToken(token)) return false;

        Position field = Game.getGame().getPlayingField().getCorrespondingPlayingField(position);
        if (field.getToken().tokenType() == TokenType.NONE) return false;

        handTokens.remove(getCorrespondingToken(token));
        handTokens.add(field.getToken());
        GameLogger.logMoveRemove(this, field);
        field.setToken(new Token(TokenType.NONE));
        Game.getGame().getGUIConnector().removerAmountText();
        var playerIDs = new int[Constants.PLAYER_COUNT];
        for (int i = 0; i < Constants.PLAYER_COUNT; i++) {
            playerIDs[i] = Game.getGame().getPlayers().get(i).getPlayerID();
        }
        var playerHands = new TokenType[Constants.PLAYER_COUNT][Constants.HAND_SIZE];
        for (int i = 0; i < Constants.PLAYER_COUNT; i++) {
            for (int j = 0; j < Game.getGame().getPlayers().get(i).getHandTokens().size(); j++) {
                playerHands[i][j] = Game.getGame().getPlayers().get(i).getHandTokens().get(j).tokenType();
            }
        }
        Game.getGame().getGUIConnector().performMoveUIUpdate(playerIDs, playerHands,
                Game.getGame().getPlayingField().convertToTokenTypeArray(), Game.getGame().pointsArray());

        if (this instanceof AI) {
            Game.getGame().getGUIConnector().removerAnimationFrame(field.getX(), field.getY());
            Game.getGame().getGUIConnector().updateSpecialTokenIcons(TokenType.REMOVER);
        }
        return true;

    }

    public Player copy() {
        //create a copy of the player instance
        Player player = new Player(this.playerID, this.isActive, this.name);
        //copy the handTokens
        player.handTokens.addAll(this.handTokens);
        //copy the team
        player.team = this.team;
        return player;
    }


    public boolean moverTokenTurn(final Token token, Position start, Position end) {
        if (token.tokenType() != TokenType.MOVER) return false;
        if (hasNotToken(token)) return false;
        Position fieldStart = Game.getGame().getPlayingField().getCorrespondingPlayingField(start);
        Position fieldEnd = Game.getGame().getPlayingField().getCorrespondingPlayingField(end);

        if (fieldStart.getToken().tokenType() == TokenType.NONE) return false;
        if (fieldEnd.getToken().tokenType() != TokenType.NONE) return false;

        handTokens.remove(getCorrespondingToken(token));
        GameLogger.logMoveMove(this, fieldStart, fieldEnd);
        fieldEnd.setToken(fieldStart.getToken());
        fieldStart.setToken(new Token(TokenType.NONE));
        var playerIDs = new int[Constants.PLAYER_COUNT];
        for (int i = 0; i < Constants.PLAYER_COUNT; i++) {
            playerIDs[i] = Game.getGame().getPlayers().get(i).getPlayerID();
        }
        var playerHands = new TokenType[Constants.PLAYER_COUNT][Constants.HAND_SIZE];
        for (int i = 0; i < Constants.PLAYER_COUNT; i++) {
            for (int j = 0; j < Game.getGame().getPlayers().get(i).getHandTokens().size(); j++) {
                playerHands[i][j] = Game.getGame().getPlayers().get(i).getHandTokens().get(j).tokenType();
            }
        }
        Game.getGame().getGUIConnector().performMoveUIUpdate(playerIDs, playerHands,
                Game.getGame().getPlayingField().convertToTokenTypeArray(), Game.getGame().pointsArray());

        if (this instanceof AI) {
            Game.getGame().getGUIConnector().removerAnimationFrame(fieldStart.getX(), fieldStart.getY());
            Game.getGame().getGUIConnector().placerAnimationFrame(fieldEnd.getX(), fieldEnd.getY(), fieldEnd.getToken().tokenType());
            Game.getGame().getGUIConnector().updateSpecialTokenIcons(TokenType.MOVER);

        }
        Game.getGame().getGUIConnector().moverAmountText();

        return true;
    }

    public boolean swapperTokenTurn(final Token token, final Position first, final Position second) {
        if (token.tokenType() != TokenType.SWAPPER) return false;
        if (hasNotToken(token)) return false;
        Position fieldFirst = Game.getGame().getPlayingField().getCorrespondingPlayingField(first);
        if (fieldFirst == null) return false;
        Position fieldSecond = Game.getGame().getPlayingField().getCorrespondingPlayingField(second);
        if (fieldSecond == null) return false;
        if (fieldFirst.getToken().tokenType() == TokenType.NONE) return false;
        if (fieldSecond.getToken().tokenType() == TokenType.NONE) return false;
        handTokens.remove(getCorrespondingToken(token));
        GameLogger.logMoveSwapper(this, fieldFirst, fieldSecond);
        Token temp = fieldFirst.getToken();
        fieldFirst.setToken(fieldSecond.getToken());
        fieldSecond.setToken(temp);

        Game.getGame().getGUIConnector().swapperAmountText();
        var playerIDs = new int[Constants.PLAYER_COUNT];
        for (int i = 0; i < Constants.PLAYER_COUNT; i++) {
            playerIDs[i] = Game.getGame().getPlayers().get(i).getPlayerID();
        }
        var playerHands = new TokenType[Constants.PLAYER_COUNT][Constants.HAND_SIZE];
        for (int i = 0; i < Constants.PLAYER_COUNT; i++) {
            for (int j = 0; j < Game.getGame().getPlayers().get(i).getHandTokens().size(); j++) {
                playerHands[i][j] = Game.getGame().getPlayers().get(i).getHandTokens().get(j).tokenType();
            }
        }
        Game.getGame().getGUIConnector().performMoveUIUpdate(playerIDs, playerHands,
                Game.getGame().getPlayingField().convertToTokenTypeArray(), Game.getGame().pointsArray());
        if (this instanceof AI) {
            Game.getGame().getGUIConnector().placerAnimationFrame(fieldFirst.getX(), fieldFirst.getY(), fieldFirst.getToken().tokenType());
            Game.getGame().getGUIConnector().placerAnimationFrame(fieldSecond.getX(), fieldSecond.getY(), fieldSecond.getToken().tokenType());
            Game.getGame().getGUIConnector().updateSpecialTokenIcons(TokenType.SWAPPER);

        }
        return true;
    }

    /**
     * Perform a move for a Replacer-Token
     *
     * @param token              Token to be replaced
     * @param fieldTokenPosition position on field, that will be swapped with hand
     * @param handTokenPosition  position on hand, that will be swapped with field
     * @return true, if everything went correctly, otherwise false
     */
    public boolean replacerTokenTurn(final Token token, final Position fieldTokenPosition, final Position handTokenPosition) {
        if (hasNotToken(token)) return false;
        if (token.tokenType() != TokenType.REPLACER) return false;
        if (hasNotToken(getCorrespondingToken(handTokenPosition))) return false;

        Position replacerField = Game.getGame().getPlayingField().getCorrespondingPlayingField(fieldTokenPosition);
        if (replacerField.getToken().tokenType() == TokenType.NONE) return false;
        Token replacerToken = getCorrespondingToken(token);
        if (replacerToken == null) return false;
        Token handToken = getCorrespondingToken(handTokenPosition);
        if (handToken == null) return false;
        if (handToken.tokenType() == TokenType.NONE) return false;
        GameLogger.logMoveReplacer(this, replacerField, handToken);
        handTokens.remove(replacerToken);
        handTokens.remove(handToken);
        handTokens.add(replacerField.getToken());
        replacerField.setToken(handToken);
        Game.getGame().getGUIConnector().replacerAmountText();

        var playerIDs = new int[Constants.PLAYER_COUNT];
        for (int i = 0; i < Constants.PLAYER_COUNT; i++) {
            playerIDs[i] = Game.getGame().getPlayers().get(i).getPlayerID();
        }
        var playerHands = new TokenType[Constants.PLAYER_COUNT][Constants.HAND_SIZE];
        for (int i = 0; i < Constants.PLAYER_COUNT; i++) {
            for (int j = 0; j < Game.getGame().getPlayers().get(i).getHandTokens().size(); j++) {
                playerHands[i][j] = Game.getGame().getPlayers().get(i).getHandTokens().get(j).tokenType();
            }
        }
        Game.getGame().getGUIConnector().performMoveUIUpdate(playerIDs, playerHands,
                Game.getGame().getPlayingField().convertToTokenTypeArray(), Game.getGame().pointsArray());

        if (this instanceof AI) {
            Game.getGame().getGUIConnector().placerAnimationFrame(replacerField.getX(), replacerField.getY(), replacerField.getToken().tokenType());
            Game.getGame().getGUIConnector().updateSpecialTokenIcons(TokenType.REPLACER);

        }

        return true;
    }

    /**
     * Get Token on the hand which is similar to the given Token (TokenType)
     *
     * @param token Token, which will be compared
     * @return corresponding Token, null if it didn't exist
     */
    private Token getCorrespondingToken(Token token) {
        for (Token t : handTokens) {
            if (t.equals(token)) return t;
        }
        return null;
    }

    /**
     * Get Token on the hand which is similar to the token on the given handPosition (TokenType)
     *
     * @param position Position on the hand of the player
     * @return corresponding Token, null it wasn't a hand position
     */
    private Token getCorrespondingToken(Position position) {
        if (!position.isHand()) return null;
        return handTokens.get(position.getHandPosition());
    }

    public Token getCorrespondingToken(String tokenName) {
        for (Token t : handTokens) {
            if (t.tokenType().name().equals(tokenName)) {
                return t;
            }
        }
        return null;
    }

    public Token getCorrespondingToken(TokenType type) {
        for (Token t : handTokens) {
            if (t.tokenType() == type) {
                return t;
            }
        }
        return null;
    }

    /**
     * Draw a token out of the drawPile of the game
     *
     * @throws NoTokenException If no more tokens are left
     */
    public void drawToken() throws NoTokenException {
        //Test method to slow down the drawing of Tokens
        try {
            if (CrossWise.UI) Thread.sleep(CrossWise.DELAY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        //If no tokens are left in the pile, throw NoTokensException
        if (Game.getGame().getTokenDrawPile().isEmpty()) {
            var handTokenTypeArray = new TokenType[handTokens.size()];
            for (int i = 0; i < handTokens.size(); i++) {
                handTokenTypeArray[i] = handTokens.get(i).tokenType();
            }
            Game.getGame().getGUIConnector().updatePlayerHandIcons(playerID, handTokenTypeArray);
            return;
        }

        //return if there is no None token in tokens
        if (handTokens.size() >= Constants.HAND_SIZE && handTokens.get(Constants.HAND_SIZE - 1).tokenType() != TokenType.NONE)
            return;

        Token token = Game.getGame().getTokenDrawPile().get(new Random().nextInt(Game.getGame().getTokenDrawPile().size()));
        Game.getGame().removeTokenDrawPileToken(token);


        //remove all tokens from tokens if the TokenType is None
        handTokens.removeIf(t -> t.tokenType() == TokenType.NONE);

        //Add the token to the hand
        handTokens.add(token);
        //Fill up hand with empty Tokens (at start of the game)
        while (handTokens.size() < Constants.HAND_SIZE) {
            handTokens.add(new Token(TokenType.NONE));
        }

        GameLogger.logDraw(this, token);
        var handTokenTypeArray = new TokenType[Constants.HAND_SIZE];
        for (int i = 0; i < Constants.HAND_SIZE; i++) {
            handTokenTypeArray[i] = handTokens.get(i).tokenType();
        }
        Game.getGame().getGUIConnector().updatePlayerHandIcons(playerID, handTokenTypeArray);

    }

    /**
     * Calculates the amount of a specific token in the player hand
     *
     * @param token Token to search occurrences for
     * @return Amount of occurrences
     */
    public int tokenAmountInHand(TokenType token) {
        //the amount of tokens with the same TokenType in hand
        int amount = 0;
        for (Token t : handTokens) {
            if (t.tokenType() == token) amount++;
        }
        return amount;
    }

    /**
     * Get a Set of Indexes of only Symbol-Tokens, that are on the hand
     *
     * @return HashSet of Indexes of only Symbol-Tokens on hand
     */
    public Set<Integer> getHandSymbolTokenPositions() {
        Token[] handCopy = this.getHandTokens().toArray(new Token[0]);
        HashSet<Integer> returnSet = new HashSet<>();
        for (int i = 0; i < handCopy.length; i++) {
            if (handCopy[i].tokenType().getValue() <= Constants.UNIQUE_SYMBOL_TOKENS) {
                returnSet.add(i);
            }
        }
        return returnSet;
    }

    /**
     * converts the Hand of the player to an Array
     *
     * @return returns the hand of the player as an TokenType Array
     */
    public TokenType[] convertHandToTokenTypeArray() {
        TokenType[] array = new TokenType[Constants.HAND_SIZE];
        for (int index = 0; index < handTokens.size(); index++) {
            array[index] = handTokens.get(index).tokenType();
        }
        return array;
    }

    public List<Token> getHandTokens() {
        return (List<Token>) handTokens.clone();
    }

    public void addTokenToHand(Token token) {
        handTokens.add(token);
    }

    public int getPlayerID() {
        return playerID;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getName() {
        return "" + name;
    }

    /**
     * Creates String representation of the players Hand
     *
     * @return String representation of the players Hand
     */
    public String handRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Token t : handTokens) {
            builder.append(t.tokenType().getValue());
            builder.append(", ");
        }
        if (builder.length() > 2) {
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");
        return builder.toString();
    }

    public Team getTeam() {
        return this.team.clone();
    }


}
