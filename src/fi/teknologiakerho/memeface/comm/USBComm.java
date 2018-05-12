package fi.teknologiakerho.memeface.comm;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;

// See: https://github.com/ra1fh/nxtctl/blob/master/nxt.c

public class USBComm implements Comm {
	
	private static final short LEGO_VENDOR_ID = 0x0694;
	private static final short NXT_PRODUCT_ID = 0x0002;
	private static final byte NXT_WRITE_ENDPOINT = 0x01;
	private static final byte NXT_READ_ENDPOINT = (byte) 0x82;
	private static final int USB_INTERFACE = 0;
	private static final int USB_CONFIG = 1;
	private static final long USB_TIMEOUT = 1000;
	
	private final DeviceHandle handle;
	private final IntBuffer ib;
	
	public USBComm(DeviceHandle handle) throws IOException {
		this.handle = handle;
		init();
		ib = IntBuffer.allocate(1);
	}

	@Override
	public void write(ByteBuffer out) throws IOException {
		int r = LibUsb.bulkTransfer(
				handle,
				NXT_WRITE_ENDPOINT,
				out,
				ib,
				USB_TIMEOUT
		);
		
		if(r != LibUsb.SUCCESS)
			throw new USBException("Write error", r);
	}

	@Override
	public int read(ByteBuffer in) throws IOException {
		int r = LibUsb.bulkTransfer(
				handle,
				NXT_READ_ENDPOINT,
				in,
				ib,
				USB_TIMEOUT
		);
		
		if(r != LibUsb.SUCCESS)
			throw new USBException("Read error", r);
		
		return ib.get(0);
	}

	@Override
	public void close() throws IOException {
		int r = LibUsb.releaseInterface(handle, USB_INTERFACE);
		LibUsb.close(handle);
		
		if(r != LibUsb.SUCCESS)
			throw new USBException(r);
	}
	
	private void init() throws IOException {
		int r = LibUsb.resetDevice(handle);
		if(r != LibUsb.SUCCESS)
			throw new USBException("Failed to reset device", r);
		
		r = LibUsb.setConfiguration(handle, USB_CONFIG);
		if(r != LibUsb.SUCCESS)
			throw new USBException("Failed to set config", r);
		
		r = LibUsb.claimInterface(handle, USB_INTERFACE);
		if(r != LibUsb.SUCCESS)
			throw new USBException("Failed to claim interface", r);
	}
	
	public static USBComm openAnyNXT() throws IOException {
		DeviceHandle handle = LibUsb.openDeviceWithVidPid(null, LEGO_VENDOR_ID, NXT_PRODUCT_ID);
		if(handle == null)
			throw new IOException("No NXT devices found");
		
		return new USBComm(handle);
	}
	
	static {
		LibUsb.init(null);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> LibUsb.exit(null)));
	}

}
