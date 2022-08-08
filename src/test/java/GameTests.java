import gui.FileInputReader;
import logic.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class GameTests {

    @BeforeEach
    void setup() {
        if (Game.getGame() != null)
            Game.getGame().cancel();
    }

    @Test
    void gameTest1() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswise.json"), new FakeGUI());
        Assertions.assertDoesNotThrow(Game.getGame()::start);
    }

    @Test
    void gameTest2() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswise1.json"), new FakeGUI());
        Assertions.assertDoesNotThrow(Game.getGame()::start);
    }

    @Test
    void gameTest3() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswise2.json"), new FakeGUI());
        Assertions.assertDoesNotThrow(Game.getGame()::start);
    }

    @Test
    void gameTest4() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswise3.json"), new FakeGUI());
        Assertions.assertDoesNotThrow(Game.getGame()::start);
    }

    @Test
    void gameTest5() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswise4.json"), new FakeGUI());
        Assertions.assertDoesNotThrow(Game.getGame()::start);
    }
}
