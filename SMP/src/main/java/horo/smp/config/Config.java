package horo.smp.config;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Config {


    public String dotmc;
    public File progfolder;
    public File configFile;
    private boolean oneInstance;


    public Config()
    {
        this.dotmc = System.getenv("APPDATA");
        this.progfolder  = new File(dotmc+"/SMP");
        this.configFile  = new File(progfolder+"/config.xml");
        verifyConfig();
        loadConfig();
    }

    private void verifyConfig()
    {
        boolean successFlag = false;
        if (!progfolder.exists())
        {
            successFlag = progfolder.mkdirs();
            if (!successFlag)
            {
                Log.Fatal(this,"Could not create folder for application data!");
            }
            else
            {
                Log.Information(this,"Created folder for application data");
            }
        }
        if (!configFile.exists())
        {
            try (FileWriter fw = new FileWriter(configFile))
            {
                fw.write("""
                        <?xml version="1.0" encoding="UTF-8"?>
                        <Config>
                            <oneInstance>true</oneInstance>
                        </Config>""");
            } catch (IOException e)
            {
                Log.Error(this,"Failed to write new config file!");
                Log.Fatal(this,e.getMessage());
            }
        }
    }

    private void loadConfig()
    {
        CDOM cfg =  new CDOM();
        cfg.loadConfig(this);
    }

    //region G/S
    public boolean getOneInstance()
    {
        return oneInstance;
    }



    public void setConfigValue(Object val,String configName)
    {
        switch (configName)
        {
            case "oneInstance":
                this.oneInstance = (boolean)val;
                return;
            default:
                Log.Error(this,"Invalid config tag %s passed, this is not valid!".formatted(configName));
        }
        return;
    }
    //endregion

}
