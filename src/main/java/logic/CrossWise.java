package logic;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class CrossWise extends Application {


    public static final boolean DEBUG = true;
    public static long time;
    public static final boolean UI = true;
    public static int DELAY = 10;


    public static void main(String[] args) {
        launch(args);
    }

    /*
     * TODO: Structure of the program!!!
     * TODO: Responsive Design
     * TODO: GUI Points
     * TODO end timer event premature
     * TODO fakeGUI
     * TODO Create players via createGame method and not in the createGame class
     * TODO last token not responsive?
     * TODO border pane from empty token doesnt get removed
     * TODO border pane to special tokens
     *  ----kleinere sachen
     * TODO disable GUI elements while AI move
     * TODO colors of background and create game background0
     * TODO spieler hand creation in 1 methode?
     * TODO change opacity of special tokens when used up
     * TODO cant pick replacer, if no symbol tokens on hand
     * TODO cant pick swapper if less than 2 tokens on field
     * */

    @Override
    public void start(Stage primaryStage) throws IOException {
        Platform.setImplicitExit(false);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/GameWindow.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1280, 1024);
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
