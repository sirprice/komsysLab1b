package clientapplication;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by cj on 20/09/16.
 */
public class User implements Runnable {

    private Socket clientSocket;
    private AtomicBoolean running;
    private BufferedReader input;
    private PrintWriter output;
    public User(String ip, int port) throws IOException {
        this.clientSocket = new Socket(ip,port);
        this.running = new AtomicBoolean(true);
        try {
            this.input = new  BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        Thread th = new Thread(this);
        th.start();
        Scanner scanner = new Scanner(System.in);
        while (running.get()){
            String msg = scanner.nextLine();
            try {
                postMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
                running.set(false);
            }
        }
    }

    public void postMessage(String msg) throws IOException {
        DataOutputStream data = null;
        this.output.println(msg);
        this.output.flush();

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
                    if (msg == null){
                        running.set(false);
                        return;
                    }
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
