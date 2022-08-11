/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "NoMovePossibleException" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.util.exceptions;

/**
 * Exception for situation, where there are no more moves possible
 *
 * @author Carina Sophie Schoppe
 */
public class NoMovePossibleException extends RuntimeException {
    /**
     * Standard message for no moves possible
     */
    public NoMovePossibleException() {
        super("There is no move possible");
    }

    /**
     * Specific exception for no moves possible
     *
     * @param message specific exception message text
     */
    public NoMovePossibleException(String message) {
        super(message);
    }
}
