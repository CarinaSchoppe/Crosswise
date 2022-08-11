/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "MoveNotPerformedException" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.util.exceptions;

/**
 * Exception for situation, where the move didn't get performed
 *
 * @author Carina Sophie Schoppe
 */
public class MoveNotPerformedException extends RuntimeException {
    /**
     * Exception for a not performed move
     */
    public MoveNotPerformedException() {
        super("Move could not be performed!");
    }
}
