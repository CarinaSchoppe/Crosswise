/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "Action" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.util.constants;

/**
 * Enum class for an action, used by the logger
 *
 * @author Carina Sophie Schoppe
 */
public enum Action {
    /**
     * All possible actions
     */
    PLACE("placed"), REMOVE("removed"), SWAPPED("swapped"), MOVED("moved"), REPLACED("replaced");
    /**
     * String text of the performed action
     */
    private final String text;

    /**
     * Constructor
     *
     * @param text given action
     */
    Action(String text) {
        this.text = text;
    }

    /**
     * Getter
     *
     * @return String output of the action
     */
    public String getText() {
        return text;
    }
}
