package marler.networktools;

import java.net.Socket;

public class SocketTunnel implements Runnable, NamedWorkerThread {
    private boolean aSocketHasClosed;
    public final int bufferSize;
    private final SocketThreadCallback callback;
    private final Socket inputSocket;
    private SocketTunnel otherTunnel;
    private final Socket outputSocket;
    private Object syncObject;
    public final String workerName;
    private boolean youWillShutDownSockets;

    public static void StartOneAndRunOne(int handlerID, Socket socketA, Socket socketB, int bufferSize, SocketThreadCallback callback) {
        SocketTunnel tunnelAToB = new SocketTunnel(handlerID, null, socketA, socketB, bufferSize, callback);
        SocketTunnel tunnelBToA = new SocketTunnel(handlerID, tunnelAToB, socketB, socketA, bufferSize, callback);
        tunnelAToB.setOtherTunnel(tunnelBToA);
        new Thread(tunnelAToB).start();
        tunnelBToA.run();
    }

    private SocketTunnel(int handlerID, SocketTunnel otherTunnel, Socket inputSocket, Socket outputSocket, int bufferSize, SocketThreadCallback callback) {
        this.otherTunnel = otherTunnel;
        if (otherTunnel != null) {
            this.syncObject = otherTunnel.syncObject;
        } else {
            this.syncObject = new Object();
        }
        this.inputSocket = inputSocket;
        this.outputSocket = outputSocket;
        this.workerName = String.format("Handler %d %s -> %s", new Object[]{Integer.valueOf(handlerID), inputSocket.getInetAddress().toString(), outputSocket.getInetAddress().toString()});
        this.bufferSize = bufferSize;
        this.callback = callback;
        this.aSocketHasClosed = false;
        this.youWillShutDownSockets = true;
    }

    public String getWorkerName() {
        return this.workerName;
    }

