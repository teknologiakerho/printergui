package fi.teknologiakerho.memeface.comm;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NXTComm implements Closeable {
	
	private final Comm comm;
	private final ByteBuffer buffer;
	
	public NXTComm(Comm comm) {
		this.comm = comm;
		buffer = ByteBuffer.allocateDirect(64);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
	}
	
	public void messageWrite(byte mailbox, byte[] msg, int off, int len) throws IOException {
		resetBuffer().put((byte) 0x00)
			.put((byte) 0x09)
			.put(mailbox)
			.put((byte) len);
			//.put((byte) (len + 1));
		buffer.put(msg, off, len);
		// nxt dev docs require the message to be null terminated but robotc
		// doesn't so we don't do it
		//buffer.put((byte) 0);
		
		nxtSendCommand();
	}
	
	public int messageRead(byte remoteMailbox, byte localMailbox, byte[] out, int off) throws IOException {
		resetBuffer().put((byte) 0x00)
			.put((byte) 0x13)
			.put(remoteMailbox)
			.put(localMailbox)
			.put((byte) 1);

		nxtSendCommand();
		
		byte sz = buffer.get(4);
		buffer.position(5);
		buffer.get(out, off, sz-1);
		return sz;
	}
	
	public void readIOMap(byte[] dest, int moduleId, int off, int num) throws IOException {
		resetBuffer().put((byte) 0x01)
			.put((byte) 0x94)
			.putInt(moduleId)
			.putShort((short) off)
			.putShort((short) num);
		
		nxtSendCommand();
		
		buffer.position(7);
		num = buffer.getShort();
		buffer.get(dest, 0, num);
	}
	
	private int nxtSendCommand() throws IOException {
		buffer.flip();
		comm.write(buffer);
		
		buffer.rewind();
		int rcv = comm.read(buffer);
		buffer.limit(rcv);
		
		byte status = buffer.get(2);
		if(status != 0)
			throw new NXTException(status);
		
		return rcv;
	}
	
	private ByteBuffer resetBuffer() {
		return (ByteBuffer) buffer.limit(buffer.capacity()).rewind();
	}

	@Override
	public void close() throws IOException {
		comm.close();
	}
}
