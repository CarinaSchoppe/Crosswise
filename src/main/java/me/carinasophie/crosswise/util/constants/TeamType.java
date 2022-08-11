/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "TeamType" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.util.constants;

/**
 * Enum for a TeamType
 *
 * @author Carina Sophie Schoppe
 */
public enum TeamType {

    VERTICAL("Vertical"), HORIZONTAL("Horizontal"), DEACTIVE("Deactive");
    /**
     * Name of the team
     */
    private final String team;

    /**
     * Constructor
     *
     * @param team team name
     */
    TeamType(String team) {
        this.team = team;
    }

    /**
     * Getter
     *
     * @return team name
     */
    public String getTeamName() {
        return team;
    }
}
