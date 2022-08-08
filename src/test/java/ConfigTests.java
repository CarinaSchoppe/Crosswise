import gui.FileInputReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

class ConfigTests {


    @Test
    void configValidTest() {
        var configFolder = new File("src/test/resources/configs/good");
        var files = new ArrayList<File>();
        Collections.addAll(files, Objects.requireNonNull(configFolder.listFiles()));
        for (var config : files) {
            Assertions.assertDoesNotThrow(() -> FileInputReader.readFile(config, new FakeGUI()));
        }
    }

    @Test
    void configInvalidTest() {
        var configFolder = new File("src/test/resources/configs/bad");
        var files = new ArrayList<File>();
        Collections.addAll(files, Objects.requireNonNull(configFolder.listFiles()));
        for (var config : files) {
            Assertions.assertThrows(RuntimeException.class, () -> FileInputReader.readFile(config, new FakeGUI()));
        }
    }
}
