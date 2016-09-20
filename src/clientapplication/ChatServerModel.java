package clientapplication;

import java.io.IOException;

/**
 * Created by o_0 on 2016-09-20.
 */
public interface ChatServerModel {
    public void postMessage(String msg) throws IOException;
}
