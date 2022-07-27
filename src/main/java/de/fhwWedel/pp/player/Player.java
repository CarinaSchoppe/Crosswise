/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The Latest changes made by Carina on 7/27/22, 11:21 AM All contents of "Player" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwWedel.pp.player;

import de.fhwWedel.pp.game.Game;
import de.fhwWedel.pp.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;


public class Player {

    private final HashSet<Token> tokens = new HashSet<>();
    private final int playerID;
    private final Team team;
    private final boolean isActive;
    private int points = 0;
    private final String name;

    public Player(int playerID, Team team, boolean isActive, String name) {
        this.playerID = playerID;
        this.team = team;
        this.isActive = isActive;
        this.name = name;
    }

    public boolean normalTokenTurn(@NotNull final Token token, @NotNull final Position position) {
        if (!tokens.contains(token))
            return false;
        var field = Game.getGame().getPlayingField().getCorrespondingPlayingField(position);
        if (field.getToken() != null)
            return false;

        tokens.remove(token);
        field.setToken(token);
        return true;
        //TODO: Update GUI
    }

    public boolean removerTokenTurn(final Token token, final Position position) {
        if (token.getTokenType() != TokenType.Remover)
            return false;

        if (!tokens.contains(token))
            return false;

        var field = Game.getGame().getPlayingField().getCorrespondingPlayingField(position);
        if (field.getToken() == null)
            return false;

        tokens.remove(token);
        tokens.add(field.getToken());
        field.setToken(null);
        return true;

        //TODO: Update GUI for added Token
    }

    public boolean moverTokenTurn(final Token token, Position start, Position end) {
        if (token.getTokenType() != TokenType.Mover)
            return false;
        if (!tokens.contains(token))
            return false;
        var fieldStart = Game.getGame().getPlayingField().getCorrespondingPlayingField(start);
        var fieldEnd = Game.getGame().getPlayingField().getCorrespondingPlayingField(end);

        if (fieldStart.getToken() == null)
            return false;
        if (fieldEnd.getToken() != null)
            return false;

        tokens.remove(token);
        fieldEnd.setToken(fieldStart.getToken());
        fieldStart.setToken(null);
        return true;
    }

    public boolean swapperTokenTurn(final Token token, final Position first, final Position second) {
        if (token.getTokenType() != TokenType.Swapper)
            return false;
        if (!tokens.contains(token))
            return false;
        var fieldFirst = Game.getGame().getPlayingField().getCorrespondingPlayingField(first);
        var fieldSecond = Game.getGame().getPlayingField().getCorrespondingPlayingField(second);
        if (fieldFirst.getToken() == null)
            return false;
        if (fieldSecond.getToken() == null)
            return false;
        tokens.remove(token);
        var temp = fieldFirst.getToken();
        fieldFirst.setToken(fieldSecond.getToken());
        fieldSecond.setToken(temp);
        return true;

        //TODO: Update GUI
    }


    public Token drawToken() throws NoTokenException {
        if (Game.getGame().getTokenDrawPile().isEmpty())
            throw new NoTokenException("No more tokens left in the Pile!");

        var token = Game.getGame().getTokenDrawPile().get(new Random().nextInt(Game.getGame().getTokenDrawPile().size()));
        Game.getGame().getTokenDrawPile().remove(token);
        tokens.add(token);
        return token;

        //TODO: Add token to Players GUI
    }

    public HashSet<Token> getTokens() {
        return tokens;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }
}
