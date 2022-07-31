/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/28/22, 7:08 PM by Carina The Latest changes made by Carina on 7/28/22, 7:08 PM All contents of "Team" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.util.game;

import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.player.Player;

import java.util.ArrayList;

public class Team {

    private static final Team horizontalTeam = new Team(TeamType.HORIZONTAL);
    private static final Team verticalTeam = new Team(TeamType.VERTICAL);
    private static final Team deactiveTeam = new Team(TeamType.DEACTIVE);
    private final TeamType teamType;
    private final ArrayList<Player> players = new ArrayList<>();
    private int points;

    public Team(TeamType teamType) {
        this.teamType = teamType;
    }

    public static Team addPlayerToTeam(Player player) {
        if (!player.isActive()) {
            deactiveTeam.players.add(player);
            return deactiveTeam;
        }

        if (player.getPlayerID() % 2 == 0) {
            verticalTeam.players.add(player);
            return verticalTeam;
        } else {
            horizontalTeam.players.add(player);
            return horizontalTeam;
        }

    }

    public static void givePoints() {
        var pointsMap = AI.calculateCurrentOverallPoints();
        horizontalTeam.points = 0;
        verticalTeam.points = 0;
        for (var key : pointsMap.keySet()) {
            if (key > 0) {
                verticalTeam.points += pointsMap.get(key);
            } else {
                horizontalTeam.points += pointsMap.get(key);
            }
        }
    }

    public static Team getHorizontalTeam() {
        return horizontalTeam;
    }

    public static Team getVerticalTeam() {
        return verticalTeam;
    }

    public TeamType getTeamType() {
        return teamType;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
