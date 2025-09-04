package horo.smp.view;

public class SettingsWindow extends View
{
    private final String title = "Settings";

    public SettingsWindow()
    {
        this.setWindowTypeID(0);
        this.setResourceName("fxml_settings.fxml");
        this.setWindowIcon("icon");
        this.setTitle(title);
    }

}