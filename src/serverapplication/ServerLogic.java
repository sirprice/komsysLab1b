package serverapplication;

/**
 * Created by o_0 on 2016-09-20.
 */
public interface ServerLogic {
    void removeClient(Client client);
    boolean broadcastMsg(String msg,Client from);
    void evaluateCommand(String msg, Client client);
}
