import java.io.BufferedReader;
import java.net.Socket;

/**
 * Created by magnus on 2016-09-20.
 */
public class Client implements Runnable {
    private Socket clientSocket;
    private ConnectionState currentState = null;

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        while(true) {
            //BufferedReader buff = new BufferedReader()
            //clientSocket.getInputStream();
        }
    }

    public void sendMsgToclient(String msg) {
        clientSocket.getInputStream();
    }
}
