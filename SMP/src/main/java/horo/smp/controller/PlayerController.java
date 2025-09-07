package horo.smp.controller;
import horo.smp.config.Log;
import horo.smp.main.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


public class PlayerController {

    private MediaPlayer mediaPlayer;
    private Media media;
    private boolean playing = false;

    //region FXMLFields
    @FXML
    private Label durationlbl;

    @FXML
    private ToggleButton loopBtn;

    @FXML
    private Button skiptoEndbtn;

    @FXML
    private Button skiptoStartBtn;

    @FXML
    private ToggleButton playButton;

    @FXML
    private ToggleButton fullscreenBtn;

    @FXML
    private MediaView videoView;

    @FXML
    private BorderPane playerRootPane;

    @FXML
    private Slider progressSlider;

    @FXML
    private Slider volumeSlider;

    @FXML
    private VBox playerControlsVbox;

    @FXML
    private Button mediaOpenBtn;

    @FXML
    private Button mediaSettingsBtn;
    //endregion

    private String GetFormattedTime(double time)
    {
        int tk = (int)time;
        return ((tk/3600 > 9) ? "" : "0")+tk/3600+":"+((tk%3600/60 > 9) ? "" : "0")+tk%3600/60+":"+((tk%60 > 9) ? "" : "0")+tk%60;
    }

    @FXML
    public void initialize() {
        //Can't run bindings before FXML has been injected
        Platform.runLater(this::BindPlayer);
        if (Main.inputFile == null || Main.inputFile.getAbsolutePath().isBlank()) return;
        Log.Information(this,"Media player opening with supplied path: "+Main.inputFile.getAbsolutePath());
        startPlayer(Main.inputFile.toURI().toString());
    }

    private void startPlayer(String path)
    {
        this.media = new Media(path);
        this.mediaPlayer = new MediaPlayer(this.media);
        this.videoView.setMediaPlayer(this.mediaPlayer);


        AddListeners();

        this.volumeSlider.setValue(Main.getConfig().getVolume()*100);
        this.mediaPlayer.setVolume(Main.getConfig().getVolume());

        //this.mediaPlayer.setAutoPlay(true);
        this.mediaPlayer.play();
        this.playing = true;

        return;
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

        this.volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.mediaPlayer.setVolume(Math.round((newVal.doubleValue() / 100.0) * 100.0) / 100.0);
            Main.getConfig().setConfigValue(this.mediaPlayer.getVolume(),"volume");
        });

        this.mediaPlayer.setOnEndOfMedia(() -> {
           if (this.loopBtn.isSelected())
           {
               this.mediaPlayer.seek(Duration.ZERO);
               this.mediaPlayer.play();
           }
           else
           {
               this.playButton.setText(">");
               this.playing = false;
           }
        });
    }

    private void BindPlayer()
    {
        Scene playerScene = this.videoView.getScene();
        Stage stage = (Stage) playerScene.getWindow();

        this.playButton.setText("||");
        this.playButton.getStyleClass().add("controlbarBtn");
        this.skiptoEndbtn.getStyleClass().add("controlbarBtn");
        this.skiptoStartBtn.getStyleClass().add("controlbarBtn");
        this.mediaOpenBtn.getStyleClass().add("controlbarBtn");
        //this.mediaSettingsBtn.getStyleClass().add("controlbarBtn");
        this.loopBtn.getStyleClass().add("controlbarBtn");
        this.durationlbl.getStyleClass().add("durationCtrl");
        this.fullscreenBtn.getStyleClass().add("controlbarBtn");

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
            double minWidth = this.mediaPlayer.getMedia().getWidth() + this.playerControlsVbox.getWidth()*0.18;
            stage.setMinWidth(minWidth);
        });

    }

    //region Events
    @FXML
    void onPlayBtnPressed(ActionEvent event) {
        if (this.playing)
        {
            this.mediaPlayer.pause();
            this.playButton.setText(">");
        }
        else
        {
            if (this.mediaPlayer.getCurrentTime().greaterThanOrEqualTo(this.mediaPlayer.getMedia().getDuration())) this.mediaPlayer.seek(Duration.ZERO);
            this.mediaPlayer.play();
            this.playButton.setText("||");
        }
        this.playing = !this.playing;
    }

    @FXML
    void onDurationSliderPressed(MouseEvent event) {
        this.mediaPlayer.seek(Duration.seconds(this.progressSlider.getValue()));
    }

    @FXML
    void mediaHandleKeypress(KeyEvent event) {
        ((Node) event.getSource()).getScene().getRoot().requestFocus();
        if (event.getCode() == KeyCode.SPACE) {
            this.onPlayBtnPressed(new ActionEvent());//Works as long as this callback doesnt need the event obj
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            this.fullscreenBtn.setSelected(((Stage)((Node) event.getSource()).getScene().getWindow()).isFullScreen());
        }
    }

    @FXML
    void openFile(ActionEvent event) {
        Stage s = (Stage)((Node) event.getSource()).getScene().getWindow();
        ((Node) event.getSource()).getScene().getRoot().requestFocus();
        FileChooser fc = new FileChooser();
        fc.setTitle("Open File");
        FileChooser.ExtensionFilter videoFilter = new FileChooser.ExtensionFilter(
                "Video Files",
                "*.mp4", "*.mov", "*.avi", "*.mkv",
                "*.flv", "*.wmv", "*.webm", "*.mpeg", "*.mpg", "*.m4v"
        );
        // Fallback: all files
        FileChooser.ExtensionFilter allFilesFilter = new FileChooser.ExtensionFilter(
                "All Files", "*.*"
        );
        fc.getExtensionFilters().addAll(videoFilter, allFilesFilter);
        var file = fc.showOpenDialog(s);
        if (file != null) {
            Log.Information(this,"Selected file from ui: " + file.getAbsolutePath());

            if (this.mediaPlayer == null)
            {
                startPlayer(file.toURI().toString());
            }

            this.mediaPlayer.stop();
            this.playButton.setText("||");
            this.playing = false;
            this.mediaPlayer.dispose();

            Media md = new Media(file.toURI().toString());
            this.media = md;
            this.mediaPlayer = new MediaPlayer(md);
            this.videoView.setMediaPlayer(mediaPlayer);
            this.volumeSlider.setValue(Main.getConfig().getVolume()*100);
            this.mediaPlayer.setVolume(Main.getConfig().getVolume());
            this.AddListeners();
            this.mediaPlayer.play();
            this.playing = true;
        }
    }

    @FXML
    void openSettings(ActionEvent event) {
        //NOOP
    }

    @FXML
    void skipToEnd(ActionEvent event) {
        this.mediaPlayer.seek(this.mediaPlayer.getMedia().getDuration().subtract(Duration.millis(50)));
    }

    @FXML
    void skipToStart(ActionEvent event) {
        this.mediaPlayer.seek(Duration.ZERO);
    }

    @FXML
    void toggleFullscreen(ActionEvent event) {
        //I love javafx's design for scope of small, unimportant variables like THE SCENE
        Stage s = (Stage)((Node) event.getSource()).getScene().getWindow();
        ((Node) event.getSource()).getScene().getRoot().requestFocus();//graaah
        s.setFullScreen(!s.isFullScreen());
        this.fullscreenBtn.setSelected(s.isFullScreen());
    }

    @FXML
    void handleRepeatClick(ActionEvent event) {
        Stage s = (Stage)((Node) event.getSource()).getScene().getWindow();
        ((Node) event.getSource()).getScene().getRoot().requestFocus();
    }
    //endregion
}
