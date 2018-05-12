package fi.teknologiakerho.memeface.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import fi.teknologiakerho.viipal01ja.gui.GUIAction;
import fi.teknologiakerho.viipal01ja.gui.Viipal01jaWindow;

public class MemefaceWindow extends JFrame {
	
	private final MemefaceGUI gui;
	
	private final JFileChooser fileChooser;
	
	public MemefaceWindow() {
		super("727pp blue zenith lol");
		setLayout(new BorderLayout());
		setSize(1200, 800);
		setResizable(false);
		add(gui = new MemefaceGUI(), BorderLayout.CENTER);
		fileChooser = new JFileChooser();
		setupMenu();
	}
	
	private void setupMenu() {
		JMenuBar mb = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new JMenuItem(GUIAction.wrap("Open hcode", KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK,
				e -> openHCode())));
		fileMenu.add(new JMenuItem(GUIAction.wrap("Open viipal01ja", KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK,
				e -> openViipal01ja())));
		mb.add(fileMenu);
		
		setJMenuBar(mb);
	}
	
	public MemefaceGUI getGUI() {
		return gui;
	}
	
	public void openViipal01ja() {
		Viipal01jaWindow w = new Viipal01jaWindow();
		
		JButton okButton = new JButton("OK");
		//okButton.addActionListener(e -> gui.openHcode(null));
		w.add(okButton, BorderLayout.SOUTH);
		
		w.setLocationRelativeTo(null);
		w.setVisible(true);
	}
	
	public void openHCode() {
		int res = fileChooser.showOpenDialog(null);
		if(res == JFileChooser.APPROVE_OPTION)
			openHCode(fileChooser.getSelectedFile());
	}
	
	public void openHCode(File file) {
		gui.openHcode(file);
	}

}
