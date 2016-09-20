package serverapplication;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.*;

/**
 * Created by o_0 on 2016-09-20.
 */
public class Server implements Runnable, ServerLogic {
    private static final String DELIMITERS = "/ ";
    ServerSocket serverSocket;
    private ConcurrentHashMap<InetAddress, Client> clientLookup;
    private BlockingQueue<String> messageToBroadcast = new LinkedBlockingQueue<String>();
    private ExecutorService threadPool;
    private ConcurrentHashMap<String,Command> commandList = new ConcurrentHashMap<String,Command>();
    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clientLookup = new ConcurrentHashMap<InetAddress, Client>();
        this.threadPool = Executors.newCachedThreadPool();
    }

    private void registrateAllCommands() {
        commandList.put("TestCmd",new CommandDefault());
    }
    private void broadcastMessage(String msg) {
        for (Map.Entry<InetAddress, Client> entry : clientLookup.entrySet()) {
            System.out.println(entry);
            Client c = entry.getValue();
            c.sendMsgToclient(msg);
        }
    }

    public void run() {
        while (true) {
            try {
                String msg = messageToBroadcast.take();

                broadcastMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void start() throws IOException {
        threadPool.execute(this);
        while (true) {
            System.out.println("Waiting for connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");
            InetAddress inetAddress = clientSocket.getInetAddress();
            Client client = new Client(clientSocket, this);
            Client oldClient = clientLookup.put(inetAddress, client);
            if (oldClient != null) {
                oldClient.terminateClient();
            }
            System.out.println("Client Added!");
            threadPool.execute(client);
        }


    }

    @Override
    public boolean post(String msg) {
        return messageToBroadcast.offer(msg);
    }

    @Override
    public void evaluateCommand(String msg, Client client) {
        StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITERS);
        String cmd = null;
        if (tokenizer.hasMoreTokens()) {
             cmd = tokenizer.nextToken();
            if (tokenizer.hasMoreTokens())
                cmd = tokenizer.nextToken();
        }

        Command command = commandList.get(cmd);
        if (command != null) {
            command.processCommand(msg,client);
        }


    }
}
