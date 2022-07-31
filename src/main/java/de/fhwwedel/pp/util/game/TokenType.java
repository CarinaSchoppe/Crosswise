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
    NONE(0, false, "/gui/0 - none.png"),
    SUN(1, false, "/gui/1 - sun.png"),
    CROSS(2, false, "/gui/2 - cross.png"),
    TRIANGLE(3, false, "/gui/3 - triangle.png"),
    SQUARE(4, false, "/gui/4 - square.png"),
    PENTAGON(5, false, "/gui/5 - pentagon.png"),
    STAR(6, false, "/gui/6 - star.png"),
    REMOVER(7, true, "/gui/7 - remover.png"),
    MOVER(8, true, "/gui/8 - mover.png"),
    SWAPPER(9, true, "/gui/9 - swapper.png"),
    REPLACER(10, true, "/gui/10 - replacer.png");


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
