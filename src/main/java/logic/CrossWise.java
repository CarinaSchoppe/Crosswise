package logic;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class CrossWise extends Application {


    public static final boolean DEBUG = false;
    public static long time;
    public static final boolean UI = true;
    public static final int DELAY = 10;


    public static void main(String[] args) {
        launch(args);
    }

    /*
     * TODO Responsive Design
     * TODO GUI Points

     * TODO: Tests schreiben
     * TODO Create players via createGame method and not in the createGame class
     * TODO last token not responsive?
     * TODO border pane from empty token doesnt get removed
     *  ---tests
     *  ---vll
     * TODO drag and drop having image in it
     *  ---questions
     *
     * ---TODO's for jacob only:
     * TODO end timer event premature
     * TODO win text label of hori or verti ( > 100)
     * TODO disable GUI elements while AI move
     * TODO drag and drop highlight
     * TODO colors of background and create game background
     * TODO fix is preventing loss in big chooser method if both are preventing loss
     * TODO Structure of the program?
     *
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
