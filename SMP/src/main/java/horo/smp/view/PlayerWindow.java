package horo.smp.view;

public class PlayerWindow extends View {
    private final String title = "Media Player";

    public PlayerWindow()
    {
        this.setResourceName("fxml_media.fxml");
        this.setWindowIcon("icon");
        this.setTitle(title);
    }

}
