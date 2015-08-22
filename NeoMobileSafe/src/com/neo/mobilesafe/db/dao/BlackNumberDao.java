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
 * ���������ݿ����ɾ�Ĳ�ҵ����
 * 
 */

public class BlackNumberDao {
	private BlackNumberDBOpenHelper helper;
	private static String BlackNumberTable = "blacknumber";

	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}

	/*
	 * �����ҵ����뼯
	 */

	/**
	 * ��ѯ�����������Ƿ����
	 * 
	 * @param number
	 * @return
	 */

	public boolean find(String number) {
		boolean result = false;
		SQLiteDatabase dbDatabase = helper.getWritableDatabase();
		// �����ݿ�õ��α�
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
	 * ��ѯ���������������ģʽ
	 * 
	 * @param number
	 * @return
	 */
	public String findMode(String number) {
		String result = null;
		SQLiteDatabase dbDatabase = helper.getWritableDatabase();
		// �����ݿ�õ��α�
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
	 * ��ѯȫ��������
	 * 
	 * @return
	 */

	public List<BlackNumberInfo> findAll() {
		List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase dbDatabase = helper.getWritableDatabase();
		// �����ݿ�õ��α�
		Cursor cursor = dbDatabase.rawQuery(
				"select number,mode from blacknumber order by _id desc", null);
		while (cursor.moveToNext()) {
			// �õ�һ��,ȡÿһ�е�����
			BlackNumberInfo info = new BlackNumberInfo();
			// ��ӵ�Info
			String numberString = cursor.getString(0);
			String modeString = cursor.getString(1);
			info.setNumber(numberString);
			info.setMode(modeString);
			// ��ӵ��б�
			result.add(info);
		}
		// �ͷ���Դ
		cursor.close();
		dbDatabase.close();
		return result;
	}

	/**
	 * ���������������ɾ����
	 * 
	 * 
	 */
	/**
	 * ��Ӻ���������
	 * 
	 * @param number
	 *            ����������
	 * @param mode
	 *            ����ģʽ 1.�绰���� 2.�������� 3.ȫ������
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
	 * �޸ĺ������������ģʽ
	 * 
	 * @param number
	 *            Ҫ���µĺ���
	 * @param newmode
	 *            Ҫ���µ�ģʽ
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
	 * ɾ������
	 * 
	 * @param number
	 *            Ҫɾ���ĺ���
	 */

	public void delete(String number) {
		SQLiteDatabase dbDatabase = helper.getWritableDatabase();
		dbDatabase
				.delete(BlackNumberTable, "number=?", new String[] { number });
		dbDatabase.close();
	}

}
