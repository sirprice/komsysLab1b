package serverapplication;

/**
 * Created by o_0 on 2016-09-20.
 */
public class CmdChangeNick implements Command{
    private ServerActions serverDelegate;
    public CmdChangeNick(ServerActions serverDelegate) {
        this.serverDelegate = serverDelegate;
    }
    @Override
    public void processCommand(String msg, Client sender) {
        int idx = msg.indexOf(" ");
        if (idx == -1) {
            sender.sendMsgToclient("Invalid nickname...");
            return;
        }
        String oldName = sender.getNickName();
        String newName = msg.substring(idx);
        if (newName.length() > 0) {
            sender.setNickName(newName);
            serverDelegate.broadcastMsg(oldName + " changed nickname to " + newName,sender);
        }
    }
}
