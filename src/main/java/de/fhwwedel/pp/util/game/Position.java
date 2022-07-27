/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 12:11 PM by Carina The Latest changes made by Carina on 7/27/22, 11:22 AM All contents of "Position" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.util.game;


public class Position {
    private final int x;
    private final int y;
    private Token token;
    private final boolean hand;
    private final int handPosition;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.hand = false;
        this.handPosition = -1;
    }

    public Position(int handPosition) {
        this.handPosition = handPosition;
        hand = true;
        x = -1;
        y = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position position)) return false;

        if (getX() != position.getX()) return false;
        return getY() == position.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public boolean isHand() {
        return hand;
    }

    public int getHandPosition() {
        return handPosition;
    }
}
