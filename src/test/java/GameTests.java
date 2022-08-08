import gui.FileInputReader;
import logic.CrossWise;
import logic.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class GameTests {

    @BeforeEach
    void setup() {
        CrossWise.UI = false;
        if (Game.getGame() != null)
            Game.getGame().cancel();
    }

    @Test
    void gameTest1() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswise.json"), new FakeGUI());
        Assertions.assertDoesNotThrow( () -> Game.getGame().testStart(false));
    }

    @Test
    void gameTest2() {
        Game.createNewGame(List.of("test1", "test2", "test3", "test4"), List.of(true, true, true, true), List.of(true, false, true, true), new FakeGUI(), false, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Game.getGame().testStart(false));
    }

    @Test
    void gameTest3() {
        Game.createNewGame(List.of("test1", "test2", "test3", "test4"), List.of(true, true, true, true), List.of(false, false, false, true), new FakeGUI(), false, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Game.getGame().testStart(false));
    }

    @Test
    void gameTest4() {
        Game.createNewGame(List.of("test1", "test2", "test3", "test4"), List.of(true, true, true, true), List.of(false, false, false, false), new FakeGUI(), false, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Game.getGame().testStart(false));
    }

    @Test
    void gameTest5() {
        Game.createNewGame(List.of("test1", "test2", "test3", "test4"), List.of(true, true, true, true), List.of(false, true, false, true), new FakeGUI(), false, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Game.getGame().testStart(false));
    }
}
