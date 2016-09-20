package serverapplication;

/**
 * Created by o_0 on 2016-09-20.
 */
public interface ServerLogic {
    boolean post(String msg);
    void evaluateCommand(String msg, Client client);
}
