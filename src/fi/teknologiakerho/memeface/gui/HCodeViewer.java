package fi.teknologiakerho.memeface.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JComponent;

import fi.teknologiakerho.memeface.Config;
import fi.teknologiakerho.viipal01ja.hcode.Command;
import fi.teknologiakerho.viipal01ja.hcode.HCode;
import fi.teknologiakerho.viipal01ja.hcode.MoveZCommand;
import fi.teknologiakerho.viipal01ja.hcode.PositionCommand;

public class HCodeViewer extends JComponent {
	
	private final MemefaceGUI gui;
	
	private HCode hcode;
	private long minX, minY, maxX, maxY;
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
		
		minX = Long.MAX_VALUE;
		minY = Long.MAX_VALUE;
		maxX = Long.MIN_VALUE;
		maxY = Long.MIN_VALUE;
		
		for(Command cmd : hcode.commands) {
			char c = cmd.getCmd();
			if(c == 'm' || c == 'M') {
				PositionCommand p = (PositionCommand) cmd;
				long x = (long) (Config.COORD_RESOLUTION * p.x);
				long y = (long) (Config.COORD_RESOLUTION * p.y);
				if(x < minX) minX = x;
				if(x > maxX) maxX = x;
				if(y < minY) minY = y;
				if(y > maxY) maxY = y;
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
			((double)getWidth()) / ((double)(maxX - minX)),
			((double)getHeight()) / ((double)(maxY - minY))
		);
		
		boolean penDown = true;
		long x = 0, y = 0;

		for(int i=from;i<to;i++) {
			Command cmd = cmds.get(i);
			char c = cmd.getCmd();
			long mx, my;
			
			if(c == 'M' || c == 'm') {
				PositionCommand pc = (PositionCommand) cmd;
				mx = (long) (Config.COORD_RESOLUTION * pc.x);
				my = (long) (Config.COORD_RESOLUTION * pc.y);
				long x2 = c == 'M' ? mx : (x+mx);
				long y2 = c == 'M' ? my : (y+my);
				
				if(penDown) {
					g.drawLine(
						(int) (scale*(x-minX)),
						(int) (scale*(y-minY)),
						(int) (scale*(x2-minX)),
						(int) (scale*(y2-minY))
					);
				}
				
				x = x2;
				y = y2;
			}else if(c == 'Z') {
				penDown = !((MoveZCommand)cmd).up;
			}

		}
	}
	
}
