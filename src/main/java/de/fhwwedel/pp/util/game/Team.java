/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 12:11 PM by Carina The Latest changes made by Carina on 7/27/22, 11:22 AM All contents of "Team" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.util.game;

public enum Team {

    VERTICAL("Vertical"), HORIZONTAL("Horizontal");


    private final String team;

    Team(String team) {
        this.team = team;
    }

    public static Team getTeamName(int playerID) {
        if (playerID % 2 == 0) {
            return VERTICAL;
        } else {
            return HORIZONTAL;
        }
    }

    public String getTeamName() {
        return team;
    }
}
