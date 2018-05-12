package fi.teknologiakerho.memeface.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;

import fi.teknologiakerho.memeface.comm.Comm;
import fi.teknologiakerho.memeface.comm.USBComm;
import fi.teknologiakerho.memeface.control.HcodeConverter;
import fi.teknologiakerho.memeface.control.NXTConnection;
import fi.teknologiakerho.memeface.print.Printer;
import fi.teknologiakerho.viipal01ja.hcode.Command;
import fi.teknologiakerho.viipal01ja.hcode.HCode;

public class MemefaceGUI extends JComponent {
	
	private final TopControls topControls;
	private final PrinterControls printerControls;
	private final HCodeViewer hcodeViewer;
	private final PrinterLog printerLog;
	
	private NXTConnection conn;
	private HCode hcode;
	private PrinterThread printThread;
	
	public MemefaceGUI(){
		setLayout(new BorderLayout());
		add(topControls = new TopControls(this), BorderLayout.NORTH);
		add(printerControls = new PrinterControls(this), BorderLayout.WEST);
		add(hcodeViewer = new HCodeViewer(this), BorderLayout.CENTER);
		add(printerLog = new PrinterLog(this), BorderLayout.EAST);
		
		printerLog.setPreferredSize(new Dimension(250, 0));
	}
	
	public void log(String s) {
		printerLog.log(s);
	}
	
	public void openHcode(HCode hcode) {
		this.hcode = hcode;
		hcodeViewer.setHCode(hcode);
	}
	
	public void openHcode(File file) {
		HCode h = HcodeConverter.parseHCode(file);
		if(h != null)
			openHcode(h);
	}
	
	public void startPrint() {
		if(printThread != null) {
			log("Can't start print: already printing");
			return;
		}
		
		if(conn == null) {
			log("Not connected");
			return;
		}
		
		if(hcode == null) {
			log("No hcode loaded");
			return;
		}
		
		printThread = new PrinterThread(this, new Printer(conn, hcode));
		printThread.start();
	}
	
	public void cancelPrint() {
		if(printThread == null)
			return;
		
		log("Canceling print.");

		printThread.cancel();
		try {
			printThread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		printThread = null;
	}
	
	public void setProgress(int progress) {
		hcodeViewer.setProgress(progress);
	}
	
	public void setConnection(NXTConnection conn) {
		if(this.conn != null && conn != null) {
			System.err.println("Already connected u ok??!");
			try {
				this.conn.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		this.conn = conn;
		topControls.setConnectEnabled(conn == null);
		
		if(conn == null)
			return;
		
		try {
			String fw = conn.readFirmwareName();
			log("Firmware: " + fw);
		} catch(IOException e) {
			e.printStackTrace();
			log("Failed to read firmware name");
		}
	}
	
	public NXTConnection getConnection() {
		return conn;
	}
	
	public void connect() {
		if(conn != null)
			return;
		
		log("Connecting to nxt...");
		Comm comm;
		try {
			comm = USBComm.openAnyNXT();
		} catch(IOException e) {
			log("Connect failed: " + e);
			e.printStackTrace();
			return;
		}
		
		setConnection(new NXTConnection(comm));
		log("Connected.");
	}
	
	public void disconnect() {
		if(conn == null)
			return;
		
		cancelPrint();
		
		try {
			conn.close();
		} catch(IOException e) {
			log(e.toString());
			e.printStackTrace();
		}
		
		setConnection(null);
		log("Disconnected.");
	}
	
	public void sendCommand(Command command) {
		byte[] buf = new byte[8];
		int len = HcodeConverter.encodeCommand(buf, 0, command);
		try {
			conn.send(buf, 0, len);
		} catch(IOException e) {
			log("Failed to send command: " + e.toString());
			e.printStackTrace();
		}
		
		if(topControls.echoCommands())
			log(">>> " + command.toString());
	}

}
