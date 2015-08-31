package com.neo.mobilesafe.utils;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.R.dimen;
import android.R.integer;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

/**
 * ���Žӿڹ�����
 * @author Lenovo
 *
 */

public class SmsUtils {

	
	/**
	 * ���ݶ��Żص��ӿ�
	 * @author Lenovo
	 *
	 */
	public interface BackUpCallBack{
		
		/**
		 * �ܽ���
		 * @param max
		 */
		public void beforeBackup(int max);
		/**
		 * ���ݹ����У����ӽ���
		 * @param progress
		 */
		public void onSmsBackup (int progress);
	}
	
	
	
	/**
	 * ���ݶ��Žӿ�
	 * @param context
	 * @param callBack  ���ûص��ӿ�  
	 */
	
	public static void backupSms(Context context,BackUpCallBack callBack)
	throws Exception{
		ContentResolver contentResolver=context.getContentResolver();
		File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
		FileOutputStream foStream=new FileOutputStream(file);
		//���û��Ķ���һ��һ��������,����һ���ĸ�ʽд���ļ���
		//��ȡXM�ļ���������(���л���)
		XmlSerializer serializer=Xml.newSerializer();
		serializer.setOutput(foStream, "utf-8");
		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "smss");
		Uri uri=Uri.parse("content://sms/");
		Cursor cursor=contentResolver.query(uri, new String [] {"body","address","type","date"}, null, null, null);
		
		int max=cursor.getCount();
		callBack.beforeBackup(max);
		serializer.attribute(null, "max", max+"");
		int process=0;
		while (cursor.moveToNext()) {
			Thread.sleep(500);
			String body=cursor.getString(0);
			String address=cursor.getString(0);
			String type=cursor.getString(0);
			String date=cursor.getString(0);
			
			//ƴ�ַ���
			serializer.startTag(null, "sms");
			
			serializer.startTag(null, "body");
			serializer.text(body);
			serializer.endTag(null, "body");
			
			serializer.startTag(null, "address");
			serializer.text(address);
			serializer.endTag(null, "address");
			
			serializer.startTag(null, "type");
			serializer.text(type);
			serializer.endTag(null, "type");
			
			serializer.startTag(null, "date");
			serializer.text(date);
			serializer.endTag(null, "date");
			
			serializer.endTag(null, "sms");
			process++;
			callBack.onSmsBackup(process);
		}	
		
		cursor.close();
		serializer.endTag(null, "smss");
		foStream.close();
		
	}
	
	/**
	 * ��ԭ���Žӿ�
	 * @param context
	 * @param flag  �Ƿ����ԭ������
	 */
	public static void restoreSms(Context context,boolean flag){
		Uri uri=Uri.parse("content://sms/");
		if (flag) {
			context.getContentResolver().delete(uri, null, null);
		}
		
		// 1.��ȡsd���ϵ�xml�ļ�
		// Xml.newPullParser();

		// 2.��ȡmax

		// 3.��ȡÿһ��������Ϣ��body date type address

		// 4.�Ѷ��Ų��뵽ϵͳ��ϢӦ�á�
		ContentValues values=new ContentValues();
		values.put("body", "wosh dasdfasdfasdf");
		values.put("date", "wosh 1395045035573");
		values.put("type", "1");
		values.put("address", "5558");
		context.getContentResolver().insert(uri, values);
	}
	
	
	
	
	
	
	
}
