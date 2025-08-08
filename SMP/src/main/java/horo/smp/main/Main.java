package horo.smp.main;
import horo.smp.config.Log;
import horo.smp.controller.PlayerController;
import horo.smp.view.PlayerWindow;
import horo.smp.view.SettingsWindow;
import horo.smp.config.Config;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static Config cfg;
    private static List<String> clArgs;
    public static File inputFile;
    //0 - Settings, 1 - Video Player, 2 - Image Viewer, 3 - Music player
    private static int openingMenu;

    public static void main(String[] args) {
        cfg = new Config();
        clArgs = List.of(args);
        if (clArgs.isEmpty()) {
            openingMenu = 0;
            launch();
            return;
        }
        File f = new File(clArgs.get(0));
        if (!f.canRead()) {
            Log.Error(null,"Can't read file: " + f.getPath());
            return;
        };
        inputFile = f;
        openingMenu = 1;
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Open settings if program is directly ran, otherwise open media as passed by windows
        switch(openingMenu) {
            case 0:
                SettingsWindow stW = new SettingsWindow();
                stW.start(stage);
                return;
            case 1:
                PlayerWindow mpW = new PlayerWindow();
                mpW.start(stage);
                return;
            case 2:
            case 3:
                Log.Information(this,"Currently unimplemented");
                return;
            default:
                Log.Warning(this,"Stage could not be built with provided opening menu value. defaulting to settings");
                SettingsWindow bstW = new SettingsWindow();
                bstW.start(stage);
        }


    }
}