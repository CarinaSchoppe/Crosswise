/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "GameLogger" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.util;

import me.carinasophie.crosswise.CrossWise;
import me.carinasophie.crosswise.game.Game;
import me.carinasophie.crosswise.game.Player;
import me.carinasophie.crosswise.util.constants.Action;
import me.carinasophie.crosswise.util.constants.Constants;
import me.carinasophie.crosswise.util.constants.Token;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Logger class, which writes a log entry after every Turn
 *
 * @author Carina Sophie Schoppe
 */
public final class GameLogger {

    /**
     * Current log messages
     */
    private static final ArrayList<String> logMessages = new ArrayList<>();


    /**
     * Constructor, ignored here
     */
    private GameLogger() {
    }

    /**
     * write message to logfile and print out if debug is active
     *
     * @param logMessage message to be put into the log
     */
    private static void log(String logMessage) {
        //get the current time and date
        if (CrossWise.DEBUG) {
            System.out.println(logMessage);
        }
        logMessages.add(new java.util.Date() + ": " + logMessage);
        saveLogToFile();
    }

    /**
     * Logger call for the log of a normal token move
     *
     * @param player Player, who did the move
     * @param placed Token that was placed
     * @param to Position of the placed token
     */
    public static void logMove(Player player, Token placed, Position to) {
        String builder = "Player: \"" + player.getName() + "\" and ID: \"" + player.getPlayerID()
                + "\" " + Action.PLACE.getText() + ": " + placed.tokenType().getValue() + " on: "
                + to.toString() + " new Hand: " + player.handRepresentation() + "\n"
                + Game.getGame().getPlayingField().toString();
        log(builder);
    }

    /**
     * Logger call for the log or a mover move
     *
     * @param player Player, who did the move
     * @param from position from where the token was moved
     * @param to position where the token was moved
     */
    public static void logMoveMove(Player player, Position from, Position to) {
        String builder = "Player: \"" + player.getName() + "\" and ID: \""
                + player.getPlayerID() + "\" " + Action.MOVED.getText() + " from: "
                + from.toStringWithToken() + " to: " + to.toString() + " new Hand: "
                + player.handRepresentation() + "\n" + Game.getGame().getPlayingField().toString();
        log(builder);
    }

    /**
     * Logger call for a remover move
     *
     * @param player Player who did the move
     * @param from position from where the token was removed
     */
    public static void logMoveRemove(Player player, Position from) {
        String builder = "Player: \"" + player.getName() + "\" and ID: \"" + player.getPlayerID()
                + "\" " + Action.REMOVE.getText() + " Token: " + from.getToken().tokenType().
                getValue() + " from: " + from + " new Hand: " + player.handRepresentation()
                + "\n" + Game.getGame().getPlayingField().toString();
        log(builder);
    }

    /**
     * Logger call for a swapper move
     *
     * @param player Player, who did the move
     * @param start Start position of the move
     * @param to End position of the move
     */
    public static void logMoveSwapper(Player player, Position start, Position to) {
        String builder = "Player: \"" + player.getName() + "\" and ID: \"" + player.getPlayerID()
                + "\" " + Action.SWAPPED.getText() + ": " + start.toStringWithToken() + " with "
                + to.toStringWithToken() + " new Hand: " + player.handRepresentation() + "\n"
                + Game.getGame().getPlayingField().toString();
        log(builder);
    }

    /**
     * Logger call for a replacer move
     *
     * @param player Player, who did the move
     * @param start Start position of the replacer move
     * @param with swapped hand token
     */
    public static void logMoveReplacer(Player player, Position start, Token with) {
        String builder = "Player: \"" + player.getName() + "\" and ID: \"" + player.getPlayerID()
                + "\" " + Action.REPLACED.getText() + ": " + start.toStringWithToken()
                + " with value: " + with.tokenType().getValue() + " new Hand: "
                + player.handRepresentation() + "\n"
                + Game.getGame().getPlayingField().toString();
        log(builder);
    }

    /**
     * Saves the file in the case of an unexpected shutdown
     */
    private static void saveLogToFile() {
        File file = new File(Constants.LOG_FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            for (String logMessage : logMessages) {
                writer.write(logMessage + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Logger entry for the setup at the beginning of a game
     */
    public static void logGameSetupLog() {
        StringBuilder builder = new StringBuilder();
        builder.append("Game Setup: \n");
        for (Player player : Game.getGame().getPlayers()) {
            builder.append(player.toString()).append("\n");
        }
        log(builder.toString());
    }

    /**
     * Logger entry for a drawn token
     *
     * @param player Player, who drew the token
     * @param token Token, that was drawn
     */
    public static void logDraw(Player player, Token token) {
        String builder = "Player \"" + player.getName() + "\" and ID: \"" + player.getPlayerID()
                + "\" draws: \"" + token.tokenType().getValue() + "\" new Hand: "
                + player.handRepresentation() + "\n";
        log(builder);
    }
}
