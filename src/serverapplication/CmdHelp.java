package serverapplication;

/**
 * Created by cj on 20/09/16.
 */
public class CmdHelp implements Command {
    private ServerActions serverDelegate;
    public CmdHelp(ServerActions serverDelegate) {
        this.serverDelegate = serverDelegate;
    }

    @Override
    public void processCommand(String msg, Client sender) {
        sender.sendMsgToclient(serverDelegate.listCommands());
    }
}
