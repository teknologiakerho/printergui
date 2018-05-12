package fi.teknologiakerho.memeface;

public class Util {
	
	public static String dumpByteArray(byte[] b, int off, int len) {
		return dumpByteArray(b, off, len, 8);
	}
	
	public static String dumpByteArray(byte[] b, int off, int len, int width) {
		StringBuilder ret = new StringBuilder();

		for(int i=0;i<len;i++) {
			ret.append(String.format("%02x", b[off+i]));
			ret.append(((i+1)%width) == 0 ? '\n' : ' ');
		}
		
		return ret.toString();
	}

}
