package fi.teknologiakerho.memeface.gui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

public class TopControls extends JComponent {
	
	private final MemefaceGUI gui;

	private final JButton connectButton, startPrintButton, stopPrintButton;
	private final JCheckBox echoBox;
	
	public TopControls(MemefaceGUI gui) {
		this.gui = gui;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(connectButton = new JButton("Connect"));
		add(startPrintButton = new JButton("Start print"));
		add(stopPrintButton = new JButton("Stop print"));
		add(echoBox = new JCheckBox("Echo commands"));
		//setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		
		echoBox.setSelected(true);
		
		setupCallbacks();
	}
	
	public void setConnectEnabled(boolean enabled) {
		if(enabled)
			connectButton.setText("Connect");
		else
			connectButton.setText("Disconnect");
	}
	
	public boolean echoCommands() {
		return echoBox.isSelected();
	}
	
	private void setupCallbacks() {
		connectButton.addActionListener(e -> {
			new Thread(() -> {
				if(gui.getConnection() != null)
					gui.disconnect();
				else
					gui.connect();
			}).start();
		});
		
		startPrintButton.addActionListener(e -> {
			gui.startPrint();
		});
		
		stopPrintButton.addActionListener(e -> {
			gui.cancelPrint();
		});
	}
	
}
