package fi.teknologiakerho.memeface.gui;

import java.awt.GridBagConstraints;

public class UIUtil {
	
	public static GridBagConstraints gridbag(int x, int y) {
		GridBagConstraints ret = new GridBagConstraints();
		ret.gridx = x;
		ret.gridy = y;
		return ret;
	}
	
	public static GridBagConstraints gridbag(int x, int y, int w, int h) {
		GridBagConstraints ret = gridbag(x, y);
		ret.gridwidth = w;
		ret.gridheight = h;
		return ret;
	}

	public static GridBagConstraints gridbag(int x, int y, int w, int h, double wx, double wy) {
		GridBagConstraints ret = gridbag(x, y, w, h);
		ret.weightx = wx;
		ret.weighty = wy;
		return ret;
	}
	
	public static GridBagConstraints gridbagPad(int x, int y, int w, int h, int px, int py) {
		GridBagConstraints ret = gridbag(x, y, w, h);
		ret.ipadx = px;
		ret.ipady = py;
		return ret;
	}
}
