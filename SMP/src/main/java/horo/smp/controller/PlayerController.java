package horo.smp.controller;
import horo.smp.config.Log;
import horo.smp.main.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;


public class PlayerController {

    private MediaPlayer mediaPlayer;
    private Media media;
    private boolean playing = false;

    @FXML
    private Label durationlbl;

    @FXML
    private ToggleButton playButton;

    @FXML
    private MediaView videoView;

    @FXML
    private BorderPane playerRootPane;

    @FXML
    private Slider progressSlider;

    @FXML
    public void initialize() {
        Log.Information(this,"Media player opening with supplied path: "+Main.inputFile.getAbsolutePath());
        this.media = new Media(Main.inputFile.toURI().toString());
        this.mediaPlayer = new MediaPlayer(this.media);
        this.videoView.setMediaPlayer(this.mediaPlayer);

        AddListeners();
        //Can't run bindings before FXML has been injected
        Platform.runLater(() -> BindPlayer());

        //this.mediaPlayer.setAutoPlay(true);
        this.mediaPlayer.play();
        this.playing = true;
    }

    private void AddListeners()
    {
        this.mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            this.progressSlider.setValue(newValue.toSeconds());
            this.durationlbl.setText("%X".formatted((int)this.progressSlider.getValue()));
        });
        this.mediaPlayer.setOnReady(() -> {
            Duration d = this.media.getDuration();
            this.progressSlider.setMax(d.toSeconds());
            this.progressSlider.setValue(0);
        });
        return;
    }

    private void BindPlayer()
    {
        Scene player = this.videoView.getScene();

        this.videoView.fitHeightProperty().bind(player.heightProperty());
        this.videoView.fitWidthProperty().bind(player.widthProperty());
        return;
    }

    //region Events
    @FXML
    void onPlayBtnPressed(ActionEvent event) {
        if (this.playing)
        {
            this.mediaPlayer.pause();
        }
        else
        {
            this.mediaPlayer.play();
        }
        this.playing = !this.playing;
    }

    @FXML
    void onDurationSliderPressed(MouseEvent event) {
        this.mediaPlayer.seek(Duration.seconds(progressSlider.getValue()));
    }
    //endregion
}
