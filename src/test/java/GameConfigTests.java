import gui.FileInputReader;
import logic.*;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameConfigTests {


    FakeGUI gui = new FakeGUI();

    @Test
    void configValidTest() {
        File file = new File("crosswise.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(4, 5), 3, TokenType.getTokenType(2), false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        var tokenMove = ai.calculateAIMove();
        System.out.println(tokenMove);
        assertEquals(move, tokenMove);

    }

}
