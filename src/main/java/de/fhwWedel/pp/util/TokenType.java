package de.fhwWedel.pp.util;

public enum TokenType {
    None(0, false),
    Sun(1, false),
    Cross(2, false),
    Triangle(3, false),
    Square(4, false),
    Pentagon(5, false),
    Star(6, false),
    Remover(7, true),
    Mover(8, true),
    Swapper(9, true),
    Replacer(10, true);


    private final int value;

    private final boolean special;

    TokenType(int value, boolean special) {
        this.value = value;
        this.special = special;
    }

    public boolean isSpecial() {
        return special;
    }

    public int getValue() {
        return this.value;
    }
}