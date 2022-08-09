package logic.util;


import logic.ConstantsEnums.Token;

import java.util.Objects;

/**
 * Class to store a specific position on the game grid of the hand of a player
 *
 * @author Jacob Klövekorn
 */
public class Position {
    /**
     * First coordinate on the game grid
     */
    private final int x;
    /**
     * Second coordinate on the game grid
     */
    private final int y;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position position)) {
            return false;
        }

        if (!Objects.equals(getX(), position.getX())) {
            return false;
        }
        if (getY() == null && position.getY() == null) {
            return true;
        } else {
            return Objects.equals(getY(), position.getY());
        }
    }

    //-------------------------------------------------Getter-----------------------------------------------------------

    public Integer getX() {
        return x;
    }

    public Integer getY() {
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

    //---------------------------------------------toString overrides---------------------------------------------------

    @Override
    public String toString() {
        return "Position{" + "y=" + x + ", x=" + y + "hand=" + '}';
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
