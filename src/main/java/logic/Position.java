package logic;


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

    @Override
    public String toString() {
        return "Position{" +
                "y=" + x +
                ", x=" + y +
                '}';
    }

    public String toStringWithToken() {
        return "Position{" +
                "y=" + x +
                ", x=" + y + ", token=" + token.tokenType().getValue() +
                '}';
    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        result = 31 * result + (getToken() != null ? getToken().hashCode() : 0);
        result = 31 * result + (isHand() ? 1 : 0);
        result = 31 * result + getHandPosition();
        return result;
    }
}
