/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 12:10 PM by Carina The Latest changes made by Carina on 7/27/22, 11:49 AM All contents of "FileInputReader" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwWedel.pp.util.special;

import com.google.gson.Gson;
import de.fhwWedel.pp.game.Game;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileInputReader {

    private static File selectFile(@NotNull Scene scene) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        return chooser.showOpenDialog(scene.getWindow());
    }

    public static void readFile(@NotNull File file) throws FileNotFoundException {
        //read in the lines from file
        FileReader reader = new FileReader(file);
        Gson gson = new Gson();
        gson.fromJson(reader, Game.class);
    }
}
