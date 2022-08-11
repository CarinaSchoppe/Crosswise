/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "Position" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.util;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.carinasophie.crosswise.util.constants.Token;

import java.util.Objects;

/**
 * Class to store a specific position on the game grid of the hand of a player
 *
 * @author Carina Sophie Schoppe
 */

@ToString
@Getter
@Setter
public class Position {
    /**
     * First coordinate on the game grid
     */
    private final Integer x;
    /**
     * Second coordinate on the game grid
     */
    private final Integer y;
    /**
     * Token on the position
     */
    private Token token;
    /**
     * Boolean, if the position is a hand position
     */
    private final boolean hand;
    /**
     * Index of position on the hand of the current player
     */
    private final int handPosition;

    /**
     * Constructor for a position on the game grid
     *
     * @param x first coordinate of the game grid
     * @param y second coordinate of teh game grid
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.hand = false;
        this.handPosition = -1;
    }

    /**
     * Constructor for a position on a hand of a player
     *
     * @param handPosition hand index
     */
    public Position(int handPosition) {
        this.handPosition = handPosition;
        hand = true;
        x = -1;
        y = -1;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Position position)) {
            return false;
        }

        if (!Objects.equals(getX(), position.getX())) {
            return false;
        }
        return Objects.equals(getY(), position.getY());
    }

    /**
     * Gives out String representation with the token
     *
     * @return toString string
     */
    public String toStringWithToken() {
        return "Position{" + "y=" + x + ", x=" + y + ", token=" + token.tokenType().getValue()
                + '}';
    }
}
