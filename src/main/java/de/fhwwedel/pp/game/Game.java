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
import de.fhwwedel.pp.gui.GameWindowHandler;
import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.exceptions.NoTokenException;
import de.fhwwedel.pp.util.game.Team;
import de.fhwwedel.pp.util.game.Token;
import de.fhwwedel.pp.util.game.TokenType;
import de.fhwwedel.pp.util.special.Constants;
import de.fhwwedel.pp.util.special.GameLogger;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class for a game instance of the game Crosswise
 *
 * @author Jacob Klövekorn
 */
@SuppressWarnings("ALL")
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
     * Players, that are playing this game round
     */
    private final List<Player> players;
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

    private final GameWindowHandler gameWindowHandler;

    //----------------------------------------------------------------------------------------------

    public Game(PlayingField playingField, List<Player> players, GameWindowHandler gameWindowHandler) {
        this.playingField = playingField;
        this.players = players;
        if (gameWindowHandler == null)
            throw new IllegalArgumentException("gameWindowHandler must not be null");
        this.gameWindowHandler = gameWindowHandler;
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
            int randomIndex = new Random().nextInt(tokenDrawPile.size());
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
                    throw new NoTokenException("No more tokens left in the Pile while startup! "
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
        var allPlayers = this.players.stream().filter(Player::isActive).toList();
        //puts the first ever to play player to the one with the ID: 0
        if (currentPlayer == null && !allPlayers.isEmpty()) {
            currentPlayer = allPlayers.get(0);
        } else if (currentPlayer != null && !allPlayers.isEmpty()) {
            int index = allPlayers.indexOf(currentPlayer);
            if (index == allPlayers.size() - 1) {
                currentPlayer = allPlayers.get(0);
            } else {
                currentPlayer = allPlayers.get(index + 1);
            }
        } else {
            //handler for the fact that the game is over
            currentPlayer = null;
            handleOver();
            return;
        }

        gameWindowHandler.showHand(currentPlayer instanceof AI, currentPlayer.getPlayerID());
        gameWindowHandler.setCurrentPlayerText(currentPlayer.getName());
        System.out.println("Current player is: " + currentPlayer.getName() + " with ID: " + currentPlayer.getPlayerID());
        //if the player is an AI player, let the AI make their move
        if (currentPlayer instanceof AI ai) {
            ai.makeMove();
        } else {
            gameWindowHandler.notifyTurn(currentPlayer.getName(), currentPlayer.getPlayerID());
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
            gameWindowHandler.gameWonNotifier(null, 0, false);
            return true;
        } else if (over.containsKey(true)) {
            var team = over.get(true);
            if (team == null) {
                System.out.println("Game is over, but no team has won!");
                gameWindowHandler.gameWonNotifier(null, 0, false);
                //TODO: Handle game over! GUI stuff
            } else {
                gameWindowHandler.gameWonNotifier(team.getTeamType(), team.getPoints(), team.isRowWin());
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
            gameWindowHandler.notifyTurn(currentPlayer.getName(), currentPlayer.getPlayerID());
        }
    }

    /**
     * Computes logic for turns, that are over
     */
    public void turnDone() {
        Team.givePoints();
        gameWindowHandler.performMoveUIUpdate(players, playingField.convertToTokenTypeArray());
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
            if (CrossWise.SLOW)
                Thread.sleep(CrossWise.DELAY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        //Let the next player do their turn
        nextPlayer();
    }

    public PlayingField getPlayingField() {
        return playingField;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Token> getUsedActionTokens() {
        return usedSpecialTokens;
    }

    public List<Token> getTokenDrawPile() {
        return tokenDrawPile;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }


    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        Game.game = game;
    }

    public GameWindowHandler getGameWindowHandler() {
        return gameWindowHandler;
    }
}
