/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The Latest changes made by Carina on 7/27/22, 11:21 AM All contents of "PlayingField" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.game;

import de.fhwwedel.pp.util.game.Position;
import de.fhwwedel.pp.util.game.Token;
import de.fhwwedel.pp.util.game.TokenType;


public class PlayingField {

    private final int size;
    private final Position[][] fieldMap;

    public PlayingField(final int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Size of playing field must be at least 2");
        }
        this.size = size;
        this.fieldMap = new Position[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.fieldMap[i][j] = new Position(i, j);
                this.fieldMap[i][j].setToken(new Token(TokenType.None));
                this.fieldMap[i][j].getToken().setPosition(this.fieldMap[i][j]);
            }
        }
    }

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

    public int getSize() {
        return size;
    }

    public Position[][] getFieldMap() {
        return fieldMap;
    }

    public void addDataFromJSON(int[][] field) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fieldMap[i][j].setToken(new Token(TokenType.getTokenType(field[i][j])));
                fieldMap[i][j].getToken().setPosition(fieldMap[i][j]);
            }
        }


    }

    public TokenType[][] convertToTokenTypeArray() {
        //convert the fieldMap to a Token[][]
        TokenType[][] tokenArray = new TokenType[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tokenArray[i][j] = fieldMap[i][j].getToken().getTokenType();
            }
        }
        return tokenArray;
    }

    public Token[][] convertToTokenArray() {
        //convert the fieldMap to a Token[][]
        Token[][] tokenArray = new Token[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
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
                stringBuilder.append(fieldMap[i][j].getToken().getTokenType().getValue());
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
