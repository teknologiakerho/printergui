package fi.teknologiakerho.memeface.print;

import java.io.IOException;

import fi.teknologiakerho.memeface.Util;
import fi.teknologiakerho.memeface.control.HcodeConverter;
import fi.teknologiakerho.memeface.control.NXTConnection;
import fi.teknologiakerho.viipal01ja.hcode.Command;
import fi.teknologiakerho.viipal01ja.hcode.HCode;

public class Printer {
	
	private static final int LEFT_THRES = 8;
	private static final int MAX_BUF_SIZE = 58;
	
	private final NXTConnection conn;
	private final HCode hcode;
	private int ip = 0, sent = 0;
	
	public Printer(NXTConnection conn, HCode hcode) {
		this.conn = conn;
		this.hcode = hcode;
	}
	
	public void init() throws IOException, InterruptedException {
		// Home, set zero and reset instruction pointer
		conn.send(new byte[] { 'R', 'z', 'I', 0, 0 });
		// Wait until IP resets
		for(;;) {
			Thread.sleep(500);
			int ip = conn.readIP();
			System.out.println("[Printer] init: IP=" + ip);
			if(conn.readIP() == 0)
				return;
		}
	}
	
	public void loop() throws IOException {
		ip = conn.readIP();
		System.out.printf("[Printer] IP: %d / %d\n", ip, hcode.commands.size());
		if(sent-ip < LEFT_THRES && sent < hcode.commands.size())
			sendMore();
	}
	
	public int getIp() {
		return ip;
	}
	
	public int getSent() {
		return sent;
	}
	
	public int getTotal() {
		return hcode.commands.size();
	}
	
	public boolean done() {
		return ip == hcode.commands.size();
	}
	
	private void sendMore() throws IOException {
		byte[] buf = new byte[MAX_BUF_SIZE];
		int off = 0;
		
		while(sent < hcode.commands.size()) {
			Command cmd = hcode.commands.get(sent);
			if(off+HcodeConverter.commandSize(cmd.getCmd()) < MAX_BUF_SIZE)
				off += HcodeConverter.encodeCommand(buf, off, cmd);
			else
				break;
			sent++;
		}
		
		if(off > 0)
			conn.send(buf, 0, off);
		
		System.out.printf("[Printer] Sent %d bytes, hcode at %d/%d\n", off, ip, hcode.commands.size());
		System.out.println(Util.dumpByteArray(buf, 0, off));
	}

}
