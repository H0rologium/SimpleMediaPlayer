package horo.smp.controller;

import horo.smp.config.Log;
import horo.smp.main.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageController {

    //region FXMLFields
    @FXML
    private Label imageNameLbl;

    @FXML
    private BorderPane imageRootPane;

    @FXML
    private VBox playerControlsVbox;

    @FXML
    private Button imgOpenBtn;

    @FXML
    private StackPane imageStackPane;

    @FXML
    private ImageView imageView;
    //endregion
    private Image currentImg;
    private double originalWidth;
    private double originalHeight;
    private double zoomFactor = 1.0;
    private double fitScale = 1.0;
    private double mouseAnchorX;
    private double mouseAnchorY;
    private double translateAnchorX;
    private double translateAnchorY;

    @FXML
    public void initialize() {
        Log.Information(this,"Image viewer opening with supplied path: "+ Main.inputFile.getAbsolutePath());

        this.imgOpenBtn.getStyleClass().add("controlbarBtn");

        setImage();
        addListeners();
    }

    private void setImage()
    {
        this.currentImg = new Image(Main.inputFile.toURI().toString());
        this.imageView.setImage(this.currentImg);
        this.imageView.setSmooth(true);
        this.imageView.setPreserveRatio(true);

        this.originalWidth = this.currentImg.getWidth();
        this.originalHeight = this.currentImg.getHeight();
        this.imageStackPane.setPadding(new Insets(20));

        this.imageNameLbl.setText(Main.inputFile.getName());
    }

    private void addListeners()
    {
        this.imageStackPane.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            double paneWidth = newBounds.getWidth() - this.imageStackPane.getPadding().getLeft() - this.imageStackPane.getPadding().getRight();
            double paneHeight = newBounds.getHeight() - this.imageStackPane.getPadding().getTop() - this.imageStackPane.getPadding().getBottom();
            this.fitScale = Math.min(paneWidth / this.originalWidth, paneHeight / this.originalHeight);
            this.fitScale = Math.min(this.fitScale, 1.0);
            updateImageSize();
        });
    }

    private void updateImageSize() {
        double finalWidth = this.originalWidth * this.fitScale * this.zoomFactor;
        double finalHeight = this.originalHeight * this.fitScale * this.zoomFactor;

        this.imageView.setFitWidth(finalWidth);
        this.imageView.setFitHeight(finalHeight);
    }

    //region Events
    @FXML
    void openImg(ActionEvent event) {
        Stage s = (Stage)((Node) event.getSource()).getScene().getWindow();
        ((Node) event.getSource()).getScene().getRoot().requestFocus();
        FileChooser fc = new FileChooser();
        fc.setTitle("Open File");
        FileChooser.ExtensionFilter imgFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp");

        fc.getExtensionFilters().addAll(imgFilter);
        var file = fc.showOpenDialog(s);
        if (file != null) {
            Log.Information(this,"Selected file from ui: " + file.getAbsolutePath());

            this.currentImg = new Image(file.toURI().toString());
            this.imageView.setImage(this.currentImg);
            this.imageNameLbl.setText(file.getName());
        }
    }

    @FXML
    void adjustImageZoom(ScrollEvent event) {
        double delta = event.getDeltaY();
        //Steps per zoom, essentially zooming "speed"
        double zoomChange = delta > 0 ? 1.1 : 0.9;
        //They call me a mathematical wizard the way i be casting magical numbers
        this.zoomFactor *= zoomChange;
        this.zoomFactor = Math.max(0.1, Math.min(this.zoomFactor, 3.0));
        updateImageSize();
        event.consume();
    }

    @FXML
    void handleMousePressed(MouseEvent event) {
        this.mouseAnchorX = event.getSceneX();
        this.mouseAnchorY = event.getSceneY();
        this.translateAnchorX = this.imageView.getTranslateX();
        this.translateAnchorY = this.imageView.getTranslateY();
    }

    @FXML
    void handleMouseDragged(MouseEvent event) {
        double deltaX = event.getSceneX() - this.mouseAnchorX;
        double deltaY = event.getSceneY() - this.mouseAnchorY;

        this.imageView.setTranslateX(this.translateAnchorX + deltaX);
        this.imageView.setTranslateY(this.translateAnchorY + deltaY);
    }

    @FXML
    void handleMouseDoubleClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            this.zoomFactor = 1.0;
            this.imageView.setTranslateX(0);
            this.imageView.setTranslateY(0);
            updateImageSize();
        }
    }
    //endregion
}
