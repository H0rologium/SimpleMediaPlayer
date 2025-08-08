package horo.smp.view;

public class SettingsWindow extends View
{
    private final String title = "Settings";

    public SettingsWindow()
    {
        this.setResourceName("fxml_settings.fxml");
        this.setWindowIcon("icon");
        this.setTitle(title);
    }

}