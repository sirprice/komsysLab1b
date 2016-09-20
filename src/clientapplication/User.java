package clientapplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.Buffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by cj on 20/09/16.
 */
public class User implements Runnable {

    private Socket clientSocket;
    private AtomicBoolean running;
    public User(String ip, int port) throws IOException {
        this.clientSocket = new Socket(ip,port);
        this.running = new AtomicBoolean(true);
    }

    public void start() {
        Thread th = new Thread(this);
        th.start();
    }

    public void postMessage(String msg) throws IOException {
        DataOutputStream data = null;
        data = new DataOutputStream(clientSocket.getOutputStream());
        data.writeBytes(msg);
        data.flush();
    }

    public void terminateUser() {
        running.set(false);
    }
    @Override
    public void run() {
        BufferedReader buffer = null;
        try {
            while (running.get()) {
                try {
                    buffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String msg = buffer.readLine();
                    System.out.println("Incoming msg: " + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
