/**
 * Created by cj on 16/09/16.
 */
public class Main {

    private static final String CLIENT = "client";
    private static final String SERVER = "server";

    public static void main(String[] args) {

        if  (args[0].toLowerCase().equals(CLIENT)){
            System.out.println("Starting server.Client...");

        }
        if (args[0].toLowerCase().equals(SERVER)){
            System.out.println("Starting Server...");

        }
    }
}
