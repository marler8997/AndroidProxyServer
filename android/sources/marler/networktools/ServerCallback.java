package marler.networktools;

import java.net.Socket;

public interface ServerCallback extends SocketThreadCallback {
    void clientSentInvalidData(NamedWorkerThread namedWorkerThread, Socket socket, String str);

    void error(NamedWorkerThread namedWorkerThread, Socket socket, String str);
}
