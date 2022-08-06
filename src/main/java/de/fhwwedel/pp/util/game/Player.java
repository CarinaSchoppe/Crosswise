/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The Latest changes made by Carina on 7/27/22, 11:21 AM All contents of "Player" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.util.game;

import de.fhwwedel.pp.CrossWise;
import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.util.exceptions.NoTokenException;
import de.fhwwedel.pp.util.special.Constants;
import de.fhwwedel.pp.util.special.GameLogger;

import java.util.*;

/**
 * Class for a player of the game Crosswise
 *
 * @author Jacob Klövekorn
 */
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
        if (hasToken(token)) {
            return false;
        }
        Position field = Game.getGame().getPlayingField().getCorrespondingPlayingField(position);
        //return false, if the field is already occupied with a token
        if (field.getToken().getTokenType() != TokenType.NONE) {
            return false;
        }
        handTokens.remove(getCorrespondingToken(token));
        field.setToken(token);
        field.getToken().setPosition(field);
        Game.getGame().getGUIConnector().performMoveUIUpdate(Game.getGame().getPlayers(),
                Game.getGame().getPlayingField().convertToTokenTypeArray(), Game.getGame().pointsArray());
        if (this instanceof AI)
            Game.getGame().getGUIConnector().placerAnimationFrame(field.getX(), field.getY(), field.getToken().getTokenType());
        GameLogger.logMove(this, token, position);

        return true;
    }

    /**
     * Checks if the token is in the hand of the player
     *
     * @param token Token to be checked
     * @return true, if it isn't, otherwise false
     */
    private boolean hasToken(Token token) {
        for (var t : handTokens) {
            if (t.equals(token)) return false;
        }
        return true;
    }


    public boolean removerTokenTurn(final Token token, final Position position) {
        if (token.getTokenType() != TokenType.REMOVER) return false;

        if (hasToken(token)) return false;

        var field = Game.getGame().getPlayingField().getCorrespondingPlayingField(position);
        if (field.getToken().getTokenType() == TokenType.NONE) return false;

        handTokens.remove(getCorrespondingToken(token));
        handTokens.add(field.getToken());
        GameLogger.logMoveRemove(this, field);
        field.setToken(new Token(TokenType.NONE));
        field.getToken().setPosition(field);
        Game.getGame().getGUIConnector().removerAmountText();
        Game.getGame().getGUIConnector().performMoveUIUpdate(Game.getGame().getPlayers(),
                Game.getGame().getPlayingField().convertToTokenTypeArray(), Game.getGame().pointsArray());

        if (this instanceof AI)
            Game.getGame().getGUIConnector().removerAnimationFrame(field.getX(), field.getY());

        return true;

    }

    public boolean moverTokenTurn(final Token token, Position start, Position end) {
        if (token.getTokenType() != TokenType.MOVER) return false;
        if (hasToken(token)) return false;
        var fieldStart = Game.getGame().getPlayingField().getCorrespondingPlayingField(start);
        var fieldEnd = Game.getGame().getPlayingField().getCorrespondingPlayingField(end);

        if (fieldStart.getToken().getTokenType() == TokenType.NONE) return false;
        if (fieldEnd.getToken().getTokenType() != TokenType.NONE) return false;

        handTokens.remove(getCorrespondingToken(token));
        GameLogger.logMoveMove(this, fieldStart, fieldEnd);
        fieldEnd.setToken(fieldStart.getToken());
        fieldEnd.getToken().setPosition(fieldEnd);
        fieldStart.setToken(new Token(TokenType.NONE));
        fieldStart.getToken().setPosition(fieldStart);
        Game.getGame().getGUIConnector().performMoveUIUpdate(Game.getGame().getPlayers(),
                Game.getGame().getPlayingField().convertToTokenTypeArray(), Game.getGame().pointsArray());

        if (this instanceof AI) {
            Game.getGame().getGUIConnector().removerAnimationFrame(fieldStart.getX(), fieldStart.getY());
            Game.getGame().getGUIConnector().placerAnimationFrame(fieldEnd.getX(), fieldEnd.getY(), fieldEnd.getToken().getTokenType());
        }
        Game.getGame().getGUIConnector().moverAmountText();

        return true;
    }

    public boolean swapperTokenTurn(final Token token, final Position first, final Position second) {
        if (token.getTokenType() != TokenType.SWAPPER) return false;
        if (hasToken(token)) return false;
        var fieldFirst = Game.getGame().getPlayingField().getCorrespondingPlayingField(first);
        if (fieldFirst == null) return false;
        var fieldSecond = Game.getGame().getPlayingField().getCorrespondingPlayingField(second);
        if (fieldSecond == null) return false;
        if (fieldFirst.getToken().getTokenType() == TokenType.NONE) return false;
        if (fieldSecond.getToken().getTokenType() == TokenType.NONE) return false;
        handTokens.remove(getCorrespondingToken(token));
        GameLogger.logMoveSwapper(this, fieldFirst, fieldSecond);
        var temp = fieldFirst.getToken();
        fieldFirst.setToken(fieldSecond.getToken());
        fieldFirst.getToken().setPosition(fieldFirst);
        fieldSecond.setToken(temp);
        fieldSecond.getToken().setPosition(fieldSecond);

        Game.getGame().getGUIConnector().swapperAmountText();
        Game.getGame().getGUIConnector().performMoveUIUpdate(Game.getGame().getPlayers(),
                Game.getGame().getPlayingField().convertToTokenTypeArray(), Game.getGame().pointsArray());
        if (this instanceof AI) {
            Game.getGame().getGUIConnector().placerAnimationFrame(fieldFirst.getX(), fieldFirst.getY(), fieldFirst.getToken().getTokenType());
            Game.getGame().getGUIConnector().placerAnimationFrame(fieldSecond.getX(), fieldSecond.getY(), fieldSecond.getToken().getTokenType());
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
        if (hasToken(token)) return false;
        if (token.getTokenType() != TokenType.REPLACER) return false;
        if (hasToken(getCorrespondingToken(handTokenPosition))) return false;

        var replacerField = Game.getGame().getPlayingField().getCorrespondingPlayingField(fieldTokenPosition);
        if (replacerField.getToken().getTokenType() == TokenType.NONE) return false;
        var replacerToken = getCorrespondingToken(token);
        if (replacerToken == null) return false;
        var handToken = getCorrespondingToken(handTokenPosition);
        if (handToken == null) return false;
        if (handToken.getTokenType() == TokenType.NONE) return false;
        GameLogger.logMoveReplacer(this, replacerField, handToken);
        handTokens.remove(replacerToken);
        handTokens.remove(handToken);
        handTokens.add(replacerField.getToken());
        replacerField.setToken(handToken);
        replacerField.getToken().setPosition(replacerField);
        Game.getGame().getGUIConnector().replacerAmountText();

        Game.getGame().getGUIConnector().performMoveUIUpdate(Game.getGame().getPlayers(),
                Game.getGame().getPlayingField().convertToTokenTypeArray(), Game.getGame().pointsArray());

        if (this instanceof AI)
            Game.getGame().getGUIConnector().placerAnimationFrame(replacerField.getX(), replacerField.getY(), replacerField.getToken().getTokenType());

        return true;
    }

    /**
     * Get Token on the hand which is similar to the given Token (TokenType)
     *
     * @param token Token, which will be compared
     * @return corresponding Token, null if it didn't exist
     */
    private Token getCorrespondingToken(Token token) {
        for (var t : handTokens) {
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
        for (var t : handTokens) {
            if (t.getTokenType().name().equals(tokenName)) {
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
        if (Game.getGame().getTokenDrawPile().isEmpty()) throw new NoTokenException("No more tokens left in the Pile!");

        //return if there is no None token in tokens
        if (handTokens.size() >= Constants.HAND_SIZE && handTokens.get(Constants.HAND_SIZE - 1).getTokenType() != TokenType.NONE)
            return;

        var token = Game.getGame().getTokenDrawPile().get(new Random().nextInt(Game.getGame().getTokenDrawPile().size()));
        Game.getGame().getTokenDrawPile().remove(token);


        //remove all tokens from tokens if the TokenType is None
        handTokens.removeIf(t -> t.getTokenType() == TokenType.NONE);

        //Add the token to the hand
        handTokens.add(token);
        //Fill up hand with empty Tokens (at start of the game)
        while (handTokens.size() < Constants.HAND_SIZE) {
            handTokens.add(new Token(TokenType.NONE));
        }

        GameLogger.logDraw(this, token);

        Game.getGame().getGUIConnector().updatePlayerHandIcons(playerID, handTokens);


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
        for (var t : handTokens) {
            if (t.getTokenType() == token) amount++;
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
        for (var index = 0; index < handTokens.size(); index++) {
            array[index] = handTokens.get(index).getTokenType();
        }
        return array;
    }

    public List<Token> getHandTokens() {
        return handTokens;
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
        for (var t : handTokens) {
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
