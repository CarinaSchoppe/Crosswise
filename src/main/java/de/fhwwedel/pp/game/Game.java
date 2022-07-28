/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The Latest changes made by Carina on 7/27/22, 11:22 AM All contents of "Game" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.game;

import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.exceptions.NoTokenException;
import de.fhwwedel.pp.util.game.AnimationTime;
import de.fhwwedel.pp.util.game.Token;
import de.fhwwedel.pp.util.game.TokenType;
import de.fhwwedel.pp.util.special.GameLogger;

import java.util.ArrayList;

public class Game {

    private static Game game;
    private final PlayingField playingField;
    private final AnimationTime animationTime = AnimationTime.MIDDLE;
    private final ArrayList<Player> players;
    private final ArrayList<Token> usedSpecialTokens = new ArrayList<>();
    private final ArrayList<Token> tokenDrawPile = new ArrayList<>();
    private GameLogic gameLogic;
    private Player currentPlayer = null;

    public Game(PlayingField playingField, ArrayList<Player> players) {
        this.playingField = playingField;
        this.players = players;
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        Game.game = game;
    }

    private void fillPile() {
        for (var token : TokenType.values()) {
            if (token == TokenType.None) continue;
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

    public void setup(boolean fileLoaded) {
        gameLogic = new GameLogic(this);
        handleOver();
        if (players.size() < 2)
            throw new IllegalArgumentException("There must be at least 2 players");
        fillPile();
        if (!fileLoaded) {
            currentPlayer = players.get(0);
            playerPileSetup();
        }
    }

    private void nextPlayer() {
        if (currentPlayer == null && !players.isEmpty()) {
            currentPlayer = players.get(0);
        } else if (currentPlayer != null && !players.isEmpty()) {
            int index = players.indexOf(currentPlayer);
            if (index == players.size() - 1) {
                currentPlayer = players.get(0);
            } else {
                currentPlayer = players.get(index + 1);
            }
        } else {
            currentPlayer = null;
            handleOver();
            return;
        }

        System.out.println("Current player is: " + currentPlayer.getName() + " with ID: " + currentPlayer.getPlayerID());
        if (currentPlayer instanceof AI ai) {
            ai.makeMove();
        } else {
            currentPlayer.notifyTurn();
        }
    }

    private boolean handleOver() {
        var over = gameLogic.isGameOver(playingField);
        if (players.isEmpty()) {
            System.out.println("No players left!");
            GameLogger.saveLogToFile("Logfile.txt");
            return true;
        } else if (over.containsKey(true)) {
            var team = over.get(true);

            //noinspection StatementWithEmptyBody
            if (team == null) {
                System.out.println("Game is over, but no team has won!");
                //TODO: Handle game over! GUI stuff
            } else {
                System.out.println("Game is over, team " + team + " has won!");
            }
            GameLogger.saveLogToFile("Logfile.txt");
            return true;
        }
        return false;
    }

    public void start() {
        if (currentPlayer instanceof AI ai) {
            ai.makeMove();
        } else {
            currentPlayer.notifyTurn();
        }
    }

    public void turnDone() {

        if (handleOver()) {
            return;
        }
        try {
            currentPlayer.drawToken();
        } catch (NoTokenException e) {
            System.out.println("No more tokens left in the Pile!");
        }
        nextPlayer();
    }

    public PlayingField getPlayingField() {
        return playingField;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Token> getUsedActionTokens() {
        return usedSpecialTokens;
    }

    public ArrayList<Token> getTokenDrawPile() {
        return tokenDrawPile;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public AnimationTime getAnimationTime() {
        return animationTime;
    }
}
