/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 5:53 PM by Carina The Latest changes made by Carina on 7/27/22, 5:53 PM All contents of "Calculation" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.ai;

/**
 * Record for a Calculation
 *
 * @param pointsChange Change in points for the move
 * @param isCreatingLoss will the move create a loss
 * @param gameWinning will the move win the game
 *
 * @author Jacob Klövekorn
 */
public record Calculation(Integer pointsChange, boolean isCreatingLoss, boolean gameWinning) {
}
