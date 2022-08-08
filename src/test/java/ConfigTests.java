import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

class ConfigTests {


    @Test
    void configValidTest() {

        var configFolder = new File("configs/good/");
        var files = new ArrayList<File>();
        Collections.addAll(files, configFolder.listFiles());


    }
}
