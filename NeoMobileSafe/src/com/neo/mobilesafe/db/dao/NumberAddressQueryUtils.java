package com.neo.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryUtils {
	private static String path = "data/data/com.neo.mobilesafe/files/address.db";

	public static String queryNumber(String number) {
		String address = number;
		// path 把address.db这个数据库拷贝到data/data/《包名》/files/address.db
		SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		String sqlString="select location from data2 where id = (select outkey from data1 where id = ?)";
		if (number.matches("^1[34568]\\{9}$")) {
			Cursor cursor = database
					.rawQuery(
							sqlString,
							new String[] { number.substring(0, 7) });
			while (cursor.moveToNext()) {
				String location=cursor.getString(0);
				address=location;
			}
			
		}
		else {
			//其他号码 的处理逻辑
			switch (number.length()) {
			
			case 3:
				address="匪警号码";
				break;
				
			case 4:
				address="摸拟器";
				break;

			default:
				
				if (number.length()>10&&number.startsWith("0")) {
					// 010-59790386
					Cursor cursor = database.rawQuery(
							"select location from data2 where area = ?",
							new String[] { number.substring(1, 3) });
					while (cursor.moveToNext()) {
						String location=cursor.getString(0);
						address=location.substring(0,location.length()-2);
					}
					cursor.close();
				}
				break;
			}
			
		}
		return address;
	}
}
