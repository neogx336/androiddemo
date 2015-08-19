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
			// ��������
			tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			// ��ȡ֮ǰ�����SIM��Ϣ
			String saveSim = sp.getString("sim", "") + "afu";
			// ��ȡ��ǰ��SIM����Ϣ
			String realSim = tm.getSimSerialNumber();
			// �Ƚ��Ƿ�һ��
			if (saveSim.equals(realSim)) {
				// sim����ͬһ���ֻ�
			} else {
				System.out.println("sim����");
				Toast.makeText(context, "SIM����", Toast.LENGTH_LONG).show();
				SmsManager.getDefault().sendTextMessage(
						sp.getString("safenumber", ""), null,
						"sim changing...", null, null);
			}
		}

	}

}
