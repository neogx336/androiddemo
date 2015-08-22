package com.neo.mobilesafe.db.dao;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.neo.mobilesafe.db.BlackNumberDBOpenHelper;
import com.neo.mobilesafe.domain.BlackNumberInfo;

/*
 * 黑名单数据库的增删改查业务类
 * 
 */

public class BlackNumberDao {
	private BlackNumberDBOpenHelper helper;
	private static String BlackNumberTable = "blacknumber";

	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}

	/*
	 * 下面的业务代码集
	 */

	/**
	 * 查询黑名单号码是否存在
	 * 
	 * @param number
	 * @return
	 */

	public boolean find(String number) {
		boolean result = false;
		SQLiteDatabase dbDatabase = helper.getWritableDatabase();
		// 打开数据库得到游标
		Cursor cursor = dbDatabase.rawQuery(
				"select number from blacknumber where number=?",
				new String[] { number });
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		dbDatabase.close();
		return result;
	}

	/**
	 * 查询黑名单号码的拦截模式
	 * 
	 * @param number
	 * @return
	 */
	public String findMode(String number) {
		String result = null;
		SQLiteDatabase dbDatabase = helper.getWritableDatabase();
		// 打开数据库得到游标
		Cursor cursor = dbDatabase.rawQuery(
				"select mode from blacknumber where number=?",
				new String[] { number });
		if (cursor.moveToNext()) {
			result = cursor.getString(0);
		}
		cursor.close();
		dbDatabase.close();
		return result;
	}

	/**
	 * 查询全部黑名单
	 * 
	 * @return
	 */

	public List<BlackNumberInfo> findAll() {
		List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase dbDatabase = helper.getWritableDatabase();
		// 打开数据库得到游标
		Cursor cursor = dbDatabase.rawQuery(
				"select number,mode from blacknumber order by _id desc", null);
		while (cursor.moveToNext()) {
			// 得到一行,取每一列的数据
			BlackNumberInfo info = new BlackNumberInfo();
			// 添加到Info
			String numberString = cursor.getString(0);
			String modeString = cursor.getString(1);
			info.setNumber(numberString);
			info.setMode(modeString);
			// 添加到列表
			result.add(info);
		}
		// 释放资源
		cursor.close();
		dbDatabase.close();
		return result;
	}

	/**
	 * 黑名单号码的增，删，改
	 * 
	 * 
	 */
	/**
	 * 添加黑名单号码
	 * 
	 * @param number
	 *            黑名单号码
	 * @param mode
	 *            拦截模式 1.电话拦截 2.短信拦截 3.全部拦截
	 */

	public void add(String number, String mode) {
		SQLiteDatabase dbDatabase = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		dbDatabase.insert(BlackNumberTable, null, values);
		dbDatabase.close();
	}

	/**
	 * 修改黑名号码的拦截模式
	 * 
	 * @param number
	 *            要更新的号码
	 * @param newmode
	 *            要更新的模式
	 */
	public void update(String number, String newmode) {
		SQLiteDatabase dbDatabase = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newmode);
		dbDatabase.update(BlackNumberTable, values, "number=?",
				new String[] { number });
		dbDatabase.close();
	}

	/**
	 * 删除号码
	 * 
	 * @param number
	 *            要删除的号码
	 */

	public void delete(String number) {
		SQLiteDatabase dbDatabase = helper.getWritableDatabase();
		dbDatabase
				.delete(BlackNumberTable, "number=?", new String[] { number });
		dbDatabase.close();
	}

}
