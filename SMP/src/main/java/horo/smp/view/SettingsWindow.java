package horo.smp.view;
import horo.smp.config.Log;

public class SettingsWindow extends View
{
    private final String title = "Settings";

    public SettingsWindow()
    {
        this.setResourceName("fxml_settings.fxml");
        this.setWindowIcon("icon");
    }

}