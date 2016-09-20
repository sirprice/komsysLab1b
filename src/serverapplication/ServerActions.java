package serverapplication;

/**
 * Created by o_0 on 2016-09-20.
 */
public interface ServerActions {
    void disconnectClient(Client client);
    boolean broadcastMsg(String msg,Client from);
}
