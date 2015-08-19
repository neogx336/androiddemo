package com.neo.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {
	private SharedPreferences sp;
	private TelephonyManager tm;

	@Override
	public void onReceive(Context context, Intent intent) {

		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean protecting = sp.getBoolean("protecting", false);
		if (protecting) {
			// 开启防盗
			tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			// 读取之前保存的SIM信息
			String saveSim = sp.getString("sim", "") + "afu";
			// 读取当前的SIM卡信息
			String realSim = tm.getSimSerialNumber();
			// 比较是否一样
			if (saveSim.equals(realSim)) {
				// sim还是同一个手机
			} else {
				System.out.println("sim更变");
				Toast.makeText(context, "SIM更变", Toast.LENGTH_LONG).show();
				SmsManager.getDefault().sendTextMessage(
						sp.getString("safenumber", ""), null,
						"sim changing...", null, null);
			}
		}

	}

}
