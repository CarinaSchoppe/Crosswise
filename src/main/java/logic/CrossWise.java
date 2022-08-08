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
    public static boolean UI = true;
    public static final int DELAY = 10;


    public static void main(String[] args) {
        launch(args);
    }

    /*
     * 3 TODO: drag and drop having image in it (optional)
     *
     * ---TODO's for jacob only:
     * 3 TODO: drag and drop highlight
     * 3 TODO: colors of background and create game background
     * 2 TODO: fix is preventing loss in big chooser method if both are preventing loss
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
