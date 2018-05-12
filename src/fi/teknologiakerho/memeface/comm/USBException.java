package fi.teknologiakerho.memeface.comm;

import java.io.IOException;

import org.usb4java.LibUsb;

public class USBException extends IOException {
	
	public USBException(int err) {
		super(LibUsb.strError(err));
	}
	
	public USBException(String desc, int err) {
		super(desc + ": " + LibUsb.strError(err));
	}

}
