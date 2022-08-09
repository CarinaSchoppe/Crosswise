import logic.Game.PlayingField;
import logic.ConstantsEnums.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldTest {

    @Test
    void gameFieldSizeCorrect() {

        Assertions.assertEquals(4, new PlayingField(4).getFieldMap().length);
        Assertions.assertEquals(5, new PlayingField(5).getFieldMap().length);
        Assertions.assertEquals(6, new PlayingField(6).getFieldMap().length);
        Assertions.assertEquals(7, new PlayingField(7).getFieldMap().length);
        Assertions.assertEquals(8, new PlayingField(8).getFieldMap().length);
        Assertions.assertEquals(9, new PlayingField(9).getFieldMap().length);
        Assertions.assertEquals(10, new PlayingField(10).getFieldMap().length);
        Assertions.assertEquals(11, new PlayingField(11).getFieldMap().length);


    }

    @Test
    void allowedGameFieldValues() {
        assertThrows(IllegalArgumentException.class, () -> new PlayingField(-2));
        assertThrows(IllegalArgumentException.class, () -> new PlayingField(-1));
        assertThrows(IllegalArgumentException.class, () -> new PlayingField(-3));
        assertThrows(IllegalArgumentException.class, () -> new PlayingField(0));
        assertThrows(IllegalArgumentException.class, () -> new PlayingField(1));

        assertDoesNotThrow(() -> new PlayingField(2));
        assertDoesNotThrow(() -> new PlayingField(3));
        assertDoesNotThrow(() -> new PlayingField(4));
        assertDoesNotThrow(() -> new PlayingField(5));
        assertDoesNotThrow(() -> new PlayingField(Constants.GAMEGRID_SIZE));
    }

    @Test
    void checkAllRowsSame() {
        assertTrue(allRowsEqual(new PlayingField(2)));
        assertTrue(allRowsEqual(new PlayingField(3)));
        assertTrue(allRowsEqual(new PlayingField(4)));
        assertTrue(allRowsEqual(new PlayingField(5)));
        assertTrue(allRowsEqual(new PlayingField(6)));
        assertTrue(allRowsEqual(new PlayingField(7)));
        assertTrue(allRowsEqual(new PlayingField(8)));
        assertTrue(allRowsEqual(new PlayingField(9)));
        assertTrue(allRowsEqual(new PlayingField(10)));

    }


    private boolean allRowsEqual(PlayingField field) {
        //check if all rows have the same length than the columns
        for (int i = 0; i < field.getSize(); i++) {
            if (field.getFieldMap()[i].length != field.getFieldMap().length) {
                return false;
            }
        }
        return true;
    }
}
