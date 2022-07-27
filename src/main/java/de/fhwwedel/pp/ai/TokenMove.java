/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 5:34 PM by Carina The Latest changes made by Carina on 7/27/22, 5:34 PM All contents of "TokenMove" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.ai;

import de.fhwwedel.pp.util.game.Position;
import de.fhwwedel.pp.util.game.Token;

public class TokenMove {

    /**
     * Position, an den der Stein platziert werden soll. Falls es sich um einen Aktionsstein,
     * handelt, gelten die Folgenden Regeln:
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
    private final Token token;
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
     * Mover - Position, an den der Stein plaziert werden soll
     * Swapper - Position, die gewechselt wird
     * Replacer - Position, des auszutauschenden Tokens auf der Hand
     */
    private Position secondaryMovePosition = null;


    public TokenMove(Position primaryMovePosition, Integer relativeChange,
                     Token token, boolean gameWinning, boolean isPreventingLoss) {
        this.primaryMovePosition = primaryMovePosition;
        this.relativeChange = relativeChange;
        this.token = token;
        this.gameWinning = gameWinning;
        this.isPreventingLoss = isPreventingLoss;
    }

    public TokenMove(Position primaryMovePosition, Position secondaryMovePosition, Integer relativeChange,
                     Token token, boolean gameWinning, boolean isPreventingLoss) {
        this(primaryMovePosition, relativeChange, token, gameWinning, isPreventingLoss);
        this.secondaryMovePosition = secondaryMovePosition;

    }

    public Position getPrimaryMovePosition() {
        return primaryMovePosition;
    }

    public Position getSecondaryMovePosition() {
        return secondaryMovePosition;
    }

    public Integer getRelativeChange() {
        return relativeChange;
    }

    public Token getToken() {
        return token;
    }

    public boolean isGameWinning() {
        return gameWinning;
    }

    public boolean isPreventingLoss() {
        return isPreventingLoss;
    }
}
