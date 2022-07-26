package de.fhwWedel.pp.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class that starts our application.
 *
 * @author mjo
 */
public class FrontEnd extends Application {

    /**
     * Main method
     *
     * @param args unused
     */
    public static void main(String... args) {
        launch(args);
    }

    /**
     * Creating the stage and showing it. This is where the initial size and the
     * title of the window are set.
     *
     * @param primaryStage the stage to be shown
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/gui/UserInterface.fxml"));

        //UserInterfaceController controller = new UserInterfaceController();
        //controller.initialize(null, null);
        int v = 1280;
        int v1 = 1024;

        Parent root = fxmlLoader.load();
        primaryStage.setResizable(true);
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(new Scene(root, v, v1));
        primaryStage.show();

    }
}
