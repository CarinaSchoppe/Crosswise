/*
 * Copyright Notice for CrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:29 PM by Carina The Latest changes made by Carina on 8/9/22, 12:51 PM All contents of "TeamTests" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

import me.carinasophie.crosswise.game.Player;
import me.carinasophie.crosswise.game.Team;
import me.carinasophie.crosswise.util.constants.TeamType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeamTests {

    @BeforeEach
    void setup() {
        Team.setHorizontalTeam(new Team(TeamType.HORIZONTAL));
        Team.setVerticalTeam(new Team(TeamType.VERTICAL));
        Team.setDeactiveTeam(new Team(TeamType.DEACTIVE));
    }

    @Test
    void teamCreationAllTests() {
        var player1 = new Player(0, true, "test");
        var player2 = new Player(1, true, "test");
        var player3 = new Player(2, true, "test");
        var player4 = new Player(3, true, "test");

        Team.addPlayerToTeam(player1);
        Team.addPlayerToTeam(player2);
        Team.addPlayerToTeam(player3);
        Team.addPlayerToTeam(player4);
        Assertions.assertEquals(0, Team.getDeactiveTeam().getPlayers().size());
        Assertions.assertEquals(2, Team.getHorizontalTeam().getPlayers().size());
        Assertions.assertEquals(2, Team.getVerticalTeam().getPlayers().size());
    }


    @Test
    void teamCreationTwoTests() {
        var player1 = new Player(0, true, "test");
        var player2 = new Player(1, true, "test");

        Team.addPlayerToTeam(player1);
        Team.addPlayerToTeam(player2);

        Assertions.assertEquals(1, Team.getHorizontalTeam().getPlayers().size());
        Assertions.assertEquals(1, Team.getVerticalTeam().getPlayers().size());
        Assertions.assertEquals(0, Team.getDeactiveTeam().getPlayers().size());

    }

    @Test
    void teamCreationDeactiveTests() {
        var player1 = new Player(0, true, "test");
        var player2 = new Player(1, false, "test");
        var player3 = new Player(2, false, "test");
        var player4 = new Player(3, true, "test");

        Team.addPlayerToTeam(player1);
        Team.addPlayerToTeam(player2);
        Team.addPlayerToTeam(player3);
        Team.addPlayerToTeam(player4);

        Assertions.assertEquals(1, Team.getHorizontalTeam().getPlayers().size());
        Assertions.assertEquals(1, Team.getVerticalTeam().getPlayers().size());
        Assertions.assertEquals(2, Team.getDeactiveTeam().getPlayers().size());
    }
}
