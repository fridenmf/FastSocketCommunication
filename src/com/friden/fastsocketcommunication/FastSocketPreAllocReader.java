package com.friden.fastsocketcommunication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ClosedChannelException;

/**
 * @author friden
 * This one is just a slight modification of FastSocketReader, to save preallocate the
 * byte array to save some of the garbage collection when used on Android 
 */
public class FastSocketPreAllocReader{

	private InputStream in = null;
	
	private byte[] byteBuffer = null;
	
	public InputStream getInputStream(){
		return in;
	}

	public FastSocketPreAllocReader(InputStream is) throws IOException{
		in  = is;
		byteBuffer = new byte[8];
	}

	public byte readByte() throws ClosedChannelException, IOException {
		int b = in.read();
		if(b == -1){
			throw new ClosedChannelException();
		}
		return (byte)b;
	}

	public int readBytes(byte[] b, int off, int len) throws IOException{
		return in.read(b, off, len);
	}

	public int readShort() throws IOException {
		readBytesFully(byteBuffer, 0, 2, in);
		return FastByteOperations.getShort(byteBuffer);
	}

	public int readInt() throws IOException {
		readBytesFully(byteBuffer, 0, 4, in);
		return FastByteOperations.getInt(byteBuffer);
	}

	public long readLong() throws IOException {
		readBytesFully(byteBuffer, 0, 8, in);
		return FastByteOperations.getLong(byteBuffer);
	}
	
	public float readFloat() throws IOException{
		return Float.intBitsToFloat(readInt());
	}

	public String readString() throws IOException {
		int numBytes = readInt();
		byte[] bytes = new byte[numBytes];
		readBytesFully(bytes, 0, numBytes, in);
		return new String(bytes, "UTF-8");
	}

	public boolean readFile(File outFile, long bytes, int bufferSize) throws IOException{
		
		boolean successful = false;
		byte[] buffer = new byte[bufferSize];
		
		if(!outFile.exists()){
			successful = outFile.createNewFile();
			if(!successful){
				//TODO check this up
				dumpBytes(buffer, bytes);
				return false;
			}
		}
		
		FileOutputStream fos = new FileOutputStream(outFile);
		long totalRead = 0;
		while(totalRead < bytes){
			
			int toRead = bytes - totalRead < buffer.length ? (int)(bytes - totalRead) : buffer.length;
			
			readBytesFully(buffer, 0, toRead, in);
			fos.write(buffer, 0, toRead);
			fos.flush();
			totalRead += toRead;
			
		}
		fos.close();
		return true;
	}
	
	private void dumpBytes(byte[] buffer, long bytes) throws IOException {
		long totalRead = 0;
		while(totalRead < bytes){
			int toRead = (int) (buffer.length < bytes - totalRead ? buffer.length : bytes - totalRead);
			int read = in.read(buffer, 0, toRead);
			if(read > 0){
				totalRead += read;
			}
		}
	}

	public char readChar() throws IOException {
		byte[] b = new byte[2]; 
		readBytesFully(b, 0, 2, in);
		return FastByteOperations.getChar(b);
	}
	
	public static void readBytesFully(byte[] b, int off, int len, InputStream stream) throws IOException{
		int read = 0;
		while(read < len){
			int justRead = stream.read(b, off + read, len - read);
			if(justRead == -1){
				//TODO error here?
				return;
			}else{
				read += justRead;
			}
		}
	}

}