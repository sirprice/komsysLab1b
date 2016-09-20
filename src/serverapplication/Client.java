package serverapplication;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by magnus on 2016-09-20.
 */
public class Client implements Runnable {
    private ServerBroadcast serverDelegate;
    private Socket clientSocket;
    private ServerLogic currentState = null;
    private AtomicBoolean runClient ;
    public Client(Socket clientSocket, ServerBroadcast serverDelegate) {
        this.serverDelegate = serverDelegate;
        this.clientSocket = clientSocket;
        this.runClient = new AtomicBoolean(true);
    }

    @Override
    public void run() {
        BufferedReader buffer = null;
        try {
            while (runClient.get()) {
                //BufferedReader buff = new BufferedReader()
                //clientSocket.getInputStream();
                buffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String msg = buffer.readLine();
                serverDelegate.post(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public void terminateClient() {
        runClient.set(false);
    }
    public void sendMsgToclient(String msg) {
        System.out.println("Sending msg: " + msg + " to: " + clientSocket.getInetAddress());
        DataOutputStream data = null;
        try {
            data = new DataOutputStream(clientSocket.getOutputStream());
            data.writeBytes(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (data != null) {
                try {
                    data.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //clientSocket.getInputStream();
    }
}
