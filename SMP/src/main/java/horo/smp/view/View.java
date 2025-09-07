package horo.smp.view;
import horo.smp.config.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import static horo.smp.main.Main.getConfig;

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

            primaryStage.setOnCloseRequest(event -> {
                if (!primaryStage.isFullScreen()) getConfig().setConfigValue(new double[]{primaryStage.getWidth(),primaryStage.getHeight()},"lastWindowDimension");
                getConfig().saveConfig();
            });

            //primaryStage.getScene().getWindow().setWidth(getConfig().getWinDimensions()[0]);
            //primaryStage.getScene().getWindow().setHeight(getConfig().getWinDimensions()[1]);
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
            case 2:
                retPath = "mediaplayer_style.css";
                break;
            default:
                retPath = "blank.css";
                break;
        }

        return "/"+retPath;
    }
}