package de.fhwWedel.pp;

import de.fhwWedel.pp.game.Game;
import de.fhwWedel.pp.game.PlayingField;
import de.fhwWedel.pp.player.Player;
import de.fhwWedel.pp.util.Team;

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
        //TODO:     Crosswise.main(args);

        var player1 = new Player(1, Team.HORIZONTAL, true, "Player 1");
        var player2 = new Player(2, Team.VERTICAL, true, "Player 2");
        var game = new Game(new PlayingField(6), new ArrayList<>(List.of(player1, player2)));
        Game.setGame(game);

    }


}
