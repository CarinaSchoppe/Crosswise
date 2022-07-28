/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/28/22, 4:17 PM by Carina The Latest changes made by Carina on 7/28/22, 4:17 PM All contents of "Logger" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.util.special;

import de.fhwwedel.pp.game.Game;
import de.fhwwedel.pp.player.Player;
import de.fhwwedel.pp.util.game.Position;
import de.fhwwedel.pp.util.game.Token;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GameLogger {
    private static final ArrayList<String> loggMessages = new ArrayList<>();

    public static void log(String logMessage) {
        //get the current time and date
        String timeStamp = new java.util.Date().toString();
        loggMessages.add(timeStamp + logMessage);
    }

    public static void logMove(Player player, Token placed, Position to, Action action) {
        String builder = "Player " + player.getName() + " " + action.getAction() + " " + placed.getTokenType().getValue() + " on " + to.toString() + " new Hand " + player.handRepresentation() +
                "\n" +
                Game.getGame().getPlayingField().toString();
        log(builder);
    }

    public static void saveLogToFile(String fileName) {
        var file = new File(fileName);
        try {
            var writer = new FileWriter(file);
            for (var logMessage : loggMessages) {
                writer.write(logMessage + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
