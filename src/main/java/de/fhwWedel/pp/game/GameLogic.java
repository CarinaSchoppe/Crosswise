/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The Latest changes made by Carina on 7/27/22, 11:21 AM All contents of "GameLogic" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwWedel.pp.game;

import org.jetbrains.annotations.NotNull;

public class GameLogic {

    private final Game game;


    public GameLogic(Game game) {
        this.game = game;
    }

    public final boolean isGameWon(@NotNull PlayingField field) {
        //
    }

    public Game getGame() {
        return game;
    }


}
