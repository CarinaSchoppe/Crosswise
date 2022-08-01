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

import de.fhwwedel.pp.CrossWise;
import de.fhwwedel.pp.ai.AI;
import de.fhwwedel.pp.gui.GameWindow;
import de.fhwwedel.pp.gui.GameWindowHandler;
import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.exceptions.NoTokenException;
import de.fhwwedel.pp.util.game.AnimationTime;
import de.fhwwedel.pp.util.game.Team;
import de.fhwwedel.pp.util.game.Token;
import de.fhwwedel.pp.util.game.TokenType;
import de.fhwwedel.pp.util.special.Constants;
import de.fhwwedel.pp.util.special.GameLogger;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.ArrayList;

/**
 * Class for a game instance of the game Crosswise
 *
 * @author Jacob Klövekorn
 */
public class Game {
    /**
     * Static attribut for the current running game
     */
    private static Game game;
    /**
     * Playing field of the game
     */
    private final PlayingField playingField;
    /**
     * Time for an animation of an AI turn
     */
    private AnimationTime animationTime = AnimationTime.MIDDLE;
    /**
     * Players, that are playing this game round
     */
    private final ArrayList<Player> players;
    /**
     * Array of amount of used special tokens
     */
    private final ArrayList<Token> usedSpecialTokens = new ArrayList<>();
    /**
     * Draw pile of Tokens the players can draw from
     */
    private ArrayList<Token> tokenDrawPile = new ArrayList<>();
    /**
     * Instance of the GameLogic running for this game
     */
    private GameLogic gameLogic;
    /**
     * Current player
     */
    private Player currentPlayer = null;

    //----------------------------------------------------------------------------------------------

    public Game(PlayingField playingField, ArrayList<Player> players) {
        this.playingField = playingField;
        this.players = players;
    }

    /**
     * Tests if the Teams have the same amount of active players
     *
     * @return true, if they have the same amount, otherwise false
     */
    public boolean teamSizeEqual() {
        if (Team.getHorizontalTeam().getPlayers().size() == Team.getVerticalTeam().getPlayers().size()) {
            return true;
        }
        //Exception, if the game got created with no corresponding GameWindow
        if (GameWindow.getGameWindow() == null) {
            throw new RuntimeException("Window is null");
        }

        var alert = new Alert(Alert.AlertType.INFORMATION, "The game that should be loaded is not allowed to be loaded!");
        alert.setTitle("Wrong configuration");
        alert.setHeaderText("Wrong configuration!");
        alert.showAndWait();
        return false;
    }

    /**
     * Creates a new DrawPile and fills it with tokens
     */
    private void fillPile() {
        this.tokenDrawPile = new ArrayList<>();
        for (var token : TokenType.values()) {
            if (token == TokenType.NONE) continue;
            if (token.isSpecial()) {
                for (int i = 0; i < Constants.AMOUNT_ACTION_TOKENS; i++) { //12 tokens
                    tokenDrawPile.add(new Token(token));
                }
            } else {
                for (int i = 0; i < Constants.AMOUNT_NORMAL_TOKENS; i++) { //42 tokens
                    tokenDrawPile.add(new Token(token));
                }
            }
        }
        for (int i = 0; i < tokenDrawPile.size(); i++) {
            int randomIndex = (int) (Math.random() * tokenDrawPile.size());
            var temp = tokenDrawPile.get(i);
            tokenDrawPile.set(i, tokenDrawPile.get(randomIndex));
            tokenDrawPile.set(randomIndex, temp);
        }
    }

    /**
     * Makes every Player draw Tokens, until their hands are full
     */
    private void playerPileSetup() {
        for (Player player : players) {
            if (!player.isActive()) continue;
            for (int i = 0; i < Constants.HAND_SIZE; i++) {
                try {
                    player.drawToken();
                } catch (NoTokenException e) {
                    throw new RuntimeException("No more tokens left in the Pile while startup! "
                            + "Configuration error!");
                }
            }
        }
    }

    /**
     * Creates new gameLogic, creates a drawPile and makes all players draw tokens, until their
     * hands are full. If the game is loaded from a file, skip the creating and drawing
     *
     * @param fileLoaded was the game loaded from a file
     */
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

    /**
     * Computes logic for having the next players turn
     */
    private void nextPlayer() {
        //gets all active players
        var players = this.players.stream().filter(Player::isActive).toList();
        //puts the first ever to play player to the one with the ID: 0
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
            //handler for the fact that the game is over
            currentPlayer = null;
            handleOver();
            return;
        }

        if (GameWindowHandler.getGameWindowHandler() != null) {
            GameWindowHandler.getGameWindowHandler().showHand(this);
        }
        if (GameWindow.getGameWindow() != null) {
            Platform.runLater(() -> GameWindow.getGameWindow().getCurrentPlayerText().setText(currentPlayer.getName()));
        }
        System.out.println("Current player is: " + currentPlayer.getName() + " with ID: " + currentPlayer.getPlayerID());
        //if the player is an AI player, let the AI make their move
        if (currentPlayer instanceof AI ai) {
            ai.makeMove();
        } else {
            GameWindow.notifyTurn(currentPlayer);
        }
    }

    /**
     * Computes logic for the ending of a game
     *
     * @return true, if the game is over
     */
    private boolean handleOver() {
        var over = gameLogic.isGameOver(playingField);
        if (players.isEmpty()) {
            System.out.println("No players left!");
            GameLogger.saveLogToFile("Logfile");
            return true;
        } else if (over.containsKey(true)) {
            var team = over.get(true);
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

    /**
     * Starts the Game
     */
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

    /**
     * Computes logic for turns, that are over
     */
    public void turnDone() {
        Team.givePoints();
        if (GameWindowHandler.getGameWindowHandler() != null)
            GameWindowHandler.getGameWindowHandler().performMoveUIUpdate(players, playingField);
        //if the turn is over, do nothing
        if (handleOver()) {
            return;
        }
        //otherwise try to draw a token
        try {
            currentPlayer.drawToken();
        } catch (NoTokenException e) {
            System.out.println("No more tokens left in the Pile!");
        }
        try {
            if (CrossWise.slow)
                Thread.sleep(CrossWise.delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Let the next player do their turn
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

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        Game.game = game;
    }

    public void setAnimationTime(AnimationTime animationTime) {
        this.animationTime = animationTime;
    }
}
