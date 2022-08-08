import logic.Player;
import logic.Token;
import logic.util.TokenType;
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

        player.addTokenToHand(new Token(TokenType.REMOVER));
        player.addTokenToHand(new Token(TokenType.SUN));
        player.addTokenToHand(new Token(TokenType.PENTAGON));
        player.addTokenToHand(new Token(TokenType.CROSS));

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

        player.addTokenToHand(new Token(TokenType.SUN));
        player.addTokenToHand(new Token(TokenType.SUN));
        player.addTokenToHand(new Token(TokenType.SUN));
        player.addTokenToHand(new Token(TokenType.SUN));

        var player1 = new Player(0, true, "test");
        player1.create();

        player1.addTokenToHand(new Token(TokenType.SUN));
        player1.addTokenToHand(new Token(TokenType.SUN));
        player1.addTokenToHand(new Token(TokenType.SUN));

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
