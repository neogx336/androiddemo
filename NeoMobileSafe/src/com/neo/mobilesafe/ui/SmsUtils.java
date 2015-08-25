package com.neo.mobilesafe.ui;

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
 * 短信接口工具类
 * @author Lenovo
 *
 */

public class SmsUtils {

	
	/**
	 * 备份短信回调接口
	 * @author Lenovo
	 *
	 */
	public interface BackUpCallBack{
		
		/**
		 * 总进度
		 * @param max
		 */
		public void beforeBackup(int max);
		/**
		 * 备份过程中，增加进度
		 * @param progress
		 */
		public void onSmsBackup (int progress);
	}
	
	
	
	/**
	 * 备份短信接口
	 * @param context
	 * @param callBack  调用回调接口  
	 */
	
	public static void backupSms(Context context,BackUpCallBack callBack)
	throws Exception{
		ContentResolver contentResolver=context.getContentResolver();
		File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
		FileOutputStream foStream=new FileOutputStream(file);
		//把用户的短信一条一条读出来,按照一定的格式写到文件里
		//获取XM文件的生成器(序列化器)
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
			
			//拼字符串
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
	 * 还原短信接口
	 * @param context
	 * @param flag  是否清除原来短信
	 */
	public static void restoreSms(Context context,boolean flag){
		Uri uri=Uri.parse("content://sms/");
		if (flag) {
			context.getContentResolver().delete(uri, null, null);
		}
		
		// 1.读取sd卡上的xml文件
		// Xml.newPullParser();

		// 2.读取max

		// 3.读取每一条短信信息，body date type address

		// 4.把短信插入到系统短息应用。
		ContentValues values=new ContentValues();
		values.put("body", "wosh dasdfasdfasdf");
		values.put("date", "wosh 1395045035573");
		values.put("type", "1");
		values.put("address", "5558");
		context.getContentResolver().insert(uri, values);
	}
	
	
	
	
	
	
	
}
