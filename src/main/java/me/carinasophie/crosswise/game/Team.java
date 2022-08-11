/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "Team" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.game;

import lombok.Data;
import me.carinasophie.crosswise.util.constants.TeamType;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class for the Team of the game Crosswise
 *
 * @author Carina Sophie Schoppe
 */
@SuppressWarnings({"MethodDoesntCallSuperMethod"})
@Data
public class Team {
    /**
     * Static instance of a horizontal team
     */
    private static Team horizontalTeam = new Team(TeamType.HORIZONTAL);
    /**
     * Static instance of a vertical team
     */
    private static Team verticalTeam = new Team(TeamType.VERTICAL);
    /**
     * Static instance of an inactive team
     */
    private static Team deactiveTeam = new Team(TeamType.DEACTIVE);
    /**
     * TeamType
     */
    private final TeamType teamType;
    /**
     * List of players in the team
     */
    private final ArrayList<Player> players = new ArrayList<>();
    /**
     * Current Points of the team
     */
    private int points;
    /**
     * Team won with a full row
     */
    private boolean rowWin = false;

    //----------------------------------------------------------------------------------------------



    /**
     * Adds player into the corresponding team, based on their ID and activity
     *
     * @param player Player to add into the team
     * @return returns the updated team
     */
    public static Team addPlayerToTeam(Player player) {
        //if the player is inactive
        if (!player.isActive()) {
            deactiveTeam.players.add(player);
            return deactiveTeam;
        }
        //if the player is active
        if (player.getPlayerID() % 2 == 0) {
            verticalTeam.players.add(player);
            return verticalTeam;
        } else {
            horizontalTeam.players.add(player);
            return horizontalTeam;
        }
    }

    /**
     * Calculate points for the teams
     */
    public static void givePoints() {
        Map<Integer, Integer> pointsMap = AI.calculateCurrentOverallPoints();
        horizontalTeam.points = 0;
        verticalTeam.points = 0;
        for (Integer key : pointsMap.keySet()) {
            if (key > 0) {
                verticalTeam.points += pointsMap.get(key);
            } else {
                horizontalTeam.points += pointsMap.get(key);
            }
        }
    }

    //---------------------------------------------Getter + Setter------------------------------------------------------


    public static Team getHorizontalTeam() {
        return horizontalTeam;
    }

    public static Team getVerticalTeam() {
        return verticalTeam;
    }

    public static Team getDeactiveTeam() {
        return deactiveTeam;
    }

    public static void setHorizontalTeam(Team horizontalTeam) {
        Team.horizontalTeam = horizontalTeam;
    }

    public static void setVerticalTeam(Team verticalTeam) {
        Team.verticalTeam = verticalTeam;
    }

    public static void setDeactiveTeam(Team deactiveTeam) {
        Team.deactiveTeam = deactiveTeam;
    }

    @Override
    public String toString() {
        return teamType.getTeamName();
    }
}
