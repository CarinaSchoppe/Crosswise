import logic.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerTests {


    @Test
    public void playerCreationTests() {
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

}
