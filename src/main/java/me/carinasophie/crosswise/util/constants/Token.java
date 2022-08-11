/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "Token" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.util.constants;

/**
 * Class for a token for the Game Crosswise
 *
 * @param tokenType Type of the Token
 * @author Carina Sophie Schoppe
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof Token token)) {
            return false;
        }

        return tokenType() == token.tokenType();
    }

    @Override
    public String toString() {
        return tokenType.name();
    }
}
