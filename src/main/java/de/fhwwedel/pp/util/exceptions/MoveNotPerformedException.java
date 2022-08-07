package de.fhwwedel.pp.util.exceptions;

public class MoveNotPerformedException extends RuntimeException {

    public MoveNotPerformedException() {
        super("Move could not be performed!");
    }
}
