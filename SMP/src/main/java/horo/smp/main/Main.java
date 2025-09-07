package horo.smp.main;
import horo.smp.config.Log;
import horo.smp.view.ImageWindow;
import horo.smp.view.PlayerWindow;
import horo.smp.view.SettingsWindow;
import horo.smp.config.Config;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main extends Application {

    private static Config cfg;
    private static List<String> clArgs;
    public static File inputFile;
    //0 - Settings, 1 - Video Player, 2 - Image Viewer, 3 - Music player
    private static int openingMenu;

    public static Config getConfig()
    {
        return cfg;
    }

    public static void main(String[] args) {
        cfg = new Config();
        clArgs = List.of(args);
        File f = new File(Paths.get(clArgs.isEmpty() ? "" : clArgs.get(0)).toAbsolutePath().toUri());
        if (!f.canRead()) {
            Log.Error(null,"Can't read file: " + f.getPath());
            openingMenu = 3;
        }
        else
        {
            inputFile = f;
        }
        try{
            String mimetype = Files.probeContentType(f.toPath());
            if (mimetype != null) {
                openingMenu = mimetype.startsWith("image/") ? 2 : (mimetype.startsWith("video/") ?  1 : 3);
            }
        } catch (IOException e) {
            Log.Error(null,"Cannot determine MIME type for file: "+f.toPath());
            openingMenu = 1;
        }
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Open settings if program is directly ran, otherwise open media as passed by windows
        switch(openingMenu) {
            case 1:
                PlayerWindow mpW = new PlayerWindow();
                mpW.start(stage);
                return;
            case 2:
                ImageWindow imW = new ImageWindow();
                imW.start(stage);
                return;
            case 0:
            case 3:
                Log.Information(this,"Currently unimplemented, defaulting to media player until support for [3] or [0] is supported");
                inputFile = null;
                PlayerWindow mppW = new PlayerWindow();
                mppW.start(stage);
                return;
            default:
                Log.Warning(this,"Stage could not be built with provided opening menu value. defaulting to media");
                PlayerWindow mpppW = new PlayerWindow();
                mpppW.start(stage);
        }
    }
}