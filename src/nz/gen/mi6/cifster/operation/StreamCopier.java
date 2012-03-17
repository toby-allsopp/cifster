package nz.gen.mi6.cifster.operation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamCopier {

    public interface Listener {
        void onProgress(long numBytes);
    }

    public static final int DEFAULT_BUFFER_SIZE = 100000;
    private final byte[] m_buffer;

    public StreamCopier(final int bufferSize) {
        m_buffer = new byte[bufferSize];
    }

    public StreamCopier() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public void copy(
            final InputStream in,
            final OutputStream out,
            final Listener listener) throws IOException {
        while (true) {
            final int numRead = in.read(m_buffer);
            if (numRead == -1) {
                break;
            }
            out.write(m_buffer, 0, numRead);
            if (listener != null) {
                listener.onProgress(numRead);
            }
        }
    }
}
