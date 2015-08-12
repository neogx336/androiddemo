package com.neo.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {
	
	/**
	 * @param is 输入流
	 * @return String 返回的字符串
	 * @throws IOException 
	 */
	
	public static String readFromString(InputStream is)  throws IOException{
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		byte[] buff=new byte[2048];
		int len=0;
		while ((len=is.read(buff))!=-1) {
			baos.write(buff, 0, len);
		}
		is.close();
		String resultString=baos.toString();
		baos.close();
		return resultString;
				
	}

}
