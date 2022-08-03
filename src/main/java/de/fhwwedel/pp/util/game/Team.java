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
import java.util.List;

/**
 * Class for the Team of the game Crosswise
 *
 * @author Jacob Klövekorn
 */
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
     * Constructor
     *
     * @param teamType TeamType for the team
     */
    public Team(TeamType teamType) {
        this.teamType = teamType;
    }

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

    public static void setHorizontalTeam(Team horizontalTeam) {
        Team.horizontalTeam = horizontalTeam;
    }

    public static void setVerticalTeam(Team verticalTeam) {
        Team.verticalTeam = verticalTeam;
    }

    public static void setDeactiveTeam(Team deactiveTeam) {
        Team.deactiveTeam = deactiveTeam;
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

    public List<Player> getPlayers() {
        return players;
    }

    public int getPoints() {
        return points;
    }

    public boolean isRowWin() {
        return rowWin;
    }

    public void setRowWin(boolean rowWin) {
        this.rowWin = rowWin;
    }
}
