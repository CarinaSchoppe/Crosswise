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


    //----------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param tokenType Type of the new Token
     */
    public Token(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public TokenType getTokenType() {
        return tokenType;
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
        result = 31 * result;
        return result;
    }
}
