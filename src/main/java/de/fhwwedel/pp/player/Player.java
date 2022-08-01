/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The Latest changes made by Carina on 7/27/22, 11:21 AM All contents of "Player" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.player;

import de.fhwwedel.pp.CrossWise;
import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.gui.GameWindow;
import de.fhwwedel.pp.gui.GameWindowHandler;
import de.fhwwedel.pp.util.exceptions.NoTokenException;
import de.fhwwedel.pp.util.game.Position;
import de.fhwwedel.pp.util.game.Team;
import de.fhwwedel.pp.util.game.Token;
import de.fhwwedel.pp.util.game.TokenType;
import de.fhwwedel.pp.util.special.Action;
import de.fhwwedel.pp.util.special.Constants;
import de.fhwwedel.pp.util.special.GameLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Class for a player of the game Crosswise
 *
 * @author Jacob Klövekorn
 */
public class Player {
    /**
     * Tokens on the hand of the player
     */
    private final ArrayList<Token> tokens = new ArrayList<>();
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
     * @param name Name of the player
     */
    public Player(int playerID, boolean isActive, String name) {
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
     * @param token Token to perform the move with
     * @param position Position, where the token should be placed
     * @return true, if everything went correctly, otherwise false
     */
    public boolean normalTokenTurn(final Token token, final Position position) {
        //return false, if the token isn't in the hand of the player
        if (hasToken(token)) {
            return false;
        }
        Position field = Game.getGame().getPlayingField().getCorrespondingPlayingField(position);
        //return false, if the field is already occupied with a token
        if (field.getToken().getTokenType() != TokenType.NONE) {
            return false;
        }
        tokens.remove(getCorrespondingToken(token));
        field.setToken(token);
        GameLogger.logMove(this, token, position, Action.PLACE);
        return true;
    }

    /**
     * Checks if the token is in the hand of the player
     *
     * @param token Token to be checked
     * @return true, if it isn't, otherwise false
     */
    private boolean hasToken(Token token) {
        for (var t : tokens) {
            if (t.equals(token))
                return false;
        }
        return true;
    }



    public boolean removerTokenTurn(final Token token, final Position position) {
        if (token.getTokenType() != TokenType.REMOVER)
            return false;

        if (hasToken(token))
            return false;

        var field = Game.getGame().getPlayingField().getCorrespondingPlayingField(position);
        if (field.getToken().getTokenType() == TokenType.NONE)
            return false;

        tokens.remove(getCorrespondingToken(token));
        tokens.add(field.getToken());
        field.setToken(new Token(TokenType.NONE));
        field.getToken().setPosition(field);
        if (GameWindow.getGameWindow() != null)
            GameWindow.getGameWindow().removerAmountText();
        GameLogger.logMove(this, token, field, Action.REMOVE);

        return true;

    }

    public boolean moverTokenTurn(final Token token, Position start, Position end) {
        if (token.getTokenType() != TokenType.MOVER)
            return false;
        if (hasToken(token))
            return false;
        var fieldStart = Game.getGame().getPlayingField().getCorrespondingPlayingField(start);
        var fieldEnd = Game.getGame().getPlayingField().getCorrespondingPlayingField(end);

        if (fieldStart.getToken().getTokenType() == TokenType.NONE)
            return false;
        if (fieldEnd.getToken().getTokenType() != TokenType.NONE)
            return false;

        tokens.remove(getCorrespondingToken(token));

        fieldEnd.setToken(fieldStart.getToken());
        fieldStart.setToken(new Token(TokenType.NONE));

        if (GameWindow.getGameWindow() != null)
            GameWindow.getGameWindow().moverAmountText();
        GameLogger.logMove(this, token, fieldStart, Action.REMOVE);
        GameLogger.logMove(this, token, fieldEnd, Action.PLACE);

        return true;
    }

    public boolean swapperTokenTurn(final Token token, final Position first,
                                    final Position second) {
        if (token.getTokenType() != TokenType.SWAPPER)
            return false;
        if (hasToken(token))
            return false;
        var fieldFirst = Game.getGame().getPlayingField().getCorrespondingPlayingField(first);
        var fieldSecond = Game.getGame().getPlayingField().getCorrespondingPlayingField(second);
        if (fieldFirst.getToken().getTokenType() == TokenType.NONE)
            return false;
        if (fieldSecond.getToken().getTokenType() == TokenType.NONE)
            return false;
        tokens.remove(getCorrespondingToken(token));
        var temp = fieldFirst.getToken();
        fieldFirst.setToken(fieldSecond.getToken());
        fieldSecond.setToken(temp);
        if (GameWindow.getGameWindow() != null)
            GameWindow.getGameWindow().swapperAmountText();
        GameLogger.logMove(this, fieldSecond.getToken(), fieldFirst, Action.PLACE);
        GameLogger.logMove(this, fieldFirst.getToken(), fieldSecond, Action.PLACE);
        return true;
    }

    /**
     * Perform a move for a Replacer-Token
     *
     * @param token Token to be replaced
     * @param fieldTokenPosition position on field, that will be swapped with hand
     * @param handTokenPosition position on hand, that will be swapped with field
     * @return true, if everything went correctly, otherwise false
     */
    public boolean replacerTokenTurn(final Token token, final Position fieldTokenPosition,
            final Position handTokenPosition) {
        if (hasToken(token))
            return false;
        if (token.getTokenType() != TokenType.REPLACER)
            return false;
        if (hasToken(getCorrespondingToken(handTokenPosition)))
            return false;

        var field =
                Game.getGame().getPlayingField().getCorrespondingPlayingField(fieldTokenPosition);
        if (field.getToken().getTokenType() == TokenType.NONE)
            return false;
        var fieldToken = getCorrespondingToken(token);
        var handToken = getCorrespondingToken(handTokenPosition);
        tokens.remove(fieldToken);
        tokens.remove(handToken);
        field.setToken(handToken);
        if (GameWindow.getGameWindow() != null)
            GameWindow.getGameWindow().replacerAmountText();
        tokens.add(field.getToken());
        assert fieldToken != null;
        GameLogger.logMove(this, fieldToken, field, Action.REMOVE);
        assert handToken != null;
        GameLogger.logMove(this, handToken, field, Action.PLACE);
        if (GameWindowHandler.getGameWindowHandler() != null) {
            GameWindowHandler.getGameWindowHandler().updatePlayerHandIcons(this);
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
        for (var t : tokens) {
            if (t.equals(token))
                return t;
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
        if (!position.isHand())
            return null;
        return tokens.get(position.getHandPosition());
    }

    /**
     * Draw a token out of the drawPile of the game
     *
     * @throws NoTokenException If no more tokens are left
     */
    public void drawToken() throws NoTokenException {
        //Test method to slow down the drawing of Tokens
        try {
            if (CrossWise.slow)
                Thread.sleep(CrossWise.delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //If no tokens are left in the pile, throw NoTokensException
        if (Game.getGame().getTokenDrawPile().isEmpty())
            throw new NoTokenException("No more tokens left in the Pile!");

        //return if there is no None token in tokens
        if (tokens.size() >= Constants.HAND_SIZE && tokens.get(Constants.HAND_SIZE - 1).getTokenType() != TokenType.NONE)
            return;

        var token = Game.getGame().getTokenDrawPile()
                .get(new Random().nextInt(Game.getGame().getTokenDrawPile().size()));
        Game.getGame().getTokenDrawPile().remove(token);


        //remove all tokens from tokens if the TokenType is None
        for (var t : new ArrayList<>(tokens)) {
            if (t.getTokenType() == TokenType.NONE)
                tokens.remove(t);
        }
        //Add the token to the hand
        tokens.add(token);
        //Fill up hand with empty Tokens (at start of the game)
        while (tokens.size() < Constants.HAND_SIZE) {
            tokens.add(new Token(TokenType.NONE));
        }

        GameLogger.logDraw(this, token);

        if (GameWindowHandler.getGameWindowHandler() != null) {
            GameWindowHandler.getGameWindowHandler().updatePlayerHandIcons(this);
        }

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
        for (var t : tokens) {
            if (t.getTokenType() == token)
                amount++;
        }
        return amount;
    }

    /**
     * Get a Set of Indexes of only Symbol-Tokens, that are on the hand
     *
     * @return HashSet of Indexes of only Symbol-Tokens on hand
     */
    public HashSet<Integer> getHandSymbolTokenPositions() {
        Token[] handCopy = this.getTokens().toArray(new Token[0]);
        HashSet<Integer> returnSet = new HashSet<>();
        for (int i = 0; i < handCopy.length; i++) {
            if (handCopy[i].getTokenType().getValue() <= Constants.UNIQUE_SYMBOL_TOKENS) {
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
        for (var index = 0; index < tokens.size(); index++) {
            array[index] = tokens.get(index).getTokenType();
        }
        return array;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public int getPlayerID() {
        return playerID;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    /**
     * Creates String representation of the players Hand
     *
     * @return String representation of the players Hand
     */
    public String handRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (var t : tokens) {
            builder.append(t.getTokenType().getValue());
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
        return this.team;
    }
}
