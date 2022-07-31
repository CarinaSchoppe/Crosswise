/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 12:11 PM by Carina The Latest changes made by Carina on 7/27/22, 11:22 AM All contents of "TokenType" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.util.game;

public enum TokenType {
    NONE(0, false, "pictures/0none.png"),
    SUN(1, false, "pictures/1sun.png"),
    CROSS(2, false, "pictures/2cross.png"),
    TRIANGLE(3, false, "pictures/3triangle.png"),
    SQUARE(4, false, "pictures/4square.png"),
    PENTAGON(5, false, "pictures/5pentagon.png"),
    STAR(6, false, "pictures/6star.png"),
    REMOVER(7, true, "pictures/7remove.png"),
    MOVER(8, true, "pictures/8move.png"),
    SWAPPER(9, true, "pictures/9swaponboard.png"),
    REPLACER(10, true, "pictures/10swapwithhand.png");


    private final int value;

    private final boolean special;
    private final String imagePath;

    TokenType(int value, boolean special, String imagePath) {
        this.value = value;
        this.special = special;
        this.imagePath = imagePath;
    }

    public static TokenType getTokenType(int token) {
        for (TokenType type : TokenType.values()) {
            if (type.getValue() == token) {
                return type;
            }
        }
        return NONE;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    public boolean isSpecial() {
        return special;
    }

    public int getValue() {
        return this.value;
    }
}
