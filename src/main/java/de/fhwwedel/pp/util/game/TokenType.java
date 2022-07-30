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

    public static TokenType getTokenType(int token) {
        for (TokenType type : TokenType.values()) {
            if (type.getValue() == token) {
                return type;
            }
        }
        return None;
    }

    @Override
    public String toString(){
        return ""+value;
    }

    public boolean isSpecial() {
        return special;
    }

    public int getValue() {
        return this.value;
    }
}
