package logic.util;

import logic.ConstantsEnums.TokenType;

/**
 * Class for a specific move of a token in the game Crosswise
 *
 * @author Jacob Klövekorn
 */

public class TokenMove {

    /**
     * Position, an den der Stein platziert werden soll. Falls es sich um einen Aktionsstein,
     * handelt, gelten die folgenden Regeln:
     * Remover - Position von dem der Stein entfernt wird,
     * Mover - Position, an den der Stein platziert wird
     * Swapper - Position, die gewechselt wird
     * Replacer - Position, des auszutauschenden Tokens auf dem Board
     */
    private final Position primaryMovePosition;
    /**
     * Änderung der Punktezahl bei Tätigung dieses Zuges
     */
    private final Integer relativeChange;
    /**
     * Das zu benutzende Token
     */
    private final TokenType token;
    /**
     * boolean, ob der Zug einen Gewinn erzeugt
     */
    private final boolean gameWinning;
    /**
     * boolean, ob der Zug eine Niederlage verhindert
     */
    private final boolean isPreventingLoss;
    /**
     * Position für Aktionssteine mit folgenden Regeln:
     * Mover - Position, an den der Stein platziert werden soll
     * Swapper - Position, die gewechselt wird
     * Replacer - Position, des auszutauschenden Tokens auf der Hand
     */
    private Position secondaryMovePosition = null;

    //----------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param primaryMovePosition first move position (for clarification look at class variable)
     * @param relativeChange      change of points with this move
     * @param token               Token, that got used for this move
     * @param gameWinning         Is the move game winning
     * @param isPreventingLoss    Is the move preventing a loss
     */
    public TokenMove(Position primaryMovePosition, Integer relativeChange,
                     TokenType token, boolean gameWinning, boolean isPreventingLoss) {
        this.primaryMovePosition = primaryMovePosition;
        this.relativeChange = relativeChange;
        this.token = token;
        this.gameWinning = gameWinning;
        this.isPreventingLoss = isPreventingLoss;
    }

    /**
     * Constructor for specific special TokenTypes
     *
     * @param primaryMovePosition   first move position (for clarification look at class variable)
     * @param secondaryMovePosition second move position (for clarification look at class variable)
     * @param relativeChange        change of points with this move
     * @param token                 Token, that got used for this move
     * @param gameWinning           Is the move game winning
     * @param isPreventingLoss      Is the move preventing a loss
     */
    public TokenMove(Position primaryMovePosition, Position secondaryMovePosition,
                     Integer relativeChange, TokenType token, boolean gameWinning,
                     boolean isPreventingLoss) {
        this(primaryMovePosition, relativeChange, token, gameWinning, isPreventingLoss);
        this.secondaryMovePosition = secondaryMovePosition;
    }

    //---------------------------------------------------Getter---------------------------------------------------------

    public Position getPrimaryMovePosition() {
        return primaryMovePosition;
    }

    public Position getSecondaryMovePosition() {
        return secondaryMovePosition;
    }

    public Integer getRelativeChange() {
        return relativeChange;
    }

    public TokenType getToken() {
        return token;
    }

    public boolean isGameWinning() {
        return gameWinning;
    }

    public boolean isPreventingLoss() {
        return isPreventingLoss;
    }

    //---------------------------------------------------Overrides------------------------------------------------------

    @Override
    public boolean equals(Object other) {
        if (other instanceof TokenMove move) {

            return primaryMovePosition.equals(move.primaryMovePosition)
                    && secondaryMovePosition == null ? move.secondaryMovePosition == null
                    : secondaryMovePosition.equals(move.secondaryMovePosition)
                    && relativeChange.equals(move.relativeChange) && token.equals(move.token)
                    && gameWinning == move.gameWinning && isPreventingLoss == move.isPreventingLoss;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "TokenMove{" +  "primaryMovePosition=" + primaryMovePosition + ", relativeChange="
                + relativeChange + ", token=" + token + ", gameWinning=" + gameWinning
                + ", isPreventingLoss=" + isPreventingLoss + ", secondaryMovePosition="
                + secondaryMovePosition + '}';
    }
}
