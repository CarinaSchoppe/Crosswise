package logic;

import java.util.*;

/**
 * Class of an AI player
 *
 * @author Jacob Klövekorn
 */
public class AI extends Player {

    /**
     * Constructor
     *
     * @param playerID ID of the player
     * @param active boolean, if AI is active
     * @param name name of the AI
     */
    public AI(int playerID, boolean active, String name) {
        super(playerID, active, name);
    }


    //##############################################################################################
    //#################################### AI Move Calculation #####################################
    //##############################################################################################

    /**
     * Calculates the occurrences of each token (except for the empty one) for a given line
     *
     * @param tokens Line of tokens
     * @return Map with TokenTypes and their occurrences in the line
     */
    private static EnumMap<TokenType, Integer> calculateOccurrencesPerLine(TokenType[] tokens) {
        EnumMap<TokenType, Integer> map = new EnumMap<>(TokenType.class);
        Arrays.stream(tokens).forEach(x -> map.put(x, map.computeIfAbsent(x, s -> 0) + 1));

        map.remove(null);
        map.remove(TokenType.NONE);
        return map;
    }

    /**
     * Swaps a matrix diagonally
     *
     * @param input matrix that will be swapped
     * @return swapped matrix
     */
    public static TokenType[][] swapMatrix(TokenType[][] input) {
        TokenType[][] swap = new TokenType[input.length][input.length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                swap[j][i] = input[i][j];
            }
        }
        return swap;
    }

    //##############################################################################################
    //#################################### Move Comparison #########################################
    //##############################################################################################

    /**
     * Compares two moves with each over with some given rules. If a rule doesn't apply, go to the
     * next one.
     * <p>
     * 1. Check, if the move will win you the game
     * 2. Check, if the move will prevent the enemy team from winning
     * 3. Check, if the move gives you more points than the other
     * 4. Check, if the token is a symbol Token rather than a special one
     * 5. Check, if the new move-token is more often on your hand
     * 6. Check, if the new move-token is less frequent on the game-board
     * 7. Check, if the new move-token has a smaller ordinality than the current best one
     * 8. Check, if the new move-token will be positioned higher than the current best one
     * 9. Check, if the new move-token will be positioned more on the left side than the current
     * best one
     *
     * @param newMove         New input Token-Move
     * @param currentBestMove Current best Token-Move
     * @return returns true, if newMove is better than the old one, otherwise returns false
     */
    public boolean isBetterMove(TokenMove newMove, TokenMove currentBestMove) {
        //check if the move will win you the game
        if (newMove.isGameWinning()) {
            return true;
        } else if (currentBestMove.isGameWinning()) {
            return false;
        }

        //check, if the move will prevent a loss
        if (newMove.isPreventingLoss()) {
            return true;
        } else if (currentBestMove.isPreventingLoss()) {
            return false;
        }

        //compare change in points of the two turns
        int difference = newMove.getRelativeChange() - currentBestMove.getRelativeChange();
        if (this.getTeam().getTeamType() == TeamType.VERTICAL) {
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

        //choose a symbol token over a special one
        int compareTokenValue = compareTypeOfToken(newMove.getToken(), currentBestMove.getToken());
        if (compareTokenValue == 1) {
            return true;
        }
        if (compareTokenValue == -1) {
            return false;
        }

        //compare on occurrences on hand of the tokens
        int tokenAmountInHandDifference = this.tokenAmountInHand(newMove.getToken()) - this.tokenAmountInHand(currentBestMove.getToken());
        if (tokenAmountInHandDifference > 0) {
            return true;
        }
        if (tokenAmountInHandDifference < 0) {
            return false;
        }

        //compare amount of tokens on the board (only with symbol tokens)
        if (newMove.getToken().getValue() <= Constants.UNIQUE_SYMBOL_TOKENS) {
            int differenceOccurrencesOnBoard = countNumberOfTokenOnGrid(newMove.getToken()) - countNumberOfTokenOnGrid(currentBestMove.getToken());
            if (differenceOccurrencesOnBoard > 0) {
                return false;
            }
            if (differenceOccurrencesOnBoard < 0) {
                return true;
            }
        }

        //compare ordinality of the moves
        int differenceOrdinalityOfToken = newMove.getToken().getValue() - currentBestMove.getToken().getValue();
        if (differenceOrdinalityOfToken > 0) {
            return false;
        }
        if (differenceOrdinalityOfToken < 0) {
            return true;
        }

        //compare vertical position of the tokens
        int differenceVerticalPosition = newMove.getPrimaryMovePosition().getX() - currentBestMove.getPrimaryMovePosition().getX();

        if (differenceVerticalPosition < 0) {
            return true;
        }
        if (differenceVerticalPosition > 0) {
            return false;
        }
        //compare horizontal position of the token
        int differenceHorizontalPosition = newMove.getPrimaryMovePosition().getY() - currentBestMove.getPrimaryMovePosition().getY();
        if (differenceHorizontalPosition < 0) {
            return true;
        }
        if (differenceHorizontalPosition > 0) {
            return false;
        }

        //if special token:
        if (newMove.getToken().getValue() > Constants.UNIQUE_SYMBOL_TOKENS + 1 && newMove.getToken().getValue() < (Constants.UNIQUE_SYMBOL_TOKENS + Constants.UNIQUE_ACTION_TOKENS)) {

            //if replacer, the new turn will be the better one
            if (newMove.getToken().getValue() == Constants.UNIQUE_ACTION_TOKENS + Constants.UNIQUE_SYMBOL_TOKENS) {
                return true;
            }
            //compare the x-position of the secondary position
            int differenceVerticalPosition2 = newMove.getSecondaryMovePosition().getX() - currentBestMove.getSecondaryMovePosition().getX();
            if (differenceVerticalPosition2 < 0) {
                return true;
            }
            if (differenceVerticalPosition2 > 0) {
                return false;
            }
            //compare the y-position of the secondary position
            int differenceHorizontalPosition2 = newMove.getSecondaryMovePosition().getY() - currentBestMove.getSecondaryMovePosition().getY();
            return differenceHorizontalPosition2 < 0;
        }

        //if both turns are the exact same move
        return false;
    }

    /**
     * Calculates Points for every Line
     *
     * @return Map (line, points)
     */
    public static Map<Integer, Integer> calculateCurrentOverallPoints() {
        //get all occurrences of tokens
        Map<Integer, EnumMap<TokenType, Integer>> occurrenceMap = getOccurrencesOfTokens();
        Map<Integer, Integer> pointMap = new HashMap<>();

        for (Map.Entry<Integer, EnumMap<TokenType, Integer>> entry : occurrenceMap.entrySet()) {
            //calculates the amount of points for each line and puts them into the resultMap
            pointMap.put(entry.getKey(), calculate(entry.getValue()));
        }
        return pointMap;
    }


    /**
     * Compares two token with each other whether they are symbol or action tokens
     *
     * @param newToken     new TokenType, which will be compared
     * @param currentToken current TokenType, which will be compared
     * @return 0 = Both are in the same category, 1 = newToken is a Symbol and current is a Special,
     * -1 = newToken is a Special and current is a Symbol
     */
    private Integer compareTypeOfToken(TokenType newToken, TokenType currentToken) {
        int range = Constants.UNIQUE_SYMBOL_TOKENS;
        int nValue = newToken.getValue();
        int cValue = currentToken.getValue();
        if (nValue <= range && cValue <= range || nValue > range && cValue > range) {
            return 0;
        } else if (nValue <= range) {
            return 1;
        } else {
            return -1;
        }
    }

    //##############################################################################################
    //################################### Best Move Calculation ####################################
    //##############################################################################################

    /**
     * Calculates best TokenMove for a given Token
     *
     * @param token TokenType for comparison
     * @return returns a TokenMove with the necessary parameters
     */
    private TokenMove calculateBestTokenMove(TokenType token) {
        //get all possible moves for a specific token
        Set<TokenMove> tokenMovesPerToken = createPossibleMoves(token);
        TokenMove bestMove = null;
        if (tokenMovesPerToken == null) {
            throw new NoMovePossibleException();
        } else {
            for (TokenMove tokenMove : tokenMovesPerToken) {
                //check, if its the first move that is getting compared
                if (bestMove == null) {
                    bestMove = tokenMove;
                } else {
                    //replaces the current best move, if the new one is better
                    if (isBetterMove(tokenMove, bestMove)) {
                        bestMove = tokenMove;
                    }
                }
            }
        }
        return bestMove;
    }

    /**
     * Decider method, which will lead to Possible Creating methods for specific TokenTypes
     *
     * @param token TokenType, for which the possible moves will be created
     * @return HashSet of all possible TokenMoves for this Token
     */
    private Set<TokenMove> createPossibleMoves(TokenType token) {
        return switch (token.getValue()) {
            case 1, 2, 3, 4, 5, 6 -> createPossibleSymbolTokenMoves(token);
            case 7 -> createPossibleRemoverTokenMoves();
            case 8 -> createPossibleMoverTokenMoves();
            case 9 -> createPossibleSwapperTokenMoves();
            case 10 -> createPossibleReplacerTokenMoves();
            default -> null;
        };
    }


    //##############################################################################################
    //################################## Symbol Move Calculation ###################################
    //##############################################################################################

    /**
     * Creates all possible Moves for the given Symbol Token
     *
     * @param token Symbol TokenType
     * @return returns HashSet with all possible Moves for the given Symbol Token
     */
    public Set<TokenMove> createPossibleSymbolTokenMoves(TokenType token) {
        HashSet<TokenMove> tokenMoves = new HashSet<>();
        HashSet<Position> emptyFields = emptyFields();
        for (Position position : emptyFields) {
            TokenType[][] changedTokenGrid = getGridCopyWithAddedToken(position, token);
            Calculation currentCalculation = calculateChangeWithMove(changedTokenGrid);
            //Create new TokenMove
            tokenMoves.add(new TokenMove(position, currentCalculation.pointsChange(), token, currentCalculation.gameWinning(), isMovePreventingLoss(changedTokenGrid)));
        }
        return tokenMoves;
    }

    /**
     * Calculates Points for a single given Line.
     *
     * @param map Map with TokenTypes and the number of occurrences for this Token on the line
     * @return Points per line
     */
    public static Integer calculate(Map<TokenType, Integer> map) {
        int current = 0;
        if (map.size() == Constants.GAMEGRID_SIZE) {
            return 6;
        }
        for (Map.Entry<TokenType, Integer> entry : map.entrySet()) {
            // if a row is filled with Tokens from a specific
            if (entry.getValue() == Constants.GAMEGRID_SIZE && entry.getKey() != TokenType.NONE) {
                return -1000;
            } else if (entry.getValue() > 1) {
                //adds the amount of points for that line
                current = current + entry.getValue() * 2 - 3;
            }
        }
        return current;
    }

    /**
     * Calculates Occurrences of Tokens for the current Grid
     *
     * @return returns a Map of Line indexes and their corresponding occurrenceMap. The row-indexes
     * are named -1, -2, ... and the column-indexes are named 1, 2, ...
     */
    public static Map<Integer, EnumMap<TokenType, Integer>> getOccurrencesOfTokens() {
        Map<Integer, EnumMap<TokenType, Integer>> occurrenceMap = new HashMap<>();
        TokenType[][] grid = Game.getGame().getPlayingField().convertToTokenTypeArray();
        //get occurrences for the horizontal team
        for (int i = 0; i < grid.length; i++) {
            occurrenceMap.put(-i - 1, calculateOccurrencesPerLine(grid[i]));
        }
        //swap the matrix (deep copy)
        TokenType[][] reverseArray = swapMatrix(grid);
        //get occurrences for the vertical team
        for (int i = 0; i < reverseArray.length; i++) {
            occurrenceMap.put(i + 1, calculateOccurrencesPerLine(reverseArray[i]));
        }
        return occurrenceMap;
    }

    /**
     * Call Player Turn method for corresponding TokenMove
     */
    public void makeMove() {
        TokenMove move = calculateAIMove();
        //Debug sleep timer
        try {
            Thread.sleep(Constants.AI_TURN_TIME);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        //perform different turn actions for each tokenType
        switch (move.getToken().getValue()) {
            case 1, 2, 3, 4, 5, 6 -> {
                if (!normalTokenTurn(new Token(move.getToken()), move.getPrimaryMovePosition()))
                    throw new MoveNotPerformedException();
            }
            case 7 -> {
                if (!removerTokenTurn(new Token(move.getToken()), move.getPrimaryMovePosition()))
                    throw new MoveNotPerformedException();
            }
            case 8 -> {
                if (!moverTokenTurn(new Token(move.getToken()), move.getSecondaryMovePosition(), move.getPrimaryMovePosition()))
                    throw new MoveNotPerformedException();
            }
            case 9 -> {
                if (!swapperTokenTurn(new Token(move.getToken()), move.getPrimaryMovePosition(), move.getSecondaryMovePosition()))
                    throw new MoveNotPerformedException();
            }
            case 10 -> {
                if (!replacerTokenTurn(new Token(move.getToken()), move.getPrimaryMovePosition(), move.getSecondaryMovePosition()))
                    throw new MoveNotPerformedException();
            }
            default -> throw new NoMovePossibleException("No move possible");
        }
        //handle the end of a turn
        Game.getGame().turnDone();
    }

    /**
     * Calculates best Move for AI Player. First it calculates the best Move per Token in hand and
     * then compares those two with each other
     *
     * @return Best Move the AI can do with his Hand-Tokens
     */
    public TokenMove calculateAIMove() {
        ArrayList<TokenMove> bestMovePerToken = new ArrayList<>();
        TokenType[] playerHand = this.convertHandToTokenTypeArray();
        //calcualte best move for each hand token
        for (TokenType token : playerHand) {
            if (token != null) {
                bestMovePerToken.add(calculateBestTokenMove(token));
            }
        }
        //Debug Ausgaben
        if (CrossWise.DEBUG)
            for (TokenMove move : bestMovePerToken) {
                if (move != null)
                    System.out.println(move.getToken() + " " + move.getRelativeChange() + " " + move.getPrimaryMovePosition().getX() + "/" + move.getPrimaryMovePosition().getY());
            }
        //compare the best token move of each hand token and overwrite the current best tokenMove, if the new one is better
        Integer bestToken = null;
        for (int i = 0; i < getHandTokens().size(); i++) {
            if (bestMovePerToken.get(i) != null && (bestToken == null || isBetterMove(bestMovePerToken.get(i), bestMovePerToken.get(bestToken)))) {
                bestToken = i;
            }
        }
        return bestMovePerToken.get(Objects.<Integer>requireNonNull(bestToken));
    }

    /**
     * Counts the number of a given TokenType on the grid
     *
     * @param token TokenType that will be counted
     * @return Number of given TokenType on the grid
     */
    private Integer countNumberOfTokenOnGrid(TokenType token) {
        Integer counter = 0;
        Token[][] grid = Game.getGame().getPlayingField().convertToTokenArray();
        //increases counter for e occurrence of the asked token in the GameGrid
        for (Token[] tokens : grid) {
            for (Token value : tokens) {
                if (value.tokenType() == token) {
                    counter++;
                }
            }
        }
        return counter;
    }

    //##############################################################################################
    //################################### Remover Token Moves ######################################
    //##############################################################################################

    /**
     * Creates all possible TokenMoves for a Remover Token
     *
     * @return HashSet of all possible Moves
     */
    public Set<TokenMove> createPossibleRemoverTokenMoves() {
        HashSet<TokenMove> tokenMoves = new HashSet<>();
        //get all occupied fields
        Set<Position> occupiedFields = occupiedFields();
        for (Position position : occupiedFields) {
            TokenType[][] changedTokenGrid = getGridCopyWithAddedToken(position, TokenType.NONE);
            Calculation currentCalculation = calculateChangeWithMove(changedTokenGrid);
            //Create Token Move for every possible move and add it to the existing moves
            tokenMoves.add(new TokenMove(position, currentCalculation.pointsChange(), TokenType.REMOVER, false, isMovePreventingLoss(changedTokenGrid)));
        }
        return tokenMoves;
    }

    //##############################################################################################
    //################################### Mover Token Moves ########################################
    //##############################################################################################

    /**
     * Create possible Moves for a Mover token
     *
     * @return HashSet of all possible Moves
     */
    public Set<TokenMove> createPossibleMoverTokenMoves() {
        HashSet<TokenMove> tokenMoves = new HashSet<>();
        //all empty fields
        HashSet<Position> emptyFields = emptyFields();
        //all occupied fields
        Set<Position> occupiedFields = occupiedFields();
        for (Position occupiedPosition : occupiedFields) {
            for (Position emptyPosition : emptyFields) {
                //create a new grid with the current move
                TokenType[][] changedTokenGrid = getGridCopyWithSwappedTokens(emptyPosition, getTokenAtPosition(occupiedPosition), occupiedPosition, TokenType.NONE);
                Calculation currentCalculation = calculateChangeWithMove(changedTokenGrid);
                //Create Token Move for every possible move and add it to the existing moves
                tokenMoves.add(new TokenMove(emptyPosition, occupiedPosition, currentCalculation.pointsChange(), TokenType.MOVER, currentCalculation.gameWinning(), isMovePreventingLoss(changedTokenGrid)));
            }
        }
        return tokenMoves;
    }

    //##############################################################################################
    //################################### Swapper Token Moves ######################################
    //##############################################################################################

    /**
     * Create all possible Moves for a Swapper Token
     *
     * @return HashSet of possible Moves
     */
    public Set<TokenMove> createPossibleSwapperTokenMoves() {
        HashSet<TokenMove> tokenMoves = new HashSet<>();
        Set<Position> occupiedFields = occupiedFields();
        //iterate through all positions and again through all to create all possible combinations of two non 0 tokens
        for (Position pos1 : occupiedFields) {
            for (Position pos2 : occupiedFields) {
                if (!pos1.equals(pos2)) {
                    TokenType[][] changedTokenGrid = getGridCopyWithSwappedTokens(pos1, getTokenAtPosition(pos2), pos2, getTokenAtPosition(pos1));
                    Calculation currentCalculation = calculateChangeWithMove(changedTokenGrid);
                    //Create Token Move for every possible move and add it to the existing moves
                    tokenMoves.add(new TokenMove(pos1, pos2, currentCalculation.pointsChange(), TokenType.SWAPPER, currentCalculation.gameWinning(), isMovePreventingLoss(changedTokenGrid)));
                }
            }
        }
        return tokenMoves;
    }

    //##############################################################################################
    //################################## Replacer Token Moves ######################################
    //##############################################################################################

    /**
     * Calculates all occurrences of Tokens on a new grid per line
     *
     * @param grid grid how it will look, after the move is done
     * @return returns a Map of Line indexes and their corresponding occurrenceMap. The row-indexes
     * are named -1, -2, ... and the column-indexes are named 1, 2, ...
     */
    public Map<Integer, EnumMap<TokenType, Integer>> getOccurrencesOfTokensWithChangedToken(TokenType[][] grid) {
        Map<Integer, EnumMap<TokenType, Integer>> occurrenceMap = new HashMap<>();
        //calculate occurrences for each row
        for (int i = 0; i < grid.length; i++) {
            occurrenceMap.put(-i - 1, calculateOccurrencesPerLine(grid[i]));
        }
        TokenType[][] reverseArray = swapMatrix(grid);
        //calculate occurrences for each column
        for (int i = 0; i < reverseArray.length; i++) {
            occurrenceMap.put(i + 1, calculateOccurrencesPerLine(reverseArray[i]));
        }
        return occurrenceMap;
    }

    //##############################################################################################
    //###################################### Helper Methods ########################################
    //##############################################################################################

    /**
     * Calculates all empty fields
     *
     * @return HashSet of positions of empty fields
     */
    private HashSet<Position> emptyFields() {
        HashSet<Position> positions = new HashSet<>();
        Token[][] grid = Game.getGame().getPlayingField().convertToTokenArray();
        //iterate through all fields of the game field and add it to the new HashSet of positions, if its empty
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j].tokenType().getValue() == 0) {
                    positions.add(new Position(i, j));
                }
            }
        }
        return positions;
    }

    /**
     * Calculates all occupied fields
     *
     * @return HashSet of positions of occupied fields
     */
    public Set<Position> occupiedFields() {
        HashSet<Position> positions = new HashSet<>();
        Token[][] grid = Game.getGame().getPlayingField().convertToTokenArray();
        //iterate through all fields of the game field and add it to the new HashSet of positions, if its not empty
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j].tokenType().getValue() != 0) {
                    positions.add(new Position(i, j));
                }
            }
        }
        return positions;
    }

    /**
     * Calculates whether the move will prevent a loss (blocks an almost finished line of the same
     * Token or uses a special Token to disrupt it
     *
     * @param changedMap new grid, which already has the TokenMove implemented
     * @return returns true if it will prevent a loss, otherwise returns false
     */
    private boolean isMovePreventingLoss(TokenType[][] changedMap) {

        Map<Integer, EnumMap<TokenType, Integer>> map = getOccurrencesOfTokens();
        Map<Integer, EnumMap<TokenType, Integer>> changedOccurrenceMap = getOccurrencesOfTokensWithChangedToken(changedMap);

        for (Map.Entry<Integer, EnumMap<TokenType, Integer>> entry : map.entrySet()) {
            if (entry.getKey() < 0) {
                if (this.getTeam().getTeamType() == TeamType.VERTICAL && entry.getValue().size() == 1) {
                    for (Map.Entry<TokenType, Integer> count : entry.getValue().entrySet()) {
                        if (count.getValue() == Constants.GAMEGRID_SIZE - 1) {
                            EnumMap<TokenType, Integer> line = changedOccurrenceMap.get(entry.getKey());
                            //Check, if there are other tokens in the same line after the move
                            if (line.size() != 1) {
                                return true;
                            }
                            //Check, if there isn't just one missing Token anymore to win in the
                            //line
                            for (Map.Entry<TokenType, Integer> lineMap : line.entrySet()) {
                                if (lineMap.getValue() != Constants.GAMEGRID_SIZE - 1) {
                                    return true;
                                }
                            }
                        }
                    }

                }
            } else {
                if (this.getTeam().getTeamType() == TeamType.HORIZONTAL && entry.getValue().size() == 1) {
                    for (Map.Entry<TokenType, Integer> count : entry.getValue().entrySet()) {
                        if (count.getValue() == Constants.GAMEGRID_SIZE - 1) {
                            Map<TokenType, Integer> line = changedOccurrenceMap.get(entry.getKey());
                            //Check, if there are other tokens in the same line after the move
                            if (line.size() != 1) {
                                return true;
                            }
                            //Check, if there isn't just one missing Token anymore to win in the
                            //line
                            for (Map.Entry<TokenType, Integer> lineMap : line.entrySet()) {
                                if (lineMap.getValue() != Constants.GAMEGRID_SIZE - 1) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Create all possible moves for a Replacer Token
     *
     * @return HashSet of possible TokenMoves
     */
    public Set<TokenMove> createPossibleReplacerTokenMoves() {
        HashSet<TokenMove> tokenMoves = new HashSet<>();
        //convert player.getTokens() to Array of Tokens
        Token[] playerHand = this.getHandTokens().toArray(new Token[0]);
        Set<Integer> handSymbolTokenSet = this.getHandSymbolTokenPositions();
        Set<Position> occupiedFields = occupiedFields();
        for (Position occupiedField : occupiedFields) {
            for (Integer handPosition : handSymbolTokenSet) {
                TokenType[][] changedTokenGrid = getGridCopyWithAddedToken(occupiedField, playerHand[handPosition].tokenType());
                Calculation currentCalculation = calculateChangeWithMove(changedTokenGrid);
                //Create Token Move for every possible move and add it to the existing moves
                tokenMoves.add(new TokenMove(occupiedField, new Position(handPosition), currentCalculation.pointsChange(), TokenType.REPLACER, currentCalculation.gameWinning(), isMovePreventingLoss(changedTokenGrid)));
            }
        }
        return tokenMoves;
    }

    /**
     * Calculates point difference (p>0 -> vertical team winning, p<0 -> horizontal team winning)
     * if the current move would occur
     *
     * @return Calculation object, with change and if it's creating a win or loss
     */
    public Calculation calculateChangeWithMove(TokenType[][] newGrid) {
        Map<Integer, Integer> pointsMap = calculateCurrentOverallPointsWithChangedToken(newGrid);

        int curr = 0;
        boolean isWinning = false;
        for (Map.Entry<Integer, Integer> entry : pointsMap.entrySet()) {
            if (entry.getValue() < -100) {
                if (entry.getKey() > 0) {
                    if (this.getTeam().getTeamType() == TeamType.VERTICAL) {
                        isWinning = true;
                    } else {
                        curr = curr - 1000;
                    }
                } else {
                    if (this.getTeam().getTeamType() == TeamType.VERTICAL) {
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
        return new Calculation(curr, isWinning);
    }

    /**
     * Calculates Points for every Line with the new grid if the move would occur
     *
     * @return Map (line, points)
     */
    public Map<Integer, Integer> calculateCurrentOverallPointsWithChangedToken(TokenType[][] newGrid) {
        Map<Integer, EnumMap<TokenType, Integer>> occurrenceMap = getOccurrencesOfTokensWithChangedToken(newGrid);

        Map<Integer, Integer> pointMap = new HashMap<>();

        for (Map.Entry<Integer, EnumMap<TokenType, Integer>> entry : occurrenceMap.entrySet()) {
            pointMap.put(entry.getKey(), calculate(entry.getValue()));
        }

        return pointMap;
    }

    /**
     * Get copy of grid with an added Token
     *
     * @param position Position, where the token should be placed
     * @param token    Token, which will be placed
     * @return new grid with the added Token
     */
    public TokenType[][] getGridCopyWithAddedToken(Position position, TokenType token) {
        TokenType[][] originalGrid = Game.getGame().getPlayingField().convertToTokenTypeArray();
        TokenType[][] grid = new TokenType[Constants.GAMEGRID_SIZE][Constants.GAMEGRID_SIZE];

        for (int i = 0; i < Constants.GAMEGRID_SIZE; i++) {
            System.arraycopy(originalGrid[i], 0, grid[i], 0, Constants.GAMEGRID_SIZE);
        }
        //Add token to the original grid
        grid[position.getX()][position.getY()] = token;

        return grid;
    }

    /**
     * Get the Token at the given position
     *
     * @param position Position of the Token
     * @return TokenType of that position
     */
    public TokenType getTokenAtPosition(Position position) {
        TokenType[][] originalGrid = Game.getGame().getPlayingField().convertToTokenTypeArray();
        return originalGrid[position.getX()][position.getY()];
    }

    /**
     * Get a copy of a grid with two tokens swapped
     *
     * @param swap1pos Position of the first Token
     * @param swap1    Token that will be swapped
     * @param swap2pos Position of the second Token
     * @param swap2    Token that will be swapped
     * @return new grid with the swapped Tokens
     */
    public TokenType[][] getGridCopyWithSwappedTokens(Position swap1pos, TokenType swap1, Position swap2pos, TokenType swap2) {
        TokenType[][] originalGrid = Game.getGame().getPlayingField().convertToTokenTypeArray();
        TokenType[][] grid = new TokenType[Constants.GAMEGRID_SIZE][Constants.GAMEGRID_SIZE];

        for (int i = 0; i < Constants.GAMEGRID_SIZE; i++) {
            System.arraycopy(originalGrid[i], 0, grid[i], 0, Constants.GAMEGRID_SIZE);
        }
        grid[swap1pos.getX()][swap1pos.getY()] = swap1;
        grid[swap2pos.getX()][swap2pos.getY()] = swap2;

        return grid;
    }
}



