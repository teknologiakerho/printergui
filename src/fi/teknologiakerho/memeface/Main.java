package fi.teknologiakerho.memeface;

import java.io.IOException;

import javax.swing.JFrame;

import fi.teknologiakerho.memeface.gui.MemefaceWindow;
import fi.teknologiakerho.viipal01ja.CvUtil;

public class Main {
	
	public static void main(String[] args) throws IOException {
		CvUtil.loadLibs();

		MemefaceWindow w = new MemefaceWindow();
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.setLocationRelativeTo(null);
		w.setVisible(true);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			w.getGUI().disconnect();
		}));
	}

}
