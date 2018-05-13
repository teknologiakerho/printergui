package fi.teknologiakerho.memeface.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import fi.teknologiakerho.memeface.Config;
import fi.teknologiakerho.memeface.Util;
import fi.teknologiakerho.viipal01ja.hcode.Command;
import fi.teknologiakerho.viipal01ja.hcode.HCode;
import fi.teknologiakerho.viipal01ja.hcode.MoveCommand;
import fi.teknologiakerho.viipal01ja.hcode.MoveRelativeCommand;
import fi.teknologiakerho.viipal01ja.hcode.MoveZCommand;
import fi.teknologiakerho.viipal01ja.hcode.PositionCommand;
import fi.teknologiakerho.viipal01ja.hcode.ResetCommand;
import fi.teknologiakerho.viipal01ja.hcode.SetIPCommand;
import fi.teknologiakerho.viipal01ja.hcode.SetZeroCommand;

public class HcodeConverter {
	
	public static int encodeCommand(byte[] dest, int off, Command cmd) {
		char c = cmd.getCmd();
		dest[off] = (byte) c; 

		switch(c) {
			case 'M':
			case 'm':
				PositionCommand pcmd = (PositionCommand) cmd;
				Util.writeLong(dest, off+1, (long) (Config.COORD_RESOLUTION * pcmd.x));
				Util.writeLong(dest, off+5, (long) (Config.COORD_RESOLUTION * pcmd.y));
				return 9;

			case 'Z':
				dest[off+1] = (byte) (((MoveZCommand)cmd).up ? 1 : 0);
				return 2;
				
			case 'I':
				Util.writeInt(dest, off+1, ((SetIPCommand)cmd).ip);
				return 3;
		}
		
		return 1;
	}
	
	public static int commandSize(char c) {
		switch(c) {
			case 'M':
			case 'm':
				return 9;
			case 'Z':
				return 2;
			case 'I':
				return 3;
		}
		return 1;
	}
	
	public static Command parseCommand(String s) {
		char c = s.charAt(0);
		s = s.length() > 1 ? s.substring(2).trim() : "";
		
		switch(c) {
			case 'M':
			case 'm':
				String[] pos = s.split("\\s+");
				double x = Double.parseDouble(pos[0]), y = Double.parseDouble(pos[1]);
				return c == 'M' ? new MoveCommand(x, y) : new MoveRelativeCommand(x, y);
				
			case 'Z':
				return new MoveZCommand(Integer.parseInt(s) != 0);
				
			case 'z':
				return new SetZeroCommand();

			case 'R':
				return new ResetCommand();
				
			case 'I':
				return new SetIPCommand(Integer.parseInt(s));
		}
		
		return null;
	}
	
	public static HCode parseHCode(File f) {
		try(BufferedReader in = new BufferedReader(new FileReader(f))){
			HCode ret = new HCode();
			String s;
			while((s = in.readLine()) != null) {
				ret.commands.add(parseCommand(s));
			}
			return ret;
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
