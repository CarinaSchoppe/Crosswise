/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 5:42 PM by Carina The Latest changes made by Carina on 7/27/22, 5:42 PM All contents of "NoMovePossibleException" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwWedel.pp.util.exceptions;

public class NoMovePossibleException extends RuntimeException {
    public NoMovePossibleException() {
        super("There is no move possible");
    }
}
