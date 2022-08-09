package logic.util;
/**
 * Exception for situation, where is no token available
 *
 * @author Jacob Klövekorn
 */
public class NoTokenException extends RuntimeException {
    /**
     * Exception for no token available
     *
     * @param message specific exception message text
     */
    public NoTokenException(String message) {
        super(message);
    }
}
