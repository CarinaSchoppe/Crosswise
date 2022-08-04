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
import de.fhwwedel.pp.gui.FakeGUI;
import de.fhwwedel.pp.gui.GameWindow;
import de.fhwwedel.pp.player.Player;
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

    //TODO: hands buggy not really showing
    // TODO: hands on start remove


    public static final boolean DEBUG = true;
    public static long time;
    public static final boolean UI = true;
    public static int DELAY = 200;

    public static void main(String... args) {
        var window = new GameWindow();
        var fakeWindow = new FakeGUI();
        new Thread(() -> {
            try {
                if (CrossWise.UI) Thread.sleep(CrossWise.DELAY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            time = System.currentTimeMillis();
            var player1 = new AI(0, true, "Player 1");
            player1.create();
            var player2 = new AI(1, true, "Player 2");
            player2.create();
            var player3 = new Player(2, true, "Player 3");
            player3.create();
            var player4 = new Player(3, true, "Player 4");
            player4.create();
            Game game;
            if (UI)
                game = new Game(new PlayingField(Constants.GAMEGRID_ROWS), new ArrayList<>(List.of(player1, player2, player3, player4)), window);
            else
                game = new Game(new PlayingField(Constants.GAMEGRID_ROWS), new ArrayList<>(List.of(player1, player2, player3, player4)), fakeWindow);
            Game.setGame(game);
            game.setup(false);
            DELAY = 200;
            game.start();
        }).start();
        if (CrossWise.UI) {
            window.start();
        }

    }


}
