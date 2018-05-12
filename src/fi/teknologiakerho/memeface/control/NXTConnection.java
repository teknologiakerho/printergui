package fi.teknologiakerho.memeface.control;

import java.io.Closeable;
import java.io.IOException;

import fi.teknologiakerho.memeface.comm.Comm;
import fi.teknologiakerho.memeface.comm.NXTComm;

public class NXTConnection implements Closeable {
	
	public static final byte CONTROL_MBOX = 0;
	public static final byte STOP_MBOX = 1;

	private static final int COMM_IOMAP = 0x00050001;
	private static final int COMM_IOMAP_OFFSET = 1289;
	private static final int FW_NAME_OFFSET = 0;
	private static final int POSITION_OFFSET = 20;
	private static final int IP_OFFSET = 28;

	private final NXTComm nxt;
	
	public NXTConnection(Comm comm) {
		this.nxt = new NXTComm(comm);
	}
	
	public void send(byte[] cmds) throws IOException {
		send(cmds, 0, cmds.length);
	}
	
	public void send(byte[] cmds, int off, int len) throws IOException {
		nxt.messageWrite(CONTROL_MBOX, cmds, off, len);
	}
	
	public String readFirmwareName() throws IOException {
		byte[] buf = new byte[20];
		nxt.readIOMap(buf, COMM_IOMAP, COMM_IOMAP_OFFSET+FW_NAME_OFFSET, 20);
		return new String(buf);
	}
	
	public Position readPosition() throws IOException {
		byte[] buf = new byte[8];
		nxt.readIOMap(buf, COMM_IOMAP, COMM_IOMAP_OFFSET+POSITION_OFFSET, 8);
		return new Position(
			buf[0] | buf[1] << 8,
			buf[2] | buf[3] << 8,
			buf[4] | buf[5] << 8,
			buf[6] | buf[7] << 8
		);
	}
	
	public int readIP() throws IOException {
		byte[] buf = new byte[2];
		nxt.readIOMap(buf, COMM_IOMAP, COMM_IOMAP_OFFSET+IP_OFFSET, 2);
		return (buf[0] & 0xff) | (buf[1] & 0xff) << 8;
	}
	
	@Override
	public void close() throws IOException {
		nxt.close();
	}

}
