package marler.networktools;

import java.io.IOException;
import java.net.Socket;

public class ProxyServerProgram implements ServerCallback {
    public static final int httpListenPort = 8080;
    public static final int socksListenPort = 8080;

    public static void main(String[] args) {
        new HttpProxyListener(8080, new ProxyServerProgram()).run();
    }

    public void ioException(NamedWorkerThread workerThread, Socket client, IOException e) {
        System.out.println(String.format("[%s] IOException: %s", new Object[]{workerThread.getWorkerName(), e.getMessage()}));
    }

    public void ioException(NamedWorkerThread workerThread, Socket socketA, Socket socketB, IOException e) {
        System.out.println(String.format("[%s] IOException: %s", new Object[]{workerThread.getWorkerName(), e.getMessage()}));
    }

    public void closed(NamedWorkerThread workerThread, Socket client) {
        System.out.println(String.format("[%s] Closed", new Object[]{workerThread.getWorkerName()}));
    }

    public void clientSentInvalidData(NamedWorkerThread workerThread, Socket client, String errorMessage) {
        System.out.println(String.format("[%s] Error: %s", new Object[]{workerThread.getWorkerName(), errorMessage}));
    }

    public void error(NamedWorkerThread workerThread, Socket client, String errorMessage) {
        System.out.println(String.format("[%s] Error: %s", new Object[]{workerThread.getWorkerName(), errorMessage}));
    }

    public void log(NamedWorkerThread workerThread, String logMessage) {
        System.out.println(String.format("[%s] LOG: %s", new Object[]{workerThread.getWorkerName(), logMessage}));
    }

    public void debug(NamedWorkerThread workerThread, String debugMessage) {
        System.out.println(String.format("[%s] DEBUG: %s", new Object[]{workerThread.getWorkerName(), debugMessage}));
    }
}
