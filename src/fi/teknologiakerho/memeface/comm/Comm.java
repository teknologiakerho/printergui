package fi.teknologiakerho.memeface.comm;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

public interface Comm extends Closeable {
	
	void write(ByteBuffer out) throws IOException;
	int read(ByteBuffer in) throws IOException;
	
}
