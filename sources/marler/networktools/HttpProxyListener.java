package marler.networktools;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpProxyListener implements Runnable, NamedWorkerThread {
    public final int listenPort;
    private final ServerCallback serverCallback;
    private ServerSocket serverSocket;

    public HttpProxyListener(int listenPort, ServerCallback serverCallback) {
        if (serverCallback == null) {
            throw new NullPointerException("serverCallback");
        }
        this.listenPort = listenPort;
        this.serverCallback = serverCallback;
        this.serverSocket = null;
    }

    public String getWorkerName() {
        return "ProxyListener";
    }

    public void stop() {
        ServerSocket serverSocket = this.serverSocket;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
        }
    }

    public void run() {
        int nextHandlerID = 0;
        try {
            this.serverSocket = new ServerSocket(this.listenPort);
            while (true) {
                this.serverCallback.debug(this, "Listening on port " + this.listenPort);
                new Thread(new HttpProxyHandler(nextHandlerID, this.serverSocket.accept(), this.serverCallback)).start();
                if (nextHandlerID >= Integer.MAX_VALUE) {
                    nextHandlerID = 0;
                } else {
                    nextHandlerID++;
                }
            }
        } catch (IOException e) {
            this.serverCallback.ioException(this, null, e);
            if (this.serverSocket != null) {
                try {
                    this.serverSocket.close();
                } catch (IOException e2) {
                }
            }
        } catch (Throwable th) {
            if (this.serverSocket != null) {
                try {
                    this.serverSocket.close();
                } catch (IOException e3) {
                }
            }
        }
    }
}
