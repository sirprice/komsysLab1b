package serverapplication;

/**
 * Created by o_0 on 2016-09-20.
 */
public interface Command {
    void processCommand(String msg, Client sender);
}
