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
