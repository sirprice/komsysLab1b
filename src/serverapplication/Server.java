package serverapplication;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.*;

/**
 * Created by o_0 on 2016-09-20.
 */
public class Server implements Runnable, ServerLogic, ServerActions {
    private static final String DELIMITERS = "/ ";
    ServerSocket serverSocket;
    private ConcurrentHashMap<InetAddress, Client> clientLookup;
    private BlockingQueue<MsgContainer> messageToBroadcast = new LinkedBlockingQueue<MsgContainer>();
    private ExecutorService threadPool;
    private ConcurrentHashMap<String, Command> commandList = new ConcurrentHashMap<String, Command>();

    class MsgContainer {
        public String msg;
        public Client client;

        public MsgContainer(String msg, Client client) {
            this.msg = msg;
            this.client = client;
        }
    }

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clientLookup = new ConcurrentHashMap<InetAddress, Client>();
        this.threadPool = Executors.newCachedThreadPool();
        registrateAllCommands();
    }

    private void registrateAllCommands() {
        commandList.put("quit", new CommandDefault("quit"));
        commandList.put("who", new CommandDefault("who"));
        commandList.put("nick", new CmdChangeNick(this));
        commandList.put("help", new CommandDefault("help"));
    }

    private void sendBroadcastMessage(MsgContainer msg) {
        sendBroadcastMessage(msg.msg, msg.client);
    }

    private void sendBroadcastMessage(String msg, Client from) {
        for (Map.Entry<InetAddress, Client> entry : clientLookup.entrySet()) {
            System.out.println(entry);
            Client c = entry.getValue();
            if (!c.getInetAddress().equals(from.getInetAddress())) {
                c.sendMsgToclient(msg);
            }
        }
    }

    public void run() {
        while (true) {
            try {
                MsgContainer msg = messageToBroadcast.take();

                sendBroadcastMessage(msg);
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
    public void disconnectClient(Client client) {

    }

    @Override
    public String listNicknames() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<InetAddress, Client> entry : clientLookup.entrySet()) {
            System.out.println(entry);
            Client c = entry.getValue();
            sb.append(c.getNickName() + "\n");
        }
        return sb.toString();
    }

    @Override
    public String listCommands() {
        return "Commands: \n"
                +"/quit = logout and exit \n"
                +"/who = list of all connected users \n"
                +"/nick <newNickName> = change nickname \n"
                +"/help = list all available commands";
    }
    @Override
    public boolean broadcastMsg(String msg, Client from) {
        return messageToBroadcast.offer(new MsgContainer(msg, from));
    }
//    @Override
//    boolean broadcastMsg(String msg, Client from) {
//        return messageToBroadcast.offer(new MsgContainer(msg,from));
//    }

    @Override
    public void evaluateCommand(String msg, Client client) {
        StringTokenizer tokenizer = new StringTokenizer(msg.substring(1), DELIMITERS);
        String cmd = null;
        if (tokenizer.hasMoreTokens()) {
            cmd = tokenizer.nextToken();
        }

        if (cmd == null) {
            return;
        }
        Command command = commandList.get(cmd);
        if (command != null) {
            command.processCommand(msg, client);
        }
    }
}
