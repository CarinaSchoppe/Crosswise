/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/28/22, 4:56 PM by Carina The Latest changes made by Carina on 7/28/22, 4:56 PM All contents of "Action" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.util.special;

public enum Action {
    PLACE("placed"), REMOVE("removed"), SWAPPED("swapped"), MOVED("moved"), REPLACED("replaced");

    private final String text;

    Action(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
