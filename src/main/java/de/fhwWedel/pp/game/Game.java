package de.fhwWedel.pp.game;

import de.fhwWedel.pp.player.Player;
import de.fhwWedel.pp.util.NoTokenException;
import de.fhwWedel.pp.util.Token;
import de.fhwWedel.pp.util.TokenType;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Game {

    private static Game game;

    private final PlayingField playingField;
    private final ArrayList<Player> players;
    private final ArrayList<Token> usedSpecialTokens = new ArrayList<>();
    private final ArrayList<Token> tokenDrawPile = new ArrayList<>();
    private Player currentPlayer = null;

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        Game.game = game;
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

    public void nextPlayer() {
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
    }

    public void start() {
        if (players.size() < 2)
            throw new IllegalArgumentException("There must be at least 2 players");
        setCurrentPlayer(players.get(0));
        fillPile();
        playerPileSetup();
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
}
