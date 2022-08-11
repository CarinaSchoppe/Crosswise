/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "GameData" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.util.filehandle;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Game Data class which stores Data to be saved or loaded from/to a file
 *
 * @author Carina Sophie Schoppe
 */
@Data
public final class GameData {
    /**
     * Players as PlayerData objects
     */
    private final PlayerData[] players;
    /**
     * current player as int
     */
    @SerializedName("currPlayer")
    private final Integer currentPlayer;
    /**
     * game field as double array int representation
     */
    private final int[][] field;
    /**
     * used action tiles as int array representation
     */
    private final int[] usedActionTiles;


}
