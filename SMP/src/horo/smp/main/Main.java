package horo.smp.main;

public class Main {
    public static void main(String[] args) {
        horo.smp.config.Config cfg = new horo.smp.config.Config();
        //There is no other function when directly running the program
        if (args.length == 0)  args = new String[] { "SETTINGS" };
        String path = args[0];
        if (path.equals("SETTINGS"))
        {

            return;
        }

    }
}