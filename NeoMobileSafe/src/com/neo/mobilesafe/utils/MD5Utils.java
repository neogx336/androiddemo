package com.neo.mobilesafe.utils;

import java.security.MessageDigest;

public class MD5Utils {
	
	public static String md5Password( String password) {
		
		try {
			MessageDigest digest=MessageDigest.getInstance("md5");
			byte [] result =digest.digest(password.getBytes());
			StringBuffer buffer=new StringBuffer();
			for(byte b:result){
				int number=b&0xff;
				String str=Integer.toHexString(number);
				if (str.length()==1) {
					buffer.append("0");
				}
					buffer.append(str);								
			}			
			return buffer.toString();
			
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
		
	}

}
