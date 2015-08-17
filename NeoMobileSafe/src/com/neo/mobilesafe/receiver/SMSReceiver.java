package com.neo.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.neo.mobilesafe.R;
import com.neo.mobilesafe.service.GPSService;

public class SMSReceiver extends BroadcastReceiver {
	private static final String TAG = "SMSReceiver";
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {

		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		for (Object b : objs) {
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) b);
			String sender = sms.getOriginatingAddress();
			String safenumber = sp.getString("safenumber", "");
			Log.i(TAG, "=====semder====	" + sender);
			String body = sms.getMessageBody();

			if (sender.contains(safenumber)) {
				if ("#*location*#".equals(body)) {
					Log.i(TAG, "得到手机的GPS");
					Intent intent1 = new Intent(context,
							GPSService.class);
					context.startService(intent1);
					String lastlocation = sp.getString("lastlocation", null);
					if (TextUtils.isEmpty(lastlocation)) {
						SmsManager.getDefault().sendTextMessage(sender, null,
								"geting gpsinfo``````", null, null);

					} else {
						SmsManager.getDefault().sendTextMessage(sender, null,
								lastlocation, null, null);

					}
					abortBroadcast();

				} else if ("#*alarm*#".equals(body)) {
					Log.i(TAG, "播放音乐");
					MediaPlayer player = MediaPlayer
							.create(context, R.raw.ylzs);
					player.setLooping(false);
					player.setVolume(0.5f, 0.5f);
					player.start();
					abortBroadcast();
				} else if ("#*wipedata*#".equals(body)) {
					Log.i(TAG, "远程清理数据");
					abortBroadcast();

				} else if ("#*lockscreen*#".equals(body)) {
					Log.i(TAG, "远程锁屏");
					abortBroadcast();
				}

			}

		}

	}

}
