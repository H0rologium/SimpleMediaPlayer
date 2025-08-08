package horo.smp.view;
import horo.smp.config.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public abstract class View extends Application {

    private String resourceName;
    private String title;
    private Image icon;

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    public void setWindowIcon(String resourceName) {
        this.icon = new Image("/"+resourceName+".png");
    }

    public static void launchApp() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/"+this.resourceName));

            Scene scene = new Scene(root, 400, 300); // optional size
            primaryStage.setTitle(this.title);
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.getIcons().add(this.icon);
            primaryStage.show();

        } catch (Exception e) {
            Log.Fatal(this,e.toString());
        }
    }
}