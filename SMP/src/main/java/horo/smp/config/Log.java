package horo.smp.config;

public class Log {
    //Yeah I'm not sure why i split these into separate methods either 🤷‍♂️
    public static void Information(Object callingClass, String message)
    {
        cPrint(message);
    }
    public static void Warning(Object callingClass,String message)
    {
        cPrint(message);
    }
    public static void Error(Object callingClass,String message)
    {
        cPrint(message);
    }
    public static void Fatal(Object callingClass,String message)
    {
        cPrint(message);
    }

    //region Private Methods
    private static void cPrint(String message)
    {
        System.out.println(message);
    }
    //endregion
}
