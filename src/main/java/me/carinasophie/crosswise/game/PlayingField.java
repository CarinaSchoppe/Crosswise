/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "PlayingField" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.game;


import lombok.Getter;
import me.carinasophie.crosswise.util.Position;
import me.carinasophie.crosswise.util.constants.Constants;
import me.carinasophie.crosswise.util.constants.Token;
import me.carinasophie.crosswise.util.constants.TokenType;

/**
 * Class of the playing field of the game Crosswise
 */
@Getter
public class PlayingField {
    /**
     * Size of the game field
     */
    private final int size;
    /**
     * double array of positions
     */
    private final Position[][] fieldMap;

    /**
     * Constructor
     *
     * @param size Size of the playing field
     */
    public PlayingField(final int size) {
        //check, if the size of the new playing field is at least 2 or bigger,
        // otherwise throw an exception
        if (size < Constants.MIN_GAMEGRID_SIZE) {
            throw new IllegalArgumentException("Size of playing field must be at least 2");
        }
        this.size = size;
        this.fieldMap = new Position[size][size];
        //Puts empty token on each field of the fieldMap
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.fieldMap[i][j] = new Position(i, j);
                this.fieldMap[i][j].setToken(new Token(TokenType.NONE));
            }
        }
    }

    /**
     * Get a specific position of the game field
     *
     * @param position Position which corresponds to a position on the game field
     * @return position of the game field, if it doesn't exist, return null
     */
    public Position getCorrespondingPlayingField(final Position position) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (fieldMap[i][j].equals(position)) {
                    return fieldMap[i][j];
                }
            }
        }
        return null;
    }



    /**
     * adds all tokens to grid from a given int array
     *
     * @param field double int array from the json output
     */
    public void addDataFromJSON(int[][] field) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //place new token on the field
                fieldMap[i][j].setToken(new Token(TokenType.getTokenType(field[i][j])));
            }
        }
    }

    /**
     * Concert current playing field to array of TokenTypes
     *
     * @return return array of tokenTypes
     */
    public TokenType[][] convertToTokenTypeArray() {
        //convert the fieldMap to a Token[][]
        TokenType[][] tokenArray = new TokenType[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //get token type from fieldMap token and add it to the new array
                tokenArray[i][j] = fieldMap[i][j].getToken().tokenType();
            }
        }
        return tokenArray;
    }

    /**
     * Convert current playing field to Token array
     *
     * @return return array of tokens
     */
    public Token[][] convertToTokenArray() {
        //convert the fieldMap to a Token[][]
        Token[][] tokenArray = new Token[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //get token from fieldMap and add it to the new array
                tokenArray[i][j] = fieldMap[i][j].getToken();
            }
        }
        return tokenArray;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FieldMap:\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                stringBuilder.append(fieldMap[i][j].getToken().tokenType().getValue());
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
