import logic.Constants;
import logic.Game;
import logic.PlayingField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class AI {


    @BeforeEach
    public void createGame() {
        Game.createNewGame(new ArrayList<>(List.of(new logic.AI(0, true, "AI1"), new logic.AI(1, true, "AI2"), new logic.AI(2, true, "AI3"), new logic.AI(3, true, "AI4"))), new FakeGUI(), false, new PlayingField(Constants.GAMEGRID_SIZE));

    }

    @Test
    public void testAI() {
        Assertions.assertDoesNotThrow(Game.getGame()::activate);
    }


}
