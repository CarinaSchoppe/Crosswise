/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The Latest changes made by Carina on 7/26/22, 4:39 PM All contents of "AI" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.ai;

import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.exceptions.NoMovePossibleException;
import de.fhwwedel.pp.util.exceptions.NoTokenException;
import de.fhwwedel.pp.util.game.Position;
import de.fhwwedel.pp.util.game.Team;
import de.fhwwedel.pp.util.game.Token;
import de.fhwwedel.pp.util.game.TokenType;
import de.fhwwedel.pp.util.special.Constants;

import java.util.*;

@SuppressWarnings("DuplicatedCode")
public class AI extends Player {
    public AI(int playerID, boolean active, String name) {
        super(playerID, active, name);
    }


    private void performAnimation() {

        //TODO: here fill out
    }

    public void makeMove() {

        /*
        Restrictions:
        - Immer zug machen wenn zug gewinn machbar
        - Kein Zug machen wenn Gegner zug gewinn möglich
        - Zug machen der am meisten Punkte bringt

        - lieber normaler zug als special
          - wenn 2 züge gleich dann nimm den mit mehr tokens auf hand
            - gleich? nimm den wo weniger auf dem feld liegt
              - wenn gleich? nimm stein mit der geringsten zahl
                - wenn gleich? nimm feld ganz oben links dann nach rechts dann unten
         */

        try {
            var move = calculateAIMove(this);
            switch (move.getToken().getTokenType().getValue()) {
                case 1, 2, 3, 4, 5, 6 -> normalTokenTurn(move.getToken(), move.getPrimaryMovePosition());
                case 7 -> removerTokenTurn(move.getToken(), move.getPrimaryMovePosition());
                case 8 -> moverTokenTurn(move.getToken(), move.getPrimaryMovePosition(), move.getSecondaryMovePosition());
                case 9 -> swapperTokenTurn(move.getToken(), move.getPrimaryMovePosition(), move.getSecondaryMovePosition());
                case 10 -> replacerTokenTurn(move.getToken(), move.getPrimaryMovePosition(), move.getSecondaryMovePosition());
            }

            performAnimation();
            Game.getGame().turnDone();
        } catch (NoTokenException e) {
            //TODO: needed?
        }
    }


    public TokenMove calculateAIMove(Player player) throws NoTokenException {
        ArrayList<TokenMove> bestMovePerToken = new ArrayList<>();
        Token[] playerHand = player.getTokens().toArray(new Token[0]);

        for (Token token : playerHand) {
            System.out.println("current Token: " + token.getTokenType().getValue());
            bestMovePerToken.add(calculateBestTokenMove(token, player));
        }
        int counter = 0;

        Integer bestToken = null;
        for (int i = 0; i < playerHand.length; i++) {
            if (bestMovePerToken.get(i) != null) {
                if (bestToken == null) {
                    bestToken = i;
                } else if (isBetterMove(bestMovePerToken.get(i), bestMovePerToken.get(bestToken), player)) {
                    bestToken = i;
                }
                }
            }
        if (bestToken == null) {
            throw new NoTokenException("No token more on hand!");
        } else {
            return bestMovePerToken.get(bestToken);
        }
    }

