import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class MainView extends Application {
    public void start(Stage stage) throws Exception {
        // Load the FXML file.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ImageView.fxml"));
        Parent parent = loader.load();
        MainViewController controller = loader.getController();
        controller.setMyStage(stage);

        // Build the scene graph.
        Scene scene = new Scene(parent);
        controller.setUpAlbum();

        // Display our window, using the scene graph.
        stage.setTitle("ImageViewDemo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Launch the application.
        launch(args);
    }
}
