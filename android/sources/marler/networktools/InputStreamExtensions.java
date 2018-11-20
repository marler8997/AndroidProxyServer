package marler.networktools;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamExtensions {
    public static int readLine(InputStream inputStream, ByteArrayReference arrayReference) throws IOException {
        int totalBytesRead = 0;
        if (arrayReference.array == null) {
            arrayReference.array = new byte[256];
        }
        while (true) {
            int next = inputStream.read();
            if (next < 0) {
                break;
            } else if (next == 10) {
                return totalBytesRead;
            } else {
                int totalBytesRead2;
                if (next == 13) {
                    next = inputStream.read();
                    if (next == 10) {
                        return totalBytesRead;
                    }
                    if (totalBytesRead >= arrayReference.array.length) {
                        arrayReference.array = new byte[(arrayReference.array.length + 256)];
                    }
                    totalBytesRead2 = totalBytesRead + 1;
                    arrayReference.array[totalBytesRead] = (byte) 13;
                    totalBytesRead = totalBytesRead2;
                }
                if (totalBytesRead >= arrayReference.array.length) {
                    arrayReference.array = new byte[(arrayReference.array.length + 256)];
                }
                totalBytesRead2 = totalBytesRead + 1;
                arrayReference.array[totalBytesRead] = (byte) next;
                totalBytesRead = totalBytesRead2;
            }
        }
        if (totalBytesRead == 0) {
            return -1;
        }
        return totalBytesRead;
    }

    public static String readLine(InputStream inputStream, StringBuilder builder) throws IOException {
        builder.setLength(0);
        while (true) {
            int next = inputStream.read();
            if (next < 0) {
                break;
            } else if (next == 10) {
                return builder.toString();
            } else {
                if (next == 13) {
                    next = inputStream.read();
                    if (next == 10) {
                        return builder.toString();
                    }
                    builder.append('\r');
                }
                builder.append((char) next);
            }
        }
        if (builder.length() == 0) {
            return null;
        }
        return builder.toString();
    }
}
