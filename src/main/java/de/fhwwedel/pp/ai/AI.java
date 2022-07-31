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
import de.fhwwedel.pp.util.game.TeamType;
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
            switch (move.getToken().getValue()) {
                case 1, 2, 3, 4, 5, 6 -> normalTokenTurn(new Token(move.getToken()), move.getPrimaryMovePosition());
                case 7 -> removerTokenTurn(new Token(move.getToken()), move.getPrimaryMovePosition());
                case 8 -> moverTokenTurn(new Token(move.getToken()), move.getSecondaryMovePosition(), move.getPrimaryMovePosition());
                case 9 -> swapperTokenTurn(new Token(move.getToken()), move.getPrimaryMovePosition(), move.getSecondaryMovePosition());
                case 10 -> replacerTokenTurn(new Token(move.getToken()), move.getPrimaryMovePosition(), move.getSecondaryMovePosition());
            }
            performAnimation();
            Game.getGame().turnDone();
        } catch (NoTokenException e) {
            //TODO: needed?
        }
    }


    public TokenMove calculateAIMove(Player player) throws NoTokenException {
        ArrayList<TokenMove> bestMovePerToken = new ArrayList<>();
        TokenType[] playerHand = player.convertHandToTokenTypeArray();

        for (TokenType token : playerHand) {
            if (token != null) {
                System.out.println("current Token: " + token.getValue());
                bestMovePerToken.add(calculateBestTokenMove(token, player));
            }
        }

        for (var bla : bestMovePerToken) {
            if (bla != null)
                System.out.println(bla.getToken() + " " + bla.getRelativeChange() + " " + bla.getPrimaryMovePosition().getX() + "/" + bla.getPrimaryMovePosition().getY());
        }

        Integer bestToken = null;
        //TODO getTokens .size?!
        for (int i = 0; i < getTokens().size(); i++) {
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


        if (player.getTeam().getTeamType() == TeamType.VERTICAL) {
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
        if (newMove.getToken().getValue() <= Constants.UNIQUE_SYMBOL_TOKENS) {
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
        int differenceOrdinalityOfToken = newMove.getToken().getValue() -
                currentBestMove.getToken().getValue();
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
        if (newMove.getToken().getValue() > Constants.UNIQUE_SYMBOL_TOKENS && newMove.getToken()
                .getValue() < (Constants.UNIQUE_SYMBOL_TOKENS + Constants.UNIQUE_ACTION_TOKENS)) {
            //Falls SpezialStein: Vergleich auf zweite vertikale Position des Tokens
            //Falls Replacer, wird der neue Zug als besser bewertet
            if (newMove.getToken().getValue() >= Constants.UNIQUE_ACTION_TOKENS +
                    Constants.UNIQUE_SYMBOL_TOKENS - 1) {
                return true;
                //TODO möglicherweise bessere AI Logik hier
            }
            int differenceVerticalPosition2 = newMove.getSecondaryMovePosition().getX()
                    - currentBestMove.getSecondaryMovePosition().getX();
            if (differenceVerticalPosition2 < 0) {
                return true;
            }
            if (differenceVerticalPosition2 > 0) {
                return false;
            }


            //Falls SpezialStein: Vergleich auf zweite horizontale Position des Tokens
            int differenceHorizontalPosition2 = newMove.getSecondaryMovePosition().getY()
                    - currentBestMove.getSecondaryMovePosition().getY();
            return differenceHorizontalPosition2 < 0;
        }
        //Falls der exakt gleiche Zug verglichen wird
        return false;
    }

    private Integer countNumberOfTokenOnGrid(TokenType token) {
        Integer counter = 0;
        Token[][] grid = Game.getGame().getPlayingField().convertToTokenArray();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; i < grid[0].length; i++) {
                if (grid[i][j].getTokenType() == token) {
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
    private Integer compareTypeOfToken(TokenType newToken, TokenType currentToken) {
        int range = Constants.UNIQUE_SYMBOL_TOKENS;
        int nValue = newToken.getValue();
        int cValue = currentToken.getValue();
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

    private TokenMove calculateBestTokenMove(TokenType token, Player player) {
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


    private HashSet<TokenMove> createPossibleMoves(TokenType token, Player player) {
        System.out.println("Possible Moves: " + this.getPlayerID() + " Token: " + token);
        return switch (token.getValue()) {
            case 1, 2, 3, 4, 5, 6 -> createPossibleSymbolTokenMoves(token, player);
            case 7 -> createPossibleRemoverTokenMoves(player);
            case 8 -> createPossibleMoverTokenMoves(player);
            case 9 -> createPossibleSwapperTokenMoves(player);
            case 10 -> createPossibleReplacerTokenMoves(player);
            default -> null;
        };
    }


    //----------------------------------Calculation Symbol Token Moves------------------------------

    public HashSet<TokenMove> createPossibleSymbolTokenMoves(TokenType token, Player player) {
        HashSet<TokenMove> tokenMoves = new HashSet<>();
        HashSet<Position> emptyFields = emptyFields();
        for (Position position : emptyFields) {
            System.out.println("Move: Player " + player.getPlayerID() + " " + token + ", " + position.getX() + "/" + position.getY());
            Calculation currentCalculation = calculateChangeWithMove(player, getGridCopyWithAddedToken(position, token));
            //System.out.println("PointsChange for previous calc: " + currentCalculation.pointsChange());

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
    public Calculation calculateChangeWithMove(Player player, TokenType[][] newGrid) {
        Map<Integer, Integer> pointsMap = calculateCurrentOverallPointsWithChangedToken(newGrid);

        int curr = 0;
        boolean isWinning = false;
        boolean isCreatingLoss = false;
        for (Map.Entry<Integer, Integer> entry : pointsMap.entrySet()) {
            if (entry.getValue() < -100) {
                if (entry.getKey() > 0) {
                    if (player.getTeam().getTeamType() == TeamType.VERTICAL) {
                        isWinning = true;
                    } else {
                        curr = curr - 1000;
                    }
                } else {
                    if (player.getTeam().getTeamType() == TeamType.VERTICAL) {
                        curr = curr + 1000;
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
    public Map<Integer, Integer> calculateCurrentOverallPointsWithChangedToken(TokenType[][] newGrid) {
        Map<Integer, Map<TokenType, Integer>> occurrenceMap = getOccurrencesOfTokensWithChangedToken(newGrid);

        /*
        for (var entry : occurrenceMap.entrySet()) {
            System.out.print(entry.getKey() + ":: ");
            for (var entry2 : entry.getValue().entrySet()) {
                System.out.print(entry2.getKey()  + "=" + entry2.getValue());
            }
            System.out.println(" ");
        }
        System.out.println("------");

         */


        Map<Integer, Integer> PointMap = new HashMap<>();

        for (Map.Entry<Integer, Map<TokenType, Integer>> entry : occurrenceMap.entrySet()) {
            PointMap.put(entry.getKey(), calculate(entry.getValue()));
        }

        return PointMap;
    }

    public TokenType[][] getGridCopyWithAddedToken(Position position, TokenType token) {
        TokenType[][] originalGrid = Game.getGame().getPlayingField().convertToTokenTypeArray();



        TokenType[][] grid = new TokenType[Constants.GAMEGRID_ROWS][Constants.GAMEGRID_COLUMNS];

        for (int i = 0; i < Constants.GAMEGRID_ROWS; i++) {
            System.arraycopy(originalGrid[i], 0, grid[i], 0, Constants.GAMEGRID_COLUMNS);
        }
        grid[position.getX()][position.getY()] = token;
        /*
        System.out.println("AAA");
        System.out.println(token.getTokenType());
        System.out.println(position.getX() + "/" + position.getY());
        for (int i = 0; i < originalGrid.length; i++) {
            for (int j = 0; j < originalGrid.length; j++) {
                System.out.print(grid[i][j].getTokenType().getValue() + " ");
            }
            System.out.println("");
        }

         */


        return grid;
    }

    /**
     * Berrechnet Punkte für einzelne Linie
     *
     * @param map Map(Token, Anzahl Vorkommnisse)
     * @return Points per Line
     */
    public Integer calculate(Map<TokenType, Integer> map) {
        int current = 0;
        if (map.size() == Constants.GAMEGRID_ROWS) {
            return 6;
        }
        for (Map.Entry<TokenType, Integer> entry : map.entrySet()) {
            if (entry.getValue() == Constants.GAMEGRID_ROWS && entry.getKey() != TokenType.None) {
                return -1000;
            } else if (entry.getValue() > 1) {
                current = current + entry.getValue() * 2 - 3;
            }
        }
        return current;
    }

    public Map<Integer, Map<TokenType, Integer>> getOccurrencesOfTokensWithChangedToken(TokenType[][] grid) {
        Map<Integer, Map<TokenType, Integer>> occurrenceMap = new HashMap<>();

        for (int i = 0; i < grid.length; i++) {
            occurrenceMap.put(-i - 1, calculateOccurrencesPerLine(grid[i]));
        }
        TokenType[][] reverseArray = swapMatrix(grid);

        for (int i = 0; i < reverseArray.length; i++) {
            occurrenceMap.put(i + 1, calculateOccurrencesPerLine(reverseArray[i]));
        }

        return occurrenceMap;
    }

    private HashMap<TokenType, Integer> calculateOccurrencesPerLine(TokenType[] tokens) {
        HashMap<TokenType, Integer> map = new HashMap<>();
        Arrays.stream(tokens).forEach(x -> map.put(x, map.computeIfAbsent(x, s -> 0) + 1));

        map.remove(null);
        map.remove(TokenType.None);
        return map;
    }

    public TokenType[][] swapMatrix(TokenType[][] input) {
        TokenType[][] swap = new TokenType[input.length][input.length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                swap[j][i] = input[i][j];
            }
        }
        return swap;
    }

    private boolean isMovePreventingLoss(/*Player player, Position position, Token token*/) {

        HashMap<Integer, HashMap<TokenType, Integer>> map = getOccurrencesOfTokens();







        //TODO: here fill up
        return false;
    }

    //-------------------------------Calculation Removes Token Moves--------------------------------

    public HashSet<TokenMove> createPossibleRemoverTokenMoves(Player player) {
        HashSet<TokenMove> tokenMoves = new HashSet<>();
        HashSet<Position> occupiedFields = occupiedFields();
        for (Position position : occupiedFields) {

            System.out.println("Move: Player " + player.getPlayerID() + " " + TokenType.Remover + ", " + position.getX() + "/" + position.getY());
            Calculation currentCalculation = calculateChangeWithMove(player, getGridCopyWithAddedToken(position, TokenType.None));
            //System.out.println("PointsChange for previous calc: " + currentCalculation.pointsChange());
            //TODO: Prevent Loss
            tokenMoves.add(new TokenMove(position, currentCalculation.pointsChange(), TokenType.Remover, false, isMovePreventingLoss()));

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
                System.out.println("Move: Player " + player.getPlayerID() + " " + TokenType.Mover + ", " +
                        occupiedPosition.getX() + "/" + occupiedPosition.getY() + " to " + emptyPosition.getX() + "/" + emptyPosition.getY());
                Calculation currentCalculation = calculateChangeWithMove(player,
                        getGridCopyWithSwappedTokens(emptyPosition, getTokenAtPosition(occupiedPosition),
                                occupiedPosition, TokenType.None));
                //System.out.println("PointsChange for previous calc: " + currentCalculation.pointsChange());
                //TODO Prevent Loss
                tokenMoves.add(
                        new TokenMove(emptyPosition, occupiedPosition, currentCalculation.pointsChange(), TokenType.Mover,
                                currentCalculation.gameWinning(), isMovePreventingLoss()));
            }
        }
        return tokenMoves;
    }

    public TokenType getTokenAtPosition(Position position) {
        TokenType[][] originalGrid = Game.getGame().getPlayingField().convertToTokenTypeArray();
        return originalGrid[position.getX()][position.getY()];
    }

    public TokenType[][] getGridCopyWithSwappedTokens(Position swap1pos, TokenType swap1, Position swap2pos, TokenType swap2) {
        TokenType[][] originalGrid = Game.getGame().getPlayingField().convertToTokenTypeArray();
        TokenType[][] grid = new TokenType[Constants.GAMEGRID_ROWS][Constants.GAMEGRID_COLUMNS];

        for (int i = 0; i < Constants.GAMEGRID_ROWS; i++) {
            System.arraycopy(originalGrid[i], 0, grid[i], 0, Constants.GAMEGRID_COLUMNS);
        }


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
                            TokenType.Swapper, currentCalculation.gameWinning(), isMovePreventingLoss()));
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
                        getGridCopyWithAddedToken(occupiedField, playerHand[handPosition].getTokenType()));
                //TODO Prevent Loss
                tokenMoves.add(new TokenMove(occupiedField, new Position(handPosition), currentCalculation.pointsChange(),
                        TokenType.Replacer, currentCalculation.gameWinning(), isMovePreventingLoss()));
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

    public static HashMap<Integer, Integer> calculateCurrentOverallPoints() {
        HashMap<Integer, HashMap<TokenType, Integer>> occurrenceMap = getOccurrencesOfTokens();
        HashMap<Integer, Integer> PointMap = new HashMap<>();

        for (var entry : occurrenceMap.entrySet()) {
            PointMap.put(entry.getKey(), calculate(entry.getValue()));
        }
        return PointMap;
    }

    public static HashMap<Integer, HashMap<TokenType, Integer>> getOccurrencesOfTokens() {
        HashMap<Integer, HashMap<TokenType, Integer>> occurrenceMap = new HashMap<>();
        TokenType[][] grid = Game.getGame().getPlayingField().convertToTokenTypeArray();

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



