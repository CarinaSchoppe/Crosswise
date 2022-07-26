package de.fhwWedel.pp.util;

import org.jetbrains.annotations.NotNull;

public class NoTokenException extends Exception {

    public NoTokenException(@NotNull String message) {
        super(message);
    }
}
