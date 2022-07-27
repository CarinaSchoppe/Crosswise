/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 12:08 PM by Carina The Latest changes made by Carina on 7/27/22, 11:22 AM All contents of "NoTokenException" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwWedel.pp.util.exceptions;

import org.jetbrains.annotations.NotNull;

public class NoTokenException extends Exception {

    public NoTokenException(@NotNull String message) {
        super(message);
    }
}
