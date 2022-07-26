package de.fhwWedel.pp.util;

import lombok.Data;

@Data
public class Token {

    private final TokenType tokenType;
    private Position position;

}
