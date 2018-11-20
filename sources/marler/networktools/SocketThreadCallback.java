package marler.networktools;

import java.io.IOException;
import java.net.Socket;

public interface SocketThreadCallback {
    void closed(NamedWorkerThread namedWorkerThread, Socket socket);

    void debug(NamedWorkerThread namedWorkerThread, String str);

    void ioException(NamedWorkerThread namedWorkerThread, Socket socket, IOException iOException);

    void ioException(NamedWorkerThread namedWorkerThread, Socket socket, Socket socket2, IOException iOException);

    void log(NamedWorkerThread namedWorkerThread, String str);
}
