package com.friden.fastsocketcommunication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ClosedChannelException;

public class FastSocketReader{

	private InputStream in = null;

	public FastSocketReader(InputStream is) throws IOException{
		in  = is;
	}

	public byte readByte() throws ClosedChannelException, IOException {
		int b = in.read();
		if(b == -1){
			throw new ClosedChannelException();
		}
		return (byte)b;
	}

	public void readBytesFully(byte[] b, int off, int len) throws IOException{
		int read = 0;
		while(read < len){
			int justRead = in.read(b, off + read, len - read);
			if(justRead == -1){
				//TODO error?
				return;
			}else{
				read += justRead;
			}
		}
	}
	
	public int readBytes(byte[] b, int off, int len) throws IOException{
		return in.read(b, off, len);
	}

	public int readShort() throws IOException {
		byte[] b = new byte[2];
		readBytesFully(b, 0, 2);
		return FastByteOperations.getShort(b);
	}

	public int readInt() throws IOException {
		byte[] b = new byte[4]; 
		readBytesFully(b, 0, 4);
		return FastByteOperations.getInt(b);
	}

	public long readLong() throws IOException {
		byte[] b = new byte[8];
		readBytesFully(b, 0, 8);
		return FastByteOperations.getLong(b);
	}
	
	public float readFloat() throws IOException{
		return Float.intBitsToFloat(readInt());
	}

	public String readString() throws IOException {
		int numBytes = readInt();
		byte[] bytes = new byte[numBytes];
		readBytesFully(bytes, 0, numBytes);
		return new String(bytes, "UTF-8");
	}

	public boolean readFile(File outFile, long bytes, int bufferSize) throws IOException{
		
		boolean successful = false;
		byte[] buffer = new byte[bufferSize];
		
		if(!outFile.exists()){
			successful = outFile.createNewFile();
			if(!successful){
				//TODO check this
				dumpBytes(buffer, bytes);
				return false;
			}
		}
		
		FileOutputStream fos = new FileOutputStream(outFile);
		long totalRead = 0;
		while(totalRead < bytes){
			
			int toRead = bytes - totalRead < buffer.length ? (int)(bytes - totalRead) : buffer.length;
			
			readBytesFully(buffer, 0, toRead);
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
		readBytesFully(b, 0, 2);
		return FastByteOperations.getChar(b);
	}
	
	public InputStream getInputStream(){
		return in;
	}
	
	public void close() throws IOException{
		in.close();
	}

}