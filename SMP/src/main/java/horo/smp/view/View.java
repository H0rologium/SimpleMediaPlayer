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
    private int windowTypeID;

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    public void setWindowIcon(String resourceName) {
        this.icon = new Image("/"+resourceName+".png");
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void  setWindowTypeID(int windowTypeID) {
        this.windowTypeID = windowTypeID;
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
            primaryStage.getScene().getStylesheets().add(getClass().getResource(getWindowStyleSheet(this.windowTypeID)).toExternalForm());
            primaryStage.setResizable(true);
            primaryStage.getIcons().add(this.icon);
            primaryStage.show();

        } catch (Exception e) {
            Log.Fatal(this,e.toString());
        }
    }

    private String getWindowStyleSheet(int windowID)
    {
        String retPath;
        switch(windowID)
        {
            case 0:
                retPath = "settings_style.css";
                break;
            case 1:
                retPath = "mediaplayer_style.css";
                break;
            case 2:
                retPath = "imageview_style.css";
                break;
            default:
                retPath = "blank.css";
                break;
        }

        return "/"+retPath;
    }
}