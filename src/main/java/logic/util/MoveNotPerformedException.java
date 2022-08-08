package logic.util;

public class MoveNotPerformedException extends RuntimeException {

    public MoveNotPerformedException() {
        super("Move could not be performed!");
    }
}
