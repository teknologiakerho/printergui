package fi.teknologiakerho.memeface.gui;

import java.io.IOException;

import fi.teknologiakerho.memeface.print.Printer;

public class PrinterThread extends Thread {
	
	private final MemefaceGUI gui;
	private final Printer printer;
	
	public PrinterThread(MemefaceGUI gui, Printer printer) {
		this.gui = gui;
		this.printer = printer;
	}
	
	@Override
	public void run() {
		System.out.println("Starting printer thread");
		gui.log("Init printer state");

		try {
			printer.init();
		} catch(IOException e) {
			System.err.println("Failed to init print; exiting print thread");
			gui.log("Failed to init printer: " + e);
			e.printStackTrace();
			return;
		} catch(InterruptedException e) {
			System.out.println("Interrupted while waiting for printer init");
			return;
		}
		
		System.out.println("Entering print loop");
		gui.log("Entering print loop");

		while(!printer.done()) {
			try {
				printer.loop();
			} catch(IOException e) {
				System.err.println("Exception in printer loop");
				gui.log("Exception in printer loop: " + e);
				e.printStackTrace();
				break;
			}
			
			try {
				Thread.sleep(500);
			} catch(InterruptedException e) {
				System.out.println("Printer thread interrupted");
				break;
			}
			
			gui.setProgress(printer.getIp());
		}

		System.out.println("Exiting printer thread");
		gui.log("Print done.");
	}
	
	public void cancel() {
		interrupt();
		// some state deinit maybe goes here 
	}

}
