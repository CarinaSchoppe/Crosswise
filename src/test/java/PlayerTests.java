/*
 * Copyright Notice for CrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:29 PM by Carina The Latest changes made by Carina on 8/9/22, 12:51 PM All contents of "PlayerTests" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

import me.carinasophie.crosswise.game.Player;
import me.carinasophie.crosswise.util.constants.Token;
import me.carinasophie.crosswise.util.constants.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTests {


    @Test
    void playerCreationTests() {
        assertThrows(IllegalArgumentException.class, () -> new Player(-5, true, "test"));
        assertThrows(IllegalArgumentException.class, () -> new Player(-4, true, "test"));
        assertThrows(IllegalArgumentException.class, () -> new Player(-3, true, "test"));
        assertThrows(IllegalArgumentException.class, () -> new Player(-2, true, "test"));
        assertThrows(IllegalArgumentException.class, () -> new Player(-1, true, "test"));


        assertDoesNotThrow(() -> new Player(0, true, "test"));
        assertDoesNotThrow(() -> new Player(1, true, "test"));
        assertDoesNotThrow(() -> new Player(2, true, "test"));
        assertDoesNotThrow(() -> new Player(3, true, "test"));

        assertThrows(IllegalArgumentException.class, () -> new Player(4, true, "test"));
        assertThrows(IllegalArgumentException.class, () -> new Player(5, true, "test"));
        assertThrows(IllegalArgumentException.class, () -> new Player(6, true, "test"));
    }


    @Test
    void hasTokenOnHandTest() {
        var player = new Player(0, true, "test");
        player.create();

        player.getHandTokens().add(new Token(TokenType.REMOVER));
        player.getHandTokens().add(new Token(TokenType.SUN));
        player.getHandTokens().add(new Token(TokenType.PENTAGON));
        player.getHandTokens().add(new Token(TokenType.CROSS));

        assertFalse(player.hasNotToken(new Token(TokenType.REMOVER)));
        assertFalse(player.hasNotToken(new Token(TokenType.SUN)));
        assertFalse(player.hasNotToken(new Token(TokenType.PENTAGON)));
        assertFalse(player.hasNotToken(new Token(TokenType.CROSS)));

        assertTrue(player.hasNotToken(new Token(TokenType.NONE)));
        assertTrue(player.hasNotToken(new Token(TokenType.TRIANGLE)));
        assertTrue(player.hasNotToken(new Token(TokenType.SQUARE)));
        assertTrue(player.hasNotToken(new Token(TokenType.STAR)));


    }

    @Test
    void tokenAmountInHandTest() {
        var player = new Player(0, true, "test");
        player.create();

        player.getHandTokens().add(new Token(TokenType.SUN));
        player.getHandTokens().add(new Token(TokenType.SUN));
        player.getHandTokens().add(new Token(TokenType.SUN));
        player.getHandTokens().add(new Token(TokenType.SUN));

        var player1 = new Player(0, true, "test");
        player1.create();

        player1.getHandTokens().add(new Token(TokenType.SUN));
        player1.getHandTokens().add(new Token(TokenType.SUN));
        player1.getHandTokens().add(new Token(TokenType.SUN));

        var player2 = new Player(0, true, "test");
        player2.create();

        player2.addTokenToHand(new Token(TokenType.SUN));
        player2.addTokenToHand(new Token(TokenType.SUN));

        var player3 = new Player(0, true, "test");
        player3.create();

        player3.addTokenToHand(new Token(TokenType.SUN));

        assertEquals(4, player.tokenAmountInHand(TokenType.SUN));
        assertEquals(3, player1.tokenAmountInHand(TokenType.SUN));
        assertEquals(2, player2.tokenAmountInHand(TokenType.SUN));
        assertEquals(1, player3.tokenAmountInHand(TokenType.SUN));
    }
}
