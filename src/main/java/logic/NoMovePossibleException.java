package logic;

public class NoMovePossibleException extends RuntimeException {
    public NoMovePossibleException() {
        super("There is no move possible");
    }

    public NoMovePossibleException(String message) {
        super(message);
    }
}
