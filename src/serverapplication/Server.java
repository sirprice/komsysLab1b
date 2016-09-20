
import serverapplication.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by o_0 on 2016-09-20.
 */
public class Server implements Runnable {
    ServerSocket serverSocket ;
    private ConcurrentHashMap<InetAddress, Client> clientLookup;
    private BlockingQueue<String> messageToBroadcast = new BlockingQueue<String>();
    private ExecutorService threadPool;
    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clientLookup = new ConcurrentHashMap<InetAddress, Client>();
        this.threadPool = Executors.newCachedThreadPool();
    }


    private void broadcastMessage(String msg) {
        for(client : clientLookup) {

        }
    }

    public void run() {
        while (true) {
            try {
                String msg = messageToBroadcast.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void start(int port){
        threadPool.execute(this);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            InetAddress inetAddress = clientSocket.getInetAddress();
            Client client = new Client(clientSocket);
            Client oldClient = clientLookup.put(inetAddress, client);
            if (oldClient != null) {
                oldClient.terminateClient();
            }
            threadPool.execute(client);
        }


    }
}
