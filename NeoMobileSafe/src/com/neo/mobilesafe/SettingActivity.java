package com.neo.mobilesafe;

import com.neo.mobilesafe.service.AddressService;
import com.neo.mobilesafe.ui.SettingClickView;
import com.neo.mobilesafe.ui.SettingItemView;
import com.neo.mobilesafe.utils.ServiceUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

	// ���ÿ����Զ�����
	private SettingItemView siv_update;
	private SharedPreferences sp;

	// �������Ƿ����Թ�������
	private SettingItemView siv_show_address;
	private Intent showAddress;

	// ���ù�������ʾ�򱳾�
	private SettingClickView scv_changebg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		/*
		 * ��Ҫ��ʼ�������� 1.�Զ����µ�ITEM 2.�����ط���ITEM 3.����ͼITEM
		 */
		// ���ò���
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		/*
		 * ��һ���� �Զ�����
		 */

		// ʵʼ��
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		siv_update.setTitle("�Զ�����ѡ��");
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean update = sp.getBoolean("update", false);

		if (update) {
			siv_update.setChecked(true);
			siv_update.setDesc("����");

		} else {
			siv_update.setChecked(false);
			siv_update.setDesc("�ر�");

		}

		siv_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();

				if (siv_update.isChecked()) {
					siv_update.setChecked(false);
					siv_update.setDesc("�ر�");
					editor.putBoolean("update", false);

				} else {
					siv_update.setChecked(true);
					siv_update.setDesc("����");
					editor.putBoolean("update", true);

				}
				editor.commit();

			}
		});

		/*
		 * �ڶ����� ������
		 */
		siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
		showAddress = new Intent(this, AddressService.class);
		boolean isServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.neo.mobilesafe.service.AddressService");
		if (isServiceRunning) {
			siv_show_address.setChecked(true);
		} else {
			siv_show_address.setChecked(false);
		}

		siv_show_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �������״̬
				// TODO Auto-generated method stub
				if (siv_show_address.isChecked()) {
					siv_show_address.setChecked(false);
					stopService(showAddress);
				} else {
					siv_show_address.setChecked(true);
					startService(showAddress);
				}
			}
		});

		/*
		 * �������� ����ѡ��ͼ
		 */

		scv_changebg = (SettingClickView) findViewById(R.id.scv_changebg);
		scv_changebg.setTitle("��������ʾ����");
		final String[] items = { "��͸��", "������", "��ʿ��", "������", "ƻ����" };
		int which = sp.getInt("which", 0);
		scv_changebg.setDesc(items[which]);

		scv_changebg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �����Ի���ѡ����ɫ
				int dd = sp.getInt("which", 0);

				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("��������ʾ���");
				builder.setSingleChoiceItems(items, dd,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								Editor editor = sp.edit();
								editor.putInt("which", which);
								editor.commit();
								scv_changebg.setDesc(items[which]);
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("cancel", null);
				builder.show();
			}
		});
	}

	@Override
	protected void onResume() {
		// ��ֹ���񱻹ص�
		// ���ٽ���ʱ�����ж�
		super.onResume();

		showAddress = new Intent(this, AddressService.class);
		boolean isServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.neo.mobilesafe.service.AddressService");
		if (isServiceRunning) {
			// ��������ķ����ǿ�����
			siv_show_address.setChecked(true);
		} else {
			siv_show_address.setChecked(false);
		}

	}

}
