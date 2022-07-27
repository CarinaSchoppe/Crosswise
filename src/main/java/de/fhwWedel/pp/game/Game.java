/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The Latest changes made by Carina on 7/27/22, 11:22 AM All contents of "Game" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwWedel.pp.game;

import de.fhwWedel.pp.ai.AI;
import de.fhwWedel.pp.player.Player;
import de.fhwWedel.pp.util.exceptions.NoTokenException;
import de.fhwWedel.pp.util.game.Token;
import de.fhwWedel.pp.util.game.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Game {

    private static Game game;

    private GameLogic gameLogic;
    private final PlayingField playingField;
    private final ArrayList<Player> players;
    private final ArrayList<Token> usedSpecialTokens = new ArrayList<>();
    private final ArrayList<Token> tokenDrawPile = new ArrayList<>();
    private Player currentPlayer = null;

    public Game(@NotNull PlayingField playingField, @NotNull ArrayList<Player> players) {
        this.playingField = playingField;
        this.players = players;
    }


    private void fillPile() {
        for (var token : TokenType.values()) {
            if (token.isSpecial()) {
                for (int i = 0; i < 3; i++) {
                    tokenDrawPile.add(new Token(token));
                }
            } else {
                for (int i = 0; i < 7; i++) {
                    tokenDrawPile.add(new Token(token));
                }
            }
        }
    }

    public static Game getGame() {
        return game;
    }


    private void playerPileSetup() {
        for (Player player : players) {
            for (int i = 0; i < 4; i++) {
                try {
                    player.drawToken();
                } catch (NoTokenException e) {
                    throw new RuntimeException("No more tokens left in the Pile while startup! Configuration error!");
                }
            }
        }
    }

    public static void setGame(Game game) {
        Game.game = game;
    }

    private void nextPlayer() {
        if (currentPlayer == null && players.size() > 0) {
            currentPlayer = players.get(0);
        } else {
            int index = players.indexOf(currentPlayer);
            if (index == players.size() - 1) {
                currentPlayer = players.get(0);
            } else {
                currentPlayer = players.get(index + 1);
            }
        }

        if (currentPlayer instanceof AI AIPlayer) {
            AIPlayer.makeMove();
        } else {
            currentPlayer.notifyTurn();
        }

    }

    private boolean handleOver() {
        var over = gameLogic.isGameOver(playingField);
        if (over.containsKey(true)) {
            var team = over.get(true);
            //TODO: Handle game over! GUI stuff

            if (team == null) {
            } else {

            }
        } else {
            return false;
        }
        return true;
    }

    public void turnDone() {
        if (handleOver()) {
            return;
        }
        nextPlayer();
    }

    public PlayingField getPlayingField() {
        return playingField;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Token> getUsedSpecialTokens() {
        return usedSpecialTokens;
    }

    public ArrayList<Token> getTokenDrawPile() {
        return tokenDrawPile;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void start() {
        gameLogic = new GameLogic(this);
        handleOver();
        if (players.size() < 2)
            throw new IllegalArgumentException("There must be at least 2 players");
        currentPlayer = players.get(0);
        fillPile();
        playerPileSetup();
        //TODO: logic that a players turn is starting
    }


}
