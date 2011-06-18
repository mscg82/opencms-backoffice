package com.mscg.bucket;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class ExtendedPipedInputStream extends PipedInputStream {

    public ExtendedPipedInputStream() {
        super();
    }

    public ExtendedPipedInputStream(PipedOutputStream src) throws IOException {
        super(src);
    }

    public ExtendedPipedInputStream(int bufferSize) {
        super();
        init(bufferSize);
    }

    public ExtendedPipedInputStream(PipedOutputStream src, int bufferSize) throws IOException {
        super(src);
        init(bufferSize);
    }

    protected void init(int bufferSize) {
        buffer = new byte[bufferSize];
    }
}
