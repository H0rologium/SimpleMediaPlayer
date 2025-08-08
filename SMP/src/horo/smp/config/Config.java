package horo.smp.config;

public class Config {

    private boolean oneInstance;

    public Config()
    {
        setConfigValue(true,"oneInstance");
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
                this.oneInstance = val;
        }
        return;
    }
    //endregion

}
