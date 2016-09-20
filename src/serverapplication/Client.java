package serverapplication;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by magnus on 2016-09-20.
 */
public class Client implements Runnable {
    private ServerLogic serverDelegate;
    private Socket clientSocket;
    private ServerLogic currentState = null;
    private AtomicBoolean runClient;
    private BufferedReader input;
    private PrintWriter output;
    private String nickName;
    public Client(Socket clientSocket, ServerLogic serverDelegate) {
        this.serverDelegate = serverDelegate;
        this.clientSocket = clientSocket;
        this.runClient = new AtomicBoolean(true);
        String addr = clientSocket.getInetAddress().toString();
        this.nickName = "player" +addr.substring(addr.length() - 3);
        try {
            this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getInetAddress() {
        return clientSocket.getInetAddress();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public void run() {

        try {
            while (runClient.get()) {

                String msg = input.readLine();
                if (msg != null) {
                    if (msg.length() > 0 && msg.charAt(0) == '/') {
                        // evaluate command
                        serverDelegate.evaluateCommand(msg,this);
                    }else{
                        serverDelegate.broadcastMsg(this.nickName + ": " + msg,this);
                    }
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {

            serverDelegate.removeClient(this);
            terminateClient();
            if (clientSocket != null) {
                output.close();
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    }
}
