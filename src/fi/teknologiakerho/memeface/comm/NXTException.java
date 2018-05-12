package fi.teknologiakerho.memeface.comm;

import java.io.IOException;

public class NXTException extends IOException {
	
	public NXTException(byte status) {
		super(strerror(status));
	}
	
	public NXTException(String desc, byte status) {
		super(desc + ": " + strerror(status));
	}
	
	public static String strerror(byte status) {
		// TODO
		// These are defined in the bluetooth developer kit appendix 2
		return String.format("Error (0x%02x)", status);
	}
}
