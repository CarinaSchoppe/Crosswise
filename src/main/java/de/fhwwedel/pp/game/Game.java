/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The latest changes made by Carina on 7/27/22, 11:22 AM All contents of "Game" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.game;

import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.gui.GameWindow;
import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.exceptions.NoTokenException;
import de.fhwwedel.pp.util.game.AnimationTime;
import de.fhwwedel.pp.util.game.Team;
import de.fhwwedel.pp.util.game.Token;
import de.fhwwedel.pp.util.game.TokenType;
import de.fhwwedel.pp.util.special.Constants;
import de.fhwwedel.pp.util.special.GameLogger;
import javafx.scene.control.Alert;

import java.util.ArrayList;

public class Game {

    private static Game game;
    private final PlayingField playingField;
    private AnimationTime animationTime = AnimationTime.MIDDLE;
    private final ArrayList<Player> players;
    private final ArrayList<Token> usedSpecialTokens = new ArrayList<>();
    private ArrayList<Token> tokenDrawPile = new ArrayList<>();
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


    public boolean teamSizeEqual() {
        if (Team.getHorizontalTeam().getPlayers().size() == Team.getVerticalTeam().getPlayers().size()) {
            return true;
        }
        if (GameWindow.getGameWindow() == null) {
            throw new RuntimeException("Window is null");
        }

        var alert = new Alert(Alert.AlertType.INFORMATION, "The game that should be loaded is not allowed to be loaded!");
        alert.setTitle("Wrong configuration");
        alert.setHeaderText("Wrong configuration!");
        alert.showAndWait();
        return false;
    }

    private void fillPile() {
        this.tokenDrawPile = new ArrayList<>();
        for (var token : TokenType.values()) {
            if (token == TokenType.None) continue;
            if (token.isSpecial()) {
                for (int i = 0; i < Constants.AMOUNT_ACTION_TOKENS; i++) { //12 tokens
                    tokenDrawPile.add(new Token(token));
                }
            } else {
                for (int i = 0; i < Constants.AMOUNT_NORMAL_TOKENS; i++) { //42     54-36-8-12
                    tokenDrawPile.add(new Token(token));
                }
            }
        }
    }

    private void playerPileSetup() {
        for (Player player : players) {
            for (int i = 0; i < Constants.HAND_SIZE; i++) {
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
        if (players.size() < Constants.MIN_PLAYER_SIZE)
            throw new IllegalArgumentException("There must be at least 2 players");
        fillPile();
        if (!fileLoaded) {
            currentPlayer = players.get(0);
            playerPileSetup();
        }
    }

    private void nextPlayer() {
        var players = this.players.stream().filter(Player::isActive).toList();
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


        if (GameWindow.getGameWindow() != null)
            GameWindow.getGameWindow().getCurrentPlayerText().setText(currentPlayer.getName());
        System.out.println("Current player is: " + currentPlayer.getName() + " with ID: " + currentPlayer.getPlayerID());
        if (currentPlayer instanceof AI ai) {
            ai.makeMove();
        } else {
            GameWindow.notifyTurn(currentPlayer);
        }
    }

    private boolean handleOver() {
        var over = gameLogic.isGameOver(playingField);
        if (players.isEmpty()) {
            System.out.println("No players left!");
            GameLogger.saveLogToFile("Logfile");
            return true;
        } else if (over.containsKey(true)) {
            var team = over.get(true);

            //noinspection StatementWithEmptyBody
            if (team == null) {
                System.out.println("Game is over, but no team has won!");
                //TODO: Handle game over! GUI stuff
            } else {
                System.out.println(Team.getVerticalTeam().getPoints() + " " + Team.getHorizontalTeam().getPoints());
                System.out.println("Game is over, team " + team.getTeamType().getTeamName() + " has won!");
            }
            GameLogger.saveLogToFile("Logfile");
            return true;
        }
        return false;
    }

    public void start() {
        if (!teamSizeEqual()) {
            return;
        }
        if (currentPlayer instanceof AI ai) {
            ai.makeMove();
        } else {
            GameWindow.notifyTurn(currentPlayer);
        }
    }

    public void turnDone() {
        Team.givePoints();
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

    public void setAnimationTime(AnimationTime animationTime) {
        this.animationTime = animationTime;
    }
}
