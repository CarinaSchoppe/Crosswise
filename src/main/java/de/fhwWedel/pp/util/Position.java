package de.fhwWedel.pp.util;

import lombok.Data;

@Data
public class Position {
    private final int x;
    private final int y;
    private Token token;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position position)) return false;

        if (getX() != position.getX()) return false;
        return getY() == position.getY();
    }
}
