package fi.teknologiakerho.memeface.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JComponent;

import fi.teknologiakerho.viipal01ja.hcode.Command;
import fi.teknologiakerho.viipal01ja.hcode.HCode;
import fi.teknologiakerho.viipal01ja.hcode.MoveCommand;
import fi.teknologiakerho.viipal01ja.hcode.MoveRelativeCommand;
import fi.teknologiakerho.viipal01ja.hcode.MoveZCommand;
import fi.teknologiakerho.viipal01ja.hcode.PositionCommand;

public class HCodeViewer extends JComponent {
	
	private final MemefaceGUI gui;
	
	private HCode hcode;
	private int hWidth, hHeight;
	private int progress;
	
	public HCodeViewer(MemefaceGUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.GRAY);
		if(hcode == null) {
			g.drawString("No path loaded", getWidth()/2, getHeight()/2);
		}else {
			drawHCode(g, hcode.commands, 0, hcode.commands.size());
			g.setColor(Color.RED);
			drawHCode(g, hcode.commands, 0, progress);
		}
	}
	
	public void setHCode(HCode hcode) {
		this.hcode = hcode;
		
		hWidth = 0;
		hHeight = 0;
		
		for(Command cmd : hcode.commands) {
			char c = cmd.getCmd();
			if(c == 'm' || c == 'M') {
				PositionCommand p = (PositionCommand) cmd;
				if(p.x > hWidth) hWidth = p.x;
				if(p.y > hHeight) hHeight = p.y;
			}
		}
		
		progress = 0;
		repaint();
	}
	
	public void setProgress(int progress) {
		if(progress != this.progress) {
			this.progress = progress;
			repaint();
		}
	}
	
	private void drawHCode(Graphics g, List<Command> cmds, int from, int to) {
		double scale = Math.min(
			((double)getWidth()) / ((double)hWidth),
			((double)getHeight()) / ((double)hHeight)
		);
		
		boolean penDown = true;
		int x = 0, y = 0;

		for(int i=from;i<to;i++) {
			Command cmd = cmds.get(i);
			char c = cmd.getCmd();
			switch(c) {
				case 'M':
					MoveCommand mv = (MoveCommand) cmd;
					if(penDown)
						g.drawLine((int)(scale*x), (int)(scale*y), (int)(scale*mv.x), (int)(scale*mv.y));
					x = mv.x;
					y = mv.y;
					break;
				case 'm':
					MoveRelativeCommand mvr = (MoveRelativeCommand) cmd;
					if(penDown)
						g.drawLine((int)(scale*x), (int)(scale*y), (int)(scale*(x+mvr.x)), (int)(scale*(y+mvr.y)));
					x += mvr.x;
					y += mvr.y;
					break;
				case 'Z':
					penDown = !((MoveZCommand)cmd).up;
					break;
			}
		}
	}
	
}
