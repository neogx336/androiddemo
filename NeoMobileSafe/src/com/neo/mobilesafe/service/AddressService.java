package com.neo.mobilesafe.service;

import com.neo.mobilesafe.R;
import com.neo.mobilesafe.R.id;
import com.neo.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
/*
 * 本服务应该完成的功能描述
 * 1.当来电时使用自定义TOAST来展现归属地的地方，当挂电时取消显示
 * 2.抽出一个开关了配置服务的开关状态
 * 
 * 运用到的知识
 * 1.windownmanager 
 * 2.自定义TOAST  参考系统toast的编写模式
 * 
 */
import android.view.WindowManager;
import android.widget.TextView;

public class AddressService extends Service {

	/**
	 * 窗体管理者
	 * 
	 */
	private WindowManager wm;
	private View view;

	/**
	 * 电话服务
	 * 
	 */
	private TelephonyManager tm;
	private MyListenerPhone listenerPhone;
	private OutCallReceiver receiver;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 监听业电
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listenerPhone = new MyListenerPhone();
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);

		// 用代码去注册广播接收者
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);

		// 实例化窗体
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	class MyListenerPhone extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 来电铃声响起
				String address = NumberAddressQueryUtils
						.queryNumber(incomingNumber);
				myToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:// 电话的空闲状态：挂电话，来电拒绝
				if (view != null) {
					wm.removeView(view);
				}
				break;
			default:
				break;
			}
		}

	}

	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//这就是我们拿到的播出去的电话号码
			String phoneString = getResultData();
			//查数据库
			String address = NumberAddressQueryUtils.queryNumber(phoneString);
			
			myToast(address);
		}
	}

	public void myToast(String address) {
		view=View.inflate(this, R.layout.address_show, null);
		TextView textView=(TextView) view.findViewById(R.id.tv_address);
		
		// "半透明","活力橙","卫士蓝","金属灰","苹果绿"
		int[] ids = { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		SharedPreferences spPreferences=getSharedPreferences("config", MODE_PRIVATE);
		view.setBackgroundResource(ids[spPreferences.getInt("which", 0)]);
		textView.setText(address);//加载号码
		
		
		//窗体参数设置，参考系统的TOAST的写法
		WindowManager.LayoutParams params=new WindowManager.LayoutParams();
		params.width=WindowManager.LayoutParams.WRAP_CONTENT;
		params.height=WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags =WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
									|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
									|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		
		params.format=PixelFormat.TRANSLUCENT;
		params.type=WindowManager.LayoutParams.TYPE_TOAST;
		wm.addView(view, params);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		//取消监听来电
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
		listenerPhone=null;
		
		//用代码消取广播接收者
		unregisterReceiver(receiver);
		receiver = null;
	}

}
