package horo.smp.controller;
import horo.smp.config.Log;
import horo.smp.main.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


public class PlayerController {

    private MediaPlayer mediaPlayer;
    private Media media;

    @FXML
    private Label durationlbl;

    @FXML
    private MediaView videoView;

    @FXML
    private BorderPane playerRootPane;

    @FXML
    public void initialize() {
        Log.Information(this,"Media player opening with supplied path: "+Main.inputFile.getAbsolutePath());
        this.media = new Media(Main.inputFile.toURI().toString());
        this.mediaPlayer = new MediaPlayer(this.media);

        this.videoView.setMediaPlayer(this.mediaPlayer);
        this.mediaPlayer.setAutoPlay(true);
    }

}
