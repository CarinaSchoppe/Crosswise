package logic;

/**
 * Class for a token for the Game Crosswise
 *
 * @param tokenType Type of the Token
 * @author Jacob Klövekorn
 */
public record Token(TokenType tokenType) {
    //----------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param tokenType Type of the new Token
     */
    public Token {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token token)) return false;

        return tokenType() == token.tokenType();
    }

    @Override
    public int hashCode() {
        int result = getTokenType() != null ? getTokenType().hashCode() : 0;
        result = 31 * result;
        return result;
    }
}