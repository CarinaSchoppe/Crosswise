/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The Latest changes made by Carina on 7/27/22, 11:07 AM All contents of "CrossWise" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp;

import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.game.PlayingField;
import de.fhwwedel.pp.util.special.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class is necessary as the main class for our program must not inherit
 * from {@link javafx.application.Application}
 *
 * @author mjo
 */
public class CrossWise {


    public static void main(String... args) {

        //TODO: GameWindow.start();
        var player1 = new AI(1, true, "Player 1");
        player1.create();
        var player2 = new AI(2, true, "Player 2");
        player2.create();
        var player3 = new AI(3, false, "Player 3");
        player3.create();
        var player4 = new AI(4, false, "Player 4");
        player4.create();
        var game = new Game(new PlayingField(Constants.GAMEGRID_ROWS), new ArrayList<>(List.of(player1, player2, player3, player4)));
        Game.setGame(game);
        game.setup(false);
        game.start();

    }

    //TODO: Team in handleGameOver (weil jetzt ja Klasse statt instanz?)
    // TODO: GUI bei Team game over!


}
