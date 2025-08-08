package horo.smp.view;
import horo.smp.config.Log;
import horo.smp.controller.PlayerController;
import horo.smp.controller.SettingsController;
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
    public void setTitle(String title) {
        this.title = title;
    }

    public static void launchApp() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/"+this.resourceName));
            Parent root = loader.load();
            Scene scene = new Scene(root);
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