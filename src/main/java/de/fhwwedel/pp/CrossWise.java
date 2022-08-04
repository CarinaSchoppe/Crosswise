/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The Latest changes made by Carina on 7/27/22, 11:07 AM All contents of "CrossWise" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Wrapper class is necessary as the main class for our program must not inherit
 * from {@link javafx.application.Application}
 *
 * @author mjo
 */
public class CrossWise extends Application {

    //TODO: hands buggy not really showing
    // TODO: hands on start remove


    public static final boolean DEBUG = true;
    public static long time;
    public static final boolean UI = true;
    public static int DELAY = 200;


    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Platform.setImplicitExit(false);

        //TODO: generateGrid();
        //TODO: setupDragAndDropEvent();


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/GameWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 1024);
        primaryStage.setTitle("Crosswise");
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.onCloseRequestProperty().setValue(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();


    }


}
