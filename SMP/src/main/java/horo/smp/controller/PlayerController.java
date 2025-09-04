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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
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
    private VBox playerControlsVbox;

    private String GetFormattedTime(double time)
    {
        int tk = (int)time;
        return ((tk/3600 > 9) ? "" : "0")+tk/3600+":"+((tk%3600/60 > 9) ? "" : "0")+tk%3600/60+":"+((tk%60 > 9) ? "" : "0")+tk%60;
    }

    @FXML
    public void initialize() {
        Log.Information(this,"Media player opening with supplied path: "+Main.inputFile.getAbsolutePath());
        this.media = new Media(Main.inputFile.toURI().toString());
        this.mediaPlayer = new MediaPlayer(this.media);
        this.videoView.setMediaPlayer(this.mediaPlayer);

        AddListeners();
        //Can't run bindings before FXML has been injected
        Platform.runLater(this::BindPlayer);

        //this.mediaPlayer.setAutoPlay(true);
        this.mediaPlayer.play();
        this.playing = true;
    }

    private void AddListeners()
    {
        this.mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            this.progressSlider.setValue(newValue.toSeconds());
            this.durationlbl.setText(GetFormattedTime(this.progressSlider.getValue()));
        });
        this.mediaPlayer.setOnReady(() -> {
            Duration d = this.media.getDuration();
            this.progressSlider.setMax(d.toSeconds());
            this.progressSlider.setValue(0);
        });
        this.progressSlider.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
           if (!newValue)
           {
               this.mediaPlayer.seek(Duration.seconds(this.progressSlider.getValue()));
               this.mediaPlayer.play();
           }
           else
           {
               this.mediaPlayer.pause();
           }
        });
    }

    private void BindPlayer()
    {
        Scene playerScene = this.videoView.getScene();
        Stage stage = (Stage) playerScene.getWindow();

        this.videoView.setPreserveRatio(true);
        VBox.setVgrow(this.videoView, Priority.ALWAYS);
        this.videoView.fitWidthProperty().bind(this.playerRootPane.widthProperty());
        this.videoView.fitHeightProperty().bind(
                this.playerRootPane.heightProperty().subtract(this.playerControlsVbox.heightProperty())
        );
        this.playerRootPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            double minHeight = (this.mediaPlayer.getMedia().getHeight()*0.2) + this.playerControlsVbox.getHeight();
            stage.setMinHeight(minHeight);
        });
        this.playerRootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            double minWidth = this.mediaPlayer.getMedia().getWidth();
            stage.setMinWidth(minWidth);
        });
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
        this.mediaPlayer.seek(Duration.seconds(this.progressSlider.getValue()));
    }
    //endregion
}
