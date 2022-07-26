/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "CrossWise" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Application class to set the scene for the user
 */
public class CrossWise extends Application {
    /**
     * Debug Constant - activate to get System out messages
     */
    public static final boolean DEBUG = false;
    /**
     * Constant for the time on how long the program takes to run from start to finish
     */
    public static long time;
    /**
     * Variable to toggle the use of the UI and trigger exception for the tests
     */
    public static boolean UI = true;
    /**
     * Delay between turns
     */
    public static final int DELAY = 10;
    /**
     * GameSize of window
     */
    public static double v = 1280;
    /**
     * GameSize of window
     */
    public static double v1 = 1024;
    /**
     * Main method, with is called by the Main class
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        launch(args);
    }

    /*
     * TODO: Tests, lines, more special tokens
     * TODO checkstyle
     * */

    @Override
    public void start(Stage primaryStage) throws IOException {
        Platform.setImplicitExit(false);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/GameWindow.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, v, v1);
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
