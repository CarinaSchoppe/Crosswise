/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 12:11 PM by Carina The Latest changes made by Carina on 7/27/22, 11:22 AM All contents of "Token" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.util.game;

/**
 * Class for a token for the Game Crosswise
 *
 * @author Jacob Klövekorn
 */
public class Token {
    /**
     * Type of the Token
     */
    private final TokenType tokenType;
    /**
     * Position of the Token, if this is set its a Token on the field of the game
     */
    private Position position;

    //----------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param tokenType Type of the new Token
     */
    public Token(TokenType tokenType) {
        this.tokenType = tokenType;
        position = null;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token token)) return false;

        return getTokenType() == token.getTokenType();
    }

    @Override
    public int hashCode() {
        int result = getTokenType() != null ? getTokenType().hashCode() : 0;
        result = 31 * result + (getPosition() != null ? getPosition().hashCode() : 0);
        return result;
    }
}