    public void setOtherTunnel(SocketTunnel otherTunnel) {
        this.otherTunnel = otherTunnel;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r9 = this;
        r5 = r9.bufferSize;	 Catch:{ IOException -> 0x002c }
        r0 = new byte[r5];	 Catch:{ IOException -> 0x002c }
        r5 = r9.inputSocket;	 Catch:{ IOException -> 0x002c }
        r3 = r5.getInputStream();	 Catch:{ IOException -> 0x002c }
        r5 = r9.outputSocket;	 Catch:{ IOException -> 0x002c }
        r4 = r5.getOutputStream();	 Catch:{ IOException -> 0x002c }
    L_0x0010:
        r5 = r9.aSocketHasClosed;	 Catch:{ IOException -> 0x002c }
        if (r5 == 0) goto L_0x001d;
    L_0x0014:
        r6 = r9.syncObject;
        monitor-enter(r6);
        r5 = r9.youWillShutDownSockets;	 Catch:{ all -> 0x00fd }
        if (r5 != 0) goto L_0x00c4;
    L_0x001b:
        monitor-exit(r6);	 Catch:{ all -> 0x00fd }
    L_0x001c:
        return;
    L_0x001d:
        r1 = r3.read(r0);	 Catch:{ IOException -> 0x002c }
        if (r1 > 0) goto L_0x0042;
    L_0x0023:
        r5 = 1;
        r9.aSocketHasClosed = r5;	 Catch:{ IOException -> 0x002c }
        r5 = r9.otherTunnel;	 Catch:{ IOException -> 0x002c }
        r6 = 1;
        r5.aSocketHasClosed = r6;	 Catch:{ IOException -> 0x002c }
        goto L_0x0014;
    L_0x002c:
        r2 = move-exception;
        r5 = r9.callback;	 Catch:{ all -> 0x004b }
        r6 = r9.inputSocket;	 Catch:{ all -> 0x004b }
        r7 = r9.outputSocket;	 Catch:{ all -> 0x004b }
        r5.ioException(r9, r6, r7, r2);	 Catch:{ all -> 0x004b }
        r6 = r9.syncObject;
        monitor-enter(r6);
        r5 = r9.youWillShutDownSockets;	 Catch:{ all -> 0x003f }
        if (r5 != 0) goto L_0x0058;
    L_0x003d:
        monitor-exit(r6);	 Catch:{ all -> 0x003f }
        goto L_0x001c;
    L_0x003f:
        r5 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x003f }
        throw r5;
    L_0x0042:
        r5 = r9.aSocketHasClosed;	 Catch:{ IOException -> 0x002c }
        if (r5 != 0) goto L_0x0014;
    L_0x0046:
        r5 = 0;
        r4.write(r0, r5, r1);	 Catch:{ IOException -> 0x002c }
        goto L_0x0010;
    L_0x004b:
        r5 = move-exception;
        r6 = r9.syncObject;
        monitor-enter(r6);
        r7 = r9.youWillShutDownSockets;	 Catch:{ all -> 0x0055 }
        if (r7 != 0) goto L_0x008f;
    L_0x0053:
        monitor-exit(r6);	 Catch:{ all -> 0x0055 }
        goto L_0x001c;
    L_0x0055:
        r5 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0055 }
        throw r5;
    L_0x0058:
        r5 = r9.otherTunnel;	 Catch:{ all -> 0x003f }
        r7 = 0;
        r5.youWillShutDownSockets = r7;	 Catch:{ all -> 0x003f }
        monitor-exit(r6);	 Catch:{ all -> 0x003f }
        r5 = r9.inputSocket;
        r5 = r5.isConnected();
        if (r5 == 0) goto L_0x0070;
    L_0x0066:
        r5 = r9.inputSocket;	 Catch:{ IOException -> 0x0122 }
        r5.shutdownInput();	 Catch:{ IOException -> 0x0122 }
    L_0x006b:
        r5 = r9.inputSocket;	 Catch:{ IOException -> 0x011f }
        r5.shutdownOutput();	 Catch:{ IOException -> 0x011f }
    L_0x0070:
        r5 = r9.outputSocket;
        r5 = r5.isConnected();
        if (r5 == 0) goto L_0x0082;
    L_0x0078:
        r5 = r9.outputSocket;	 Catch:{ IOException -> 0x011c }
        r5.shutdownInput();	 Catch:{ IOException -> 0x011c }
    L_0x007d:
        r5 = r9.outputSocket;	 Catch:{ IOException -> 0x0119 }
        r5.shutdownOutput();	 Catch:{ IOException -> 0x0119 }
    L_0x0082:
        r5 = r9.inputSocket;	 Catch:{ IOException -> 0x0116 }
        r5.close();	 Catch:{ IOException -> 0x0116 }
    L_0x0087:
        r5 = r9.outputSocket;	 Catch:{ IOException -> 0x008d }
        r5.close();	 Catch:{ IOException -> 0x008d }
        goto L_0x001c;
    L_0x008d:
        r5 = move-exception;
        goto L_0x001c;
    L_0x008f:
        r7 = r9.otherTunnel;	 Catch:{ all -> 0x0055 }
        r8 = 0;
        r7.youWillShutDownSockets = r8;	 Catch:{ all -> 0x0055 }
        monitor-exit(r6);	 Catch:{ all -> 0x0055 }
        r6 = r9.inputSocket;
        r6 = r6.isConnected();
        if (r6 == 0) goto L_0x00a7;
    L_0x009d:
        r6 = r9.inputSocket;	 Catch:{ IOException -> 0x0114 }
        r6.shutdownInput();	 Catch:{ IOException -> 0x0114 }
    L_0x00a2:
        r6 = r9.inputSocket;	 Catch:{ IOException -> 0x0112 }
        r6.shutdownOutput();	 Catch:{ IOException -> 0x0112 }
    L_0x00a7:
        r6 = r9.outputSocket;
        r6 = r6.isConnected();
        if (r6 == 0) goto L_0x00b9;
    L_0x00af:
        r6 = r9.outputSocket;	 Catch:{ IOException -> 0x0110 }
        r6.shutdownInput();	 Catch:{ IOException -> 0x0110 }
    L_0x00b4:
        r6 = r9.outputSocket;	 Catch:{ IOException -> 0x010e }
        r6.shutdownOutput();	 Catch:{ IOException -> 0x010e }
    L_0x00b9:
        r6 = r9.inputSocket;	 Catch:{ IOException -> 0x010c }
        r6.close();	 Catch:{ IOException -> 0x010c }
    L_0x00be:
        r6 = r9.outputSocket;	 Catch:{ IOException -> 0x010a }
        r6.close();	 Catch:{ IOException -> 0x010a }
    L_0x00c3:
        throw r5;
    L_0x00c4:
        r5 = r9.otherTunnel;	 Catch:{ all -> 0x00fd }
        r7 = 0;
        r5.youWillShutDownSockets = r7;	 Catch:{ all -> 0x00fd }
        monitor-exit(r6);	 Catch:{ all -> 0x00fd }
        r5 = r9.inputSocket;
        r5 = r5.isConnected();
        if (r5 == 0) goto L_0x00dc;
    L_0x00d2:
        r5 = r9.inputSocket;	 Catch:{ IOException -> 0x0108 }
        r5.shutdownInput();	 Catch:{ IOException -> 0x0108 }
    L_0x00d7:
        r5 = r9.inputSocket;	 Catch:{ IOException -> 0x0106 }
        r5.shutdownOutput();	 Catch:{ IOException -> 0x0106 }
    L_0x00dc:
        r5 = r9.outputSocket;
        r5 = r5.isConnected();
        if (r5 == 0) goto L_0x00ee;
    L_0x00e4:
        r5 = r9.outputSocket;	 Catch:{ IOException -> 0x0104 }
        r5.shutdownInput();	 Catch:{ IOException -> 0x0104 }
    L_0x00e9:
        r5 = r9.outputSocket;	 Catch:{ IOException -> 0x0102 }
        r5.shutdownOutput();	 Catch:{ IOException -> 0x0102 }
    L_0x00ee:
        r5 = r9.inputSocket;	 Catch:{ IOException -> 0x0100 }
        r5.close();	 Catch:{ IOException -> 0x0100 }
    L_0x00f3:
        r5 = r9.outputSocket;	 Catch:{ IOException -> 0x00fa }
        r5.close();	 Catch:{ IOException -> 0x00fa }
        goto L_0x001c;
    L_0x00fa:
        r5 = move-exception;
        goto L_0x001c;
    L_0x00fd:
        r5 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x00fd }
        throw r5;
    L_0x0100:
        r5 = move-exception;
        goto L_0x00f3;
    L_0x0102:
        r5 = move-exception;
        goto L_0x00ee;
    L_0x0104:
        r5 = move-exception;
        goto L_0x00e9;
    L_0x0106:
        r5 = move-exception;
        goto L_0x00dc;
    L_0x0108:
        r5 = move-exception;
        goto L_0x00d7;
    L_0x010a:
        r6 = move-exception;
        goto L_0x00c3;
    L_0x010c:
        r6 = move-exception;
        goto L_0x00be;
    L_0x010e:
        r6 = move-exception;
        goto L_0x00b9;
    L_0x0110:
        r6 = move-exception;
        goto L_0x00b4;
    L_0x0112:
        r6 = move-exception;
        goto L_0x00a7;
    L_0x0114:
        r6 = move-exception;
        goto L_0x00a2;
    L_0x0116:
        r5 = move-exception;
        goto L_0x0087;
    L_0x0119:
        r5 = move-exception;
        goto L_0x0082;
    L_0x011c:
        r5 = move-exception;
        goto L_0x007d;
    L_0x011f:
        r5 = move-exception;
        goto L_0x0070;
    L_0x0122:
        r5 = move-exception;
        goto L_0x006b;
        */
        throw new UnsupportedOperationException("Method not decompiled: marler.networktools.SocketTunnel.run():void");
    }
}