    public boolean isBetterMove(TokenMove newMove, TokenMove currentBestMove, Player player) {
        //Vergleich auf Siegchance

        if (newMove.isGameWinning()) {
            return true;
        } else if (currentBestMove.isGameWinning()) {
            return false;
        }


        //Vergleich auf Verhinderung einer Niederlage
        if (newMove.isPreventingLoss()) {
            return true;
        } else if (currentBestMove.isPreventingLoss()) {
            return false;
        }

        //Vergleich auf Änderung der Punkte
        int difference = newMove.getRelativeChange() - currentBestMove.getRelativeChange();
        if (player.getTeam() == Team.VERTICAL) {
            if (difference < 0) {
                return false;
            }
            if (difference > 0) {
                return true;
            }
        } else {
            if (difference > 0) {
                return false;
            }
            if (difference < 0) {
                return true;
            }
        }
        //Vergleich auf Aktionsstein gegenüber Symbolstein
        int compareTokenValue = compareTypeOfToken(newMove.getToken(), currentBestMove.getToken());
        if (compareTokenValue == 1) {
            return true;
        }
        if (compareTokenValue == -1) {
            return false;
        }
        //Vergleich auf häufigstes Hand Vorkommen
        int tokenAmountInHandDifference = player.tokenAmountInHand(newMove.getToken()) -
                player.tokenAmountInHand(currentBestMove.getToken());
        if (tokenAmountInHandDifference > 0) {
            return true;
        }
        if (tokenAmountInHandDifference < 0) {
            return false;
        }

        //Vergleich auf Anzahl des Tokens auf Brett (nur bei Symbolsteinen)
        if (newMove.getToken().getTokenType().getValue() <= Constants.UNIQUE_SYMBOL_TOKENS) {
            int differenceOccurrencesOnBoard =
                    countNumberOfTokenOnGrid(newMove.getToken()) - countNumberOfTokenOnGrid(
                            currentBestMove.getToken());
            if (differenceOccurrencesOnBoard > 0) {
                return false;
            }
            if (differenceOccurrencesOnBoard < 0) {
                return true;
            }
        }
        //Vergleich auf Ordinalität von Token
        int differenceOrdinalityOfToken = newMove.getToken().getTokenType().getValue() -
                currentBestMove.getToken().getTokenType().getValue();
        if (differenceOrdinalityOfToken > 0) {
            return false;
        }
        if (differenceOrdinalityOfToken < 0) {
            return true;
        }
        //Vergleich auf vertikale Position des Tokens
        int differenceVerticalPosition = newMove.getPrimaryMovePosition().getX() -
                currentBestMove.getPrimaryMovePosition().getX();

        if (differenceVerticalPosition < 0) {
            return true;
        }
        if (differenceVerticalPosition > 0) {
            return false;
        }

        //Vergleich auf horizontale Position des Tokens
        int differenceHorizontalPosition = newMove.getPrimaryMovePosition().getY() -
                currentBestMove.getPrimaryMovePosition().getY();
        if (differenceHorizontalPosition < 0) {
            return true;
        }
        if (differenceHorizontalPosition > 0) {
            return false;
        }
        if (newMove.getToken().getTokenType().getValue() > Constants.UNIQUE_SYMBOL_TOKENS && newMove.getToken().
                getTokenType().getValue() < (Constants.UNIQUE_SYMBOL_TOKENS + Constants.UNIQUE_ACTION_TOKENS)) {
            //Falls SpezialStein: Vergleich auf zweite vertikale Position des Tokens
            int differenceVerticalPosition2 = newMove.getSecondaryMovePosition().getX()
                    - currentBestMove.getSecondaryMovePosition().getX();
            if (differenceVerticalPosition2 < 0) {
                return true;
            }
            if (differenceVerticalPosition2 > 0) {
                return false;
            }
            //Falls Replacer, wird der neue Zug als besser bewertet
            if (newMove.getToken().getTokenType().getValue() == Constants.UNIQUE_ACTION_TOKENS +
                    Constants.UNIQUE_SYMBOL_TOKENS) {
                return true;
                //TODO möglicherweise bessere AI Logik hier
            }
            //Falls SpezialStein: Vergleich auf zweite horizontale Position des Tokens
            int differenceHorizontalPosition2 = newMove.getSecondaryMovePosition().getY()
                    - currentBestMove.getSecondaryMovePosition().getY();
            return differenceHorizontalPosition2 < 0;
        }
        //Falls der exakt gleiche Zug verglichen wird
        return false;
    }

