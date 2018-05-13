package fi.teknologiakerho.memeface.gui;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;

import fi.teknologiakerho.viipal01ja.gui.UIUtil;
import fi.teknologiakerho.viipal01ja.hcode.Command;
import fi.teknologiakerho.viipal01ja.hcode.MoveRelativeCommand;

public class PrinterControls extends JComponent {
	
	private final MemefaceGUI gui;
	
	private final JButton plusXButton, minusXButton, plusYButton, minusYButton;
	private final JSlider moveAmtSlider;
	
	public PrinterControls(MemefaceGUI gui) {
		this.gui = gui;

		setLayout(new GridBagLayout());

		add(plusYButton = new MoveButton("+Y", 0, 1), UIUtil.gridbag(1, 0, 1, 1, 1, 0));
		add(minusXButton = new MoveButton("-X", -1, 0), UIUtil.gridbag(0, 1, 1, 1, 1, 0));
		add(plusXButton = new MoveButton("+X", 1, 0), UIUtil.gridbag(2, 1, 1, 1, 1, 0));
		add(minusYButton = new MoveButton("-Y", 0, -1), UIUtil.gridbag(1, 2));
		
		add(new JLabel("Move amount (mm):"), UIUtil.gridbagPad(0, 3, 3, 1, 0, 40));
		add(moveAmtSlider = new JSlider(0, 50, 5), UIUtil.gridbag(0, 4, 3, 1));
		moveAmtSlider.setMajorTickSpacing(10);
		moveAmtSlider.setPaintTicks(true);
		moveAmtSlider.setPaintLabels(true);
	}
	
	private class MoveButton extends JButton implements ActionListener {
		
		final int mx, my;
		
		MoveButton(String name, int mx, int my){
			super(name);
			this.mx = mx;
			this.my = my;
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int amt = moveAmtSlider.getValue();
			gui.sendCommand(new MoveRelativeCommand(mx*amt, my*amt));
		}

	}
	
	private class CommandButton extends JButton {
		
		final Command command;
		
		CommandButton(String text, Command command) {
			super(text);
			this.command = command;
			addActionListener(e -> gui.sendCommand(this.command));
		}

	}
	
}
