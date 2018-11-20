package marler.networktools;

import java.net.Socket;

public class HttpProxyHandler implements Runnable, NamedWorkerThread {
    public final Socket client;
    public final byte[] connectAsBytes = "CONNECT".getBytes();
    private int forwardPort;
    private final int handlerID;
    public final ServerCallback serverCallback;
    public final String workedThreadName;

    public HttpProxyHandler(int handlerID, Socket client, ServerCallback serverCallback) {
        if (serverCallback == null) {
            throw new NullPointerException("serverCallback");
        }
        this.handlerID = handlerID;
        this.client = client;
        this.workedThreadName = String.format("Handler %d %s", new Object[]{Integer.valueOf(handlerID), client.getInetAddress().toString()});
        this.serverCallback = serverCallback;
    }

    public String getWorkerName() {
        return this.workedThreadName;
    }

    private String parseConnectLine(String line) {
        String uri = line.substring(this.connectAsBytes.length + 1);
        int uriEndIndex = uri.indexOf(32);
        if (uriEndIndex <= 0) {
            this.serverCallback.clientSentInvalidData(this, this.client, String.format("Invalid first line of CONNECT '%s': Missing space after uri", new Object[]{line}));
            return null;
        }
        uri = uri.substring(0, uriEndIndex);
        int lastColonIndex = uri.lastIndexOf(58);
        if (lastColonIndex <= 0) {
            this.serverCallback.clientSentInvalidData(this, this.client, String.format("Invalid first line of CONNECT '%s': Missing colon in host name", new Object[]{line}));
            return null;
        }
        String portString = uri.substring(lastColonIndex + 1);
        uri = uri.substring(0, lastColonIndex);
        try {
            this.forwardPort = Integer.parseInt(portString);
            String hostName = uri;
            if (hostName.startsWith("https://")) {
                return hostName.substring("https://".length());
            }
            if (hostName.startsWith("http://")) {
                return hostName.substring("http://".length());
            }
            return hostName;
        } catch (NumberFormatException e) {
            this.serverCallback.clientSentInvalidData(this, this.client, String.format("Invalid first line of CONNECT '%s': Failed to parse port number after colon", new Object[]{line}));
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r18 = this;
        r11 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0285 }
        r13 = 1;
        r11.<init>(r13);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0285 }
        r7 = r13.getInputStream();	 Catch:{ IOException -> 0x0285 }
        r2 = marler.networktools.InputStreamExtensions.readLine(r7, r11);	 Catch:{ IOException -> 0x0285 }
        if (r2 != 0) goto L_0x0041;
    L_0x0014:
        r0 = r18;
        r13 = r0.serverCallback;	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r14 = r0.client;	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13.closed(r0, r14);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13 = r0.client;
        r13 = r13.isConnected();
        if (r13 == 0) goto L_0x0039;
    L_0x002b:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x03bf }
        r13.shutdownInput();	 Catch:{ IOException -> 0x03bf }
    L_0x0032:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x03bc }
        r13.shutdownOutput();	 Catch:{ IOException -> 0x03bc }
    L_0x0039:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x03b9 }
        r13.close();	 Catch:{ IOException -> 0x03b9 }
    L_0x0040:
        return;
    L_0x0041:
        r13 = "CONNECT";
        r13 = r2.startsWith(r13);	 Catch:{ IOException -> 0x0285 }
        if (r13 == 0) goto L_0x0169;
    L_0x0049:
        r0 = r18;
        r4 = r0.parseConnectLine(r2);	 Catch:{ IOException -> 0x0285 }
        if (r4 != 0) goto L_0x0073;
    L_0x0051:
        r0 = r18;
        r13 = r0.client;
        r13 = r13.isConnected();
        if (r13 == 0) goto L_0x0069;
    L_0x005b:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x03b6 }
        r13.shutdownInput();	 Catch:{ IOException -> 0x03b6 }
    L_0x0062:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x03b3 }
        r13.shutdownOutput();	 Catch:{ IOException -> 0x03b3 }
    L_0x0069:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0071 }
        r13.close();	 Catch:{ IOException -> 0x0071 }
        goto L_0x0040;
    L_0x0071:
        r13 = move-exception;
        goto L_0x0040;
    L_0x0073:
        r9 = marler.networktools.InputStreamExtensions.readLine(r7, r11);	 Catch:{ IOException -> 0x0285 }
        if (r9 != 0) goto L_0x00a8;
    L_0x0079:
        r0 = r18;
        r13 = r0.serverCallback;	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r14 = r0.client;	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13.closed(r0, r14);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13 = r0.client;
        r13 = r13.isConnected();
        if (r13 == 0) goto L_0x009e;
    L_0x0090:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x03b0 }
        r13.shutdownInput();	 Catch:{ IOException -> 0x03b0 }
    L_0x0097:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x03ad }
        r13.shutdownOutput();	 Catch:{ IOException -> 0x03ad }
    L_0x009e:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x00a6 }
        r13.close();	 Catch:{ IOException -> 0x00a6 }
        goto L_0x0040;
    L_0x00a6:
        r13 = move-exception;
        goto L_0x0040;
    L_0x00a8:
        r13 = r9.length();	 Catch:{ IOException -> 0x0285 }
        if (r13 > 0) goto L_0x0073;
    L_0x00ae:
        r0 = r18;
        r13 = r0.serverCallback;	 Catch:{ IOException -> 0x0285 }
        r14 = "CONNECT %s:%s";
        r15 = 2;
        r15 = new java.lang.Object[r15];	 Catch:{ IOException -> 0x0285 }
        r16 = 0;
        r15[r16] = r4;	 Catch:{ IOException -> 0x0285 }
        r16 = 1;
        r0 = r18;
        r0 = r0.forwardPort;	 Catch:{ IOException -> 0x0285 }
        r17 = r0;
        r17 = java.lang.Integer.valueOf(r17);	 Catch:{ IOException -> 0x0285 }
        r15[r16] = r17;	 Catch:{ IOException -> 0x0285 }
        r14 = java.lang.String.format(r14, r15);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13.log(r0, r14);	 Catch:{ IOException -> 0x0285 }
        r3 = new java.net.Socket;	 Catch:{ UnknownHostException -> 0x0125 }
        r0 = r18;
        r13 = r0.forwardPort;	 Catch:{ UnknownHostException -> 0x0125 }
        r3.<init>(r4, r13);	 Catch:{ UnknownHostException -> 0x0125 }
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0285 }
        r13 = r13.getOutputStream();	 Catch:{ IOException -> 0x0285 }
        r14 = "HTTP/1.1 200 Connection established\r\n\r\n";
        r14 = r14.getBytes();	 Catch:{ IOException -> 0x0285 }
        r13.write(r14);	 Catch:{ IOException -> 0x0285 }
    L_0x00ec:
        r0 = r18;
        r13 = r0.handlerID;	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r14 = r0.client;	 Catch:{ IOException -> 0x0285 }
        r15 = marler.networktools.ProxyServerConfig.ProxyBufferSize;	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r0 = r0.serverCallback;	 Catch:{ IOException -> 0x0285 }
        r16 = r0;
        r0 = r16;
        marler.networktools.SocketTunnel.StartOneAndRunOne(r13, r14, r3, r15, r0);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13 = r0.client;
        r13 = r13.isConnected();
        if (r13 == 0) goto L_0x0119;
    L_0x010b:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0381 }
        r13.shutdownInput();	 Catch:{ IOException -> 0x0381 }
    L_0x0112:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x037e }
        r13.shutdownOutput();	 Catch:{ IOException -> 0x037e }
    L_0x0119:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0122 }
        r13.close();	 Catch:{ IOException -> 0x0122 }
        goto L_0x0040;
    L_0x0122:
        r13 = move-exception;
        goto L_0x0040;
    L_0x0125:
        r12 = move-exception;
        r0 = r18;
        r13 = r0.serverCallback;	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r14 = r0.client;	 Catch:{ IOException -> 0x0285 }
        r15 = "UnknownHostException for host '%s'";
        r16 = 1;
        r0 = r16;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x0285 }
        r16 = r0;
        r17 = 0;
        r16[r17] = r4;	 Catch:{ IOException -> 0x0285 }
        r15 = java.lang.String.format(r15, r16);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13.error(r0, r14, r15);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13 = r0.client;
        r13 = r13.isConnected();
        if (r13 == 0) goto L_0x015d;
    L_0x014f:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x03aa }
        r13.shutdownInput();	 Catch:{ IOException -> 0x03aa }
    L_0x0156:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x03a7 }
        r13.shutdownOutput();	 Catch:{ IOException -> 0x03a7 }
    L_0x015d:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0166 }
        r13.close();	 Catch:{ IOException -> 0x0166 }
        goto L_0x0040;
    L_0x0166:
        r13 = move-exception;
        goto L_0x0040;
    L_0x0169:
        r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0285 }
        r5.<init>();	 Catch:{ IOException -> 0x0285 }
        r5.append(r2);	 Catch:{ IOException -> 0x0285 }
        r13 = "\r\n";
        r5.append(r13);	 Catch:{ IOException -> 0x0285 }
        r4 = 0;
        r6 = "";
        r13 = 80;
        r0 = r18;
        r0.forwardPort = r13;	 Catch:{ IOException -> 0x0285 }
    L_0x017f:
        r9 = marler.networktools.InputStreamExtensions.readLine(r7, r11);	 Catch:{ IOException -> 0x0285 }
        if (r9 != 0) goto L_0x01b8;
    L_0x0185:
        r0 = r18;
        r13 = r0.serverCallback;	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r14 = r0.client;	 Catch:{ IOException -> 0x0285 }
        r15 = "Client stopped sending data in the middle of the HTTP headers";
        r0 = r18;
        r13.clientSentInvalidData(r0, r14, r15);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13 = r0.client;
        r13 = r13.isConnected();
        if (r13 == 0) goto L_0x01ac;
    L_0x019e:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x03a4 }
        r13.shutdownInput();	 Catch:{ IOException -> 0x03a4 }
    L_0x01a5:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x03a1 }
        r13.shutdownOutput();	 Catch:{ IOException -> 0x03a1 }
    L_0x01ac:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x01b5 }
        r13.close();	 Catch:{ IOException -> 0x01b5 }
        goto L_0x0040;
    L_0x01b5:
        r13 = move-exception;
        goto L_0x0040;
    L_0x01b8:
        r13 = "Proxy-Connection:";
        r13 = r9.startsWith(r13);	 Catch:{ IOException -> 0x0285 }
        if (r13 != 0) goto L_0x017f;
    L_0x01c0:
        r5.append(r9);	 Catch:{ IOException -> 0x0285 }
        r13 = "\r\n";
        r5.append(r13);	 Catch:{ IOException -> 0x0285 }
        r13 = "Host: ";
        r13 = r9.startsWith(r13);	 Catch:{ IOException -> 0x0285 }
        if (r13 == 0) goto L_0x01db;
    L_0x01d0:
        r6 = r9;
        r13 = "Host: ";
        r13 = r13.length();	 Catch:{ IOException -> 0x0285 }
        r4 = r6.substring(r13);	 Catch:{ IOException -> 0x0285 }
    L_0x01db:
        r13 = r9.length();	 Catch:{ IOException -> 0x0285 }
        if (r13 > 0) goto L_0x017f;
    L_0x01e1:
        if (r4 != 0) goto L_0x0216;
    L_0x01e3:
        r0 = r18;
        r13 = r0.serverCallback;	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r14 = r0.client;	 Catch:{ IOException -> 0x0285 }
        r15 = "Client's HTTP is missing the 'Host' header";
        r0 = r18;
        r13.clientSentInvalidData(r0, r14, r15);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13 = r0.client;
        r13 = r13.isConnected();
        if (r13 == 0) goto L_0x020a;
    L_0x01fc:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x039e }
        r13.shutdownInput();	 Catch:{ IOException -> 0x039e }
    L_0x0203:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x039b }
        r13.shutdownOutput();	 Catch:{ IOException -> 0x039b }
    L_0x020a:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0213 }
        r13.close();	 Catch:{ IOException -> 0x0213 }
        goto L_0x0040;
    L_0x0213:
        r13 = move-exception;
        goto L_0x0040;
    L_0x0216:
        r13 = "http://";
        r13 = r4.startsWith(r13);	 Catch:{ IOException -> 0x0285 }
        if (r13 == 0) goto L_0x02b7;
    L_0x021e:
        r13 = "http://";
        r13 = r13.length();	 Catch:{ IOException -> 0x0285 }
        r4 = r4.substring(r13);	 Catch:{ IOException -> 0x0285 }
    L_0x0228:
        r13 = 58;
        r8 = r4.indexOf(r13);	 Catch:{ IOException -> 0x0285 }
        if (r8 < 0) goto L_0x0243;
    L_0x0230:
        r13 = r8 + 1;
        r10 = r4.substring(r13);	 Catch:{ IOException -> 0x0285 }
        r13 = 0;
        r4 = r4.substring(r13, r8);	 Catch:{ IOException -> 0x0285 }
        r13 = java.lang.Integer.parseInt(r10);	 Catch:{ NumberFormatException -> 0x02f2 }
        r0 = r18;
        r0.forwardPort = r13;	 Catch:{ NumberFormatException -> 0x02f2 }
    L_0x0243:
        r0 = r18;
        r13 = r0.serverCallback;	 Catch:{ IOException -> 0x0285 }
        r14 = "%s Host: %s:%d";
        r15 = 3;
        r15 = new java.lang.Object[r15];	 Catch:{ IOException -> 0x0285 }
        r16 = 0;
        r15[r16] = r2;	 Catch:{ IOException -> 0x0285 }
        r16 = 1;
        r15[r16] = r4;	 Catch:{ IOException -> 0x0285 }
        r16 = 2;
        r0 = r18;
        r0 = r0.forwardPort;	 Catch:{ IOException -> 0x0285 }
        r17 = r0;
        r17 = java.lang.Integer.valueOf(r17);	 Catch:{ IOException -> 0x0285 }
        r15[r16] = r17;	 Catch:{ IOException -> 0x0285 }
        r14 = java.lang.String.format(r14, r15);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13.log(r0, r14);	 Catch:{ IOException -> 0x0285 }
        r3 = new java.net.Socket;	 Catch:{ UnknownHostException -> 0x033a }
        r0 = r18;
        r13 = r0.forwardPort;	 Catch:{ UnknownHostException -> 0x033a }
        r3.<init>(r4, r13);	 Catch:{ UnknownHostException -> 0x033a }
        r13 = r3.getOutputStream();	 Catch:{ IOException -> 0x0285 }
        r14 = r5.toString();	 Catch:{ IOException -> 0x0285 }
        r14 = r14.getBytes();	 Catch:{ IOException -> 0x0285 }
        r13.write(r14);	 Catch:{ IOException -> 0x0285 }
        goto L_0x00ec;
    L_0x0285:
        r1 = move-exception;
        r0 = r18;
        r13 = r0.serverCallback;	 Catch:{ all -> 0x02d1 }
        r0 = r18;
        r14 = r0.client;	 Catch:{ all -> 0x02d1 }
        r0 = r18;
        r13.ioException(r0, r14, r1);	 Catch:{ all -> 0x02d1 }
        r0 = r18;
        r13 = r0.client;
        r13 = r13.isConnected();
        if (r13 == 0) goto L_0x02ab;
    L_0x029d:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0390 }
        r13.shutdownInput();	 Catch:{ IOException -> 0x0390 }
    L_0x02a4:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x038d }
        r13.shutdownOutput();	 Catch:{ IOException -> 0x038d }
    L_0x02ab:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x02b4 }
        r13.close();	 Catch:{ IOException -> 0x02b4 }
        goto L_0x0040;
    L_0x02b4:
        r13 = move-exception;
        goto L_0x0040;
    L_0x02b7:
        r13 = "https://";
        r13 = r4.startsWith(r13);	 Catch:{ IOException -> 0x0285 }
        if (r13 == 0) goto L_0x0228;
    L_0x02bf:
        r13 = "https://";
        r13 = r13.length();	 Catch:{ IOException -> 0x0285 }
        r4 = r4.substring(r13);	 Catch:{ IOException -> 0x0285 }
        r13 = 443; // 0x1bb float:6.21E-43 double:2.19E-321;
        r0 = r18;
        r0.forwardPort = r13;	 Catch:{ IOException -> 0x0285 }
        goto L_0x0228;
    L_0x02d1:
        r13 = move-exception;
        r0 = r18;
        r14 = r0.client;
        r14 = r14.isConnected();
        if (r14 == 0) goto L_0x02ea;
    L_0x02dc:
        r0 = r18;
        r14 = r0.client;	 Catch:{ IOException -> 0x038a }
        r14.shutdownInput();	 Catch:{ IOException -> 0x038a }
    L_0x02e3:
        r0 = r18;
        r14 = r0.client;	 Catch:{ IOException -> 0x0387 }
        r14.shutdownOutput();	 Catch:{ IOException -> 0x0387 }
    L_0x02ea:
        r0 = r18;
        r14 = r0.client;	 Catch:{ IOException -> 0x0384 }
        r14.close();	 Catch:{ IOException -> 0x0384 }
    L_0x02f1:
        throw r13;
    L_0x02f2:
        r1 = move-exception;
        r0 = r18;
        r13 = r0.serverCallback;	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r14 = r0.client;	 Catch:{ IOException -> 0x0285 }
        r15 = "The HTTP Host Header '%s' contained an invalid string after the colon in the host value. Expected a port number but got '%s'";
        r16 = 2;
        r0 = r16;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x0285 }
        r16 = r0;
        r17 = 0;
        r16[r17] = r6;	 Catch:{ IOException -> 0x0285 }
        r17 = 1;
        r16[r17] = r10;	 Catch:{ IOException -> 0x0285 }
        r15 = java.lang.String.format(r15, r16);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13.clientSentInvalidData(r0, r14, r15);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13 = r0.client;
        r13 = r13.isConnected();
        if (r13 == 0) goto L_0x032e;
    L_0x0320:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0399 }
        r13.shutdownInput();	 Catch:{ IOException -> 0x0399 }
    L_0x0327:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0397 }
        r13.shutdownOutput();	 Catch:{ IOException -> 0x0397 }
    L_0x032e:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0337 }
        r13.close();	 Catch:{ IOException -> 0x0337 }
        goto L_0x0040;
    L_0x0337:
        r13 = move-exception;
        goto L_0x0040;
    L_0x033a:
        r12 = move-exception;
        r0 = r18;
        r13 = r0.serverCallback;	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r14 = r0.client;	 Catch:{ IOException -> 0x0285 }
        r15 = "UnknownHostException for host '%s'";
        r16 = 1;
        r0 = r16;
        r0 = new java.lang.Object[r0];	 Catch:{ IOException -> 0x0285 }
        r16 = r0;
        r17 = 0;
        r16[r17] = r4;	 Catch:{ IOException -> 0x0285 }
        r15 = java.lang.String.format(r15, r16);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13.error(r0, r14, r15);	 Catch:{ IOException -> 0x0285 }
        r0 = r18;
        r13 = r0.client;
        r13 = r13.isConnected();
        if (r13 == 0) goto L_0x0372;
    L_0x0364:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0395 }
        r13.shutdownInput();	 Catch:{ IOException -> 0x0395 }
    L_0x036b:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x0393 }
        r13.shutdownOutput();	 Catch:{ IOException -> 0x0393 }
    L_0x0372:
        r0 = r18;
        r13 = r0.client;	 Catch:{ IOException -> 0x037b }
        r13.close();	 Catch:{ IOException -> 0x037b }
        goto L_0x0040;
    L_0x037b:
        r13 = move-exception;
        goto L_0x0040;
    L_0x037e:
        r13 = move-exception;
        goto L_0x0119;
    L_0x0381:
        r13 = move-exception;
        goto L_0x0112;
    L_0x0384:
        r14 = move-exception;
        goto L_0x02f1;
    L_0x0387:
        r14 = move-exception;
        goto L_0x02ea;
    L_0x038a:
        r14 = move-exception;
        goto L_0x02e3;
    L_0x038d:
        r13 = move-exception;
        goto L_0x02ab;
    L_0x0390:
        r13 = move-exception;
        goto L_0x02a4;
    L_0x0393:
        r13 = move-exception;
        goto L_0x0372;
    L_0x0395:
        r13 = move-exception;
        goto L_0x036b;
    L_0x0397:
        r13 = move-exception;
        goto L_0x032e;
    L_0x0399:
        r13 = move-exception;
        goto L_0x0327;
    L_0x039b:
        r13 = move-exception;
        goto L_0x020a;
    L_0x039e:
        r13 = move-exception;
        goto L_0x0203;
    L_0x03a1:
        r13 = move-exception;
        goto L_0x01ac;
    L_0x03a4:
        r13 = move-exception;
        goto L_0x01a5;
    L_0x03a7:
        r13 = move-exception;
        goto L_0x015d;
    L_0x03aa:
        r13 = move-exception;
        goto L_0x0156;
    L_0x03ad:
        r13 = move-exception;
        goto L_0x009e;
    L_0x03b0:
        r13 = move-exception;
        goto L_0x0097;
    L_0x03b3:
        r13 = move-exception;
        goto L_0x0069;
    L_0x03b6:
        r13 = move-exception;
        goto L_0x0062;
    L_0x03b9:
        r13 = move-exception;
        goto L_0x0040;
    L_0x03bc:
        r13 = move-exception;
        goto L_0x0039;
    L_0x03bf:
        r13 = move-exception;
        goto L_0x0032;
        */
        throw new UnsupportedOperationException("Method not decompiled: marler.networktools.HttpProxyHandler.run():void");
    }
}
