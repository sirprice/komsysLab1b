package serverapplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by magnus on 2016-09-09.
 */
public interface ServerLogic {
    void processIncoming(DatagramPacket packet);
    void processResult();
    void respond(DatagramSocket outSocket) throws IOException;
    ServerLogic nextState();
}
