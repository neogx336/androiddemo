package com.neo.mobilesafe;

import com.neo.mobilesafe.service.AddressService;
import com.neo.mobilesafe.service.CallSmsSafeService;
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

	// 设置开启自动更新
	private SettingItemView siv_update;
	private SharedPreferences sp;

	// 设置是是否开启显归显属地
	private SettingItemView siv_show_address;
	private Intent showAddress;

	// 设置归属地显示框背景
	private SettingClickView scv_changebg;

	// 设置黑名单
	private SettingItemView siv_callsms_safe;
	private Intent callSmsSafeIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		/*
		 * 需要初始化三部份 1.自动更新的ITEM 2.归属地服务ITEM 3.背景图ITEM
		 */
		// 设置布局
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		// 1:自动更新条目
		initUpdateItem();

		/*
		 * 2. 归属地
		 */
		initAddressItem();

		// 3.黑名单设置
		initBlackNumber();

	}

	private void initBlackNumber() {
		// TODO Auto-generated method stub

		siv_callsms_safe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
		callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
		boolean isServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.neo.mobilesafe.service.CallSmsSafeService");
		if (isServiceRunning) {
			siv_callsms_safe.setChecked(true);
		} else {
			siv_callsms_safe.setChecked(false);
		}
		siv_callsms_safe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 更变服务状态
				// TODO Auto-generated method stub
				if (siv_callsms_safe.isChecked()) {
					siv_callsms_safe.setChecked(false);
					stopService(callSmsSafeIntent);
				} else {
					siv_callsms_safe.setChecked(true);
					startService(callSmsSafeIntent);
				}
			}
		});
	}

	private void initAddressItem() {
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
				// 更变服务状态
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
		 * 第三部份 背景选择图
		 */

		scv_changebg = (SettingClickView) findViewById(R.id.scv_changebg);
		scv_changebg.setTitle("归属地提示框风格");
		final String[] items = { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
		int which = sp.getInt("which", 0);
		scv_changebg.setDesc(items[which]);

		scv_changebg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 弹出对话框选择颜色
				int dd = sp.getInt("which", 0);

				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("归属地提示风格");
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

	private void initUpdateItem() {
		/*
		 * 第一部份 自动更新
		 */

		// 实始化
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		siv_update.setTitle("自动升级选项");
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean update = sp.getBoolean("update", false);

		if (update) {
			siv_update.setChecked(true);
			siv_update.setDesc("开启");

		} else {
			siv_update.setChecked(false);
			siv_update.setDesc("关闭");

		}

		siv_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();

				if (siv_update.isChecked()) {
					siv_update.setChecked(false);
					siv_update.setDesc("关闭");
					editor.putBoolean("update", false);

				} else {
					siv_update.setChecked(true);
					siv_update.setDesc("开启");
					editor.putBoolean("update", true);

				}
				editor.commit();

			}
		});
	}

	@Override
	protected void onResume() {
		// 防止服务被关掉
		// 在再进入时再做判断
		super.onResume();

		showAddress = new Intent(this, AddressService.class);
		boolean isServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.neo.mobilesafe.service.AddressService");
		if (isServiceRunning) {
			// 监听来电的服务是开启的
			siv_show_address.setChecked(true);
		} else {
			siv_show_address.setChecked(false);
		}

	}

}
