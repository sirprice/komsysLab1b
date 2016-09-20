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
    private AtomicBoolean runClient;
    private BufferedReader input;
    private PrintWriter output;
    public Client(Socket clientSocket, ServerBroadcast serverDelegate) {
        this.serverDelegate = serverDelegate;
        this.clientSocket = clientSocket;
        this.runClient = new AtomicBoolean(true);
        try {
            this.input = new  BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            while (runClient.get()) {
                System.out.println("Start of loop");
                //BufferedReader buff = new BufferedReader()
                //clientSocket.getInputStream();
                //buffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("ReadLine...");
                String msg = input.readLine();
                System.out.println("ReadLine done");
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
        this.output.println(msg);
        this.output.flush();
//        try {
//            data = new DataOutputStream(clientSocket.getOutputStream());
//            data.writeBytes(msg);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (data != null) {
//                try {
//                    data.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        //clientSocket.getInputStream();
    }
}
