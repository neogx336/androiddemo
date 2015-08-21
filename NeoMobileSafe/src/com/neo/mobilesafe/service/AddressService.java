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
 * ������Ӧ����ɵĹ�������
 * 1.������ʱʹ���Զ���TOAST��չ�ֹ����صĵط������ҵ�ʱȡ����ʾ
 * 2.���һ�����������÷���Ŀ���״̬
 * 
 * ���õ���֪ʶ
 * 1.windownmanager 
 * 2.�Զ���TOAST  �ο�ϵͳtoast�ı�дģʽ
 * 
 */
import android.view.WindowManager;
import android.widget.TextView;

public class AddressService extends Service {

	/**
	 * ���������
	 * 
	 */
	private WindowManager wm;
	private View view;

	/**
	 * �绰����
	 * 
	 */
	private TelephonyManager tm;
	private MyListenerPhone listenerPhone;
	private OutCallReceiver receiver;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// ����ҵ��
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listenerPhone = new MyListenerPhone();
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);

		// �ô���ȥע��㲥������
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);

		// ʵ��������
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
			case TelephonyManager.CALL_STATE_RINGING:// ������������
				String address = NumberAddressQueryUtils
						.queryNumber(incomingNumber);
				myToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:// �绰�Ŀ���״̬���ҵ绰������ܾ�
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
			//����������õ��Ĳ���ȥ�ĵ绰����
			String phoneString = getResultData();
			//�����ݿ�
			String address = NumberAddressQueryUtils.queryNumber(phoneString);
			
			myToast(address);
		}
	}

	public void myToast(String address) {
		view=View.inflate(this, R.layout.address_show, null);
		TextView textView=(TextView) view.findViewById(R.id.tv_address);
		
		// "��͸��","������","��ʿ��","������","ƻ����"
		int[] ids = { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		SharedPreferences spPreferences=getSharedPreferences("config", MODE_PRIVATE);
		view.setBackgroundResource(ids[spPreferences.getInt("which", 0)]);
		textView.setText(address);//���غ���
		
		
		//����������ã��ο�ϵͳ��TOAST��д��
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
		
		//ȡ����������
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
		listenerPhone=null;
		
		//�ô�����ȡ�㲥������
		unregisterReceiver(receiver);
		receiver = null;
	}

}
