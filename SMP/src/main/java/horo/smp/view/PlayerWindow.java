package horo.smp.view;

public class PlayerWindow extends View {
    private final String title = "Media Player";

    public PlayerWindow()
    {
        this.setWindowTypeID(1);
        this.setResourceName("fxml_media.fxml");
        this.setWindowIcon("icon");
        this.setTitle(title);
    }

}
