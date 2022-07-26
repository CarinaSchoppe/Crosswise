package de.fhwWedel.pp.player;

import de.fhwWedel.pp.game.Game;
import de.fhwWedel.pp.util.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;


@Data
public class Player {

    private final HashSet<Token> tokens = new HashSet<>();

    private final int playerID;
    private final Team team;
    private final boolean isActive;
    private final String name;

    public boolean performNormalTurn(@NotNull final Token token, @NotNull final Position position) {
        if (!tokens.contains(token))
            return false;

        //get the position on the PlayingField corresponding to the given position
        //check if the position on the PlayingField is empty
        //if so, set the token on the position and return true
        //else return false
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

}
