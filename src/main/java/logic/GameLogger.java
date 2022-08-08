package logic;

import logic.util.Action;
import logic.util.Constants;
import logic.util.Position;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Logger class, which writes a log entry after every Turn
 *
 * @author Jacob Klövekorn
 */
public class GameLogger {
    /**
     * Constructor, ignored here
     */
    private GameLogger() {
    }

    private static final ArrayList<String> logMessages = new ArrayList<>();

    private static void log(String logMessage) {
        //get the current time and date
        if (CrossWise.DEBUG)
            System.out.println(logMessage);
        logMessages.add(new java.util.Date() + ": " + logMessage);
        saveLogToFile();
    }

    public static void logMove(Player player, Token placed, Position to) {
        String builder = "Player: \"" + player.getName() + "\" and ID: \"" + player.getPlayerID() + "\" " + Action.PLACE.getText() + ": " + placed.tokenType().getValue() + " on: " + to.toString() + " new Hand: " + player.handRepresentation() +
                "\n" +
                Game.getGame().getPlayingField().toString();
        log(builder);
    }

    public static void logMoveMove(Player player, Position from, Position to) {
        String builder = "Player: \"" + player.getName() + "\" and ID: \"" + player.getPlayerID() + "\" " + Action.MOVED.getText() + " from: " + from.toStringWithToken() + " to: " + to.toString() + " new Hand: " + player.handRepresentation() +
                "\n" +
                Game.getGame().getPlayingField().toString();
        log(builder);
    }

    public static void logMoveRemove(Player player, Position from) {
        String builder = "Player: \"" + player.getName() + "\" and ID: \"" + player.getPlayerID() + "\" " + Action.REMOVE.getText() + " Token: " + from.getToken().tokenType().getValue() + " from: " + from + " new Hand: " + player.handRepresentation() +
                "\n" +
                Game.getGame().getPlayingField().toString();
        log(builder);
    }

    public static void logMoveSwapper(Player player, Position start, Position to) {
        String builder = "Player: \"" + player.getName() + "\" and ID: \"" + player.getPlayerID() + "\" " + Action.SWAPPED.getText() + ": " + start.toStringWithToken() + " with " + to.toStringWithToken() + " new Hand: " + player.handRepresentation() +
                "\n" +
                Game.getGame().getPlayingField().toString();
        log(builder);
    }

    public static void logMoveReplacer(Player player, Position start, Token with) {
        String builder = "Player: \"" + player.getName() + "\" and ID: \"" + player.getPlayerID() + "\" " + Action.REPLACED.getText() + ": " + start.toStringWithToken() + " with value: " + with.tokenType().getValue() + " new Hand: " + player.handRepresentation() +
                "\n" +
                Game.getGame().getPlayingField().toString();
        log(builder);
    }

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

    public static void logGameSetupLog() {
        var builder = new StringBuilder();
        builder.append("Game Setup: \n");
        for (var player : Game.getGame().getPlayers())
            builder.append(player.toString()).append("\n");
        log(builder.toString());
    }

    public static void logDraw(Player player, Token token) {
        String builder = "Player \"" + player.getName() + "\" and ID: \"" + player.getPlayerID() + "\" draws: \"" + token.tokenType().getValue() + "\" new Hand: " + player.handRepresentation() + "\n";
        log(builder);
    }
}
