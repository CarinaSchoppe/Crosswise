package logic.Exceptions;

/**
 * Exception for situation, where there are no more moves possible
 *
 * @author Jacob Klövekorn
 */
public class NoMovePossibleException extends RuntimeException {
    /**
     * Standard message for no moves possible
     */
    public NoMovePossibleException() {
        super("There is no move possible");
    }

    /**
     * Specific exception for no moves possible
     *
     * @param message specific exception message text
     */
    public NoMovePossibleException(String message) {
        super(message);
    }
}
