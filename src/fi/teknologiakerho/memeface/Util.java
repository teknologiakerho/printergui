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
	
	public static void writeInt(byte[] dest, int off, int src) {
		dest[off] = (byte) (src & 0xff);
		dest[off+1] = (byte) ((src >> 8) & 0xff);
	}
	
	public static void writeLong(byte[] dest, int off, long src) {
		dest[off] = (byte) (src & 0xff);
		dest[off+1] = (byte) ((src >> 8) & 0xff);
		dest[off+2] = (byte) ((src >> 16) & 0xff);
		dest[off+3] = (byte) ((src >> 24) & 0xff);
		System.out.println("writeLong " + src);
	}

}
