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

import java.util.ArrayList;

public class Team {

    private static final Team horizontalTeam = new Team(TeamType.HORIZONTAL);
    private static final Team verticalTeam = new Team(TeamType.VERTICAL);
    private final TeamType teamType;
    private final ArrayList<Integer> playerIDs = new ArrayList<>();
    private int points;

    public Team(TeamType teamType) {
        this.teamType = teamType;
    }

    public static Team addPlayerToTeam(int player) {
        if (player % 2 == 0) {
            verticalTeam.playerIDs.add(player);
            return verticalTeam;
        } else {
            horizontalTeam.playerIDs.add(player);
            return horizontalTeam;
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

    public ArrayList<Integer> getPlayerIDs() {
        return playerIDs;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
