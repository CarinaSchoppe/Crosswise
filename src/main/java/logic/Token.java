package logic;

import logic.util.TokenType;

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
    public String toString() {
        return tokenType.name();
    }
}
