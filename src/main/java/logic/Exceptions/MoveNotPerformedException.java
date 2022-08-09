package logic.Exceptions;
/**
 * Exception for situation, where the move didn't get performed
 *
 * @author Jacob Klövekorn
 */
public class MoveNotPerformedException extends RuntimeException {
    /**
     * Exception for a not performed move
     */
    public MoveNotPerformedException() {
        super("Move could not be performed!");
    }
}
