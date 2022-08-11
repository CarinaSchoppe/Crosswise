/*
 * Copyright Notice for CrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:29 PM by Carina The Latest changes made by Carina on 8/9/22, 12:51 PM All contents of "ConfigTests" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

import me.carinasophie.crosswise.util.filehandle.FileInputReader;
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