    private Integer countNumberOfTokenOnGrid(Token token) {
        Integer counter = 0;
        Token[][] grid = Game.getGame().getPlayingField().convertToTokenArray();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; i < grid[0].length; i++) {
                if (grid[i][j] == token) {
                    counter++;
                }
            }
        }
        return counter;
    }


    /**
     * Vergleicht zwei Token-Arten miteinander
     *
     * @param newToken     Neues Token
     * @param currentToken Momentan bestes Token
     * @return 0 = Arten sind gleich, 1 = neues Token ist Symbol und momentanes Token ist Aktion,
     * -1 = neues Token ist Aktion und momentanes Token ist Symbol
     */
    private Integer compareTypeOfToken(Token newToken, Token currentToken) {
        int range = Constants.UNIQUE_SYMBOL_TOKENS;
        int nValue = newToken.getTokenType().getValue();
        int cValue = currentToken.getTokenType().getValue();
        if (nValue <= range && cValue <= range ||
                nValue > range && cValue > range) {
            return 0;
        } else if (nValue <= range) {
            return 1;
        } else {
            return -1;
        }
    }

    //---------------------------------------Best Move Calculation----------------------------------

    private TokenMove calculateBestTokenMove(Token token, Player player) {
        HashSet<TokenMove> tokenMovesPerToken = createPossibleMoves(token, player);
        TokenMove bestMove = null;
        if (tokenMovesPerToken == null) {
            throw new NoMovePossibleException();
        } else {
            for (TokenMove tokenMove : tokenMovesPerToken) {
                if (bestMove == null) {
                    bestMove = tokenMove;
                } else {
                    if (isBetterMove(tokenMove, bestMove, player)) {
                        bestMove = tokenMove;
                    }
                }
            }
        }
        return bestMove;
    }


    private HashSet<TokenMove> createPossibleMoves(Token token, Player player) {
        System.out.println("Possible Moves: " + this.getPlayerID() + " Token: " + token.getTokenType());
        return switch (token.getTokenType().getValue()) {
            case 1, 2, 3, 4, 5, 6 -> createPossibleSymbolTokenMoves(token, player);
            case 7 -> createPossibleRemoverTokenMoves(player);
            case 8 -> createPossibleMoverTokenMoves(player);
            case 9 -> createPossibleSwapperTokenMoves(player);
            case 10 -> createPossibleReplacerTokenMoves(player);
            default -> null;
        };
    }


    //----------------------------------Calculation Symbol Token Moves------------------------------

    public HashSet<TokenMove> createPossibleSymbolTokenMoves(Token token, Player player) {
        HashSet<TokenMove> tokenMoves = new HashSet<>();
        HashSet<Position> emptyFields = emptyFields();
        for (Position position : emptyFields) {
            Calculation currentCalculation = calculateChangeWithMove(player, getGridCopyWithAddedToken(position, token));
            //TODO Prevent Loss

            tokenMoves.add(new TokenMove(position, currentCalculation.pointsChange(), token, currentCalculation.gameWinning(), isMovePreventingLoss()));
        }
        return tokenMoves;
    }

    private HashSet<Position> emptyFields() {
        HashSet<Position> positions = new HashSet<>();
        Token[][] grid = Game.getGame().getPlayingField().convertToTokenArray();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j].getTokenType().getValue() == 0) {
                    positions.add(new Position(i, j));
                }
            }
        }
        return positions;
    }

    /**
     * Berechnet Punkte Veränderung insgesamt
     *
     * @return Punkte Differenz auf das ganze Feld bezogen
     */
    public Calculation calculateChangeWithMove(Player player, Token[][] newGrid) {
        Map<Integer, Integer> pointsMap = calculateCurrentOverallPointsWithChangedToken(newGrid);

        int curr = 0;
        boolean isWinning = false;
        boolean isCreatingLoss = false;
        for (Map.Entry<Integer, Integer> entry : pointsMap.entrySet()) {
            if (entry.getValue() < -100) {
                if (entry.getKey() > 0) {
                    if (player.getTeam() == Team.VERTICAL) {
                        isWinning = true;
                    } else {
                        isCreatingLoss = true;
                    }
                } else {
                    if (player.getTeam() == Team.VERTICAL) {
                        isCreatingLoss = true;
                    } else {
                        isWinning = true;
                    }
                }
            }
            if (entry.getKey() > 0) {
                curr = curr + entry.getValue();
            } else {
                curr = curr - entry.getValue();
            }
        }
        return new Calculation(curr, isCreatingLoss, isWinning);
    }


    /**
     * Berechnet Punkte für jede Linie
     *
     * @return Map (Linie, Punkte)
     */
    public Map<Integer, Integer> calculateCurrentOverallPointsWithChangedToken(Token[][] newGrid) {
        Map<Integer, Map<Token, Integer>> occurrenceMap = getOccurrencesOfTokensWithChangedToken(newGrid);

        Map<Integer, Integer> PointMap = new HashMap<>();

        for (Map.Entry<Integer, Map<Token, Integer>> entry : occurrenceMap.entrySet()) {
            PointMap.put(entry.getKey(), calculate(entry.getValue()));
        }
        return PointMap;
    }

    public Token[][] getGridCopyWithAddedToken(Position position, Token token) {
        Token[][] originalGrid = Game.getGame().getPlayingField().convertToTokenArray();

        Token[][] grid = new Token[Constants.GAMEGRID_ROWS][Constants.GAMEGRID_COLUMNS];
        /*
        for (int i = 0; i < Constants.GAMEGRID_ROWS; i++) {
            System.arraycopy(originalGrid[i], 0, grid[i], 0, Constants.GAMEGRID_COLUMNS);
        }

         */
        grid[position.getX()][position.getY()] = token;

        return grid;
    }

    /**
     * Berrechnet Punkte für einzelne Linie
     *
     * @param map Map(Token, Anzahl Vorkommnisse)
     * @return Points per Line
     */
    public Integer calculate(Map<Token, Integer> map) {
        int current = 0;
        if (map.size() == Constants.GAMEGRID_ROWS) {
            return 6;
        }
        for (Map.Entry<Token, Integer> entry : map.entrySet()) {

            if (entry.getValue() == Constants.GAMEGRID_ROWS && entry.getKey().getTokenType() != TokenType.None) {
                return -1000;
            } else if (entry.getValue() > 1) {
                current = current + entry.getValue() * 2 - 3;
            }
        }
        return current;
    }

    public Map<Integer, Map<Token, Integer>> getOccurrencesOfTokensWithChangedToken(Token[][] grid) {
        Map<Integer, Map<Token, Integer>> occurrenceMap = new HashMap<>();

        for (int i = 0; i < grid.length; i++) {
            occurrenceMap.put(-i - 1, calculateOccurrencesPerLine(grid[i]));
        }
        Token[][] reverseArray = swapMatrix(grid);

        for (int i = 0; i < reverseArray.length; i++) {
            occurrenceMap.put(i + 1, calculateOccurrencesPerLine(reverseArray[i]));
        }
        return occurrenceMap;
    }

    private HashMap<Token, Integer> calculateOccurrencesPerLine(Token[] tokens) {
        HashMap<Token, Integer> map = new HashMap<>();
        Arrays.stream(tokens).forEach(x -> map.put(x, map.computeIfAbsent(x, s -> 0) + 1));

        map.remove(null);

        return map;
    }

    public Token[][] swapMatrix(Token[][] input) {
        Token[][] swap = new Token[input.length][input.length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                swap[j][i] = input[i][j];
            }
        }
        return swap;
    }

    private boolean isMovePreventingLoss(/*Player player, Position position, Token token*/) {
        //TODO: here fill up
        return false;
    }

    //-------------------------------Calculation Removes Token Moves--------------------------------

    public HashSet<TokenMove> createPossibleRemoverTokenMoves(Player player) {
        HashSet<TokenMove> tokenMoves = new HashSet<>();
        HashSet<Position> occupiedFields = occupiedFields();
        for (Position position : occupiedFields) {
            Calculation currentCalculation = calculateChangeWithMove(player, getGridCopyWithAddedToken(position, new Token(TokenType.None)));
            //TODO: Prevent Loss
            tokenMoves.add(new TokenMove(position, currentCalculation.pointsChange(), new Token(TokenType.Remover), false, isMovePreventingLoss()));

        }
        return tokenMoves;
    }


    public HashSet<Position> occupiedFields() {
        HashSet<Position> positions = new HashSet<>();
        Token[][] grid = Game.getGame().getPlayingField().convertToTokenArray();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j].getTokenType().getValue() != 0) {
                    positions.add(new Position(i, j));
                }
            }
        }
        return positions;
    }

    //-----------------------------------Calculation Mover Token Moves------------------------------

    public HashSet<TokenMove> createPossibleMoverTokenMoves(Player player) {
        HashSet<TokenMove> tokenMoves = new HashSet<>();
        HashSet<Position> emptyFields = emptyFields();
        HashSet<Position> occupiedFields = occupiedFields();
        for (Position occupiedPosition : occupiedFields) {
            for (Position emptyPosition : emptyFields) {
                Calculation currentCalculation = calculateChangeWithMove(player,
                        getGridCopyWithSwappedTokens(emptyPosition, getTokenAtPosition(occupiedPosition),
                                occupiedPosition, new Token(TokenType.None)));
                //TODO Prevent Loss
                tokenMoves.add(
                        new TokenMove(emptyPosition, occupiedPosition, currentCalculation.pointsChange(), new Token(TokenType.Mover),
                                currentCalculation.gameWinning(), isMovePreventingLoss()));
            }
        }
        return tokenMoves;
    }

    public Token getTokenAtPosition(Position position) {
        Token[][] originalGrid = Game.getGame().getPlayingField().convertToTokenArray();
        return originalGrid[position.getX()][position.getY()];
    }

    public Token[][] getGridCopyWithSwappedTokens(Position swap1pos, Token swap1, Position swap2pos, Token swap2) {
        Token[][] originalGrid = Game.getGame().getPlayingField().convertToTokenArray();
        Token[][] grid = new Token[Constants.GAMEGRID_ROWS][Constants.GAMEGRID_COLUMNS];
        /*
        for (int i = 0; i < Constants.GAMEGRID_ROWS; i++) {
            System.arraycopy(originalGrid[i], 0, grid[i], 0, Constants.GAMEGRID_COLUMNS);
        }

         */
        grid[swap1pos.getX()][swap1pos.getY()] = swap1;
        grid[swap2pos.getX()][swap2pos.getY()] = swap2;

        return grid;
    }

    //-------------------------------Calculation Swapper Token Moves--------------------------------

    public HashSet<TokenMove> createPossibleSwapperTokenMoves(Player player) {
        HashSet<TokenMove> tokenMoves = new HashSet<>();
        HashSet<Position> occupiedFields = occupiedFields();
        for (Position pos1 : occupiedFields) {
            for (Position pos2 : occupiedFields) {
                if (!pos1.equals(pos2)) {
                    Calculation currentCalculation = calculateChangeWithMove(player,
                            getGridCopyWithSwappedTokens(pos1, getTokenAtPosition(pos2), pos2,
                                    getTokenAtPosition(pos1)));
                    //TODO Prevent Loss
                    tokenMoves.add(new TokenMove(pos1, pos2, currentCalculation.pointsChange(),
                            new Token(TokenType.Swapper), currentCalculation.gameWinning(), isMovePreventingLoss()));
                }
            }
        }
        return tokenMoves;
    }

    //------------------------------Calculation Replacer Token Moves--------------------------------

    public HashSet<TokenMove> createPossibleReplacerTokenMoves(Player player) {
        HashSet<TokenMove> tokenMoves = new HashSet<>();
        //convert player.getTokens() to Array of Tokens
        Token[] playerHand = player.getTokens().toArray(new Token[0]);
        HashSet<Integer> handSymbolTokenSet = player.getHandSymbolTokenPositions();
        HashSet<Position> occupiedFields = occupiedFields();
        for (Position occupiedField : occupiedFields) {
            for (Integer handPosition : handSymbolTokenSet) {
                Calculation currentCalculation = calculateChangeWithMove(player,
                        getGridCopyWithAddedToken(occupiedField, playerHand[handPosition]));
                //TODO Prevent Loss
                tokenMoves.add(new TokenMove(occupiedField, new Position(handPosition), currentCalculation.pointsChange(),
                        new Token(TokenType.Replacer), currentCalculation.gameWinning(), isMovePreventingLoss()));
            }
        }
        return tokenMoves;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Berechnet Punkte für jede Linie
     *
     * @return Map (Linie, Punkte)
     */
    private HashMap<Integer, Integer> calculateCurrentOverallPoints() {
        HashMap<Integer, HashMap<Token, Integer>> occurrenceMap = getOccurrencesOfTokens();
        HashMap<Integer, Integer> PointMap = new HashMap<>();

        for (var entry : occurrenceMap.entrySet()) {
            PointMap.put(entry.getKey(), calculate(entry.getValue()));
        }
        return PointMap;
    }

    public HashMap<Integer, HashMap<Token, Integer>> getOccurrencesOfTokens() {
        HashMap<Integer, HashMap<Token, Integer>> occurrenceMap = new HashMap<>();
        Token[][] grid = Game.getGame().getPlayingField().convertToTokenArray();

        for (int i = 0; i < grid.length; i++) {
            occurrenceMap.put(-i - 1, calculateOccurrencesPerLine(grid[i]));
        }
        Token[][] reverseArray = swapMatrix(grid);

        for (int i = 0; i < reverseArray.length; i++) {
            occurrenceMap.put(i + 1, calculateOccurrencesPerLine(reverseArray[i]));
        }
        return occurrenceMap;
    }
}



