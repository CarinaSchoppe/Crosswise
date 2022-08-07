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
                this.fieldMap[i][j].setToken(new Token(TokenType.NONE));
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
        return fieldMap.clone();
    }

    public void addDataFromJSON(int[][] field) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fieldMap[i][j].setToken(new Token(TokenType.getTokenType(field[i][j])));
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
