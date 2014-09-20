package com.friden.fastsocketcommunication;
import java.util.Arrays;

public class FastByteOperations {

	public static final byte BYTE_ZERO = 0;
	public static final byte BYTE_SIZE = 8;
	public static final byte BYTE_MAX  = 127;
	public static final byte BYTE_MIN  = -128;

	public static final long halfBitsOfLong = 4294967296l; 

	public static final short SHORT_TEST = 255;

	public static byte[] getBytes(short shortvalue){
		return new byte[]{
				(byte)(shortvalue >> BYTE_SIZE), 
				(byte)(shortvalue & SHORT_TEST)
		};
	}
	
	public static byte[] getBytes(short shortvalue, byte[] buffer){
		buffer[0] = (byte)(shortvalue >> BYTE_SIZE);
		buffer[1] = (byte)(shortvalue & SHORT_TEST);
		return buffer;
	}
	
	public static byte[] getBytes(int intvalue){
		byte[] result = new byte[]{
				(byte)(intvalue >> BYTE_SIZE * 3 & SHORT_TEST), 
				(byte)(intvalue >> BYTE_SIZE * 2 & SHORT_TEST),
				(byte)(intvalue >> BYTE_SIZE * 1 & SHORT_TEST),
				(byte)(intvalue >> BYTE_SIZE * 0 & SHORT_TEST)
		};
		return result;
	}

	public static byte[] getBytes(int intvalue, byte[] buffer){
		buffer[0] = (byte)(intvalue >> BYTE_SIZE * 3 & SHORT_TEST); 
		buffer[1] = (byte)(intvalue >> BYTE_SIZE * 2 & SHORT_TEST);
		buffer[2] = (byte)(intvalue >> BYTE_SIZE * 1 & SHORT_TEST);
		buffer[3] = (byte)(intvalue >> BYTE_SIZE * 0 & SHORT_TEST);
		return buffer;
	}

	public static void setBytes(int intvalue, byte[] toPutIn, int startPos){
		toPutIn[startPos+0] = (byte)(intvalue >> BYTE_SIZE * 3 & SHORT_TEST); 
		toPutIn[startPos+1] = (byte)(intvalue >> BYTE_SIZE * 2 & SHORT_TEST);
		toPutIn[startPos+2] = (byte)(intvalue >> BYTE_SIZE * 1 & SHORT_TEST);
		toPutIn[startPos+3] = (byte)(intvalue >> BYTE_SIZE * 0 & SHORT_TEST);
	}

	public static byte[] getBytes(long longvalue){
		byte[] result = new byte[8];
		setBytes((int)(longvalue >> 32), result, 0);
		setBytes((int)longvalue, result, 4);
		return result;
	}
	
	public static byte[] getBytes(long longvalue, byte[] buffer){
		setBytes((int)(longvalue >> 32), buffer, 0);
		setBytes((int)longvalue, buffer, 4);
		return buffer;
	}

	public static byte[] getBytes(char c){
		return getBytes((short)c);
	}
	
	public static byte[] getBytes(char c, byte[] buffer){
		return getBytes((short)c, buffer);
	}

	public static char getChar(byte[] bytes){
		return (char)getShort(bytes);
	}

	public static short getShort(byte[] bytes){
		return (short)((bytes[0] << 8) | (bytes[1] & SHORT_TEST));
	}

	public static int getInt(byte[] bytes){
		return ((bytes[0] << 24) & (0xff << 24)) | 
			   ((bytes[1] << 16) & (0xff << 16)) | 
			   ((bytes[2] << 8 ) & (0xff << 8 )) |  
			    (bytes[3] & 0xff);
	}
	
	public static int getInt(byte[] bytes, int off){
		return ((bytes[0+off] << 24) & (0xff << 24)) | 
			   ((bytes[1+off] << 16) & (0xff << 16)) |
			   ((bytes[2+off] << 8 ) & (0xff << 8 )) |  
			    (bytes[3+off] & 0xff);
	}

	public static long getLong(byte[] bytes){
		byte[] leftside = Arrays.copyOfRange(bytes, 0, 4);
		byte[] rightside = Arrays.copyOfRange(bytes, 4, 8);
		return halfBitsOfLong * ((long)getInt(leftside) & (halfBitsOfLong-1l)) + 
			((long)getInt(rightside) & (halfBitsOfLong-1l));
	}

	public static void printBytes(byte[] bytes){
		System.out.print("[");
		for (int i = 0; i < bytes.length; i++) {
			System.out.print(bytes[i]);
			if(i != bytes.length - 1){
				System.out.print(", ");
			}
		}
		System.out.println("]");
	}

}