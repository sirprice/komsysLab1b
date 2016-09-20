package serverapplication;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by magnus on 2016-09-20.
 */
public class Client implements Runnable {
    private Socket clientSocket;
    private ServerLogic currentState = null;
    private AtomicBoolean runClient ;
    public Client(Socket clientSocket) {

        this.clientSocket = clientSocket;
        this.runClient = new AtomicBoolean(true);
    }

    @Override
    public void run() {
        try {
            while (runClient.get()) {
                //BufferedReader buff = new BufferedReader()
                //clientSocket.getInputStream();
            }
        }finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void terminateClient() {
        runClient.set(false);
    }
    public void sendMsgToclient(String msg) {
        clientSocket.getInputStream();
    }
}
