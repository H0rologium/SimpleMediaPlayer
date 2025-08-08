package horo.smp.main;
import horo.smp.view.SettingsWindow;
import horo.smp.config.Config;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static Config cfg;
    private static List<String> clArgs;

    public static void main(String[] args) {
        cfg = new Config();
        clArgs = List.of(args);
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Open settings if program is directly ran, otherwise open media as passed by windows
        if (clArgs.isEmpty()) {
            SettingsWindow stW = new SettingsWindow();
            stW.start(stage);
        }

    }
}