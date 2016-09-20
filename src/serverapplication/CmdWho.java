package serverapplication;

/**
 * Created by cj on 20/09/16.
 */
public class CmdWho implements Command {
    private ServerActions serverDelegate;
    public CmdWho(ServerActions serverDelegate) {
        this.serverDelegate = serverDelegate;
    }

    @Override
    public void processCommand(String msg, Client sender) {
        sender.sendMsgToclient(serverDelegate.listNicknames());
    }
}
