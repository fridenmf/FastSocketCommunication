package com.friden.fastsocketcommunication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author friden
 * This one is just a slight modification of FastSocketReader, to save preallocate the
 * byte array to save some of the garbage collection when used on Android 
 */
public class FastSocketPreAllocWriter{

	private OutputStream out = null;
	
	public OutputStream getOutputStream(){
		return out;
	}
	
	public FastSocketPreAllocWriter(OutputStream os) throws IOException{
		out = os;		
	}
	
	public void write(byte b) throws IOException{
		out.write(b);
	}
	
	public void writeBytes(byte[] b, int off, int len) throws IOException{
		out.write(b, off, len);
	}
	
	public void writeShort(short s) throws IOException{
		out.write(FastByteOperations.getBytes(s), 0, 2);
	}
	
	public void writeInt(int i) throws IOException {
		out.write(FastByteOperations.getBytes(i), 0, 4);
	}
	
	public void writeLong(long l) throws IOException {
		out.write(FastByteOperations.getBytes(l), 0, 8);
	}
	
	public void writeChar(char c) throws IOException {
		writeShort((short)c);
	}
	
	public void writeFloat(float f) throws IOException {
		writeInt(Float.floatToRawIntBits(f));
	}
	
	public void writeString(String string) throws IOException{
		byte[] bytes = string.getBytes("UTF-8");
		writeInt(bytes.length);
		writeBytes(bytes, 0, bytes.length);
	}
	
	public void writeFile(File file, int bufferSize) throws IOException {
		long fileLength = file.length();
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[bufferSize]; 
		long totalRead = 0;
		while(totalRead < fileLength){
			int toRead = fileLength - totalRead < buffer.length ? (int)(fileLength - totalRead) : buffer.length;
			int justRead = fis.read(buffer, 0, toRead);
			writeBytes(buffer, 0, justRead);
			totalRead += justRead;
		}
		fis.close();		
	}

	/**
	 * As this only calls flush on the outputstream, and the flush method in the output stream does nothing, this also does nothing
	 */
	public void flush() throws IOException {
		out.flush();
	}

}