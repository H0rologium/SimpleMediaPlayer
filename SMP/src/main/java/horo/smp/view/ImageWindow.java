package horo.smp.view;

public class ImageWindow extends View {
    private final String title = "Image View";

    public ImageWindow()
    {
        this.setWindowTypeID(2);
        this.setResourceName("fxml_image.fxml");
        this.setWindowIcon("icon");
        this.setTitle(title);
    }

}