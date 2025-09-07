package horo.smp.config;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Config {


    public String dotmc;
    public File progfolder;
    public File configFile;
    private boolean oneInstance;
    private double[] winDimensions;
    private double volume;


    public Config()
    {
        this.dotmc = System.getenv("APPDATA");
        this.progfolder  = new File(dotmc+"/SMP");
        this.configFile  = new File(progfolder+"/config.xml");
        this.winDimensions  = new double[]{0,0};
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
                            <oneInstance>false</oneInstance>
                            <winDimensionH>50.0</winDimensionH>
                            <winDimensionW>50.0</winDimensionW>
                            <volume>0.5</volume>
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
        return this.oneInstance;
    }
    public double[] getWinDimensions()
    {
        return this.winDimensions;
    }
    public File getConfigPath()
    {
        return this.configFile;
    }
    public double getVolume()
    {
        return this.volume;
    }


    public void saveConfig()
    {
        CDOM cfg =  new CDOM();
        cfg.saveConfig(this);
    }

    public void setConfigValue(Object val,String configName)
    {
        switch (configName)
        {
            case "oneInstance":
                this.oneInstance = (boolean)val;
                return;
            case "winDimensionW":
                this.winDimensions[0] = (double)val;
                return;
            case "winDimensionH":
                this.winDimensions[1] = (double)val;
                return;
            case "lastWindowDimension":
                double[] dimensions = (double[])val;
                setConfigValue(dimensions[0],"winDimensionW");
                setConfigValue(dimensions[1],"winDimensionH");
                return;
            case "volume":
                this.volume = (double)val;
                //Log.Information(this,"Saving volume to config with value: "+this.volume);
                return;
            default:
                Log.Error(this,"Invalid config tag %s passed, this is not valid!".formatted(configName));
        }
        return;
    }
    //endregion

}
