package fi.teknologiakerho.memeface.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import fi.teknologiakerho.memeface.control.HcodeConverter;
import fi.teknologiakerho.viipal01ja.hcode.Command;

public class PrinterLog extends JComponent implements KeyListener {
	
	private final MemefaceGUI gui;
	
	private final JTextArea logText;
	private final JTextField inputField;
	
	public PrinterLog(MemefaceGUI gui) {
		this.gui = gui;
		setLayout(new BorderLayout());
		add(new JScrollPane(logText = new JTextArea()), BorderLayout.CENTER);
		add(inputField = new JTextField(), BorderLayout.SOUTH);
		
		logText.setEditable(false);
		logText.setLineWrap(true);
		logText.setWrapStyleWord(true);
		((DefaultCaret)logText.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		inputField.addKeyListener(this);
	}
	
	public void log(String s) {
		logText.append(s);
		logText.append("\n");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			Command cmd;
			try {
				cmd = HcodeConverter.parseCommand(inputField.getText());
			} catch(Exception ex) {
				ex.printStackTrace();
				return;
			}
			
			inputField.setText("");
			gui.sendCommand(cmd);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) { }

	@Override
	public void keyTyped(KeyEvent arg0) { }
}
